package hu.xannosz.tarokk.client.tui.frame;

import com.googlecode.lanterna.TerminalSize;
import com.googlecode.lanterna.gui2.*;
import com.googlecode.lanterna.input.KeyStroke;
import com.googlecode.lanterna.input.KeyType;
import com.tisza.tarock.proto.MainProto;
import hu.xannosz.tarokk.client.game.GameType;
import hu.xannosz.tarokk.client.tui.TuiClient;
import hu.xannosz.tarokk.client.util.MessageTranslator;
import hu.xannosz.tarokk.client.util.ThemeHandler;
import hu.xannosz.tarokk.client.util.Util;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class LobbyFrame extends Frame {

    private int gamePage = 0;
    private int namePage = 0;
    private boolean namePanelActivated = false;
    private List< MainProto.GameSession> gameSessions;

    private int maxNameSize;
    private TerminalSize gameListSize;
    private TerminalSize gameNamePanelSize;
    private TerminalSize gameDataPanelSize;
    private TerminalSize nameListSize;
    private TerminalSize nameNamePanelSize;
    private TerminalSize nameOnlinePanelSize;
    private TerminalSize nameFriendPanelSize;
    private boolean drawNamePanel;
    private boolean drawNameOnlinePanel;
    private boolean drawNameFriendPanel;
    private int nameListHeight;
    private int gameListHeight;

    public LobbyFrame(TuiClient tuiClient) {
        super(tuiClient);
    }

    @Override
    public void handleKeyStroke(KeyStroke keyStroke) {
        if (keyStroke.getKeyType().equals(KeyType.ArrowLeft) && namePanelActivated) {
            namePanelActivated = false;
        }
        if (keyStroke.getKeyType().equals(KeyType.ArrowRight) && !namePanelActivated && drawNamePanel) {
            namePanelActivated = true;
        }

        if (keyStroke.getKeyType().equals(KeyType.ArrowUp)) {
            if (namePanelActivated) {
                namePage--;
            } else {
                gamePage--;
            }
        }
        if (keyStroke.getKeyType().equals(KeyType.ArrowDown)) {
            if (namePanelActivated) {
                namePage++;
            } else {
                gamePage++;
            }
        }
        resetPagers();

        if (keyStroke.getKeyType().equals(KeyType.Enter) && !namePanelActivated) {
            tuiClient.getConnection().sendMessage(MessageTranslator.joinToGame(gameSessions.get(gamePage).getId()));
            tuiClient.setFrame(new GameFrame(tuiClient, gameSessions.get(gamePage).getId()));
        }
        if (keyStroke.getKeyType().equals(KeyType.Character) && keyStroke.getCharacter().equals('+') && !namePanelActivated) {
            tuiClient.setFrame(new NewGameFrame(tuiClient));
        }
        if (keyStroke.getKeyType().equals(KeyType.Character) && keyStroke.getCharacter().equals('-') && !namePanelActivated) {
            tuiClient.getConnection().sendMessage(MessageTranslator.deleteToGame(gameSessions.get(gamePage).getId()));
        }

        update();
        tuiClient.redraw();
    }

    @Override
    public void update() {
        frame = new Panel();
        frame.setLayoutManager(new GridLayout(2));

        if (Util.anyNull(tuiClient.getServerLiveData().getGameSessions(),
                tuiClient.getServerLiveData().getUsers(),
                tuiClient.getServerLiveData().getLoginResult())) {
            return;
        }

        gameSessions = new ArrayList<>(tuiClient.getServerLiveData().getGameSessions());
        resetPagers();
        updateFooter();
        calculatePanelSizes();

        Panel gamePanel = new Panel();
        for (int i = gamePage; i < tuiClient.getServerLiveData().getGameSessions().size() && i < gamePage + gameListHeight; i++) {
            if (i == gamePage && !namePanelActivated) {
                gamePanel.addComponent(createGameSessionPanel(gameSessions.get(i)).withBorder(Borders.doubleLine()));
            } else {
                gamePanel.addComponent(createGameSessionPanel(gameSessions.get(i)).withBorder(Borders.singleLine()));
            }
        }
        frame.addComponent(gamePanel.setPreferredSize(gameListSize).withBorder(Borders.singleLine()));

        if (drawNamePanel) {
            Panel userPanel = new Panel();
            List<Map.Entry<Integer, MainProto.User>> userList = new ArrayList<>(tuiClient.getServerLiveData().getUsers().entrySet());
            for (int i = namePage; i < userList.size() && i < namePage + nameListHeight; i++) {
                if (!userList.get(i).getValue().getBot() && userList.get(i).getValue().getId() != tuiClient.getServerLiveData().getLoginResult().getUserId()) {
                    userPanel.addComponent(createUserPanel(userList.get(i)));
                }
            }
            if (namePanelActivated) {
                frame.addComponent(userPanel.setPreferredSize(nameListSize).withBorder(Borders.doubleLine()));
            } else {
                frame.addComponent(userPanel.setPreferredSize(nameListSize).withBorder(Borders.singleLine()));
            }
        }
    }

    private void calculatePanelSizes() {
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
        int listPanelHeight = size.getRows() - 2 - 2; //minus board border and two main line
        nameListHeight = listPanelHeight;
        gameListHeight = listPanelHeight / (6 + 2); //data panel height and border

        gameDataPanelSize = new TerminalSize(gameDataPanelWidth, 6);
        gameNamePanelSize = new TerminalSize(maxNameSize + 3, 1);
        nameNamePanelSize = new TerminalSize(maxNameSize, 1);
        nameOnlinePanelSize = new TerminalSize(7, 1);
        nameFriendPanelSize = new TerminalSize(10, 1);

        if (namePanelWidth + onlinePanelWidth + friendPanelWidth <= halfBoardWidth && gameListMinWidth <= halfBoardWidth) {
            gameListSize = new TerminalSize(halfBoardWidth, listPanelHeight);
            nameListSize = new TerminalSize(halfBoardWidth, listPanelHeight);
            drawNamePanel = true;
            drawNameOnlinePanel = true;
            drawNameFriendPanel = true;
            return;
        }
        if (namePanelWidth + onlinePanelWidth + friendPanelWidth + gameListMinWidth <= 2 * halfBoardWidth) {
            gameListSize = new TerminalSize(2 * halfBoardWidth - (namePanelWidth + onlinePanelWidth + friendPanelWidth), listPanelHeight);
            nameListSize = new TerminalSize(namePanelWidth + onlinePanelWidth + friendPanelWidth, listPanelHeight);
            drawNamePanel = true;
            drawNameOnlinePanel = true;
            drawNameFriendPanel = true;
            return;
        }
        if (namePanelWidth + onlinePanelWidth + gameListMinWidth <= 2 * halfBoardWidth) {
            gameListSize = new TerminalSize(2 * halfBoardWidth - (namePanelWidth + onlinePanelWidth), listPanelHeight);
            nameListSize = new TerminalSize(namePanelWidth + onlinePanelWidth, listPanelHeight);
            drawNamePanel = true;
            drawNameOnlinePanel = true;
            drawNameFriendPanel = false;
            return;
        }
        if (namePanelWidth + gameListMinWidth <= 2 * halfBoardWidth) {
            gameListSize = new TerminalSize(2 * halfBoardWidth - namePanelWidth, listPanelHeight);
            nameListSize = new TerminalSize(namePanelWidth, listPanelHeight);
            drawNamePanel = true;
            namePanelActivated = false;
            drawNameOnlinePanel = false;
            drawNameFriendPanel = false;
            return;
        }

        gameListSize = new TerminalSize(boardWidth - 2, listPanelHeight);
        nameListSize = new TerminalSize(1, listPanelHeight);
        drawNamePanel = false;
        drawNameOnlinePanel = false;
        drawNameFriendPanel = false;
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
        if (drawNameOnlinePanel) {
            if (user.getValue().getOnline()) {
                panel.addComponent(new Label("Online ").setPreferredSize(nameOnlinePanelSize).setTheme(ThemeHandler.getHighLightedThemeMainPanel(tuiClient.getTerminalSettings())));
            } else {
                panel.addComponent(new Label("Offline").setPreferredSize(nameOnlinePanelSize).setTheme(ThemeHandler.getSubLightedThemeMainPanel(tuiClient.getTerminalSettings())));
            }
        }
        if (drawNameFriendPanel) {
            if (user.getValue().getIsFriend()) {
                panel.addComponent(new Label("  Friend  ").setPreferredSize(nameFriendPanelSize).setTheme(ThemeHandler.getHighLightedThemeMainPanel(tuiClient.getTerminalSettings())));
            } else {
                panel.addComponent(new Label("Not Friend").setPreferredSize(nameFriendPanelSize).setTheme(ThemeHandler.getSubLightedThemeMainPanel(tuiClient.getTerminalSettings())));
            }
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
            panel.addComponent(Util.createUserPanel(game.getUserId(i), tuiClient).setPreferredSize(gameNamePanelSize));
        }
        for (int i = game.getUserIdCount(); i < 4; i++) {
            panel.addComponent(new Label("").setPreferredSize(gameNamePanelSize));
        }

        panel.addComponent(new Label("Status:").setPreferredSize(gameNamePanelSize));
        panel.addComponent(new Label("" + game.getState()).setPreferredSize(gameNamePanelSize));

        return panel;
    }

    private void resetPagers() {
        if (gamePage < 0) {
            gamePage = 0;
        }
        if (namePage > tuiClient.getServerLiveData().getUsers().size() - nameListHeight) {
            namePage = tuiClient.getServerLiveData().getUsers().size() - nameListHeight;
        }
        if (namePage < 0) {
            namePage = 0;
        }
        if (gamePage > tuiClient.getServerLiveData().getGameSessions().size() - 1) {
            gamePage = tuiClient.getServerLiveData().getGameSessions().size() - 1;
        }
    }

    private void updateFooter() {
        footer = new Panel();
        if (Util.anyNull(tuiClient.getServerLiveData().getGameSessions(),
                tuiClient.getServerLiveData().getUsers(),
                tuiClient.getServerLiveData().getLoginResult(),
                gameSessions)) {
            return;
        }

        MainProto.GameSession gameInfo = gameSessions.get(gamePage);
        int userID = tuiClient.getServerLiveData().getLoginResult().getUserId();

        String joinButtonText;
        if (gameInfo.getState() == MainProto.GameSession.State.LOBBY) {
            joinButtonText = "Join to lobby";
        } else if (gameInfo.getUserIdList().contains(userID)) {
            joinButtonText = "Join to game";
        } else {
            joinButtonText = "Join to game as observer";
        }

        boolean deleteButtonVisible;
        if (gameInfo.getState() == MainProto.GameSession.State.LOBBY) {
            deleteButtonVisible = gameInfo.getUserIdCount() > 0 && gameInfo.getUserId(0) == userID;
        } else {
            deleteButtonVisible = gameInfo.getUserIdList().contains(userID);
        }

        footer.setLayoutManager(new GridLayout(deleteButtonVisible ? 12 : 9));

        footer.addComponent(new Label("["));
        footer.addComponent(new Label("Arrows").setTheme(ThemeHandler.getKeyThemeFooterPanel(tuiClient.getTerminalSettings())));
        footer.addComponent(new Label("]: Movement"));

        footer.addComponent(new Label("["));
        footer.addComponent(new Label("Enter").setTheme(ThemeHandler.getKeyThemeFooterPanel(tuiClient.getTerminalSettings())));
        footer.addComponent(new Label("]: " + joinButtonText));

        footer.addComponent(new Label("["));
        footer.addComponent(new Label("+").setTheme(ThemeHandler.getKeyThemeFooterPanel(tuiClient.getTerminalSettings())));
        footer.addComponent(new Label("]: Create Game"));

        if (deleteButtonVisible) {
            footer.addComponent(new Label("["));
            footer.addComponent(new Label("-").setTheme(ThemeHandler.getKeyThemeFooterPanel(tuiClient.getTerminalSettings())));
            footer.addComponent(new Label("]: Delete Game"));
        }
    }
}
