package hu.xannosz.tarokk.client.tui.subframe;

import com.googlecode.lanterna.gui2.Component;
import com.googlecode.lanterna.gui2.GridLayout;
import com.googlecode.lanterna.gui2.Panel;
import com.googlecode.lanterna.input.KeyStroke;
import com.googlecode.lanterna.input.KeyType;
import com.tisza.tarock.proto.MainProto;
import hu.xannosz.microtools.pack.Douplet;
import hu.xannosz.tarokk.client.game.Card;
import hu.xannosz.tarokk.client.network.Action;
import hu.xannosz.tarokk.client.tui.TuiClient;
import hu.xannosz.tarokk.client.network.Messages;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import static hu.xannosz.tarokk.client.util.Util.*;

public class GamePlaySubFrame extends SubFrame {
    private List<Card> card;
    private final int gameId;

    public GamePlaySubFrame(TuiClient tuiClient, int gameId) {
        super(tuiClient);
        this.gameId = gameId;
    }

    @Override
    public Component getPanel() {
        MainProto.GameSession gameData = getGameData(gameId, tuiClient);
        Panel panel = new Panel();

        card = new ArrayList<>(tuiClient.getServerLiveData().getPlayerCard());

        Panel playCard = new Panel();
        playCard.setLayoutManager(new GridLayout(4));
        if (tuiClient.getServerLiveData().getPlayerActions().get("play") != null) {
            for (Douplet<Integer, String> play : tuiClient.getServerLiveData().getTurnPlayerActions()) {
                addData(playCard, getPlayerName(play.getFirst(), gameData, tuiClient) + " play card", getFormattedCardName(play.getSecond()), tuiClient);
            }
            for (Douplet<Integer, String> play : tuiClient.getServerLiveData().getPlayerActions().get("play")) {
                if (gameData.getUserId(play.getFirst()) == tuiClient.getServerLiveData().getLoginResult().getUserId()) {
                    card.remove(Card.parseCard(play.getSecond()));
                }
            }
        }
        panel.addComponent(playCard);

        Panel cards = new Panel();
        cards.setLayoutManager(new GridLayout(8));
        for (int i = 0; i < card.size(); i++) {
            addKeyWithCardToPanel(cards, "" + (i + 1), card.get(i), tuiClient);
        }
        panel.addComponent(cards);

        return panel;
    }

    @Override
    public Map<String, String> getFooter() {
        return Collections.emptyMap();
    }

    @Override
    public void handleKeyStroke(KeyStroke keyStroke) {
        if (keyStroke.getKeyType().equals(KeyType.Character)) {
            for (int i = 0; i < card.size(); i++) {
                if (keyStroke.getCharacter().toString().equals("" + (i + 1))) {
                    tuiClient.getConnection().sendMessage(Messages.sendAction(Action.play(card.get(i))));
                }
            }
        }
    }
}
