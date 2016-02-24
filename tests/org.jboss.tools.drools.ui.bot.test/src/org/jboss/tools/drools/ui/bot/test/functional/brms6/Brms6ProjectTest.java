package org.jboss.tools.drools.ui.bot.test.functional.brms6;

import static org.jboss.reddeer.eclipse.ui.problems.ProblemsView.ProblemType.ERROR;
import static org.jboss.reddeer.eclipse.ui.problems.ProblemsView.ProblemType.WARNING;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

import org.apache.log4j.Logger;
import org.jboss.reddeer.common.matcher.RegexMatcher;
import org.jboss.reddeer.common.wait.WaitWhile;
import org.jboss.reddeer.core.condition.JobIsRunning;
import org.jboss.reddeer.eclipse.jdt.ui.ide.NewJavaProjectWizardDialog;
import org.jboss.reddeer.eclipse.jdt.ui.ide.NewJavaProjectWizardPage;
import org.jboss.reddeer.eclipse.jdt.ui.packageexplorer.PackageExplorer;
import org.jboss.reddeer.eclipse.ui.perspectives.JavaPerspective;
import org.jboss.reddeer.eclipse.ui.problems.ProblemsView;
import org.jboss.reddeer.junit.requirement.inject.InjectRequirement;
import org.jboss.reddeer.junit.runner.RedDeerSuite;
import org.jboss.reddeer.swt.api.TreeItem;
import org.jboss.reddeer.swt.exception.SWTLayerException;
import org.jboss.reddeer.swt.impl.menu.ContextMenu;
import org.jboss.reddeer.swt.impl.tree.DefaultTreeItem;
import org.jboss.tools.drools.reddeer.dialog.DroolsRuntimeDialog;
import org.jboss.tools.drools.reddeer.perspective.DroolsPerspective;
import org.jboss.tools.drools.reddeer.preference.DroolsRuntimesPreferencePage;
import org.jboss.tools.drools.reddeer.properties.DroolsProjectProperties;
import org.jboss.tools.drools.reddeer.wizard.NewDroolsProjectWithExamplesWizardPage;
import org.jboss.tools.drools.reddeer.wizard.NewDroolsProjectWizard;
import org.jboss.tools.drools.ui.bot.test.annotation.Drools6Runtime;
import org.jboss.tools.drools.ui.bot.test.annotation.UseDefaultProject;
import org.jboss.tools.drools.ui.bot.test.annotation.UsePerspective;
import org.jboss.tools.drools.ui.bot.test.group.SmokeTest;
import org.jboss.tools.drools.ui.bot.test.util.TestParent;
import org.jboss.tools.runtime.reddeer.requirement.RuntimeReqType;
import org.jboss.tools.runtime.reddeer.requirement.RuntimeRequirement;
import org.jboss.tools.runtime.reddeer.requirement.RuntimeRequirement.Runtime;
import org.junit.Assert;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.junit.runner.RunWith;

@Runtime(type = RuntimeReqType.DROOLS)
@RunWith(RedDeerSuite.class)
public class Brms6ProjectTest extends TestParent {
	private static final Logger LOGGER = Logger.getLogger(Brms6ProjectTest.class);

	@InjectRequirement
	private RuntimeRequirement droolsRequirement;

