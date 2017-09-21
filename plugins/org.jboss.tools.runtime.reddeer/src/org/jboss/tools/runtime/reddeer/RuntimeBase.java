package org.jboss.tools.runtime.reddeer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.eclipse.wst.server.core.IRuntime;
import org.eclipse.wst.server.core.ServerCore;
import org.jboss.tools.runtime.reddeer.impl.RuntimeBrms;
import org.jboss.tools.runtime.reddeer.impl.RuntimeDrools;
import org.jboss.tools.runtime.reddeer.impl.RuntimeJbpm;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

/**
 * 
 * @author apodhrad
 * 
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY)
@JsonSubTypes({
	@JsonSubTypes.Type(name = "brms", value = RuntimeBrms.class),
	@JsonSubTypes.Type(name = "drools", value = RuntimeDrools.class),
	@JsonSubTypes.Type(name = "jbpm", value = RuntimeJbpm.class) })
public abstract class RuntimeBase {

	protected String name;
	private String version;
	private String home;
	private Map<String, String> properties;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public String getHome() {
		return home;
	}

	public void setHome(String home) {
		this.home = home;
	}

	public Map<String, String> getProperties() {
		return properties;
	}

	public void setProperties(Map<String, String> properties) {
		this.properties = properties;
	}

	public String getProperty(String key) {
		return properties != null ? properties.get(key) : null;
	}

	public List<String> getProperties(String key) {
		return properties != null ? Arrays.asList(new String[] { properties.get(key) }) : new ArrayList<String>();
	}

	public String getProperty(String key, String defaultValue) {
		String value = getProperty(key);
		return value != null ? value : defaultValue;
	}

	public boolean exists() {
		IRuntime[] runtime = ServerCore.getRuntimes();
		for (int i = 0; i < runtime.length; i++) {
			if (runtime[i].getId().equals(name)) {
				return true;
			}
		}
		return false;
	}

	public abstract void create();
}
