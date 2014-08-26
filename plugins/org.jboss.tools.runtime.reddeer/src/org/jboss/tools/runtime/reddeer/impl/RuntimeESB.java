package org.jboss.tools.runtime.reddeer.impl;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

import org.jboss.reddeer.direct.preferences.Preferences;
import org.jboss.tools.runtime.reddeer.Namespaces;
import org.jboss.tools.runtime.reddeer.RuntimeBase;
import org.jboss.tools.runtime.reddeer.wizard.ESBRuntimePreferencePage;
import org.jboss.tools.runtime.reddeer.wizard.ESBRuntimeWizard;

/**
 * ESB Runtime
 * 
 * @author apodhrad
 * 
 */
@XmlRootElement(name = "esb", namespace = Namespaces.SOA_REQ)
@XmlAccessorType(XmlAccessType.FIELD)
public class RuntimeESB extends RuntimeBase {

	@Override
	public void create() {
		// Add runtime
		ESBRuntimePreferencePage esbRuntimePreferencePage = new ESBRuntimePreferencePage();
		esbRuntimePreferencePage.open();
		ESBRuntimeWizard esbRuntimeWizard = esbRuntimePreferencePage.addESBRuntime();
		esbRuntimeWizard.setName(getName());
		esbRuntimeWizard.setHomeFolder(getHome());
		esbRuntimeWizard.finish();
		esbRuntimePreferencePage.ok();
	}

	@Override
	public boolean exists() {
		String esbDefinition = Preferences.get("org.jboss.tools.esb.project.core", "jbossesbruntimelocation");
		if (esbDefinition == null) {
			return false;
		}
		String esbName = esbDefinition.split("\\|")[1];
		return esbName.equals(name);
	}
}
