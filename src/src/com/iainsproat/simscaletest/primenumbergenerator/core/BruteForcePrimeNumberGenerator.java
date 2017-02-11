package com.iainsproat.simscaletest.primenumbergenerator.core;

import java.util.ArrayList;
import java.util.List;

public class BruteForcePrimeNumberGenerator implements
		PrimeNumberGeneratorStrategy {

	@Override
	public List<Integer> execute(int lowerBound, int upperBound) {
		//TODO
				List<Integer> result = new ArrayList<Integer>(3);
				result.add(3);
				result.add(5);
				result.add(7);
				return result;
	}
}
