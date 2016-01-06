package org.jboss.tools.switchyard.ui.bot.test;

import org.jboss.reddeer.eclipse.ui.perspectives.JavaEEPerspective;
import org.jboss.reddeer.junit.runner.RedDeerSuite;
import org.jboss.reddeer.requirements.openperspective.OpenPerspectiveRequirement.OpenPerspective;
import org.jboss.reddeer.requirements.server.ServerReqState;
import org.jboss.tools.runtime.reddeer.requirement.ServerReqType;
import org.jboss.tools.runtime.reddeer.requirement.ServerRequirement.Server;
import org.jboss.tools.switchyard.reddeer.requirement.SwitchYardRequirement.SwitchYard;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * Test importing camel quickstarts
 * 
 * @author apodhrad
 * 
 */
@SwitchYard(server = @Server(type = ServerReqType.ANY, state = ServerReqState.PRESENT) )
@OpenPerspective(JavaEEPerspective.class)
@RunWith(RedDeerSuite.class)
public class QuickstartsCamelTest extends QuickstartsTest {

	public QuickstartsCamelTest() {
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
