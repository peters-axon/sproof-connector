package com.axonivy.connector.sproof.service;

import ch.ivyteam.ivy.environment.Ivy;

public class SproofService {
	private static final String SPROOF_CONNECTOR_API_TOKEN = "com.axonivy.connector.sproof.apiToken";
	private static final SproofService INSTANCE = new SproofService();
	
	public static SproofService get() {
		return INSTANCE;
	}
	
	/**
	 * Get the Sproof API Token.
	 * 
	 * @return
	 */
	public String getApiToken() {
		return Ivy.var().get(SPROOF_CONNECTOR_API_TOKEN);
	}
}
