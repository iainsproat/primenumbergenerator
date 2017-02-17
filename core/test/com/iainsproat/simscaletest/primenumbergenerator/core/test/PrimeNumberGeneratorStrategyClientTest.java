package com.iainsproat.simscaletest.primenumbergenerator.core.test;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import com.iainsproat.simscaletest.primenumbergenerator.core.PrimeNumberGeneratorStrategy;
import com.iainsproat.simscaletest.primenumbergenerator.core.PrimeNumberGeneratorStrategyClient;

public class PrimeNumberGeneratorStrategyClientTest {

	@Test
	public void testExecutePrimeNumberGeneratorStrategyIntInt() {
		PrimeNumberGeneratorStrategyClient SUT = new PrimeNumberGeneratorStrategyClient();
		PrimeNumberGeneratorStrategyClient.Result result = SUT.execute(new MockPrimeNumberGeneratorStrategy(), 0, 10);
		assertEquals(result.getPrimeNumbers().size(), 3);
		assertEquals((int)result.getPrimeNumbers().get(1), 5);
	}
	
	public class MockPrimeNumberGeneratorStrategy implements PrimeNumberGeneratorStrategy
	{
		@Override
		public List<Integer> execute(int lowerBound, int upperBound) {
			try {
				Thread.sleep(500);
			} catch (InterruptedException e) {
				fail(e.getMessage());
			}
			
			List<Integer> result = new ArrayList<Integer>();
			result.add(3);
			result.add(5);
			result.add(7);
			return result;
		}
		
	}
}
