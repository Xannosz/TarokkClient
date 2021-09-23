package hu.xannosz.tarokk.client.network;

import hu.xannosz.tarokk.client.game.Announcement;
import hu.xannosz.tarokk.client.game.Card;
import hu.xannosz.tarokk.client.game.DoubleRoundType;
import hu.xannosz.tarokk.client.game.GameType;
import hu.xannosz.tarokk.client.util.Util;
import lombok.Getter;

import java.io.IOException;
import java.util.List;

public class NetworkHandler {
    private final ProtoConnection connection;
    @Getter
    private final ServerLiveData liveData;

    public NetworkHandler(Runnable runOnUpdate) throws IOException {
        liveData = new ServerLiveData();
        liveData.addCallOnUpdate(runOnUpdate);
        connection = Util.createProtoConnection(liveData);
    }

    public void fbLogin(String token) {
        connection.sendMessage(Messages.fbLogin(token));
    }

    public void joinToGame(int id) {
        connection.sendMessage(Messages.joinToGame(id));
    }

    public void deleteToGame(int id) {
        connection.sendMessage(Messages.deleteToGame(id));
    }

    public void newGame(DoubleRoundType doubleRoundType, GameType type) {
        connection.sendMessage(Messages.newGame(doubleRoundType, type));
    }

    public void startGame() {
        connection.sendMessage(Messages.startGame());
    }

    public void bid(int bid) {
        sendAction(Action.bid(bid));
    }

    public void fold(List<Card> cards) {
        sendAction(Action.fold(cards));
    }

    public void call(Card card) {
        sendAction(Action.call(card));
    }

    public void announce(Announcement announcement) {
        sendAction(Action.announce(announcement));
    }

    public void announcePass() {
        sendAction(Action.announcePass());
    }

    public void play(Card card) {
        sendAction(Action.play(card));
    }

    public void readyForNewGame() {
        sendAction(Action.readyForNewGame());
    }

    public void throwCards() {
        sendAction(Action.throwCards());
    }

    private void sendAction(Action action) {
        connection.sendMessage(Messages.sendAction(action));
    }
}
