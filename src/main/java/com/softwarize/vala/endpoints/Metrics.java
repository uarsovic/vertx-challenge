package com.softwarize.vala.endpoints;

import io.vertx.ext.web.RoutingContext;

/**
 * @descrption Metrics interface
 * @author u.arsovic
 *
 */
public interface Metrics {
	
	/**
	 * getMetrics method for getting metrics data from mongoDB
	 */
	public void getMetrics(RoutingContext context);

}
