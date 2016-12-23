package core;

import java.io.IOException;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import org.restlet.Component;
import org.restlet.data.Protocol;

import betting_service.BetVictorService;
import betting_service.SkyBetService;
import betting_service.WilliamHillService;


public class BettingServer {
	public static void main(String[] args) throws Exception {
		 Component component = new Component();
		 component.getServers().add(Protocol.HTTP, 8182);
		 component.getDefaultHost().attach("/fixtures", new FixturesRestlet());
		 component.getDefaultHost().attach("/betvictor", new FootballMatchApplication(new BetVictorService()));
		 component.getDefaultHost().attach("/williamhill", new FootballMatchApplication(new WilliamHillService()));
		 component.getDefaultHost().attach("/skybet", new FootballMatchApplication(new SkyBetService()));
		 component.start();
		 
		 // This will update all the links for betting services with the latest odds info
		 // It runs every minute
		 ScheduledExecutorService executor = Executors.newScheduledThreadPool(1);
		 executor.scheduleAtFixedRate(updateOdds, 0, 1, TimeUnit.MINUTES);
	}
	
	static Runnable updateOdds = new Runnable() {
	    public void run(){
	    	Fixtures service = new Fixtures();
			RestletBookie broker = new RestletBookie();
			try {
				for (Fixture fixture : service.getFixtures()) {
					broker.getFootballMatches(fixture);
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
	    }
	};
}
