package org.jboss.tools.drools.ui.bot.test.smoke;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

import org.jboss.reddeer.common.wait.AbstractWait;
import org.jboss.reddeer.common.wait.TimePeriod;
import org.jboss.reddeer.common.wait.WaitWhile;
import org.jboss.reddeer.core.condition.JobIsRunning;
import org.jboss.reddeer.eclipse.jdt.ui.ide.NewJavaProjectWizardDialog;
import org.jboss.reddeer.eclipse.jdt.ui.ide.NewJavaProjectWizardPage;
import org.jboss.reddeer.eclipse.jdt.ui.packageexplorer.PackageExplorer;
import org.jboss.reddeer.eclipse.ui.perspectives.JavaPerspective;
import org.jboss.reddeer.eclipse.ui.problems.ProblemsView;
import org.jboss.reddeer.eclipse.ui.problems.ProblemsView.ProblemType;
import org.jboss.reddeer.junit.requirement.inject.InjectRequirement;
import org.jboss.reddeer.junit.runner.RedDeerSuite;
import org.jboss.reddeer.swt.api.TreeItem;
import org.jboss.reddeer.swt.impl.menu.ContextMenu;
import org.jboss.reddeer.swt.impl.tree.DefaultTreeItem;
import org.jboss.tools.drools.reddeer.perspective.DroolsPerspective;
import org.jboss.tools.drools.ui.bot.test.util.TestParent;
import org.jboss.tools.drools.ui.bot.test.util.annotation.UsePerspective;
import org.jboss.tools.runtime.reddeer.requirement.RuntimeReqType;
import org.jboss.tools.runtime.reddeer.requirement.RuntimeRequirement;
import org.jboss.tools.runtime.reddeer.requirement.RuntimeRequirement.Runtime;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

@Runtime(type = RuntimeReqType.DROOLS)
@RunWith(RedDeerSuite.class)
public class DroolsProjectTest extends TestParent {
	
	private static final String DROOLS_LIBRARY_NAME = "Drools Library";
	private static final String DROOLS_CORE_JAR_PREFIX = "drools-core";
	private static final String[] CONVERT_TO_DROOLS_PROJECT_PATH = {"Configure", "Convert to Drools Project"};
	
	@InjectRequirement
	private RuntimeRequirement droolsRuntime;

	@Test
	@UsePerspective(DroolsPerspective.class)
	public void createRunAndDeleteProjectTest() throws UnsupportedEncodingException {
		final String projectName = this.getMethodName();
		
		assertNoErrorsInProblemsVies();
		createDroolsProjectWithSamples(projectName);
		runAndCheckDroolsTest(projectName);
		PackageExplorer packageExplorer = new PackageExplorer();
		
		Assert.assertTrue("Project is not created.", packageExplorer.containsProject(projectName));
		Assert.assertTrue("Project does not have Drools dependencies.", packageExplorer.getProject(projectName).containsItem(DROOLS_LIBRARY_NAME));
		Assert.assertTrue("Wrong drools runtime is used.", findDroolsCoreJar(projectName).contains(getRuntimeLocation()));
		assertNoErrorsInProblemsVies();
		
		packageExplorer.getProject(projectName).delete(true);
		
		Assert.assertFalse("Project is not deleted.", packageExplorer.containsProject(projectName));
	}
	
	@Test
	@UsePerspective(JavaPerspective.class)
	public void convertToDroolsProjectTest() {
		final String projectName = this.getMethodName();
		
		createJavaProject(projectName);
		PackageExplorer packageExplorer = new PackageExplorer();
		
		Assert.assertTrue("Project is not created.", packageExplorer.containsProject(projectName));
		Assert.assertFalse("Project already has Drools dependencies.",packageExplorer.getProject(projectName).containsItem(DROOLS_LIBRARY_NAME));

		packageExplorer.getProject(projectName).select();
		new ContextMenu(CONVERT_TO_DROOLS_PROJECT_PATH).select();
		new WaitWhile(new JobIsRunning());

		Assert.assertTrue("Project does not have Drools dependencies.", packageExplorer.getProject(projectName).containsItem(DROOLS_LIBRARY_NAME));
		assertNoErrorsInProblemsVies();
	}
	
	private String findDroolsCoreJar(String projectName) {
		new PackageExplorer().open();
		TreeItem droolsLibrary = new DefaultTreeItem(projectName, DROOLS_LIBRARY_NAME);
		for (TreeItem droolsJar : droolsLibrary.getItems()) {
			if (droolsJar.getText().startsWith(DROOLS_CORE_JAR_PREFIX)) {
				return droolsJar.getText();
			}
		}

		throw new RuntimeException(DROOLS_CORE_JAR_PREFIX + " jar is not found in " + DROOLS_LIBRARY_NAME + ".");
	}
	
	private void assertNoErrorsInProblemsVies() {
		ProblemsView problemsView = new ProblemsView();
		problemsView.open();
		AbstractWait.sleep(TimePeriod.SHORT);
		Assert.assertEquals("There are errors in problems view.", 0, problemsView.getProblems(ProblemType.ERROR).size());
	}
	
	private String getRuntimeLocation() throws UnsupportedEncodingException {
		return new File(URLDecoder.decode(droolsRuntime.getConfig().getRuntimeFamily().getHome(), "utf-8")).getPath();
	}
	
	private void createJavaProject(String projectName) {
		NewJavaProjectWizardDialog newJavaProjectWizardDialog = new NewJavaProjectWizardDialog();
		newJavaProjectWizardDialog.open();
		new NewJavaProjectWizardPage().setProjectName(projectName);
		newJavaProjectWizardDialog.finish();
	}
}
