package core;

public class FootballMatch {
	
	public FootballMatch(String MatchID, String HomeTeam, String AwayTeam,String HomeTeamWin, String Draw, String AwayTeamWin){
		this.MatchID = MatchID;
		this.HomeTeam = HomeTeam;
		this.AwayTeam = AwayTeam;
		this.HomeTeamWin = HomeTeamWin;
		this.Draw = Draw;
		this.AwayTeamWin = AwayTeamWin;
	}
	
	public String MatchID;
	public String HomeTeam;
	public String AwayTeam;
	public String HomeTeamWin;
	public String AwayTeamWin;
	public String Draw;

}
