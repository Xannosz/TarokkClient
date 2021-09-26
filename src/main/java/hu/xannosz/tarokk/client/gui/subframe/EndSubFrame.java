package hu.xannosz.tarokk.client.gui.subframe;

import hu.xannosz.tarokk.client.gui.ConnectionsData;
import hu.xannosz.tarokk.client.gui.Event;
import hu.xannosz.tarokk.client.gui.frame.GameFrame;
import hu.xannosz.tarokk.client.network.NetworkHandler;
import hu.xannosz.tarokk.client.util.Util;
import hu.xannosz.veneos.core.html.HtmlComponent;

import static hu.xannosz.tarokk.client.gui.util.DataToComponent.createEndComponent;

public class EndSubFrame extends SubFrame {
    public EndSubFrame(NetworkHandler networkHandler, ConnectionsData connectionsData, GameFrame gameFrame) {
        super(networkHandler, connectionsData, gameFrame);
    }

    @Override
    public HtmlComponent updateComponent() {
        return createEndComponent(Util.getGameData(gameFrame.getGameId(), networkHandler.getLiveData()),
                networkHandler.getLiveData());
    }

    @Override
    public void handleEvent(Event event) {

    }
}
