package hu.xannosz.tarokk.client.gui;

import hu.xannosz.tarokk.client.network.NetworkHandler;
import hu.xannosz.veneos.core.html.structure.Page;
import lombok.Data;

@Data
public class ConnectionsData {
   private NetworkHandler networkHandler;
   private Page page;
}
