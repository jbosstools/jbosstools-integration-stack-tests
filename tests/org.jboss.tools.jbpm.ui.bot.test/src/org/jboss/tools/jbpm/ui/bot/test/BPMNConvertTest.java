package org.jboss.tools.jbpm.ui.bot.test;

import org.jboss.reddeer.eclipse.jdt.ui.ide.NewJavaProjectWizardDialog;
import org.jboss.reddeer.eclipse.jdt.ui.ide.NewJavaProjectWizardPage;
import org.jboss.reddeer.eclipse.jdt.ui.packageexplorer.PackageExplorer;
import org.jboss.reddeer.eclipse.ui.perspectives.JavaPerspective;
import org.jboss.reddeer.junit.runner.RedDeerSuite;
import org.jboss.reddeer.requirements.cleanworkspace.CleanWorkspaceRequirement.CleanWorkspace;
import org.jboss.reddeer.requirements.openperspective.OpenPerspectiveRequirement.OpenPerspective;
import org.jboss.reddeer.swt.impl.button.PushButton;
import org.jboss.reddeer.swt.impl.menu.ShellMenu;
import org.jboss.reddeer.swt.impl.text.LabeledText;
import org.jboss.tools.jbpm.ui.bot.test.wizard.ExportBPMNWizard;
import org.jboss.tools.jbpm.ui.bot.test.wizard.ImportFileWizard;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

@CleanWorkspace
@OpenPerspective(JavaPerspective.class)
@RunWith(RedDeerSuite.class)
public class BPMNConvertTest {

	private String projectName = "BPMNConvertProject";
	private String originalFolder = "original";
	private String targetFolder = "target";
	private String file1 = "PolicyPricingProcess.bpmn";

	@Before
	public void prepareProject() {
		// Create Java Project
		NewJavaProjectWizardDialog projectWizard = new NewJavaProjectWizardDialog();
		projectWizard.open();
		new NewJavaProjectWizardPage().setProjectName(projectName);
		projectWizard.finish();

		// create original folder
		new PackageExplorer().getProject(projectName).select();
		new ShellMenu("File", "New", "Folder").select();
		new LabeledText("Folder name:").setText(originalFolder);
		new PushButton("Finish").click();

		// create target folder
		new PackageExplorer().getProject(projectName).select();
		new ShellMenu("File", "New", "Folder").select();
		new LabeledText("Folder name:").setText(targetFolder);
		new PushButton("Finish").click();

		// import files
		new PackageExplorer().getProject(projectName).getProjectItem(originalFolder).select();
		new ImportFileWizard().importFile("resources/" + originalFolder);
	}

	@Test
	public void convertProcess() {
		new PackageExplorer().getProject(projectName).getProjectItem(originalFolder, file1).select();
		new ExportBPMNWizard().exportFile(projectName, targetFolder);
		new PackageExplorer().getProject(projectName)
				.getProjectItem(targetFolder, "jpdl", file1, "Policy Pricing", "processdefinition.xml").open();
	}

}
