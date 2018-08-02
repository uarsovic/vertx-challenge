package com.softwarize.vala.mongo.impl;

import com.softwarize.vala.commons.CommonStrings;
import com.softwarize.vala.mongo.MongoOperations;

import io.vertx.core.json.JsonObject;
import io.vertx.ext.mongo.MongoClient;

/**
 * 
 * @author u.arsovic
 * @description implementation of mongo operations interface
 */
public class MongoOperationsImpl implements MongoOperations{

	// this can be decoupled with spring
	private MongoClient mongoClient;

	//constructor can be decoupled with spring
	public MongoOperationsImpl(MongoClient mongoClient) {
		this.mongoClient = mongoClient;
	}

	// save a country if it doesn't exist in the DB	 
	private void saveCountry(String country) {
		JsonObject document = new JsonObject().put(CommonStrings.COUNTRY, country).put(CommonStrings.REQUEST_COUNT,
				Integer.valueOf(1));
		mongoClient.insert(CommonStrings.COLLECTION_NAME, document, result -> {
			// here we can implement logic for error handling
		});
	}

	//update country if it exist in the DB
	private void updateCountry(String country, Integer count) {
		count++;
		JsonObject query = new JsonObject().put(CommonStrings.COUNTRY, country);
		JsonObject update = new JsonObject().put(CommonStrings.COUNTRY, country).put(CommonStrings.REQUEST_COUNT,
				count);
		// had problems with updateDocument method so I used replce
		mongoClient.replaceDocuments(CommonStrings.COLLECTION_NAME, query, update, result -> {
			// here we can implement logic for error handling
		});
	}

	public void saveOrUpdateCountryMetrics(String country) {
		JsonObject query = new JsonObject().put(CommonStrings.COUNTRY, country);
		mongoClient.find(CommonStrings.COLLECTION_NAME, query, result -> {
			if (result.succeeded()) {
				if (result.result().isEmpty()) {
					saveCountry(country);
				} else {
					updateCountry(country, result.result().get(0).getInteger(CommonStrings.REQUEST_COUNT));
				}
			}
			// here we can have else for error handling
		});
	}
}
