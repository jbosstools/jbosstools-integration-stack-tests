package org.jboss.tools.bpel.reddeer.wizard;

import org.jboss.reddeer.jface.wizard.NewWizardDialog;
import org.jboss.reddeer.swt.impl.text.LabeledText;

/**
 * 
 * @author apodhrad
 * 
 */
public class NewDescriptorWizard extends NewWizardDialog {

	public static final String LABEL_PROJECT = "BPEL Project:";

	private String projectName;

	public NewDescriptorWizard(String projectName) {
		super("BPEL 2.0", "BPEL Deployment Descriptor");
		this.projectName = projectName;
	}

	public void execute() {
		open();

		new LabeledText(LABEL_PROJECT).setText(projectName + "/bpelContent");

		finish();
	}
}
