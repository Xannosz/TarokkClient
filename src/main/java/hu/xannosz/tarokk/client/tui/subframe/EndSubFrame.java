package hu.xannosz.tarokk.client.tui.subframe;

import com.googlecode.lanterna.gui2.Component;
import com.googlecode.lanterna.gui2.GridLayout;
import com.googlecode.lanterna.gui2.Panel;
import com.googlecode.lanterna.input.KeyStroke;
import com.googlecode.lanterna.input.KeyType;
import hu.xannosz.tarokk.client.network.Action;
import hu.xannosz.tarokk.client.tui.TuiClient;
import hu.xannosz.tarokk.client.util.MessageTranslator;

import java.util.Collections;
import java.util.Map;

import static hu.xannosz.tarokk.client.util.Util.addData;
import static hu.xannosz.tarokk.client.util.Util.getPlayerName;

public class EndSubFrame extends SubFrame {
    public EndSubFrame(TuiClient tuiClient) {
        super(tuiClient);
    }

    @Override
    public Component getPanel() {
        Panel panel = new Panel();
        panel.setLayoutManager(new GridLayout(4));
        for (Map.Entry<Integer, Integer> playerPoints : tuiClient.getServerLiveData().getPlayerPoints().entrySet()) {
            addData(panel, getPlayerName(playerPoints.getKey()) + " points", "" + playerPoints.getValue(), tuiClient);
        }
        for (Map.Entry<Integer, Integer> playerIncrementPoints : tuiClient.getServerLiveData().getIncrementPlayerPoints().entrySet()) {
            addData(panel, getPlayerName(playerIncrementPoints.getKey()) + " incremented points", "" + playerIncrementPoints.getValue(), tuiClient);
        }
        return panel;
    }

    @Override
    public Map<String, String> getFooter() {
        return Collections.singletonMap("Enter","new game");
    }

    @Override
    public void handleKeyStroke(KeyStroke keyStroke) {
        if (keyStroke.getKeyType().equals(KeyType.Enter)) {
            tuiClient.getConnection().sendMessage(MessageTranslator.sendAction(Action.readyForNewGame()));
            tuiClient.getServerLiveData().clearGameData();
        }
    }
}
