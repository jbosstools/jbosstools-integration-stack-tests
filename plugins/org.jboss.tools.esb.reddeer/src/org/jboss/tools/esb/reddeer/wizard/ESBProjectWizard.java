package org.jboss.tools.esb.reddeer.wizard;

import org.jboss.reddeer.eclipse.jface.wizard.NewWizardDialog;
import org.jboss.reddeer.swt.impl.combo.DefaultCombo;
import org.jboss.reddeer.swt.impl.shell.DefaultShell;
import org.jboss.reddeer.swt.impl.text.LabeledText;

/**
 * Wizard for creating an ESB project.
 * 
 * @author apodhrad
 * 
 */
public class ESBProjectWizard extends NewWizardDialog {

	public static final String DIALOG_TITLE = "New ESB Project Wizard";
	public static final String PROJECT_NAME = "Project name:";

	public ESBProjectWizard() {
		super("ESB", "ESB Project");
	}

	public ESBProjectWizard activate() {
		new DefaultShell(DIALOG_TITLE);
		return this;
	}

	public ESBProjectWizard openWizard() {
		open();
		activate();
		return this;
	}

	public ESBProjectWizard setName(String name) {
		new LabeledText(PROJECT_NAME).setFocus();
		new LabeledText(PROJECT_NAME).setText(name);
		return this;
	}

	public String getName() {
		return new LabeledText(PROJECT_NAME).getText();
	}

	public void setServer(String server) {
		new DefaultCombo(0).setSelection(server);
	}

	public void setVersion(String version) {
		new DefaultCombo(1).setSelection(version);
	}

}
