package betting_service;

import java.util.ArrayList;
import java.util.List;
import java.io.IOException;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.select.Elements;

public class SkyBetService implements BettingService {
	
	public static void main (String args[]) throws IOException{
		List<List<String>> allFootballMatches = new ArrayList<List<String>>();
		Document doc = Jsoup.connect("https://www.skybet.com/football/premier-league").get();

		/*
		 * Typically football matches happen on Saturday, Sunday and Monday(not always Monday)
		 * "Table" means the HTML table from the web page.
		 * For now I am assuming there are games every Sat, Sun and Mon
		 * TODO: Add a check to make sure there are in fact football matches on Sat, Sun, Mon
		 */
		
		List<Element> tables = new ArrayList<Element>();
		tables.add(doc.select("#0-group > div:nth-child(2) > table > tbody").get(0)); // Sat
		tables.add(doc.select("#0-group > div:nth-child(3) > table > tbody").get(0)); // Sun
		tables.add(doc.select("#0-group > div:nth-child(4) > table > tbody").get(0)); // Mon
		
		for (Element table : tables){
			for(Element el : table.children()){
				List<String> footballMatch = new ArrayList<String>();
				footballMatch.add("Home:" + el.child(1).text());
				footballMatch.add(el.child(2).text());
				footballMatch.add("Away:" + el.child(3).text());
				allFootballMatches.add(footballMatch);
			}
			System.out.println(allFootballMatches);
			allFootballMatches.clear();
		}
		
	}

	@Override
	public List<String> getBettingData() {
		
		
		return null;
	}

}
