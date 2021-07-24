package hu.xannosz.tarokk.client.android.network.message;

import hu.xannosz.tarokk.client.android.game.Announcement;
import hu.xannosz.tarokk.client.android.game.card.Card;
import hu.xannosz.tarokk.client.android.Utils;

import java.util.*;

public class Action
{
	private final String id;

	public Action(String id)
	{
		this.id = id;

		if (id.length() >= 1024)
			throw new IllegalArgumentException("action id length >= 1024: " + id.length());
	}

	public String getId()
	{
		return id;
	}

	public static Action bid(int bid)
	{
		return new Action("bid:" + (bid < 0 ? "p" : bid));
	}

	public static Action fold(List<Card> cards)
	{
		return new Action("fold:" + String.join(",", Utils.map(cards, Card::getID)));
	}

	public static Action call(Card card)
	{
		return new Action("call:" + card.getID());
	}

	public static Action announce(Announcement announcement)
	{
		return new Action("announce:" + announcement.getID());
	}

	public static Action announcePassz()
	{
		return new Action("announce:passz");
	}

	public static Action play(Card card)
	{
		return new Action("play:" + card.getID());
	}

	public static Action readyForNewGame()
	{
		return new Action("newgame:");
	}

	public static Action throwCards()
	{
		return new Action("throw:");
	}

	public void handle(EventHandler handler, int player)
	{
		int colonIndex = id.indexOf(":");
		String actionType = id.substring(0, colonIndex);
		String actionParams = id.substring(colonIndex + 1);
		switch (actionType)
		{
			case "bid":
				int bid = actionParams.equals("p") ? -1 : Integer.parseInt(actionParams);
				handler.bid(player, bid);
				break;
			case "fold":
				List<Card> cards = new ArrayList<>();
				for (String cardID : actionParams.split(","))
					if (!cardID.isEmpty())
						cards.add(Card.fromId(cardID));
				handler.fold(player, cards);
				break;
			case "call":
				handler.call(player, Card.fromId(actionParams));
				break;
			case "announce":
				if (actionParams.equals("passz"))
					handler.announcePassz(player);
				else
					handler.announce(player, Announcement.fromID(actionParams));
				break;
			case "play":
				handler.playCard(player, Card.fromId(actionParams));
				break;
			case "newgame":
				handler.readyForNewGame(player);
				break;
			case "throw":
				handler.throwCards(player);
				break;
			default:
				System.out.println("Action"+"invalid action: " + actionType);
		}
	}
}
