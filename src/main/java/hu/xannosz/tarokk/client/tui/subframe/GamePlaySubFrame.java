package hu.xannosz.tarokk.client.tui.subframe;

import com.googlecode.lanterna.gui2.Component;
import com.googlecode.lanterna.gui2.GridLayout;
import com.googlecode.lanterna.gui2.Panel;
import com.googlecode.lanterna.input.KeyStroke;
import com.googlecode.lanterna.input.KeyType;
import hu.xannosz.microtools.pack.Douplet;
import hu.xannosz.tarokk.client.game.Card;
import hu.xannosz.tarokk.client.game.GamePhase;
import hu.xannosz.tarokk.client.network.Action;
import hu.xannosz.tarokk.client.tui.TuiClient;
import hu.xannosz.tarokk.client.util.MessageTranslator;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import static hu.xannosz.tarokk.client.util.Util.*;
import static hu.xannosz.tarokk.client.util.Util.getFormattedCardName;

public class GamePlaySubFrame extends SubFrame {
    private List<Card> card;

    public GamePlaySubFrame(TuiClient tuiClient) {
        super(tuiClient);
    }

    @Override
    public Component getPanel() {
        Panel panel = new Panel();
        panel.setLayoutManager(new GridLayout(4));

        card = new ArrayList<>(tuiClient.getServerLiveData().getPlayerCard());

        if (tuiClient.getServerLiveData().getPlayerActions().get(GamePhase.GAMEPLAY) != null) {
            for (Douplet<Integer, String> play : tuiClient.getServerLiveData().getPlayerActions().get(GamePhase.GAMEPLAY)) {
                addData(panel, getPlayerName(play.getFirst()) + " play card", getFormattedCardName(play.getSecond().replace("play:", "")), tuiClient);
           //TODO see just last round.
            }
        }

        for (int i = 0; i < card.size(); i++) {
            addKeyWithCardToPanel(panel, "" + (i + 1), card.get(i).getFormattedName(), tuiClient);
        }
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
                if (keyStroke.getCharacter().equals((char) (i + 1))) {
                    tuiClient.getConnection().sendMessage(MessageTranslator.sendAction(Action.play(card.get(i))));
                }
            }
        }
    }
}
