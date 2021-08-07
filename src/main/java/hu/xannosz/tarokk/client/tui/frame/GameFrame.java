package hu.xannosz.tarokk.client.tui.frame;

import com.googlecode.lanterna.gui2.Borders;
import com.googlecode.lanterna.gui2.GridLayout;
import com.googlecode.lanterna.gui2.Panel;
import com.googlecode.lanterna.input.KeyStroke;
import com.googlecode.lanterna.input.KeyType;
import com.tisza.tarock.proto.EventProto;
import com.tisza.tarock.proto.MainProto;
import hu.xannosz.tarokk.client.game.GamePhase;
import hu.xannosz.tarokk.client.tui.TuiClient;
import hu.xannosz.tarokk.client.tui.panel.CardPanel;
import hu.xannosz.tarokk.client.tui.panel.DataPanel;
import hu.xannosz.tarokk.client.tui.panel.HudPanel;
import hu.xannosz.tarokk.client.tui.panel.StatisticPanel;
import hu.xannosz.tarokk.client.tui.subframe.*;
import hu.xannosz.tarokk.client.util.Util;
import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

import static hu.xannosz.tarokk.client.util.Util.getGameData;

public class GameFrame extends Frame {

    @Getter
    private final int gameId;
    private SubFrame subFrame;
    private MainProto.GameSession gameData;
    private final Map<GamePhase, SubFrame> subFrames = new HashMap<>();

    public GameFrame(TuiClient tuiClient, int gameId) {
        super(tuiClient);
        this.gameId = gameId;
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

        updateFooter();

        frame.addComponent(new CardPanel(tuiClient).withBorder(Borders.singleLine(" Cards ")));
        frame.addComponent(new DataPanel(tuiClient, gameData).withBorder(Borders.singleLine(" Data ")));

        EventProto.Event.Statistics statistic = tuiClient.getServerLiveData().getStatistics();
        if (!Util.anyNull(statistic)) {
            frame.setLayoutManager(new GridLayout(3));
            frame.addComponent(new StatisticPanel(tuiClient, statistic));
        }

        gameData = getGameData(gameId, tuiClient);

        if (Util.anyNull(gameData)) {
            return;
        }

        if (gameData.getState().equals(MainProto.GameSession.State.LOBBY)) {
            subFrame = new StartGameSubFrame(tuiClient, gameId);
        } else {
            if (Util.anyNull(tuiClient.getServerLiveData().getPhase())) {
                return;
            }

            subFrames.computeIfAbsent(GamePhase.BIDDING, k -> new BiddingSubFrame(tuiClient, gameId));
            subFrames.computeIfAbsent(GamePhase.FOLDING, k -> new FoldingSubFrame(tuiClient, gameId));
            subFrames.computeIfAbsent(GamePhase.CALLING, k -> new CallingSubFrame(tuiClient));
            subFrames.computeIfAbsent(GamePhase.ANNOUNCING, k -> new AnnouncingSubFrame(tuiClient, gameId));
            subFrames.computeIfAbsent(GamePhase.GAMEPLAY, k -> new GamePlaySubFrame(tuiClient, gameId));
            subFrames.computeIfAbsent(GamePhase.END, k -> new EndSubFrame(tuiClient, gameId));

            subFrame = subFrames.get(tuiClient.getServerLiveData().getPhase());
        }

        frame.addComponent(subFrame.getPanel().withBorder(Borders.singleLine(" " + (tuiClient.getServerLiveData().getPhase() == null ? "Lobby" : tuiClient.getServerLiveData().getPhase().getName()) + " ")));
        frame.addComponent(new HudPanel(tuiClient, gameData).withBorder(Borders.singleLine(" Hud ")));
    }

    private void updateFooter() {
        footer = new Panel();

        if (Util.anyNull(subFrame)) {
            return;
        }

        Map<String, String> subFrameFooter = subFrame.getFooter();

        footer.setLayoutManager(new GridLayout(3 + subFrameFooter.size() * 3));

        Util.addKey(footer, "/", "Back to Lobby", tuiClient);

        for (Map.Entry<String, String> entry : subFrameFooter.entrySet()) {
            Util.addKey(footer, entry.getKey(), entry.getValue(), tuiClient);
        }
    }
}