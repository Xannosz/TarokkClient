package hu.xannosz.tarokk.client.gui.util;

import com.googlecode.lanterna.Symbols;
import com.tisza.tarock.proto.MainProto;
import hu.xannosz.tarokk.client.util.translator.Translator;
import hu.xannosz.veneos.core.html.HtmlComponent;
import hu.xannosz.veneos.core.html.box.Div;
import hu.xannosz.veneos.core.html.str.P;
import hu.xannosz.veneos.trie.TryButton;
import lombok.experimental.UtilityClass;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentMap;

import static hu.xannosz.tarokk.client.gui.GuiConstants.*;

@UtilityClass
public class DataToComponent {

    public static HtmlComponent createGameListComponent(
            List<MainProto.GameSession> gameSessions, ConcurrentMap<Integer, MainProto.User> users,
            int selectedGame, int selfUserId) {
        Div gameDiv = new Div();
        for (int i = 0; i < gameSessions.size(); i++) {
            if (selectedGame == i) {
                gameDiv.add(createGameSessionComponent(gameSessions.get(i), users, selfUserId).addClass(SELECTED_GAME_CLAZZ));
            } else {
                gameDiv.add(createGameSessionComponent(gameSessions.get(i), users, selfUserId));
            }
        }
        return gameDiv;
    }

    public static HtmlComponent createNameListComponent(ConcurrentMap<Integer, MainProto.User> users, int selfUserId) {
        Div userDiv = new Div();
        List<Map.Entry<Integer, MainProto.User>> userList = new ArrayList<>(users.entrySet());
        for (Map.Entry<Integer, MainProto.User> integerUserEntry : userList) {
            if (!integerUserEntry.getValue().getBot() && integerUserEntry.getValue().getId() != selfUserId) {
                userDiv.add(createUserComponent(integerUserEntry.getValue()));
            }
        }
        return userDiv;
    }

    public static HtmlComponent createGameSessionComponent(MainProto.GameSession game, ConcurrentMap<Integer, MainProto.User> users, int selfUserId) {
        Div gameDiv = new Div();

        gameDiv.add(new P(Translator.INST.type));
        gameDiv.add(new P(game.getType()));

        for (int i = 0; i < game.getUserIdCount(); i++) {
            if (game.getUserId(i) != 0) {
                gameDiv.add(createUserPanelComponent(users.get(game.getUserId(i))));
            } else {
                gameDiv.add(new P(""));
            }
        }
        for (int i = game.getUserIdCount(); i < 4; i++) {
            gameDiv.add(new P(""));
        }

        gameDiv.add(new P(Translator.INST.status));
        gameDiv.add(new P("" + game.getState()));

        gameDiv.add(new TryButton(JOIN_GAME_EVENT_ID, Collections.singletonMap(GAME_ID, game.getId()), Translator.INST.joinToGame));
        if (game.getUserIdList().contains(selfUserId)) {
            gameDiv.add(new TryButton(DELETE_GAME_EVENT_ID, Collections.singletonMap(GAME_ID, game.getId()), Translator.INST.deleteGame));
        }

        return gameDiv;
    }

    public static HtmlComponent createUserComponent(MainProto.User user) {
        Div userDiv = new Div();
        userDiv.add(new P(user.getName()));
        if (user.getOnline()) {
            userDiv.add(new P(Translator.INST.online).addClass(ONLINE_CLAZZ));
        } else {
            userDiv.add(new P(Translator.INST.offline).addClass(OFFLINE_CLAZZ));
        }
        if (user.getIsFriend()) {
            userDiv.add(new P(Translator.INST.friend).addClass(FRIEND_CLAZZ));
        } else {
            userDiv.add(new P(Translator.INST.notFriend).addClass(NON_FRIEND_CLAZZ));
        }
        return userDiv;
    }

    public static HtmlComponent createUserPanelComponent(MainProto.User user) {
        if (user.getBot()) {
            return new P("Bot");
        } else {
            Div userDiv = new Div();
            userDiv.add(new P(user.getName()));
            if (user.getOnline()) {
                userDiv.add(new P("" + Symbols.TRIANGLE_UP_POINTING_BLACK));
            } else {
                userDiv.add(new P("" + Symbols.TRIANGLE_DOWN_POINTING_BLACK));
            }
            return userDiv;
        }
    }
}
