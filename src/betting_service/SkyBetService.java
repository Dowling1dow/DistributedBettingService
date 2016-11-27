package betting_service;

import java.util.ArrayList;
import java.util.List;
import java.io.IOException;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.select.Elements;

import core.BettingService;
import core.Fixture;
import core.FootballMatch;

public class SkyBetService implements BettingService {
	public static final String PREFIX = "SB"; // SkyBet

	@Override
	public FootballMatch getBettingData(Fixture fixture) throws IOException {
		List<FootballMatch> allFootballMatches = new ArrayList<FootballMatch>();
		FootballMatch footballMatch = null;
		Document doc = Jsoup.connect("https://www.skybet.com/football/premier-league").get();
		
		List<Element> tables = new ArrayList<Element>();
		tables.add(doc.select("#0-group > div:nth-child(2) > table > tbody").get(0)); // Sat
		tables.add(doc.select("#0-group > div:nth-child(3) > table > tbody").get(0)); // Sun
		tables.add(doc.select("#0-group > div:nth-child(4) > table > tbody").get(0)); // Mon
		
		for (Element table : tables){
			for(Element el : table.children()){
				String home = el.child(1).text();
				String draw = el.child(2).text();
				String away = el.child(3).text();
				String homeTeam = home.substring(0, home.lastIndexOf(" ")).replaceAll("\\s+","");
				String awayTeam = away.substring(0, away.lastIndexOf(" ")).replaceAll("\\s+","");
				String fixtureHome = fixture.homeTeam.replaceAll("\\s+","");
				String fixtureAway = fixture.awayTeam.replaceAll("\\s+","");
				
				System.out.println("1st Home : "+homeTeam);
				System.out.println("1st Away : "+awayTeam);
				System.out.println("2nd Home : "+fixtureHome);
				System.out.println("2nd Away : "+fixtureAway);
				
				if (homeTeam.equals(fixtureHome) && awayTeam.equals(fixtureAway)){
					String matchID = PREFIX+homeTeam+"vs"+awayTeam;
					String homeTeamWin = home.substring(home.lastIndexOf(' ') + 1);
					String awayTeamWin = away.substring(away.lastIndexOf(' ') + 1);
					String drawMatch = draw.substring(draw.lastIndexOf(' ') + 1);
					footballMatch = new FootballMatch(matchID, homeTeam, awayTeam, homeTeamWin, drawMatch, awayTeamWin);
					allFootballMatches.add(footballMatch);
					System.out.println(homeTeam+" "+awayTeam+" "+matchID);
					return footballMatch;
				}else{
					continue;
				}
			}
		}
		return footballMatch;
	}

}
