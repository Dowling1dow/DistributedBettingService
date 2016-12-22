package core;

import java.io.IOException;

import org.restlet.Request;
import org.restlet.Response;
import org.restlet.Restlet;
import org.restlet.data.MediaType;
import org.restlet.data.Method;
import org.restlet.data.Status;

public class FixturesRestlet extends Restlet{
	@Override
	public void handle(Request request, Response response) {
		if (request.getMethod().equals(Method.GET)) {
			try {
				String out = "{ \"Fixtures\": [";
				boolean first = true;
				for (Fixture fixture : new Fixtures().getFixtures()) {
					if (first) first = false; else out += ",";
					out += "{\"HomeTeam\" : \"" + fixture.homeTeam + "\", \"AwayTeam\":\"" + fixture.awayTeam + "\"}";
				}
				response.setEntity(out + "]}", MediaType.TEXT_PLAIN);
			
			} catch (IOException e) {
				e.printStackTrace();
			}
		}else{
			response.setStatus(Status.CLIENT_ERROR_FORBIDDEN);
		}
	}
}
