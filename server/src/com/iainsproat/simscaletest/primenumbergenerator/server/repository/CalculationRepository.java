package com.iainsproat.simscaletest.primenumbergenerator.server.repository;

import com.iainsproat.simscaletest.primenumbergenerator.core.PrimeNumberGeneratorStrategyClient.Result;

public class CalculationRepository {

	public boolean store(Result calculationResult) {
		DatabaseConnection db = new DatabaseConnection();
		return db.addCalculation(calculationResult);
	}
}
