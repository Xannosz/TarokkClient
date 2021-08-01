package hu.xannosz.tarokk.client.network;

import com.tisza.tarock.proto.EventProto;
import com.tisza.tarock.proto.MainProto;
import hu.xannosz.microtools.pack.Douplet;
import hu.xannosz.tarokk.client.android.network.MessageHandler;
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
    private final Map<GamePhase,List<Douplet<Integer, String>>> playerActions = new HashMap<>();
    private List<String> playerCards;
    private EventProto.Event.Chat chat;
    private EventProto.Event.PlayerTeamInfo playerTeamInfo;
    private EventProto.Event.AvailableBids availableBids;
    private EventProto.Event.AvailableCalls availableCalls;
    private EventProto.Event.ChangeDone foldDone;
    private EventProto.Event.SkartTarock skartTarock;
    private EventProto.Event.AvailableAnnouncements availableAnnouncements;
    private EventProto.Event.CardsTaken cardsTaken;
    private EventProto.Event.Statistics statistics;
    private EventProto.Event.PlayerPoints playerPoints;
    private EventProto.Event.PendingNewGame pendingNewGame;

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
                        break;
                    case CHAT:
                        chat = event.getChat();
                        break;
                    case TURN:
                        playerTurn = event.getTurn().getPlayer();
                        break;
                    case PLAYER_TEAM_INFO:
                        playerTeamInfo = event.getPlayerTeamInfo();
                        break;
                    case PLAYER_CARDS:
                        playerCards = event.getPlayerCards().getCardList();
                        break;
                    case PHASE_CHANGED:
                        phase = GamePhase.getPhase(event.getPhaseChanged().getPhase());
                        break;
                    case AVAILABLE_BIDS:
                        availableBids = event.getAvailableBids();
                        break;
                    case AVAILABLE_CALLS:
                        availableCalls = event.getAvailableCalls();
                        break;
                    case FOLD_DONE:
                        foldDone = event.getFoldDone();
                        break;
                    case SKART_TAROCK:
                        skartTarock = event.getSkartTarock();
                        break;
                    case AVAILABLE_ANNOUNCEMENTS:
                        availableAnnouncements = event.getAvailableAnnouncements();
                        break;
                    case CARDS_TAKEN:
                        cardsTaken = event.getCardsTaken();
                        break;
                    case STATISTICS:
                        statistics = event.getStatistics();
                        break;
                    case PLAYER_POINTS:
                        playerPoints = event.getPlayerPoints();
                        break;
                    case PENDING_NEW_GAME:
                        pendingNewGame = event.getPendingNewGame();
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
            case DELETE_GAME_SESSION:
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
}
