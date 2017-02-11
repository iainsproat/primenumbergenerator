package com.iainsproat.simscaletest.primenumbergenerator.server;

import static spark.Spark.*;

public class API {
	public static void main(String[] args) {
		get("/hello", (req, res) -> "Hello World");
	}
}
