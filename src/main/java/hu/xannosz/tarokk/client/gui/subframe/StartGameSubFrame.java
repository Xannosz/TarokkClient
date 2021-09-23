package hu.xannosz.tarokk.client.gui.subframe;

import hu.xannosz.tarokk.client.gui.ConnectionsData;
import hu.xannosz.tarokk.client.gui.Event;
import hu.xannosz.tarokk.client.gui.GuiConstants;
import hu.xannosz.tarokk.client.gui.frame.GameFrame;
import hu.xannosz.tarokk.client.network.NetworkHandler;
import hu.xannosz.tarokk.client.util.Util;
import hu.xannosz.veneos.core.html.HtmlComponent;

import static hu.xannosz.tarokk.client.gui.util.DataToComponent.createStartGameComponent;

public class StartGameSubFrame extends SubFrame {
    public StartGameSubFrame(NetworkHandler networkHandler, ConnectionsData connectionsData, GameFrame gameFrame) {
        super(networkHandler, connectionsData, gameFrame);
    }

    @Override
    public HtmlComponent updateComponent() {
        return createStartGameComponent(Util.getGameData(gameFrame.getGameId(), networkHandler.getLiveData()),
                networkHandler.getLiveData());
    }

    @Override
    public void handleEvent(Event event) {
        if (event.getEventId().equals(GuiConstants.START_GAME_EVENT_ID)) {
            networkHandler.startGame();
        }
    }
}
