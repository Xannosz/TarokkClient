package hu.xannosz.tarokk.client.gui.util;

import com.tisza.tarock.proto.MainProto;
import lombok.experimental.UtilityClass;

import java.util.concurrent.ConcurrentLinkedQueue;

@UtilityClass
public class DataUtil {
    public static int getGameId(ConcurrentLinkedQueue<MainProto.GameSession> gameSessions, int userId) {
        int gameId = 0;
        for (MainProto.GameSession gameData : gameSessions) {
            if (gameData.getState() == MainProto.GameSession.State.LOBBY && gameData.getUserIdList().contains(userId)) {
                gameId = gameData.getId();
            }
        }
        return gameId;
    }
}
