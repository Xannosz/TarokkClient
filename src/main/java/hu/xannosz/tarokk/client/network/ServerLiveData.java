package hu.xannosz.tarokk.client.network;

import com.tisza.tarock.proto.MainProto;
import hu.xannosz.tarokk.client.android.network.MessageHandler;
import hu.xannosz.tarokk.client.tui.TuiClient;
import hu.xannosz.tarokk.client.util.Util;
import lombok.Getter;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Getter
public class ServerLiveData implements MessageHandler {

    private List<MainProto.GameSession> gameSessions;
    private MainProto.LoginResult loginResult;
    private Map<Integer, MainProto.User> users;

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
                // Server message
                return;
            case KEEPALIVE:
                break;
            case LOGIN_RESULT:
                loginResult = message.getLoginResult();
                break;
            case FCM_TOKEN:
                break;
            case ACTION:
                break;
            case EVENT:
                break;
            case SERVER_STATUS:
                gameSessions = message.getServerStatus().getAvailableGameSessionList();
                users = new HashMap<>();
                for (MainProto.User user : message.getServerStatus().getAvailableUserList()) {
                    users.put(user.getId(),user);
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
