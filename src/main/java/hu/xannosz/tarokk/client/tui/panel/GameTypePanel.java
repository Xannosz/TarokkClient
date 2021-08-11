package hu.xannosz.tarokk.client.tui.panel;

import com.googlecode.lanterna.gui2.Label;
import com.googlecode.lanterna.gui2.Panel;
import hu.xannosz.tarokk.client.game.GameType;
import hu.xannosz.tarokk.client.tui.TuiClient;
import hu.xannosz.tarokk.client.util.ThemeHandler;

public class GameTypePanel extends Panel {
    public GameTypePanel(GameType gameTypeIn, TuiClient tuiClient) {
        for (GameType gameType : GameType.values()) {
            if (gameType.equals(gameTypeIn)) {
                addComponent(new Label(gameType.getName()).setTheme(ThemeHandler.getHighLightedThemeMainPanel(tuiClient.getTerminalSettings())));
            } else {
                addComponent(new Label(gameType.getName()));
            }
        }
    }
}