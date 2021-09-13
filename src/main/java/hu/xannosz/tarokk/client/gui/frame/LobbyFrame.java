package hu.xannosz.tarokk.client.gui.frame;

import com.tisza.tarock.proto.MainProto;
import hu.xannosz.tarokk.client.gui.ConnectionsData;
import hu.xannosz.tarokk.client.gui.Event;
import hu.xannosz.tarokk.client.gui.util.PageCreator;
import hu.xannosz.tarokk.client.network.NetworkHandler;
import hu.xannosz.veneos.core.html.structure.Page;

import java.util.ArrayList;
import java.util.List;

import static hu.xannosz.tarokk.client.gui.GuiConstants.*;

public class LobbyFrame extends Frame {

    private int selectedGame = 0;
    private List<MainProto.GameSession> gameSessions;

    public LobbyFrame(NetworkHandler networkHandler, ConnectionsData connectionsData) {
        super(networkHandler, connectionsData);
    }

    @Override
    public Page updatePage() {
        gameSessions = new ArrayList<>(networkHandler.getLiveData().getGameSessions());
        return PageCreator.createLobbyPage(gameSessions, selectedGame,
                networkHandler.getLiveData().getUsers(), networkHandler.getLiveData().getLoginResult().getUserId());
    }

    @Override
    public void handleEvent(Event event) {
        if (event.getEventId().equals(ARROW_UP_KEY_CODE)) {
            selectedGame--;
        }
        if (event.getEventId().equals(ARROW_DOWN_KEY_CODE)) {
            selectedGame++;
        }

        if (event.getEventId().equals(ENTER_KEY_CODE)) {
            networkHandler.joinToGame(gameSessions.get(selectedGame).getId());
            connectionsData.setFrame(new GameFrame(networkHandler, connectionsData, gameSessions.get(selectedGame).getId()));
        }
        if (event.getEventId().equals(PLUS_KEY_CODE)) {
            connectionsData.setFrame(new NewGameFrame(networkHandler, connectionsData));
        }
        if (event.getEventId().equals(MINUS_KEY_CODE)) {
            networkHandler.deleteToGame(gameSessions.get(selectedGame).getId());
        }

        if (event.getEventId().equals(JOIN_GAME_EVENT_ID)) {
            networkHandler.joinToGame((Integer) event.getAdditionalParams().get(GAME_ID));
            connectionsData.setFrame(new GameFrame(networkHandler, connectionsData, (Integer) event.getAdditionalParams().get(GAME_ID)));
        }
        if (event.getEventId().equals(CREATE_GAME_EVENT_ID)) {
            connectionsData.setFrame(new NewGameFrame(networkHandler, connectionsData));
        }
        if (event.getEventId().equals(DELETE_GAME_EVENT_ID)) {
            networkHandler.deleteToGame((Integer) event.getAdditionalParams().get(GAME_ID));
        }
    }
}
