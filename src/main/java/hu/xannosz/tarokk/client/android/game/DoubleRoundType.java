package hu.xannosz.tarokk.client.android.game;

public enum DoubleRoundType
{
	NONE, PECULATING, STACKING, MULTIPLYING;

	public String getID()
	{
		switch (this)
		{
			case NONE: return "none";
			case PECULATING: return "peculating";
			case STACKING: return "stacking";
			case MULTIPLYING: return "multiplying";
		}
		throw new RuntimeException();
	}

	public static DoubleRoundType fromID(String id)
	{
		switch (id)
		{
			case "none": return NONE;
			case "peculating": return PECULATING;
			case "stacking": return STACKING;
			case "multiplying": return MULTIPLYING;
		}
		throw new IllegalArgumentException("invalid double round type: " + id);
	}
}
