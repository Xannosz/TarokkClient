package hu.xannosz.tarokk.client.android.game;


import hu.xannosz.tarokk.client.android.network.message.Action;

public interface ActionButtonItem
{
	public Action getAction();
	public String toString();
	public default void onClicked() {};
}
