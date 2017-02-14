package com.iainsproat.simscaletest.primenumbergenerator.core;

/**
 * Responsible for selecting the correct strategy given a string description
 */
public class StrategySelector {
	public static PrimeNumberGeneratorStrategy selectStrategy(String requestedStrategy)
	{
		switch(requestedStrategy.toLowerCase()){
		case "naive1":
			return new Naive1PrimeNumberGenerator();
		default:
			throw new UnsupportedOperationException(String.format("The requested strategy,  %s, is not implemented", requestedStrategy));
		}
	}
}
