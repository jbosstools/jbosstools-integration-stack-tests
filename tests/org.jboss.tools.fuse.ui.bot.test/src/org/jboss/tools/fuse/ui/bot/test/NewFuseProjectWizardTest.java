package org.jboss.tools.fuse.ui.bot.test;

import static org.jboss.reddeer.requirements.server.ServerReqState.PRESENT;
import static org.jboss.tools.runtime.reddeer.requirement.ServerReqType.Fuse;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.File;

import org.jboss.reddeer.junit.requirement.inject.InjectRequirement;
import org.jboss.reddeer.junit.runner.RedDeerSuite;
import org.jboss.tools.common.reddeer.LogGrapper;
import org.jboss.tools.common.reddeer.ResourceHelper;
import org.jboss.tools.common.reddeer.ext.ProjectExt;
import org.jboss.tools.fuse.reddeer.wizard.NewFuseIntegrationProjectWizard;
import org.jboss.tools.fuse.ui.bot.test.utils.ProjectFactory;
import org.jboss.tools.runtime.reddeer.impl.ServerFuse;
import org.jboss.tools.runtime.reddeer.requirement.ServerRequirement;
import org.jboss.tools.runtime.reddeer.requirement.ServerRequirement.Server;
import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * Tests wizard for creating a new Fuse Integration Project
 * 
 * @author tsedmik
 */
@RunWith(RedDeerSuite.class)
@Server(type = Fuse, state = PRESENT)
public class NewFuseProjectWizardTest {

	@InjectRequirement
	private ServerRequirement serverRequirement;

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
		assertTrue("There are some errors in Error Log", LogGrapper.getPluginErrors("fuse").size() == 0);
	}

	/**
	 * <p>
	 * Tests 'Target Runtime' option 
	 * </p>
	 * <b>Steps:</b>
	 * <ol>
	 * <li>setup a JBoss Fuse runtime</li>
	 * <li>Invoke <i>File --> New --> Fuse Integration Project</i> wizard</li>
	 * <li>Set project name</li>
	 * <li>Hit 'Next'</li>
	 * <li>Check whether the configured runtime is present in 'Target Runtime' combobox</li>
	 * <li>Select the configured runtime and check whether Camel version is set properly and user cannot change it</li>
	 * <li>Select 'Target Runtime' to 'No Runtime Selected'</li>
	 * <li>Check whether user can change Camel Version</li>
	 * <li>Cancel the wizard</li> 
	 * </ol>
	 */
	@Test
	public void testRuntime() {
		NewFuseIntegrationProjectWizard wiz = new NewFuseIntegrationProjectWizard();
		wiz.open();
		wiz.setProjectName("test");
		wiz.next();
		assertEquals("There is something wrong in 'Target Runtime' Combo box!", 2, wiz.getTargetRuntimes().size());
		for (String temp : wiz.getTargetRuntimes()) {
			if (!(temp.equals("No Runtime selected") || temp.equals(serverRequirement.getConfig().getServerBase().getRuntimeName()))) {
				fail("'Target Runtime' Combo box contains something wrong!");
			}
		}
		wiz.selectTargetRuntime(serverRequirement.getConfig().getServerBase().getRuntimeName());
		assertFalse("Path should not be editable!. The runtime is set.", wiz.isCamelVersionEditable());
		assertEquals("Camel versions are different (runtime vs wizard)!", ((ServerFuse) serverRequirement.getConfig().getServerBase()).getCamelVersion(), wiz.getCamelVersion());
		wiz.cancel();
		assertTrue("There are some errors in Error Log", LogGrapper.getPluginErrors("fuse").size() == 0);
	}

	/**
	 * <p>
	 * Tests 'Camel Version' option
	 * </p>
	 * <b>Steps:</b>
	 * <ol>
	 * <li>Invoke <i>File --> New --> Fuse Integration Project</i> wizard</li>
	 * <li>Set project name</li>
	 * <li>Hit 'Next'</li>
	 * <li>Verify that 'No Runtime Selected' is in 'Target Runtime'</li>
	 * <li>Change the version of Camel to '2.15'</li>
	 * <li>Finish the wizard</li>
	 * <li>Check whether the project has in 'pom.xml' right version of Camel</li>
	 * </ol>
	 */
	@Test
	public void testCamelVersion() {
		fail("Not implemented yet!");
	}

	/**
	 * <p>
	 * Tries to create an empty project.
	 * </p>
	 * <b>Steps:</b>
	 * <ol>
	 * <li>Invoke <i>File --> New --> Fuse Integration Project</i> wizard</li>
	 * <li>Set project name</li>
	 * <li>Hit 'Next'</li>
	 * <li>Hit 'Next'</li>
	 * <li>Check whether the 'Start with an empty project' is selected and 'Blueprint DSL'</li>
	 * <li>Finish the wizard</li>
	 * <li>Check the project - should be created without any problems</li>
	 * <li>Check Error Log - no error from Fuse Tooling should be present</li>
	 * </ol>
	 */
	@Test
	public void testEmptyProject() {
		fail("Not implemented yet!");
	}

	/**
	 * <p>
	 * Tries to create a project from 'Content Based Router'
	 * </p>
	 * <b>Steps:</b>
	 * <ol>
	 * <li>Invoke <i>File --> New --> Fuse Integration Project</i> wizard</li>
	 * <li>Set project name</li>
	 * <li>Hit 'Next'</li>
	 * <li>Hit 'Next'</li>
	 * <li>Select 'Use a predefined template' and 'JBoss Fuse --> Beginner --> Content Based Router</li>
	 * <li>Select 'Blueprint DSL'</li>
	 * <li>Finish the wizard</li>
	 * <li>Check the project - should be created without any problems</li>
	 * <li>Check Error Log - no error from Fuse Tooling should be present</li>
	 * <li></li>
	 * </ol>
	 */
	@Test
	public void testCBRProject() {
		fail("Not implemented yet!");
	}

	/**
	 * <p>
	 * Tries to create an empty project from all available project types (Blueprint, Spring, Java)
	 * </p>
	 * <b>Steps:</b>
	 * <ol>
	 * <li>Invoke <i>File --> New --> Fuse Integration Project</i> wizard</li>
	 * <li>Set project name</li>
	 * <li>Hit 'Next'</li>
	 * <li>Hit 'Next'</li>
	 * <li>Check whether the 'Start with an empty project' is selected</li>
	 * <li>Try to create a project from all available project types (Blueprint, Spring, Java)
	 * <li>Finish the wizard</li>
	 * <li>Check the project - should be created without any problems</li>
	 * <li>Check Error Log - no error from Fuse Tooling should be present</li>
	 * <li>Check whether the project uses selected DSL</li>
	 * <li></li>
	 * </ol>
	 */
	@Test
	public void testProjectTypes() {
		fail("Not implemented yet!");
	}
}
