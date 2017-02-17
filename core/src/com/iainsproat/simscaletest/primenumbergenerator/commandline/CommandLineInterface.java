package com.iainsproat.simscaletest.primenumbergenerator.commandline;

import java.io.PrintStream;
import java.util.List;

import com.beust.jcommander.JCommander;
import com.beust.jcommander.ParameterException;
import com.iainsproat.simscaletest.primenumbergenerator.core.PrimeNumberGeneratorStrategyClient;

/**
 * This class is responsible for reading and writing to the command line
 * Command line arguments are defined by CommandLineParameters class
 *
 */
public class CommandLineInterface {

	public static void main(String[] args) {
		CommandLineInterface cli = new CommandLineInterface();
		cli.execute(args);
		
	}
	
	public void execute(String[] args)
	{
		this.execute(args, System.out);
	}
	
	/**
	 * Overloaded to allow command line to be tested by injecting a PrintStream that can be tested.
	 */
	public void execute(String[] args, PrintStream out)
	{
		CommandLineParameters parameters = new CommandLineParameters();
		try {
			new JCommander(parameters, args); //parsing the command line is delegated to the JCommander library, and the parameters object properties are set by it

			PrimeNumberGeneratorStrategyClient client = new PrimeNumberGeneratorStrategyClient();

			PrimeNumberGeneratorStrategyClient.Result result = client.execute(parameters.strategy, parameters.lowerBound.intValue(), parameters.upperBound.intValue()); //TODO we're passing a list reference here.  It may be more memory efficient to change the method signature so a printstream is passed and written directly.  

			//print results to output
			if(result.hasError()){
				out.println(String.format("An error was encountered when trying to execute the %s strategy for range %d to %d", 
						parameters.strategy, 
						parameters.lowerBound.intValue(), 
						parameters.upperBound.intValue()));
				for(String error : result.getErrors()){
					out.println(error);
				}
			} else {
				PrintOutput(out, result.getPrimeNumbers());
			}
		
		}
		catch(ParameterException e){
			out.println(String.format("Error: %s", e.getMessage()));
		}
	}
	
	/**
	 * Prints a comma&space-delimited (e.g. ", ") list of integers to the PrintStream
	 * e.g. 3, 5, 7
	 */
	public void PrintOutput(PrintStream out, List<Integer> primes)
	{
		int numberOfPrimes = primes.size();
		if (numberOfPrimes < 1)
			return; //fail silently if there is no result		
		
		out.print(String.format("%d", primes.get(0)));	//the first is outside the loop to ensure that delimiters are only placed between primes, and not trailing at the end of the output	
		for(int i = 1; i < numberOfPrimes; i++)
		{
			out.print(", "); //delimiter between primes
			out.print(String.format("%d", primes.get(i)));
		}
	}
}
