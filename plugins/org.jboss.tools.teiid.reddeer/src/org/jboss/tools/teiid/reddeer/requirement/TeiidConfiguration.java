package org.jboss.tools.teiid.reddeer.requirement;

import java.util.List;

import org.jboss.tools.runtime.reddeer.requirement.ServerConfiguration;

/**
 * 
 * @author asmigala
 *
 */
public class TeiidConfiguration extends ServerConfiguration {

	private List<ConnectionProfileConfig> connectionProfiles;

	public List<ConnectionProfileConfig> getConnectionProfiles() {
		return connectionProfiles;
	}

	public void setConnectionProfiles(List<ConnectionProfileConfig> connectionProfiles) {
		this.connectionProfiles = connectionProfiles;
	}

	public ConnectionProfileConfig getConnectionProfile(String name) {
		for (ConnectionProfileConfig cp : connectionProfiles) {
			if (cp.getName().equals(name)) {
				return cp;
			}
		}
		return null;
	}

}
