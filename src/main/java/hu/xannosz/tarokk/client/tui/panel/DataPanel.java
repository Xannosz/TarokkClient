package hu.xannosz.tarokk.client.tui.panel;

import com.googlecode.lanterna.gui2.GridLayout;
import com.googlecode.lanterna.gui2.Panel;
import com.tisza.tarock.proto.MainProto;
import hu.xannosz.microtools.pack.Douplet;
import hu.xannosz.tarokk.client.tui.TuiClient;
import hu.xannosz.tarokk.client.util.Util;

import java.util.Map;

import static hu.xannosz.tarokk.client.util.Util.*;

public class DataPanel extends Panel {
    public DataPanel(TuiClient tuiClient, MainProto.GameSession gameData) {
        setLayoutManager(new GridLayout(2));

        if (!Util.anyNull(tuiClient.getServerLiveData().getGameType(),
                tuiClient.getServerLiveData().getPhase(),
                tuiClient.getServerLiveData().getBeginnerPlayer(),
                gameData)) {
            addData(this, "Game Type", tuiClient.getServerLiveData().getGameType(), tuiClient);
            addData(this, "Beginner Player", getPlayerName(tuiClient.getServerLiveData().getBeginnerPlayer(), gameData, tuiClient), tuiClient);
            addData(this, "Phase", tuiClient.getServerLiveData().getPhase().getName(), tuiClient);
            addData(this, "Player in Turn", getPlayerName(tuiClient.getServerLiveData().getPlayerTurn(), gameData, tuiClient), tuiClient);

            if (tuiClient.getServerLiveData().getPlayerActions().get("fold") != null) {
                for (Douplet<Integer, String> folded : tuiClient.getServerLiveData().getPlayerActions().get("fold")) {
                    addData(this, getPlayerName(folded.getFirst(), gameData, tuiClient) + " folded tarock", getFormattedCardName(folded.getSecond()), tuiClient);
                }
            }

            for (
                    Map.Entry<Integer, Integer> skartedTarock : tuiClient.getServerLiveData().getSkartedTarocks().entrySet()) {
                addData(this, getPlayerName(skartedTarock.getKey(), gameData, tuiClient) + " folded tarock", "" + skartedTarock.getValue(), tuiClient);
            }

            if (tuiClient.getServerLiveData().getPlayerActions().get("call") != null) {
                for (Douplet<Integer, String> call : tuiClient.getServerLiveData().getPlayerActions().get("call")) {
                    addData(this, getPlayerName(call.getFirst(), gameData, tuiClient) + " called tarock", getFormattedCardName(call.getSecond()), tuiClient);
                }
            }

            for (Map.Entry<Integer, Integer> entry : tuiClient.getServerLiveData().getCardsTakenUsers().entrySet()) {
                addData(this, getPlayerName(entry.getKey(), gameData, tuiClient) + " taken cards", "" + entry.getValue(), tuiClient);
            }
        }
    }
}
