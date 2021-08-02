package hu.xannosz.tarokk.client.tui.frame;

import com.googlecode.lanterna.TerminalSize;
import com.googlecode.lanterna.gui2.*;
import com.googlecode.lanterna.input.KeyStroke;
import com.googlecode.lanterna.input.KeyType;
import hu.xannosz.tarokk.client.game.DoubleRoundType;
import hu.xannosz.tarokk.client.game.GameType;
import hu.xannosz.tarokk.client.tui.TuiClient;
import hu.xannosz.tarokk.client.util.MessageTranslator;
import hu.xannosz.tarokk.client.util.ThemeHandler;

public class NewGameFrame extends Frame {

    private Panel frame = new Panel();
    private GameType gameType = GameType.values()[0];
    private DoubleRoundType doubleRoundType = DoubleRoundType.values()[0];
    private boolean doublePanelActivated;

    public NewGameFrame(TuiClient tuiClient) {
        super(tuiClient);
    }

    @Override
    public Component getPanel() {
        return frame;
    }

    @Override
    public Component getFooter() {
        Panel footer = new Panel();
        footer.setLayoutManager(new GridLayout(9));

        footer.addComponent(new Label("["));
        footer.addComponent(new Label("Arrows").setTheme(ThemeHandler.getKeyThemeFooterPanel(tuiClient.getTerminalSettings())));
        footer.addComponent(new Label("]: Movement"));

        footer.addComponent(new Label("["));
        footer.addComponent(new Label("Enter").setTheme(ThemeHandler.getKeyThemeFooterPanel(tuiClient.getTerminalSettings())));
        footer.addComponent(new Label("]: Create game"));

        footer.addComponent(new Label("["));
        footer.addComponent(new Label("/").setTheme(ThemeHandler.getKeyThemeFooterPanel(tuiClient.getTerminalSettings())));
        footer.addComponent(new Label("]: Cancel"));

        return footer;
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
            tuiClient.getConnection().sendMessage(MessageTranslator.newGame(doubleRoundType, gameType));
            tuiClient.setFrame(new GameFrame(tuiClient));
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

        Panel gameTypePanel = new Panel();
        gameTypePanel.setPreferredSize(new TerminalSize(size.getColumns() / 2, size.getRows() - 2));
        for (GameType gameType : GameType.values()) {
            if (gameType.equals(this.gameType)) {
                gameTypePanel.addComponent(new Label(gameType.getName()).setTheme(ThemeHandler.getHighLightedThemeMainPanel(tuiClient.getTerminalSettings())));
            } else {
                gameTypePanel.addComponent(new Label(gameType.getName()));
            }
        }

        Panel gameDoubleRoundPanel = new Panel();
        gameDoubleRoundPanel.setPreferredSize(new TerminalSize(size.getColumns() / 2, size.getRows() - 2));
        for (DoubleRoundType doubleRoundType : DoubleRoundType.values()) {
            if (doubleRoundType.equals(this.doubleRoundType)) {
                gameDoubleRoundPanel.addComponent(new Label(doubleRoundType.getName()).setTheme(ThemeHandler.getHighLightedThemeMainPanel(tuiClient.getTerminalSettings())));
            } else {
                gameDoubleRoundPanel.addComponent(new Label(doubleRoundType.getName()));
            }
        }

        if (doublePanelActivated) {
            frame.addComponent(gameTypePanel.withBorder(Borders.singleLine(" Game Type ")));
            frame.addComponent(gameDoubleRoundPanel.withBorder(Borders.doubleLine(" Double Round Type ")));
        } else {
            frame.addComponent(gameTypePanel.withBorder(Borders.doubleLine(" Game Type ")));
            frame.addComponent(gameDoubleRoundPanel.withBorder(Borders.singleLine(" Double Round Type ")));
        }
    }
}
