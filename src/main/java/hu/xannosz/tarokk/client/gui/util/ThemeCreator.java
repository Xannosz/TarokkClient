package hu.xannosz.tarokk.client.gui.util;

import hu.xannosz.tarokk.client.gui.GuiConstants;
import hu.xannosz.veneos.core.css.CssComponent;
import hu.xannosz.veneos.core.css.CssProperty;
import hu.xannosz.veneos.core.css.Theme;
import hu.xannosz.veneos.core.css.selector.HtmlSelector;
import hu.xannosz.veneos.core.css.selector.ParameterizedPseudoClass;
import lombok.experimental.UtilityClass;

import static hu.xannosz.tarokk.client.gui.GuiConstants.*;

@UtilityClass
public class ThemeCreator {
    public Theme createDefaultTheme() {
        Theme theme = new Theme();

        CssComponent body = new CssComponent(HtmlSelector.BODY);
        body.addProperty(CssProperty.BACKGROUND_COLOR, "gray");
        theme.add(body);

        CssComponent div = new CssComponent(HtmlSelector.DIV);
        div.addProperty(CssProperty.BORDER_COLOR, "gray");
        div.addProperty(CssProperty.BORDER_STYLE, "solid");
        div.addProperty(CssProperty.BORDER, "2px");
        div.addProperty(CssProperty.MARGIN, "10px");
        div.addProperty(CssProperty.PADDING, "10px");
        theme.add(div);

        CssComponent pWithLeft = new CssComponent(NAME_LIST_SUB_PANEL_CLAZZ.getSelector());
        pWithLeft.addProperty(CssProperty.FLOAT,"left");
        pWithLeft.addProperty(CssProperty.DISPLAY,"block");
        theme.add(pWithLeft);

        CssComponent namePanels = new CssComponent(NAME_LIST_PANEL_CLAZZ.getSelector());
        namePanels.addProperty(CssProperty.FLOAT,"left");
        theme.add(namePanels);

        return theme;
    }
}
