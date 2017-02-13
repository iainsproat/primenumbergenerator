package com.iainsproat.simscaletest.primenumbergenerator.server.repository;

import com.iainsproat.simscaletest.primenumbergenerator.core.PrimeNumberGeneratorStrategyClient.Result;

public class CalculationRepository implements AutoCloseable{

	DatabaseConnection db;
	
	public void store(Result calculationResult) {
		checkIfDatabaseExistsAndCreateIfNot();
		
		db.addCalculation(calculationResult);
	}

	protected void checkIfDatabaseExistsAndCreateIfNot() {
		if(!databaseExists()){
			db = DatabaseConnection.getInstance();
		}
	}
	
	protected boolean databaseExists()
	{
		return this.db != null;
	}

	@Override
	public void close() throws Exception {
		if(databaseExists()){
			db.close();
		}
	}
}
