package org.jboss.tools.switchyard.reddeer.requirement;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import org.jboss.tools.runtime.reddeer.Namespaces;

@XmlRootElement(name = "integrationPack", namespace = Namespaces.SOA_REQ)
@XmlAccessorType(XmlAccessType.FIELD)
public class IntegrationPack {

	@XmlElement(name = "kieVersion", namespace = Namespaces.SOA_REQ)
	private String kieVersion;
	@XmlElement(name = "integrationPackVersion", namespace = Namespaces.SOA_REQ)
	private String integrationPackVersion;

	public String getKieVersion() {
		return kieVersion;
	}

	public void setKieVersion(String kieVersion) {
		this.kieVersion = kieVersion;
	}

	public String getIntegrationPackVersion() {
		return integrationPackVersion;
	}

	public void setIntegrationPackVersion(String integrationPackVersion) {
		this.integrationPackVersion = integrationPackVersion;
	}

}
