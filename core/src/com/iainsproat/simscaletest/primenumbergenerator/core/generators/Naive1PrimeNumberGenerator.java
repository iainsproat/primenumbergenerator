package com.iainsproat.simscaletest.primenumbergenerator.core.generators;

import java.util.ArrayList;
import java.util.List;

import com.iainsproat.simscaletest.primenumbergenerator.core.PrimeNumberGeneratorStrategy;

/**
 * Very naive implementation.
 * Brute force every single possibility
 * O(n^2) ?
 */
public class Naive1PrimeNumberGenerator implements
		PrimeNumberGeneratorStrategy {

	@Override
	public List<Integer> execute(int lowerBound, int upperBound) {
		List<Integer> result = new ArrayList<Integer>();
		
		//try every single possibility
		for(int i = lowerBound + 1; i < upperBound; i++)
		{
			if(isPrime(i))
			{
				result.add(i);
			}
		}
		
		return result;
	}

	private boolean isPrime(int candidate) {
		if(candidate < 2){
			return false;
		}
		
		for(int j = 2; j < candidate; j++){ //check all numbers between 1 and itself
			if (candidate % j == 0) { // it is perfectly divisible by a number between 1 and itself
				return false;
			}
		}
		
		//exhausted all possibilities, so must be prime
		return true;
	}
}
