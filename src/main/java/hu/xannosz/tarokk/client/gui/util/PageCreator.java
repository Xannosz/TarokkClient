package hu.xannosz.tarokk.client.gui.util;

import com.tisza.tarock.proto.MainProto;
import hu.xannosz.tarokk.client.game.DoubleRoundType;
import hu.xannosz.tarokk.client.game.GameType;
import hu.xannosz.tarokk.client.util.translator.Translator;
import hu.xannosz.veneos.core.html.structure.Page;
import hu.xannosz.veneos.trie.TryButton;
import lombok.experimental.UtilityClass;

import java.util.List;
import java.util.concurrent.ConcurrentMap;

import static hu.xannosz.tarokk.client.gui.GuiConstants.*;

@UtilityClass
public class PageCreator {
    public static Page createLoginPage() {
        Page page = new Page();
        page.addComponent(new TryButton(LOGIN_EVENT_ID, "Login"));
        return page;
    }

    public static Page createLobbyPage(List<MainProto.GameSession> gameSessions, int selectedGame,
                                       ConcurrentMap<Integer, MainProto.User> users, int selfUserId) {
        Page page = new Page();
        page.addComponent(DataToComponent.createGameListComponent(gameSessions, users, selectedGame, selfUserId));
        page.addComponent(DataToComponent.createNameListComponent(users, selfUserId));
        page.addComponent(new TryButton(CREATE_GAME_EVENT_ID, Translator.INST.createGame));
        return page;
    }

    public static Page createNewGamePage(GameType gameType, DoubleRoundType doubleRoundType) {
        Page page = new Page();
        page.addComponent(DataToComponent.createGameTypeComponent(gameType));
        page.addComponent(DataToComponent.createDoubleRoundTypeComponent(doubleRoundType));
        page.addComponent(new TryButton(CREATE_GAME_EVENT_ID, Translator.INST.createGame));
        page.addComponent(new TryButton(CANCEL_EVENT_ID, Translator.INST.cancel));
        return page;
    }
}
