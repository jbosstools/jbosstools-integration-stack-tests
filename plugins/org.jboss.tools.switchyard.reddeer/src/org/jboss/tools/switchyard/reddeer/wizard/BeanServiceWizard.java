package org.jboss.tools.switchyard.reddeer.wizard;

import org.jboss.reddeer.jface.wizard.WizardDialog;
import org.jboss.reddeer.swt.impl.shell.DefaultShell;
import org.jboss.reddeer.swt.impl.text.LabeledText;
import org.jboss.tools.switchyard.reddeer.widget.Link;

/**
 * Wizard for creating a bean service.
 * 
 * @author apodhrad
 * 
 */
public class BeanServiceWizard extends WizardDialog {

	public static final String DIALOG_TITLE = "New Bean Service";

	public BeanServiceWizard() {
	}

	public BeanServiceWizard activate() {
		new DefaultShell(DIALOG_TITLE);
		return this;
	}

	public BeanServiceWizard setName(String name) {
		activate();
		new LabeledText("Name:").setText(name);
		return this;
	}

	public BeanServiceWizard createNewInterface(String name) {
		activate();
		new Link("Interface:").click();
		new JavaInterfaceWizard().activate().setName(name).finish();
		return this;
	}

	public BeanServiceWizard setExistingInterface(String name) {
		throw new UnsupportedOperationException();
	}

}
