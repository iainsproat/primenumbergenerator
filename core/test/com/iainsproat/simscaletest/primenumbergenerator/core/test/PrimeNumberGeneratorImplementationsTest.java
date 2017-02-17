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
		primeNumberGenerators.add(new EratosthenesParallelPrimeNumberGenerator());
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
		primeNumberGenerators.clear();
		primeNumberGenerators = null;
	}

	@Test
	public void ifRange0To3Return2(){
		for(PrimeNumberGeneratorStrategy SUT : primeNumberGenerators)
		{
			List<Integer> result = SUT.execute(0, 3);
			assertEquals(String.format("ifRange0To3Return2 failed by %s - incorrect size", SUT.getClass().getSimpleName()),
					1, result.size());
			assertEquals(String.format("ifRange0To3Return2 failed by %s - should contain 2", SUT.getClass().getSimpleName()),
					2, (int)result.get(0));
		}
	}

	@Test
	public void ifRange0To4Return2_3(){
		for(PrimeNumberGeneratorStrategy SUT : primeNumberGenerators)
		{
			List<Integer> result = SUT.execute(0, 4);
			assertEquals(String.format("ifRange0To4Return2_3 failed by %s - incorrect size", SUT.getClass().getSimpleName()),
					2, result.size());
			assertEquals(String.format("ifRange0To4Return2_3 failed by %s - should contain 2", SUT.getClass().getSimpleName()),
					2, (int)result.get(0));
			assertEquals(String.format("ifRange0To4Return2_3 failed by %s - should contain 3", SUT.getClass().getSimpleName()),
					3, (int)result.get(result.size() - 1));
		}
	}

	@Test
	public void lowerBoundIsExcluded(){
		for(PrimeNumberGeneratorStrategy SUT : primeNumberGenerators)
		{
			List<Integer> result = SUT.execute(2, 6);
			assertEquals(String.format("lowerBoundIsExcluded failed by %s - incorrect size", SUT.getClass().getSimpleName()),
					2, result.size());
			assertEquals(String.format("lowerBoundIsExcluded failed by %s - should contain 3", SUT.getClass().getSimpleName()),
					3, (int)result.get(0));
			assertEquals(String.format("lowerBoundIsExcluded failed by %s - should contain 5", SUT.getClass().getSimpleName()),
					5, (int)result.get(result.size() - 1));
		}
	}

	@Test
	public void upperBoundIsExcluded(){
		for(PrimeNumberGeneratorStrategy SUT : primeNumberGenerators)
		{
			List<Integer> result = SUT.execute(6, 11);
			assertEquals(String.format("upperBoundIsExcluded failed by %s - incorrect size", SUT.getClass().getSimpleName()),
					1, result.size());
			assertEquals(String.format("upperBoundIsExcluded failed by %s - should contain 7", SUT.getClass().getSimpleName()),
					7, (int)result.get(0));
		}
	}

	@Test
	public void ifRange50To250Return38Results(){
		for(PrimeNumberGeneratorStrategy SUT : primeNumberGenerators)
		{
			List<Integer> result = SUT.execute(50, 250);
			assertEquals(String.format("ifRange50To250ReturnSize38 failed by %s - incorrect size", SUT.getClass().getSimpleName()),
					38, result.size());
			assertEquals(String.format("ifRange50To250ReturnSize38 failed by %s - should contain 53", SUT.getClass().getSimpleName()),
					53, (int)result.get(0));
			assertEquals(String.format("ifRange50To250ReturnSize38 failed by %s - should contain 241", SUT.getClass().getSimpleName()),
					241, (int)result.get(result.size() - 1));
		}
	}
	
	@Test
	public void ifRange47To2000Return288Results(){
		for(PrimeNumberGeneratorStrategy SUT : primeNumberGenerators)
		{
			List<Integer> result = SUT.execute(47, 2000);
			assertEquals(String.format("ifRange47To2000Return288Results failed by %s - incorrect size", SUT.getClass().getSimpleName()),
					288, result.size());
			assertEquals(String.format("ifRange47To2000Return288Results failed by %s - should contain 53", SUT.getClass().getSimpleName()),
					53, (int)result.get(0));
			assertEquals(String.format("ifRange47To2000Return288Results failed by %s - should contain 1999", SUT.getClass().getSimpleName()),
					1999, (int)result.get(result.size() - 1));
		}
	}
	
	/**
	 * This is used to test parallel implementations where the lower bound is greater than sqrt of the upper bound
	 */
	@Test
	public void canCalculateWhereLowerBoundIsGreaterThanSqrtOfUpperBound(){
		for(PrimeNumberGeneratorStrategy SUT : primeNumberGenerators)
		{
			List<Integer> result = SUT.execute(1800, 2000);
			assertEquals(String.format("canCalculateWhereLowerBoundIsGreaterThanSqrtOfUpperBound failed by %s - incorrect size", SUT.getClass().getSimpleName()),
					25, result.size());
			assertEquals(String.format("canCalculateWhereLowerBoundIsGreaterThanSqrtOfUpperBound failed by %s - should contain 1801", SUT.getClass().getSimpleName()),
					1801, (int)result.get(0));
			assertEquals(String.format("canCalculateWhereLowerBoundIsGreaterThanSqrtOfUpperBound failed by %s - should contain 1999", SUT.getClass().getSimpleName()),
					1999, (int)result.get(result.size() - 1));
		}
	}

}
