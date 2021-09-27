package hu.xannosz.tarokk.client.tui;

import com.googlecode.lanterna.TerminalPosition;
import com.googlecode.lanterna.TerminalSize;
import com.googlecode.lanterna.gui2.*;
import com.googlecode.lanterna.input.KeyStroke;
import com.googlecode.lanterna.input.KeyType;
import com.googlecode.lanterna.screen.Screen;
import com.tisza.tarock.proto.MainProto;
import hu.xannosz.tarokk.client.network.Messages;
import hu.xannosz.tarokk.client.network.ProtoConnection;
import hu.xannosz.tarokk.client.network.ServerLiveData;
import hu.xannosz.tarokk.client.tui.frame.Frame;
import hu.xannosz.tarokk.client.tui.frame.LobbyFrame;
import hu.xannosz.tarokk.client.util.InternalData;
import hu.xannosz.tarokk.client.util.ThemeHandler;
import hu.xannosz.tarokk.client.util.Util;
import hu.xannosz.tarokk.client.util.translator.Translator;
import lombok.Getter;
import lombok.Setter;

import java.util.Collections;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;

import static hu.xannosz.tarokk.client.util.Util.addKey;

public class TuiClient implements WindowListener {

    private Screen screen;
    @Getter
    private ProtoConnection connection;
    @Getter
    private final ServerLiveData serverLiveData = new ServerLiveData();
    private final BasicWindow window = new BasicWindow();

    @Setter
    private Frame frame = new LobbyFrame(this);

    public TuiClient() {
        serverLiveData.addCallOnUpdate(this::update);
        try {
            // start screen
            screen = Util.createScreen();
            screen.startScreen();

            // init window
            window.addWindowListener(this);
            window.setHints(Collections.singletonList(Window.Hint.FULL_SCREEN));

            // start proto
            connection = Util.createProtoConnection(serverLiveData);
            connection.sendMessage(Messages.fbLogin(InternalData.INSTANCE.getFaceBookToken()));

            // init gui
            MultiWindowTextGUI gui = new MultiWindowTextGUI(screen);
            gui.setTheme(ThemeHandler.getTheme());
            gui.addWindowAndWait(window);
        } catch (Exception ex) {
            Util.Log.logError("Error client creation: " + ex);
        }
    }

    public void update() {
        frame.update();
        redraw();
    }

    public void redraw() {
        Panel mainPanel = new Panel();
        mainPanel.addComponent(createHeadPanel());
        mainPanel.addComponent(frame.getPanel());
        mainPanel.addComponent(createFooterPanel());
        window.setComponent(mainPanel);
    }

    public TerminalSize getSize() {
        return window.getSize();
    }

    private Component createHeadPanel() {
        Panel head = new Panel();
        if (serverLiveData.getLoginResult() != null) {
            int userId = serverLiveData.getLoginResult().getUserId();
            head.addComponent(new Label(Translator.INST.userId));
            head.addComponent(new Label("" + userId).setTheme(ThemeHandler.getHighLightedTheme()));
            head.setLayoutManager(new GridLayout(2));
            if (serverLiveData.getUsers() != null) {
                if (userId != 0) {
                    MainProto.User user = serverLiveData.getUsers().get(userId);
                    head.addComponent(new Label(Translator.INST.userName));
                    head.addComponent(new Label(user == null ? "" : user.getName()).setTheme(ThemeHandler.getHighLightedTheme()));
                    head.setLayoutManager(new GridLayout(4));
                } else {
                    head.addComponent(new Label(Translator.INST.loginNotSucceed).setTheme(ThemeHandler.getErrorTheme()));
                    head.setLayoutManager(new GridLayout(3));
                }
            }
        } else {
            head.addComponent(new Label(Translator.INST.notLoggedIn));
            head.setLayoutManager(new GridLayout(1));
        }
        return head;
    }

    private Component createFooterPanel() {
        Panel footerPanel = new Panel();
        footerPanel.setTheme(ThemeHandler.getFooterPanelTheme());
        Map<String, String> footer = frame.getFooter();
        footerPanel.setLayoutManager(new GridLayout(3 * (1 + footer.size())));
        addKey(footerPanel, "Esc", Translator.INST.quit);
        for (Map.Entry<String, String> entry : footer.entrySet()) {
            addKey(footerPanel, entry.getKey(), entry.getValue());
        }
        footerPanel.setPreferredSize(new TerminalSize(getSize().getColumns(), 1));
        return footerPanel;
    }

    @Override
    public void onInput(Window basePane, KeyStroke keyStroke, AtomicBoolean deliverEvent) {
        Util.Log.logKey("Key: " + keyStroke);
        try {
            if (keyStroke.getKeyType() == KeyType.Escape) {
                try {
                    screen.close();
                } catch (Exception | Error e) {
                    //Not expected
                }
                try {
                    connection.close();
                } catch (Exception | Error e) {
                    //Not expected
                }
                System.exit(0);
            } else {
                frame.handleKeyStroke(keyStroke);
            }
        } catch (Exception ex) {
            Util.Log.logError("Error during execution: ");
            Util.Log.logError(ex);
        }
    }

    @Override
    public void onUnhandledInput(Window basePane, KeyStroke keyStroke, AtomicBoolean hasBeenHandled) {
        // Not used function
    }

    @Override
    public void onResized(Window window, TerminalSize oldSize, TerminalSize newSize) {
        update();
    }

    @Override
    public void onMoved(Window window, TerminalPosition oldPosition, TerminalPosition newPosition) {
        // Not used function
    }
}
