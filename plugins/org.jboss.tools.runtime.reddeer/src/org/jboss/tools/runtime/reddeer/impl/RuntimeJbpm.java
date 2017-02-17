package org.jboss.tools.runtime.reddeer.impl;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

import org.jboss.tools.runtime.reddeer.Namespaces;
import org.jboss.tools.runtime.reddeer.RuntimeBase;
import org.jboss.tools.runtime.reddeer.wizard.JbpmRuntimePreferencePage;
import org.jboss.tools.runtime.reddeer.wizard.JbpmRuntimeWizard;

@XmlRootElement(name = "bpms", namespace = Namespaces.SOA_REQ)
@XmlAccessorType(XmlAccessType.FIELD)
public class RuntimeJbpm extends RuntimeBase {

	@Override
	public void create() {
		JbpmRuntimePreferencePage jbpmRuntimePreferencePage = new JbpmRuntimePreferencePage();
		jbpmRuntimePreferencePage.open();
		JbpmRuntimeWizard jbpmRuntimeWizard = jbpmRuntimePreferencePage.addRuntime();
		jbpmRuntimeWizard.setName(getName());
		jbpmRuntimeWizard.setPath(getHome());
		jbpmRuntimeWizard.ok();
		jbpmRuntimePreferencePage.setJbpmRuntimeAsDefault(getName());
		jbpmRuntimePreferencePage.ok();
	}
}
