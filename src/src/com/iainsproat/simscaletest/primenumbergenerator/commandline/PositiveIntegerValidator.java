package com.iainsproat.simscaletest.primenumbergenerator.commandline;

import com.beust.jcommander.IParameterValidator;
import com.beust.jcommander.ParameterException;

public class PositiveIntegerValidator implements IParameterValidator {
	public void validate(String name, String value)
			throws ParameterException {
		try{
		int n = Integer.parseInt(value);
		if (n < 0) {
			throw new ParameterException(String.format("Parameter %s should be a positive integer (found %d)", name, value));
		}
		}
		catch(NumberFormatException e)
		{
			throw new ParameterException(String.format("Parameter %s should be a positive integer (found %s)", name, value));
		}
	}
}