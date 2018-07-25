package com.softwarize.vala.endpoints;

import io.vertx.ext.web.RoutingContext;

/**
 * 
 * @author u.arsovic
 * @description Places interfaces
 */
public interface Places {

	/**
	 *  @description getPlaces method is handling google api requests and writing metrics to mongoDB
	 */
	public void getPlaces(RoutingContext context);
}
