package hu.xannosz.tarokk.client.tui.panel;

import com.googlecode.lanterna.gui2.GridLayout;
import com.googlecode.lanterna.gui2.Panel;
import com.tisza.tarock.proto.MainProto;
import hu.xannosz.tarokk.client.tui.TuiClient;
import hu.xannosz.tarokk.client.util.Util;

import java.util.Map;

import static hu.xannosz.tarokk.client.util.Util.addData;
import static hu.xannosz.tarokk.client.util.Util.getPlayerName;

public class HudPanel extends Panel {
    public HudPanel(TuiClient tuiClient, MainProto.GameSession gameData) {
        setLayoutManager(new GridLayout(2));
        if (!Util.anyNull(gameData)) {
            for (Map.Entry<Integer, Boolean> info : tuiClient.getServerLiveData().getPlayerTeamInfo().entrySet()) {
                addData(this, getPlayerName(info.getKey(), gameData, tuiClient) + " is caller:", "" + info.getValue(), tuiClient);
            }
        }
    }
}
