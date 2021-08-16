package hu.xannosz.tarokk.client.tui.subframe;

import com.googlecode.lanterna.gui2.Component;
import com.googlecode.lanterna.gui2.GridLayout;
import com.googlecode.lanterna.gui2.Label;
import com.googlecode.lanterna.gui2.Panel;
import com.googlecode.lanterna.input.KeyStroke;
import com.googlecode.lanterna.input.KeyType;
import hu.xannosz.tarokk.client.game.Card;
import hu.xannosz.tarokk.client.network.Action;
import hu.xannosz.tarokk.client.network.Messages;
import hu.xannosz.tarokk.client.tui.TuiClient;
import hu.xannosz.tarokk.client.util.ThemeHandler;
import hu.xannosz.tarokk.client.util.Util;
import hu.xannosz.tarokk.client.util.translator.Translator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CallingSubFrame extends SubFrame {
    private int page = 0;
    private List<Card> availableCards;

    public CallingSubFrame(TuiClient tuiClient) {
        super(tuiClient);
    }

    @Override
    public Component getPanel() {
        Panel panel = new Panel();
        panel.setLayoutManager(new GridLayout(3));

        if (Util.anyNull(tuiClient.getServerLiveData().getAvailableCalls())) {
            return panel;
        }

        availableCards = new ArrayList<>(tuiClient.getServerLiveData().getAvailableCalls());

        resetPager();

        for (int i = 0; i < availableCards.size(); i++) {
            if (i == page) {
                panel.addComponent(new Label(availableCards.get(i).getFormattedName()).setTheme(ThemeHandler.getHighLightedThemeMainPanel()));
            } else {
                panel.addComponent(new Label(availableCards.get(i).getFormattedName()));
            }
        }
        return panel;
    }

    @Override
    public Map<String, String> getFooter() {
        Map<String, String> response = new HashMap<>();
        response.put(Translator.INST.arrows, Translator.INST.movement);
        response.put("Enter", Translator.INST.sendCalling);
        return response;
    }

    @Override
    public void handleKeyStroke(KeyStroke keyStroke) {
        if (keyStroke.getKeyType().equals(KeyType.ArrowUp) || keyStroke.getKeyType().equals(KeyType.ArrowLeft)) {
            page--;
        }
        if (keyStroke.getKeyType().equals(KeyType.ArrowDown) || keyStroke.getKeyType().equals(KeyType.ArrowRight)) {
            page++;
        }
        resetPager();
        if (keyStroke.getKeyType().equals(KeyType.Enter)) {
            tuiClient.getConnection().sendMessage(Messages.sendAction(Action.call(availableCards.get(page))));
        }
    }

    private void resetPager() {
        page = Math.max(page, 0);
        page = Math.min(page, availableCards.size() - 1);
    }
}
