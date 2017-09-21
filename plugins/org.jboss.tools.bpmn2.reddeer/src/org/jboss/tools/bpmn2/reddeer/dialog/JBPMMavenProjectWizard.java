package org.jboss.tools.bpmn2.reddeer.dialog;

import org.eclipse.reddeer.eclipse.selectionwizard.NewMenuWizard;
import org.eclipse.reddeer.swt.impl.button.PushButton;
import org.eclipse.reddeer.swt.impl.text.LabeledText;

/**
 * 
 */
public class JBPMMavenProjectWizard extends NewMenuWizard {

	/**
	 * 
	 */
	public JBPMMavenProjectWizard() {
		super("", "jBPM", "jBPM project");
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
