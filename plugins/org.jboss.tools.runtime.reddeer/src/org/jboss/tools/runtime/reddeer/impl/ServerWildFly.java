package org.jboss.tools.runtime.reddeer.impl;

/**
 * WildFly Server
 * 
 * @author apodhrad
 * 
 */
public class ServerWildFly extends ServerAS {

	private final String category = "JBoss Community";

	private final String label = "WildFly";

	@Override
	public String getCategory() {
		return category;
	}

	@Override
	public String getServerType() {
		return label + "  " + getVersion();
	}

	@Override
	public String getRuntimeType() {
		return label + "  " + getVersion() + " Runtime";
	}

}
