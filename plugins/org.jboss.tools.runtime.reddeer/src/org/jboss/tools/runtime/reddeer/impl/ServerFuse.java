package org.jboss.tools.runtime.reddeer.impl;

/**
 * JBoss Fuse Server
 * 
 * @author apodhrad
 */
public class ServerFuse extends ServerKaraf {

	private final String category = "Red Hat JBoss Middleware";
	private final String label = "Red Hat JBoss Fuse";

	private String camelVersion;

	public String getCamelVersion() {
		return camelVersion;
	}

	public void setCamelVersion(String camelVersion) {
		this.camelVersion = camelVersion;
	}

	@Override
	public String getCategory() {
		return category;
	}

	@Override
	public String getServerType() {
		return label + " " + getVersion() + " Server";
	}

	@Override
	public String getRuntimeType() {
		return label + " " + getVersion();
	}

	@Override
	public String getRuntimeName() {
		return label + " " + getVersion() + " Runtime";
	}
}
