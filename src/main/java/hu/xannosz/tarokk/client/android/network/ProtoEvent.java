package hu.xannosz.tarokk.client.android.network;

import com.tisza.tarock.proto.*;
import hu.xannosz.tarokk.client.android.game.PhaseEnum;
import hu.xannosz.tarokk.client.android.game.card.Card;
import hu.xannosz.tarokk.client.android.network.message.Action;
import hu.xannosz.tarokk.client.android.network.message.AnnouncementResult;
import hu.xannosz.tarokk.client.android.network.message.Event;
import hu.xannosz.tarokk.client.android.network.message.EventHandler;
import hu.xannosz.tarokk.client.android.game.GameType;
import hu.xannosz.tarokk.client.android.game.Team;
import hu.xannosz.tarokk.client.android.Utils;

import java.util.*;

public class ProtoEvent implements Event
{
	private final EventProto.Event event;

	public ProtoEvent(EventProto.Event event)
	{
		this.event = event;
	}

	private static List<Card> cardIDsToCards(List<String> cardIDs)
	{
		List<Card> cards = new ArrayList<>();
		for (String cardID : cardIDs)
		{
			cards.add(Card.fromId(cardID));
		}
		return cards;
	}

	@Override
	public void handle(EventHandler handler)
	{
		switch (event.getEventTypeCase())
		{
			case START_GAME:
				EventProto.Event.StartGame startGame = event.getStartGame();
				handler.startGame(GameType.fromID(startGame.getGameType()), startGame.getBeginnerPlayer());
				break;
			case TURN:
				handler.turn(event.getTurn().getPlayer());
				break;
			case PLAYER_TEAM_INFO:
				EventProto.Event.PlayerTeamInfo playerTeamInfo = event.getPlayerTeamInfo();
				handler.playerTeamInfo(playerTeamInfo.getPlayer(), playerTeamInfo.getIsCaller() ? Team.CALLER : Team.OPPONENT);
				break;
			case PLAYER_CARDS:
				handler.cardsChanged(cardIDsToCards(event.getPlayerCards().getCardList()), event.getPlayerCards().getCanBeThrown());
				break;
			case PHASE_CHANGED:
				handler.phaseChanged(PhaseEnum.fromID(event.getPhaseChanged().getPhase()));
				break;
			case AVAILABLE_BIDS:
				handler.availableBids(event.getAvailableBids().getBidList());
				break;
			case AVAILABLE_CALLS:
				handler.availableCalls(cardIDsToCards(event.getAvailableCalls().getCardList()));
				break;
			case FOLD_DONE:
				handler.foldDone(event.getFoldDone().getPlayer());
				break;
			case SKART_TAROCK:
				int[] tarockCounts = new int[4];
				for (int i = 0; i < 4; i++)
				{
					tarockCounts[i] = event.getSkartTarock().getCount(i);
				}
				handler.foldTarock(tarockCounts);
				break;
			case AVAILABLE_ANNOUNCEMENTS:
				handler.availableAnnouncements(Utils.announcementListFromProto(event.getAvailableAnnouncements().getAnnouncementList()));
				break;
			case CARDS_TAKEN:
				handler.cardsTaken(event.getCardsTaken().getPlayer());
				break;
			case STATISTICS:
				EventProto.Event.Statistics statisticsEvent = event.getStatistics();
				int callerGamePoints = statisticsEvent.getCallerGamePoints();
				int opponentGamePoints = statisticsEvent.getOpponentGamePoints();
				List<AnnouncementResult> announcementResults = Utils.staticticsListFromProto(statisticsEvent.getAnnouncementResultList());
				int sumPoints = statisticsEvent.getSumPoints();
				int pointsMultiplier = statisticsEvent.getPointMultiplier();
				handler.statistics(callerGamePoints, opponentGamePoints, announcementResults, sumPoints, pointsMultiplier);
				break;
			case PLAYER_POINTS:
				EventProto.Event.PlayerPoints playerPointsEvent = event.getPlayerPoints();
				List<Integer> points = playerPointsEvent.getPointList();
				List<Integer> incrementPoints = playerPointsEvent.getIncrementPointsList();
				handler.playerPoints(points, incrementPoints);
				break;
			case PENDING_NEW_GAME:
				handler.pendingNewGame();
				break;
			case CHAT:
				handler.chat(event.getChat().getUserId(), event.getChat().getMessage());
				break;
			case PLAYER_ACTION:
				handlePlayerAction(handler, event.getPlayerAction().getPlayer(), event.getPlayerAction().getAction());
				break;
			default:
				System.err.println("unhandled event");
				break;
		}
	}

	private void handlePlayerAction(EventHandler handler, int player, String action)
	{
		new Action(action).handle(handler, player);
	}
}
