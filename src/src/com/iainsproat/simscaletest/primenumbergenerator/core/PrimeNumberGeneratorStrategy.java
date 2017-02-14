package com.iainsproat.simscaletest.primenumbergenerator.core;

import java.util.List;

/**
 * Interface for Strategy pattern
 * Concrete implementations of Prime Number Generators must implement this interface
 * 
 *
 */
public interface PrimeNumberGeneratorStrategy {
	/**
	 * Implementing classes must deal with the case where both are equal to each other.
	 * 
	 * @param lowerBound the lower bound of the range in which to search.  This is exclusive from the range. Implementing classes must deal with values zero or greater.
	 * @param upperBound the upper bound of the range in which to search. This is exclusive from the range.  Implementing classes must deal with values zero or greater.
	 * @return
	 */
	public List<Integer> execute(int lowerBound, int upperBound); 
}
