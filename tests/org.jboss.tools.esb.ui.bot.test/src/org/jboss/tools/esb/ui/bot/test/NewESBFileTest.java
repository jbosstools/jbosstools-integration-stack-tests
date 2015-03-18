package org.jboss.tools.esb.ui.bot.test;

import static org.junit.Assert.assertTrue;

import org.jboss.reddeer.eclipse.jdt.ui.ProjectExplorer;
import org.jboss.reddeer.eclipse.core.resources.Project;
import org.jboss.reddeer.eclipse.ui.perspectives.JavaEEPerspective;
import org.jboss.reddeer.eclipse.ui.problems.ProblemsView;
import org.jboss.reddeer.common.logging.Logger;
import org.jboss.reddeer.junit.runner.RedDeerSuite;
import org.jboss.reddeer.requirements.cleanworkspace.CleanWorkspaceRequirement.CleanWorkspace;
import org.jboss.reddeer.requirements.openperspective.OpenPerspectiveRequirement.OpenPerspective;
import org.jboss.reddeer.swt.impl.shell.WorkbenchShell;
import org.jboss.tools.esb.reddeer.wizard.ESBFileWizard;
import org.jboss.tools.esb.reddeer.wizard.ESBProjectWizard;
import org.junit.After;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * 
 * @author apodhrad
 *
 */
@CleanWorkspace
@OpenPerspective(JavaEEPerspective.class)
@RunWith(RedDeerSuite.class)
public class NewESBFileTest {

	public static final String PROJECT_NAME = "esb-file";
	public static final String ESB_FILE = "jboss-esb.xml";
	public static final String NAME_SUFFIX = "-name";

	public Logger log = Logger.getLogger(NewESBFileTest.class);

	@BeforeClass
	public static void createProject() {
		new WorkbenchShell().maximize();
	}

	@After
	public void deleteProject() {
		new ProjectExplorer().getProject(PROJECT_NAME).delete(true);
		ProjectExplorer projectExplorer = new ProjectExplorer();
		projectExplorer.open();
		for (Project project : projectExplorer.getProjects()) {
			project.delete(true);
		}
	}

	@Test
	public void createEsbFileTest() {
		new ESBProjectWizard().openWizard().setName(PROJECT_NAME).finish();

		new ProjectExplorer().getProject(PROJECT_NAME).select();
		new ESBFileWizard().openWizard().setName("another-esb-config").finish();

		ProblemsView problemsView = new ProblemsView();
		problemsView.open();
		assertTrue("ESB Editor opened problems", problemsView.getAllErrors().isEmpty());
	}

}
