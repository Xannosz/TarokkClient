package hu.xannosz.tarokk.client.gui.widget;

import hu.xannosz.tarokk.client.network.NetworkHandler;
import hu.xannosz.veneos.core.html.HtmlComponent;

public abstract class Widget {
    protected final NetworkHandler networkHandler;

    public Widget(NetworkHandler networkHandler) {
        this.networkHandler = networkHandler;
    }

    public abstract HtmlComponent updateComponent();
}
