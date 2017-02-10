package com.iainsproat.simscaletest.primenumbergenerator.commandline;

import com.beust.jcommander.JCommander;

import com.iainsproat.simscaletest.primenumbergenerator.core.PrimeNumberGeneratorStrategyClient;

/**
 * This class is responsible for reading and writing to the command line
 *
 */
public class CommandLineInterface {

	public static void main(String[] args) {
		CommandLineParameters parameters = new CommandLineParameters();
		new JCommander(parameters, args); //parsing the command line is delegated to the JCommander library
		PrimeNumberGeneratorStrategyClient client = new PrimeNumberGeneratorStrategyClient();
		
		PrimeNumberGeneratorStrategyClient.Result result = client.execute(parameters.strategy, parameters.lowerBound.intValue(), parameters.upperBound.intValue());
		
		System.out.println(String.format("You requested a prime numbers between %s and %s calculated using the %s strategy", result.getLowerBound(), result.getUpperBound(), result.getRequestedStrategy()));
		System.out.println(String.format("The program calculated the following results in %s seconds:", result.getExecutionDuration()/1000000000));
		for(Integer i : result.getPrimeNumbers())
		{
			System.out.print(String.format("%s, ", i));
		}
	}
}
