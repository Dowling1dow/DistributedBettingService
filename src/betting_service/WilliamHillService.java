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
	
	public static void main (String args[]) throws IOException{
		// something here maybe
	}

	@Override
	public FootballMatch getBettingData(Fixture fixture) throws IOException {
//		List<List<String>> allFootballMatches = new ArrayList<List<String>>();
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
		System.out.println(matches.text());
		String allMatches = matches.text();
		// TODO: Come up with better way to split the data, regex to split on " + Bets" 
		String[] allFootballMathes = allMatches.split("\\+");
		for (String match : allFootballMathes){
			tables.add(match);
		}
		
		return null;
	}

}
