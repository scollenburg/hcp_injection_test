package com.example.inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/*
 *  We could also use @Alternative here, but adding or removing the @Alternative annotation requires
 *  simultaneous edits be made to the beans.xml file. Use of a stereotype annotation only requires
 *  changes be made in one place.
 */

@ParentProjectAlternative
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
