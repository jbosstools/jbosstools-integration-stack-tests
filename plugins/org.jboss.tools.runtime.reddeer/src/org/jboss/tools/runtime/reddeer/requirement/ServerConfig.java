package org.jboss.tools.runtime.reddeer.requirement;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElements;
import javax.xml.bind.annotation.XmlRootElement;

import org.jboss.tools.runtime.reddeer.Namespaces;
import org.jboss.tools.runtime.reddeer.ServerBase;
import org.jboss.tools.runtime.reddeer.impl.ServerAS;
import org.jboss.tools.runtime.reddeer.impl.ServerEAP;
import org.jboss.tools.runtime.reddeer.impl.ServerFuse;
import org.jboss.tools.runtime.reddeer.impl.ServerKaraf;
import org.jboss.tools.runtime.reddeer.impl.ServerServiceMix;
import org.jboss.tools.runtime.reddeer.impl.ServerWildFly;

/**
 * 
 * @author apodhrad
 * 
 */

@XmlRootElement(name = "server-requirement", namespace = Namespaces.SOA_REQ)
public class ServerConfig {

	private String name;

	@XmlAttribute(name = "name")
	public void setName(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	@XmlElements({
		@XmlElement(name = "as", namespace = Namespaces.SOA_REQ, type = ServerAS.class),
		@XmlElement(name = "eap", namespace = Namespaces.SOA_REQ, type = ServerEAP.class),
		@XmlElement(name = "wildfly", namespace = Namespaces.SOA_REQ, type = ServerWildFly.class),
		@XmlElement(name = "karaf", namespace = Namespaces.SOA_REQ, type = ServerKaraf.class),
		@XmlElement(name = "fuse", namespace = Namespaces.SOA_REQ, type = ServerFuse.class),
		@XmlElement(name = "servicemix", namespace = Namespaces.SOA_REQ, type = ServerServiceMix.class),
		@XmlElement(name = "fabric8", namespace = Namespaces.SOA_REQ, type = ServerServiceMix.class) })
	private ServerBase serverBase;

	public ServerBase getServerBase() {
		if (serverBase != null) {
			serverBase.setName(name);
		}
		return serverBase;
	}

}