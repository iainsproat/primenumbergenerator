package com.iainsproat.simscaletest.primenumbergenerator.core.test;

import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.instanceOf;

import org.junit.Test;

import com.iainsproat.simscaletest.primenumbergenerator.core.PrimeNumberGeneratorStrategy;
import com.iainsproat.simscaletest.primenumbergenerator.core.StrategySelector;
import com.iainsproat.simscaletest.primenumbergenerator.core.generators.*;

public class StrategySelectorTest {

	@Test
	public void canSelectNaive1Strategy() {

		PrimeNumberGeneratorStrategy result = StrategySelector.selectStrategy("naive1");
		assertThat(result, instanceOf(Naive1PrimeNumberGenerator.class));
	}
	
	@Test
	public void canSelectNaive2Strategy() {

		PrimeNumberGeneratorStrategy result = StrategySelector.selectStrategy("naive2");
		assertThat(result, instanceOf(Naive2PrimeNumberGenerator.class));
	}
	
	@Test
	public void canSelectEratosthenesStrategy() {

		PrimeNumberGeneratorStrategy result = StrategySelector.selectStrategy("eratosthenes");
		assertThat(result, instanceOf(EratosthenesPrimeNumberGenerator.class));
	}
	
	@Test
	public void canSelectEratosthenesBitSetStrategy() {

		PrimeNumberGeneratorStrategy result = StrategySelector.selectStrategy("eratosthenesbitset");
		assertThat(result, instanceOf(EratosthenesBitSetPrimeNumberGenerator.class));
	}
	
	@Test
	public void canSelectSundaramStrategy() {

		PrimeNumberGeneratorStrategy result = StrategySelector.selectStrategy("sundaram");
		assertThat(result, instanceOf(SundaramPrimeNumberGenerator.class));
	}
	
	@Test
	public void canSelectEratosthenesParallelStrategy() {

		PrimeNumberGeneratorStrategy result = StrategySelector.selectStrategy("eratosthenesparallel");
		assertThat(result, instanceOf(EratosthenesParallelPrimeNumberGenerator.class));
	}
	
	@Test(expected = UnsupportedOperationException.class)
	public void cannotSelectIncorrectStrategy() {
		StrategySelector.selectStrategy("");
	}
}
