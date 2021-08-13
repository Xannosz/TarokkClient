package hu.xannosz.tarokk.client.tui.frame;

import com.googlecode.lanterna.gui2.Component;
import com.googlecode.lanterna.gui2.Panel;
import com.googlecode.lanterna.input.KeyStroke;
import hu.xannosz.tarokk.client.tui.TuiClient;

import java.util.HashMap;
import java.util.Map;

public abstract class Frame {

    protected TuiClient tuiClient;
    protected Panel frame = new Panel();
    protected Map<String,String> footer = new HashMap<>();

    public Frame(TuiClient tuiClient) {
        this.tuiClient = tuiClient;
    }

    public Component getPanel() {
        return frame;
    }

    public Map<String,String> getFooter() {
        return footer;
    }

    public abstract void handleKeyStroke(KeyStroke keyStroke);

    public abstract void update();
}
