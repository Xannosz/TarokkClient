package hu.xannosz.tarokk.client.gui.frame;

import hu.xannosz.tarokk.client.gui.ConnectionsData;
import hu.xannosz.tarokk.client.gui.Event;
import hu.xannosz.tarokk.client.network.NetworkHandler;
import hu.xannosz.veneos.core.html.structure.Page;

public class NewGameFrame extends Frame{
    public NewGameFrame(NetworkHandler networkHandler, ConnectionsData connectionsData) {
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
