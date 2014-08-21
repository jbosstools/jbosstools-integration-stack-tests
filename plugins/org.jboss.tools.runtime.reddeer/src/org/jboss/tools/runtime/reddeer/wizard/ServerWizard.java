package org.jboss.tools.runtime.reddeer.wizard;

import org.jboss.reddeer.jface.wizard.NewWizardDialog;
import org.jboss.reddeer.swt.impl.combo.DefaultCombo;
import org.jboss.reddeer.swt.impl.text.LabeledText;
import org.jboss.reddeer.swt.impl.tree.DefaultTreeItem;

/**
 * 
 * @author apodhrad
 * 
 */
public class ServerWizard extends NewWizardDialog {

	public static final String HOST_NAME = "Server's host name:";
	public static final String SERVER_NAME = "Server name:";
	public static final String RUNTIME = "Server runtime environment:";
	
	public ServerWizard() {
		super("Server", "Server");
	}

	public ServerWizard setType(String category, String label) {
		new DefaultTreeItem(category, label).select();
		return this;
	}

	public ServerWizard setName(String name) {
		new LabeledText(SERVER_NAME).setText(name);
		return this;
	}

	public ServerWizard setRuntime(String runtime) {
		try {
			new DefaultCombo(0).setSelection(runtime);
		} catch (Exception ex) {
			new DefaultCombo(1).setSelection(runtime);
		}
		return this;
	}

	/*
	 * JBoss Fuse
	 */
	public static final String PORT_NUMBER = "SSH Port: ";
	public static final String USER_NAME = "User Name:";
	public static final String PASSWORD = "Password: ";
	
	public ServerWizard setPort(String port) {
		new LabeledText(PORT_NUMBER).setText(port);
		return this;
	}

	public ServerWizard setUsername(String username) {
		new LabeledText(USER_NAME).setText(username);
		return this;
	}

	public ServerWizard setPassword(String password) {
		new LabeledText(PASSWORD).setText(password);
		return this;
	}
}
