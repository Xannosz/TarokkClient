package hu.xannosz.tarokk.client.tui.subframe;

import com.googlecode.lanterna.gui2.Component;
import com.googlecode.lanterna.gui2.Panel;
import com.googlecode.lanterna.input.KeyStroke;
import hu.xannosz.microtools.pack.Douplet;
import hu.xannosz.tarokk.client.game.GamePhase;
import hu.xannosz.tarokk.client.network.Action;
import hu.xannosz.tarokk.client.tui.KeyMapDictionary;
import hu.xannosz.tarokk.client.tui.TuiClient;
import hu.xannosz.tarokk.client.util.MessageTranslator;

import java.util.Map;

import static hu.xannosz.tarokk.client.util.Util.addData;
import static hu.xannosz.tarokk.client.util.Util.getPlayerName;

public class BiddingSubFrame extends SubFrame {

    private int lastBid = 4;
    private boolean hold;
    private final KeyMapDictionary keyMapDictionary = new KeyMapDictionary();

    public BiddingSubFrame(TuiClient tuiClient) {
        super(tuiClient);
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
        keyMapDictionary.clear();
        Panel panel = new Panel();
        for (Douplet<Integer, String> action : tuiClient.getServerLiveData().getPlayerActions().get(GamePhase.BIDDING)) {
            addData(panel, getPlayerName(action.getFirst()), biddingToString(action.getSecond()), tuiClient);
        }
        if (lastBid == 4) {
            keyMapDictionary.addFunction("3", "Three", () ->
                    tuiClient.getConnection().sendMessage(MessageTranslator.sendAction(Action.bid(3))));
            keyMapDictionary.addFunction("2", "Two", () ->
                    tuiClient.getConnection().sendMessage(MessageTranslator.sendAction(Action.bid(2))));
            keyMapDictionary.addFunction("1", "One", () ->
                    tuiClient.getConnection().sendMessage(MessageTranslator.sendAction(Action.bid(1))));
            keyMapDictionary.addFunction("0", "Solo", () ->
                    tuiClient.getConnection().sendMessage(MessageTranslator.sendAction(Action.bid(0))));
        }
        if (lastBid == 3) {
            if (!hold) {
                keyMapDictionary.addFunction("3", "Hold", () ->
                        tuiClient.getConnection().sendMessage(MessageTranslator.sendAction(Action.bid(3))));
            }
            keyMapDictionary.addFunction("2", "Two", () ->
                    tuiClient.getConnection().sendMessage(MessageTranslator.sendAction(Action.bid(2))));
            keyMapDictionary.addFunction("1", "One", () ->
                    tuiClient.getConnection().sendMessage(MessageTranslator.sendAction(Action.bid(1))));
            keyMapDictionary.addFunction("0", "Solo", () ->
                    tuiClient.getConnection().sendMessage(MessageTranslator.sendAction(Action.bid(0))));
        }
        if (lastBid == 2) {
            if (!hold) {
                keyMapDictionary.addFunction("2", "Hold", () ->
                        tuiClient.getConnection().sendMessage(MessageTranslator.sendAction(Action.bid(2))));
            }
            keyMapDictionary.addFunction("1", "One", () ->
                    tuiClient.getConnection().sendMessage(MessageTranslator.sendAction(Action.bid(1))));
            keyMapDictionary.addFunction("0", "Solo", () ->
                    tuiClient.getConnection().sendMessage(MessageTranslator.sendAction(Action.bid(0))));
        }
        if (lastBid == 1) {
            if (!hold) {
                keyMapDictionary.addFunction("1", "Hold", () ->
                        tuiClient.getConnection().sendMessage(MessageTranslator.sendAction(Action.bid(1))));
            }
            keyMapDictionary.addFunction("0", "Solo", () ->
                    tuiClient.getConnection().sendMessage(MessageTranslator.sendAction(Action.bid(0))));
        }
        if (lastBid == 0) {
            if (!hold) {
                keyMapDictionary.addFunction("0", "Hold", () ->
                        tuiClient.getConnection().sendMessage(MessageTranslator.sendAction(Action.bid(0))));
            }
        }
        keyMapDictionary.addFunction("-", "Pass", () ->
                tuiClient.getConnection().sendMessage(MessageTranslator.sendAction(Action.bid(-1))));

        return panel;
    }

    private String biddingToString(String action) {
        if (action.equals("bid:p")) {
            return "Pass";
        }
        int num = Integer.parseInt(action.split(":")[1]);
        if (num == lastBid) {
            hold = true;
            return "Hold";
        }
        lastBid = num;
        hold = false;
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
        return null;
    }
}
