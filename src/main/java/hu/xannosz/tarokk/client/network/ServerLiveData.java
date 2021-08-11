package hu.xannosz.tarokk.client.network;

import com.tisza.tarock.proto.EventProto;
import com.tisza.tarock.proto.MainProto;
import hu.xannosz.microtools.pack.Douplet;
import hu.xannosz.tarokk.client.android.legacy.MessageHandler;
import hu.xannosz.tarokk.client.game.Card;
import hu.xannosz.tarokk.client.game.GamePhase;
import hu.xannosz.tarokk.client.tui.TuiClient;
import hu.xannosz.tarokk.client.util.Util;
import lombok.Getter;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ConcurrentMap;

@Getter
public class ServerLiveData implements MessageHandler {
//TODO use fold method
    private ConcurrentLinkedQueue<MainProto.GameSession> gameSessions;
    private MainProto.LoginResult loginResult;
    private ConcurrentMap<Integer, MainProto.User> users;

    //Game data
    private String gameType;
    private int beginnerPlayer;
    private GamePhase phase;
    private int playerTurn;
    private boolean pendingNewGame = false;
    private EventProto.Event.Statistics statistics;
    private ConcurrentLinkedQueue<Integer> availableBids;
    private ConcurrentLinkedQueue<Card> availableCalls;
    private ConcurrentLinkedQueue<String> availableAnnouncements;
    private final ConcurrentMap<String, ConcurrentLinkedQueue<Douplet<Integer, String>>> playerActions = new ConcurrentHashMap<>();
    private final ConcurrentLinkedQueue<Card> playerCard = new ConcurrentLinkedQueue<>();
    private final ConcurrentLinkedQueue<Integer> foldDone = new ConcurrentLinkedQueue<>();
    private final ConcurrentMap<Integer, Integer> skartedTarocks = new ConcurrentHashMap<>();
    private final ConcurrentMap<Integer, Boolean> playerTeamInfo = new ConcurrentHashMap<>();
    private final ConcurrentMap<Integer, Integer> cardsTakenUsers = new ConcurrentHashMap<>();
    private final ConcurrentMap<Integer, Integer> playerPoints = new ConcurrentHashMap<>();
    private final ConcurrentMap<Integer, Integer> incrementPlayerPoints = new ConcurrentHashMap<>();
    private final ConcurrentLinkedQueue<Douplet<Integer, String>> turnPlayerActions = new ConcurrentLinkedQueue<>();
    private final ConcurrentLinkedQueue<Douplet<Integer, String>> chat = new ConcurrentLinkedQueue<>();  //TODO implement chatting

    private final TuiClient tuiClient;

    public ServerLiveData(TuiClient tuiClient) {
        this.tuiClient = tuiClient;
    }

