package org.jboss.tools.runtime.ui.bot.test;

import static org.junit.Assert.assertEquals;

import org.jboss.reddeer.workbench.ui.dialogs.WorkbenchPreferenceDialog;
import org.jboss.reddeer.eclipse.m2e.core.ui.preferences.MavenSettingsPreferencePage;
import org.jboss.reddeer.junit.requirement.inject.InjectRequirement;
import org.jboss.reddeer.junit.runner.RedDeerSuite;
import org.jboss.reddeer.requirements.server.ServerReqState;
import org.jboss.tools.runtime.reddeer.requirement.ServerReqType;
import org.jboss.tools.runtime.reddeer.requirement.ServerRequirement;
import org.jboss.tools.runtime.reddeer.requirement.ServerRequirement.Server;
import org.junit.Test;
import org.junit.runner.RunWith;

@Server(type = ServerReqType.ANY, state = ServerReqState.PRESENT, property = "preference")
@RunWith(RedDeerSuite.class)
public class ServerPreferencesTest {

	@InjectRequirement
	protected ServerRequirement requirement;

	@Test
	public void serverPreferenceTest() {
		MavenSettingsPreferencePage page = new MavenSettingsPreferencePage();
		
		WorkbenchPreferenceDialog preferences = new WorkbenchPreferenceDialog();
		preferences.open();
		preferences.select(page);
		String location = page.getUserSettingsLocation();
		preferences.cancel();
		
		assertEquals("target/settings.xml", location);
	}

}
