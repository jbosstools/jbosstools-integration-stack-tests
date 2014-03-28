package org.jboss.tools.bpmn2.ui.bot.test.testcase.dialog;

import org.eclipse.swtbot.swt.finder.SWTBotTestCase;
import org.jboss.reddeer.eclipse.jdt.ui.ProjectExplorer;
import org.jboss.reddeer.eclipse.jdt.ui.packageexplorer.Project;
import org.jboss.tools.bpmn2.reddeer.dialog.JBPMMavenProjectWizard;
import org.junit.After;
import org.junit.Test;

/**
 * Verify functionality of the Maven project wizard.
 */
public class JBPMMavenProjectWizardTest extends SWTBotTestCase {

	ProjectExplorer explorerView = new ProjectExplorer();
	JBPMMavenProjectWizard wizardView  = new JBPMMavenProjectWizard();
	
	@After
	public void deleteAllProjects() {
		for (Project p : explorerView.getProjects()) {
			p.delete(true);
		}
	}
	
	@Test
	public void newProjectWithSimpleProcessTest() throws Exception {
		wizardView.execute("TestMavenProject");
		Project p = explorerView.getProject("TestMavenProject");
		assertTrue(p.containsItem("src/main/resources", "com.sample", "sample.bpmn"));
		assertTrue(p.containsItem("src/main/resources", "META-INF", "kmodule.xml"));
		assertTrue(p.containsItem("src/main/java", "com.sample", "ProcessMain.java"));
	}
	
}
