package org.jboss.tools.runtime.reddeer.requirement;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElements;
import javax.xml.bind.annotation.XmlRootElement;

import org.jboss.tools.runtime.reddeer.Namespaces;
import org.jboss.tools.runtime.reddeer.RuntimeBase;
import org.jboss.tools.runtime.reddeer.impl.RuntimeESB;
import org.jboss.tools.runtime.reddeer.impl.RuntimeJBPM;

/**
 * 
 * @author apodhrad
 * 
 */

@XmlRootElement(name = "runtime-requirement", namespace = Namespaces.SOA_REQ)
public class RuntimeConfig {

	private String name;

	@XmlAttribute(name = "name")
	public void setName(String serverName) {
		this.name = serverName;
	}

	public String getName() {
		return name;
	}

	@XmlElements({ @XmlElement(name = "esb", namespace = Namespaces.SOA_REQ, type = RuntimeESB.class),
			@XmlElement(name = "jbpm", namespace = Namespaces.SOA_REQ, type = RuntimeJBPM.class) })
	private RuntimeBase runtimeBase;

	public RuntimeBase getRuntimeFamily() {
		runtimeBase.setName(name);
		return runtimeBase;
	}

}