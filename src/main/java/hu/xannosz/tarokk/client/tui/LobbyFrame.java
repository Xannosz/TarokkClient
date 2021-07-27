package hu.xannosz.tarokk.client.tui;

import com.googlecode.lanterna.Symbols;
import com.googlecode.lanterna.TerminalSize;
import com.googlecode.lanterna.gui2.*;
import com.googlecode.lanterna.input.KeyStroke;
import com.tisza.tarock.proto.MainProto;
import hu.xannosz.tarokk.client.game.GameType;
import hu.xannosz.tarokk.client.util.ThemeHandler;
import hu.xannosz.tarokk.client.util.Util;

import java.util.Map;

public class LobbyFrame extends Frame {

    private Panel frame = new Panel();
    private int gamePage = 0;
    private int userPage = 0;
    private int selected = 0;

    private int maxNameSize;
    private TerminalSize gameListSize;
    private TerminalSize gameNamePanelSize;
    private TerminalSize gameDataPanelSize;
    private TerminalSize nameListSize;
    private TerminalSize nameNamePanelSize;
    private TerminalSize nameOnlinePanelSize;
    private TerminalSize nameFriendPanelSize;

    public LobbyFrame(TuiClient tuiClient) {
        super(tuiClient);
    }

    @Override
    public Component getPanel() {
        return frame;
    }

    @Override
    public void handleKeyStroke(KeyStroke keyStroke) {

    }

    @Override
    public void update() {
        frame = new Panel();
        frame.setLayoutManager(new GridLayout(2));
        TerminalSize size = tuiClient.getSize();

        if (Util.anyNull(tuiClient.getServerLiveData().getGameSessions(),
                tuiClient.getServerLiveData().getUsers(),
                tuiClient.getServerLiveData().getLoginResult())) {
            return;
        }

        calculatePanelSizes();

        Panel gamePanel = new Panel();
        gamePanel.setPreferredSize(new TerminalSize(size.getColumns() / 2, size.getRows() - 2));
        for (MainProto.GameSession game : tuiClient.getServerLiveData().getGameSessions()) {
            gamePanel.addComponent(createGameSessionPanel(game).withBorder(Borders.singleLine()));
        }

        Panel userPanel = new Panel();
        userPanel.setPreferredSize(new TerminalSize(size.getColumns() / 2, size.getRows() - 2));
        for (Map.Entry<Integer, MainProto.User> user : tuiClient.getServerLiveData().getUsers().entrySet()) {
            if (!user.getValue().getBot() && user.getValue().getId() != tuiClient.getServerLiveData().getLoginResult().getUserId()) {
                userPanel.addComponent(createUserPanel(user));
            }
        }

        frame.addComponent(gamePanel.setPreferredSize(gameListSize).withBorder(Borders.singleLine()));
        frame.addComponent(userPanel.setPreferredSize(nameListSize).withBorder(Borders.singleLine()));
    }

    private void calculatePanelSizes() { //TODO
        getMaxNameSize();
        TerminalSize size = tuiClient.getSize();
        int gamePanelNameWidth = maxNameSize + 3 + 2; //name, online indicator and ghost border
        int gameDataPanelWidth = 2 * gamePanelNameWidth + 4; //two name card side by side and ghost borders
        int gameListMinWidth = gameDataPanelWidth + 2; //cards and border
        int namePanelWidth = maxNameSize + 2; //name and ghost border
        int boardWidth = size.getColumns() - 2; //terminal minus border
        int onlinePanelWidth = 7 + 2; //Word and ghost border
        int friendPanelWidth = 10 + 2; //Word and ghost border
        int halfBoardWidth = boardWidth / 2 - 2; //minus border
        int listPanelHeight = size.getRows() - 2 - 2 - 2; //minus board border, two main line and list border

        gameDataPanelSize = new TerminalSize(gameDataPanelWidth, 6);
        gameNamePanelSize = new TerminalSize(maxNameSize + 3, 1);
        nameNamePanelSize = new TerminalSize(maxNameSize, 1);

        if (namePanelWidth + onlinePanelWidth + friendPanelWidth >= halfBoardWidth && gameListMinWidth >= halfBoardWidth) {
            gameListSize = new TerminalSize(halfBoardWidth, listPanelHeight);
            nameListSize = new TerminalSize(halfBoardWidth, listPanelHeight);
            nameOnlinePanelSize = new TerminalSize(7, 1);
            nameFriendPanelSize = new TerminalSize(10, 1);
            return;
        }
        if (namePanelWidth + onlinePanelWidth + friendPanelWidth + gameListMinWidth >= 2*halfBoardWidth) {
            gameListSize = new TerminalSize(2*halfBoardWidth-(namePanelWidth + onlinePanelWidth + friendPanelWidth), listPanelHeight);
            nameListSize = new TerminalSize(namePanelWidth + onlinePanelWidth + friendPanelWidth, listPanelHeight);
            nameOnlinePanelSize = new TerminalSize(7, 1);
            nameFriendPanelSize = new TerminalSize(10, 1);
            return;
        }
        if (namePanelWidth + onlinePanelWidth  + gameListMinWidth >= 2*halfBoardWidth) {
            gameListSize = new TerminalSize(2*halfBoardWidth-(namePanelWidth + onlinePanelWidth), listPanelHeight);
            nameListSize = new TerminalSize(namePanelWidth + onlinePanelWidth, listPanelHeight);
            nameOnlinePanelSize = new TerminalSize(7, 1);
            nameFriendPanelSize = new TerminalSize(0, 1);
            return;
        }
        if (namePanelWidth   + gameListMinWidth >= 2*halfBoardWidth) {
            gameListSize = new TerminalSize(2*halfBoardWidth-namePanelWidth , listPanelHeight);
            nameListSize = new TerminalSize(namePanelWidth , listPanelHeight);
            nameOnlinePanelSize = new TerminalSize(0, 1);
            nameFriendPanelSize = new TerminalSize(0, 1);
            return;
        }

        gameListSize = new TerminalSize(boardWidth - 2, listPanelHeight);
        nameListSize = new TerminalSize(0, listPanelHeight);
        nameOnlinePanelSize = new TerminalSize(0, 1);
        nameFriendPanelSize = new TerminalSize(0, 1);
    }

