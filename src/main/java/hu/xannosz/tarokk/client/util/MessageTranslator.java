package hu.xannosz.tarokk.client.util;

import com.tisza.tarock.proto.MainProto;
import hu.xannosz.tarokk.client.game.DoubleRoundType;
import hu.xannosz.tarokk.client.game.GameType;
import hu.xannosz.tarokk.client.network.Action;

public class MessageTranslator {
    public static MainProto.Message fbLogin(String token){
        return MainProto.Message.newBuilder().setLogin( MainProto.Login.newBuilder().setFacebookToken(token).build()).build();
    }

    public static MainProto.Message listLobby(){
        return MainProto.Message.newBuilder().setStartGameSessionLobby(MainProto.StartGameSessionLobby.getDefaultInstance()).build();
    }

    public static MainProto.Message joinToGame(int id) {
        return MainProto.Message.newBuilder().setJoinGameSession(MainProto.JoinGameSession.newBuilder().setGameSessionId(id).build()).build();
    }

    public static MainProto.Message newGame(DoubleRoundType doubleRoundType, GameType type) {
        return MainProto.Message.newBuilder().setCreateGameSession(MainProto.CreateGameSession.newBuilder()
                .setDoubleRoundType(doubleRoundType.getName()).setType(type.getName()).build()).build();
    }

    public static MainProto.Message sendAction(Action action) {
        return MainProto.Message.newBuilder().setAction(action.getId()).build();
    }
}
