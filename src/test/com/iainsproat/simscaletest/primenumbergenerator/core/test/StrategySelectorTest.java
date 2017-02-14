package com.iainsproat.simscaletest.primenumbergenerator.core.test;

import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.instanceOf;

import org.junit.Test;

import com.iainsproat.simscaletest.primenumbergenerator.core.PrimeNumberGeneratorStrategy;
import com.iainsproat.simscaletest.primenumbergenerator.core.StrategySelector;
import com.iainsproat.simscaletest.primenumbergenerator.core.generators.Naive1PrimeNumberGenerator;

public class StrategySelectorTest {

	@Test
	public void canSelectnaive1Strategy() {

		PrimeNumberGeneratorStrategy result = StrategySelector.selectStrategy("naive1");
		assertThat(result, instanceOf(Naive1PrimeNumberGenerator.class));
	}
	
	@Test(expected = UnsupportedOperationException.class)
	public void cannotSelectIncorrectStrategy() {
		StrategySelector.selectStrategy("");
	}
}
