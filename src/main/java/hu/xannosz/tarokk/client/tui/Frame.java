package hu.xannosz.tarokk.client.tui;

import com.googlecode.lanterna.gui2.Component;
import com.googlecode.lanterna.input.KeyStroke;

public abstract class Frame {

    protected TuiClient tuiClient;

    public Frame(TuiClient tuiClient) {
        this.tuiClient = tuiClient;
        update();
    }

    public abstract Component getPanel();

    public abstract void handleKeyStroke(KeyStroke keyStroke);

    public abstract void update();
}
