package test;

import java.io.IOException;

import core.Fixture;
import core.Fixtures;
import core.FootballMatch;
import core.RestletBookie;


public class Main {
	public static void main(String[] args) throws IOException {
		Fixtures service = new Fixtures();
		RestletBookie broker = new RestletBookie();
		for (Fixture fixture : service.getFixtures()) {
			System.out.println("Match: "+ fixture.homeTeam + " vs " + fixture.awayTeam);
			for(FootballMatch footballMatch : broker.getFootballMatches(fixture)) {
				System.out.println("MatchID: " + footballMatch.MatchID + 
						"\nHomeTeam: " + footballMatch.HomeTeam +
						"\nAwayTeam: " + footballMatch.AwayTeam + 
						"\nHome to win: " + footballMatch.HomeTeamWin + 
						"\nAway to win: " + footballMatch.AwayTeamWin +
						"\nDraw: " + footballMatch.Draw
						);
			}
		}
	}

}
