package hu.xannosz.tarokk.client.tui.panel;

import com.googlecode.lanterna.TerminalSize;
import com.googlecode.lanterna.gui2.GridLayout;
import com.googlecode.lanterna.gui2.Label;
import com.googlecode.lanterna.gui2.Panel;
import com.tisza.tarock.proto.MainProto;
import hu.xannosz.tarokk.client.tui.TuiClient;
import hu.xannosz.tarokk.client.util.ThemeHandler;

public class UserPanel extends Panel {
    public UserPanel(MainProto.User user, boolean drawNameOnlinePanel, boolean drawNameFriendPanel, TerminalSize nameNamePanelSize, TerminalSize nameOnlinePanelSize, TerminalSize nameFriendPanelSize, TuiClient tuiClient) {
        addComponent(new Label(user.getName()).setPreferredSize(nameNamePanelSize));
        if (drawNameOnlinePanel) {
            if (user.getOnline()) {
                addComponent(new Label("Online ").setPreferredSize(nameOnlinePanelSize).setTheme(ThemeHandler.getHighLightedThemeMainPanel(tuiClient.getTerminalSettings())));
            } else {
                addComponent(new Label("Offline").setPreferredSize(nameOnlinePanelSize).setTheme(ThemeHandler.getSubLightedThemeMainPanel(tuiClient.getTerminalSettings())));
            }
        }
        if (drawNameFriendPanel) {
            if (user.getIsFriend()) {
                addComponent(new Label("  Friend  ").setPreferredSize(nameFriendPanelSize).setTheme(ThemeHandler.getHighLightedThemeMainPanel(tuiClient.getTerminalSettings())));
            } else {
                addComponent(new Label("Not Friend").setPreferredSize(nameFriendPanelSize).setTheme(ThemeHandler.getSubLightedThemeMainPanel(tuiClient.getTerminalSettings())));
            }
        }
        setLayoutManager(new GridLayout(3));
    }
}
