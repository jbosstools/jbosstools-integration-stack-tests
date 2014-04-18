package org.jboss.tools.esb.ui.bot.test;

import org.jboss.reddeer.eclipse.jdt.ui.ProjectExplorer;
import org.jboss.reddeer.eclipse.jdt.ui.packageexplorer.Project;
import org.jboss.reddeer.eclipse.ui.perspectives.JavaEEPerspective;
import org.jboss.reddeer.junit.logging.Logger;
import org.jboss.reddeer.junit.requirement.inject.InjectRequirement;
import org.jboss.reddeer.junit.runner.RedDeerSuite;
import org.jboss.reddeer.requirements.cleanworkspace.CleanWorkspaceRequirement.CleanWorkspace;
import org.jboss.reddeer.requirements.openperspective.OpenPerspectiveRequirement.OpenPerspective;
import org.jboss.tools.esb.reddeer.editor.ESBEditor;
import org.jboss.tools.esb.reddeer.wizard.ESBProjectWizard;
import org.jboss.tools.esb.ui.bot.test.requirement.ESBRequirement;
import org.jboss.tools.esb.ui.bot.test.requirement.ServerRequirement;
import org.jboss.tools.esb.ui.bot.test.requirement.ESBRequirement.ESB;
import org.jboss.tools.esb.ui.bot.test.requirement.ServerRequirement.Server;
import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * 
 * @author apodhrad
 *
 */
@ESB
@Server
@CleanWorkspace
@OpenPerspective(JavaEEPerspective.class)
@RunWith(RedDeerSuite.class)
public class NewProjectUsingServerTest {

	public Logger log = Logger.getLogger(NewProjectUsingServerTest.class);

	@InjectRequirement
	private ESBRequirement esbRequirement;

	@InjectRequirement
	private ServerRequirement serverRequirement;

	@After
	public void deleteProject() {
		ProjectExplorer projectExplorer = new ProjectExplorer();
		projectExplorer.open();
		for (Project project : projectExplorer.getProjects()) {
			project.delete(true);
		}
	}

	@Test
	public void createProjectUsingServerTest() {
		String project = "esb-project-using-server";

		ESBProjectWizard wizard = new ESBProjectWizard();
		wizard.open();
		wizard.setName(project);
		wizard.setServer(serverRequirement.getName());
		wizard.setVersion(esbRequirement.getVersion());
		wizard.finish();

		new ESBEditor().saveAndClose();

		ProjectExplorer projectExplorer = new ProjectExplorer();
		projectExplorer.open();
		projectExplorer.getProject(project).delete(true);
	}

}
