package hu.xannosz.tarokk.client.gui.widget;

import hu.xannosz.tarokk.client.network.NetworkHandler;
import hu.xannosz.tarokk.client.util.Util;
import hu.xannosz.veneos.core.html.HtmlComponent;
import hu.xannosz.veneos.core.html.box.Div;

import static hu.xannosz.tarokk.client.gui.util.DataToComponent.createCardsComponent;

public class CardWidget extends Widget {

    public CardWidget(NetworkHandler networkHandler) {
        super(networkHandler);
    }

    @Override
    public HtmlComponent updateComponent() {
        if (!Util.anyNull(networkHandler.getLiveData().getPlayerCard())) {
            return createCardsComponent(networkHandler.getLiveData().getPlayerCard());
        } else {
            return new Div();
        }
    }
}
