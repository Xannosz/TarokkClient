package hu.xannosz.tarokk.client.gui;

import hu.xannosz.veneos.core.VeneosServer;
import hu.xannosz.veneos.core.VeneosServerConfig;
import hu.xannosz.veneos.trie.*;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Slf4j
public class GuiClient implements TryHandler {

    private final TryWebSocketServer webSocketServer;
    private final Map<String, ConnectionsData> connectionsDataMap = new HashMap<>();

    public GuiClient() {
        new VeneosServer().createServer(new VeneosServerConfig(this, 8000));
        webSocketServer = new TryWebSocketServer(8400, this);
    }


    @Override
    public ResponseBody handleRequest(RequestBody requestBody) {
        final String sessionId = requestBody.getSessionId();
        if (!connectionsDataMap.containsKey(sessionId)) {
            try {
                connectionsDataMap.put(sessionId, new ConnectionsData(sessionId, webSocketServer));
            } catch (IOException e) {
                log.error("Connection creation failed.", e);
            }
        }
        if (requestBody.getRequestType().equals(RequestTypes.BUTTON_REQUEST)) {
            connectionsDataMap.get(sessionId).handleEvent(new Event(requestBody.getEventId()));
        }
        if (requestBody.getRequestType().equals(RequestTypes.KEY_STROKE_REQUEST)) {
            connectionsDataMap.get(sessionId).handleEvent(
                    new Event(KeyStrokeEvent.getFromMap(requestBody.getAdditionalParams()).getCode()));
        }
        if (requestBody.getRequestType().equals(RequestTypes.ON_CLOSE_WEB_SOCKET_REQUEST)) {
            connectionsDataMap.remove(sessionId);
            return new ResponseBody();
        }
        return new ResponseBody(connectionsDataMap.get(sessionId).updatePage());
    }
}
