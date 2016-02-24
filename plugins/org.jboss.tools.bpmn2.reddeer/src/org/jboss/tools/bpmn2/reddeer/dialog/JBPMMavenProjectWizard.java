package org.jboss.tools.bpmn2.reddeer.dialog;

import org.jboss.reddeer.jface.wizard.NewWizardDialog;
import org.jboss.reddeer.swt.impl.button.PushButton;
import org.jboss.reddeer.swt.impl.text.LabeledText;

/**
 * 
 */
public class JBPMMavenProjectWizard extends NewWizardDialog {

	/**
	 * 
	 */
	public JBPMMavenProjectWizard() {
		super("jBPM", "jBPM project");
	}

	/**
	 * 
	 * @param projectName
	 */
	public void execute(String projectName) {
		open();
		new PushButton(0).click();
		next();
		new LabeledText("Project name:").setText(projectName);
		finish();
	}

}
