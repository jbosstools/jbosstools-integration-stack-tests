package org.jboss.tools.switchyard.ui.bot.test;

import static org.junit.Assert.assertTrue;

import java.io.File;
import java.util.List;

import org.eclipse.swtbot.eclipse.finder.SWTWorkbenchBot;
import org.jboss.reddeer.eclipse.jdt.ui.ProjectExplorer;
import org.jboss.reddeer.eclipse.jdt.ui.packageexplorer.Project;
import org.jboss.reddeer.eclipse.ui.perspectives.JavaEEPerspective;
import org.jboss.reddeer.eclipse.ui.problems.ProblemsView;
import org.jboss.reddeer.requirements.cleanworkspace.CleanWorkspaceRequirement.CleanWorkspace;
import org.jboss.reddeer.requirements.openperspective.OpenPerspectiveRequirement.OpenPerspective;
import org.jboss.reddeer.swt.api.TreeItem;
import org.jboss.reddeer.swt.condition.JobIsRunning;
import org.jboss.reddeer.swt.test.RedDeerTest;
import org.jboss.reddeer.swt.wait.AbstractWait;
import org.jboss.reddeer.swt.wait.TimePeriod;
import org.jboss.reddeer.swt.wait.WaitWhile;
import org.jboss.tools.switchyard.reddeer.wizard.ImportMavenWizard;
import org.jboss.tools.switchyard.ui.bot.test.suite.ServerRequirement.Server;
import org.jboss.tools.switchyard.ui.bot.test.suite.ServerRequirement.State;
import org.jboss.tools.switchyard.ui.bot.test.suite.ServerRequirement.Type;
import org.jboss.tools.switchyard.ui.bot.test.suite.SwitchyardSuite;
import org.junit.After;
import org.junit.runner.RunWith;

/**
 * Abstract test class for importing quickstarts
 * 
 * @author apodhrad
 * 
 */
@CleanWorkspace
@OpenPerspective(JavaEEPerspective.class)
@Server(type = Type.ALL, state = State.PRESENT)
@RunWith(SwitchyardSuite.class)
public abstract class QuickstartsTest extends RedDeerTest {

	protected String quickstartPath;

	public QuickstartsTest(String quickstartPath) {
		this.quickstartPath = quickstartPath;
	}

	protected void testQuickstart(String path) {
		String fullPath = SwitchyardSuite.getServerHome() + "/" + quickstartPath + "/" + path;
		assertTrue("Path '" + fullPath + "' doesn exist!", new File(fullPath).exists());

		new ImportMavenWizard().importProject(fullPath);

		checkErrors(path);
	}

	protected void checkErrors(String path) {
		AbstractWait.sleep(TimePeriod.NORMAL);
		new WaitWhile(new JobIsRunning(), TimePeriod.LONG);

		ProblemsView problemsView = new ProblemsView();
		problemsView.open();
		List<TreeItem> errors = problemsView.getAllErrors();
		assertTrue("After importing the quickstart '" + path
				+ "' there are the following errors:\n" + toString(errors), errors.isEmpty());
	}

	@After
	public void deleteAllProjects() {
		new SWTWorkbenchBot().closeAllShells();
		ProjectExplorer projectExplorer = new ProjectExplorer();
		for (Project project : projectExplorer.getProjects()) {
			project.delete(false);
		}
	}

	protected String toString(List<TreeItem> items) {
		StringBuffer result = new StringBuffer();
		for (TreeItem item : items) {
			result.append(item.getText());
			result.append("\n");
		}
		return result.toString();
	}

}
