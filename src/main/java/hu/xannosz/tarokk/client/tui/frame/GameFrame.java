package hu.xannosz.tarokk.client.tui.frame;

import com.googlecode.lanterna.gui2.*;
import com.googlecode.lanterna.input.KeyStroke;
import com.googlecode.lanterna.input.KeyType;
import hu.xannosz.microtools.pack.Douplet;
import hu.xannosz.tarokk.client.game.GamePhase;
import hu.xannosz.tarokk.client.tui.TuiClient;
import hu.xannosz.tarokk.client.tui.subframe.BiddingSubFrame;
import hu.xannosz.tarokk.client.tui.subframe.CallingSubFrame;
import hu.xannosz.tarokk.client.tui.subframe.FoldingSubFrame;
import hu.xannosz.tarokk.client.tui.subframe.SubFrame;
import hu.xannosz.tarokk.client.util.ThemeHandler;
import hu.xannosz.tarokk.client.util.Util;

import java.util.Map;

import static hu.xannosz.tarokk.client.util.Util.*;

public class GameFrame extends Frame {
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
        Map<String, String> subFrameFooter = subFrame.getFooter();

        Panel panel = new Panel();
        panel.setLayoutManager(new GridLayout(3 + subFrameFooter.size() * 3));

        Util.addKey(panel, "/", "Back to Lobby", tuiClient);

        for (Map.Entry<String, String> entry : subFrameFooter.entrySet()) {
            Util.addKey(panel, entry.getKey(), entry.getValue(), tuiClient);
        }

        return panel;
    }

    @Override
    public void handleKeyStroke(KeyStroke keyStroke) {
        if (keyStroke.getKeyType().equals(KeyType.Character) && keyStroke.getCharacter().equals('/')) {
            tuiClient.setFrame(new LobbyFrame(tuiClient));
        } else {
            subFrame.handleKeyStroke(keyStroke);
        }
        update();
        tuiClient.redraw();
    }

    @Override
    public void update() {
        frame = new Panel();
        frame.setLayoutManager(new GridLayout(2));

        if (Util.anyNull(tuiClient.getServerLiveData().getPhase())) {
            return;
        }

        frame.addComponent(createCardPanel().withBorder(Borders.singleLine(" Cards ")));
        frame.addComponent(createDatePanel().withBorder(Borders.singleLine(" Data ")));

        switch (tuiClient.getServerLiveData().getPhase()) {
            case BIDDING:
                subFrame = new BiddingSubFrame(tuiClient);
                break;
            case FOLDING:
                subFrame = new FoldingSubFrame(tuiClient);
                break;
            case CALLING:
                subFrame = new CallingSubFrame(tuiClient);
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
        for (String card : tuiClient.getServerLiveData().getPlayerCardIds()) {
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
            for (Douplet<Integer, String> folded : tuiClient.getServerLiveData().getPlayerActions().get(GamePhase.FOLDING)) {
                addData(data, getPlayerName(folded.getFirst()) + " folded tarock", getFormattedCardName(folded.getSecond().replace("fold:", "")), tuiClient);
            }
        }
        for (Map.Entry<Integer, Integer> skartedTarock : tuiClient.getServerLiveData().getSkartedTarocks().entrySet()) {
            addData(data, getPlayerName(skartedTarock.getKey()) + " folded tarock", "" + skartedTarock.getValue(), tuiClient);
        }
        return data;
    }

    private Panel createHudPanel() { //TODO
        return new Panel();
    }
}