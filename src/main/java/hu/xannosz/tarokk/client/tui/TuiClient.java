package hu.xannosz.tarokk.client.tui;

import com.googlecode.lanterna.TerminalPosition;
import com.googlecode.lanterna.TerminalSize;
import com.googlecode.lanterna.gui2.*;
import com.googlecode.lanterna.input.KeyStroke;
import com.googlecode.lanterna.input.KeyType;
import com.googlecode.lanterna.screen.Screen;
import com.tisza.tarock.proto.MainProto;
import hu.xannosz.tarokk.client.android.network.ProtoConnection;
import hu.xannosz.tarokk.client.network.ServerLiveData;
import hu.xannosz.tarokk.client.tui.frame.Frame;
import hu.xannosz.tarokk.client.tui.frame.LobbyFrame;
import hu.xannosz.tarokk.client.util.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Collections;
import java.util.concurrent.atomic.AtomicBoolean;

public class TuiClient implements WindowListener {

    private Screen screen;
    @Getter
    private ProtoConnection connection;
    @Getter
    private final TerminalSettings terminalSettings;
    @Getter
    private final ServerLiveData serverLiveData = new ServerLiveData(this);
    private final BasicWindow window = new BasicWindow();

    @Setter
    private Frame frame = new LobbyFrame(this);

    public TuiClient(TerminalSettings terminalSettings) {
        this.terminalSettings = terminalSettings;
        try {
            // start screen
            screen = Util.createScreen();
            screen.startScreen();

            // init window
            window.addWindowListener(this);
            window.setHints(Collections.singletonList(Window.Hint.FULL_SCREEN));

            // start proto
            connection = Util.createProtoConnection(serverLiveData);
            connection.sendMessage(MessageTranslator.fbLogin(Constants.FB_TOKEN));

            // init gui
            MultiWindowTextGUI gui = new MultiWindowTextGUI(screen);
            gui.setTheme(ThemeHandler.getTheme(terminalSettings));
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
            head.addComponent(new Label("User Id:"));
            head.addComponent(new Label("" + userId).setTheme(ThemeHandler.getHighLightedTheme(terminalSettings)));
            if (serverLiveData.getUsers() != null) {
                if (userId != 0) {
                    MainProto.User user = serverLiveData.getUsers().get(userId);
                    head.addComponent(new Label("User Name:"));
                    head.addComponent(new Label(user == null ? "" : user.getName()).setTheme(ThemeHandler.getHighLightedTheme(terminalSettings)));
                    head.setLayoutManager(new GridLayout(4));
                } else {
                    head.addComponent(new Label("Login not succeed.").setTheme(ThemeHandler.getErrorTheme(terminalSettings)));
                    head.setLayoutManager(new GridLayout(3));
                }
            }
        } else {
            head.addComponent(new Label("Not logged in."));
        }
        return head;
    }

    private Component createFooterPanel() {
        Panel footerPanel = new Panel();
        footerPanel.setTheme(ThemeHandler.getFooterPanelTheme(terminalSettings));
        footerPanel.setLayoutManager(new GridLayout(4));
        footerPanel.addComponent(new Label("["));
        footerPanel.addComponent(new Label("Esc").setTheme(ThemeHandler.getKeyThemeFooterPanel(terminalSettings)));
        footerPanel.addComponent(new Label("]: Quit"));
        footerPanel.addComponent(frame.getFooter());
        footerPanel.setPreferredSize(new TerminalSize(getSize().getColumns(),1));
        return footerPanel;
    }

    @Override
    public void onInput(Window basePane, KeyStroke keyStroke, AtomicBoolean deliverEvent) {
        Util.Log.logKey("Key: " + keyStroke);
        try {
            if (keyStroke.getKeyType() == KeyType.Escape) {
                connection.close();
                screen.stopScreen();
                window.close();
            }
            frame.handleKeyStroke(keyStroke);
        } catch (Exception ex) {
            Util.Log.logError("Error during execution: ");
            ex.printStackTrace(); //TODO print to file
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
