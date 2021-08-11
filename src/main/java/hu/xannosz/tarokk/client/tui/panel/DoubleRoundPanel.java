package hu.xannosz.tarokk.client.tui.panel;

import com.googlecode.lanterna.gui2.Label;
import com.googlecode.lanterna.gui2.Panel;
import hu.xannosz.tarokk.client.game.DoubleRoundType;
import hu.xannosz.tarokk.client.tui.TuiClient;
import hu.xannosz.tarokk.client.util.ThemeHandler;

public class DoubleRoundPanel extends Panel {
    public DoubleRoundPanel(DoubleRoundType doubleRoundTypeIn, TuiClient tuiClient){
        for (DoubleRoundType doubleRoundType : DoubleRoundType.values()) {
            if (doubleRoundType.equals(doubleRoundTypeIn)) {
                addComponent(new Label(doubleRoundType.getName()).setTheme(ThemeHandler.getHighLightedThemeMainPanel(tuiClient.getTerminalSettings())));
            } else {
                addComponent(new Label(doubleRoundType.getName()));
            }
        }
    }
}
