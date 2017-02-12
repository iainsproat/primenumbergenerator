package com.iainsproat.simscaletest.primenumbergenerator.core;

import java.time.Duration;
import java.time.Instant;
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
		PrimeNumberGeneratorStrategy strategy = StrategySelector.selectStrategy(requestedStrategy);
		return execute(strategy, lowerBound, upperBound);
	}
	
	/**
	 * Overloaded operator to allow for test injection of Mock strategy
	 */
	public Result execute(PrimeNumberGeneratorStrategy strategy, int lowerBound, int upperBound)
	{		
		Instant start = Instant.now();
		List<Integer> primes = strategy.execute(lowerBound, upperBound);		
		Instant end = Instant.now();
		
		return new Result(start.getEpochSecond(), strategy.getClass().getSimpleName(), lowerBound, upperBound, primes, Duration.between(start, end).getSeconds());
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
	}
}
