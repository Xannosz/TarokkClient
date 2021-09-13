package hu.xannosz.tarokk.client.gui.frame;

import hu.xannosz.tarokk.client.gui.ConnectionsData;
import hu.xannosz.tarokk.client.gui.Event;
import hu.xannosz.tarokk.client.network.NetworkHandler;
import hu.xannosz.veneos.core.html.structure.Page;

public class GameFrame extends Frame{
    public GameFrame(NetworkHandler networkHandler, ConnectionsData connectionsData,int gameId) {
        super(networkHandler, connectionsData);
    }

    @Override
    public Page updatePage() {
        return null;
    }

    @Override
    public void handleEvent(Event event) {

    }
}
