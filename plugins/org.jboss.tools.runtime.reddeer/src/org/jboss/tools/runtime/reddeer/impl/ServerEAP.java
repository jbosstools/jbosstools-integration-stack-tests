package org.jboss.tools.runtime.reddeer.impl;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

import org.jboss.tools.runtime.reddeer.Namespaces;

/**
 * EAP Server
 * 
 * @author apodhrad
 * 
 */
@XmlRootElement(name = "eap", namespace = Namespaces.SOA_REQ)
@XmlAccessorType(XmlAccessType.FIELD)
public class ServerEAP extends ServerAS {

	public static final String CATEGORY = "Red Hat JBoss Middleware";

	private final String label = "JBoss Enterprise Application Platform";

	@Override
	public String getCategory() {
		return CATEGORY;
	}

	@Override
	public String getServerType() {
		return label + " " + getVersion();
	}

	@Override
	public String getRuntimeType() {
		return label + " " + getVersion() + " Runtime";
	}

}
