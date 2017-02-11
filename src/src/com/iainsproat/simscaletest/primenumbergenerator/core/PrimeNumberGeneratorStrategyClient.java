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
		
		return new Result(start.getNano(), strategy.getClass().getSimpleName(), lowerBound, upperBound, primes, Duration.between(start, end).getNano());
	}
	
	/**
	 * Holds data pertaining to the input parameters and results of a single run of a Prime Number Generator
	 *
	 */
	public class Result{
		private int _timeStamp; //FIXME this should be a GUID as timestamps are not guaranteed to be universally unique (despite it being in nanoseconds!).
		private String _strategy;
		private int _lowerBound;
		private int _upperBound;
		private List<Integer> _primes;
		private int _duration;
		
		public Result(int timeStamp, String requestedStrategy, int lowerBound, int upperBound, List<Integer> primeNumbers, int executionDuration)
		{
			this._timeStamp = timeStamp;
			this._strategy = requestedStrategy;
			this._lowerBound = lowerBound;
			this._upperBound = upperBound;
			this._primes = primeNumbers;
			this._duration = executionDuration;
		}
		
		public int getTimeStamp()
		{
			return this._timeStamp;
		}
		
		public String getRequestedStrategy()
		{
			return this._strategy;
		}
		
		public int getLowerBound()
		{
			return this._lowerBound;
		}
		
		public int getUpperBound()
		{
			return this._upperBound;
		}
		
		public List<Integer> getPrimeNumbers()
		{
			return this._primes;
		}
		
		public int getExecutionDuration()
		{
			return this._duration;
		}
	}
}
