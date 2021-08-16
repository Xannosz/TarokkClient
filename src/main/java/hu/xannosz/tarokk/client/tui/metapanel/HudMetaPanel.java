package hu.xannosz.tarokk.client.tui.metapanel;

import com.googlecode.lanterna.gui2.GridLayout;
import com.googlecode.lanterna.gui2.Panel;
import com.tisza.tarock.proto.MainProto;
import hu.xannosz.tarokk.client.tui.TuiClient;
import hu.xannosz.tarokk.client.util.translator.Translator;
import hu.xannosz.tarokk.client.util.Util;

import java.util.Map;

import static hu.xannosz.tarokk.client.util.Util.addData;
import static hu.xannosz.tarokk.client.util.Util.getPlayerName;

public class HudMetaPanel extends Panel {
    public HudMetaPanel(TuiClient tuiClient, MainProto.GameSession gameData) {
        setLayoutManager(new GridLayout(2));
        if (!Util.anyNull(gameData)) {
            for (Map.Entry<Integer, Boolean> info : tuiClient.getServerLiveData().getPlayerTeamInfo().entrySet()) {
                addData(this, getPlayerName(info.getKey(), gameData, tuiClient) + Translator.INST.isCaller, "" + info.getValue());
            }
        }
    }
}
