package hu.xannosz.tarokk.client.gui.frame;

import hu.xannosz.tarokk.client.game.GamePhase;
import hu.xannosz.tarokk.client.gui.ConnectionsData;
import hu.xannosz.tarokk.client.gui.Event;
import hu.xannosz.tarokk.client.gui.subframe.SubFrame;
import hu.xannosz.tarokk.client.network.NetworkHandler;
import hu.xannosz.veneos.core.html.structure.Page;

import java.util.HashMap;
import java.util.Map;

import static hu.xannosz.tarokk.client.gui.GuiConstants.CANCEL_EVENT_ID;

public class GameFrame extends Frame {

    private final int gameId;
    private SubFrame subFrame;
    private final Map<GamePhase, SubFrame> subFrames = new HashMap<>();

    public GameFrame(NetworkHandler networkHandler, ConnectionsData connectionsData, int gameId) {
        super(networkHandler, connectionsData);
        this.gameId = gameId;
    }

    @Override
    public Page updatePage() {
        return null;
    }

    @Override
    public void handleEvent(Event event) {
        if (event.getEventId().equals(CANCEL_EVENT_ID)) {
            connectionsData.setFrame(new LobbyFrame(networkHandler, connectionsData));
        }
        subFrame.handleEvent(event);
    }
}
