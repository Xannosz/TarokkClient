package hu.xannosz.tarokk.client.tui.subframe;

import com.googlecode.lanterna.gui2.Component;
import com.googlecode.lanterna.gui2.Label;
import com.googlecode.lanterna.gui2.Panel;
import hu.xannosz.tarokk.client.tui.KeyMapDictionary;
import hu.xannosz.tarokk.client.tui.TuiClient;
import hu.xannosz.tarokk.client.util.ThemeHandler;
import hu.xannosz.tarokk.client.util.Util;

import static hu.xannosz.tarokk.client.util.Util.getFormattedCardName;

public class FoldingSubFrame extends SubFrame {
    public FoldingSubFrame(TuiClient tuiClient, KeyMapDictionary keyMapDictionary) {
        super(tuiClient, keyMapDictionary);
    }

    @Override
    public Component getPanel() {
        Panel panel = new Panel();
        for (int foldedUser : tuiClient.getServerLiveData().getFoldDone()) {
            Util.addData(panel, Util.getPlayerName(foldedUser), "Fold done", tuiClient);
        }
        for (String card : tuiClient.getServerLiveData().getPlayerCards()) {
            panel.addComponent(new Label(getFormattedCardName(card)).setTheme(ThemeHandler.getHighLightedThemeMainPanel(tuiClient.getTerminalSettings())));
        //keyMapDictionary.addFunction();
        }
        return panel;
    }
}
