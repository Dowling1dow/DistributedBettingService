package core;

import org.restlet.Component;
import org.restlet.data.Protocol;

import betting_service.SkyBetService;
import betting_service.WilliamHillService;


public class BettingServer {
	public static void main(String[] args) throws Exception {
		 Component component = new Component();
		 component.getServers().add(Protocol.HTTP, 8182);
		 component.getDefaultHost().attach("/fixtures", new FixturesRestlet());
//		 component.getDefaultHost().attach("/williamhill", new FootballMatchApplication(new WilliamHillService()));
		 component.getDefaultHost().attach("/skybet", new FootballMatchApplication(new SkyBetService()));
		 component.start();
	} 
}
