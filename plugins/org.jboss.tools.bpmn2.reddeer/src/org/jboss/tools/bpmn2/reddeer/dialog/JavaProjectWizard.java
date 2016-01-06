package org.jboss.tools.bpmn2.reddeer.dialog;

import org.jboss.reddeer.jface.wizard.NewWizardDialog;
import org.jboss.reddeer.swt.impl.text.LabeledText;

/**
 * 
 */
public class JavaProjectWizard extends NewWizardDialog {

	/**
	 * 
	 */
	public JavaProjectWizard() {
		super("Java", "Java Project");
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
