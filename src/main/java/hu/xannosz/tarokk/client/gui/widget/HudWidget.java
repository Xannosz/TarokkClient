package hu.xannosz.tarokk.client.gui.widget;

import com.tisza.tarock.proto.MainProto;
import hu.xannosz.tarokk.client.network.NetworkHandler;
import hu.xannosz.tarokk.client.util.Util;
import hu.xannosz.veneos.core.html.HtmlComponent;
import hu.xannosz.veneos.core.html.box.Div;

import static hu.xannosz.tarokk.client.gui.util.DataToComponent.createHudComponent;

public class HudWidget extends Widget {

    private final int gameId;

    public HudWidget(NetworkHandler networkHandler, int gameId) {
        super(networkHandler);
        this.gameId = gameId;
    }

    @Override
    public HtmlComponent updateComponent() {
        MainProto.GameSession gameData = Util.getGameData(gameId, networkHandler.getLiveData());
        if (!Util.anyNull(gameData)) {
            return createHudComponent(networkHandler, gameData);
        } else {
            return new Div();
        }
    }
}
