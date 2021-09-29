package hu.xannosz.tarokk.client.gui.util;

import com.tisza.tarock.proto.MainProto;
import hu.xannosz.tarokk.client.game.DoubleRoundType;
import hu.xannosz.tarokk.client.game.GameType;
import hu.xannosz.tarokk.client.util.Util;
import hu.xannosz.tarokk.client.util.translator.Translator;
import hu.xannosz.veneos.core.html.HtmlComponent;
import hu.xannosz.veneos.core.html.str.P;
import hu.xannosz.veneos.core.html.structure.Page;
import hu.xannosz.veneos.trie.TryButton;
import hu.xannosz.veneos.util.Scripts;
import lombok.experimental.UtilityClass;

import java.util.List;
import java.util.concurrent.ConcurrentMap;

import static hu.xannosz.tarokk.client.gui.GuiConstants.*;

@UtilityClass
public class PageCreator {

    private static final String SCRIPT = Scripts.getCreateWebSocketScript("localhost:" + 8400);

    public static Page createLoginPage() {
        Page page = new Page();
        page.addComponent(new TryButton(LOGIN_EVENT_ID, "Login"));
        page.addScript(SCRIPT);
        return page;
    }

    public static Page createLobbyPage(List<MainProto.GameSession> gameSessions, int selectedGame,
                                       ConcurrentMap<Integer, MainProto.User> users, MainProto.LoginResult loginResult) {
        Page page = new Page();
        if (Util.anyNull(loginResult)) {
            page.addComponent(new P(Translator.INST.notLoggedIn));
        } else {
            page.addComponent(new P(Translator.INST.userId + loginResult.getUserId()));
            if (loginResult.getUserId() == 0) {
                page.addComponent(new P(Translator.INST.notLoggedIn));
            }
            if (!Util.anyNull(gameSessions, users)) {
                page.addComponent(DataToComponent.createGameListComponent(gameSessions, users, selectedGame, loginResult.getUserId()));
                page.addComponent(DataToComponent.createNameListComponent(users, loginResult.getUserId()));
                page.addComponent(new TryButton(CREATE_GAME_EVENT_ID, Translator.INST.createGame));
            }
        }
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

    public static Page createGamePage(HtmlComponent cards, HtmlComponent data, HtmlComponent subFrame, HtmlComponent hud) {
        Page page = new Page();
        page.addComponent(cards);
        page.addComponent(data);
        page.addComponent(subFrame);
        page.addComponent(hud);
        return page;
    }
}
