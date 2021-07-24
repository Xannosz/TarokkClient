package hu.xannosz.tarokk.client.android.game.card;

import hu.xannosz.tarokk.client.android.network.message.Action;

import java.util.*;

public abstract class Card implements  Comparable<Card>
{

	public static Map<Card, String> cardToName = new HashMap<>();
	private static final Map<String, Card> idToCard = new LinkedHashMap<>();
	private static final TarockCard[] tarockCards = new TarockCard[22];
	private static final SuitCard[][] suitCards = new SuitCard[4][5];

	public abstract int getPoints();
	public abstract String getID();
	public abstract boolean isHonor();
	public abstract boolean doesBeat(Card otherCard);

	public int hashCode()
	{
		return getID().hashCode();
	}

	public boolean equals(Object o)
	{
		if (!(o instanceof Card)) return false;
		Card other = (Card)o;
		return getID().equals(other.getID());
	}

	public static Collection<Card> getAll()
	{
		return idToCard.values();
	}

	public static TarockCard getTarockCard(int value)
	{
		return tarockCards[value - 1];
	}

	public static SuitCard getSuitCard(int suit, int value)
	{
		return suitCards[suit][value - 1];
	}

	public static Card fromId(String id)
	{
		if (!idToCard.containsKey(id))
			throw new IllegalArgumentException("invalid card: " + id);

		return idToCard.get(id);
	}

	public Action getAction()
	{
		return Action.call(this);
	}

	@Override
	public String toString()
	{
		return cardToName.get(this);
	}

	static
	{
		for (int s = 0; s < 4; s++)
		{
			for (int v = 1; v <= 5; v++)
			{
				SuitCard suitCard = new SuitCard(s, v);
				suitCards[s][v - 1] = suitCard;
				idToCard.put(suitCard.getID(), suitCard);
			}
		}
		for (int v = 1; v <= 22; v++)
		{
			TarockCard tarockCard = new TarockCard(v);
			tarockCards[v - 1] = tarockCard;
			idToCard.put(tarockCard.getID(), tarockCard);
		}
	}
}
