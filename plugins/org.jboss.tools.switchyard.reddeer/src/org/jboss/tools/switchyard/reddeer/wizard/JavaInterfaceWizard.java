package org.jboss.tools.switchyard.reddeer.wizard;

import org.eclipse.reddeer.eclipse.selectionwizard.NewMenuWizard;
import org.eclipse.reddeer.swt.impl.shell.DefaultShell;
import org.eclipse.reddeer.swt.impl.text.LabeledText;

/**
 * Wizard for creating Java interface.
 * 
 * @author apodhrad
 * 
 */
public class JavaInterfaceWizard extends NewMenuWizard {

	public static final String DIALOG_TITLE = "New Java Interface";

	public JavaInterfaceWizard() {
		super(DIALOG_TITLE, "Java", "Interface");
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
