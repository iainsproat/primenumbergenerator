package com.iainsproat.simscaletest.primenumbergenerator.server;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.iainsproat.simscaletest.primenumbergenerator.server.controller.PrimeNumberGeneratorController;

import spark.Spark;
import spark.servlet.SparkApplication;

/**
 * Responsible for defining routes of the REST API
 * handling requests and returning responses
 *
 *
 * See http://sparkjava.com/documentation.html for documentation of this framework. 
 */
public class API implements SparkApplication {
	private Logger logger = LoggerFactory.getLogger(API.class);
	
	PrimeNumberGeneratorController controller;
	
	@Override
	public void init() {
		controller = new PrimeNumberGeneratorController();

		Spark.exception(Exception.class, (exception, request, response) -> {
			exception.printStackTrace();
		});
	}
	
	@Override
	public void destroy() {
		try {
			spark.Spark.stop(); //stop the server
			controller.close(); //close the database connection
		} catch (Exception e) {
			logger.error(e.getMessage());
		}
	}
}
