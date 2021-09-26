package hu.xannosz.tarokk.client.gui.subframe;

import hu.xannosz.microtools.Json;
import hu.xannosz.tarokk.client.game.Card;
import hu.xannosz.tarokk.client.gui.ConnectionsData;
import hu.xannosz.tarokk.client.gui.Event;
import hu.xannosz.tarokk.client.gui.GuiConstants;
import hu.xannosz.tarokk.client.gui.frame.GameFrame;
import hu.xannosz.tarokk.client.network.NetworkHandler;
import hu.xannosz.veneos.core.html.HtmlComponent;

import static hu.xannosz.tarokk.client.gui.util.DataToComponent.createCallingComponent;

public class CallingSubFrame extends SubFrame {
    public CallingSubFrame(NetworkHandler networkHandler, ConnectionsData connectionsData, GameFrame gameFrame) {
        super(networkHandler, connectionsData, gameFrame);
    }

    @Override
    public HtmlComponent updateComponent() {
        return createCallingComponent(networkHandler.getLiveData());
    }

    @Override
    public void handleEvent(Event event) {
        if (event.getEventId().equals(GuiConstants.CALLING_EVENT_ID)) {
            networkHandler.call(Json.castObjectToSpecificClass(event.getAdditionalParams().get(GuiConstants.CALLING_ID), Card.class));
        }
    }
}
