package com.iainsproat.simscaletest.primenumbergenerator.server;

import static spark.Spark.*;

public class API {
	public static void main(String[] args) {
		get("/primes/:strategy", (req, res) -> {
			return String.format("You requested Prime Numbers calculated by the %s strategy, between %d and %d",
					req.params(":strategy"), 
					req.queryMap("lower").integerValue(), 
					req.queryMap("upper").integerValue()); 
		});
	}
}
