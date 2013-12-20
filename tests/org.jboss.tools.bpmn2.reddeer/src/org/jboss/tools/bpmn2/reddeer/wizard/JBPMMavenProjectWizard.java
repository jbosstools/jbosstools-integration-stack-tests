package org.jboss.tools.bpmn2.reddeer.wizard;

import static org.junit.Assert.assertTrue;

import org.jboss.reddeer.eclipse.jdt.ui.packageexplorer.PackageExplorer;
import org.jboss.reddeer.eclipse.jface.wizard.NewWizardDialog;
import org.jboss.reddeer.eclipse.jface.wizard.WizardPage;
import org.jboss.reddeer.swt.impl.text.LabeledText;

/**
 * 
 * @author Marek Baluch <mbaluch@redhat.com>
 */
public class JBPMMavenProjectWizard extends NewWizardDialog {

	/**
	 * Creates a new instance of JBPMMavenProjectWizard.
	 */
	public JBPMMavenProjectWizard() {
		super("jBPM", "jBPM project (Maven)");
	}
	
	@Override
	public WizardPage getFirstPage() {
		return null;
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
