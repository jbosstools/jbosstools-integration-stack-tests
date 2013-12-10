package org.jboss.tools.fuse.reddeer.wizard;

import org.jboss.reddeer.eclipse.jface.wizard.NewWizardDialog;
import org.jboss.reddeer.swt.impl.text.LabeledText;
import org.jboss.reddeer.swt.impl.tree.DefaultTreeItem;

/**
 * Wizard for creating a Fuse server.
 * 
 * @author tsedmik
 * 
 */
public class ServerWizard extends NewWizardDialog {
	
	private static final String SERVER_SECTION = "JBoss Fuse";
	private static final String HOST_NAME = "Server's host name:";
	private static final String NAME = "Server name:";
	private static final String PORT_NUMBER = "Port Number: ";
	private static final String USER_NAME = "User Name:";
	private static final String PASSWORD = "Password: ";

	private String type;
	private String name;
	private String hostName;
	private String portNumber;
	private String userName;
	private String password;

	public ServerWizard() {
		super("Server", "Server");
	}

	public void setType(String type) {
		this.type = type;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setHostName(String hostName) {
		this.hostName = hostName;
	}

	public void setPortNumber(String portNumber) {
		this.portNumber = portNumber;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public void execute() {
		open();

		new DefaultTreeItem(SERVER_SECTION, type).select();
		if (name != null) {
			new LabeledText(NAME).setText(name);	
		}
		if (hostName != null) {
			new LabeledText(HOST_NAME).setText(hostName);
		}
		
		next();
		
		if (portNumber != null) {
			new LabeledText(PORT_NUMBER).setText(portNumber);
		}
		if (userName != null) {
			new LabeledText(USER_NAME).setText(userName);
		}
		if (password != null) {
			new LabeledText(PASSWORD).setText(password);
		}
		
		finish();
	}
}