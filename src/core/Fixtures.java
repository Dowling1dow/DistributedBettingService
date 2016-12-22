package core;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

/*
 * This class is used to collect the current fixtures that will be used
 * for posting to the services 
 */

public class Fixtures{
	
//	public static void main(String args[]) throws IOException{
//		getFixtures(); // remove static when not testing
//	}
	public List<Fixture> getFixtures() throws IOException{
		List<Fixture> allFixtures = new ArrayList<Fixture>();
		// SkySports will only show current months matches !!
		// TODO: create a fix for this. If in last week of month, search in next month
		
		Document doc = Jsoup.connect("http://www.skysports.ie/premier-league-fixtures").get();
//		Document doc = Jsoup.connect("http://www.skysports.ie/premier-league-fixtures/december-2016").get(); // for testing
		
		Element ul = doc.getElementsByClass("fixres__body").first();
		int count = 3;
		
		// There should always be 10 matches happening
		// we use the variable below to make sure we get all 10
		int matches = 10;
		while(true){
			Fixture fixture = null;
			//#widgetLite-5 > div:nth-child(3) > a > span.matches__item-col.matches__participant.matches__participant--side1 > span > span
			String home = ul.select("div:nth-child("+count+") > a > span.matches__item-col.matches__participant.matches__participant--side1 > span > span").text();
			String away = ul.select("div:nth-child("+count+") > a > span.matches__item-col.matches__participant.matches__participant--side2 > span > span").text();
			if (home.isEmpty() || away.isEmpty()){
				count++;
				continue;
			}
			
			if (matches==0){ break; }
			matches--;
			count++;
			fixture = new Fixture(home, away);
			allFixtures.add(fixture);
		}
		return allFixtures;
	}
}
