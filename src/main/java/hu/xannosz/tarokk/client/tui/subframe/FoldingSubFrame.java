package hu.xannosz.tarokk.client.tui.subframe;

import com.googlecode.lanterna.gui2.Component;
import com.googlecode.lanterna.gui2.GridLayout;
import com.googlecode.lanterna.gui2.Label;
import com.googlecode.lanterna.gui2.Panel;
import com.googlecode.lanterna.input.KeyStroke;
import com.googlecode.lanterna.input.KeyType;
import hu.xannosz.tarokk.client.game.Card;
import hu.xannosz.tarokk.client.network.Action;
import hu.xannosz.tarokk.client.tui.TuiClient;
import hu.xannosz.tarokk.client.util.MessageTranslator;
import hu.xannosz.tarokk.client.util.ThemeHandler;
import hu.xannosz.tarokk.client.util.Util;

import java.util.*;

import static hu.xannosz.tarokk.client.util.Util.addKeyWithCardToPanel;
import static hu.xannosz.tarokk.client.util.Util.getFormattedCardName;

public class FoldingSubFrame extends SubFrame {

    private final List<Card> foldedCard = new ArrayList<>();
    private List<Card> card;

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

        card = new ArrayList<>(tuiClient.getServerLiveData().getPlayerCard());
        card.removeAll(foldedCard);

        for (int i = 0; i < card.size(); i++) {
            if (i <= 9) {
                addKeyWithCardToPanel(panel, "" + i, card.get(i).getFormattedName(),tuiClient);
            }
            if (i == 10) {
                addKeyWithCardToPanel(panel, "-", card.get(i).getFormattedName(),tuiClient);
            }
            if (i == 11) {
                addKeyWithCardToPanel(panel, "+", card.get(i).getFormattedName(),tuiClient);
            }
        }

        panel.addComponent(new Label("Folded Cards:"));
        for (Card card : foldedCard) {
            panel.addComponent(new Label(card.getFormattedName()).setTheme(ThemeHandler.getHighLightedThemeMainPanel(tuiClient.getTerminalSettings())));
        }

        return panel;
    }

    @Override
    public Map<String, String> getFooter() {
        Map<String, String> response = new HashMap<>();
        response.put("*", "Reset folding");
        response.put("Enter", "Send folding");
        return response;
    }

    @Override
    public void handleKeyStroke(KeyStroke keyStroke) {
        if (keyStroke.getKeyType().equals(KeyType.Enter)) {
            tuiClient.getConnection().sendMessage(MessageTranslator.sendAction(Action.fold(foldedCard)));
        }
        if (keyStroke.getKeyType().equals(KeyType.Character)) {
            if (keyStroke.getCharacter().equals('*')) {
                foldedCard.clear();
            }
            if (keyStroke.getCharacter().equals('-')) {
                if (card.size() > 10) {
                    foldedCard.add(card.get(10));
                }
            }
            if (keyStroke.getCharacter().equals('+')) {
                if (card.size() > 11) {
                    foldedCard.add(card.get(11));
                }
            }
            for (int i = 0; i < card.size(); i++) {
                if (keyStroke.getCharacter().equals((char) i)) {
                    foldedCard.add(card.get(i));
                }
            }
        }
    }
}
