package org.jboss.tools.switchyard.reddeer.requirement;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import org.jboss.tools.runtime.reddeer.Namespaces;
import org.jboss.tools.runtime.reddeer.requirement.ServerConfig;

/**
 * 
 * @author apodhrad
 *
 */
@XmlRootElement(name = "switchyard-requirement", namespace = Namespaces.SOA_REQ)
@XmlAccessorType(XmlAccessType.FIELD)
public class SwitchYardConfig extends ServerConfig {

	@XmlElement(name = "configurationVersion", namespace = Namespaces.SOA_REQ)
	private String configurationVersion;
	@XmlElement(name = "targetRuntime", namespace = Namespaces.SOA_REQ)
	private String targetRuntime;
	@XmlElement(name = "libraryVersion", namespace = Namespaces.SOA_REQ)
	private String libraryVersion;
	@XmlElement(name = "componentRestriction", namespace = Namespaces.SOA_REQ)
	private String componentRestriction;

	public String getConfigurationVersion() {
		return configurationVersion;
	}

	public void setConfigurationVersion(String configurationVersion) {
		this.configurationVersion = configurationVersion;
	}

	public String getTargetRuntime() {
		return targetRuntime;
	}

	public void setTargetRuntime(String targetRuntime) {
		this.targetRuntime = targetRuntime;
	}

	public String getLibraryVersion() {
		return libraryVersion;
	}

	public void setLibraryVersion(String libraryVersion) {
		this.libraryVersion = libraryVersion;
	}

	public String getComponentRestriction() {
		return componentRestriction;
	}

	public void setComponentRestriction(String componentRestriction) {
		this.componentRestriction = componentRestriction;
	}

}
