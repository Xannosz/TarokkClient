package hu.xannosz.tarokk.client.gui.util;

import hu.xannosz.veneos.core.html.structure.Page;
import hu.xannosz.veneos.trie.TryButton;
import lombok.experimental.UtilityClass;

import static hu.xannosz.tarokk.client.gui.GuiConstants.LOGIN_EVENT_ID;

@UtilityClass
public class PageCreator {
    public static Page createLoginPage(){
        Page page = new Page();
        page.addComponent(new TryButton(LOGIN_EVENT_ID,"Login"));
        return page;
    }
}
