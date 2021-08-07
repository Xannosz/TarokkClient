package hu.xannosz.tarokk.client.tui.panel;

import com.googlecode.lanterna.gui2.GridLayout;
import com.googlecode.lanterna.gui2.Label;
import com.googlecode.lanterna.gui2.Panel;
import hu.xannosz.tarokk.client.game.Card;
import hu.xannosz.tarokk.client.tui.TuiClient;
import hu.xannosz.tarokk.client.util.ThemeHandler;
import hu.xannosz.tarokk.client.util.Util;

public class CardPanel extends Panel {
    public CardPanel(TuiClient tuiClient) {
        if (!Util.anyNull(tuiClient.getServerLiveData().getPlayerCard())) {
            setLayoutManager(new GridLayout(3));
            for (Card card : tuiClient.getServerLiveData().getPlayerCard()) {
                addComponent(new Label(card.getFormattedName()).setTheme(ThemeHandler.getHighLightedThemeMainPanel(tuiClient.getTerminalSettings())));
            }
        }
    }
}
