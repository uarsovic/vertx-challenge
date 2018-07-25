package com.softwarize.vala.endpoints.impl;

import com.softwarize.vala.commons.CommonStrings;
import com.softwarize.vala.endpoints.Places;
import com.softwarize.vala.mongo.MongoOperations;

import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.client.WebClient;

/**
 * 
 * @author u.arsovic 
 * @description Implementation class for places interface
 */
public class PlacesImpl implements Places {

	// this can be decoupled with spring
	private WebClient client;
	private MongoOperations mongoOperations;

	//constructor can be decoupled with spring
	public PlacesImpl(WebClient client, MongoOperations mongoOperations) {
		this.client = client;
		this.mongoOperations = mongoOperations;
	}

	public void getPlaces(RoutingContext context) {
		context.response().setChunked(true);
		// getting post request body values
		JsonObject requestBody = context.getBodyAsJson();
		String country = requestBody.getString(CommonStrings.COUNTRY);
		String userText = requestBody.getString(CommonStrings.USER_TEXT);
		// logic if country code is not by ISO standard
		if (country.length() > 2 || country.length() < 2) {
			JsonObject errorResponse = new JsonObject().put(CommonStrings.MESSAGE, "Country length must be by ISO 3166-1 Alpha-2 e.g. rs");
			context.response().setStatusCode(400).write(errorResponse.toString()).end();
			return;
		}
		
		client.get(443, "maps.googleapis.com", "/maps/api/place/autocomplete/json?components=country:" + country
				+ "&input=" + userText + "&key=AIzaSyBCccY-rddH5zKJkDR_keIz2WBoNlFLeHM").ssl(true).send(asyncResult -> {
					if (asyncResult.succeeded()) {
						mongoOperations.saveOrUpdateCountryMetrics(country);
						context.response().putHeader("Content-Type", "application/json")
								.write(asyncResult.result().bodyAsBuffer()).end();
					} else {
						//this response can be done better
						context.response().setStatusCode(404).write(asyncResult.cause().getMessage()).end();
					}
				});
	}

}
