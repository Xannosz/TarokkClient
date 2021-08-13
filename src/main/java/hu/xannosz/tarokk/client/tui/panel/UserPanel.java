package hu.xannosz.tarokk.client.tui.panel;

import com.googlecode.lanterna.TerminalSize;
import com.googlecode.lanterna.gui2.GridLayout;
import com.googlecode.lanterna.gui2.Label;
import com.googlecode.lanterna.gui2.Panel;
import com.tisza.tarock.proto.MainProto;
import hu.xannosz.tarokk.client.util.ThemeHandler;
import hu.xannosz.tarokk.client.util.Translator;

public class UserPanel extends Panel {
    public UserPanel(MainProto.User user, boolean drawNameOnlinePanel, boolean drawNameFriendPanel, TerminalSize nameNamePanelSize, TerminalSize nameOnlinePanelSize, TerminalSize nameFriendPanelSize) {
        addComponent(new Label(user.getName()).setPreferredSize(nameNamePanelSize));
        if (drawNameOnlinePanel) {
            if (user.getOnline()) {
                addComponent(new Label(Translator.INST.online).setPreferredSize(nameOnlinePanelSize).setTheme(ThemeHandler.getHighLightedThemeMainPanel()));
            } else {
                addComponent(new Label(Translator.INST.offline).setPreferredSize(nameOnlinePanelSize).setTheme(ThemeHandler.getSubLightedThemeMainPanel()));
            }
        }
        if (drawNameFriendPanel) {
            if (user.getIsFriend()) {
                addComponent(new Label(Translator.INST.friend).setPreferredSize(nameFriendPanelSize).setTheme(ThemeHandler.getHighLightedThemeMainPanel()));
            } else {
                addComponent(new Label(Translator.INST.notFriend).setPreferredSize(nameFriendPanelSize).setTheme(ThemeHandler.getSubLightedThemeMainPanel()));
            }
        }
        setLayoutManager(new GridLayout(3));
    }
}
