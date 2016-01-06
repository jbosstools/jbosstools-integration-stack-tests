package org.jboss.tools.teiid.reddeer.requirement;

import java.util.Properties;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;

import org.jboss.tools.runtime.reddeer.Namespaces;

@XmlAccessorType(XmlAccessType.FIELD)
public class ConnectionProfileConfig {

	@XmlElement(namespace = Namespaces.SOA_REQ)
	protected String hostname;
	@XmlElement(required = true, namespace = Namespaces.SOA_REQ)
	protected String vendor;
	@XmlElement(required = true, namespace = Namespaces.SOA_REQ)
	protected String template;
	@XmlElement(name = "name", namespace = Namespaces.SOA_REQ)
	protected String dbName;
	@XmlElement(namespace = Namespaces.SOA_REQ)
	protected String description;
	@XmlElement(name = "vendor_template", namespace = Namespaces.SOA_REQ)
	protected String vendorTemplate;
	@XmlElement(name = "jdbc_path", namespace = Namespaces.SOA_REQ)
	protected String jdbcPath;
	@XmlElement(required = true, namespace = Namespaces.SOA_REQ)
	protected String password;
	@XmlElement(namespace = Namespaces.SOA_REQ)
	protected String version;
	@XmlElement(name = "jdbc_class", namespace = Namespaces.SOA_REQ)
	protected String jdbcClass;
	@XmlElement(namespace = Namespaces.SOA_REQ)
	protected String port;
	@XmlElement(required = true, namespace = Namespaces.SOA_REQ)
	protected String username;
	@XmlAttribute(name = "name", required = true)
	protected String name;
	@XmlElement(name = "properties", namespace = Namespaces.SOA_REQ)
	private ConnectionProfileProperties extraProperties;

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
			result.putAll(extraProperties.getAllProperties());
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

	private void putIfNotNull(Properties props, String key, String value) {
		if (value != null) {
			props.put(key, value);
		}
	}

}
