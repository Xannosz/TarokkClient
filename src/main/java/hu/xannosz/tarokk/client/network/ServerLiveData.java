package hu.xannosz.tarokk.client.network;

import com.tisza.tarock.proto.EventProto;
import com.tisza.tarock.proto.MainProto;
import hu.xannosz.microtools.pack.Douplet;
import hu.xannosz.tarokk.client.android.network.MessageHandler;
import hu.xannosz.tarokk.client.game.Card;
import hu.xannosz.tarokk.client.game.GamePhase;
import hu.xannosz.tarokk.client.tui.TuiClient;
import hu.xannosz.tarokk.client.util.Util;
import lombok.Getter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Getter
public class ServerLiveData implements MessageHandler {

    private List<MainProto.GameSession> gameSessions;
    private MainProto.LoginResult loginResult;
    private Map<Integer, MainProto.User> users;

    //Game data
    private String gameType;
    private int beginnerPlayer;
    private GamePhase phase;
    private int playerTurn;
    private boolean pendingNewGame = false;
    private EventProto.Event.Statistics statistics;
    private List<Integer> availableBids;  //TODO Not used jet.
    private List<Card> availableCalls;
    private List<String> availableAnnouncements;
    private final Map<GamePhase, List<Douplet<Integer, String>>> playerActions = new HashMap<>();
    private final List<Card> playerCard = new ArrayList<>();
    private final List<Integer> foldDone = new ArrayList<>();
    private final Map<Integer, Integer> skartedTarocks = new HashMap<>();
    private final Map<Integer, Boolean> playerTeamInfo = new HashMap<>();
    private final Map<Integer, Integer> cardsTakenUsers = new HashMap<>();
    private final Map<Integer, Integer> playerPoints = new HashMap<>();
    private final Map<Integer, Integer> incrementPlayerPoints = new HashMap<>();
    private final List<Douplet<Integer, String>> turnPlayerActions = new ArrayList<>();
    private final List<Douplet<Integer, String>> chat = new ArrayList<>();  //TODO implement chatting

    private final TuiClient tuiClient;

    public ServerLiveData(TuiClient tuiClient) {
        this.tuiClient = tuiClient;
    }

    @Override
    public void handleMessage(MainProto.Message message) {
        Util.info("Message Type: " + message.getMessageTypeCase());
        Util.debug("Message: " + message);
        switch (message.getMessageTypeCase()) {
            case CREATE_GAME_SESSION:
            case LOGIN:
            case JOIN_GAME_SESSION:
            case DELETE_GAME_SESSION:
            case START_GAME_SESSION_LOBBY:
            case ACTION:
                // Server message
                return;//TODO
            case KEEPALIVE:
                break;
            case LOGIN_RESULT:
                loginResult = message.getLoginResult();
                break;
            case FCM_TOKEN:
                break;
            case EVENT:
                EventProto.Event event = message.getEvent();
                switch (event.getEventTypeCase()) {
                    case START_GAME:
                        gameType = event.getStartGame().getGameType();
                        beginnerPlayer = event.getStartGame().getBeginnerPlayer();
                        break;
                    case PLAYER_ACTION:
                        playerActions.computeIfAbsent(phase, k -> new ArrayList<>());
                        playerActions.get(phase).add(new Douplet<>(event.getPlayerAction().getPlayer(), event.getPlayerAction().getAction()));
                        if (phase.equals(GamePhase.GAMEPLAY)) {
                            turnPlayerActions.add(new Douplet<>(event.getPlayerAction().getPlayer(), event.getPlayerAction().getAction().replace("play:", "")));
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
                        for (String id : event.getPlayerCards().getCardList()) {
                            playerCard.add(Card.parseCard(id));
                        }
                        break;
                    case PHASE_CHANGED:
                        phase = GamePhase.getPhase(event.getPhaseChanged().getPhase());
                        break;
                    case AVAILABLE_BIDS:
                        availableBids = event.getAvailableBids().getBidList();
                        break;
                    case AVAILABLE_CALLS:
                        availableCalls = new ArrayList<>();
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
                        availableAnnouncements = event.getAvailableAnnouncements().getAnnouncementList();
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
                        Util.error("Event type not set: " + message);
                        break;
                }
                EventProto.Event.PhaseChanged phaseChanged = event.getPhaseChanged();
                phaseChanged.getPhase();
                break;
            case SERVER_STATUS:
                gameSessions = message.getServerStatus().getAvailableGameSessionList();
                users = new HashMap<>();
                for (MainProto.User user : message.getServerStatus().getAvailableUserList()) {
                    users.put(user.getId(), user);
                }
                break;
            case JOIN_HISTORY_GAME:
                break;
            case CHAT:
                break;
            case JOIN_HISTORY_GAME_RESULT:
                break;
            case MESSAGETYPE_NOT_SET:
                Util.error("Message type not set: " + message);
                return;
        }
        tuiClient.update();
    }

    @Override
    public void connectionError(ErrorType errorType) {
        Util.error("Error: " + errorType);
    }

    @Override
    public void connectionClosed() {
        Util.info("Connection closed!");
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
