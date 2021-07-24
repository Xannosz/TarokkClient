package hu.xannosz.tarokk.client.android.game;

public enum Team
{
	CALLER, OPPONENT;
	
	public Team getOther()
	{
		return this == CALLER ? OPPONENT : CALLER;
	}
}
