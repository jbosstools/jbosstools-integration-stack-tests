package org.jboss.tools.runtime.reddeer.impl;

import org.eclipse.reddeer.workbench.ui.dialogs.WorkbenchPreferenceDialog;
import org.jboss.tools.runtime.reddeer.RuntimeBase;
import org.jboss.tools.runtime.reddeer.wizard.JbpmRuntimePreferencePage;
import org.jboss.tools.runtime.reddeer.wizard.JbpmRuntimeWizard;

public class RuntimeJbpm extends RuntimeBase {

	@Override
	public void create() {
		WorkbenchPreferenceDialog preferences = new WorkbenchPreferenceDialog();
		preferences.open();
		JbpmRuntimePreferencePage jbpmRuntimePreferencePage = new JbpmRuntimePreferencePage(preferences);
		preferences.select(jbpmRuntimePreferencePage);
		JbpmRuntimeWizard jbpmRuntimeWizard = jbpmRuntimePreferencePage.addRuntime();
		jbpmRuntimeWizard.setName(getName());
		jbpmRuntimeWizard.setPath(getHome());
		jbpmRuntimeWizard.ok();
		jbpmRuntimePreferencePage.setJbpmRuntimeAsDefault(getName());
		preferences.ok();
	}
}
