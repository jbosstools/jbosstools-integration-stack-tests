package org.jboss.tools.runtime.ui.bot.test;

import static org.junit.Assert.assertEquals;

import org.eclipse.reddeer.eclipse.m2e.core.ui.preferences.MavenSettingsPreferencePage;
import org.eclipse.reddeer.junit.requirement.inject.InjectRequirement;
import org.eclipse.reddeer.junit.runner.RedDeerSuite;
import org.eclipse.reddeer.requirements.server.ServerRequirementState;
import org.eclipse.reddeer.workbench.ui.dialogs.WorkbenchPreferenceDialog;
import org.jboss.tools.runtime.reddeer.requirement.ServerRequirement;
import org.jboss.tools.runtime.reddeer.requirement.ServerRequirement.Server;
import org.junit.Test;
import org.junit.runner.RunWith;

@Server(state = ServerRequirementState.PRESENT, property = "preference")
@RunWith(RedDeerSuite.class)
public class ServerPreferencesTest {

	@InjectRequirement
	protected ServerRequirement requirement;

	@Test
	public void serverPreferenceTest() {
		WorkbenchPreferenceDialog preferences = new WorkbenchPreferenceDialog();
		preferences.open();
		MavenSettingsPreferencePage page = new MavenSettingsPreferencePage(preferences);

		preferences.select(page);
		String location = page.getUserSettingsLocation();
		preferences.cancel();

		assertEquals("target/settings.xml", location);
	}

}
