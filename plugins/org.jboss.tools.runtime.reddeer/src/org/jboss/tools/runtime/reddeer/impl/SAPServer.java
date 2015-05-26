package org.jboss.tools.runtime.reddeer.impl;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import org.jboss.tools.runtime.reddeer.Namespaces;

@XmlRootElement(name = "server", namespace = Namespaces.SOA_REQ)
@XmlAccessorType(XmlAccessType.FIELD)
public class SAPServer {

	@XmlElement(name = "gwhost", namespace = Namespaces.SOA_REQ)
	private String gwhost;

	@XmlElement(name = "progid", namespace = Namespaces.SOA_REQ)
	private String progid;

	@XmlElement(name = "connectionCount", namespace = Namespaces.SOA_REQ)
	private String connectionCount;

	public String getGwhost() {
		return gwhost;
	}

	public void setGwhost(String gwhost) {
		this.gwhost = gwhost;
	}

	public String getProgid() {
		return progid;
	}

	public void setProgid(String progid) {
		this.progid = progid;
	}

	public String getConnectionCount() {
		return connectionCount;
	}

	public void setConnectionCount(String connectionCount) {
		this.connectionCount = connectionCount;
	}

}
