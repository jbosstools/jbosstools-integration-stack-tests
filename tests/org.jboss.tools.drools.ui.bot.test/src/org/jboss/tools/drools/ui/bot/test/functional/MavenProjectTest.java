package org.jboss.tools.drools.ui.bot.test.functional;

import org.apache.log4j.Logger;
import org.jboss.reddeer.eclipse.ui.console.ConsoleView;
import org.jboss.reddeer.eclipse.ui.perspectives.JavaPerspective;
import org.jboss.reddeer.junit.runner.RedDeerSuite;
import org.jboss.reddeer.common.wait.TimePeriod;
import org.jboss.reddeer.common.wait.WaitUntil;
import org.jboss.tools.drools.reddeer.wizard.NewDroolsProjectWithExamplesWizardPage;
import org.jboss.tools.drools.reddeer.wizard.NewDroolsProjectWizard;
import org.jboss.tools.drools.ui.bot.test.annotation.UsePerspective;
import org.jboss.tools.drools.ui.bot.test.util.ApplicationIsTerminated;
import org.jboss.tools.drools.ui.bot.test.util.RunUtility;
import org.jboss.tools.drools.ui.bot.test.util.TestParent;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(RedDeerSuite.class)
public class MavenProjectTest extends TestParent {

	private static final Logger LOGGER = Logger.getLogger(RulesManagementTest.class);
	private static final String DEBUG_REGEX = "(SLF4J: .*[\r\n]+)+?" + "(kmodules: file:(/.*)+/kmodule.xml[\r\n]+)?";
	private static final String SUCCESSFUL_RUN_REGEX = DEBUG_REGEX + "Hello World[\r\n]+Goodbye cruel world[\r\n]+";

	@Test
	@UsePerspective(JavaPerspective.class)
	public void createMavenProjectTest() {
		final String projectName = this.getMethodName();
		createMavenProjectWithAllSamples(projectName);

		ConsoleView console = new ConsoleView();
		console.open();

		RunUtility.runAsJavaApplication(projectName, "src/main/java", "com.sample", "DroolsTest.java");
		new WaitUntil(new ApplicationIsTerminated());
		waitASecond();

		console.open();
		String consoleText = console.getConsoleText();
		Assert.assertNotNull("Console text was empty.", consoleText);
		LOGGER.debug(consoleText);
		Assert.assertTrue("Unexpected text in console\n" + consoleText, consoleText.matches(SUCCESSFUL_RUN_REGEX));
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
