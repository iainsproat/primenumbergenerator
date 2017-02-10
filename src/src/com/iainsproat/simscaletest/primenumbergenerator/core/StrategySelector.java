package com.iainsproat.simscaletest.primenumbergenerator.core;

/**
 * Responsible for selecting the correct strategy given a string description
 */
public class StrategySelector {
	public static PrimeNumberGeneratorStrategy selectStrategy(String requestedStrategy)
	{
		switch(requestedStrategy.toLowerCase()){
		case "brute":
			return new BruteForcePrimeNumberGenerator();
		default:
			throw new UnsupportedOperationException(String.format("The requested strategy,  %1, is not implemented", requestedStrategy));
		}
	}
}
