package org.jboss.tools.fuse.ui.bot.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.jboss.reddeer.junit.requirement.inject.InjectRequirement;
import org.jboss.reddeer.swt.exception.SWTLayerException;
import org.jboss.reddeer.swt.impl.button.PushButton;
import org.jboss.reddeer.swt.impl.shell.DefaultShell;
import org.jboss.reddeer.swt.impl.text.LabeledText;
import org.jboss.reddeer.swt.impl.toolbar.DefaultToolItem;
import org.jboss.reddeer.swt.test.RedDeerTest;
import org.jboss.reddeer.swt.wait.AbstractWait;
import org.jboss.reddeer.swt.wait.TimePeriod;
import org.jboss.tools.fuse.reddeer.server.ServerManipulator;
import org.jboss.tools.fuse.reddeer.view.FabricExplorer;
import org.jboss.tools.fuse.reddeer.view.FuseContainerProperties;
import org.jboss.tools.fuse.reddeer.view.FuseShell;
import org.jboss.tools.fuse.ui.bot.test.requirement.ServerRequirement;
import org.jboss.tools.fuse.ui.bot.test.requirement.ServerRequirement.Server;
import org.jboss.tools.fuse.ui.bot.test.utils.ServerConfig;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.Test;

/**
 * Tests <i>Fabric Explorer</i> view.
 * 
 * @author tsedmik
 */
@Server
public class FabricExplorerTest extends RedDeerTest {

	@InjectRequirement
	private static ServerRequirement serverRequirement;

	private static boolean setUpIsDone = false;
	private static FabricExplorer fab = new FabricExplorer();

	@Before
	public void setUp() {

		if (setUpIsDone) {
			return;
		}

		new ServerConfig(serverRequirement).configureNewServer();
		ServerManipulator.startServer(serverRequirement.getName());

		new FuseShell().createFabric();

		fab.open();
		fab.addFabricDetails(null, null, "admin", "admin", "admin");
		fab.connectToFabric(null);

		setUpIsDone = true;
	}

	@AfterClass
	public static void cleanUp() {

		ServerManipulator.stopServer(serverRequirement.getName());
	}

	@Test
	public void testFabricCreation() {

		fab.open();
		new DefaultToolItem("Adds a new Fabric details").click();
		new DefaultShell().setFocus();
		PushButton ok = new PushButton("OK");

		assertFalse(ok.isEnabled());
		assertTrue(new PushButton("Cancel").isEnabled());
		assertNotNull(new LabeledText("Name").getText());
		assertNotNull(new LabeledText("Jolokia URL").getText());

		new LabeledText("User name").typeText("admin");
		assertFalse(ok.isEnabled());

		new LabeledText("Password").typeText("admin");
		assertFalse(ok.isEnabled());

		new LabeledText("ZooKeeper Password").typeText("admin");
		assertTrue(ok.isEnabled());

		new PushButton("Cancel").click();
		new DefaultShell().setFocus();
	}

	@Test
	public void testProfilesManipulation() {

		fab.open();
		fab.createProfile("test", "1.0", "default");
		fab.selectNode("Fabrics", "Local Fabric", "Versions", "1.0", "default", "test");
		fab.deleteProfile();
		try {
			fab.selectNode("Fabrics", "Local Fabric", "Versions", "1.0", "default", "test");
			fail("Profile 'test' was deleted. It should not be available in the Fabric Explorer view!");
		} catch (SWTLayerException ex) {
		}
	}

	@Test
	public void testVersionsManipulation() {

		fab.open();
		fab.createVersion("2.0");
		fab.selectNode("Fabrics", "Local Fabric", "Versions", "2.0");
		fab.selectNode("Fabrics", "Local Fabric", "Containers", "root");
		fab.selectContextMenuItem("Set Version", "2.0");
		AbstractWait.sleep(TimePeriod.getCustom(5));
		try {
			fab.selectContextMenuItem("Set Version", "2.0");
			fail("Context menu item 'Set Version -> 2.0' should be disabled!");
		} catch (SWTLayerException ex) {
		}
	}

	@Test
	public void testContainersManipulation() {

		// Creation of container
		fab.open();
		fab.createContainer("testContainer", "1.0", "autoscale");
		fab.selectNode("Fabrics", "Local Fabric", "Containers", "testContainer");

		FuseContainerProperties prop = new FuseContainerProperties();
		prop.open();

		fab.open();
		fab.selectNode("Fabrics", "Local Fabric", "Containers");

		prop.open();
		prop.refresh();
		assertTrue(prop.isContainerPresent("testContainer"));
		assertEquals("success", prop.getStatus("testContainer"));

		// Manipulation with the 'root' container
		prop.selectItem("root");
		assertFalse(prop.isStartContainerEnabled());
		assertFalse(prop.isStopContainerEnabled());
		assertFalse(prop.isDestroyContainerEnabled());

		// Start/Stop/Destroy a container via Properties View
		prop.selectItem("testContainer");
		prop.stopContainer();
		assertEquals("stopped", prop.getStatus("testContainer"));

		prop.selectItem("testContainer");
		prop.startContainer();
		assertEquals("success", prop.getStatus("testContainer"));

		prop.selectItem("testContainer");
		prop.destroyContainer();
		assertFalse(prop.isContainerPresent("testContainer"));

		// Start/Stop a container via Fabric Explorer View
		fab.open();
		fab.createContainer("testContainer", "1.0", "autoscale");
		fab.selectNode("Fabrics", "Local Fabric", "Containers", "testContainer");
		fab.stopContainer("testContainer");
		try {
			fab.stopContainer("testContainer");
			fail("Context menu should not contains 'Stop Container'! The container is already stopped");
		} catch (SWTLayerException ex) {
		}
		fab.selectNode("Fabrics", "Local Fabric", "Containers");
		prop.open();
		assertEquals("stopped", prop.getStatus("testContainer"));

		fab.open();
		fab.startContainer("testContainer");
		try {
			fab.startContainer("testContainer");
			fail("Context menu should not contains 'Start Container'! The container is already started");
		} catch (SWTLayerException ex) {
		}
		fab.selectNode("Fabrics", "Local Fabric", "Containers");
		prop.open();
		assertEquals("success", prop.getStatus("testContainer"));
	}

	@Test
	public void testCloudFabric() {

		// TODO Not implemented yet!
		fail("Not implemented yet!");
	}
}
