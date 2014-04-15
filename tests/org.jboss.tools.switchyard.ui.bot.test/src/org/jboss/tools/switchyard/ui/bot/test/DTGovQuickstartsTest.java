package org.jboss.tools.switchyard.ui.bot.test;

import org.jboss.reddeer.requirements.cleanworkspace.CleanWorkspaceRequirement.CleanWorkspace;
import org.jboss.tools.switchyard.ui.bot.test.suite.PerspectiveRequirement.Perspective;
import org.jboss.tools.switchyard.ui.bot.test.suite.ServerRequirement.Server;
import org.jboss.tools.switchyard.ui.bot.test.suite.ServerRequirement.State;
import org.jboss.tools.switchyard.ui.bot.test.suite.ServerRequirement.Type;
import org.jboss.tools.switchyard.ui.bot.test.suite.SwitchyardSuite;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * Test importing dtgov quickstarts
 * 
 * @author apodhrad
 * 
 */
@CleanWorkspace
@Perspective(name = "Java EE")
@Server(type = Type.ALL, state = State.PRESENT)
@RunWith(SwitchyardSuite.class)
public class DTGovQuickstartsTest extends QuickstartsTest {

	public DTGovQuickstartsTest() {
		super("quickstarts/overlord/dtgov");
	}

	@Test
	public void dtGovDemosProjectTest() {
		testQuickstart("dtgov-demos-project");
	}

	@Test
	public void dtGovDemosSwitchYardTest() {
		testQuickstart("dtgov-demos-switchyard");
	}

}
