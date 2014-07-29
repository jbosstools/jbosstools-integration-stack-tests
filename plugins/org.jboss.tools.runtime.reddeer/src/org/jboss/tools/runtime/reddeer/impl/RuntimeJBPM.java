package org.jboss.tools.runtime.reddeer.impl;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

import org.jboss.tools.runtime.reddeer.Namespaces;
import org.jboss.tools.runtime.reddeer.RuntimeBase;

/**
 * jBPM Runtime
 * 
 * @author apodhrad
 * 
 */
@XmlRootElement(name = "jbpm", namespace = Namespaces.SOA_REQ)
@XmlAccessorType(XmlAccessType.FIELD)
public class RuntimeJBPM extends RuntimeBase {

	@Override
	public void create() {
		throw new UnsupportedOperationException();
	}
}
