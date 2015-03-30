package org.jboss.tools.teiid.reddeer.requirement;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

import org.jboss.tools.runtime.reddeer.Namespaces;
import org.jboss.tools.runtime.reddeer.requirement.ServerConfig;

/**
 * 
 * @author asmigala
 *
 */
@XmlRootElement(name="teiid-requirement", namespace=Namespaces.SOA_REQ)
@XmlAccessorType(XmlAccessType.FIELD)
public class TeiidConfiguration extends ServerConfig {

	
	@XmlElementWrapper(name="connection-profiles", namespace=Namespaces.SOA_REQ)
	@XmlElement(name="connection-profile", namespace=Namespaces.SOA_REQ)
	private List<ConnectionProfileConfig> connectionProfiles;
	
	public List<ConnectionProfileConfig> getConnectionProfiles() {
		return connectionProfiles;
	}

	public void setConnectionProfiles(
			List<ConnectionProfileConfig> connectionProfiles) {
		this.connectionProfiles = connectionProfiles;
	}

	
	public ConnectionProfileConfig getConnectionProfile(String name){
		for(ConnectionProfileConfig cp : connectionProfiles){
			if (cp.getName().equals(name)){
				return cp;
			}
		}
		return null;
	}
	

}
