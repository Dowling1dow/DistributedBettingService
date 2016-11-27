package core;

import java.io.IOException;
import java.util.Map;
import java.util.TreeMap;

import org.restlet.Application;
import org.restlet.Request;
import org.restlet.Response;
import org.restlet.Restlet;
import org.restlet.data.MediaType;
import org.restlet.data.Method;
import org.restlet.data.Status;
import org.restlet.routing.Router;

import com.google.gson.Gson;


public class FootballMatchApplication extends Application{
	Map<String, FootballMatch> footballMatches = new TreeMap<String, FootballMatch>();
	Gson gson = new Gson();
	BettingService service;
	
	public FootballMatchApplication(BettingService service) {
		this.service = service;
	}

	public Restlet createInboundRoot() {
		Router router = new Router(getContext());
		router.attach("/footballMatches", new Restlet() {
			@Override
			public void handle(Request request, Response response) {
				if (request.getMethod().equals(Method.GET)) {
					String out = "[";
					boolean first = true;
					for (String key : footballMatches.keySet()) {
						if (first) first = false; else out += ",";
						String url = request.getResourceRef().getHostIdentifier() + "/footballMatches/" + key;
						out += "{\"MatchID\" : \"" + key + "\", \"url\":\"" + url + "\"}";
					}
					response.setEntity(out + "]", MediaType.TEXT_PLAIN);
				} else if (request.getMethod().equals(Method.POST)) {
					String json = request.getEntityAsText();
					Fixture fixture = gson.fromJson(json, Fixture.class);
					try {
						System.out.println(fixture.homeTeam+" : "+fixture.awayTeam);
						FootballMatch footballMatch = (FootballMatch) service.getBettingData(fixture);
						if (footballMatches.containsKey(footballMatch.MatchID)) {
							// change this, may cause error 
							// need to allow it to be overwritten
							response.setStatus(Status.CLIENT_ERROR_FORBIDDEN);
						} else {
							footballMatches.put(footballMatch.MatchID, footballMatch);
							String url = request.getResourceRef().getHostIdentifier() + "/footballMatches/" + footballMatch.MatchID;
							response.setEntity("{\"MatchID\" : \"" + footballMatch.MatchID + "\", \"link\":\"" + url + "\"}", MediaType.TEXT_PLAIN);
						}
					} catch (IOException e) {
						e.printStackTrace();
					}

				} else {
					response.setStatus(Status.CLIENT_ERROR_FORBIDDEN);
				}
			}
		});
		router.attach("/footballMatches/{MatchID}", new Restlet() {
			@Override
			public void handle(Request request, Response response) {
				String id = (String) request.getAttributes().get("MatchID");
				if (footballMatches.containsKey(id)) {
					response.setStatus(Status.SUCCESS_OK);
					
					if (request.getMethod().equals(Method.GET)) {
						response.setEntity(gson.toJson(footballMatches.get(id)), MediaType.TEXT_PLAIN);
					} else if (request.getMethod().equals(Method.PUT)) {
						FootballMatch match = gson.fromJson(request.getEntityAsText(), FootballMatch.class);
						footballMatches.put(match.MatchID, match);
					} else if (request.getMethod().equals(Method.DELETE)) {
						footballMatches.remove(id);
					}
				} else {
					response.setStatus(Status.CLIENT_ERROR_NOT_FOUND);
				}
			}
		});
		return router;
	}
}
