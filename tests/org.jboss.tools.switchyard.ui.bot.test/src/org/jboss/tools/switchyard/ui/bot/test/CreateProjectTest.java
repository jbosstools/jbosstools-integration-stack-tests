package org.jboss.tools.switchyard.ui.bot.test;

import org.jboss.tools.switchyard.reddeer.editor.SwitchYardEditor;
import org.jboss.tools.switchyard.reddeer.wizard.SwitchYardProjectWizard;
import org.jboss.tools.switchyard.ui.bot.test.suite.SwitchyardSuite;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(SwitchyardSuite.class)
public class CreateProjectTest {

	@Test
	public void createProjectTest() {
		try {
			new SwitchYardEditor().saveAndClose();
		} catch (Exception ex) {
			// it is ok, we just try to close switchyard.xml if it is open
		}
		String version = SwitchyardSuite.getLibraryVersion();
		new SwitchYardProjectWizard("test", version).create();
		try {
			new SwitchYardEditor().saveAndClose();
		} catch (Exception ex) {
			// it is ok, we just try to close switchyard.xml if it is open
		}
	}
}
