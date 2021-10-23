package hu.xannosz.tarokk.client.gui.subframe;

import hu.xannosz.tarokk.client.gui.ConnectionsData;
import hu.xannosz.tarokk.client.gui.Event;
import hu.xannosz.tarokk.client.gui.GuiConstants;
import hu.xannosz.tarokk.client.gui.frame.GameFrame;
import hu.xannosz.tarokk.client.network.NetworkHandler;
import hu.xannosz.tarokk.client.util.Util;
import hu.xannosz.tarokk.client.util.translator.Translator;
import hu.xannosz.veneos.core.html.HtmlComponent;
import hu.xannosz.veneos.core.html.box.Div;
import hu.xannosz.veneos.trie.TryButton;

import static hu.xannosz.tarokk.client.gui.GuiConstants.START_GAME_EVENT_ID;
import static hu.xannosz.tarokk.client.gui.util.DataToComponent.createGameSessionComponent;

public class StartGameSubFrame extends SubFrame {
    public StartGameSubFrame(NetworkHandler networkHandler, ConnectionsData connectionsData, GameFrame gameFrame) {
        super(networkHandler, connectionsData, gameFrame);
    }

    @Override
    public HtmlComponent updateComponent() {
        Div gameDiv = (Div) createGameSessionComponent(Util.getGameData(gameFrame.getGameId(), networkHandler.getLiveData()),
                networkHandler.getLiveData().getUsers(),0);
        gameDiv.add(new TryButton(START_GAME_EVENT_ID, Translator.INST.startGame));
        return gameDiv;
    }

    @Override
    public void handleEvent(Event event) {
        if (event.getEventId().equals(START_GAME_EVENT_ID)) {
            networkHandler.startGame();
        }
    }
}
