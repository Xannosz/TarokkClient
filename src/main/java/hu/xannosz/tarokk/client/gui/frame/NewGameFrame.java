package hu.xannosz.tarokk.client.gui.frame;

import hu.xannosz.microtools.Json;
import hu.xannosz.microtools.Sleep;
import hu.xannosz.tarokk.client.game.DoubleRoundType;
import hu.xannosz.tarokk.client.game.GameType;
import hu.xannosz.tarokk.client.gui.ConnectionsData;
import hu.xannosz.tarokk.client.gui.Event;
import hu.xannosz.tarokk.client.gui.util.DataUtil;
import hu.xannosz.tarokk.client.gui.util.PageCreator;
import hu.xannosz.tarokk.client.network.NetworkHandler;
import hu.xannosz.veneos.core.html.structure.Page;

import static hu.xannosz.tarokk.client.gui.GuiConstants.*;

public class NewGameFrame extends Frame {

    private GameType gameType = GameType.values()[0];
    private DoubleRoundType doubleRoundType = DoubleRoundType.values()[0];

    public NewGameFrame(NetworkHandler networkHandler, ConnectionsData connectionsData) {
        super(networkHandler, connectionsData);
    }

    @Override
    public Page updatePage() {
        return PageCreator.createNewGamePage(gameType, doubleRoundType);
    }

    @Override
    public void handleEvent(Event event) {
        if (event.getEventId().equals(GAME_TYPE_EVENT_ID)) {
            gameType = Json.castObjectToSpecificClass(event.getAdditionalParams().get(GAME_TYPE_ID), GameType.class);
        }
        if (event.getEventId().equals(DOUBLE_ROUND_TYPE_EVENT_ID)) {
            doubleRoundType = Json.castObjectToSpecificClass(event.getAdditionalParams().get(DOUBLE_ROUND_TYPE_ID), DoubleRoundType.class);
        }
        if (event.getEventId().equals(CREATE_GAME_EVENT_ID)) {
            networkHandler.newGame(doubleRoundType, gameType);

            Sleep.sleepMillis(200);

            int gameId = DataUtil.getGameId(networkHandler.getLiveData().getGameSessions(), networkHandler.getLiveData().getLoginResult().getUserId());

            networkHandler.joinToGame(gameId);
            connectionsData.setFrame(new GameFrame(networkHandler, connectionsData, gameId));
        }
        if (event.getEventId().equals(CANCEL_EVENT_ID)) {
            connectionsData.setFrame(new LobbyFrame(networkHandler, connectionsData));
        }
    }
}
