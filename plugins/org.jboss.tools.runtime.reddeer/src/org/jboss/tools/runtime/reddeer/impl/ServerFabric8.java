package org.jboss.tools.runtime.reddeer.impl;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

import org.jboss.tools.runtime.reddeer.Namespaces;

/**
 * Fabric8 Server
 * 
 * @author tsedmik
 */
@XmlRootElement(name = "fabric8", namespace = Namespaces.SOA_REQ)
@XmlAccessorType(XmlAccessType.FIELD)
public class ServerFabric8 extends ServerKaraf {

	private final String category = "JBoss Fuse";
	private final String label = "Fabric8";
	
	@Override
	public String getCategory() {
		return category;
	}

	@Override
	public String getServerType() {
		return label + " " + getVersion() + " Server";
	}

	@Override
	public String getRuntimeType() {
		return label + " " + getVersion();
	}

	@Override
	public String getRuntimeName() {
		return label + " " + getVersion() + " Runtime";
	}
}
