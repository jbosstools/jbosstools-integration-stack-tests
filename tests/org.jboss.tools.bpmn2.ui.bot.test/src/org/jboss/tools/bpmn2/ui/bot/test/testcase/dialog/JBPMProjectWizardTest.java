package org.jboss.tools.bpmn2.ui.bot.test.testcase.dialog;

import org.eclipse.swtbot.swt.finder.SWTBotTestCase;
import org.jboss.reddeer.eclipse.jdt.ui.ProjectExplorer;
import org.jboss.reddeer.eclipse.jdt.ui.packageexplorer.Project;
import org.jboss.reddeer.eclipse.jface.exception.JFaceLayerException;
import org.jboss.reddeer.swt.impl.tree.DefaultTreeItem;
import org.jboss.tools.bpmn2.reddeer.dialog.JBPMProjectWizard;
import org.jboss.tools.bpmn2.ui.bot.test.requirements.ProcessRuntimeRequirement.ProcessRuntime;
import org.junit.After;
import org.junit.Assert;
import org.junit.Test;

/**
 * Verify functionality of the project wizard.
 */
@ProcessRuntime()
public class JBPMProjectWizardTest extends SWTBotTestCase {

	ProjectExplorer explorerView = new ProjectExplorer();
	JBPMProjectWizard wizardView  = new JBPMProjectWizard();
	
	@After
	public void deleteAllProjects() {
		for (Project p : explorerView.getProjects()) {
			p.delete(true);
		}
	}
	
	@Test
	public void newProjectWithSimpleProcessTest() throws Exception {
		wizardView.execute("TestProject", JBPMProjectWizard.ProcessType.SIMPLE, false);
		
		Project p = explorerView.getProject("TestProject");
		assertTrue(p.containsItem("src/main/resources", "sample.bpmn"));
		assertTrue(p.containsItem("src/main/java", "com.sample", "ProcessMain.java"));
	}
	
	@Test
	public void newProjectWithAdvancedProcessTest() {
		wizardView.execute("TestProject", JBPMProjectWizard.ProcessType.ADVANCED, true);
		
		Project p = explorerView.getProject("TestProject");
		assertTrue(p.containsItem("src/main/resources", "sample.bpmn"));
		assertTrue(p.containsItem("TestProject", "src/main/java", "com.sample", "ProcessMain.java"));
		assertTrue(p.containsItem("TestProject", "src/main/java", "com.sample", "ProcessTest.java"));
	}
	
	@Test()
	public void newEmptyProjectTest() throws Exception {
		wizardView.execute("TestProject");
		
		// the node list will contain one empty node!
		assertTrue(new DefaultTreeItem("TestProject", "src/main/java").getItems().isEmpty());
		assertTrue(new DefaultTreeItem("TestProject", "src/main/resources").getItems().isEmpty());
	}

	@Test()
	public void newProjectFormValidationTest() throws Exception {
		try {
			wizardView.execute("");
			Assert.fail("Project with an empty name was created!");
		} catch (JFaceLayerException e) {
			Assert.assertEquals("Button '&Next >' is not enabled", e.getMessage());
		} finally {
			wizardView.cancel();
		}
	}
	
}
