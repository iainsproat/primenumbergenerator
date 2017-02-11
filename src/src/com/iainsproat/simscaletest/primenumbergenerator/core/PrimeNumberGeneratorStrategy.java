package com.iainsproat.simscaletest.primenumbergenerator.core;

import java.util.List;

/**
 * Interface for Strategy pattern
 * Concrete implementations of Prime Number Generators must implement this interface
 * 
 * @author Iain Sproat
 *
 */
public interface PrimeNumberGeneratorStrategy {
	public List<Integer> execute(int lowerBound, int upperBound); 
}
