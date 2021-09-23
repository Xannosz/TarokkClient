package hu.xannosz.tarokk.client.gui.subframe;

import hu.xannosz.tarokk.client.gui.ConnectionsData;
import hu.xannosz.tarokk.client.gui.Event;
import hu.xannosz.tarokk.client.gui.GuiConstants;
import hu.xannosz.tarokk.client.gui.frame.GameFrame;
import hu.xannosz.tarokk.client.network.NetworkHandler;
import hu.xannosz.tarokk.client.util.Util;
import hu.xannosz.veneos.core.html.HtmlComponent;
import lombok.Getter;
import lombok.Setter;

import static hu.xannosz.tarokk.client.gui.util.DataToComponent.createBiddingComponent;

public class BiddingSubFrame extends SubFrame {

    @Setter
    @Getter
    private int lastBid = 4;

    public BiddingSubFrame(NetworkHandler networkHandler, ConnectionsData connectionsData, GameFrame gameFrame) {
        super(networkHandler, connectionsData, gameFrame);
    }

    @Override
    public HtmlComponent updateComponent() {
        return createBiddingComponent(Util.getGameData(gameFrame.getGameId(), networkHandler.getLiveData()),
                networkHandler.getLiveData(), this);
    }

    @Override
    public void handleEvent(Event event) {
        if (event.getEventId().equals(GuiConstants.BIDDING_EVENT_ID)) {
            networkHandler.bid((Integer) event.getAdditionalParams().get(GuiConstants.BIDDING_ID));
        }
    }
}
