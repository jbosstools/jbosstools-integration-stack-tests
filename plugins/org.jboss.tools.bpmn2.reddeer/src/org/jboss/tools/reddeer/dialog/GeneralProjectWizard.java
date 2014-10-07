package org.jboss.tools.reddeer.dialog;

import org.jboss.reddeer.jface.wizard.NewWizardDialog;
import org.jboss.reddeer.swt.impl.text.LabeledText;

/**
 * 
 */
public class GeneralProjectWizard extends NewWizardDialog {

	/**
	 * 
	 */
	public GeneralProjectWizard() {
		super("General", "Project");
	}	
	
	/**
	 * 
	 * @param projectName
	 */
	public void execute(String projectName) {
		open();
		new LabeledText("Project name:").setText(projectName);
		finish();
	}
	
}
