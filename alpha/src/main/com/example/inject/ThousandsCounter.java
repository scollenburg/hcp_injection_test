package thousands;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.example.inject.CounterInterface;

public class ThousandsCounter implements CounterInterface {
	private int current;
	private static final Logger LOGGER = LoggerFactory.getLogger(ThousandsCounter.class);

	public ThousandsCounter() {
		this.current = 1000;
		LOGGER.trace("Constructing ThousandsCounter");
	}

	@Override
	public int getNext() {
		current += 1000;
		return current;
	}

}
