package org.jboss.tools.switchyard.ui.bot.test;

import org.jboss.reddeer.junit.requirement.inject.InjectRequirement;
import org.jboss.reddeer.junit.runner.RedDeerSuite;
import org.jboss.tools.switchyard.reddeer.editor.SwitchYardEditor;
import org.jboss.tools.switchyard.reddeer.requirement.SwitchYardRequirement;
import org.jboss.tools.switchyard.reddeer.requirement.SwitchYardRequirement.SwitchYard;
import org.junit.Test;
import org.junit.runner.RunWith;

@SwitchYard
@RunWith(RedDeerSuite.class)
public class CreateProjectTest {
	
	@InjectRequirement
	private SwitchYardRequirement switchYardRequirement;
	
	@Test
	public void createProjectTest() {
		System.out.println(switchYardRequirement);
		try {
			new SwitchYardEditor().saveAndClose();
		} catch (Exception ex) {
			// it is ok, we just try to close switchyard.xml if it is open
		}
		switchYardRequirement.project("test").create();
		try {
			new SwitchYardEditor().saveAndClose();
		} catch (Exception ex) {
			// it is ok, we just try to close switchyard.xml if it is open
		}
	}
}
