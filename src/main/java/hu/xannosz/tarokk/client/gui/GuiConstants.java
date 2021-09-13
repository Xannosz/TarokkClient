package hu.xannosz.tarokk.client.gui;

import hu.xannosz.veneos.core.html.HtmlClass;
import lombok.experimental.UtilityClass;

@UtilityClass
public class GuiConstants {
    public static final String LOGIN_EVENT_ID = "loginEventId";
    public static final String CREATE_GAME_EVENT_ID = "createGameEventId";
    public static final String JOIN_GAME_EVENT_ID = "joinGameEventId";
    public static final String DELETE_GAME_EVENT_ID = "deleteGameEventId";

    public static final String ENTER_KEY_CODE = "Enter";
    public static final String ARROW_UP_KEY_CODE = "ArrowUp";
    public static final String ARROW_DOWN_KEY_CODE = "ArrowDown";
    public static final String PLUS_KEY_CODE = "PlusKey";
    public static final String MINUS_KEY_CODE = "MinusKey";

    public static final HtmlClass ONLINE_CLAZZ = new HtmlClass();
    public static final HtmlClass OFFLINE_CLAZZ = new HtmlClass();
    public static final HtmlClass FRIEND_CLAZZ = new HtmlClass();
    public static final HtmlClass NON_FRIEND_CLAZZ = new HtmlClass();
    public static final HtmlClass SELECTED_GAME_CLAZZ = new HtmlClass();

    public static final String GAME_ID = "gameId";
}
