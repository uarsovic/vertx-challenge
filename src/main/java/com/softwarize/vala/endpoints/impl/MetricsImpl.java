package com.softwarize.vala.endpoints.impl;

import java.util.List;

import com.softwarize.vala.commons.CommonStrings;
import com.softwarize.vala.endpoints.Metrics;

import io.vertx.core.json.JsonObject;
import io.vertx.ext.mongo.MongoClient;
import io.vertx.ext.web.RoutingContext;
/**
 * 
 * @author u.arsovic
 * @description Implementation of metrics interface
 */
public class MetricsImpl implements Metrics {

	// this can be decoupled with spring
	private MongoClient mongoClient;

	//constructor can be decoupled with spring
	public MetricsImpl(MongoClient mongoClient) {
		this.mongoClient = mongoClient;
	}

	public void getMetrics(RoutingContext context) {
		// empty json for find all documents in the table
		JsonObject queryAll = new JsonObject();
		//query could be done better and result could be calculated at DB level
		mongoClient.find(CommonStrings.COLLECTION_NAME, queryAll, result -> {
			JsonObject countryMetrics = new JsonObject();
			List<JsonObject> countryList = result.result();
			Integer totalCount = Integer.valueOf(0);
			for (JsonObject country : countryList) {
				totalCount += country.getInteger(CommonStrings.REQUEST_COUNT);
			}
			countryMetrics.put(CommonStrings.TOTAL_COUNT, totalCount);
			countryMetrics.put(CommonStrings.COUNTRIES, countryList);
			context.response().setChunked(true).putHeader("content-type", "application/json").write(countryMetrics.toString()).end();
		});
	}
}
