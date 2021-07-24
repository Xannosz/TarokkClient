package hu.xannosz.tarokk.client.util;

import com.tisza.tarock.proto.MainProto;

public class MessageTranslator {
    public static MainProto.Message fbLogin(String token){
        return MainProto.Message.newBuilder().setLogin( MainProto.Login.newBuilder().setFacebookToken(token).build()).build();
    }

    public static MainProto.Message listLobby(){
        return MainProto.Message.newBuilder().setStartGameSessionLobby(MainProto.StartGameSessionLobby.getDefaultInstance()).build();
    }
}
