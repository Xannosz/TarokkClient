package hu.xannosz.tarokk.client.tui.subframe;

import com.googlecode.lanterna.gui2.Component;
import com.googlecode.lanterna.input.KeyStroke;
import hu.xannosz.tarokk.client.tui.TuiClient;

import java.util.Map;

public abstract class SubFrame {
    protected TuiClient tuiClient;

    public SubFrame(TuiClient tuiClient) {
        this.tuiClient = tuiClient;
    }

    public abstract Component getPanel();

    public abstract Map<String,String> getFooter();

    public abstract void handleKeyStroke(KeyStroke keyStroke);
}