	@Test
	@Category(SmokeTest.class)
	@UsePerspective(DroolsPerspective.class)
	@Drools6Runtime
	public void testProjectCreationAndDeletion() throws UnsupportedEncodingException {
		final String projectName = "testProjectCreationAndDeletion";
		ProblemsView problems = new ProblemsView();
		problems.open();
		waitASecond();
		final int errors = problems.getProblems(ERROR).size();
		final int warnings = problems.getProblems(WARNING).size();

		NewDroolsProjectWizard wiz = new NewDroolsProjectWizard();
		wiz.open();
		wiz.getFirstPage().selectProjectWithExamples();
		wiz.next();
		NewDroolsProjectWithExamplesWizardPage page = wiz.getProjectWithExamplesPage();
		page.setProjectName(projectName);
		page.checkAll();
		page.setUseDefaultRuntime(true);
		wiz.finish();
		new WaitWhile(new JobIsRunning());

		PackageExplorer explorer = new PackageExplorer();
		Assert.assertTrue("Project was not created.", explorer.containsProject(projectName));

		Assert.assertTrue("Project does not have Drools dependencies.",
				explorer.getProject(projectName).containsItem("Drools Library"));

		String runtimeLocation = new File(
				URLDecoder.decode(droolsRequirement.getConfig().getRuntimeFamily().getHome(), "utf-8")).getPath();
		Assert.assertTrue("Wrong drools runtime used.", findDroolsCoreJar(projectName).contains(runtimeLocation));

		problems = new ProblemsView();
		problems.open();
		waitASecond();
		Assert.assertEquals("There are errors in newly created project.", errors, problems.getProblems(ERROR).size());
		Assert.assertEquals("There are warnings in newly created project.", warnings,
				problems.getProblems(WARNING).size());

		explorer.getProject(projectName).delete(true);
		Assert.assertFalse("Project was not deleted.", explorer.containsProject(projectName));
	}

	@Test
	@UsePerspective(JavaPerspective.class)
	@UseDefaultProject
	@Drools6Runtime
	public void testChangeDefaultRuntime() {
		final String secondRuntime = "testChangeDefaultRuntime";
		final String runtimeLocation = createTempDir("testChangeDefaultRuntime");
		ProblemsView problems = new ProblemsView();
		problems.open();
		waitASecond();
		final int errors = problems.getProblems(ERROR).size();
		final int warnings = problems.getProblems(WARNING).size();

		// create new runtime
		DroolsRuntimesPreferencePage pref = new DroolsRuntimesPreferencePage();
		pref.open();
		DroolsRuntimeDialog wiz = pref.addDroolsRuntime();
		wiz.setName(secondRuntime);
		wiz.createNewRuntime(runtimeLocation);
		wiz.ok();
		pref.okCloseWarning();

		// set new runtime to project
		DroolsProjectProperties props = new DroolsProjectProperties(DEFAULT_PROJECT_NAME);
		props.open();
		props.setDefaultDroolsRuntime(secondRuntime);
		props.ok();
		new WaitWhile(new JobIsRunning());

		// confirm runtime change
		Assert.assertTrue("Wrong drools runtime used.",
				findDroolsCoreJar(DEFAULT_PROJECT_NAME).contains(runtimeLocation));

		// check for new problems
		problems = new ProblemsView();
		problems.open();
		waitASecond();
		Assert.assertEquals("New errors occured.", errors, problems.getProblems(ERROR).size());
		Assert.assertEquals("New warnings occured.", warnings, problems.getProblems(WARNING).size());
	}

	@Test
	@UsePerspective(JavaPerspective.class)
	@Drools6Runtime
	public void testConvertJavaProject() {
		final String projectName = "testJavaProject";
		NewJavaProjectWizardDialog diag = new NewJavaProjectWizardDialog();
		diag.open();
		new NewJavaProjectWizardPage().setProjectName("testJavaProject");
		try {
			diag.finish();
		} catch (SWTLayerException ex) {
			LOGGER.debug("'Open Associated Perspective' dialog was not shown");
		}

		PackageExplorer explorer = new PackageExplorer();
		Assert.assertTrue("Project was not created", explorer.containsProject(projectName));
		Assert.assertFalse("Project already has Drools dependencies.",
				explorer.getProject(projectName).containsItem("Drools Library"));

		explorer.getProject(projectName).select();
		new ContextMenu(new RegexMatcher("Configure.*"), new RegexMatcher("Convert to Drools Project.*")).select();
		new WaitWhile(new JobIsRunning());

		Assert.assertTrue("Project does not have Drools dependencies.",
				explorer.getProject(projectName).containsItem("Drools Library"));
	}

	private String findDroolsCoreJar(String projectName) {
		new PackageExplorer().open();
		TreeItem lib = new DefaultTreeItem(projectName, "Drools Library");
		for (TreeItem libItem : lib.getItems()) {
			if (libItem.getText().startsWith("drools-core")) {
				return libItem.getText();
			}
		}

		return null;
	}

}
