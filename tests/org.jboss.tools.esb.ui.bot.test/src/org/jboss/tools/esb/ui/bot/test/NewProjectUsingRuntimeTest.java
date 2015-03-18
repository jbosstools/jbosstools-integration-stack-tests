package org.jboss.tools.esb.ui.bot.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

import org.jboss.reddeer.eclipse.jdt.ui.ProjectExplorer;
import org.jboss.reddeer.eclipse.core.resources.Project;
import org.jboss.reddeer.eclipse.ui.perspectives.JavaEEPerspective;
import org.jboss.reddeer.common.logging.Logger;
import org.jboss.reddeer.junit.requirement.inject.InjectRequirement;
import org.jboss.reddeer.junit.runner.RedDeerSuite;
import org.jboss.reddeer.requirements.cleanworkspace.CleanWorkspaceRequirement.CleanWorkspace;
import org.jboss.reddeer.requirements.openperspective.OpenPerspectiveRequirement.OpenPerspective;
import org.jboss.reddeer.swt.impl.button.PushButton;
import org.jboss.reddeer.swt.impl.button.RadioButton;
import org.jboss.reddeer.swt.impl.combo.DefaultCombo;
import org.jboss.tools.esb.reddeer.wizard.ESBProjectWizard;
import org.jboss.tools.runtime.reddeer.requirement.RuntimeReqType;
import org.jboss.tools.runtime.reddeer.requirement.RuntimeRequirement;
import org.jboss.tools.runtime.reddeer.requirement.RuntimeRequirement.Runtime;
import org.jboss.tools.runtime.reddeer.wizard.ESBRuntimeWizard;
import org.jboss.tools.runtime.reddeer.wizard.ESBRuntimePreferencePage;
import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * 
 * @author apodhrad
 *
 */
@Runtime(type = RuntimeReqType.ESB)
@CleanWorkspace
@OpenPerspective(JavaEEPerspective.class)
@RunWith(RedDeerSuite.class)
public class NewProjectUsingRuntimeTest {

	public Logger log = Logger.getLogger(NewProjectUsingRuntimeTest.class);

	@InjectRequirement
	private RuntimeRequirement esbRequirement;

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
		ESBRuntimePreferencePage preferences = new ESBRuntimePreferencePage();
		preferences.open();

		ESBRuntimeWizard wizard = preferences.addESBRuntime();
		assertFalse("Finish button must not be enabled when no home dir is defined",
				new PushButton("Finish").isEnabled());
		wizard.setHomeFolder(esbRequirement.getConfig().getRuntimeFamily().getHome());
		assertEquals("JBDS-1886 - Version was not automatically selected by setting ESB home dir",
				esbRequirement.getConfig().getRuntimeFamily().getVersion(), wizard.getVersion());
		wizard.setName("123_TheName");
		assertEquals("Runtime name cannot start with a number", "Runtime name is not correct", wizard.getInfoText());
		String name = "test-esb-" + esbRequirement.getConfig().getRuntimeFamily().getVersion();
		wizard.setName(name);
		wizard.finish();

		preferences.ok();

		preferences.open();
		preferences.removeESBRuntime(name);
		preferences.ok();
	}

	@Test
	public void createProjectUsingRuntimeTest() {
		String runtime = esbRequirement.getConfig().getName();
		String project = "esb-project-using-runtime";

		ESBProjectWizard wizard = new ESBProjectWizard();
		wizard.open();
		wizard.setName(project);
		wizard.setVersion(esbRequirement.getConfig().getRuntimeFamily().getVersion());
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
