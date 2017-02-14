package com.iainsproat.simscaletest.primenumbergenerator.core.generators;

import java.util.ArrayList;
import java.util.List;

import com.iainsproat.simscaletest.primenumbergenerator.core.PrimeNumberGeneratorStrategy;
/**
 * Sieve of eratosthenes
 * All non prime numbers 
 */
public class EratosthenesPrimeNumberGenerator implements PrimeNumberGeneratorStrategy {

// From Wikipedia:
//  Create a list of consecutive integers from 2 through n: (2, 3, 4, ..., n).
//	Initially, let p equal 2, the smallest prime number.
//	Enumerate the multiples of p by counting to n from 2p in increments of p, and mark them in the list (these will be 2p, 3p, 4p, ...; the p itself should not be marked).
//	Find the first number greater than p in the list that is not marked. If there was no such number, stop. Otherwise, let p now equal this new number (which is the next prime), and repeat from step 3.
//	When the algorithm terminates, the numbers remaining not marked in the list are all the primes below n.
	
	@Override
	public List<Integer> execute(int lowerBound, int upperBound) {
		boolean[] isPrime = initializePrimeArray(upperBound);
		
		int prime = 2;
		
		while(prime*prime <= upperBound){ //as per naive2 implementation, we only have to check primes up to the square route of the upperBound
			markOffAllMultiplesOfPrime(isPrime, prime, upperBound);

			int nextPrime = findNextAvailablePrime(prime, upperBound, isPrime);
			
			if(prime == nextPrime){ // we couldn't find a new prime, so must have exhausted all candidates
				break;
			}
			
			prime = nextPrime; //move on to next prime
		}
		
		return convertBooleanArrayToIntegerList(isPrime, lowerBound, upperBound);
	}
	
	/**
	 * 
	 * @param isPrime passed by reference and modified by this method
	 * @param prime
	 * @param upperBound
	 */
	private void markOffAllMultiplesOfPrime(boolean[] isPrime, int prime, int upperBound)
	{
		for(int i = 2*prime; i < upperBound; i += prime){
			isPrime[i] = false; //mark this multiple of a prime as a non-prime
		}
		
		//isPrime is passed by reference, so no need to return anything
	}
	
	/**
	 * Finds the next prime number in the array
	 * 
	 * @return the next prime number, but if no further prime number is found it returns the starting prime number
	 */
	private int findNextAvailablePrime(int prime, int upperBound, boolean[] isPrime)
	{
		//find next prime number in index. start at the next number from the last prime
		for(int j = prime+1; j < upperBound; j++)
		{
			if(isPrime[j])
			{
				return j;
			}
		}
		
		return prime; //we got here, so there were no remaining primes left.
	}
	
	/**
	 * Converts the boolean array, where a true indicates a prime number, into a list of just the prime integers
	 */
	private List<Integer> convertBooleanArrayToIntegerList(boolean[] primeArray, int lowerBound, int upperBound)
	{
		List<Integer> result = new ArrayList<Integer>();
		for(int i = lowerBound+1; i < upperBound; i++)
		{
			if(primeArray[i])
			{
				result.add(i);
			}
		}
		
		return result;
	}
	
	private boolean[] initializePrimeArray(int upperBound)
	{
		boolean[] primeArray = new boolean[upperBound];
		primeArray[0] = false; //0 is not a prime
		primeArray[1] = false; //1 is not a prime
		for(int i = 2; i < upperBound; i++)
		{
			primeArray[i] = true;
		}
		
		return primeArray;
	}

}
