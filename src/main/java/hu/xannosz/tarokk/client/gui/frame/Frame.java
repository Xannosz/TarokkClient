package hu.xannosz.tarokk.client.gui.frame;

import hu.xannosz.tarokk.client.gui.ConnectionsData;
import hu.xannosz.tarokk.client.gui.Event;
import hu.xannosz.tarokk.client.network.NetworkHandler;
import hu.xannosz.veneos.core.html.structure.Page;

public abstract class Frame {
    protected final NetworkHandler networkHandler;
    protected final ConnectionsData connectionsData;

    public Frame(NetworkHandler networkHandler, ConnectionsData connectionsData) {
        this.networkHandler = networkHandler;
        this.connectionsData = connectionsData;
    }

    public abstract Page updatePage();

    public abstract void handleEvent(Event event);
}
