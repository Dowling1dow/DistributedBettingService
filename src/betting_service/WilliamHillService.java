package betting_service;

import java.io.IOException;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import core.BettingService;
import core.Fixture;
import core.FootballMatch;

public class WilliamHillService implements BettingService {
	public static final String PREFIX = "WH"; // WilliamHill
	
	@Override
	public FootballMatch getBettingData(Fixture fixture) throws IOException {
		Document doc = Jsoup.connect("http://sports.williamhill.com/bet/en-gb/betting/t/295/English+Premier+League.html").get();
		FootballMatch footballMatch = null;
		// Get header to check day
		Elements temp = doc.select("#tup_mkt_grp_tbl_UC_9d8a08d4b13c912153e27659829a27ad");
		Element matches = temp.first();
		String allMatches = matches.text(); 
		String[] allFootballMathes = allMatches.split("\\+|UK");
		for(int i = 1; i < allFootballMathes.length;i+=2)
		{
			String match = allFootballMathes[i];{
				String home;
				String away;
				String homeWin;
				String awayWin;
				String draw;
//				tables.add(match);
				String[] info = match.split("   v   ");
				String[] info2 = info[1].split(" ");
				home = info[0];
				
				if(info2.length > 4){
					away = info2[0] +" "+ info2[1];
					homeWin = info2[2];
					awayWin = info2[3];
					draw = info2[4];
				}else{
					away = info2[0];
					homeWin = info2[1];
					awayWin = info2[2];
					draw = info2[3];
				}
				
				home = home.trim();
				away = away.trim();
				String homeTeam = home.replaceAll("\\s+","");
				String awayTeam = away.replaceAll("\\s+","");
				String fixtureHome = fixture.homeTeam.replaceAll("\\s+","");
				String fixtureAway = fixture.awayTeam.replaceAll("\\s+","");
				System.out.println("WH "+fixtureHome);
				System.out.println("WH "+fixtureAway);
				
				// Dealing with name differences
				if(homeTeam.equals("ManCity")){ homeTeam = "ManchesterCity";}
				else if(homeTeam.equals("ManUtd")){ homeTeam = "ManchesterUnited";}
				
				if(awayTeam.equals("ManCity")){ awayTeam = "ManchesterCity";}
				else if(awayTeam.equals("ManUtd")){ awayTeam = "ManchesterUnited";}
				
				footballMatch = new FootballMatch(PREFIX+fixtureHome+"vs"+fixtureAway,fixtureHome,fixtureAway,homeWin,draw ,awayWin);
				
				if (homeTeam.equals(fixtureHome) || fixtureHome.toLowerCase().contains(homeTeam.toLowerCase()) && 
						awayTeam.equals(fixtureAway) || fixtureAway.toLowerCase().contains(awayTeam.toLowerCase())){
//					System.out.println(footballMatch.MatchID);
//					System.out.println(footballMatch.HomeTeam);
//					System.out.println(footballMatch.AwayTeam);
//					System.out.println(footballMatch.HomeTeamWin);
//					System.out.println(footballMatch.Draw);
//					System.out.println(footballMatch.AwayTeamWin);
//					System.out.println(fixtureHome+"\n"+fixtureAway);
					return footballMatch;
				}
			}
		}
		return footballMatch;
	}

}
