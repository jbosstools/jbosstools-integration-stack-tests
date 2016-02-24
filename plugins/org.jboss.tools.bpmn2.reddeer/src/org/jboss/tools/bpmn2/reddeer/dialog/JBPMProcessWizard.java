package org.jboss.tools.bpmn2.reddeer.dialog;

import org.jboss.reddeer.jface.wizard.NewWizardDialog;
import org.jboss.reddeer.swt.impl.text.LabeledText;

/**
 * 
 */
public class JBPMProcessWizard extends NewWizardDialog {

	/**
	 * 
	 */
	public JBPMProcessWizard() {
		super("jBPM", "jBPM Process Diagram");
	}

	/**
	 * Create a new process definition in the root of the first project.
	 * 
	 * @param processName
	 *            name of the file.
	 */
	public void execute(String processName) {
		execute(new String[0], processName);
	}

	/**
	 * Create a new process definition.
	 * 
	 * @param location
	 *            path where the file is supposed to be stored (including project name)
	 * @param processName
	 *            name of the file
	 */
	public void execute(String[] location, String processName) {
		open();
		new LabeledText("Container:").setText(ProjectPath.valueOf(location));
		new LabeledText("Process name:").setText(processName);
		finish();
	}

}
