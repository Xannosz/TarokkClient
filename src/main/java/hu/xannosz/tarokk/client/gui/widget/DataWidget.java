package hu.xannosz.tarokk.client.gui.widget;

import com.tisza.tarock.proto.MainProto;
import hu.xannosz.tarokk.client.network.NetworkHandler;
import hu.xannosz.tarokk.client.util.Util;
import hu.xannosz.veneos.core.html.HtmlComponent;
import hu.xannosz.veneos.core.html.box.Div;

import static hu.xannosz.tarokk.client.gui.util.DataToComponent.createDataComponent;

public class DataWidget extends Widget {

    private final int gameId;

    public DataWidget(NetworkHandler networkHandler, int gameId) {
        super(networkHandler);
        this.gameId = gameId;
    }

    @Override
    public HtmlComponent updateComponent() {
        MainProto.GameSession gameData = Util.getGameData(gameId, networkHandler.getLiveData());
        if (!Util.anyNull(networkHandler.getLiveData().getGameType(),
                networkHandler.getLiveData().getPhase(),
                networkHandler.getLiveData().getBeginnerPlayer(),
                gameData)) {
            return createDataComponent(networkHandler, gameData);
        } else {
            return new Div();
        }
    }
}
