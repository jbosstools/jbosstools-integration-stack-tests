package org.jboss.tools.bpmn2.ui.bot.test.testcase.dialog;

import org.jboss.reddeer.eclipse.jdt.ui.ProjectExplorer;
import org.jboss.reddeer.eclipse.jdt.ui.packageexplorer.PackageExplorer;
import org.jboss.reddeer.jface.exception.JFaceLayerException;
import org.jboss.tools.bpmn2.reddeer.dialog.BPMN2ProcessWizard;
import org.jboss.tools.bpmn2.reddeer.dialog.JBPMProjectWizard;
import org.jboss.tools.bpmn2.ui.bot.test.requirements.ProcessRuntimeRequirement.ProcessRuntime;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * Verify functionality of the jbpm model wizard.
 */
@ProcessRuntime()
public class MixedApproachWizardTest {

	@BeforeClass
	public static void createProject() {
		new JBPMProjectWizard().execute("TestProject");
	}
	
	@AfterClass
	public static void deleteProject() throws Exception {
		new PackageExplorer().getProject("TestProject").delete(true);
	}
	
	@Test
	public void newModelTest() throws Exception {
		new BPMN2ProcessWizard().execute(new String[] {"TestProject"}, "SampleProcess.bpmn", "Sample", "jboss.org.bpmn2", "defaultPackage");
		Assert.assertTrue(new ProjectExplorer().getProject("TestProject").containsItem("SampleProcess.bpmn"));
		// Assert process name, process id and package
	}
	
	@Test
	public void formContainerExistenceValidationTest() throws Exception {
		BPMN2ProcessWizard wizard = new BPMN2ProcessWizard();
		try {
			wizard.execute(new String[] {"NonExistentProject"}, "new_process.bpmn", "Sample", "jboss.org.bpmn2", "defaultPackage");
		} catch (JFaceLayerException e) {
			Assert.assertEquals("Button '&Finish' is not enabled", e.getMessage());
		} finally {
			wizard.cancel();
		}
	}
	
	@Test
	public void formPackageNameValidationTest() throws Exception {
		BPMN2ProcessWizard wizard = new BPMN2ProcessWizard();
		try {
			wizard.execute(new String[] {"TestProject"}, "new_process.bpmn", "Sample", "jboss.org/bpmn2", "defaultPackage");
		} catch (JFaceLayerException e) {
			Assert.assertEquals("Button '&Finish' is not enabled", e.getMessage());
		} finally {
			wizard.cancel();
		}
	}
	
	@Test
	public void formFileNameFieldValidationTest() throws Exception {
		BPMN2ProcessWizard wizard = new BPMN2ProcessWizard();
		try {
			// file must end with 'bpmn' or 'bpmn2'
			wizard.execute(new String[] {"TestProject"}, "new_process.bpmnX", "Sample", "jboss.org/bpmn2", "defaultPackage");
		} catch (JFaceLayerException e) {
			Assert.assertEquals("Button '&Finish' is not enabled", e.getMessage());
		} finally {
			wizard.cancel();
		}
	}
}
