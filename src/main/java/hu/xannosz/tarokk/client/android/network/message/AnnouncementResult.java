package hu.xannosz.tarokk.client.android.network.message;
import hu.xannosz.tarokk.client.android.game.Announcement;
import hu.xannosz.tarokk.client.android.game.Team;

public class AnnouncementResult
{
	private Announcement announcement;
	private int points;
	private Team team;

	public AnnouncementResult(Announcement announcement, int points, Team team)
	{
		super();
		this.announcement = announcement;
		this.points = points;
		this.team = team;
	}

	public Announcement getAnnouncement()
	{
		return announcement;
	}

	public int getPoints()
	{
		return points;
	}

	public Team getTeam()
	{
		return team;
	}
}
