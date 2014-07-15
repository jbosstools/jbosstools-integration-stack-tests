package org.jboss.tools.fuse.ui.bot.test;

import static org.junit.Assert.assertEquals;

import org.jboss.reddeer.eclipse.condition.ConsoleHasText;
import org.jboss.reddeer.eclipse.jdt.ui.ProjectExplorer;
import org.jboss.reddeer.swt.impl.menu.ContextMenu;
import org.jboss.reddeer.swt.test.RedDeerTest;
import org.jboss.reddeer.swt.wait.TimePeriod;
import org.jboss.reddeer.swt.wait.WaitUntil;
import org.jboss.tools.fuse.reddeer.view.FabricExplorer;
import org.jboss.tools.fuse.ui.bot.test.utils.ProjectFactory;
import org.junit.Test;

/**
 * Tests Fabric in the cloud. Must be run with properly set following system
 * properties:
 * <ul>
 * <li>EC2id - Amazon EC2 Identity</li>
 * <li>EC2password - Amazon EC2 password</li>
 * <li>EC2email - Amazon EC2 owner</li>
 * </ul>
 * 
 * <b>Note:</b> Run this test only from maven:
 * <tt>cd jbosstools-integration-stack-tests</tt>
 * <tt>DISPLAY=:2 mvn verify -pl tests/org.jboss.tools.fuse.ui.bot.test -am
 * 		-Dreddeer.config=$SERVER_CONFIG_XML
 * 		-Dtest=FabricInCloudTest
 * 		-Dec2.id=$EC2id
 * 		-Dec2.pass=$EC2password
 * 		-Dec2.email=$EC2email
 * 		-Dmaven.test.failure.ignore=true</tt>
 * 
 * @author tsedmik
 */
public class FabricInCloudTest extends RedDeerTest {

	private static final String PROJECT_ARCHETYPE = "camel-archetype-spring";
	private static final String PROJECT_NAME = "camel-spring";
	private static final String PROJECT_FABS = "mvn:com.mycompany/camel-spring/1.0.0-SNAPSHOT";

	private static FabricExplorer fab = new FabricExplorer();

	@Test
	public void testCloudFabric() {

		// create a Fabric in the cloud
		fab.open();
		fab.createCloudDetail("Amazon", System.getProperty("ec2.id"), System.getProperty("ec2.pass"), System.getProperty("ec2.email"));
		fab.selectNode("Clouds", "Amazon");
		fab.createFabricInTheCloud("Amazon", "Cloud Fabric", "admin", "admin", "eu-west-1", "t1.micro", "rhel", "6");
		fab.selectNode("Fabrics", "Cloud Fabric");
		fab.connectToFabric("Cloud Fabric");
		fab.selectNode("Fabrics", "Cloud Fabric", "Versions", "1.0", "default");

		// Deploy a project to the fabric in the cloud
		ProjectFactory.createProject(PROJECT_ARCHETYPE);
		ProjectExplorer projectExplorer = new ProjectExplorer();
		projectExplorer.open();
		projectExplorer.selectProjects(PROJECT_NAME);
		new ContextMenu("Deploy to...", "Cloud Fabric", "1.0", "autoscale").select();
		new WaitUntil(new ConsoleHasText("BUILD SUCCESS"), TimePeriod.getCustom(300));
		assertEquals(PROJECT_FABS, fab.getProfileFABs("Fabrics", "Cloud Fabric", "Versions", "1.0", "default", "autoscale"));
	}
}
