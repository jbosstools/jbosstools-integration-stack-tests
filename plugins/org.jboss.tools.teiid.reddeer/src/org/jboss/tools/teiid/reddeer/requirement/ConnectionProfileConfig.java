package org.jboss.tools.teiid.reddeer.requirement;

import java.util.Map;
import java.util.Properties;

public class ConnectionProfileConfig {

	protected String hostname;
	protected String vendor;
	protected String template;
	protected String dbName;
	protected String description;
	protected String vendorTemplate;
	protected String jdbcPath;
	protected String password;
    protected String version;
	protected String jdbcClass;
	protected String port;
	protected String username;
	protected String name;
	private Map<String, String> extraProperties;

	public ConnectionProfileConfig() {
		System.err.println("creating cp config");
	}

	public Properties asProperties() {
		Properties result = new Properties();
		putIfNotNull(result, "db.hostname", hostname);
		putIfNotNull(result, "db.vendor", vendor);
		putIfNotNull(result, "db.template", template);
		putIfNotNull(result, "db.name", dbName);
		putIfNotNull(result, "db.description", description);
		putIfNotNull(result, "db.vendor_template", vendorTemplate);
		putIfNotNull(result, "db.jdbc_path", jdbcPath);
		putIfNotNull(result, "db.password", password);
		putIfNotNull(result, "db.version", version);
		putIfNotNull(result, "db.jdbc_class", jdbcClass);
		putIfNotNull(result, "db.port", port);
		putIfNotNull(result, "db.username", username);
		if (extraProperties != null) {
			result.putAll(extraProperties);
		}
		return result;
	}

	public String getHostname() {
		return hostname;
	}

	public void setHostname(String hostname) {
		this.hostname = hostname;
	}

	public String getVendor() {
		return vendor;
	}

	public void setVendor(String vendor) {
		this.vendor = vendor;
	}

	public String getTemplate() {
		return template;
	}

	public void setTemplate(String template) {
		this.template = template;
	}

	public String getDbName() {
		return dbName;
	}

	public void setDbName(String dbName) {
		this.dbName = dbName;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getVendorTemplate() {
		return vendorTemplate;
	}

	public void setVendorTemplate(String vendorTemplate) {
		this.vendorTemplate = vendorTemplate;
	}

	public String getJdbcPath() {
		return jdbcPath;
	}

	public void setJdbcPath(String jdbcPath) {
		this.jdbcPath = jdbcPath;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

    public String getVersion() {
		return version;
	}

    public void setVersion(String version) {
		this.version = version;
	}

	public String getJdbcClass() {
		return jdbcClass;
	}

	public void setJdbcClass(String jdbcClass) {
		this.jdbcClass = jdbcClass;
	}

	public String getPort() {
		return port;
	}

	public void setPort(String port) {
		this.port = port;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Map<String, String> getExtraProperties() {
		return extraProperties;
	}

	public void setExtraProperties(Map<String, String> extraProperties) {
		this.extraProperties = extraProperties;
	}

	private void putIfNotNull(Properties props, String key, String value) {
		if (value != null) {
			props.put(key, value);
		}
	}

}
