package com.iainsproat.simscaletest.primenumbergenerator.server.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.iainsproat.simscaletest.primenumbergenerator.core.PrimeNumberGeneratorStrategyClient;
import com.iainsproat.simscaletest.primenumbergenerator.server.repository.CalculationRepository;
import com.iainsproat.simscaletest.primenumbergenerator.server.service.PrimeNumberGeneratorService;

import spark.Spark;

/**
 * The controller sets the web framework route,
 * connects the service and repository for
 * creating and handling prime numbers 
 *
 */
public class PrimeNumberGeneratorController implements AutoCloseable {
	private Logger logger = LoggerFactory.getLogger(PrimeNumberGeneratorController.class);
	
	private CalculationRepository calculationRepository;
	
	public PrimeNumberGeneratorController()
	{
		calculationRepository = new CalculationRepository();
		
		Spark.get("/primes/:strategy", (req, res) -> {
			logger.info("GET /primes route called");
			
			//TODO should set response header
			//TODO should validate input and sanitise
			PrimeNumberGeneratorService service = new PrimeNumberGeneratorService();
			PrimeNumberGeneratorStrategyClient.Result calculationResult = service.calculatePrimes(
					req.params(":strategy"), 
					req.queryMap("lower").integerValue(), 
					req.queryMap("upper").integerValue());
			
			calculationRepository.store(calculationResult);
			
			return calculationResult;
		}, new JsonTransformer());
	}

	@Override
	public void close() throws Exception {
		if(calculationRepository != null)
		{
			calculationRepository.close();
		}
	}

	
}
