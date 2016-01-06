package org.jboss.tools.fuse.ui.bot.test;

import org.jboss.reddeer.junit.runner.RedDeerSuite;
import org.jboss.reddeer.requirements.cleanworkspace.CleanWorkspaceRequirement.CleanWorkspace;
import org.jboss.reddeer.requirements.openperspective.OpenPerspectiveRequirement.OpenPerspective;
import org.jboss.tools.fuse.reddeer.perspectives.Fabric8Perspective;
import org.jboss.tools.fuse.reddeer.view.Fabric8Explorer;
import org.jboss.tools.fuse.ui.bot.test.utils.FuseArchetypeNotFoundException;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * Tests Fabric in the cloud. Must be run with properly set following system properties:
 * <ul>
 * <li>EC2id - Amazon EC2 Identity</li>
 * <li>EC2password - Amazon EC2 password</li>
 * <li>EC2email - Amazon EC2 owner</li>
 * </ul>
 * 
 * <b>Note:</b> Run this test only from maven: <tt>cd jbosstools-integration-stack-tests</tt>
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
@CleanWorkspace
@OpenPerspective(Fabric8Perspective.class)
@RunWith(RedDeerSuite.class)
public class FabricInCloudTest {

	private static Fabric8Explorer fab = new Fabric8Explorer();

	/**
	 * <p>
	 * Tests fabric in the cloud.
	 * </p>
	 * <b>Steps</b>
	 * <ol>
	 * <li>create a new cloud detail</li>
	 * <li>create a new fabric in the cloud</li>
	 * <li>try to connect to the fabric</li>
	 * <li>try to go through the fabric</li>
	 * </ol>
	 */
	@Test
	public void testCloudFabric() {

		// create a Fabric in the cloud
		fab.open();
		fab.createCloudDetail("Amazon", System.getProperty("ec2.id"), System.getProperty("ec2.pass"),
				System.getProperty("ec2.email"));
		fab.selectNode("Clouds", "Amazon");
		fab.createFabricInTheCloud("Amazon", "Cloud Fabric", "admin", "admin", "eu-west-1", "t1.micro", "rhel", "6");
		fab.selectNode("Fabrics", "Cloud Fabric");
		fab.connectToFabric("Cloud Fabric");
		fab.selectNode("Fabrics", "Cloud Fabric", "Versions", "1.0", "default");
	}
}