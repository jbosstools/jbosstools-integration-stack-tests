package org.jboss.tools.runtime.reddeer.impl;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import org.jboss.tools.runtime.reddeer.Namespaces;

@XmlRootElement(name = "lib", namespace = Namespaces.SOA_REQ)
@XmlAccessorType(XmlAccessType.FIELD)
public class SAPLib {

	@XmlElement(name = "jco3", namespace = Namespaces.SOA_REQ)
	private String jco3;

	@XmlElement(name = "jidoc", namespace = Namespaces.SOA_REQ)
	private String jidoc;

	public String getJco3() {
		return jco3;
	}

	public void setJco3(String jco3) {
		this.jco3 = jco3;
	}

	public String getJidoc() {
		return jidoc;
	}

	public void setJidoc(String jidoc) {
		this.jidoc = jidoc;
	}

}
