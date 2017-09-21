package org.jboss.tools.switchyard.ui.bot.test;

import static org.junit.Assert.assertTrue;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.reddeer.common.wait.AbstractWait;
import org.eclipse.reddeer.common.wait.TimePeriod;
import org.eclipse.reddeer.common.wait.WaitUntil;
import org.eclipse.reddeer.common.wait.WaitWhile;
import org.eclipse.reddeer.eclipse.jdt.ui.packageview.PackageExplorerPart;
import org.eclipse.reddeer.eclipse.ui.perspectives.JavaEEPerspective;
import org.eclipse.reddeer.eclipse.ui.problems.Problem;
import org.eclipse.reddeer.junit.annotation.RequirementRestriction;
import org.eclipse.reddeer.junit.requirement.inject.InjectRequirement;
import org.eclipse.reddeer.junit.requirement.matcher.RequirementMatcher;
import org.eclipse.reddeer.junit.runner.RedDeerSuite;
import org.eclipse.reddeer.requirements.openperspective.OpenPerspectiveRequirement.OpenPerspective;
import org.eclipse.reddeer.requirements.server.ServerRequirementState;
import org.eclipse.reddeer.swt.api.TreeItem;
import org.eclipse.reddeer.workbench.core.condition.JobIsRunning;
import org.eclipse.reddeer.workbench.handler.WorkbenchShellHandler;
import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.jboss.tools.runtime.reddeer.requirement.ServerImplementationType;
import org.jboss.tools.switchyard.reddeer.condition.ErrorsExist;
import org.jboss.tools.switchyard.reddeer.requirement.SwitchYardRequirement;
import org.jboss.tools.switchyard.reddeer.requirement.SwitchYardRequirement.SwitchYard;
import org.jboss.tools.switchyard.reddeer.requirement.SwitchYardServerRestriction;
import org.jboss.tools.switchyard.reddeer.wizard.ImportMavenWizard;
import org.junit.After;
import org.junit.runner.RunWith;

/**
 * Abstract test class for importing quickstarts
 * 
 * @author apodhrad
 * 
 */
@SwitchYard(state = ServerRequirementState.PRESENT)
@OpenPerspective(JavaEEPerspective.class)
@RunWith(RedDeerSuite.class)
public abstract class QuickstartsTest {

	protected String quickstartPath;

	@InjectRequirement
	private SwitchYardRequirement switchyardRequirement;

	@RequirementRestriction
	public static RequirementMatcher getRequirementMatcher() {
		return new SwitchYardServerRestriction(ServerImplementationType.ANY);
	}

	public QuickstartsTest(String quickstartPath) {
		this.quickstartPath = quickstartPath;
	}

	protected void testQuickstart(String path) {
		String fullPath = switchyardRequirement.getConfiguration().getServer().getHome() + "/" + quickstartPath + "/"
				+ path;
		assertTrue("Path '" + fullPath + "' doesn exist!", new File(fullPath).exists());

		new ImportMavenWizard().importProject(fullPath);

		checkErrors(path, new FixableErrorMatcher());
	}

	protected void checkErrors(String path) {
		checkErrors(path, null);
	}

	protected void checkErrors(String path, Matcher<TreeItem> excludeMatcher) {
		AbstractWait.sleep(TimePeriod.DEFAULT);
		new WaitWhile(new JobIsRunning(), TimePeriod.LONG);

		ErrorsExist errorsExistCondition = new ErrorsExist();
		new WaitUntil(errorsExistCondition, TimePeriod.LONG, false);
		List<Problem> allErrors = errorsExistCondition.getAllErrors();
		List<Problem> errors = new ArrayList<Problem>();
		for (Problem error : allErrors) {
			if (excludeMatcher != null && excludeMatcher.matches(error)) {
				continue;
			}
			errors.add(error);
		}

		assertTrue("After importing the quickstart '" + path + "' there are the following errors:\n" + toString(errors),
				errors.isEmpty());
	}

	@After
	public void deleteAllProjects() {
		WorkbenchShellHandler.getInstance().closeAllNonWorbenchShells();

		PackageExplorerPart packageExplorer = new PackageExplorerPart();
		packageExplorer.open();
		packageExplorer.deleteAllProjects(false);
	}

	protected String toString(List<Problem> problems) {
		StringBuffer result = new StringBuffer();
		for (Problem problem : problems) {
			result.append(problem.getDescription());
			result.append("\n");
		}
		return result.toString();
	}

	private class FixableErrorMatcher extends BaseMatcher<TreeItem> {

		public static final String NOT_COVERED = "Plugin execution not covered by lifecycle configuration";

		@Override
		public boolean matches(Object obj) {
			if (obj instanceof TreeItem) {
				TreeItem treeItem = (TreeItem) obj;
				String text = treeItem.getText();
				return text != null && text.startsWith(NOT_COVERED);
			}
			return false;
		}

		@Override
		public void describeTo(Description desc) {

		}

	}

}
