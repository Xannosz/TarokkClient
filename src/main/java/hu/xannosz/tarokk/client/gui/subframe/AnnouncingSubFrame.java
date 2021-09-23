package hu.xannosz.tarokk.client.gui.subframe;

import hu.xannosz.tarokk.client.gui.ConnectionsData;
import hu.xannosz.tarokk.client.gui.Event;
import hu.xannosz.tarokk.client.gui.frame.GameFrame;
import hu.xannosz.tarokk.client.network.NetworkHandler;
import hu.xannosz.veneos.core.html.HtmlComponent;

public class AnnouncingSubFrame extends SubFrame{
    public AnnouncingSubFrame(NetworkHandler networkHandler, ConnectionsData connectionsData, GameFrame gameFrame) {
        super(networkHandler, connectionsData, gameFrame);
    }

    @Override
    public HtmlComponent updateComponent() {
        return null;
    }

    @Override
    public void handleEvent(Event event) {

    }
}
