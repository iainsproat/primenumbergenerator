package com.iainsproat.simscaletest.primenumbergenerator.core.generators;

import java.util.ArrayList;
import java.util.BitSet;
import java.util.List;

import com.iainsproat.simscaletest.primenumbergenerator.core.PrimeNumberGeneratorStrategy;
/**
 * Sieve of eratosthenes
 * All non prime numbers 
 */
public class EratosthenesBitSetPrimeNumberGenerator implements PrimeNumberGeneratorStrategy {

// From Wikipedia:
//  Create a list of consecutive integers from 2 through n: (2, 3, 4, ..., n).
//	Initially, let p equal 2, the smallest prime number.
//	Enumerate the multiples of p by counting to n from 2p in increments of p, and mark them in the list (these will be 2p, 3p, 4p, ...; the p itself should not be marked).
//	Find the first number greater than p in the list that is not marked. If there was no such number, stop. Otherwise, let p now equal this new number (which is the next prime), and repeat from step 3.
//	When the algorithm terminates, the numbers remaining not marked in the list are all the primes below n.
	
	@Override
	public List<Integer> execute(int lowerBound, int upperBound) {
		BitSet isPrime = initializePrimeArray(upperBound);
		
		int prime = 2;
		
		while(prime*prime <= upperBound){ //as per naive2 implementation, we only have to check primes up to the square route of the upperBound
			markOffAllMultiplesOfPrime(isPrime, prime, upperBound);

			int nextPrime = findNextAvailablePrime(isPrime, prime, upperBound);
			
			if(nextPrime == -1){ // we couldn't find a new prime, so must have exhausted all candidates
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
	private void markOffAllMultiplesOfPrime(BitSet isPrime, int prime, int upperBound)
	{
		for(int i = 2*prime; i < upperBound; i += prime){
			isPrime.clear(i); //mark this multiple of a prime as a non-prime
		}
		//isPrime is passed by reference, so no need to return anything
	}
	
	/**
	 * Finds the next prime number in the array
	 * 
	 * @return the next prime number, but if no further prime number is found it returns the starting prime number
	 */
	private int findNextAvailablePrime(BitSet isPrime, int prime, int upperBound)
	{
		//find next prime number in index. start at the next number from the last prime
		return isPrime.nextSetBit(prime+1);
	}
	
	/**
	 * Converts the boolean array, where a true indicates a prime number, into a list of just the prime integers
	 */
	private List<Integer> convertBooleanArrayToIntegerList(BitSet primeArray, int lowerBound, int upperBound)
	{
		List<Integer> result = new ArrayList<Integer>();
		for (int i = primeArray.nextSetBit(lowerBound+1); i >= 0; i = primeArray.nextSetBit(i+1)) {
		     result.add(i);
		     if (i == Integer.MAX_VALUE) {
		         break; // or (i+1) would overflow
		     }
		 }
		
		return result;
	}
	
	private BitSet initializePrimeArray(int upperBound)
	{
		BitSet primeArray = new BitSet(upperBound); //all initialized to false
		primeArray.set(2, upperBound); //set to true between 2 and upperBound
		
		return primeArray;
	}

}
