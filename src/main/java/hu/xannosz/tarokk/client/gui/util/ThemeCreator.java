package hu.xannosz.tarokk.client.gui.util;

import hu.xannosz.veneos.core.css.CssComponent;
import hu.xannosz.veneos.core.css.CssProperty;
import hu.xannosz.veneos.core.css.Theme;
import hu.xannosz.veneos.core.css.selector.HtmlSelector;
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
        div.addProperty(CssProperty.BORDER_COLOR, "black");
        div.addProperty(CssProperty.BORDER_STYLE, "solid");
        div.addProperty(CssProperty.BORDER, "1px");
        div.addProperty(CssProperty.MARGIN, "2px");
        div.addProperty(CssProperty.PADDING, "2px");
        theme.add(div);

        CssComponent containerChild = new CssComponent(CONTAINER_SUB_CLAZZ.getSelector());
        containerChild.addProperty(CssProperty.FLEX_BASIS, "45%");
        theme.add(containerChild);

        CssComponent namePanels = new CssComponent(NAME_LIST_PANEL_CLAZZ.getSelector());
        namePanels.addProperty(CssProperty.FLEX_BASIS, "25%");
        namePanels.addProperty(CssProperty.DISPLAY, "flex");
        theme.add(namePanels);

        CssComponent namePanelsChild = new CssComponent(
                NAME_LIST_PANEL_CLAZZ.getSelector().child(HtmlSelector.P.getSelector()));
        namePanelsChild.addProperty(CssProperty.FLEX_BASIS, "33%");
        theme.add(namePanelsChild);

        CssComponent gameList = new CssComponent(GAME_LIST_CLAZZ.getSelector());
        gameList.addProperty(CssProperty.FLEX_BASIS, "65%");
        theme.add(gameList);

        CssComponent gameListChild = new CssComponent(GAME_LIST_CLAZZ.getSelector().child(GAME_SESSION_CLAZZ.getSelector()));
        gameListChild.addProperty(CssProperty.FLEX_BASIS, "30%");
        theme.add(gameListChild);

        CssComponent gameSession = new CssComponent(
                CONTAINER_CLAZZ.getSelector(),
                GAME_LIST_CLAZZ.getSelector(),
                GAME_SESSION_CLAZZ.getSelector(),
                CARDS_COMPONENT_CLAZZ.getSelector(),
                DUAL_PANEL_CLAZZ.getSelector());
        gameSession.addProperty(CssProperty.DISPLAY, "flex");
        gameSession.addProperty(CssProperty.FLEX_DIRECTION, "row");
        gameSession.addProperty(CssProperty.FLEX_WRAP, "wrap");
        gameSession.addProperty(CssProperty.ALIGN_CONTENT, "space-around");
        theme.add(gameSession);

        CssComponent gameSessionChild = new CssComponent(
                GAME_SESSION_CLAZZ.getSelector().child(HtmlSelector.P.getSelector()),
                GAME_SESSION_CLAZZ.getSelector().child(HtmlSelector.BUTTON.getSelector()),
                DUAL_PANEL_CLAZZ.getSelector().child(HtmlSelector.P.getSelector()));
        gameSessionChild.addProperty(CssProperty.FLEX_BASIS, "45%");
        gameSessionChild.addProperty(CssProperty.DISPLAY, "block");
        theme.add(gameSessionChild);

        CssComponent cardsPanelChild = new CssComponent(
                CARDS_COMPONENT_CLAZZ.getSelector().child(HtmlSelector.P.getSelector()));
        cardsPanelChild.addProperty(CssProperty.FLEX_BASIS, "20%");
        cardsPanelChild.addProperty(CssProperty.DISPLAY, "block");
        theme.add(cardsPanelChild);

        CssComponent selected = new CssComponent(SELECTED_GAME_CLAZZ.getSelector(),
                SELECTED_GAME_TYPE_CLAZZ.getSelector(), SELECTED_DOUBLE_ROUND_TYPE_CLAZZ.getSelector());
        selected.addProperty(CssProperty.COLOR, "orange");
        theme.add(selected);

        return theme;
    }
}
