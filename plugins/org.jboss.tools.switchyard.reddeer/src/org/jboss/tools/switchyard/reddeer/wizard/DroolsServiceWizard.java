package org.jboss.tools.switchyard.reddeer.wizard;

import org.jboss.reddeer.jface.wizard.WizardDialog;
import org.jboss.reddeer.swt.impl.shell.DefaultShell;
import org.jboss.reddeer.swt.impl.text.LabeledText;
import org.jboss.tools.switchyard.reddeer.widget.Link;

public class DroolsServiceWizard extends WizardDialog {

	public static final String DIALOG_TITLE = "New File";

	public DroolsServiceWizard activate() {
		new DefaultShell(DIALOG_TITLE);
		return this;
	}

	public DroolsServiceWizard setServiceName(String fileName) {
		activate();
		new LabeledText("Service Name:").setText(fileName);
		return this;
	}
	
	public DroolsServiceWizard setFileName(String fileName) {
		activate();
		new LabeledText("File name:").setText(fileName);
		return this;
	}

	public DroolsServiceWizard createNewInterface(String name) {
		activate();
		new Link("Interface:").click();
		new JavaInterfaceWizard().activate().setName(name).finish();
		return this;
	}

	public DroolsServiceWizard setExistingInterface(String name) {
		throw new UnsupportedOperationException();
	}
}
