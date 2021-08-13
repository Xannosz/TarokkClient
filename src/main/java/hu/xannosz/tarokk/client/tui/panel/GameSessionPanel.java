package hu.xannosz.tarokk.client.tui.panel;

import com.googlecode.lanterna.TerminalSize;
import com.googlecode.lanterna.gui2.GridLayout;
import com.googlecode.lanterna.gui2.Label;
import com.googlecode.lanterna.gui2.Panel;
import com.tisza.tarock.proto.MainProto;
import hu.xannosz.tarokk.client.tui.TuiClient;
import hu.xannosz.tarokk.client.util.Translator;
import hu.xannosz.tarokk.client.util.Util;

public class GameSessionPanel extends Panel {
    public GameSessionPanel(MainProto.GameSession game, TerminalSize gameNamePanelSize, TerminalSize gameDataPanelSize, TuiClient tuiClient) {
        setLayoutManager(new GridLayout(2));
        setPreferredSize(gameDataPanelSize);

        addComponent(new Label(Translator.INST.type).setPreferredSize(gameNamePanelSize));
        addComponent(new Label(game.getType()).setPreferredSize(gameNamePanelSize));

        for (int i = 0; i < game.getUserIdCount(); i++) {
            addComponent(Util.createUserPanel(game.getUserId(i), tuiClient).setPreferredSize(gameNamePanelSize));
        }
        for (int i = game.getUserIdCount(); i < 4; i++) {
            addComponent(new Label("").setPreferredSize(gameNamePanelSize));
        }

        addComponent(new Label(Translator.INST.status).setPreferredSize(gameNamePanelSize));
        addComponent(new Label("" + game.getState()).setPreferredSize(gameNamePanelSize));
    }
}
