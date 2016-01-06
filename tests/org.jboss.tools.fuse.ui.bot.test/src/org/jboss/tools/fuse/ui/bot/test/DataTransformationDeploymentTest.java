package org.jboss.tools.fuse.ui.bot.test;

import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;
import static org.jboss.reddeer.requirements.server.ServerReqState.PRESENT;
import static org.jboss.tools.runtime.reddeer.requirement.ServerReqType.Fuse;
import static org.junit.Assert.fail;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

import org.jboss.reddeer.common.exception.WaitTimeoutExpiredException;
import org.jboss.reddeer.common.wait.WaitUntil;
import org.jboss.reddeer.junit.execution.annotation.RunIf;
import org.jboss.reddeer.junit.requirement.inject.InjectRequirement;
import org.jboss.reddeer.junit.runner.RedDeerSuite;
import org.jboss.tools.common.reddeer.condition.IssueIsClosed;
import org.jboss.tools.common.reddeer.condition.IssueIsClosed.Jira;
import org.jboss.tools.fuse.reddeer.condition.FuseLogContainsText;
import org.jboss.tools.fuse.reddeer.projectexplorer.CamelProject;
import org.jboss.tools.fuse.reddeer.server.ServerManipulator;
import org.jboss.tools.fuse.reddeer.utils.ResourceHelper;
import org.jboss.tools.fuse.reddeer.view.FuseShell;
import org.jboss.tools.fuse.ui.bot.test.utils.ProjectFactory;
import org.jboss.tools.runtime.reddeer.requirement.ServerRequirement;
import org.jboss.tools.runtime.reddeer.requirement.ServerRequirement.Server;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * Test covers Data Transformation Tooling in JBoss Fuse Runtime perspective
 * 
 * @author tsedmik
 */
@RunWith(RedDeerSuite.class)
@Server(type = Fuse, state = PRESENT)
public class DataTransformationDeploymentTest extends DefaultTest {

	@InjectRequirement
	private static ServerRequirement serverRequirement;

	/**
	 * Prepares test environment
	 */
	@BeforeClass
	public static void setupStartServer() {
		ServerManipulator.startServer(serverRequirement.getConfig().getName());
	}

	/**
	 * Cleans up test environment
	 */
	@AfterClass
	public static void setupStopServer() {
		ServerManipulator.removeAllModules(serverRequirement.getConfig().getName());
		ServerManipulator.stopServer(serverRequirement.getConfig().getName());
	}

	/**
	 * <p>
	 * Test tries to deploy a Fuse project with defined Data Transformation to JBoss Fuse runtime
	 * </p>
	 * <b>Steps</b>
	 * <ol>
	 * <li>start JBoss Fuse</li>
	 * <li>import 'XML-to-JSON' project from 'resources/projects/XML-to-JSON'</li>
	 * <li>enable Fuse Camel Nature on the project (has to be done to ensure that project can be deployed to JBoss Fuse
	 * Runtime)</li>
	 * <li>deploy the project</li>
	 * <li>invoke the route with copying a file</li>
	 * <li>check log of JBoss Fuse (deployed project should log transformed XML file in JSON format)</li>
	 * </ol>
	 */
	@Test
	@Jira("ENTESB-4452")
	@RunIf(conditionClass = IssueIsClosed.class)
	public void testDeployment() {

		ProjectFactory.importExistingProject(
				ResourceHelper.getResourceAbsolutePath(Activator.PLUGIN_ID, "resources/projects/XML-to-JSON"),
				"XML-to-JSON", true, true);
		CamelProject project = new CamelProject("XML-to-JSON");
		project.close();
		project.open();
		project.enableCamelNature();
		ServerManipulator.addModule(serverRequirement.getConfig().getName(), "XML-to-JSON");

		// invoke the route with copying a file
		String from = ResourceHelper.getResourceAbsolutePath(Activator.PLUGIN_ID,
				"resources/projects/XML-to-JSON/src/main/resources/data/abc-order.xml");
		String to = serverRequirement.getConfig().getServerBase().getHome() + "/src/main/resources/data/abc-order.xml";
		try {
			Files.copy(new File(from).toPath(), new File(to).toPath(), REPLACE_EXISTING);
		} catch (IOException e) {
			e.printStackTrace();
			fail("Tests cannot copy XML file to home folder of JBoss Fuse Runtime!");
		}
		try {
			new WaitUntil(new FuseLogContainsText(
					"{\"custId\":\"ACME-123\",\"priority\":\"GOLD\",\"orderId\":\"[ORDER1]\",\"origin\":\"web\",\"approvalCode\":\"AUTO_OK\",\"lineItems\":[{\"itemId\":\"PICKLE\",\"amount\":1000,\"cost\":2.25},{\"itemId\":\"BANANA\",\"amount\":400,\"cost\":1.25}]}"));
		} catch (WaitTimeoutExpiredException e) {
			fail("Transformation is broken! \n\n" + new FuseShell().execute("log:display"));
		}
	}
}
