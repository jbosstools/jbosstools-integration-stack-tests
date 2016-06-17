package org.jboss.tools.runtime.reddeer.impl;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import org.jboss.tools.runtime.reddeer.Namespaces;

/**
 * JBoss Fuse Server
 * 
 * @author apodhrad
 */
@XmlRootElement(name = "fuse", namespace = Namespaces.SOA_REQ)
@XmlAccessorType(XmlAccessType.FIELD)
public class ServerFuse extends ServerKaraf {

	private final String category = "JBoss Fuse";
	private final String label = "JBoss Fuse";

	@XmlElement(name = "camelVersion", namespace = Namespaces.SOA_REQ)
	private String camelVersion;

	public String getCamelVersion() {
		return camelVersion;
	}

	public void setCamelVersion(String camelVersion) {
		this.camelVersion = camelVersion;
	}

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
