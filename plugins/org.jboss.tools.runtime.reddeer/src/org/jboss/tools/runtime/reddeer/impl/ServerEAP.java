package org.jboss.tools.runtime.reddeer.impl;

/**
 * EAP Server
 * 
 * @author apodhrad
 * 
 */
public class ServerEAP extends ServerAS {

	public static final String CATEGORY = "Red Hat JBoss Middleware";

	private final String label = "Red Hat JBoss Enterprise Application Platform";

	@Override
	public String getCategory() {
		return CATEGORY;
	}

	@Override
	public String getServerType() {
		return label + " " + getVersion();
	}

	@Override
	public String getRuntimeType() {
		return label + " " + getVersion() + " Runtime";
	}

}
