package hu.xannosz.tarokk.client.tui.frame;

import com.googlecode.lanterna.TerminalSize;
import com.googlecode.lanterna.gui2.Borders;
import com.googlecode.lanterna.gui2.GridLayout;
import com.googlecode.lanterna.gui2.Panel;
import com.googlecode.lanterna.input.KeyStroke;
import com.googlecode.lanterna.input.KeyType;
import com.tisza.tarock.proto.MainProto;
import hu.xannosz.microtools.Sleep;
import hu.xannosz.tarokk.client.game.DoubleRoundType;
import hu.xannosz.tarokk.client.game.GameType;
import hu.xannosz.tarokk.client.network.Messages;
import hu.xannosz.tarokk.client.tui.TuiClient;
import hu.xannosz.tarokk.client.tui.panel.DoubleRoundPanel;
import hu.xannosz.tarokk.client.tui.panel.GameTypePanel;
import hu.xannosz.tarokk.client.util.Util;

public class NewGameFrame extends Frame {

    private GameType gameType = GameType.values()[0];
    private DoubleRoundType doubleRoundType = DoubleRoundType.values()[0];
    private boolean doublePanelActivated;

    public NewGameFrame(TuiClient tuiClient) {
        super(tuiClient);
        footer = new Panel();
        footer.setLayoutManager(new GridLayout(9));
        Util.addKey(footer, "Arrows", "Movement", tuiClient);
        Util.addKey(footer, "Enter", "Create game", tuiClient);
        Util.addKey(footer, "/", "Cancel", tuiClient);
    }

    @Override
    public void handleKeyStroke(KeyStroke keyStroke) {
        if (keyStroke.getKeyType().equals(KeyType.ArrowLeft)) {
            doublePanelActivated = false;
        }
        if (keyStroke.getKeyType().equals(KeyType.ArrowRight)) {
            doublePanelActivated = true;
        }

        if (keyStroke.getKeyType().equals(KeyType.ArrowUp)) {
            if (doublePanelActivated) {
                for (int i = 0; i < DoubleRoundType.values().length; i++) {
                    if (DoubleRoundType.values()[i] == this.doubleRoundType) {
                        if (i == 0) {
                            doubleRoundType = DoubleRoundType.values()[DoubleRoundType.values().length - 1];
                        } else {
                            doubleRoundType = DoubleRoundType.values()[i - 1];
                        }
                        break;
                    }
                }
            } else {
                for (int i = 0; i < GameType.values().length; i++) {
                    if (GameType.values()[i] == this.gameType) {
                        if (i == 0) {
                            gameType = GameType.values()[GameType.values().length - 1];
                        } else {
                            gameType = GameType.values()[i - 1];
                        }
                        break;
                    }
                }
            }
        }
        if (keyStroke.getKeyType().equals(KeyType.ArrowDown)) {
            if (doublePanelActivated) {
                for (int i = 0; i < DoubleRoundType.values().length; i++) {
                    if (DoubleRoundType.values()[i].equals(this.doubleRoundType)) {
                        doubleRoundType = DoubleRoundType.values()[(i + 1) % DoubleRoundType.values().length];
                        break;
                    }
                }
            } else {
                for (int i = 0; i < GameType.values().length; i++) {
                    if (GameType.values()[i].equals(this.gameType)) {
                        gameType = GameType.values()[(i + 1) % GameType.values().length];
                        break;
                    }
                }
            }
        }

        if (keyStroke.getKeyType().equals(KeyType.Enter)) {
            tuiClient.getConnection().sendMessage(Messages.newGame(doubleRoundType, gameType));
            Sleep.sleepMillis(200);
            int gameId = 0;
            for (MainProto.GameSession gameData : tuiClient.getServerLiveData().getGameSessions()) {
                if (gameData.getState() == MainProto.GameSession.State.LOBBY &&
                        gameData.getUserIdList().contains(tuiClient.getServerLiveData().getLoginResult().getUserId())) {
                    gameId = gameData.getId();
                }
            }
            tuiClient.getConnection().sendMessage(Messages.joinToGame(gameId));
            tuiClient.setFrame(new GameFrame(tuiClient, gameId));
        }
        if (keyStroke.getKeyType().equals(KeyType.Character) && keyStroke.getCharacter().equals('/')) {
            tuiClient.setFrame(new LobbyFrame(tuiClient));
        }

        update();
        tuiClient.redraw();
    }

    @Override
    public void update() {
        frame = new Panel();
        frame.setLayoutManager(new GridLayout(2));
        TerminalSize size = tuiClient.getSize();

        Panel gameTypePanel = new GameTypePanel(gameType, tuiClient);
        gameTypePanel.setPreferredSize(new TerminalSize(size.getColumns() / 2, size.getRows() - 2));

        Panel gameDoubleRoundPanel = new DoubleRoundPanel(doubleRoundType, tuiClient);
        gameDoubleRoundPanel.setPreferredSize(new TerminalSize(size.getColumns() / 2, size.getRows() - 2));

        if (doublePanelActivated) {
            frame.addComponent(gameTypePanel.withBorder(Borders.singleLine(" Game Type ")));
            frame.addComponent(gameDoubleRoundPanel.withBorder(Borders.doubleLine(" Double Round Type ")));
        } else {
            frame.addComponent(gameTypePanel.withBorder(Borders.doubleLine(" Game Type ")));
            frame.addComponent(gameDoubleRoundPanel.withBorder(Borders.singleLine(" Double Round Type ")));
        }
    }
}
