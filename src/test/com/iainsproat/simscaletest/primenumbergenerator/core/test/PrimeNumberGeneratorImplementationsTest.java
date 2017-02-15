package com.iainsproat.simscaletest.primenumbergenerator.core.test;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import com.iainsproat.simscaletest.primenumbergenerator.core.PrimeNumberGeneratorStrategy;
import com.iainsproat.simscaletest.primenumbergenerator.core.generators.*;

public class PrimeNumberGeneratorImplementationsTest {

	protected static List<PrimeNumberGeneratorStrategy> primeNumberGenerators;

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		primeNumberGenerators = new ArrayList<PrimeNumberGeneratorStrategy>();
		primeNumberGenerators.add(new Naive1PrimeNumberGenerator());
		primeNumberGenerators.add(new Naive2PrimeNumberGenerator());
		primeNumberGenerators.add(new EratosthenesPrimeNumberGenerator());
		primeNumberGenerators.add(new EratosthenesBitSetPrimeNumberGenerator());
		primeNumberGenerators.add(new SundaramPrimeNumberGenerator());
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
		primeNumberGenerators.clear();
		primeNumberGenerators = null;
	}
	
	@Test
	public void ifRange0To3Returns2(){
		for(PrimeNumberGeneratorStrategy SUT : primeNumberGenerators)
		{
			List<Integer> result = SUT.execute(0, 3);
			assertEquals(String.format("ifRange0To3Returns2 failed by %s - incorrect size", SUT.getClass().getSimpleName()),
					1, result.size());
			assertEquals(String.format("ifRange0To3Returns2 failed by %s", SUT.getClass().getSimpleName()),
					2, (int)result.get(0));
		}
	}
	
	//FIXME this assumes primes are returned in order - not sure if this is a requirement...
	@Test
	public void ifRange0To4Returns2_3(){
		for(PrimeNumberGeneratorStrategy SUT : primeNumberGenerators)
		{
			List<Integer> result = SUT.execute(0, 4);
			assertEquals(String.format("ifRange0To4Returns2_3 failed by %s - incorrect size", SUT.getClass().getSimpleName()),
					2, result.size());
			assertEquals(String.format("ifRange0To4Returns2_3 failed by %s - first number", SUT.getClass().getSimpleName()),
					2, (int)result.get(0));
			assertEquals(String.format("ifRange0To4Returns2_3 failed by %s - second number", SUT.getClass().getSimpleName()),
					3, (int)result.get(1));
		}
	}
	
	//FIXME this assumes primes are returned in order - not sure if this is a requirement...
	@Test
	public void lowerBoundIsExcluded(){
		for(PrimeNumberGeneratorStrategy SUT : primeNumberGenerators)
		{
			List<Integer> result = SUT.execute(2, 6);
			assertEquals(String.format("lowerBoundIsExcluded failed by %s - incorrect size", SUT.getClass().getSimpleName()),
					2, result.size());
			assertEquals(String.format("lowerBoundIsExcluded failed by %s - first number", SUT.getClass().getSimpleName()),
					3, (int)result.get(0));
			assertEquals(String.format("lowerBoundIsExcluded failed by %s - second number", SUT.getClass().getSimpleName()),
					5, (int)result.get(1));
		}
	}
	
	//FIXME this assumes primes are returned in order - not sure if this is a requirement...
		@Test
		public void upperBoundIsExcluded(){
			for(PrimeNumberGeneratorStrategy SUT : primeNumberGenerators)
			{
				List<Integer> result = SUT.execute(6, 11);
				assertEquals(String.format("upperBoundIsExcluded failed by %s - incorrect size", SUT.getClass().getSimpleName()),
						1, result.size());
				assertEquals(String.format("upperBoundIsExcluded failed by %s - first number", SUT.getClass().getSimpleName()),
						7, (int)result.get(0));
			}
		}
	
	//FIXME this assumes primes are returned in order - not sure if this is a requirement...
	@Test
	public void ifRange50To250Returns38Results(){
		for(PrimeNumberGeneratorStrategy SUT : primeNumberGenerators)
		{
			List<Integer> result = SUT.execute(50, 250);
			assertEquals(String.format("ifRange50To250ReturnsSize38 failed by %s - incorrect size", SUT.getClass().getSimpleName()),
					38, result.size());
			assertEquals(String.format("ifRange50To250ReturnsSize38 failed by %s - first number", SUT.getClass().getSimpleName()),
					53, (int)result.get(0));
			assertEquals(String.format("ifRange50To250ReturnsSize38 failed by %s - last number", SUT.getClass().getSimpleName()),
					241, (int)result.get(result.size() - 1));
		}
	}

}
