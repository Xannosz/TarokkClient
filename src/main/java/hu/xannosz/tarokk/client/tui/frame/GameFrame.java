package hu.xannosz.tarokk.client.tui.frame;

import com.googlecode.lanterna.gui2.*;
import com.googlecode.lanterna.input.KeyStroke;
import com.googlecode.lanterna.input.KeyType;
import com.tisza.tarock.proto.EventProto;
import com.tisza.tarock.proto.MainProto;
import hu.xannosz.microtools.pack.Douplet;
import hu.xannosz.tarokk.client.game.Card;
import hu.xannosz.tarokk.client.game.GamePhase;
import hu.xannosz.tarokk.client.tui.TuiClient;
import hu.xannosz.tarokk.client.tui.subframe.*;
import hu.xannosz.tarokk.client.util.ThemeHandler;
import hu.xannosz.tarokk.client.util.Util;
import lombok.Getter;

import java.util.Map;

import static hu.xannosz.tarokk.client.util.Util.*;

public class GameFrame extends Frame {

    @Getter
    private final int gameId;
    private Panel frame;
    private SubFrame subFrame;
    private MainProto.GameSession gameData;

    public GameFrame(TuiClient tuiClient, int gameId) {
        super(tuiClient);
        this.gameId = gameId;
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

        gameData = getGameData(gameId, tuiClient);

        if (gameData.getState().equals(MainProto.GameSession.State.LOBBY)) {
            subFrame = new StartGameSubFrame(tuiClient, gameId);
        } else {
            switch (tuiClient.getServerLiveData().getPhase()) {
                case BIDDING:
                    subFrame = new BiddingSubFrame(tuiClient, gameId);
                    break;
                case FOLDING:
                    subFrame = new FoldingSubFrame(tuiClient, gameId);
                    break;
                case CALLING:
                    subFrame = new CallingSubFrame(tuiClient);
                    break;
                case ANNOUNCING:
                    subFrame = new AnnouncingSubFrame(tuiClient, gameId);
                    break;
                case GAMEPLAY:
                    subFrame = new GamePlaySubFrame(tuiClient, gameId);
                    break;
                case END:
                    subFrame = new EndSubFrame(tuiClient, gameId);
                    break;
            }
        }

        frame.addComponent(subFrame.getPanel().withBorder(Borders.singleLine(" " + tuiClient.getServerLiveData().getPhase().getName() + " ")));
        frame.addComponent(createHudPanel().withBorder(Borders.singleLine(" Hud ")));
    }

    private Panel createCardPanel() {
        Panel panel = new Panel();
        panel.setLayoutManager(new GridLayout(3));
        for (Card card : tuiClient.getServerLiveData().getPlayerCard()) {
            panel.addComponent(new Label(card.getFormattedName()).setTheme(ThemeHandler.getHighLightedThemeMainPanel(tuiClient.getTerminalSettings())));
        }
        return panel;
    }

    private Panel createDatePanel() {
        Panel data = new Panel();
        data.setLayoutManager(new GridLayout(2));
        addData(data, "Game Type", tuiClient.getServerLiveData().getGameType(), tuiClient);
        addData(data, "Beginner Player", getPlayerName(tuiClient.getServerLiveData().getBeginnerPlayer(), gameData, tuiClient), tuiClient);
        addData(data, "Phase", tuiClient.getServerLiveData().getPhase().getName(), tuiClient);
        addData(data, "Player in Turn", getPlayerName(tuiClient.getServerLiveData().getPlayerTurn(), gameData, tuiClient), tuiClient);
        if (tuiClient.getServerLiveData().getPlayerActions().get(GamePhase.FOLDING) != null) {
            for (Douplet<Integer, String> folded : tuiClient.getServerLiveData().getPlayerActions().get(GamePhase.FOLDING)) {
                addData(data, getPlayerName(folded.getFirst(), gameData, tuiClient) + " folded tarock", getFormattedCardName(folded.getSecond().replace("fold:", "")), tuiClient);
            }
        }
        for (Map.Entry<Integer, Integer> skartedTarock : tuiClient.getServerLiveData().getSkartedTarocks().entrySet()) {
            addData(data, getPlayerName(skartedTarock.getKey(), gameData, tuiClient) + " folded tarock", "" + skartedTarock.getValue(), tuiClient);
        }
        if (tuiClient.getServerLiveData().getPlayerActions().get(GamePhase.CALLING) != null) {
            for (Douplet<Integer, String> call : tuiClient.getServerLiveData().getPlayerActions().get(GamePhase.CALLING)) {
                addData(data, getPlayerName(call.getFirst(), gameData, tuiClient) + " called tarock", getFormattedCardName(call.getSecond().replace("call:", "")), tuiClient);
            }
        }

        for (Map.Entry<Integer, Integer> entry : tuiClient.getServerLiveData().getCardsTakenUsers().entrySet()) {
            addData(data, getPlayerName(entry.getKey(), gameData, tuiClient) + " taken cards", "" + entry.getValue(), tuiClient);
        }

        EventProto.Event.Statistics statistic = tuiClient.getServerLiveData().getStatistics();
        addData(data, "caller game points", "" + statistic.getCallerGamePoints(), tuiClient);
        addData(data, "opponent game points", "" + statistic.getOpponentGamePoints(), tuiClient);
        addData(data, "sum points", "" + statistic.getSumPoints(), tuiClient);
        addData(data, "point multiplier", "" + statistic.getPointMultiplier(), tuiClient);

        for (EventProto.Event.Statistics.AnnouncementResult stat : statistic.getAnnouncementResultList()) {
            Panel panel = new Panel();
            addData(data, "announcement", stat.getAnnouncement(), tuiClient);
            addData(data, "points", "" + stat.getPoints(), tuiClient);
            addData(data, "caller team", "" + stat.getCallerTeam(), tuiClient);
            data.addComponent(panel.withBorder(Borders.singleLine()));
        }

        return data;
    }

    private Panel createHudPanel() {
        Panel panel = new Panel();
        panel.setLayoutManager(new GridLayout(2));
        for (Map.Entry<Integer, Boolean> info : tuiClient.getServerLiveData().getPlayerTeamInfo().entrySet()) {
            addData(panel, getPlayerName(info.getKey(), gameData, tuiClient) + " is caller:", "" + info.getValue(), tuiClient);
        }
        return panel;
    }
}