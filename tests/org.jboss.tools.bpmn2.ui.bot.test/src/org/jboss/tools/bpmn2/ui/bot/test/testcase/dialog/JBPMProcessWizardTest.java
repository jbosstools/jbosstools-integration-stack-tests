package org.jboss.tools.bpmn2.ui.bot.test.testcase.dialog;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.fail;

import org.jboss.reddeer.eclipse.jdt.ui.ProjectExplorer;
import org.jboss.tools.bpmn2.reddeer.dialog.JBPMProcessWizard;
import org.jboss.tools.bpmn2.reddeer.dialog.JBPMProjectWizard;
import org.jboss.tools.bpmn2.reddeer.dialog.JBPMProjectWizard.ProjectType;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * Verify functionality of the process wizard.
 */
public class JBPMProcessWizardTest {

	static ProjectExplorer packageView = new ProjectExplorer();
	static JBPMProjectWizard projectWizardView = new JBPMProjectWizard();
	static JBPMProcessWizard processWizardView = new JBPMProcessWizard();

	@Before
	public void createProject() {
		projectWizardView.execute("TestProject", ProjectType.EMPTY);
	}

	@After
	public void deleteProject() {
		packageView.getProject("TestProject").delete(true);
	}

	@Test
	public void newProcessTest() {
		processWizardView.execute(new String[] { "TestProject", "src/main/resources" }, "SampleProcess");
		Assert.assertTrue(
				packageView.getProject("TestProject").containsItem("src/main/resources", "SampleProcess.bpmn2"));
	}

	
	@Test
	public void newProcessFormValidationTest() throws Exception {
		try {
			processWizardView.execute(new String[] { }, "SampleProcess");
			fail("Created process without container");
		} catch(RuntimeException e) {
			processWizardView.cancel();
		}
		
		assertFalse(packageView.getProject("TestProject").containsItem("src/main/resources", "SampleProcess.bpmn2"));
	}

}
