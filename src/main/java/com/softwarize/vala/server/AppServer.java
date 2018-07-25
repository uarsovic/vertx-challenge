package com.softwarize.vala.server;

import com.softwarize.vala.endpoints.Metrics;
import com.softwarize.vala.endpoints.Places;
import com.softwarize.vala.endpoints.impl.MetricsImpl;
import com.softwarize.vala.endpoints.impl.PlacesImpl;
import com.softwarize.vala.mongo.MongoOperations;
import com.softwarize.vala.mongo.impl.MongoOperationsImpl;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;
import io.vertx.ext.mongo.MongoClient;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.client.WebClient;
import io.vertx.ext.web.handler.BodyHandler;

/**
 * 
 * @author u.arsovic
 * @description AppServer class for configuring routes and starting Verticle server
 */
public class AppServer extends AbstractVerticle {

	private Places places;
	private MongoOperations mongoOperations;
	private Metrics metrics;

	@Override
	public void start(Future<Void> futureTask) {
		WebClient webClient = WebClient.create(vertx);
		MongoClient mongoClient = MongoClient.createShared(vertx, config());
		
		//this can be decoupled with spring
		mongoOperations = new MongoOperationsImpl(mongoClient);
		places = new PlacesImpl(webClient, mongoOperations);
		metrics = new MetricsImpl(mongoClient);

		Router router = Router.router(vertx);
		router.route().handler(BodyHandler.create());
		router.post("/findPlaces").handler(this::findPlaces);
		router.get("/metrics").handler(this::getMetrics);

		vertx.createHttpServer().requestHandler(router::accept).listen(config().getInteger("http.port", 8080),
				result -> {
					if (result.succeeded()) {
						futureTask.complete();
					} else {
						futureTask.fail(result.cause());
					}
				});
	}

	private void findPlaces(RoutingContext context) {
		places.getPlaces(context);
	}
	
	private void getMetrics(RoutingContext context) {
		metrics.getMetrics(context);
	}
}
