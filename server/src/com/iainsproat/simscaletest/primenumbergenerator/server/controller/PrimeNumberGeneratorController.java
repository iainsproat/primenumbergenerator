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
public class PrimeNumberGeneratorController {
	private Logger logger = LoggerFactory.getLogger(PrimeNumberGeneratorController.class);
	
	private CalculationRepository calculationRepository = new CalculationRepository();
	
	public PrimeNumberGeneratorController()
	{
		Spark.get("/primes/:strategy", (req, res) -> {
			logger.info("GET /primes route called");
			PrimeNumberGeneratorService service = new PrimeNumberGeneratorService();
			PrimeNumberGeneratorStrategyClient.Result calculationResult = service.calculatePrimes(
					req.params(":strategy"), 
					req.queryMap("lower").integerValue(), 
					req.queryMap("upper").integerValue());
			
			calculationRepository.store(calculationResult);
			
			return calculationResult;
		}, new JsonTransformer());
	}

	
}
