package core;

import java.io.IOException;
import java.util.List;

public interface BettingService {
	public FootballMatch getBettingData(Fixture fixture) throws IOException;
}
