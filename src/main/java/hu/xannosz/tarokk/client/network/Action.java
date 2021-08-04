package hu.xannosz.tarokk.client.network;

import hu.xannosz.tarokk.client.android.Utils;
import hu.xannosz.tarokk.client.android.game.Announcement;
import hu.xannosz.tarokk.client.game.Card;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class Action {
    private final String id;

    public static Action bid(int bid) {
        return new Action("bid:" + (bid < 0 ? "p" : bid));
    }

    public static Action fold(List<Card> cards) {
        return new Action("fold:" + String.join(",", Utils.map(cards, Card::getId)));
    }

    public static Action call(Card card) {
        return new Action("call:" + card.getId());
    }

    public static Action announce(Announcement announcement) {
        return new Action("announce:" + announcement.getID());
    }

    public static Action announcePass() {
        return new Action("announce:passz");
    }

    public static Action play(Card card) {
        return new Action("play:" + card.getId());
    }

    public static Action readyForNewGame() {
        return new Action("newgame:");
    }

    public static Action throwCards() {
        return new Action("throw:");
    }
}
