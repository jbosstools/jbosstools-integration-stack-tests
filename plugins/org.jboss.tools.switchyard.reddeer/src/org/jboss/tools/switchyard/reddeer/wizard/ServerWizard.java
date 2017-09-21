package org.jboss.tools.switchyard.reddeer.wizard;

import org.eclipse.reddeer.eclipse.selectionwizard.NewMenuWizard;
import org.eclipse.reddeer.swt.impl.text.LabeledText;
import org.eclipse.reddeer.swt.impl.tree.DefaultTreeItem;

/**
 * Wizard for creating a server.
 * 
 * @author apodhrad
 * 
 */
public class ServerWizard extends NewMenuWizard {

	private String[] type;
	private String name;

	public ServerWizard() {
		super("New Server", "Server", "Server");
	}

	public void setType(String[] type) {
		this.type = type;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void execute() {
		open();

		new DefaultTreeItem(type).select();
		new LabeledText("Server name:").setText(name);

		finish();
	}
}