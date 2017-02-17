package com.iainsproat.simscaletest.primenumbergenerator.core;

import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

/**
 * Responsible for running the Prime Number Generator strategy
 *
 */
public class PrimeNumberGeneratorStrategyClient {
	
	/**
	 * 
	 * @param requestedStrategy
	 * @param lowerBound
	 * @param upperBound
	 * @return
	 */
	public Result execute(String requestedStrategy, int lowerBound, int upperBound)
	{
		try {
			PrimeNumberGeneratorStrategy strategy = StrategySelector.selectStrategy(requestedStrategy);
			return execute(strategy, lowerBound, upperBound);
		} catch(Exception e) {
			return new Result(requestedStrategy, lowerBound, upperBound, e.getMessage(), e.getStackTrace().toString());
		}
	}
	
	/**
	 * Overloaded operator to allow for test injection of Mock strategy
	 */
	public Result execute(PrimeNumberGeneratorStrategy strategy, int lowerBound, int upperBound)
	{		
		//must be positive numbers
		lowerBound = clampToZero(lowerBound);
		upperBound = clampToZero(upperBound);
		//upper bound should always be greater than lower bound.  Rather than failing, we'll be opinionated and swap the two.
		if(lowerBound > upperBound)
		{
			int temp = lowerBound;
			lowerBound = upperBound;
			upperBound = temp;
		}
		
		
		
		
		List<Integer> primes;
		Instant start = Instant.now();
		//if upper bound is 0, 1 or 2, then there are no primes.  Upper bound is itself excluded from the range, so an upper bound of 2 cannot return 2.
		
		if(upperBound < 3){
			primes = new ArrayList<Integer>(0);
		} else if(upperBound - lowerBound < 2){ //similarly, if both upperBound and lowerBound are the same number or adjacent then, as both are excluded from the range, then we can't find prime numbers 
			primes = new ArrayList<Integer>(0);
		} else {
			primes = strategy.execute(lowerBound, upperBound);
		}
		
		try {
			Instant end = Instant.now();
		
			return new Result(start.getEpochSecond(), strategy.getClass().getSimpleName(), lowerBound, upperBound, primes, Duration.between(start, end).getSeconds());
		} catch(Exception e) {
			return new Result(strategy.getClass().getSimpleName(), lowerBound, upperBound, e.getMessage(), e.getStackTrace().toString());
		}
	}
	
	/**
	 * Ensures the value can never be less than zero
	 * @param value
	 * @return
	 */
	private int clampToZero(int value)
	{
		if(value < 0)
		{
			return 0;
		}
		
		return value;
	}
	
	/**
	 * Holds data pertaining to the input parameters and results of a single run of a Prime Number Generator
	 *
	 */
	public class Result{
		private long timeStamp; //FIXME this should be a GUID as timestamps are not guaranteed to be universally unique (despite it being in nanoseconds!).
		private String strategy;
		private int lowerBound;
		private int upperBound;
		private List<Integer> primes;
		private long duration;
		private List<String> errors;
		
		public Result(String __requestedStrategy, int __lowerBound, int __upperBound, String... __errors)
		{
			this.strategy = __requestedStrategy;
			this.lowerBound = __lowerBound;
			this.upperBound = __upperBound;
			if(__errors != null){
				this.errors = new ArrayList<String>(__errors.length);
				for(String error : __errors)
					this.errors.add(error);
			}
		}
		
		public Result(long __timeStamp, String __requestedStrategy, int __lowerBound, int __upperBound, List<Integer> __primeNumbers, long __executionDuration)
		{
			this.timeStamp = __timeStamp;
			this.strategy = __requestedStrategy;
			this.lowerBound = __lowerBound;
			this.upperBound = __upperBound;
			this.primes = __primeNumbers;
			this.duration = __executionDuration;
		}
		
		public long getTimeStamp()
		{
			return this.timeStamp;
		}
		
		public String getRequestedStrategy()
		{
			return this.strategy;
		}
		
		public int getLowerBound()
		{
			return this.lowerBound;
		}
		
		public int getUpperBound()
		{
			return this.upperBound;
		}
		
		public List<Integer> getPrimeNumbers()
		{
			return this.primes;
		}
		
		public long getExecutionDuration()
		{
			return this.duration;
		}
		
		public boolean hasError()
		{
			return this.errors != null && this.errors.size() > 0;
		}
		
		public List<String> getErrors()
		{
			return this.errors;
		}
	}
}
