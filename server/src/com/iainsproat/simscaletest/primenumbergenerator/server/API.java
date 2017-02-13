package com.iainsproat.simscaletest.primenumbergenerator.server;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.iainsproat.simscaletest.primenumbergenerator.server.controller.PrimeNumberGeneratorController;

import spark.Spark;

/**
 * Responsible for defining routes of the REST API
 * handling requests and returning responses
 *
 *
 * See http://sparkjava.com/documentation.html for documentation of this framework. 
 */
public class API {
	
	public static void main(String[] args) {
		Logger logger = LoggerFactory.getLogger(API.class);
		
		PrimeNumberGeneratorController controller = new PrimeNumberGeneratorController();
		 
		 Spark.exception(Exception.class, (exception, request, response) -> {
			    exception.printStackTrace();
			});
		 
		 //FIXME I don't think this code is ever reached...
		 //stop the web service
		 try{
			 logger.info("Closing web service");
			 Spark.stop();
			 controller.close();
		 }
		 catch(Exception e)
		 {
			 logger.error(e.getMessage());
			 logger.error(e.getStackTrace().toString());
		 }
	}
}
