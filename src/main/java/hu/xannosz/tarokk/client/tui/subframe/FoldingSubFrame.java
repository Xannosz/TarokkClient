package hu.xannosz.tarokk.client.tui.subframe;

import com.googlecode.lanterna.gui2.Component;
import com.googlecode.lanterna.gui2.GridLayout;
import com.googlecode.lanterna.gui2.Label;
import com.googlecode.lanterna.gui2.Panel;
import com.googlecode.lanterna.input.KeyStroke;
import com.googlecode.lanterna.input.KeyType;
import hu.xannosz.tarokk.client.tui.TuiClient;
import hu.xannosz.tarokk.client.util.ThemeHandler;
import hu.xannosz.tarokk.client.util.Util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import static hu.xannosz.tarokk.client.util.Util.getFormattedCardName;

public class FoldingSubFrame extends SubFrame {

    private final List<String> foldedCardIds = new ArrayList<>();
    private List<String> cardIds;

    public FoldingSubFrame(TuiClient tuiClient) {
        super(tuiClient);
    }

    @Override
    public Component getPanel() {
        Panel panel = new Panel();
        panel.setLayoutManager(new GridLayout(4));
        for (int foldedUser : tuiClient.getServerLiveData().getFoldDone()) {
            Util.addData(panel, Util.getPlayerName(foldedUser), "Fold done", tuiClient);
        }

        panel.addComponent(new Label("Cards:"));

        cardIds = new ArrayList<>(tuiClient.getServerLiveData().getPlayerCardIds());
        cardIds.removeAll(foldedCardIds);

        for (int i = 0; i < cardIds.size(); i++) {
            if (i <= 9) {
                addKeyWithCardToPanel(panel, "" + i, cardIds.get(i));
            }
            if (i == 10) {
                addKeyWithCardToPanel(panel, "-", cardIds.get(i));
            }
            if (i == 11) {
                addKeyWithCardToPanel(panel, "+", cardIds.get(i));
            }
        }

        panel.addComponent(new Label("Folded Cards:"));
        for (String cardId : foldedCardIds) {
            panel.addComponent(new Label(getFormattedCardName(cardId)).setTheme(ThemeHandler.getHighLightedThemeMainPanel(tuiClient.getTerminalSettings())));
        }

        return panel;
    }

    @Override
    public Map<String, String> getFooter() {
        return Collections.singletonMap("*", "Reset folding");
    }

    @Override
    public void handleKeyStroke(KeyStroke keyStroke) {
        if (keyStroke.getKeyType().equals(KeyType.Character)) {
            if (keyStroke.getCharacter().equals('*')) {
                foldedCardIds.clear();
            }
            if (keyStroke.getCharacter().equals('-')) {
                if (cardIds.size() > 10) {
                    foldedCardIds.add(cardIds.get(10));
                }
            }
            if (keyStroke.getCharacter().equals('+')) {
                if (cardIds.size() > 11) {
                    foldedCardIds.add(cardIds.get(11));
                }
            }
            for (int i = 0; i < cardIds.size(); i++) {
                if (keyStroke.getCharacter().equals((char)i)) {
                        foldedCardIds.add(cardIds.get(i));
                }
            }
        }
    }

    private void addKeyWithCardToPanel(Panel panel, String key, String cardId) {
        panel.addComponent(new Label(key + ":"));
        panel.addComponent(new Label(getFormattedCardName(cardId)).setTheme(ThemeHandler.getHighLightedThemeMainPanel(tuiClient.getTerminalSettings())));
    }
}
