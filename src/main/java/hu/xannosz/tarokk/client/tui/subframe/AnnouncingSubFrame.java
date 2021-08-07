package hu.xannosz.tarokk.client.tui.subframe;

import com.googlecode.lanterna.gui2.Component;
import com.googlecode.lanterna.gui2.GridLayout;
import com.googlecode.lanterna.gui2.Label;
import com.googlecode.lanterna.gui2.Panel;
import com.googlecode.lanterna.input.KeyStroke;
import com.googlecode.lanterna.input.KeyType;
import com.tisza.tarock.proto.MainProto;
import hu.xannosz.microtools.pack.Douplet;
import hu.xannosz.tarokk.client.game.GamePhase;
import hu.xannosz.tarokk.client.network.Action;
import hu.xannosz.tarokk.client.tui.TuiClient;
import hu.xannosz.tarokk.client.util.MessageTranslator;
import hu.xannosz.tarokk.client.util.ThemeHandler;
import hu.xannosz.tarokk.client.util.Util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static hu.xannosz.tarokk.client.util.Util.*;

public class AnnouncingSubFrame extends SubFrame {
    private static int page = 0; //TODO remove static
    private List<String> availableAnnouncing;
    private final int gameId;

    public AnnouncingSubFrame(TuiClient tuiClient, int gameId) {
        super(tuiClient);
        this.gameId = gameId;
    }

    @Override
    public Component getPanel() {
        MainProto.GameSession gameData = getGameData(gameId, tuiClient);
        Panel panel = new Panel();
        panel.setLayoutManager(new GridLayout(4));

        if (Util.anyNull(tuiClient.getServerLiveData().getAvailableAnnouncements())) {
            return panel;
        }

        availableAnnouncing = new ArrayList<>(tuiClient.getServerLiveData().getAvailableAnnouncements());

        if (tuiClient.getServerLiveData().getPlayerActions().get(GamePhase.ANNOUNCING) != null) {
            for (Douplet<Integer, String> announce : tuiClient.getServerLiveData().getPlayerActions().get(GamePhase.ANNOUNCING)) {
                addData(panel, getPlayerName(announce.getFirst(), gameData, tuiClient) + " announce", announce.getSecond().replace("announce:", ""), tuiClient);
            }
        }

        panel.addComponent(new Label("")); //TODO empty line
        panel.addComponent(new Label(""));
        panel.addComponent(new Label(""));
        panel.addComponent(new Label(""));

        for (int i = 0; i < availableAnnouncing.size(); i++) {
            if (i == page) {
                panel.addComponent(new Label(availableAnnouncing.get(i)).setTheme(ThemeHandler.getHighLightedThemeMainPanel(tuiClient.getTerminalSettings())));
            } else {
                panel.addComponent(new Label(availableAnnouncing.get(i)));
            }
        }
        return panel;
    }

    @Override
    public Map<String, String> getFooter() {
        Map<String, String> response = new HashMap<>();
        response.put("Arrows", "Movement");
        response.put("Enter", "Send calling");
        response.put("-", "Pass");
        return response;
    }

    @Override
    public void handleKeyStroke(KeyStroke keyStroke) {
        if (keyStroke.getKeyType().equals(KeyType.ArrowUp) || keyStroke.getKeyType().equals(KeyType.ArrowLeft)) {
            page--;
            page = Math.max(page, 0);
        }
        if (keyStroke.getKeyType().equals(KeyType.ArrowDown) || keyStroke.getKeyType().equals(KeyType.ArrowRight)) {
            page++;
            page = Math.min(page, availableAnnouncing.size() - 1);
        }
        if (keyStroke.getKeyType().equals(KeyType.Enter)) {
            tuiClient.getConnection().sendMessage(MessageTranslator.sendAction(Action.announce(availableAnnouncing.get(page))));
        }
        if (keyStroke.getKeyType().equals(KeyType.Character)) {
            if (keyStroke.getCharacter().equals('-')) {
                tuiClient.getConnection().sendMessage(MessageTranslator.sendAction(Action.announcePass()));
            }
        }
    }
}
