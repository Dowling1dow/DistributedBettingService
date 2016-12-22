package core;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import org.restlet.representation.Representation;
import org.restlet.resource.ClientResource;

import com.google.gson.Gson;

public class RestletBookie implements BookieService {
	
	/*
	 * This class will return a list of 'FootballMatch'.
	 * A FootballMatch contains HomeTeam and AwayTeam
	 * along with the odds that will vary between services.
	 */

	@Override
	public List<FootballMatch> getFootballMatches(Fixture fixture) {
		List<FootballMatch> matchOdds = new LinkedList<FootballMatch>();
		Gson gson = new Gson();
		try {
			Representation skybet, williamhill;
			FootballMatch skybetMatch, williamHillMatch;
//			fixtures = new ClientResource("http://localhost:8182/fixtures").get();
			
			// Getting SkyBet odds
			skybet = new ClientResource("http://localhost:8182/skybet/footballMatches").post(gson.toJson(fixture));
			// Getting William Hill odds
			williamhill = new ClientResource("http://localhost:8182/williamhill/footballMatches").post(gson.toJson(fixture));
			
//			System.out.println(skybet.getText()); // Text has 'link' and 'MatchID'
			skybetMatch = gson.fromJson(skybet.getText(), FootballMatch.class);
			williamHillMatch = gson.fromJson(williamhill.getText(), FootballMatch.class);
			
//			System.out.println(skybetMatch.MatchID);
//			System.out.println("http://localhost:8182/skybet/footballMatches/"+skybetMatch.MatchID);
			
			Representation skybetRep, williamHillRep;
			skybetRep = new ClientResource("http://localhost:8182/skybet/footballMatches/"+skybetMatch.MatchID).get();
			skybetMatch = gson.fromJson(skybetRep.getText(), FootballMatch.class);
			
			williamHillRep = new ClientResource("http://localhost:8182/williamhill/footballMatches/"+williamHillMatch.MatchID).get();
			williamHillMatch = gson.fromJson(williamHillRep.getText(), FootballMatch.class);
//			System.out.println(skybetMatch.HomeTeamWin);
			
			matchOdds.add(skybetMatch);
			matchOdds.add(williamHillMatch);
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return matchOdds;
	}

}
