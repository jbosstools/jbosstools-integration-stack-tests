package org.jboss.tools.fuse.reddeer.requirement;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import org.jboss.tools.runtime.reddeer.Namespaces;
import org.jboss.tools.runtime.reddeer.requirement.ServerConfig;

/**
 * @author tsedmik
 */
@XmlRootElement(name = "fuse-requirement", namespace = Namespaces.SOA_REQ)
@XmlAccessorType(XmlAccessType.FIELD)
public class FuseConfig extends ServerConfig {

	@XmlElement(name = "camelVersion", namespace = Namespaces.SOA_REQ)
	private String camelVersion;

	public String getCamelVersion() {
		return camelVersion;
	}

	public void setCamelVersion(String camelVersion) {
		this.camelVersion = camelVersion;
	}
}
