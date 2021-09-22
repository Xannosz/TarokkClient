package hu.xannosz.tarokk.client.gui.widget;

import com.tisza.tarock.proto.MainProto;
import hu.xannosz.tarokk.client.network.NetworkHandler;
import hu.xannosz.tarokk.client.util.Util;
import hu.xannosz.veneos.core.html.HtmlComponent;
import hu.xannosz.veneos.core.html.box.Div;

import static hu.xannosz.tarokk.client.gui.util.DataToComponent.createDataComponent;

public class DataWidget extends Widget {

    private final MainProto.GameSession gameData;

    public DataWidget(NetworkHandler networkHandler, MainProto.GameSession gameData) {
        super(networkHandler);
        this.gameData = gameData;
    }

    @Override
    public HtmlComponent updateComponent() {
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
