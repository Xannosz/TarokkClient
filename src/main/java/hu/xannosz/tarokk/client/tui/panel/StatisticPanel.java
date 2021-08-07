package hu.xannosz.tarokk.client.tui.panel;

import com.googlecode.lanterna.gui2.Borders;
import com.googlecode.lanterna.gui2.GridLayout;
import com.googlecode.lanterna.gui2.Panel;
import com.tisza.tarock.proto.EventProto;
import hu.xannosz.tarokk.client.tui.TuiClient;

import static hu.xannosz.tarokk.client.util.Util.addData;

public class StatisticPanel extends Panel {
    public StatisticPanel(TuiClient tuiClient, EventProto.Event.Statistics statistic) {
        setLayoutManager(new GridLayout(4));
        addData(this, "caller game points", "" + statistic.getCallerGamePoints(), tuiClient);
        addData(this, "opponent game points", "" + statistic.getOpponentGamePoints(), tuiClient);
        addData(this, "sum points", "" + statistic.getSumPoints(), tuiClient);
        addData(this, "point multiplier", "" + statistic.getPointMultiplier(), tuiClient);

        for (EventProto.Event.Statistics.AnnouncementResult stat : statistic.getAnnouncementResultList()) {
            Panel panel = new Panel();
            addData(panel, "announcement", stat.getAnnouncement(), tuiClient);
            addData(panel, "points", "" + stat.getPoints(), tuiClient);
            addData(panel, "caller team", "" + stat.getCallerTeam(), tuiClient);
            addComponent(panel.withBorder(Borders.singleLine()));
        }
    }
}
