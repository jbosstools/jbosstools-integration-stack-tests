package org.jboss.tools.switchyard.reddeer.requirement;

import org.jboss.tools.runtime.reddeer.requirement.ServerConfiguration;

/**
 * 
 * @author apodhrad
 *
 */
public class SwitchYardConfiguration extends ServerConfiguration {

	private String configurationVersion;
	private String targetRuntime;
	private String libraryVersion;
	private String switchyardVersion;
	private String componentRestriction;
	private IntegrationPack integrationPack;

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

	/**
	 * Returns library version.
	 * 
	 * @deprecated Use switchyardRequirement.getLibraryVersionLabel()
	 * @return library version
	 */
	@Deprecated
	public String getLibraryVersion() {
		return libraryVersion;
	}

	public void setLibraryVersion(String libraryVersion) {
		this.libraryVersion = libraryVersion;
	}

	public String getSwitchyardVersion() {
		return switchyardVersion;
	}

	public void setSwitchyardVersion(String switchyardVersion) {
		this.switchyardVersion = switchyardVersion;
	}

	public String getComponentRestriction() {
		return componentRestriction;
	}

	public void setComponentRestriction(String componentRestriction) {
		this.componentRestriction = componentRestriction;
	}

	public IntegrationPack getIntegrationPack() {
		return integrationPack;
	}

	public void setIntegrationPack(IntegrationPack integrationPack) {
		this.integrationPack = integrationPack;
	}

}
