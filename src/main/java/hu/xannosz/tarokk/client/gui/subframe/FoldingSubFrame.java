package hu.xannosz.tarokk.client.gui.subframe;

import hu.xannosz.microtools.Json;
import hu.xannosz.tarokk.client.game.Card;
import hu.xannosz.tarokk.client.gui.ConnectionsData;
import hu.xannosz.tarokk.client.gui.Event;
import hu.xannosz.tarokk.client.gui.GuiConstants;
import hu.xannosz.tarokk.client.gui.frame.GameFrame;
import hu.xannosz.tarokk.client.network.NetworkHandler;
import hu.xannosz.tarokk.client.util.Util;
import hu.xannosz.veneos.core.html.HtmlComponent;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

import static hu.xannosz.tarokk.client.gui.util.DataToComponent.createFoldingComponent;

public class FoldingSubFrame extends SubFrame {

    @Getter
    private final List<Card> foldedCards = new ArrayList<>();

    public FoldingSubFrame(NetworkHandler networkHandler, ConnectionsData connectionsData, GameFrame gameFrame) {
        super(networkHandler, connectionsData, gameFrame);
    }

    @Override
    public HtmlComponent updateComponent() {
        return createFoldingComponent(Util.getGameData(gameFrame.getGameId(), networkHandler.getLiveData()), networkHandler.getLiveData(), this);
    }

    @Override
    public void handleEvent(Event event) {
        if (event.getEventId().equals(GuiConstants.FOLDING_EVENT_ID)) {
            foldedCards.add(Json.castObjectToSpecificClass(event.getAdditionalParams().get(GuiConstants.FOLDING_ID), Card.class));
        }
        if (event.getEventId().equals(GuiConstants.RESET_FOLDING_EVENT_ID)) {
            foldedCards.clear();
        }
        if (event.getEventId().equals(GuiConstants.SEND_FOLDING_EVENT_ID)) {
            networkHandler.fold(foldedCards);
        }
    }
}
