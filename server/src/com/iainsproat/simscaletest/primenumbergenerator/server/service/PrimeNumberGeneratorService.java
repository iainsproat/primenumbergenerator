package com.iainsproat.simscaletest.primenumbergenerator.server.service;

import com.iainsproat.simscaletest.primenumbergenerator.core.PrimeNumberGeneratorStrategyClient;
import com.iainsproat.simscaletest.primenumbergenerator.core.PrimeNumberGeneratorStrategyClient.Result;

/**
 * This service connects to the underlying prime number generator library to calculate the prime number
 *
 */
public class PrimeNumberGeneratorService {

	public Result calculatePrimes(String calculationStrategy, int lowerBound, int upperBound) {
		PrimeNumberGeneratorStrategyClient client = new PrimeNumberGeneratorStrategyClient();
		
		//TODO should be dispatched asynchronously to a queue
		PrimeNumberGeneratorStrategyClient.Result result = client.execute(
				calculationStrategy, 
				lowerBound, 
				upperBound);
		
		return result;
	}

}
