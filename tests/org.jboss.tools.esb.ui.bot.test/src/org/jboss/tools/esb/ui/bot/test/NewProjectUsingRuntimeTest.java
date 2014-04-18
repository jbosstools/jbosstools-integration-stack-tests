package org.jboss.tools.esb.ui.bot.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

import org.jboss.reddeer.eclipse.jdt.ui.ProjectExplorer;
import org.jboss.reddeer.eclipse.jdt.ui.packageexplorer.Project;
import org.jboss.reddeer.eclipse.ui.perspectives.JavaEEPerspective;
import org.jboss.reddeer.junit.logging.Logger;
import org.jboss.reddeer.junit.requirement.inject.InjectRequirement;
import org.jboss.reddeer.junit.runner.RedDeerSuite;
import org.jboss.reddeer.requirements.cleanworkspace.CleanWorkspaceRequirement.CleanWorkspace;
import org.jboss.reddeer.requirements.openperspective.OpenPerspectiveRequirement.OpenPerspective;
import org.jboss.reddeer.swt.impl.button.PushButton;
import org.jboss.reddeer.swt.impl.button.RadioButton;
import org.jboss.reddeer.swt.impl.combo.DefaultCombo;
import org.jboss.tools.esb.reddeer.wizard.ESBProjectWizard;
import org.jboss.tools.esb.reddeer.wizard.ESBRuntimePreferences;
import org.jboss.tools.esb.reddeer.wizard.ESBRuntimeWizard;
import org.jboss.tools.esb.ui.bot.test.requirement.ESBRequirement;
import org.jboss.tools.esb.ui.bot.test.requirement.ESBRequirement.ESB;
import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * 
 * @author apodhrad
 *
 */
@ESB
@CleanWorkspace
@OpenPerspective(JavaEEPerspective.class)
@RunWith(RedDeerSuite.class)
public class NewProjectUsingRuntimeTest {

	public Logger log = Logger.getLogger(NewProjectUsingRuntimeTest.class);

	@InjectRequirement
	private ESBRequirement esbRequirement;

	@After
	public void deleteProject() {
		ProjectExplorer projectExplorer = new ProjectExplorer();
		projectExplorer.open();
		for (Project project : projectExplorer.getProjects()) {
			project.delete(true);
		}
	}

	@Test
	public void runtimeCreationTest() {
		ESBRuntimePreferences preferences = new ESBRuntimePreferences();
		preferences.open();

		ESBRuntimeWizard wizard = preferences.addESBRuntime();
		assertFalse("Finish button must not be enabled when no home dir is defined",
				new PushButton("Finish").isEnabled());
		wizard.setHomeFolder(esbRequirement.getPath());
		assertEquals("JBDS-1886 - Version was not automatically selected by setting ESB home dir",
				esbRequirement.getVersion(), wizard.getVersion());
		wizard.setName("123_TheName");
		assertEquals("Runtime name cannot start with a number", "Runtime name is not correct", wizard.getInfoText());
		String name = "esb-" + esbRequirement.getVersion();
		wizard.setName(name);
		wizard.finish();

		preferences.ok();

		preferences.open();
		preferences.removeESBRuntime(name);
		preferences.ok();
	}

	@Test
	public void createProjectUsingRuntimeTest() {
		String runtime = "esb-" + esbRequirement.getVersion();
		String project = "esb-project-using-runtime";

		ESBRuntimePreferences preferences = new ESBRuntimePreferences();
		preferences.open();
		preferences.addESBRuntime(esbRequirement.getVersion(), esbRequirement.getPath());
		preferences.ok();

		ESBProjectWizard wizard = new ESBProjectWizard();
		wizard.open();
		wizard.setName(project);
		wizard.setVersion(esbRequirement.getVersion());
		wizard.next();
		wizard.next();
		new RadioButton(1).click();
		assertEquals(runtime, new DefaultCombo().getText());
		wizard.finish();

		ProjectExplorer projectExplorer = new ProjectExplorer();
		projectExplorer.open();
		projectExplorer.getProject(project).delete(true);
	}

}
