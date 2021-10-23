package hu.xannosz.tarokk.client.gui.util;

import com.tisza.tarock.proto.MainProto;
import hu.xannosz.tarokk.client.game.DoubleRoundType;
import hu.xannosz.tarokk.client.game.GameType;
import hu.xannosz.tarokk.client.util.Util;
import hu.xannosz.tarokk.client.util.translator.Translator;
import hu.xannosz.veneos.core.html.HtmlComponent;
import hu.xannosz.veneos.core.html.box.Div;
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
        page.addTheme(ThemeCreator.createDefaultTheme());
        page.addComponent(new TryButton(LOGIN_EVENT_ID, "Login"));
        page.addScript(SCRIPT);
        return page;
    }

    public static Page createLobbyPage(List<MainProto.GameSession> gameSessions, int selectedGame,
                                       ConcurrentMap<Integer, MainProto.User> users, MainProto.LoginResult loginResult) {
        Page page = new Page();
        page.addTheme(ThemeCreator.createDefaultTheme());
        if (Util.anyNull(loginResult)) {
            page.addComponent(new P(Translator.INST.notLoggedIn));
        } else {
            page.addComponent(new P(Translator.INST.userId + loginResult.getUserId()));
            if (loginResult.getUserId() == 0) {
                page.addComponent(new P(Translator.INST.notLoggedIn));
            }
            if (!Util.anyNull(gameSessions, users)) {
                Div container = new Div();
                container.addClass(CONTAINER_CLAZZ);
                container.add(DataToComponent.createGameListComponent(gameSessions, users, selectedGame, loginResult.getUserId()));
                container.add(DataToComponent.createNameListComponent(users, loginResult.getUserId()));
                page.addComponent(container);
                page.addComponent(new TryButton(CREATE_GAME_EVENT_ID, Translator.INST.createGame));
            }
        }
        return page;
    }

    public static Page createNewGamePage(GameType gameType, DoubleRoundType doubleRoundType) {
        Page page = new Page();
        page.addTheme(ThemeCreator.createDefaultTheme());
        page.addComponent(DataToComponent.createGameTypeComponent(gameType));
        page.addComponent(DataToComponent.createDoubleRoundTypeComponent(doubleRoundType));
        page.addComponent(new TryButton(CREATE_GAME_EVENT_ID, Translator.INST.createGame));
        page.addComponent(new TryButton(CANCEL_EVENT_ID, Translator.INST.cancel));
        return page;
    }

    public static Page createGamePage(HtmlComponent cards, HtmlComponent data, HtmlComponent subFrame, HtmlComponent hud, boolean end) {
        Page page = new Page();
        page.addTheme(ThemeCreator.createDefaultTheme());

        Div container = new Div();
        container.addClass(CONTAINER_CLAZZ);

        container.add(cards.addClass(CONTAINER_SUB_CLAZZ));
        container.add(data.addClass(CONTAINER_SUB_CLAZZ));
        container.add(subFrame.addClass(CONTAINER_SUB_CLAZZ));
        container.add(hud.addClass(CONTAINER_SUB_CLAZZ));
        container.add(new TryButton(CANCEL_EVENT_ID, Translator.INST.cancel).addClass(CONTAINER_SUB_CLAZZ));
        if (end) {
            container.add(new TryButton(START_GAME_EVENT_ID, Translator.INST.newGame).addClass(CONTAINER_SUB_CLAZZ));
        }

        page.addComponent(container);
        return page;
    }
}
