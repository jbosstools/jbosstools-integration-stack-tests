package org.jboss.tools.runtime.reddeer.wizard;

import org.eclipse.reddeer.jface.wizard.WizardDialog;
import org.eclipse.reddeer.swt.impl.combo.LabeledCombo;
import org.eclipse.reddeer.swt.impl.shell.DefaultShell;
import org.eclipse.reddeer.swt.impl.text.LabeledText;
import org.eclipse.reddeer.swt.impl.tree.DefaultTreeItem;

public class NewHostWizard extends WizardDialog {
	public static final String SSH_ONLY = "SSH Only";
	public static final String GENERAL = "General";
	public static final String TITLE = "New Connection";
	public static final String HOST_NAME = "Host name:";
	public static final String CONNECTION_NAME = "Connection name:";

	public NewHostWizard() {
		new DefaultShell(TITLE);
	}

	public NewHostWizard setSshOnly() {
		new DefaultTreeItem(GENERAL, SSH_ONLY).select();
		return this;
	}

	public NewHostWizard setHostName(String hostName) {
		new LabeledCombo(HOST_NAME).setText(hostName);
		return this;
	}

	public NewHostWizard setConnectionName(String connectionName) {
		new LabeledText(CONNECTION_NAME).setText(connectionName);
		return this;
	}

}
