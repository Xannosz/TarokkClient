package hu.xannosz.tarokk.client.tui.subframe;

import com.googlecode.lanterna.gui2.Component;
import com.googlecode.lanterna.gui2.Panel;
import com.googlecode.lanterna.input.KeyStroke;
import com.googlecode.lanterna.input.KeyType;
import hu.xannosz.tarokk.client.network.Action;
import hu.xannosz.tarokk.client.tui.TuiClient;
import hu.xannosz.tarokk.client.util.MessageTranslator;

import java.util.Collections;
import java.util.Map;

public class AnnouncingSubFrame extends SubFrame{
    public AnnouncingSubFrame(TuiClient tuiClient) {
        super(tuiClient);
    }

    @Override
    public Component getPanel() {
        return new Panel();
    }

    @Override
    public Map<String, String> getFooter() {
        return Collections.emptyMap();
    }

    @Override
    public void handleKeyStroke(KeyStroke keyStroke) {
        if (keyStroke.getKeyType().equals(KeyType.Enter)) {
            tuiClient.getConnection().sendMessage(MessageTranslator.sendAction(Action.announcePass())); //TODO implement
        }
    }
}
