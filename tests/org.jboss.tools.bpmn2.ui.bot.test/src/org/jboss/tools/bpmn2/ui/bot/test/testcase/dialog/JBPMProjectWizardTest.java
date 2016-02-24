package org.jboss.tools.bpmn2.ui.bot.test.testcase.dialog;

import org.eclipse.swtbot.swt.finder.SWTBotTestCase;
import org.jboss.reddeer.eclipse.jdt.ui.ProjectExplorer;
import org.jboss.reddeer.common.wait.WaitUntil;
import org.jboss.reddeer.eclipse.condition.ProblemsViewIsEmpty;
import org.jboss.reddeer.eclipse.core.resources.Project;
import org.jboss.reddeer.swt.impl.tree.DefaultTreeItem;
import org.jboss.tools.bpmn2.reddeer.dialog.JBPMProjectWizard;
import org.jboss.tools.bpmn2.reddeer.dialog.JBPMProjectWizard.ProjectType;
import org.junit.After;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * Verify functionality of the project wizard.
 */
public class JBPMProjectWizardTest extends SWTBotTestCase {
	
	private static ProjectExplorer explorerView;
	private static JBPMProjectWizard wizardView;
	
	@BeforeClass
	public static void init() {
		explorerView = new ProjectExplorer();
		wizardView = new JBPMProjectWizard();
	}

	@After
	public void deleteAllProjects() {
		try{
			new WaitUntil(new ProblemsViewIsEmpty());
		} finally {
		
			for (Project p : explorerView.getProjects()) {
				p.delete(true);
			}
		}
	}
	
	@Test()
	public void emptyProjectTest() throws Exception {
		wizardView.execute("TestProject", ProjectType.EMPTY);

		Project p = explorerView.getProject("TestProject");
		// the node list will contain one empty node!
		assertTrue(new DefaultTreeItem("TestProject", "src/main/java").getItems().isEmpty());
		assertTrue(p.containsItem("src/main/resources", "META-INF", "kmodule.xml"));
	}

	@Test
	public void projectWithHelloWorldTest() throws Exception {
		wizardView.execute("TestProject", false);

		Project p = explorerView.getProject("TestProject");
		assertTrue(p.containsItem("src/main/resources", "com.sample", "sample.bpmn"));
		assertFalse(p.containsItem("src/main/java", "com.sample", "ProcessTest.java"));
		assertTrue(p.containsItem("src/main/resources", "META-INF", "kmodule.xml"));
	}

	@Test
	public void projectWithTestedHelloWorldTest() {
		wizardView.execute("TestProject", true);

		Project p = explorerView.getProject("TestProject");
		assertTrue(p.containsItem("src/main/resources", "com.sample", "sample.bpmn"));
		assertTrue(p.containsItem("src/main/java", "com.sample", "ProcessTest.java"));
		assertTrue(p.containsItem("src/main/resources", "META-INF", "kmodule.xml"));
	}
	
	@Test
	public void projectWithExampleFromInternetTest() {
		wizardView.executeForHumanResourcesExample();

		assertTrue(explorerView.getProject("human-resources").containsItem("src/main/resources"));
		assertTrue(explorerView.getProject("human-resources-tests").containsItem("src/main/resources"));
	}

	@Test()
	public void newProjectFormValidationTest() throws Exception {
		try {
			wizardView.open();
			wizardView.finish();
			Assert.fail("Project with an empty name was created!");
		} finally {
			wizardView.cancel();
		}
		
		assertTrue(explorerView.getProjects().isEmpty());
	}

}
