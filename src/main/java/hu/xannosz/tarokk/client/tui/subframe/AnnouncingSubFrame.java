package hu.xannosz.tarokk.client.tui.subframe;

import com.googlecode.lanterna.gui2.Component;
import com.googlecode.lanterna.gui2.GridLayout;
import com.googlecode.lanterna.gui2.Label;
import com.googlecode.lanterna.gui2.Panel;
import com.googlecode.lanterna.input.KeyStroke;
import com.googlecode.lanterna.input.KeyType;
import com.tisza.tarock.proto.MainProto;
import hu.xannosz.microtools.pack.Douplet;
import hu.xannosz.tarokk.client.network.Action;
import hu.xannosz.tarokk.client.tui.TuiClient;
import hu.xannosz.tarokk.client.network.Messages;
import hu.xannosz.tarokk.client.util.ThemeHandler;
import hu.xannosz.tarokk.client.util.Util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static hu.xannosz.tarokk.client.util.Util.*;

public class AnnouncingSubFrame extends SubFrame {
    private int page = 0;
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

        if (Util.anyNull(tuiClient.getServerLiveData().getAvailableAnnouncements())) {
            return panel;
        }

        availableAnnouncing = new ArrayList<>(tuiClient.getServerLiveData().getAvailableAnnouncements());

        resetPager();

        Panel playerAnnouncing = new Panel();
        playerAnnouncing.setLayoutManager(new GridLayout(4));
        if (tuiClient.getServerLiveData().getPlayerActions().get("announce") != null) {
            for (Douplet<Integer, String> announce : tuiClient.getServerLiveData().getPlayerActions().get("announce")) {
                addData(playerAnnouncing, getPlayerName(announce.getFirst(), gameData, tuiClient) + " announce", announce.getSecond(), tuiClient);
            }
        }
        panel.addComponent(playerAnnouncing);

        Panel nextAnnouncing = new Panel();
        nextAnnouncing.setLayoutManager(new GridLayout(4));
        for (int i = 0; i < availableAnnouncing.size(); i++) {
            if (i == page) {
                nextAnnouncing.addComponent(new Label(availableAnnouncing.get(i)).setTheme(ThemeHandler.getHighLightedThemeMainPanel(tuiClient.getTerminalSettings())));
            } else {
                nextAnnouncing.addComponent(new Label(availableAnnouncing.get(i)));
            }
        }
        panel.addComponent(nextAnnouncing);

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
        }
        if (keyStroke.getKeyType().equals(KeyType.ArrowDown) || keyStroke.getKeyType().equals(KeyType.ArrowRight)) {
            page++;
        }
        resetPager();
        if (keyStroke.getKeyType().equals(KeyType.Enter)) {
            tuiClient.getConnection().sendMessage(Messages.sendAction(Action.announce(availableAnnouncing.get(page))));
        }
        if (keyStroke.getKeyType().equals(KeyType.Character)) {
            if (keyStroke.getCharacter().equals('-')) {
                tuiClient.getConnection().sendMessage(Messages.sendAction(Action.announcePass()));
            }
        }
    }

    private void resetPager() {
        page = Math.max(page, 0);
        page = Math.min(page, availableAnnouncing.size() - 1);
    }
}
