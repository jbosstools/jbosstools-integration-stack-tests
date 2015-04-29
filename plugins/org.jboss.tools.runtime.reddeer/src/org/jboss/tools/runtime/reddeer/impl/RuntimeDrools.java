package org.jboss.tools.runtime.reddeer.impl;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

import org.jboss.reddeer.direct.preferences.Preferences;
import org.jboss.tools.runtime.reddeer.Namespaces;
import org.jboss.tools.runtime.reddeer.RuntimeBase;
import org.jboss.tools.runtime.reddeer.wizard.DroolsRuntimePreferencePage;
import org.jboss.tools.runtime.reddeer.wizard.DroolsRuntimeWizard;

@XmlRootElement(name = "brms", namespace = Namespaces.SOA_REQ)
@XmlAccessorType(XmlAccessType.FIELD)
public class RuntimeDrools extends RuntimeBase {
	
	@Override
	public void create() {
		DroolsRuntimePreferencePage droolsRuntimePreferencePage = new DroolsRuntimePreferencePage();
		droolsRuntimePreferencePage.open();
		DroolsRuntimeWizard droolsRuntimeWizard = droolsRuntimePreferencePage.addRuntime();
		droolsRuntimeWizard.setName(getName());
		droolsRuntimeWizard.setPath(getHome());
		droolsRuntimeWizard.ok();
		droolsRuntimePreferencePage.ok();
	}
	
	@Override
	public boolean exists() {
		String droolsDefinition = Preferences.get("org.drools.eclipse", "Drools.Runtimes");
		if (droolsDefinition == null) {
			return false;
		}
		String[] droolsRuntimes = droolsDefinition.split("###");
		for (String runtime : droolsRuntimes) {
			if (runtime.split("#")[0].equals(name)) {
				return true;
			}
		}
		return false;
	}
}
