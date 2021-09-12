package hu.xannosz.tarokk.client.gui;

import hu.xannosz.tarokk.client.gui.frame.Frame;
import hu.xannosz.tarokk.client.gui.frame.LoginFrame;
import hu.xannosz.tarokk.client.network.NetworkHandler;
import hu.xannosz.veneos.core.html.structure.Page;
import hu.xannosz.veneos.trie.TryWebSocketServer;
import lombok.Data;

import java.io.IOException;

@Data
public class ConnectionsData {
    private final NetworkHandler networkHandler;
    private Frame frame;

    public ConnectionsData(String sessionId, TryWebSocketServer webSocketServer) throws IOException {
        networkHandler = new NetworkHandler(() -> webSocketServer.sendRefresh(sessionId));
        frame = new LoginFrame(networkHandler, this);
    }

    public Page updatePage() {
        return frame.updatePage();
    }

    public void handleEvent(Event event) {
        frame.handleEvent(event);
    }
}
