package hu.xannosz.tarokk.client.tui.subframe;

import com.googlecode.lanterna.gui2.Component;
import hu.xannosz.tarokk.client.tui.KeyMapDictionary;
import hu.xannosz.tarokk.client.tui.TuiClient;
import lombok.Getter;

public abstract class SubFrame {
    protected TuiClient tuiClient;
    @Getter
    protected KeyMapDictionary keyMapDictionary;

    public SubFrame(TuiClient tuiClient, KeyMapDictionary keyMapDictionary) {
        this.tuiClient = tuiClient;
        this.keyMapDictionary = keyMapDictionary;
    }

    public abstract Component getPanel();
}
