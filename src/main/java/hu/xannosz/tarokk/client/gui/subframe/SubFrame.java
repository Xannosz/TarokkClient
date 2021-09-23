package hu.xannosz.tarokk.client.gui.subframe;

import hu.xannosz.tarokk.client.gui.ConnectionsData;
import hu.xannosz.tarokk.client.gui.Event;
import hu.xannosz.tarokk.client.gui.frame.GameFrame;
import hu.xannosz.tarokk.client.network.NetworkHandler;
import hu.xannosz.veneos.core.html.HtmlComponent;

public abstract class SubFrame {
    protected final NetworkHandler networkHandler;
    protected final ConnectionsData connectionsData;
    protected final GameFrame gameFrame;

    public SubFrame(NetworkHandler networkHandler, ConnectionsData connectionsData, GameFrame gameFrame) {
        this.networkHandler = networkHandler;
        this.connectionsData = connectionsData;
        this.gameFrame = gameFrame;
    }

    public abstract HtmlComponent updateComponent();

    public abstract void handleEvent(Event event);
}
