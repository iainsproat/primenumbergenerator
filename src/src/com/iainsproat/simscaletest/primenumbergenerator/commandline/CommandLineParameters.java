package com.iainsproat.simscaletest.primenumbergenerator.commandline;

import com.beust.jcommander.Parameter;

/**
 * Defines the expected command line parameters
 *
 */
public class CommandLineParameters {
	@Parameter(names = {"--strategy", "-s"}, description= "The algorithm to be used for calculating the prime numbers")
	public String strategy;
	
	@Parameter(names = {"--lower", "-l"}, description= "The lower bound within which to search for a prime number.", validateWith = PositiveIntegerValidator.class)
	public Integer lowerBound;
	
	@Parameter(names = {"--upper", "-u"}, description= "The upper bound within which to search for a prime number.", validateWith = PositiveIntegerValidator.class)
	public Integer upperBound;
	
}
