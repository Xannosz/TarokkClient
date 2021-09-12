package hu.xannosz.tarokk.client.gui.frame;

import hu.xannosz.tarokk.client.gui.ConnectionsData;
import hu.xannosz.tarokk.client.gui.Event;
import hu.xannosz.tarokk.client.gui.GuiConstants;
import hu.xannosz.tarokk.client.gui.util.PageCreator;
import hu.xannosz.tarokk.client.network.NetworkHandler;
import hu.xannosz.veneos.core.html.structure.Page;

public class LoginFrame extends Frame {
    public LoginFrame(NetworkHandler networkHandler, ConnectionsData connectionsData) {
        super(networkHandler, connectionsData);
    }

    @Override
    public Page updatePage() {
        return PageCreator.createLoginPage();
    }

    @Override
    public void handleEvent(Event event) {
        if (event.getEventId().equals(GuiConstants.LOGIN_EVENT_ID)) {
            connectionsData.setFrame(new LobbyFrame(networkHandler, connectionsData));
        }
    }
}
