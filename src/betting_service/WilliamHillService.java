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

public class WilliamHillService implements BettingService {
	public static final String PREFIX = "WH"; // SkyBet
	
	public static void main (String args[]) throws IOException{
		// something here maybe
//		Document doc = Jsoup.connect("http://sports.williamhill.com/bet/en-gb/betting/t/295/English+Premier+League.html").get();
//
//		/*
//		 * Typically football matches happen on Saturday, Sunday and Monday(not always Monday)
//		 * "Table" means the HTML table from the web page.
//		 * For now I am assuming there are games every Sat, Sun and Mon
//		 * TODO: Add a check to make sure there are in fact football matches on Sat, Sun, Mon
//		 */
//		
//		List<String> tables = new ArrayList<String>();
//		// Get header to check day
//		Elements temp = doc.select("#tup_mkt_grp_tbl_UC_9d8a08d4b13c912153e27659829a27ad");
//		Element matches = temp.first();
////		System.out.println(matches.text());
//		String allMatches = matches.text();
//		// TODO: Come up with better way to split the data, regex to split on " + Bets" 
//		String[] allFootballMathes = allMatches.split("\\+|UK");
//		for(int i = 0; i < allFootballMathes.length;i++)
//		{
//			String match = allFootballMathes[i];
//			if(i % 2 == 1)
//			{
//				String home;
//				String away;
//				String homeWin;
//				String awayWin;
//				String draw;
//				tables.add(match);
//				String[] info = match.split(" v ");
//				String[] info2 = info[1].split(" ");
////				for(String str : info)
////				System.out.println(info[0]);
////				for(String str : info2)
////					System.out.println(str);
//				
//				if(info.length == 3)
//				{
//					home = info[0] +" "+ info[1];
//				}
//				else
//				{
//					home = info[0];
//				}
//				
//				if(info2.length == 5)
//				{
//					away = info2[0] +" "+ info2[1];
//					homeWin = info2[2];
//					awayWin = info2[3];
//					draw = info2[4];
//				}
//				else
//				{
//					away = info2[0];
//					homeWin = info2[1];
//					awayWin = info2[2];
//					draw = info2[3];
//				}
//				home = home.trim();
//				away = away.trim();
//				home = home.substring(0,home.length()-3);
//				away = away.substring(2);
//				FootballMatch fmatch = new FootballMatch(PREFIX+home+" vs "+away,home,away,homeWin,draw ,awayWin);
//				System.out.println(fmatch.MatchID);
//				System.out.println(fmatch.HomeTeam);
//				System.out.println(fmatch.AwayTeam);
//				System.out.println(fmatch.HomeTeamWin);
//				System.out.println(fmatch.Draw);
//				System.out.println(fmatch.AwayTeamWin);
//				String fixtureHome = fixture.homeTeam.replaceAll("\\s+","");
//				String fixtureAway = fixture.awayTeam.replaceAll("\\s+","");
//				if(home.equals(fixtureHome) && away.equals(fixtureAway)
//						return fmatch;
//			}
			
//			FootballMatch fmatch = new FootballMatch(PREFIX+home+"vs"+away,home,away,homeWin,draw ,awayWin);
//			String homeTeam = match.split(" v | ");
//			String draw = el.child(2).text();
//			String away = el.child(3).text();
//			String homeTeam = home.substring(0, home.lastIndexOf(" ")).replaceAll("\\s+","");
//			String awayTeam = away.substring(0, away.lastIndexOf(" ")).replaceAll("\\s+","");
//			String fixtureHome = fixture.homeTeam.replaceAll("\\s+","");
//			String fixtureAway = fixture.awayTeam.replaceAll("\\s+","");
//		}
//		
//		System.out.println("The Stuf left: "+tables);
	}

	@Override
	public FootballMatch getBettingData(Fixture fixture) throws IOException {
		Document doc = Jsoup.connect("http://sports.williamhill.com/bet/en-gb/betting/t/295/English+Premier+League.html").get();

		/*
		 * Typically football matches happen on Saturday, Sunday and Monday(not always Monday)
		 * "Table" means the HTML table from the web page.
		 * For now I am assuming there are games every Sat, Sun and Mon
		 * TODO: Add a check to make sure there are in fact football matches on Sat, Sun, Mon
		 */
		
		List<String> tables = new ArrayList<String>();
		// Get header to check day
		Elements temp = doc.select("#tup_mkt_grp_tbl_UC_9d8a08d4b13c912153e27659829a27ad");
		Element matches = temp.first();
		//		System.out.println(matches.text());
		String allMatches = matches.text();
		// TODO: Come up with better way to split the data, regex to split on " + Bets" 
		String[] allFootballMathes = allMatches.split("\\+|UK");
		for(int i = 0; i < allFootballMathes.length;i++)
		{
			String match = allFootballMathes[i];
			if(i % 2 == 1)
			{
				String home;
				String away;
				String homeWin;
				String awayWin;
				String draw;
				tables.add(match);
				String[] info = match.split(" v ");
				String[] info2 = info[1].split(" ");
				//				for(String str : info)
				//				System.out.println(info[0]);
				//				for(String str : info2)
				//					System.out.println(str);

				if(info.length == 3)
				{
					home = info[0] +" "+ info[1];
				}
				else
				{
					home = info[0];
				}

				if(info2.length == 5)
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
				home = home.substring(0,home.length()-3);
				away = away.substring(2);
				FootballMatch fmatch = new FootballMatch(PREFIX+home+" vs "+away,home,away,homeWin,draw ,awayWin);
				
				String fixtureHome = fixture.homeTeam.replaceAll("\\s+","");
				String fixtureAway = fixture.awayTeam.replaceAll("\\s+","");
				if(home.equals(fixtureHome) && away.equals(fixtureAway))
				{
					System.out.println(fmatch.MatchID);
					System.out.println(fmatch.HomeTeam);
					System.out.println(fmatch.AwayTeam);
					System.out.println(fmatch.HomeTeamWin);
					System.out.println(fmatch.Draw);
					System.out.println(fmatch.AwayTeamWin);
					System.out.println(fixtureHome+"\n"+fixtureAway);
					return fmatch;
				}
			}
		}
		
		return null;
	}

}
