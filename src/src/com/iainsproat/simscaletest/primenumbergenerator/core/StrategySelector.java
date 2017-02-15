package com.iainsproat.simscaletest.primenumbergenerator.core;

import com.iainsproat.simscaletest.primenumbergenerator.core.generators.*;

/**
 * Responsible for selecting the correct strategy given a string description
 */
public class StrategySelector {
	public static PrimeNumberGeneratorStrategy selectStrategy(String requestedStrategy)
	{
		switch(requestedStrategy.toLowerCase()){
		case "naive1":
			return new Naive1PrimeNumberGenerator();
		case "naive2":
			return new Naive2PrimeNumberGenerator();
		case "eratosthenes":
			return new EratosthenesPrimeNumberGenerator();
		case "eratosthenesbitset":
			return new EratosthenesBitSetPrimeNumberGenerator();
		case "sundaram":
			return new SundaramPrimeNumberGenerator();
		default:
			throw new UnsupportedOperationException(String.format("The requested strategy,  %s, is not implemented", requestedStrategy));
		}
	}
}