    private void getMaxNameSize() {
        int maximum = 0;
        for (Map.Entry<Integer, MainProto.User> user : tuiClient.getServerLiveData().getUsers().entrySet()) {
            if (maximum < user.getValue().getName().length()) {
                maximum = user.getValue().getName().length();
            }
        }
        for (GameType gameType : GameType.values()) {
            if (maximum < gameType.getName().length()) {
                maximum = gameType.getName().length();
            }
        }
        maxNameSize = maximum;
    }

    private Panel createUserPanel(Map.Entry<Integer, MainProto.User> user) {
        Panel panel = new Panel();
        panel.addComponent(new Label(user.getValue().getName()).setPreferredSize(nameNamePanelSize));
        if (user.getValue().getOnline()) {
            panel.addComponent(new Label("Online ").setPreferredSize(nameOnlinePanelSize).setTheme(ThemeHandler.getHighLightedTheme(tuiClient.getTerminalSettings())));
        } else {
            panel.addComponent(new Label("Offline").setPreferredSize(nameOnlinePanelSize).setTheme(ThemeHandler.getSubLightedTheme(tuiClient.getTerminalSettings())));
        }
        if (user.getValue().getIsFriend()) {
            panel.addComponent(new Label("  Friend  ").setPreferredSize(nameFriendPanelSize).setTheme(ThemeHandler.getHighLightedTheme(tuiClient.getTerminalSettings())));
        } else {
            panel.addComponent(new Label("Not Friend").setPreferredSize(nameFriendPanelSize).setTheme(ThemeHandler.getSubLightedTheme(tuiClient.getTerminalSettings())));
        }
        panel.setLayoutManager(new GridLayout(3));
        return panel;
    }

    private Panel createGameSessionPanel(MainProto.GameSession game) {
        Panel panel = new Panel();
        panel.setLayoutManager(new GridLayout(2));
        panel.setPreferredSize(gameDataPanelSize);

        panel.addComponent(new Label("Type:").setPreferredSize(gameNamePanelSize));
        panel.addComponent(new Label(game.getType()).setPreferredSize(gameNamePanelSize));

        for (int i = 0; i < game.getUserIdCount(); i++) {
            panel.addComponent(createUserPanel(game.getUserId(i)).setPreferredSize(gameNamePanelSize));
        }
        for (int i = game.getUserIdCount(); i < 4; i++) {
            panel.addComponent(new Label("").setPreferredSize(gameNamePanelSize));
        }

        panel.addComponent(new Label("Status:").setPreferredSize(gameNamePanelSize));
        panel.addComponent(new Label("" + game.getState()).setPreferredSize(gameNamePanelSize));

        return panel;
    }

    private Component createUserPanel(int userId) {
        if (userId != 0) {
            MainProto.User user = tuiClient.getServerLiveData().getUsers().get(userId);
            if (user.getBot()) {
                return new Label(" Bot").setTheme(ThemeHandler.getSubLightedThemeMainPanel(tuiClient.getTerminalSettings()));
            } else {
                Panel panel = new Panel(new GridLayout(2));
                panel.addComponent(new Label(user.getName()));
                if (user.getOnline()) {
                    panel.addComponent(new Label("" + Symbols.TRIANGLE_UP_POINTING_BLACK).setTheme(ThemeHandler.getOnlineColorThemeMainPanel(tuiClient.getTerminalSettings())));
                } else {
                    panel.addComponent(new Label("" + Symbols.TRIANGLE_DOWN_POINTING_BLACK).setTheme(ThemeHandler.getSubLightedThemeMainPanel(tuiClient.getTerminalSettings())));
                }
                return panel;
            }
        } else {
            return new Label("");
        }
    }
}
