package hu.xannosz.tarokk.client.tui.subframe;

import com.googlecode.lanterna.gui2.Component;
import com.googlecode.lanterna.input.KeyStroke;
import hu.xannosz.tarokk.client.tui.TuiClient;

import java.util.Map;

public class CallingSubFrame extends SubFrame{
    public CallingSubFrame(TuiClient tuiClient) {
        super(tuiClient);
    }

    @Override
    public Component getPanel() {
        return null;
    }

    @Override
    public Map<String, String> getFooter() {
        return null;
    }

    @Override
    public void handleKeyStroke(KeyStroke keyStroke) {

    }
}
