package com.iainsproat.simscaletest.primenumbergenerator.server.controller;

import com.google.gson.Gson;

import spark.ResponseTransformer;
/**
 * Transforms a Java object in to Json equivalent
 * Example code from http://sparkjava.com/documentation.html#response-transformer
 *
 */
public class JsonTransformer implements ResponseTransformer {

	private Gson gson = new Gson();
	
	public String render(Object model) throws Exception {
		return gson.toJson(model);
	}
}
