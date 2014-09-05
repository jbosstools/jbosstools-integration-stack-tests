package org.jboss.tools.switchyard.reddeer.wizard;

import org.jboss.reddeer.jface.wizard.NewWizardDialog;
import org.jboss.reddeer.swt.impl.shell.DefaultShell;
import org.jboss.reddeer.swt.impl.text.LabeledText;

/**
 * Wizard for creating Java interface.
 * 
 * @author apodhrad
 * 
 */
public class JavaInterfaceWizard extends NewWizardDialog {

	public static final String DIALOG_TITLE = "New Java Interface";

	public JavaInterfaceWizard() {
		super("Java", "Interface");
	}

	public JavaInterfaceWizard activate() {
		new DefaultShell(DIALOG_TITLE);
		return this;
	}

	public JavaInterfaceWizard setName(String name) {
		activate();
		new LabeledText("Name:").setText(name);
		return this;
	}

}
