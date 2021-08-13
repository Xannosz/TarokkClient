package hu.xannosz.tarokk.client.tui.metapanel;

import com.googlecode.lanterna.gui2.GridLayout;
import com.googlecode.lanterna.gui2.Panel;
import com.tisza.tarock.proto.MainProto;
import hu.xannosz.microtools.pack.Douplet;
import hu.xannosz.tarokk.client.game.Actions;
import hu.xannosz.tarokk.client.tui.TuiClient;
import hu.xannosz.tarokk.client.util.Translator;
import hu.xannosz.tarokk.client.util.Util;

import java.util.Map;

import static hu.xannosz.tarokk.client.util.Util.*;

public class DataMetaPanel extends Panel {
    public DataMetaPanel(TuiClient tuiClient, MainProto.GameSession gameData) {
        setLayoutManager(new GridLayout(2));

        if (!Util.anyNull(tuiClient.getServerLiveData().getGameType(),
                tuiClient.getServerLiveData().getPhase(),
                tuiClient.getServerLiveData().getBeginnerPlayer(),
                gameData)) {
            addData(this, Translator.INST.gameType, tuiClient.getServerLiveData().getGameType());
            addData(this, Translator.INST.beginnerPlayer, getPlayerName(tuiClient.getServerLiveData().getBeginnerPlayer(), gameData, tuiClient));
            addData(this, Translator.INST.phase, tuiClient.getServerLiveData().getPhase().getName());
            addData(this, Translator.INST.playerInTurn, getPlayerName(tuiClient.getServerLiveData().getPlayerTurn(), gameData, tuiClient));

            if (tuiClient.getServerLiveData().getPlayerActions().get(Actions.FOLD) != null) {
                for (Douplet<Integer, String> folded : tuiClient.getServerLiveData().getPlayerActions().get(Actions.FOLD)) {
                    addData(this, getPlayerName(folded.getFirst(), gameData, tuiClient) + Translator.INST.foldedTarock, getFormattedCardName(folded.getSecond()));
                }
            }

            for (
                    Map.Entry<Integer, Integer> skartedTarock : tuiClient.getServerLiveData().getSkartedTarocks().entrySet()) {
                addData(this, getPlayerName(skartedTarock.getKey(), gameData, tuiClient) + Translator.INST.foldedTarock, "" + skartedTarock.getValue());
            }

            if (tuiClient.getServerLiveData().getPlayerActions().get(Actions.CALL) != null) {
                for (Douplet<Integer, String> call : tuiClient.getServerLiveData().getPlayerActions().get(Actions.CALL)) {
                    addData(this, getPlayerName(call.getFirst(), gameData, tuiClient) + Translator.INST.calledTarock, getFormattedCardName(call.getSecond()));
                }
            }

            for (Map.Entry<Integer, Integer> entry : tuiClient.getServerLiveData().getCardsTakenUsers().entrySet()) {
                addData(this, getPlayerName(entry.getKey(), gameData, tuiClient) + Translator.INST.takenCards, "" + entry.getValue());
            }
        }
    }
}
