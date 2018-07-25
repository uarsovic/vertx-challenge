package com.softwarize.vala.mongo;

/**
 * 
 * @author u.arsovic
 * @description MongoOperationis interface
 */
public interface MongoOperations {

	/**
	 * @description save or update country metrics
	 * @param country here we should use two letter country code by ISO standard
	 */
	public void saveOrUpdateCountryMetrics(String country);

}
