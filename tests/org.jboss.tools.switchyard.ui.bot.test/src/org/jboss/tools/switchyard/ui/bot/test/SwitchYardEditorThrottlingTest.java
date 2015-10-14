package org.jboss.tools.switchyard.ui.bot.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.jboss.reddeer.eclipse.jdt.ui.ProjectExplorer;
import org.jboss.reddeer.eclipse.ui.perspectives.JavaEEPerspective;
import org.jboss.reddeer.junit.requirement.inject.InjectRequirement;
import org.jboss.reddeer.junit.runner.RedDeerSuite;
import org.jboss.reddeer.requirements.openperspective.OpenPerspectiveRequirement.OpenPerspective;
import org.jboss.reddeer.swt.impl.shell.DefaultShell;
import org.jboss.tools.switchyard.reddeer.component.Service;
import org.jboss.tools.switchyard.reddeer.editor.SwitchYardEditor;
import org.jboss.tools.switchyard.reddeer.preference.ThrottlingPage;
import org.jboss.tools.switchyard.reddeer.requirement.SwitchYardRequirement;
import org.jboss.tools.switchyard.reddeer.requirement.SwitchYardRequirement.SwitchYard;
import org.jboss.tools.switchyard.reddeer.wizard.DefaultServiceWizard;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * Test for setting throttling
 * 
 * @author apodhrad
 * 
 */
@SwitchYard
@OpenPerspective(JavaEEPerspective.class)
@RunWith(RedDeerSuite.class)
public class SwitchYardEditorThrottlingTest {

	public static final String PROJECT = "throttling_project";
	public static final String SERVICE = "HelloService";

	public static final String ENABLE_THROTTLING = "Enable throttling";
	public static final String MAX_REQUESTS = "Maximum Requests:";
	public static final String TIME_PERIOD = "Time Period:";

	@InjectRequirement
	private SwitchYardRequirement switchyardRequirement;

	@BeforeClass
	public static void createProject() {
		try {
			new SwitchYardEditor().saveAndClose();
		} catch (Exception ex) {
			// it is ok, we just try to close switchyard.xml if it is open
		}

	}

	@Test
	public void throttlingTest() {
		/* Create SY Project */
		switchyardRequirement.project(PROJECT).create();

		/* Add Service */
		new SwitchYardEditor().addService();
		new DefaultShell("New Service");
		new DefaultServiceWizard().createJavaInterface("Hello");
		new DefaultShell("New Service");
		new DefaultServiceWizard().setServiceName("HelloService").finish();
		new SwitchYardEditor().save();

		/* Set Throttling */
		ThrottlingPage throttlingPage = new Service("HelloService").showProperties().selectThrottling();
		throttlingPage.setThrottlingEnable(true);
		assertTrue(throttlingPage.isMaximumRequestsEnabled());
		assertTrue(throttlingPage.isTimePeriodEnabled());
		assertFalse(throttlingPage.isMaximumRequestsReadOnly());
		assertFalse(throttlingPage.isTimePeriodReadOnly());
		throttlingPage.setMaximumRequests("3");
		throttlingPage.setTimePeriod("10000");
		throttlingPage.ok();
		new SwitchYardEditor().save();

		/* Check settings */
		throttlingPage = new Service("HelloService").showProperties().selectThrottling();
		assertTrue(throttlingPage.isThrottlingEnableChecked());
		assertEquals("3", throttlingPage.getMaximumRequests());
		assertEquals("10000", throttlingPage.getTimePeriod());
		throttlingPage.ok();

		/* Disable Throttling */
		throttlingPage = new Service("HelloService").showProperties().selectThrottling();
		throttlingPage.setThrottlingEnable(false);
		assertFalse(throttlingPage.isMaximumRequestsEnabled());
		assertFalse(throttlingPage.isTimePeriodEnabled());
		throttlingPage.ok();
		new SwitchYardEditor().save();

		/* Check settings */
		throttlingPage = new Service("HelloService").showProperties().selectThrottling();
		assertFalse(throttlingPage.isThrottlingEnableChecked());
		assertFalse(throttlingPage.isMaximumRequestsEnabled());
		assertFalse(throttlingPage.isTimePeriodEnabled());
		throttlingPage.ok();
	}

	@AfterClass
	public static void deleteProject() {
		new SwitchYardEditor().saveAndClose();
		ProjectExplorer projectExplorer = new ProjectExplorer();
		projectExplorer.open();
		projectExplorer.deleteAllProjects();
	}

}
