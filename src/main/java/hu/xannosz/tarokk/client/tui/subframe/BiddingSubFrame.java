package hu.xannosz.tarokk.client.tui.subframe;

import com.googlecode.lanterna.gui2.Component;
import com.googlecode.lanterna.gui2.GridLayout;
import com.googlecode.lanterna.gui2.Panel;
import com.googlecode.lanterna.input.KeyStroke;
import com.tisza.tarock.proto.MainProto;
import hu.xannosz.microtools.pack.Douplet;
import hu.xannosz.tarokk.client.network.Action;
import hu.xannosz.tarokk.client.network.Messages;
import hu.xannosz.tarokk.client.tui.KeyMapDictionary;
import hu.xannosz.tarokk.client.tui.TuiClient;
import hu.xannosz.tarokk.client.util.Util;

import java.util.Map;

import static hu.xannosz.tarokk.client.util.Util.*;

public class BiddingSubFrame extends SubFrame {

    private int lastBid = 4;
    private final KeyMapDictionary keyMapDictionary = new KeyMapDictionary();
    private final int gameId;

    public BiddingSubFrame(TuiClient tuiClient, int gameId) {
        super(tuiClient);
        this.gameId = gameId;
    }

    @Override
    public Map<String, String> getFooter() {
        return keyMapDictionary.getFunctionNames();
    }

    @Override
    public void handleKeyStroke(KeyStroke keyStroke) {
        keyMapDictionary.runFunction(keyStroke.getCharacter().toString());
    }

    @Override
    public Component getPanel() {
        MainProto.GameSession gameData = getGameData(gameId, tuiClient);
        keyMapDictionary.clear();
        Panel panel = new Panel(new GridLayout(4));

        if (Util.anyNull(tuiClient.getServerLiveData().getPlayerActions().get("bid"),
                tuiClient.getServerLiveData().getAvailableBids())) {
            return panel;
        }

        for (Douplet<Integer, String> action : tuiClient.getServerLiveData().getPlayerActions().get("bid")) {
            addData(panel, getPlayerName(action.getFirst(), gameData, tuiClient), biddingToString(action.getSecond()), tuiClient);
        }

        for (int bid : tuiClient.getServerLiveData().getAvailableBids()) {
            keyMapDictionary.addFunction("" + bid, biddingToString(bid), () ->
                    tuiClient.getConnection().sendMessage(Messages.sendAction(Action.bid(bid))));
        }

        return panel;
    }

    private String biddingToString(String action) {
        if (action.equals("p")) {
            return "Pass";
        }

        return biddingToString(Integer.parseInt(action));
    }

    private String biddingToString(int num) {
        if (num == -1) {
            return "Pass";
        }

        if (num == lastBid) {
            return "Hold";
        }

        lastBid = num;

        if (num == 3) {
            return "Three";
        }
        if (num == 2) {
            return "Two";
        }
        if (num == 1) {
            return "One";
        }
        if (num == 0) {
            return "Solo";
        }

        return "invalid:" + num;
    }
}
