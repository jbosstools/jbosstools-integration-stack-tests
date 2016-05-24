package org.jboss.tools.runtime.reddeer.requirement;

import java.util.List;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

import org.jboss.tools.runtime.reddeer.Namespaces;
import org.jboss.tools.runtime.reddeer.impl.SAPDestination;
import org.jboss.tools.runtime.reddeer.impl.SAPLib;
import org.jboss.tools.runtime.reddeer.impl.SAPServer;

/**
 * 
 * @author apodhrad
 * 
 */

@XmlRootElement(name = "sap-requirement", namespace = Namespaces.SOA_REQ)
public class SAPConfig {

	private String name;

	@XmlAttribute(name = "name")
	public void setName(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	@XmlElement(name = "lib", namespace = Namespaces.SOA_REQ)
	private SAPLib lib;

	public SAPLib getLib() {
		return lib;
	}

	@XmlElementWrapper
	@XmlElement(name = "destination", namespace = Namespaces.SOA_REQ)
	private List<SAPDestination> destinations;

	public List<SAPDestination> getDestinations() {
		return destinations;
	}

	public SAPDestination getDestination() {
		return destinations.get(0);
	}

	@XmlElementWrapper
	@XmlElement(name = "server", namespace = Namespaces.SOA_REQ)
	private List<SAPServer> servers;

	public List<SAPServer> getServers() {
		return servers;
	}

	public SAPServer getServer() {
		return servers.get(0);
	}

}