package com.iainsproat.simscaletest.primenumbergenerator.core.generators;

import java.util.ArrayList;
import java.util.BitSet;
import java.util.List;

import com.iainsproat.simscaletest.primenumbergenerator.core.PrimeNumberGeneratorStrategy;

public class SundaramPrimeNumberGenerator implements PrimeNumberGeneratorStrategy {

	@Override
	public List<Integer> execute(int lowerBound, int upperBound) {
		//Wikipedia description:
		//Start with a list of the integers from 1 to n. From this list, remove all numbers of the form i + j + 2ij where:
		//{\displaystyle i,j\in \mathbb {N} ,\ 1\leq i\leq j} i,j\in\mathbb{N},\ 1 \le i \le j
		//{\displaystyle i+j+2ij\leq n} i + j + 2ij \le n
		//The remaining numbers are doubled and incremented by one, giving a list of the odd prime numbers (i.e., all primes except 2) below 2n + 2
		
		BitSet isPrime = this.sieve(upperBound);
		

		return calculatePrimesFromSieved(isPrime, lowerBound, upperBound);
	}
	
	protected BitSet sieve(int upperBound)
	{
		int sundaramLimit = (int)Math.ceil(upperBound/2.0); // only need to sieve half of the numbers
		BitSet isPrime = new BitSet(sundaramLimit);
		isPrime.set(0, upperBound - 1); //set all to prime

		int j = 1;

		for(int i = 1; i <= j; i++){
			for(j = i; (i + j + 2*i*j) < sundaramLimit; j++){
				isPrime.clear(i + j + 2*i*j);
			}
		}
		
		return isPrime;
	}
	
	/**
	 * Double the remaining numbers and increment by one
	 * @param isPrime
	 * @param lowerBound
	 * @param upperBound
	 * @return
	 */
	protected List<Integer> calculatePrimesFromSieved(BitSet isPrime, int lowerBound, int upperBound)
	{
		List<Integer> primes = new ArrayList<Integer>();
		
		//Sundaram algorithm starts at 3, so we need to treat 2 separately
		if(isInRange(2, lowerBound, upperBound)){
			primes.add(2);
		}
		
		if(upperBound < 4) //the for loop starts at 3. To avoid an exception we need to return if 3 is not within the upper bound
		{
			return primes;
		}
		
		//Sundaram starts at 3 (= 2 * i + 1, where i = 1)
		for (int i = isPrime.nextSetBit(1); i >= 0; i = isPrime.nextSetBit(i+1)) {
			int candidatePrime = 2*i + 1;
			
			if(isInRange(candidatePrime, lowerBound, upperBound)){
				primes.add(candidatePrime);
			}

			if (i == Integer.MAX_VALUE) {
				break; // or (i+1) would overflow
			}
		}
		
		return primes;
	}
	
	protected boolean isInRange(int candidate, int lowerBound, int upperBound)
	{
		return lowerBound < candidate && candidate < upperBound;
	}

}
