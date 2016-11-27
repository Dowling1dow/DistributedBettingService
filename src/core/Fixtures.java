package core;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

/*
 * This class is used to collect the current fixtures that will be used
 * for posting to the services 
 */

public class Fixtures{
	
//	public static void main(String args[]) throws IOException{
//		getFixtures(); // remove static when not testing
//	}
	public static List<Fixture> getFixtures() throws IOException{
		List<Fixture> allFixtures = new ArrayList<Fixture>();
		// SkySports will only show current months matches !!
		// TODO: create a fix for this. If in last week of month, search in next month
		
//		Document doc = Jsoup.connect("http://www.skysports.ie/premier-league-fixtures").get();
		Document doc = Jsoup.connect("http://www.skysports.ie/premier-league-fixtures/december-2016").get(); // for testing
		
		Element ul = doc.getElementsByClass("matches__group").first();
		int count = 1;
		while(true){
			Fixture fixture = null;
			String home = ul.select("li:nth-child("+count+") > a > span.matches__item-col.matches__participant.matches__participant--side1 > span > span").text();
			String away = ul.select("li:nth-child("+count+") > a > span.matches__item-col.matches__participant.matches__participant--side2 > span > span").text();
			if (home.isEmpty() || away.isEmpty()){
				break;
			}
//			System.out.println(home);
			count++;
//			System.out.println(home+" vs "+away);
			fixture = new Fixture(home, away);
			allFixtures.add(fixture);
		}
		System.out.println(count-1);
		return allFixtures;
	}
}
