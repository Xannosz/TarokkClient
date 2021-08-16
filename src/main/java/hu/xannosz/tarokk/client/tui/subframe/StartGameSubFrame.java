package hu.xannosz.tarokk.client.tui.subframe;

import com.googlecode.lanterna.gui2.Component;
import com.googlecode.lanterna.gui2.GridLayout;
import com.googlecode.lanterna.gui2.Label;
import com.googlecode.lanterna.gui2.Panel;
import com.googlecode.lanterna.input.KeyStroke;
import com.googlecode.lanterna.input.KeyType;
import com.tisza.tarock.proto.MainProto;
import hu.xannosz.tarokk.client.network.Messages;
import hu.xannosz.tarokk.client.tui.TuiClient;
import hu.xannosz.tarokk.client.util.translator.Translator;

import java.util.Collections;
import java.util.Map;

import static hu.xannosz.tarokk.client.util.Util.createUserPanel;
import static hu.xannosz.tarokk.client.util.Util.getGameData;

public class StartGameSubFrame extends SubFrame {
    private final int gameId;

    public StartGameSubFrame(TuiClient tuiClient, int gameId) {
        super(tuiClient);
        this.gameId = gameId;
    }

    @Override
    public Component getPanel() {
        MainProto.GameSession gameData = getGameData(gameId, tuiClient);
        Panel panel = new Panel();
        panel.setLayoutManager(new GridLayout(2));

        panel.addComponent(new Label(Translator.INST.type));
        panel.addComponent(new Label(gameData.getType()));

        for (int i = 0; i < gameData.getUserIdCount(); i++) {
            panel.addComponent(createUserPanel(gameData.getUserId(i), tuiClient));
        }
        for (int i = gameData.getUserIdCount(); i < 4; i++) {
            panel.addComponent(new Label(""));
        }

        panel.addComponent(new Label(Translator.INST.status));
        panel.addComponent(new Label("" + gameData.getState()));
        return panel;
    }

    @Override
    public Map<String, String> getFooter() {
        return Collections.singletonMap("Enter", Translator.INST.startGame);
    }

    @Override
    public void handleKeyStroke(KeyStroke keyStroke) {
        if (keyStroke.getKeyType().equals(KeyType.Enter)) {
            tuiClient.getConnection().sendMessage(Messages.startGame());
        }
    }
}
