package org.jboss.tools.runtime.reddeer.impl;

import org.eclipse.reddeer.direct.preferences.Preferences;
import org.eclipse.reddeer.workbench.ui.dialogs.WorkbenchPreferenceDialog;
import org.jboss.tools.runtime.reddeer.RuntimeBase;
import org.jboss.tools.runtime.reddeer.wizard.DroolsRuntimePreferencePage;
import org.jboss.tools.runtime.reddeer.wizard.DroolsRuntimeWizard;

public class RuntimeDrools extends RuntimeBase {

	@Override
	public void create() {
		WorkbenchPreferenceDialog dialog = new WorkbenchPreferenceDialog();
		dialog.open();
		DroolsRuntimePreferencePage droolsRuntimePreferencePage = new DroolsRuntimePreferencePage(dialog);
		dialog.select(droolsRuntimePreferencePage);
		DroolsRuntimeWizard droolsRuntimeWizard = droolsRuntimePreferencePage.addRuntime();
		droolsRuntimeWizard.setName(getName());
		droolsRuntimeWizard.setPath(getHome());
		droolsRuntimeWizard.ok();
		droolsRuntimePreferencePage.setDroolsRuntimeAsDefault(getName());
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
			if (runtime.split("#")[0].equals(getName())) {
				return true;
			}
		}
		return false;
	}
}
