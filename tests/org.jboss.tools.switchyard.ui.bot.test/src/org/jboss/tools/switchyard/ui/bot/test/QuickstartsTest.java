package org.jboss.tools.switchyard.ui.bot.test;

import static org.junit.Assert.assertTrue;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;
import org.hamcrest.Matcher;
import org.jboss.reddeer.eclipse.jdt.ui.ProjectExplorer;
import org.jboss.reddeer.eclipse.jdt.ui.packageexplorer.Project;
import org.jboss.reddeer.eclipse.ui.problems.ProblemsView;
import org.jboss.reddeer.swt.api.TreeItem;
import org.jboss.reddeer.swt.condition.JobIsRunning;
import org.jboss.reddeer.swt.condition.ShellWithTextIsActive;
import org.jboss.reddeer.swt.exception.SWTLayerException;
import org.jboss.reddeer.swt.exception.WaitTimeoutExpiredException;
import org.jboss.reddeer.swt.impl.button.PushButton;
import org.jboss.reddeer.swt.matcher.TextMatcher;
import org.jboss.reddeer.swt.reference.ReferencedComposite;
import org.jboss.reddeer.swt.test.RedDeerTest;
import org.jboss.reddeer.swt.util.Display;
import org.jboss.reddeer.swt.util.ResultRunnable;
import org.jboss.reddeer.swt.wait.AbstractWait;
import org.jboss.reddeer.swt.wait.TimePeriod;
import org.jboss.reddeer.swt.wait.WaitWhile;
import org.jboss.tools.switchyard.reddeer.wizard.ImportMavenWizard;
import org.jboss.tools.switchyard.ui.bot.test.suite.CleanWorkspaceRequirement.CleanWorkspace;
import org.jboss.tools.switchyard.ui.bot.test.suite.PerspectiveRequirement.Perspective;
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
@Perspective(name = "Java EE")
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
		AbstractWait.sleep(10 * 1000);
		new WaitWhile(new JobIsRunning(), TimePeriod.LONG);

		ProblemsView problemsView = new ProblemsView();
		problemsView.open();
		List<TreeItem> errors = problemsView.getAllErrors();
		assertTrue("After importing the quickstart '" + path
				+ "' there are the following errors:\n" + toString(errors), errors.isEmpty());
	}

	@After
	public void deleteAllProjects() {
		ProjectExplorer projectExplorer = new ProjectExplorer();
		for (Project project : projectExplorer.getProjects()) {
			try {
				project.delete(false);
			} catch (WaitTimeoutExpiredException e1) {
				clickContinue();
				new WaitWhile(new ShellWithTextIsActive("Delete Resources"), TimePeriod.LONG);
			}
		}
	}

	/**
	 * Clicks on Continue button when deleting a project which is not in sync
	 */
	protected void clickContinue() {
		List<Shell> list = Display.syncExec(new ResultRunnable<List<Shell>>() {

			@Override
			public List<Shell> run() {
				Matcher<String> matcher = new TextMatcher("Delete Resources");
				List<Shell> list = new ArrayList<Shell>();
				Shell[] shell = Display.getDisplay().getShells();
				for (int i = 0; i < shell.length; i++) {
					if (matcher.matches(shell[i])) {
						list.add(shell[i]);
					}
				}
				return list;
			}
		});
		SWTLayerException sle = null;
		for (final Shell sh : list) {
			ReferencedComposite ref = new ReferencedComposite() {

				@Override
				public Control getControl() {
					return sh;
				}

			};
			try {
				new PushButton(ref, "Continue").click();
				return;
			} catch (SWTLayerException e) {
				sle = e;
			}
		}
		if (sle != null) {
			throw sle;
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
