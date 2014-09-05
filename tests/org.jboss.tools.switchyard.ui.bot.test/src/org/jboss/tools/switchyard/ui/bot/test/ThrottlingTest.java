package org.jboss.tools.switchyard.ui.bot.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.jboss.reddeer.eclipse.jdt.ui.ProjectExplorer;
import org.jboss.reddeer.eclipse.ui.perspectives.JavaEEPerspective;
import org.jboss.reddeer.junit.requirement.inject.InjectRequirement;
import org.jboss.reddeer.junit.runner.RedDeerSuite;
import org.jboss.reddeer.requirements.openperspective.OpenPerspectiveRequirement.OpenPerspective;
import org.jboss.reddeer.swt.handler.WidgetHandler;
import org.jboss.reddeer.swt.impl.button.CheckBox;
import org.jboss.reddeer.swt.impl.shell.DefaultShell;
import org.jboss.reddeer.swt.impl.text.LabeledText;
import org.jboss.reddeer.swt.util.Display;
import org.jboss.reddeer.swt.util.ResultRunnable;
import org.jboss.tools.switchyard.reddeer.component.Service;
import org.jboss.tools.switchyard.reddeer.editor.SwitchYardEditor;
import org.jboss.tools.switchyard.reddeer.preference.PropertiesPreferencePage;
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
public class ThrottlingTest {

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
		PropertiesPreferencePage propsPage = new Service("HelloService").showProperties();
		propsPage.selectTab("Throttling");
		new CheckBox(ENABLE_THROTTLING).toggle(true);
		assertTrue(new ExtendedLabeledText(MAX_REQUESTS).isEnabled());
		assertTrue(new ExtendedLabeledText(TIME_PERIOD).isEnabled());
		assertTrue(new ExtendedLabeledText(MAX_REQUESTS).isEditable());
		assertTrue(new ExtendedLabeledText(TIME_PERIOD).isEditable());
		new LabeledText(MAX_REQUESTS).setText("3");
		new LabeledText(TIME_PERIOD).setText("10000");
		propsPage.ok();
		new SwitchYardEditor().save();

		/* Check settings */
		propsPage = new Service("HelloService").showProperties();
		propsPage.selectTab("Throttling");
		assertTrue(new CheckBox(ENABLE_THROTTLING).isChecked());
		assertEquals("3", new LabeledText(MAX_REQUESTS).getText());
		assertEquals("10000", new LabeledText(TIME_PERIOD).getText());
		propsPage.ok();

		/* Disable Throttling */
		propsPage = new Service("HelloService").showProperties();
		propsPage.selectTab("Throttling");
		new CheckBox(ENABLE_THROTTLING).toggle(false);
		assertFalse(new ExtendedLabeledText(MAX_REQUESTS).isEnabled());
		assertFalse(new ExtendedLabeledText(TIME_PERIOD).isEnabled());
		propsPage.ok();
		new SwitchYardEditor().save();

		/* Check settings */
		propsPage = new Service("HelloService").showProperties();
		propsPage.selectTab("Throttling");
		assertFalse(new CheckBox(ENABLE_THROTTLING).isChecked());
		assertFalse(new ExtendedLabeledText(MAX_REQUESTS).isEnabled());
		assertFalse(new ExtendedLabeledText(TIME_PERIOD).isEnabled());
		propsPage.ok();
	}

	@AfterClass
	public static void deleteProject() {
		new SwitchYardEditor().saveAndClose();
		ProjectExplorer projectExplorer = new ProjectExplorer();
		projectExplorer.open();
		projectExplorer.deleteAllProjects();
	}

	public class ExtendedLabeledText extends LabeledText {

		public ExtendedLabeledText(String label) {
			super(label);
		}

		public boolean isEnabled() {
			return WidgetHandler.getInstance().isEnabled(w);
		}

		public boolean isEditable() {
			return Display.syncExec(new ResultRunnable<Boolean>() {

				@Override
				public Boolean run() {
					return w.getEditable();
				}

			});
		}
	}

}
