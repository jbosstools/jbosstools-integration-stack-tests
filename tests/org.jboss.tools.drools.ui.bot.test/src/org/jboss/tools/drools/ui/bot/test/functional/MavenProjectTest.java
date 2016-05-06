package org.jboss.tools.drools.ui.bot.test.functional;

import org.jboss.reddeer.common.wait.TimePeriod;
import org.jboss.reddeer.eclipse.ui.perspectives.JavaPerspective;
import org.jboss.reddeer.junit.runner.RedDeerSuite;
import org.jboss.tools.drools.reddeer.wizard.NewDroolsProjectWithExamplesWizardPage;
import org.jboss.tools.drools.reddeer.wizard.NewDroolsProjectWizard;
import org.jboss.tools.drools.ui.bot.test.annotation.UsePerspective;
import org.jboss.tools.drools.ui.bot.test.util.TestParent;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(RedDeerSuite.class)
public class MavenProjectTest extends TestParent {

	@Test
	@UsePerspective(JavaPerspective.class)
	public void createMavenProjectTest() {
		final String projectName = this.getMethodName();
		createMavenProjectWithAllSamples(projectName);
		runAndCheckDroolsTest(projectName);
	}
	
	private void createMavenProjectWithAllSamples(String projectName) {
		NewDroolsProjectWizard projectWizard = new NewDroolsProjectWizard();
		projectWizard.open();
		projectWizard.getFirstPage().selectProjectWithExamples();
		projectWizard.next();
		NewDroolsProjectWithExamplesWizardPage projectWithExamplesWizardPage = projectWizard.getProjectWithExamplesPage();
		projectWithExamplesWizardPage.setProjectName(projectName);
		projectWithExamplesWizardPage.setUseDefaultLocation(true);
		projectWithExamplesWizardPage.useMaven();
		projectWithExamplesWizardPage.checkAll();
		projectWizard.finish(TimePeriod.VERY_LONG);
	}
}
