package betting_service;

import java.util.ArrayList;
import java.util.List;
import java.io.IOException;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import core.BettingService;
import core.Fixture;
import core.FootballMatch;

public class BetVictorService implements BettingService {
	public static final String PREFIX = "BV"; // BetVictor
	
	public static void main (String args[]) throws IOException{
		
	}

	@Override
	public FootballMatch getBettingData(Fixture fixture) throws IOException {
		Document doc = Jsoup.connect("http://www.betvictor.com/en/sports/coupons/english-premier-league").get();
		
//		List<String> tables = new ArrayList<String>();
		Elements temp = doc.select("#three_way_outright_coupon-markets");
		Element matches = temp.first();
		String allMatches = matches.text();
//		System.out.println(allMatches);

		String[] allFootballMathes = allMatches.split("[0-9][0-9]:[0-9][0-9]");
		for(int i = 1; i < allFootballMathes.length;i++)
		{
			String match = allFootballMathes[i];
			{
				String home;
				String away;
				String homeWin;
				String awayWin;
				String draw;
//				tables.add(match);
				String[] info = match.split(" v ");
				String[] info2 = info[1].split(" ");
				home = info[0];

				if(info2.length > 5)
				{
					away = info2[0] +" "+ info2[1];
					homeWin = info2[2];
					awayWin = info2[3];
					draw = info2[4];
				}
				else
				{
					away = info2[0];
					homeWin = info2[1];
					awayWin = info2[2];
					draw = info2[3];
				}
				
				home = home.trim();
				away = away.trim();
				FootballMatch footballMatch = new FootballMatch(PREFIX+home+"vs"+away,home,away,homeWin,draw,awayWin);
				String fixtureHome = fixture.homeTeam.replaceAll("\\s+","");
				String fixtureAway = fixture.awayTeam.replaceAll("\\s+","");
				if(home.equals(fixtureHome) && away.equals(fixtureAway))
				{
					System.out.println(footballMatch.MatchID);
					System.out.println(footballMatch.HomeTeam);
					System.out.println(footballMatch.AwayTeam);
					System.out.println(footballMatch.HomeTeamWin);
					System.out.println(footballMatch.Draw);
					System.out.println(footballMatch.AwayTeamWin);
					System.out.println(fixtureHome+"\n"+fixtureAway);
					return footballMatch;
				}
			}
		}
		return null;
	}

}
