package hu.xannosz.tarokk.client.gui;

import hu.xannosz.veneos.core.html.HtmlClass;
import lombok.experimental.UtilityClass;

@UtilityClass
public class GuiConstants {
    public static final String LOGIN_EVENT_ID = "loginEventId";
    public static final String CREATE_GAME_EVENT_ID = "createGameEventId";
    public static final String JOIN_GAME_EVENT_ID = "joinGameEventId";
    public static final String DELETE_GAME_EVENT_ID = "deleteGameEventId";
    public static final String GAME_TYPE_EVENT_ID = "gameTypeEventId";
    public static final String DOUBLE_ROUND_TYPE_EVENT_ID = "doubleRoundTypeEventId";
    public static final String CANCEL_EVENT_ID = "cancelEventId";
    public static final String ANNOUNCING_EVENT_ID = "announcingEventId";
    public static final String BIDDING_EVENT_ID = "biddingEventId";
    public static final String CALLING_EVENT_ID = "callingEventId";
    public static final String FOLDING_EVENT_ID = "foldingEventId";
    public static final String RESET_FOLDING_EVENT_ID = "resetFoldingEventId";
    public static final String SEND_FOLDING_EVENT_ID = "sendFoldingEventId";
    public static final String GAME_PLAY_EVENT_ID = "gamePlayEventId";
    public static final String START_GAME_EVENT_ID = "startGameEventId";

    public static final String ENTER_KEY_CODE = "Enter";
    public static final String ARROW_UP_KEY_CODE = "ArrowUp";
    public static final String ARROW_DOWN_KEY_CODE = "ArrowDown";
    public static final String PLUS_KEY_CODE = "PlusKey";
    public static final String MINUS_KEY_CODE = "MinusKey";

    public static final HtmlClass ONLINE_CLAZZ = new HtmlClass();
    public static final HtmlClass OFFLINE_CLAZZ = new HtmlClass();
    public static final HtmlClass FRIEND_CLAZZ = new HtmlClass();
    public static final HtmlClass NON_FRIEND_CLAZZ = new HtmlClass();
    public static final HtmlClass NAME_LIST_PANEL_CLAZZ = new HtmlClass();
    public static final HtmlClass SELECTED_GAME_CLAZZ = new HtmlClass();
    public static final HtmlClass SELECTED_GAME_TYPE_CLAZZ = new HtmlClass();
    public static final HtmlClass SELECTED_DOUBLE_ROUND_TYPE_CLAZZ = new HtmlClass();
    public static final HtmlClass DATA_VALUE_CLAZZ = new HtmlClass();
    public static final HtmlClass GAME_LIST_CLAZZ = new HtmlClass();
    public static final HtmlClass GAME_SESSION_CLAZZ = new HtmlClass();
    public static final HtmlClass CONTAINER_CLAZZ = new HtmlClass();
    public static final HtmlClass CONTAINER_SUB_CLAZZ = new HtmlClass();
    public static final HtmlClass CARDS_COMPONENT_CLAZZ = new HtmlClass();
    public static final HtmlClass DUAL_PANEL_CLAZZ = new HtmlClass();

    public static final String GAME_ID = "gameId";
    public static final String GAME_TYPE_ID = "gameTypeId";
    public static final String DOUBLE_ROUND_TYPE_ID = "doubleRoundTypeId";
    public static final String ANNOUNCING_ID = "announcingId";
    public static final String BIDDING_ID = "biddingId";
    public static final String CALLING_ID = "callingId";
    public static final String FOLDING_ID = "foldingId";
    public static final String GAME_PLAY_ID = "gamePlayId";
}
