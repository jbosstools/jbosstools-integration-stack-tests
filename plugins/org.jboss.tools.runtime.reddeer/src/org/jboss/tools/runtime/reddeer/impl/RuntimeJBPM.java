package org.jboss.tools.runtime.reddeer.impl;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

import org.jboss.tools.jbpm.preferences.JbpmInstallation;
import org.jboss.tools.jbpm.preferences.PreferencesManager;
import org.jboss.tools.runtime.reddeer.Namespaces;
import org.jboss.tools.runtime.reddeer.RuntimeBase;
import org.jboss.tools.runtime.reddeer.wizard.JBPMRuntimePreferencePage;
import org.jboss.tools.runtime.reddeer.wizard.JBPMRuntimeWizard;

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
		// Add runtime
		JBPMRuntimePreferencePage jbpmRuntimePreferencePage = new JBPMRuntimePreferencePage();
		jbpmRuntimePreferencePage.open();
		JBPMRuntimeWizard jbpmRuntimeWizard = jbpmRuntimePreferencePage.addRuntime();
		jbpmRuntimeWizard.setName(getName());
		jbpmRuntimeWizard.setLocation(getHome());
		jbpmRuntimeWizard.ok();
		jbpmRuntimePreferencePage.ok();
	}

	@Override
	public boolean exists() {
		JbpmInstallation jbpmInstallation = PreferencesManager.getInstance().getJbpmInstallation(name);
		return jbpmInstallation != null;
	}
}
