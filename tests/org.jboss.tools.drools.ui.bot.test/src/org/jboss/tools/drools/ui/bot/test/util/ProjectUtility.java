package org.jboss.tools.drools.ui.bot.test.util;

import org.eclipse.reddeer.common.wait.TimePeriod;
import org.eclipse.reddeer.common.wait.WaitWhile;
import org.eclipse.reddeer.eclipse.jdt.ui.wizards.JavaProjectWizard;
import org.eclipse.reddeer.eclipse.jdt.ui.wizards.NewJavaProjectWizardPageOne;
import org.eclipse.reddeer.workbench.core.condition.JobIsRunning;
import org.jboss.tools.drools.reddeer.wizard.NewDroolsProjectWithExamplesWizardPage;
import org.jboss.tools.drools.reddeer.wizard.NewDroolsProjectWizard;

public final class ProjectUtility {
	
	private static final long TEN_MINUTES = 600L;

	public static void createDroolsProjectWithAllSamples(String projectName) {
		createProjectWithSamples(projectName, false, true, true, true);
	}

	public static void createDroolsProjectWithRuleSample(String projectName) {
		createProjectWithSamples(projectName, false, true, false, false);
	}

	public static void createDroolsProjectWithDecisionTableSample(String projectName) {
		createProjectWithSamples(projectName, false, false, true, false);
	}

	public static void createDroolsProjectWithProcessSample(String projectName) {
		createProjectWithSamples(projectName, false, false, false, true);
	}

	public static void createMavenProjectWithAllSamples(String projectName) {
		createProjectWithSamples(projectName, true, true, true, true);
	}

	public static void createMavenProjectWithRuleSample(String projectName) {
		createProjectWithSamples(projectName, true, true, false, false);
	}

	public static void createMavenProjectWithDecisionTableSample(String projectName) {
		createProjectWithSamples(projectName, true, false, true, false);
	}

	public static void createMavenProjectWithProcessSample(String projectName) {
		createProjectWithSamples(projectName, true, false, false, true);
	}

	private static void createProjectWithSamples(String projectName, boolean useMaven, boolean ruleSample, boolean decisionTableSample, boolean projectSample) {
		NewDroolsProjectWizard projectWizard = new NewDroolsProjectWizard();
		projectWizard.open();
		projectWizard.getFirstPage().selectProjectWithExamples();
		projectWizard.next();
		NewDroolsProjectWithExamplesWizardPage projectWithExamplesWizardPage = projectWizard.getProjectWithExamplesPage();
		projectWithExamplesWizardPage.setProjectName(projectName);
		projectWithExamplesWizardPage.setUseDefaultLocation(true);
		projectWithExamplesWizardPage.setAddSampleRule(ruleSample);
		projectWithExamplesWizardPage.setAddSampleDecisionTable(decisionTableSample);
		projectWithExamplesWizardPage.setAddSampleProcess(projectSample);
		
		if (useMaven) {
			projectWithExamplesWizardPage.useMaven();
			projectWizard.finish(TimePeriod.getCustom(TEN_MINUTES));
		} else {
			projectWithExamplesWizardPage.useRuntime();
			if (projectWithExamplesWizardPage.getInstalledRuntimes().size() <= 0) {
				throw new RuntimeException("No runtime is installed.");
			}
			projectWizard.finish();
			new WaitWhile(new JobIsRunning());
		}
	}

	public static void createJavaProject(String projectName) {
		JavaProjectWizard newJavaProjectWizardDialog = new JavaProjectWizard();
		newJavaProjectWizardDialog.open();
		new NewJavaProjectWizardPageOne(newJavaProjectWizardDialog).setProjectName(projectName);
		newJavaProjectWizardDialog.finish();
	}
}
