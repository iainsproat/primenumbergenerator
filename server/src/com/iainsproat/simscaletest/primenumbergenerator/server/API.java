package com.iainsproat.simscaletest.primenumbergenerator.server;

import static spark.Spark.*;

import com.iainsproat.simscaletest.primenumbergenerator.core.PrimeNumberGeneratorStrategyClient;

/**
 * Responsible for defining routes of the REST API
 * handling requests and returning responses
 *
 *
 * See http://sparkjava.com/documentation.html for documentation of this framework. 
 */
public class API {
	public static void main(String[] args) {
		get("/primes/:strategy", (req, res) -> {
			PrimeNumberGeneratorStrategyClient client = new PrimeNumberGeneratorStrategyClient();
			return client.execute(
					req.params(":strategy"), 
					req.queryMap("lower").integerValue(), 
					req.queryMap("upper").integerValue());
		}, new JsonTransformer());
		
		after((req, res) -> {
			//save the data to a database
		});
	}
}
