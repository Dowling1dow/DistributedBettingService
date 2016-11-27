package core;

import java.io.IOException;
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
		Gson gson = new Gson();
		try {
			Representation skybet, williamhill;
			skybet = new ClientResource("http://localhost:8182/skybet/footballMatches").post(gson.toJson(fixture));
			System.out.println(skybet.getText()); // Text has 'link' and 'MatchID'
			
//			williamhill = new ClientResource("http://localhost:8182/williamhill/footballMatches").post(gson.toJson(fixture));
//			System.out.println(williamhill.getText());
			
			
			
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return null;
	}

}
