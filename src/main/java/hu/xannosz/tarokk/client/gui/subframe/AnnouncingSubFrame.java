package hu.xannosz.tarokk.client.gui.subframe;

import hu.xannosz.microtools.Json;
import hu.xannosz.tarokk.client.game.Announcement;
import hu.xannosz.tarokk.client.gui.ConnectionsData;
import hu.xannosz.tarokk.client.gui.Event;
import hu.xannosz.tarokk.client.gui.GuiConstants;
import hu.xannosz.tarokk.client.gui.frame.GameFrame;
import hu.xannosz.tarokk.client.network.NetworkHandler;
import hu.xannosz.tarokk.client.util.Util;
import hu.xannosz.veneos.core.html.HtmlComponent;

import static hu.xannosz.tarokk.client.gui.util.DataToComponent.createAnnouncingComponent;

public class AnnouncingSubFrame extends SubFrame {
    public AnnouncingSubFrame(NetworkHandler networkHandler, ConnectionsData connectionsData, GameFrame gameFrame) {
        super(networkHandler, connectionsData, gameFrame);
    }

    @Override
    public HtmlComponent updateComponent() {
        return createAnnouncingComponent(Util.getGameData(gameFrame.getGameId(), networkHandler.getLiveData()), networkHandler.getLiveData());
    }

    @Override
    public void handleEvent(Event event) {
        if (event.getEventId().equals(GuiConstants.ANNOUNCING_EVENT_ID)) {
            Announcement announcement = Json.castObjectToSpecificClass(event.getAdditionalParams().get(GuiConstants.ANNOUNCING_ID), Announcement.class);
            if (announcement == null) {
                networkHandler.announcePass();
            } else {
                networkHandler.announce(announcement);
            }
        }
    }
}
