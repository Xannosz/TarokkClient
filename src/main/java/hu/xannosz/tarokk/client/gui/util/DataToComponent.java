package hu.xannosz.tarokk.client.gui.util;

import com.googlecode.lanterna.Symbols;
import com.tisza.tarock.proto.MainProto;
import hu.xannosz.microtools.pack.Douplet;
import hu.xannosz.tarokk.client.game.Actions;
import hu.xannosz.tarokk.client.game.Card;
import hu.xannosz.tarokk.client.game.DoubleRoundType;
import hu.xannosz.tarokk.client.game.GameType;
import hu.xannosz.tarokk.client.gui.subframe.BiddingSubFrame;
import hu.xannosz.tarokk.client.network.NetworkHandler;
import hu.xannosz.tarokk.client.network.ServerLiveData;
import hu.xannosz.tarokk.client.util.Util;
import hu.xannosz.tarokk.client.util.translator.Translator;
import hu.xannosz.veneos.core.html.HtmlComponent;
import hu.xannosz.veneos.core.html.box.Div;
import hu.xannosz.veneos.core.html.str.P;
import hu.xannosz.veneos.trie.TryButton;
import lombok.experimental.UtilityClass;

import java.util.*;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ConcurrentMap;

import static hu.xannosz.tarokk.client.gui.GuiConstants.*;
import static hu.xannosz.tarokk.client.util.Util.getFormattedCardName;
import static hu.xannosz.tarokk.client.util.Util.getPlayerName;

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

    public static HtmlComponent createGameTypeComponent(GameType gameType) {
        Div div = new Div();
        Arrays.stream(GameType.values()).forEach((t) -> {
            TryButton button = new TryButton(GAME_TYPE_EVENT_ID, Collections.singletonMap(GAME_TYPE_ID, t), t.getName());
            if (t == gameType) {
                button.addClass(SELECTED_GAME_TYPE_CLAZZ);
            }
            div.add(button);
        });
        return div;
    }

    public static HtmlComponent createDoubleRoundTypeComponent(DoubleRoundType doubleRoundType) {
        Div div = new Div();
        Arrays.stream(DoubleRoundType.values()).forEach((t) -> {
            TryButton button = new TryButton(DOUBLE_ROUND_TYPE_EVENT_ID, Collections.singletonMap(DOUBLE_ROUND_TYPE_ID, t), t.getName());
            if (t == doubleRoundType) {
                button.addClass(SELECTED_DOUBLE_ROUND_TYPE_CLAZZ);
            }
            div.add(button);
        });
        return div;
    }

    public static HtmlComponent createCardsComponent(ConcurrentLinkedQueue<Card> playerCard) {
        Div div = new Div();
        for (Card card : playerCard) {
            div.add(new P(card.getFormattedName()));
        }
        return div;
    }

    public static HtmlComponent createDataComponent(NetworkHandler networkHandler, MainProto.GameSession gameData) {
        Div div = new Div();
        addData(div, Translator.INST.gameType, networkHandler.getLiveData().getGameType());
        addData(div, Translator.INST.beginnerPlayer, getPlayerName(networkHandler.getLiveData().getBeginnerPlayer(), gameData, networkHandler.getLiveData()));
        addData(div, Translator.INST.phase, networkHandler.getLiveData().getPhase().getName());
        addData(div, Translator.INST.playerInTurn, getPlayerName(networkHandler.getLiveData().getPlayerTurn(), gameData, networkHandler.getLiveData()));

        if (networkHandler.getLiveData().getPlayerActions().get(Actions.FOLD) != null) {
            for (Douplet<Integer, String> folded : networkHandler.getLiveData().getPlayerActions().get(Actions.FOLD)) {
                addData(div, getPlayerName(folded.getFirst(), gameData, networkHandler.getLiveData()) + Translator.INST.foldedTarock, getFormattedCardName(folded.getSecond()));
            }
        }

        for (
                Map.Entry<Integer, Integer> skartedTarock : networkHandler.getLiveData().getSkartedTarocks().entrySet()) {
            addData(div, getPlayerName(skartedTarock.getKey(), gameData, networkHandler.getLiveData()) + Translator.INST.foldedTarock, "" + skartedTarock.getValue());
        }

        if (networkHandler.getLiveData().getPlayerActions().get(Actions.CALL) != null) {
            for (Douplet<Integer, String> call : networkHandler.getLiveData().getPlayerActions().get(Actions.CALL)) {
                addData(div, getPlayerName(call.getFirst(), gameData, networkHandler.getLiveData()) + Translator.INST.calledTarock, getFormattedCardName(call.getSecond()));
            }
        }

        for (Map.Entry<Integer, Integer> entry : networkHandler.getLiveData().getCardsTakenUsers().entrySet()) {
            addData(div, getPlayerName(entry.getKey(), gameData, networkHandler.getLiveData()) + Translator.INST.takenCards, "" + entry.getValue());
        }
        return div;
    }

    public static HtmlComponent createHudComponent(NetworkHandler networkHandler, MainProto.GameSession gameData) {
        Div div = new Div();
        for (Map.Entry<Integer, Boolean> info : networkHandler.getLiveData().getPlayerTeamInfo().entrySet()) {
            addData(div, getPlayerName(info.getKey(), gameData, networkHandler.getLiveData()) + Translator.INST.isCaller, "" + info.getValue());
        }
        return div;
    }

    public static HtmlComponent createStartGameComponent(MainProto.GameSession gameData, ServerLiveData liveData) {
        Div div = new Div();

        div.add(new P(Translator.INST.type));
        div.add(new P(gameData.getType()));

        for (int i = 0; i < gameData.getUserIdCount(); i++) {
            div.add(createUserPanel(gameData.getUserId(i), liveData));
        }
        for (int i = gameData.getUserIdCount(); i < 4; i++) {
            div.add(new P(""));
        }

        div.add(new P(Translator.INST.status));
        div.add(new P("" + gameData.getState()));
        div.add(new TryButton(START_GAME_EVENT_ID, Translator.INST.startGame));

        return div;
    }

    public static HtmlComponent createBiddingComponent(MainProto.GameSession gameData, ServerLiveData liveData,
                                                       BiddingSubFrame biddingSubFrame) {
        Div div = new Div();

        if (Util.anyNull(liveData.getPlayerActions().get(Actions.BID),
                liveData.getAvailableBids())) {
            return div;
        }

        for (Douplet<Integer, String> action : liveData.getPlayerActions().get(Actions.BID)) {
            addData(div, getPlayerName(action.getFirst(), gameData, liveData),
                    biddingToString(action.getSecond(), biddingSubFrame));
        }

        for (int bid : liveData.getAvailableBids()) {
            div.add(new TryButton(BIDDING_EVENT_ID, Collections.singletonMap(BIDDING_ID, bid), biddingToString(bid, biddingSubFrame)));
        }

        return div;
    }

    private static void addData(Div div, String name, String value) {
        div.add(new P(name + ":"));
        div.add(new P(value).addClass(DATA_VALUE_CLAZZ));
    }

    private static HtmlComponent createUserPanel(int userId, ServerLiveData liveData) {
        if (userId != 0) {
            MainProto.User user = liveData.getUsers().get(userId);
            if (user.getBot()) {
                return new P(" Bot");
            } else {
                Div div = new Div();
                div.add(new P(user.getName()));
                if (user.getOnline()) {
                    div.add(new P("" + Symbols.TRIANGLE_UP_POINTING_BLACK));
                } else {
                    div.add(new P("" + Symbols.TRIANGLE_DOWN_POINTING_BLACK));
                }
                return div;
            }
        } else {
            return new Div();
        }
    }

    private String biddingToString(String action, BiddingSubFrame biddingSubFrame) {
        if (action.equals("p")) {
            return Translator.INST.pass;
        }

        return biddingToString(Integer.parseInt(action), biddingSubFrame);
    }

    private String biddingToString(int num, BiddingSubFrame biddingSubFrame) {
        if (num == -1) {
            return Translator.INST.pass;
        }

        if (num == biddingSubFrame.getLastBid()) {
            return Translator.INST.hold;
        }

        biddingSubFrame.setLastBid(num);

        if (num == 3) {
            return Translator.INST.three;
        }
        if (num == 2) {
            return Translator.INST.two;
        }
        if (num == 1) {
            return Translator.INST.one;
        }
        if (num == 0) {
            return Translator.INST.solo;
        }

        return Translator.INST.invalid + num;
    }
}
