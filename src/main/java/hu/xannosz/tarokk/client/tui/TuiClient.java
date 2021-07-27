package hu.xannosz.tarokk.client.tui;

import com.googlecode.lanterna.TerminalPosition;
import com.googlecode.lanterna.TerminalSize;
import com.googlecode.lanterna.graphics.SimpleTheme;
import com.googlecode.lanterna.gui2.*;
import com.googlecode.lanterna.input.KeyStroke;
import com.googlecode.lanterna.input.KeyType;
import com.googlecode.lanterna.screen.Screen;
import com.tisza.tarock.proto.MainProto;
import hu.xannosz.tarokk.client.android.network.ProtoConnection;
import hu.xannosz.tarokk.client.game.DoubleRoundType;
import hu.xannosz.tarokk.client.game.GameType;
import hu.xannosz.tarokk.client.network.ServerLiveData;
import hu.xannosz.tarokk.client.util.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;

public class TuiClient implements WindowListener {

    private Screen screen;
    private ProtoConnection connection;
    @Getter
    private final TerminalSettings terminalSettings;
    @Getter
    private final ServerLiveData serverLiveData = new ServerLiveData(this);
    private final BasicWindow window = new BasicWindow();
    private final KeyMapDictionary dictionary = new KeyMapDictionary();
    private final Map<String, Object> subSessionMap = new HashMap<>();

    @Setter
    private Frame frame = new LobbyFrame(this);
    @Setter
    private Component footer = new Label("");

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
            gui.setTheme(new SimpleTheme(terminalSettings.getForeGround(), terminalSettings.getBackGround()));
            gui.addWindowAndWait(window);
        } catch (Exception ex) {
            Util.error("Error client creation: " + ex);
        }
    }

    private void createSubSession(SubSessionId subSessionId) {
        switch (subSessionId) {
            case CREATE_GAME_GAME_TYPE:
                System.out.println("###" + "before clear " + subSessionMap);
                subSessionMap.clear();
                System.out.println("###" + "before dictionary clear");
                dictionary.clear();
                System.out.println("###" + "after clear");
                int gameTypeNum = 0;
                for (GameType gameType : GameType.values()) {
                    dictionary.addFunction("" + gameTypeNum++,
                            "Set game type to " + gameType,
                            () -> {
                                subSessionMap.put("gameType", gameType);
                                createSubSession(SubSessionId.CREATE_GAME_DOUBLE_ROUND);
                            });
                }
                dictionary.addFunction("/", "Cancel",
                        () -> connection.sendMessage(MessageTranslator.listLobby()));
                System.out.println("###" + "after dictionary");
                break;
            case CREATE_GAME_DOUBLE_ROUND:
                dictionary.clear();
                int doubleRoundTypeNum = 0;
                for (DoubleRoundType doubleRoundType : DoubleRoundType.values()) {
                    MainProto.Message message = MessageTranslator.newGame(doubleRoundType, (GameType) subSessionMap.get("gameType"));
                    dictionary.addFunction("" + doubleRoundTypeNum++,
                            "Create game with type: " + subSessionMap.get("gameType") +
                                    " and double round type: " + doubleRoundType,
                            () -> connection.sendMessage(message));
                }
                dictionary.addFunction("/", "Cancel",
                        () -> connection.sendMessage(MessageTranslator.listLobby()));
                subSessionMap.clear();
                break;
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

    public TerminalSize getSize(){
       return window.getSize();
    }

    private Component createHeadPanel() {
        Panel head = new Panel();
        if (serverLiveData.getLoginResult() != null) {
            int userId = serverLiveData.getLoginResult().getUserId();
            head.addComponent(new Label("User Id:"));
            head.addComponent(new Label("" + userId).setTheme(new SimpleTheme(terminalSettings.getHighLightedForeGround(), terminalSettings.getBackGround())));
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
        footerPanel.setLayoutManager(new GridLayout(4));
        footerPanel.addComponent(new Label("["));
        footerPanel.addComponent(new Label("Esc").setTheme(new SimpleTheme(terminalSettings.getKeyColor(), terminalSettings.getBackGround())));
        footerPanel.addComponent(new Label("]: Quit"));
        footerPanel.addComponent(footer);
        return footerPanel;
    }

    @Override
    public void onInput(Window basePane, KeyStroke keyStroke, AtomicBoolean deliverEvent) {
        Util.debug("Pressed: " + keyStroke);
        try {
            if (keyStroke.getKeyType() == KeyType.Escape) {
                connection.close();
                screen.stopScreen();
                window.close();
            }
            frame.handleKeyStroke(keyStroke);
        } catch (Exception ex) {
            Util.error("Error during execution: " + ex);
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

    private enum SubSessionId {
        CREATE_GAME_GAME_TYPE,
        CREATE_GAME_DOUBLE_ROUND
    }
}
