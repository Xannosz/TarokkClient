package hu.xannosz.tarokk.client.tui.frame;

import com.googlecode.lanterna.gui2.*;
import com.googlecode.lanterna.input.KeyStroke;
import hu.xannosz.microtools.pack.Douplet;
import hu.xannosz.tarokk.client.game.GamePhase;
import hu.xannosz.tarokk.client.tui.KeyMapDictionary;
import hu.xannosz.tarokk.client.tui.TuiClient;
import hu.xannosz.tarokk.client.tui.subframe.BiddingSubFrame;
import hu.xannosz.tarokk.client.tui.subframe.FoldingSubFrame;
import hu.xannosz.tarokk.client.tui.subframe.SubFrame;
import hu.xannosz.tarokk.client.util.ThemeHandler;
import hu.xannosz.tarokk.client.util.Util;

import static hu.xannosz.tarokk.client.util.Util.*;

public class GameFrame extends Frame {
    private final KeyMapDictionary keyMapDictionary = new KeyMapDictionary();
    private Panel frame;
    private SubFrame subFrame;

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

        if (Util.anyNull(tuiClient.getServerLiveData().getPhase(), keyMapDictionary)) {
            return;
        }
        keyMapDictionary.clear(); //TODO move up if super update call removed

        frame.addComponent(createCardPanel().withBorder(Borders.singleLine(" Cards ")));
        frame.addComponent(createDatePanel().withBorder(Borders.singleLine(" Data ")));

        switch (tuiClient.getServerLiveData().getPhase()) {
            case BIDDING:
                subFrame = new BiddingSubFrame(tuiClient, keyMapDictionary);
                break;
            case FOLDING:
                subFrame = new FoldingSubFrame(tuiClient, keyMapDictionary);
                break;
            case CALLING:

                break;
            case ANNOUNCING:

                break;
            case GAMEPLAY:

                break;
            case END:

                break;
        }

        frame.addComponent(subFrame.getPanel().withBorder(Borders.singleLine(" " + tuiClient.getServerLiveData().getPhase().getName() + " ")));
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

    private Panel createDatePanel() {
        Panel data = new Panel();
        data.setLayoutManager(new GridLayout(2));
        addData(data, "Game Type", tuiClient.getServerLiveData().getGameType(), tuiClient);
        addData(data, "Beginner Player", getPlayerName(tuiClient.getServerLiveData().getBeginnerPlayer()), tuiClient);
        addData(data, "Phase", tuiClient.getServerLiveData().getPhase().getName(), tuiClient);
        addData(data, "Player in Turn", getPlayerName(tuiClient.getServerLiveData().getPlayerTurn()), tuiClient);
        if (tuiClient.getServerLiveData().getPlayerActions().get(GamePhase.FOLDING) != null) {
            for (Douplet<Integer, String> folded:tuiClient.getServerLiveData().getPlayerActions().get(GamePhase.FOLDING)) {
                addData(data, getPlayerName(folded.getFirst())+" folded tarock", getFormattedCardName(folded.getSecond().replace("fold:","")), tuiClient);
            }
        }
        return data;
    }

    private Panel createHudPanel() { //TODO
        return new Panel();
    }
}