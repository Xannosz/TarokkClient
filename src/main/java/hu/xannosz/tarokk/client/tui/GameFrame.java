package hu.xannosz.tarokk.client.tui;

import com.googlecode.lanterna.gui2.*;
import com.googlecode.lanterna.input.KeyStroke;
import hu.xannosz.tarokk.client.game.GamePhase;
import hu.xannosz.tarokk.client.util.ThemeHandler;
import hu.xannosz.tarokk.client.util.Util;

public class GameFrame extends Frame {
    private final KeyMapDictionary keyMapDictionary = new KeyMapDictionary();
    private Panel frame;

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
        keyMapDictionary.clear();

        if (Util.anyNull(tuiClient.getServerLiveData().getPhase())) {
            return;
        }

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

        frame.addComponent(createDatePanel().withBorder(Borders.singleLine("Data Panel")));
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

    private void addData(Panel data, String name, String value) {
        data.addComponent(new Label(name + ":"));
        data.addComponent(new Label(value).setTheme(ThemeHandler.getHighLightedThemeMainPanel(tuiClient.getTerminalSettings())));
    }

    private Panel createBiddingFrame() {
        return null;
    }

    private Panel createFoldingFrame() {
        return null;
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
        return "player:"+playerId;
    }
}
