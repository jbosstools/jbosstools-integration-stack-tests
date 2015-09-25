package org.jboss.tools.switchyard.reddeer.properties;

import org.jboss.reddeer.common.wait.WaitWhile;
import org.jboss.reddeer.core.condition.ShellWithTextIsAvailable;
import org.jboss.reddeer.swt.impl.button.OkButton;
import org.jboss.reddeer.swt.impl.button.PushButton;
import org.jboss.reddeer.swt.impl.shell.DefaultShell;
import org.jboss.reddeer.swt.impl.tab.DefaultTabItem;
import org.jboss.reddeer.swt.impl.tree.DefaultTree;
import org.jboss.reddeer.swt.impl.tree.DefaultTreeItem;

/**
 * 
 * @author apodhrad
 *
 */
public class ProjectPropertiesJavaBuildPath {

	public ProjectPropertiesJavaBuildPath() {
		new DefaultShell();
	}

	public ProjectPropertiesJavaBuildPath(String projectName) {
		new DefaultShell("Properties for " + projectName);
	}

	public ProjectPropertiesJavaBuildPath selectLibrariesTab() {
		new DefaultTabItem("Libraries").activate();
		return this;
	}
	
	public ProjectPropertiesJavaBuildPath selectLibrary(String library) {
		selectLibrariesTab();
		new DefaultTreeItem(new DefaultTree(1), library).select();
		return this;
	}

	public EditLibraryPage edit() {
		new PushButton("Edit...").click();
		return new EditLibraryPage();
	}
	
	public void ok() {
		String title = new DefaultShell().getText();
		new OkButton().click();
		new WaitWhile(new ShellWithTextIsAvailable(title));
	}
}
