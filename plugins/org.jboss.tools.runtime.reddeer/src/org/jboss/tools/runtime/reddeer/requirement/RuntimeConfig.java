package org.jboss.tools.runtime.reddeer.requirement;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElements;
import javax.xml.bind.annotation.XmlRootElement;

import org.jboss.tools.runtime.reddeer.Namespaces;
import org.jboss.tools.runtime.reddeer.RuntimeBase;
import org.jboss.tools.runtime.reddeer.impl.RuntimeDrools;

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

	@XmlElements({
		@XmlElement(name = "brms", namespace = Namespaces.SOA_REQ, type = RuntimeDrools.class) })
	private RuntimeBase runtimeBase;

	public RuntimeBase getRuntimeFamily() {
		runtimeBase.setName(name);
		return runtimeBase;
	}

}
