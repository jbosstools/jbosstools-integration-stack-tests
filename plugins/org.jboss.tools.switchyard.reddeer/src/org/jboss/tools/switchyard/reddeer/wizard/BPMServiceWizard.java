package org.jboss.tools.switchyard.reddeer.wizard;

import org.jboss.reddeer.jface.wizard.WizardDialog;
import org.jboss.reddeer.swt.impl.shell.DefaultShell;
import org.jboss.reddeer.swt.impl.text.LabeledText;
import org.jboss.tools.switchyard.reddeer.widget.Link;

public class BPMServiceWizard extends WizardDialog {
	
	public static final String DIALOG_TITLE = "New File";

	public BPMServiceWizard activate() {
		new DefaultShell(DIALOG_TITLE);
		return this;
	}
	
	public BPMServiceWizard setFileName(String fileName) {
		activate();
		new LabeledText("File name:").setText(fileName);
		return this;
	}
	
	public BPMServiceWizard createNewInterface(String name) {
		activate();
		new Link("Interface:").click();
		new JavaInterfaceWizard().activate().setName(name).finish();
		return this;
	}

}
