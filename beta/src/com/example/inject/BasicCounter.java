package com.example.inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BasicCounter implements CounterInterface {

	private int current;
	private static final Logger LOGGER = LoggerFactory.getLogger(BasicCounter.class);

	public BasicCounter() {
		this.current = 10;
		LOGGER.trace("Constructing BasicCounter");
	}

	@Override
	public int getNext() {
		return current++;
	}

}
