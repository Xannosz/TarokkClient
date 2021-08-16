package hu.xannosz.tarokk.client.tui.subframe;

import com.googlecode.lanterna.gui2.Component;
import com.googlecode.lanterna.gui2.GridLayout;
import com.googlecode.lanterna.gui2.Panel;
import com.googlecode.lanterna.input.KeyStroke;
import com.googlecode.lanterna.input.KeyType;
import com.tisza.tarock.proto.MainProto;
import hu.xannosz.tarokk.client.network.Action;
import hu.xannosz.tarokk.client.network.Messages;
import hu.xannosz.tarokk.client.tui.TuiClient;
import hu.xannosz.tarokk.client.tui.frame.GameFrame;
import hu.xannosz.tarokk.client.util.translator.Translator;

import java.util.Collections;
import java.util.Map;

import static hu.xannosz.tarokk.client.util.Util.*;

public class EndSubFrame extends SubFrame {
    private final int gameId;

    public EndSubFrame(TuiClient tuiClient, int gameId) {
        super(tuiClient);
        this.gameId = gameId;
    }

    @Override
    public Component getPanel() {
        MainProto.GameSession gameData = getGameData(gameId, tuiClient);
        Panel panel = new Panel();
        panel.setLayoutManager(new GridLayout(4));
        for (Map.Entry<Integer, Integer> playerPoints : tuiClient.getServerLiveData().getPlayerPoints().entrySet()) {
            addData(panel, getPlayerName(playerPoints.getKey(), gameData, tuiClient) + Translator.INST.points, "" + playerPoints.getValue());
        }
        for (Map.Entry<Integer, Integer> playerIncrementPoints : tuiClient.getServerLiveData().getIncrementPlayerPoints().entrySet()) {
            addData(panel, getPlayerName(playerIncrementPoints.getKey(), gameData, tuiClient) + Translator.INST.incrementedPoints, "" + playerIncrementPoints.getValue());
        }
        return panel;
    }

    @Override
    public Map<String, String> getFooter() {
        return Collections.singletonMap("Enter", Translator.INST.newGame);
    }

    @Override
    public void handleKeyStroke(KeyStroke keyStroke) {
        if (keyStroke.getKeyType().equals(KeyType.Enter)) {
            tuiClient.getConnection().sendMessage(Messages.sendAction(Action.readyForNewGame()));
            tuiClient.setFrame(new GameFrame(tuiClient, gameId));
            tuiClient.getServerLiveData().clearGameData();
        }
    }
}
