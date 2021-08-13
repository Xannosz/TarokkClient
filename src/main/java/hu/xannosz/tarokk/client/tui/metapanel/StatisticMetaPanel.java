package hu.xannosz.tarokk.client.tui.metapanel;

import com.googlecode.lanterna.gui2.Borders;
import com.googlecode.lanterna.gui2.GridLayout;
import com.googlecode.lanterna.gui2.Panel;
import com.tisza.tarock.proto.EventProto;
import hu.xannosz.tarokk.client.util.Translator;

import static hu.xannosz.tarokk.client.util.Util.addData;

public class StatisticMetaPanel extends Panel {
    public StatisticMetaPanel(EventProto.Event.Statistics statistic) {
        setLayoutManager(new GridLayout(4));
        addData(this, Translator.INST.callerGamePoints, "" + statistic.getCallerGamePoints());
        addData(this, Translator.INST.opponentGamePoints, "" + statistic.getOpponentGamePoints());
        addData(this, Translator.INST.sumPoints, "" + statistic.getSumPoints());
        addData(this, Translator.INST.pointMultiplier, "" + statistic.getPointMultiplier());

        for (EventProto.Event.Statistics.AnnouncementResult stat : statistic.getAnnouncementResultList()) {
            Panel panel = new Panel();
            addData(panel, Translator.INST.announcement, stat.getAnnouncement());
            addData(panel, Translator.INST.points, "" + stat.getPoints());
            addData(panel, Translator.INST.callerTeam, "" + stat.getCallerTeam());
            addComponent(panel.withBorder(Borders.singleLine()));
        }
    }
}
