package org.jboss.tools.runtime.reddeer.requirement;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
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

	@XmlElement(name = "destination", namespace = Namespaces.SOA_REQ)
	private SAPDestination destination;

	public SAPDestination getDestination() {
		return destination;
	}

	@XmlElement(name = "server", namespace = Namespaces.SOA_REQ)
	private SAPServer server;

	public SAPServer getServer() {
		return server;
	}

}