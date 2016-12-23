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
			Representation skybet, williamhill, betvictor;
			FootballMatch skybetMatch, williamHillMatch, betvictorMatch;

			skybet = new ClientResource("http://localhost:8182/skybet/footballMatches").post(gson.toJson(fixture));
			williamhill = new ClientResource("http://localhost:8182/williamhill/footballMatches").post(gson.toJson(fixture));
			betvictor = new ClientResource("http://localhost:8182/betvictor/footballMatches").post(gson.toJson(fixture));
			
			skybetMatch = gson.fromJson(skybet.getText(), FootballMatch.class);
			williamHillMatch = gson.fromJson(williamhill.getText(), FootballMatch.class);
			betvictorMatch = gson.fromJson(betvictor.getText(), FootballMatch.class);
			
			Representation skybetRep, williamHillRep, betvictorRep;
			skybetRep = new ClientResource("http://localhost:8182/skybet/footballMatches/"+skybetMatch.MatchID).get();
			skybetMatch = gson.fromJson(skybetRep.getText(), FootballMatch.class);
			
			williamHillRep = new ClientResource("http://localhost:8182/williamhill/footballMatches/"+williamHillMatch.MatchID).get();
			williamHillMatch = gson.fromJson(williamHillRep.getText(), FootballMatch.class);
			
			betvictorRep = new ClientResource("http://localhost:8182/betvictor/footballMatches/"+betvictorMatch.MatchID).get();
			betvictorMatch = gson.fromJson(betvictorRep.getText(), FootballMatch.class);
			
			matchOdds.add(skybetMatch);
			matchOdds.add(williamHillMatch);
			matchOdds.add(betvictorMatch);
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return matchOdds;
	}

}
