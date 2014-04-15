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
 * Test importing camel quickstarts
 * 
 * @author apodhrad
 * 
 */
@CleanWorkspace
@Perspective(name = "Java EE")
@Server(type = Type.ALL, state = State.PRESENT)
@RunWith(SwitchyardSuite.class)
public class CamelQuickstartsTest extends QuickstartsTest {

	public CamelQuickstartsTest() {
		super("quickstarts/camel");
	}

	@Test
	public void camelExampleCxfTomcatTest() {
		testQuickstart("camel-example-cxf-tomcat");
	}

	@Test
	public void camelExampleServletTomcatTest() {
		testQuickstart("camel-example-servlet-tomcat");
	}

}
