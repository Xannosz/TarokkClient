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

import static hu.xannosz.tarokk.client.gui.util.DataToComponent.createGamePlayComponent;

public class GamePlaySubFrame extends SubFrame {
    public GamePlaySubFrame(NetworkHandler networkHandler, ConnectionsData connectionsData, GameFrame gameFrame) {
        super(networkHandler, connectionsData, gameFrame);
    }

    @Override
    public HtmlComponent updateComponent() {
        return createGamePlayComponent(Util.getGameData(gameFrame.getGameId(), networkHandler.getLiveData()), networkHandler.getLiveData());
    }

    @Override
    public void handleEvent(Event event) {
        if (event.getEventId().equals(GuiConstants.GAME_PLAY_EVENT_ID)) {
            networkHandler.play(Json.castObjectToSpecificClass(event.getAdditionalParams().get(GuiConstants.GAME_PLAY_ID), Card.class));
        }
    }
}
