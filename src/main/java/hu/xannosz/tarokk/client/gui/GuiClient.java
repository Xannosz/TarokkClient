package hu.xannosz.tarokk.client.gui;

import hu.xannosz.veneos.core.VeneosServer;
import hu.xannosz.veneos.core.VeneosServerConfig;
import hu.xannosz.veneos.trie.RequestBody;
import hu.xannosz.veneos.trie.ResponseBody;
import hu.xannosz.veneos.trie.TryHandler;
import hu.xannosz.veneos.trie.TryWebSocketServer;

import java.util.HashMap;
import java.util.Map;

public class GuiClient implements TryHandler {

    private final TryWebSocketServer webSocketServer;
    private final Map<String,ConnectionsData> connectionsDataMap = new HashMap<>();

    public GuiClient(){
        new VeneosServer().createServer(new VeneosServerConfig(this,8000));
        webSocketServer = new TryWebSocketServer(8400,this);
    }

    @Override
    public ResponseBody handleRequest(RequestBody requestBody) {
        return null;
    }
}
