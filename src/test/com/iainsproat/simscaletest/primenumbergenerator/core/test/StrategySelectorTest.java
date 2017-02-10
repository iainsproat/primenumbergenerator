package com.iainsproat.simscaletest.primenumbergenerator.core.test;

import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.instanceOf;

import org.junit.Test;

import com.iainsproat.simscaletest.primenumbergenerator.core.PrimeNumberGeneratorStrategy;
import com.iainsproat.simscaletest.primenumbergenerator.core.StrategySelector;
import com.iainsproat.simscaletest.primenumbergenerator.core.BruteForcePrimeNumberGenerator;

public class StrategySelectorTest {

	@Test
	public void canSelectBruteStrategy() {

		PrimeNumberGeneratorStrategy result = StrategySelector.selectStrategy("brute");
		assertThat(result, instanceOf(BruteForcePrimeNumberGenerator.class));
	}
	
	@Test(expected = UnsupportedOperationException.class)
	public void cannotSelectIncorrectStrategy() {
		StrategySelector.selectStrategy("");
	}
}
