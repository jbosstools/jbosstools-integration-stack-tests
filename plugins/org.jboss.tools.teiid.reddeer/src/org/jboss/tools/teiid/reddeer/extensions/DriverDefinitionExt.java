package org.jboss.tools.teiid.reddeer.extensions;

import org.jboss.reddeer.eclipse.datatools.ui.DriverDefinition;

/**
 * 
 * @author lfabriko
 *
 */
public class DriverDefinitionExt extends DriverDefinition {

	private static final String DEFAULT_DATABASE_NAME = "database";
	private String vendorTemplate;
	private String connectionUrl;
	private String databaseName;

	public String getVendorTemplate() {
		return vendorTemplate;
	}

	public void setVendorTemplate(String vendorTemplate) {
		this.vendorTemplate = vendorTemplate;
	}

	public String getConnectionUrl() {
		return connectionUrl;
	}

	public void setConnectionUrl(String connectionUrl) {
		this.connectionUrl = connectionUrl;
	}

	public String getDatabaseName() {
		if (databaseName == null) {
			return DEFAULT_DATABASE_NAME;
		}
		return this.databaseName;
	}

	public void setDatabaseName(String databaseName) {
		this.databaseName = databaseName;
	}

}
