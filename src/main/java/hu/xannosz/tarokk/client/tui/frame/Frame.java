package hu.xannosz.tarokk.client.tui.frame;

import com.googlecode.lanterna.gui2.Component;
import com.googlecode.lanterna.gui2.Panel;
import com.googlecode.lanterna.input.KeyStroke;
import hu.xannosz.tarokk.client.tui.TuiClient;

public abstract class Frame {

    protected TuiClient tuiClient;
    protected Panel frame;
    protected Panel footer;

    public Frame(TuiClient tuiClient) {
        this.tuiClient = tuiClient;
    }

    public Component getPanel(){
        return frame;
    }

    public Component getFooter(){
        return footer;
    }

    public abstract void handleKeyStroke(KeyStroke keyStroke);

    public abstract void update();
}
