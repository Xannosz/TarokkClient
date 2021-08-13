package hu.xannosz.tarokk.client.tui.subframe;

import com.googlecode.lanterna.gui2.Component;
import com.googlecode.lanterna.gui2.GridLayout;
import com.googlecode.lanterna.gui2.Label;
import com.googlecode.lanterna.gui2.Panel;
import com.googlecode.lanterna.input.KeyStroke;
import com.googlecode.lanterna.input.KeyType;
import com.tisza.tarock.proto.MainProto;
import hu.xannosz.tarokk.client.game.Card;
import hu.xannosz.tarokk.client.network.Action;
import hu.xannosz.tarokk.client.network.Messages;
import hu.xannosz.tarokk.client.tui.TuiClient;
import hu.xannosz.tarokk.client.util.ThemeHandler;
import hu.xannosz.tarokk.client.util.Translator;
import hu.xannosz.tarokk.client.util.Util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static hu.xannosz.tarokk.client.util.Util.addKeyWithCardToPanel;
import static hu.xannosz.tarokk.client.util.Util.getGameData;

public class FoldingSubFrame extends SubFrame {

    private final List<Card> foldedCard = new ArrayList<>();
    private List<Card> card;
    private final int gameId;

    public FoldingSubFrame(TuiClient tuiClient, int gameId) {
        super(tuiClient);
        this.gameId = gameId;
    }

    @Override
    public Component getPanel() {
        MainProto.GameSession gameData = getGameData(gameId, tuiClient);
        Panel panel = new Panel();
        panel.setLayoutManager(new GridLayout(1));

        Panel foldDone = new Panel();
        foldDone.setLayoutManager(new GridLayout(4));
        for (int foldedUser : tuiClient.getServerLiveData().getFoldDone()) {
            Util.addData(foldDone, Util.getPlayerName(foldedUser, gameData, tuiClient), Translator.INST.foldDone);
        }
        panel.addComponent(foldDone);

        panel.addComponent(new Label(Translator.INST.cards));

        card = new ArrayList<>(tuiClient.getServerLiveData().getPlayerCard());
        card.removeAll(foldedCard);

        Panel cards = new Panel();
        cards.setLayoutManager(new GridLayout(4));
        for (int i = 0; i < card.size(); i++) {
            if (i <= 9) {
                addKeyWithCardToPanel(cards, "" + i, card.get(i));
            }
            if (i == 10) {
                addKeyWithCardToPanel(cards, "-", card.get(i));
            }
            if (i == 11) {
                addKeyWithCardToPanel(cards, "+", card.get(i));
            }
        }
        panel.addComponent(cards);

        panel.addComponent(new Label(Translator.INST.foldedCards));

        Panel foldedCards = new Panel();
        foldedCards.setLayoutManager(new GridLayout(4));
        for (Card card : foldedCard) {
            foldedCards.addComponent(new Label(card.getFormattedName()).setTheme(ThemeHandler.getHighLightedThemeMainPanel()));
        }
        panel.addComponent(foldedCards);

        return panel;
    }

    @Override
    public Map<String, String> getFooter() {
        Map<String, String> response = new HashMap<>();
        response.put("*", Translator.INST.resetFolding);
        response.put("Enter", Translator.INST.sendFolding);
        return response;
    }

    @Override
    public void handleKeyStroke(KeyStroke keyStroke) {
        if (keyStroke.getKeyType().equals(KeyType.Enter)) {
            tuiClient.getConnection().sendMessage(Messages.sendAction(Action.fold(foldedCard)));
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
                if (keyStroke.getCharacter().toString().equals("" + i)) {
                    foldedCard.add(card.get(i));
                }
            }
        }
    }
}
