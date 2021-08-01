package hu.xannosz.tarokk.client.tui;

import com.googlecode.lanterna.gui2.*;
import com.googlecode.lanterna.input.KeyStroke;
import hu.xannosz.microtools.pack.Douplet;
import hu.xannosz.tarokk.client.game.GamePhase;
import hu.xannosz.tarokk.client.network.Action;
import hu.xannosz.tarokk.client.util.MessageTranslator;
import hu.xannosz.tarokk.client.util.ThemeHandler;
import hu.xannosz.tarokk.client.util.Util;

public class GameFrame extends Frame {
    private final KeyMapDictionary keyMapDictionary = new KeyMapDictionary();
    private Panel frame;

    //bidding
    private int lastBid = 4;
    private boolean hold;

    public GameFrame(TuiClient tuiClient) {
        super(tuiClient);
    }

    @Override
    public Component getPanel() {
        return frame;
    }

    @Override
    public Component getFooter() {
        return Util.formatActions(tuiClient.getTerminalSettings(), keyMapDictionary);
    }

    @Override
    public void handleKeyStroke(KeyStroke keyStroke) {
        keyMapDictionary.runFunction(keyStroke.getCharacter().toString());
        update();
        tuiClient.redraw();
    }

    @Override
    public void update() {
        frame = new Panel();
        frame.setLayoutManager(new GridLayout(2));

        if (Util.anyNull(tuiClient.getServerLiveData().getPhase(),keyMapDictionary)) {
            return;
        }
        keyMapDictionary.clear();

        frame.addComponent(createCardPanel().withBorder(Borders.singleLine(" Cards ")));
        frame.addComponent(createDatePanel().withBorder(Borders.singleLine(" Data ")));

        switch (tuiClient.getServerLiveData().getPhase()) {
            case BIDDING:
                frame.addComponent(createBiddingFrame().withBorder(Borders.singleLine(tuiClient.getServerLiveData().getPhase().getName())));
                break;
            case FOLDING:
                frame.addComponent(createFoldingFrame().withBorder(Borders.singleLine(tuiClient.getServerLiveData().getPhase().getName())));
                break;
            case CALLING:
                frame.addComponent(createCallingFrame().withBorder(Borders.singleLine(tuiClient.getServerLiveData().getPhase().getName())));
                break;
            case ANNOUNCING:
                frame.addComponent(createAnnouncingFrame().withBorder(Borders.singleLine(tuiClient.getServerLiveData().getPhase().getName())));
                break;
            case GAMEPLAY:
                frame.addComponent(createGamePlayFrame().withBorder(Borders.singleLine(tuiClient.getServerLiveData().getPhase().getName())));
                break;
            case END:
                frame.addComponent(createEndFrame().withBorder(Borders.singleLine(tuiClient.getServerLiveData().getPhase().getName())));
                break;
        }

        frame.addComponent(createHudPanel().withBorder(Borders.singleLine(" Hud ")));
    }

    private Panel createCardPanel() {
        Panel panel = new Panel();
        panel.setLayoutManager(new GridLayout(3));
        for (String card : tuiClient.getServerLiveData().getPlayerCards()) {
            panel.addComponent(new Label(getFormattedCardName(card)).setTheme(ThemeHandler.getHighLightedThemeMainPanel(tuiClient.getTerminalSettings())));
        }
        return panel;
    }

    private String getFormattedCardName(String card) {
        return card;
    }

    private Panel createDatePanel() {
        Panel data = new Panel();
        data.setLayoutManager(new GridLayout(2));
        addData(data, "Game Type", tuiClient.getServerLiveData().getGameType());
        addData(data, "Beginner Player", getPlayerName(tuiClient.getServerLiveData().getBeginnerPlayer()));
        addData(data, "Phase", tuiClient.getServerLiveData().getPhase().getName());
        addData(data, "Player in Turn", getPlayerName(tuiClient.getServerLiveData().getPlayerTurn()));

        return data;
    }

    private Panel createHudPanel() { //TODO
        return new Panel();
    }

    private void addData(Panel data, String name, String value) {
        data.addComponent(new Label(name + ":"));
        data.addComponent(new Label(value).setTheme(ThemeHandler.getHighLightedThemeMainPanel(tuiClient.getTerminalSettings())));
    }

    private Panel createBiddingFrame() {
        Panel panel = new Panel();
        for (Douplet<Integer, String> action : tuiClient.getServerLiveData().getPlayerActions().get(GamePhase.BIDDING)) {
            addData(panel, getPlayerName(action.getFirst()), biddingToString(action.getSecond()));
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

    private Panel createFoldingFrame() {
        Panel panel = new Panel();

        return panel;
    }

    private Panel createCallingFrame() {
        return null;
    }

    private Panel createAnnouncingFrame() {
        return null;
    }

    private Panel createGamePlayFrame() {
        return null;
    }

    private Panel createEndFrame() {
        return null;
    }

    private String getPlayerName(int playerId) {
        return "player:" + playerId;
    }
}
