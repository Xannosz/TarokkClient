package hu.xannosz.tarokk.client.gui.frame;

import hu.xannosz.tarokk.client.game.GamePhase;
import hu.xannosz.tarokk.client.gui.ConnectionsData;
import hu.xannosz.tarokk.client.gui.Event;
import hu.xannosz.tarokk.client.gui.subframe.*;
import hu.xannosz.tarokk.client.gui.util.PageCreator;
import hu.xannosz.tarokk.client.gui.widget.CardWidget;
import hu.xannosz.tarokk.client.gui.widget.DataWidget;
import hu.xannosz.tarokk.client.gui.widget.HudWidget;
import hu.xannosz.tarokk.client.network.NetworkHandler;
import hu.xannosz.veneos.core.html.structure.Page;
import lombok.Getter;
import lombok.Setter;

import static hu.xannosz.tarokk.client.gui.GuiConstants.CANCEL_EVENT_ID;

public class GameFrame extends Frame {

    @Getter
    private final int gameId;
    @Setter
    private SubFrame subFrame;
    private final CardWidget cardWidget;
    private final DataWidget dataWidget;
    private final HudWidget hudWidget;
    private GamePhase lastPhase;

    public GameFrame(NetworkHandler networkHandler, ConnectionsData connectionsData, int gameId) {
        super(networkHandler, connectionsData);
        this.gameId = gameId;
        this.cardWidget = new CardWidget(networkHandler);
        this.dataWidget = new DataWidget(networkHandler, gameId);
        this.hudWidget = new HudWidget(networkHandler, gameId);
        subFrame = new StartGameSubFrame(networkHandler, connectionsData, this);
        networkHandler.getLiveData().addCallOnUpdate(() -> {
            if (networkHandler.getLiveData().getPhase() != lastPhase) {
                lastPhase = networkHandler.getLiveData().getPhase();
                switch (lastPhase) {
                    case BIDDING:
                        subFrame = new BiddingSubFrame(networkHandler, connectionsData, this);
                        break;
                    case FOLDING:
                        subFrame = new FoldingSubFrame(networkHandler, connectionsData, this);
                        break;
                    case CALLING:
                        subFrame = new CallingSubFrame(networkHandler, connectionsData, this);
                        break;
                    case ANNOUNCING:
                        subFrame = new AnnouncingSubFrame(networkHandler, connectionsData, this);
                        break;
                    case GAMEPLAY:
                        subFrame = new GamePlaySubFrame(networkHandler, connectionsData, this);
                        break;
                    case END:
                        subFrame = new EndSubFrame(networkHandler, connectionsData, this);
                        break;
                }
            }
        });
    }

    @Override
    public Page updatePage() {
        return PageCreator.createGamePage(cardWidget.updateComponent(), dataWidget.updateComponent(),
                subFrame.updateComponent(), hudWidget.updateComponent());
    }

    @Override
    public void handleEvent(Event event) {
        if (event.getEventId().equals(CANCEL_EVENT_ID)) {
            connectionsData.setFrame(new LobbyFrame(networkHandler, connectionsData));
        }
        subFrame.handleEvent(event);
    }
}