    @Override
    public void handleMessage(MainProto.Message message) {
        Util.Log.logMessage("Message("+message.getMessageTypeCase()+"): " + message);
        switch (message.getMessageTypeCase()) {
            case CREATE_GAME_SESSION:
            case LOGIN:
            case JOIN_GAME_SESSION:
            case DELETE_GAME_SESSION:
            case START_GAME_SESSION_LOBBY:
            case ACTION:
            case KEEPALIVE:
            case FCM_TOKEN:
            case JOIN_HISTORY_GAME:
                // Server message
                return;
            case LOGIN_RESULT:
                loginResult = message.getLoginResult();
                break;
            case EVENT:
                EventProto.Event event = message.getEvent();
                switch (event.getEventTypeCase()) {
                    case START_GAME:
                        gameType = event.getStartGame().getGameType();
                        beginnerPlayer = event.getStartGame().getBeginnerPlayer();
                        break;
                    case PLAYER_ACTION:
                        String[] action = event.getPlayerAction().getAction().split(":");
                        playerActions.computeIfAbsent(action[0], k -> new ConcurrentLinkedQueue<>());
                        playerActions.get(action[0]).add(new Douplet<>(event.getPlayerAction().getPlayer(), action.length > 1 ? action[1] : action[0]));

                        if (action[0].equals("play")) {
                            turnPlayerActions.add(new Douplet<>(event.getPlayerAction().getPlayer(), action.length > 1 ? action[1] : action[0]));
                        }
                        break;
                    case CHAT:
                        chat.add(new Douplet<>(event.getChat().getUserId(), event.getChat().getMessage().replace("chat:", "")));
                        break;
                    case TURN:
                        playerTurn = event.getTurn().getPlayer();
                        break;
                    case PLAYER_TEAM_INFO:
                        playerTeamInfo.put(event.getPlayerTeamInfo().getPlayer(), event.getPlayerTeamInfo().getIsCaller());
                        break;
                    case PLAYER_CARDS:
                        playerCard.clear();
                        for (String id : event.getPlayerCards().getCardList()) {
                            playerCard.add(Card.parseCard(id));
                        }
                        break;
                    case PHASE_CHANGED:
                        phase = GamePhase.getPhase(event.getPhaseChanged().getPhase());
                        break;
                    case AVAILABLE_BIDS:
                        availableBids = new ConcurrentLinkedQueue<>(event.getAvailableBids().getBidList());
                        break;
                    case AVAILABLE_CALLS:
                        availableCalls = new ConcurrentLinkedQueue<>();
                        for (String card : event.getAvailableCalls().getCardList()) {
                            availableCalls.add(Card.parseCard(card));
                        }
                        break;
                    case FOLD_DONE:
                        foldDone.add(event.getFoldDone().getPlayer());
                        break;
                    case SKART_TAROCK:
                        for (int player = 0; player < event.getSkartTarock().getCountCount(); player++) {
                            skartedTarocks.put(player, event.getSkartTarock().getCount(player));
                        }
                        break;
                    case AVAILABLE_ANNOUNCEMENTS:
                        availableAnnouncements = new ConcurrentLinkedQueue<>(event.getAvailableAnnouncements().getAnnouncementList());
                        break;
                    case CARDS_TAKEN:
                        int player = event.getCardsTaken().getPlayer();
                        if (cardsTakenUsers.containsKey(player)) {
                            cardsTakenUsers.put(event.getCardsTaken().getPlayer(), cardsTakenUsers.get(event.getCardsTaken().getPlayer()) + 1);
                        } else {
                            cardsTakenUsers.put(event.getCardsTaken().getPlayer(), 1);
                        }
                        turnPlayerActions.clear();
                        break;
                    case STATISTICS:
                        statistics = event.getStatistics();
                        break;
                    case PLAYER_POINTS:
                        for (int i = 0; i < event.getPlayerPoints().getPointCount(); i++) {
                            playerPoints.put(i, event.getPlayerPoints().getPoint(i));
                        }
                        for (int i = 0; i < event.getPlayerPoints().getIncrementPointsCount(); i++) {
                            incrementPlayerPoints.put(i, event.getPlayerPoints().getIncrementPoints(i));
                        }
                        break;
                    case PENDING_NEW_GAME:
                        pendingNewGame = true;
                        break;
                    case EVENTTYPE_NOT_SET:
                        Util.Log.logError("Event type not set: " + message);
                        break;
                }
                EventProto.Event.PhaseChanged phaseChanged = event.getPhaseChanged();
                phaseChanged.getPhase();
                break;
            case SERVER_STATUS:
                gameSessions = new ConcurrentLinkedQueue<>(message.getServerStatus().getAvailableGameSessionList());
                users = new ConcurrentHashMap<>();
                for (MainProto.User user : message.getServerStatus().getAvailableUserList()) {
                    users.put(user.getId(), user);
                }
                break;//TODO
            case CHAT:
                break;
            case JOIN_HISTORY_GAME_RESULT:
                break;
            case MESSAGETYPE_NOT_SET:
                Util.Log.logError("Message type not set: " + message);
                return;
        }
        tuiClient.update();
    }

    @Override
    public void connectionError(ErrorType errorType) {
        Util.Log.logError("Error: " + errorType);
    }

    @Override
    public void connectionClosed() {
        Util.Log.logMessage("Connection closed!");
    }

    public void clearGameData() {
        playerActions.clear();
        playerCard.clear();
        foldDone.clear();
        skartedTarocks.clear();
        playerTeamInfo.clear();
        cardsTakenUsers.clear();
        playerPoints.clear();
        incrementPlayerPoints.clear();
        turnPlayerActions.clear();
        chat.clear();
        pendingNewGame = false;
    }
}
