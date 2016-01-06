package org.jboss.tools.bpmn2.reddeer.dialog;

import static org.junit.Assert.assertTrue;

import org.jboss.reddeer.eclipse.jdt.ui.packageexplorer.PackageExplorer;
import org.jboss.reddeer.jface.wizard.NewWizardDialog;
import org.jboss.reddeer.swt.impl.text.LabeledText;

/**
 * 
 */
public class JBPMMavenProjectWizard extends NewWizardDialog {

	/**
	 * 
	 */
	public JBPMMavenProjectWizard() {
		super("jBPM", "jBPM project (Maven)");
	}

	/**
	 * 
	 * @param projectName
	 */
	public void execute(String projectName) {
		open();
		new LabeledText("Project name:").setText(projectName);
		finish();
		assertTrue("Project '" + projectName + "' was not created", new PackageExplorer().containsProject(projectName));
	}

}
