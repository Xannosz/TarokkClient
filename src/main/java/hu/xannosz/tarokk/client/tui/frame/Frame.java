package hu.xannosz.tarokk.client.tui.frame;

import com.googlecode.lanterna.gui2.Component;
import com.googlecode.lanterna.input.KeyStroke;
import hu.xannosz.tarokk.client.tui.TuiClient;

public abstract class Frame {

    protected TuiClient tuiClient;

    public Frame(TuiClient tuiClient) {
        this.tuiClient = tuiClient;
        update();  //TODO remove and check all update();
    }

    public abstract Component getPanel();

    public abstract Component getFooter();

    public abstract void handleKeyStroke(KeyStroke keyStroke);

    public abstract void update();
}
