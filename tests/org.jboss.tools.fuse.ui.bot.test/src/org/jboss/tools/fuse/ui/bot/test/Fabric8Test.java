package org.jboss.tools.fuse.ui.bot.test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.jboss.reddeer.junit.requirement.inject.InjectRequirement;
import org.jboss.reddeer.junit.runner.RedDeerSuite;
import org.jboss.reddeer.requirements.cleanworkspace.CleanWorkspaceRequirement.CleanWorkspace;
import org.jboss.reddeer.requirements.openperspective.OpenPerspectiveRequirement.OpenPerspective;
import org.jboss.reddeer.requirements.server.ServerReqState;
import org.jboss.reddeer.swt.exception.SWTLayerException;
import org.jboss.reddeer.swt.impl.button.PushButton;
import org.jboss.reddeer.swt.impl.menu.ContextMenu;
import org.jboss.reddeer.swt.impl.shell.DefaultShell;
import org.jboss.reddeer.swt.impl.shell.WorkbenchShell;
import org.jboss.reddeer.swt.impl.text.LabeledText;
import org.jboss.reddeer.swt.impl.tree.DefaultTreeItem;
import org.jboss.reddeer.swt.wait.AbstractWait;
import org.jboss.reddeer.swt.wait.TimePeriod;
import org.jboss.tools.fuse.reddeer.perspectives.Fabric8Perspective;
import org.jboss.tools.fuse.reddeer.server.ServerManipulator;
import org.jboss.tools.fuse.reddeer.view.Fabric8Explorer;
import org.jboss.tools.fuse.reddeer.view.FuseContainerProperties;
import org.jboss.tools.fuse.reddeer.view.FuseShell;
import org.jboss.tools.runtime.reddeer.requirement.ServerReqType;
import org.jboss.tools.runtime.reddeer.requirement.ServerRequirement;
import org.jboss.tools.runtime.reddeer.requirement.ServerRequirement.Server;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * Tests <i>Fabric8 Explorer</i> view.
 * 
 * @author tsedmik
 */
@Server(type = ServerReqType.Fuse, state = ServerReqState.RUNNING)
@CleanWorkspace
@OpenPerspective(Fabric8Perspective.class)
@RunWith(RedDeerSuite.class)
public class Fabric8Test {

	@InjectRequirement
	private static ServerRequirement server;

	@BeforeClass
	public static void createFabric() {

		new FuseShell().createFabric();
		Fabric8Explorer fab = new Fabric8Explorer();
		fab.open();
		fab.addFabricDetails(null, null, "admin", "admin", "admin");
		fab.connectToFabric(null);
		new WorkbenchShell().setFocus();
	}

	@AfterClass
	public static void cleanUp() {

		// Server
		String serverName = server.getConfig().getName();
		ServerManipulator.stopServer(serverName);
		ServerManipulator.removeServer(serverName);
		ServerManipulator.removeServerRuntime(serverName + " Runtime");

		// Fabric
		Fabric8Explorer fab = new Fabric8Explorer();
		fab.open();
		fab.removeFabric(null);
	}
	
	@Test
	public void testFabricCreation() {
		
		Fabric8Explorer fab = new Fabric8Explorer();
		fab.open();
		new DefaultTreeItem("Fabrics").select();
		new ContextMenu("Add Fabric details").select();		
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

	public void testProfilesManipulation() {

		Fabric8Explorer fab = new Fabric8Explorer();
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

		Fabric8Explorer fab = new Fabric8Explorer();
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

		Fabric8Explorer fab = new Fabric8Explorer();

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
		assertTrue(new FuseShell().execute("fabric:container-list").contains("testContainer  1.0        karaf   yes          autoscale               success"));

		// Manipulation with the 'root' container
		prop.activate();
		prop.selectItem("root");
		assertFalse(prop.isStartContainerEnabled());
		assertFalse(prop.isStopContainerEnabled());
		assertFalse(prop.isDestroyContainerEnabled());

		// Start/Stop/Destroy a container via Properties View
		prop.activate();
		prop.selectItem("testContainer");
		prop.stopContainer();
		assertTrue(new FuseShell().execute("fabric:container-list").contains("testContainer  1.0        karaf   no           autoscale               stopped"));

		prop.activate();
		prop.selectItem("testContainer");
		prop.startContainer();
		assertTrue(new FuseShell().execute("fabric:container-list").contains("testContainer  1.0        karaf   yes          autoscale               success"));

		prop.activate();
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
		assertTrue(new FuseShell().execute("fabric:container-list").contains("testContainer  1.0        karaf   no           autoscale               stopped"));

		fab.open();
		fab.startContainer("testContainer");
		try {
			fab.startContainer("testContainer");
			fail("Context menu should not contains 'Start Container'! The container is already started");
		} catch (SWTLayerException ex) {
		}
		fab.selectNode("Fabrics", "Local Fabric", "Containers");
		prop.open();
		assertTrue(new FuseShell().execute("fabric:container-list").contains("testContainer  1.0        karaf   yes          autoscale               success"));
	}
}
