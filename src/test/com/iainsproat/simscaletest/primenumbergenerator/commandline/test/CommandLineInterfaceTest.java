package com.iainsproat.simscaletest.primenumbergenerator.commandline.test;

import static org.junit.Assert.assertEquals;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.iainsproat.simscaletest.primenumbergenerator.commandline.CommandLineInterface;



public class CommandLineInterfaceTest {

	CommandLineInterface SUT;
	ByteArrayOutputStream out;
	
	@Before
	public void Before()
	{
		SUT = new CommandLineInterface();
		out = new ByteArrayOutputStream();
	}
	
	@After
	public void After()
	{
		SUT = null;
		out = null;
	}
	
	@Test
	public void testExecuteVerbose() {
		String[] input = {"--strategy", "naive1", "--lower", "2", "--upper", "11"};

		SUT.execute(input, new PrintStream(out));
		
		assertEquals("3, 5, 7", out.toString());
	}
	
	@Test
	public void testExecuteConcise() {
		String[] input = {"-s", "naive1", "-l", "2", "-u", "11"};

		SUT.execute(input, new PrintStream(out));
		
		assertEquals("3, 5, 7", out.toString());
	}
}
