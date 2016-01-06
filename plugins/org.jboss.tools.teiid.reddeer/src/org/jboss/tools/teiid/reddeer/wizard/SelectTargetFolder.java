package org.jboss.tools.teiid.reddeer.wizard;

import org.jboss.reddeer.swt.impl.button.OkButton;
import org.jboss.reddeer.swt.impl.shell.DefaultShell;
import org.jboss.reddeer.swt.impl.tree.DefaultTreeItem;

public class SelectTargetFolder {
	private static final String SELECT_FOLDER = "Select a Folder";

	public SelectTargetFolder() {
		new DefaultShell(SELECT_FOLDER);
	}

	public void select(String... path) {
		new DefaultTreeItem(path).select();
		new OkButton().click();
	}
}
