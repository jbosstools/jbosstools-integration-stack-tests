package org.jboss.tools.runtime.ui.bot.test;

import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.reddeer.junit.requirement.inject.InjectRequirement;
import org.eclipse.reddeer.junit.runner.RedDeerSuite;
import org.eclipse.reddeer.workbench.ui.dialogs.WorkbenchPreferenceDialog;
import org.jboss.tools.runtime.reddeer.requirement.RuntimeRequirement;
import org.jboss.tools.runtime.reddeer.requirement.RuntimeRequirement.Runtime;
import org.jboss.tools.runtime.reddeer.wizard.DroolsRuntimePreferencePage;
import org.junit.Test;
import org.junit.runner.RunWith;

@Runtime
@RunWith(RedDeerSuite.class)
public class RuntimeTest {

	@InjectRequirement
	protected RuntimeRequirement requirement;

	@Test
	public void runtimePresentTest() {
		// Drools Runtimes
		WorkbenchPreferenceDialog preferences = new WorkbenchPreferenceDialog();
		preferences.open();
		DroolsRuntimePreferencePage droolsPage = new DroolsRuntimePreferencePage(preferences);
		droolsPage.open();
		List<String> droolsRuntimes = droolsPage.getDroolsRuntimes();
		droolsPage.ok();

		List<String> runtimes = new ArrayList<String>();
		runtimes.addAll(droolsRuntimes);
		assertTrue(runtimes.contains(requirement.getConfiguration().getName()));
	}

}
