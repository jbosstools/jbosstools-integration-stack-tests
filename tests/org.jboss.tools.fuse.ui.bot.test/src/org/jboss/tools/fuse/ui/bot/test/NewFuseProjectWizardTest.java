package org.jboss.tools.fuse.ui.bot.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.fail;

import java.io.File;

import org.jboss.reddeer.junit.runner.RedDeerSuite;
import org.jboss.tools.common.reddeer.ResourceHelper;
import org.jboss.tools.common.reddeer.ext.ProjectExt;
import org.jboss.tools.fuse.reddeer.wizard.NewFuseIntegrationProjectWizard;
import org.jboss.tools.fuse.ui.bot.test.utils.ProjectFactory;
import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * Tests wizard for creating a new Fuse Integration Project
 * 
 * @author tsedmik
 */
@RunWith(RedDeerSuite.class)
public class NewFuseProjectWizardTest {

	/**
	 * Prepares test environment
	 */
	@After
	public void setupDeleteProjects() {
		ProjectFactory.deleteAllProjects();
	}

	/**
	 * <p>
	 * Tests non default workspace location of a project
	 * </p>
	 * <b>Steps:</b>
	 * <ol>
	 * <li>Invoke <i>File --> New --> Fuse Integration Project</i> wizard</li>
	 * <li>Set project name</li>
	 * <li>Change project location</li>
	 * <li>Finish wizard</li>
	 * <li>Check whether the project was created in selected location</li>
	 * </ol>
	 */
	@Test
	public void testDifferentWorkspaceLocation() {
		File targetLocation = new File(ResourceHelper.getResourceAbsolutePath(Activator.PLUGIN_ID, "resources/projects") + "/test");
		NewFuseIntegrationProjectWizard wiz = new NewFuseIntegrationProjectWizard();
		wiz.open();
		assertFalse("The path is editable, but 'Use default Workspace location' is selected!", wiz.isPathEditable());
		wiz.setProjectName("test");
		wiz.useDefaultLocation(false);
		wiz.setLocation(targetLocation.getAbsolutePath());
		wiz.finish();
		File actualLocation = new File(new ProjectExt().getLocation("test"));
		assertEquals("Location of a project is different!", targetLocation, actualLocation);
	}

	/**
	 * <p>
	 * 
	 * </p>
	 * <b>Steps:</b>
	 * <ol>
	 * <li></li>
	 * </ol>
	 */
	@Test
	public void testRuntime() {
		fail("Not implemented yet!");
	}

	/**
	 * <p>
	 * 
	 * </p>
	 * <b>Steps:</b>
	 * <ol>
	 * <li></li>
	 * </ol>
	 */
	@Test
	public void testCamelVersion() {
		fail("Not implemented yet!");
	}

	/**
	 * <p>
	 * 
	 * </p>
	 * <b>Steps:</b>
	 * <ol>
	 * <li></li>
	 * </ol>
	 */
	@Test
	public void testEmptyProject() {
		fail("Not implemented yet!");
	}

	/**
	 * <p>
	 * 
	 * </p>
	 * <b>Steps:</b>
	 * <ol>
	 * <li></li>
	 * </ol>
	 */
	@Test
	public void testCBRProject() {
		fail("Not implemented yet!");
	}

	/**
	 * <p>
	 * 
	 * </p>
	 * <b>Steps:</b>
	 * <ol>
	 * <li></li>
	 * </ol>
	 */
	@Test
	public void testProjectTypes() {
		fail("Not implemented yet!");
	}
}
