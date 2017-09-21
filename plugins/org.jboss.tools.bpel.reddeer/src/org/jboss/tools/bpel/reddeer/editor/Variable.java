package org.jboss.tools.bpel.reddeer.editor;

import org.eclipse.reddeer.common.wait.WaitWhile;
import org.eclipse.reddeer.eclipse.ui.views.properties.PropertySheet;
import org.eclipse.reddeer.swt.api.TreeItem;
import org.eclipse.reddeer.swt.condition.ShellIsAvailable;
import org.eclipse.reddeer.swt.impl.button.CheckBox;
import org.eclipse.reddeer.swt.impl.button.OkButton;
import org.eclipse.reddeer.swt.impl.button.PushButton;
import org.eclipse.reddeer.swt.impl.shell.DefaultShell;
import org.eclipse.reddeer.swt.impl.table.DefaultTableItem;

/**
 * 
 * @author apodhrad
 *
 */
public class Variable {

	protected TreeItem treeItem;

	Variable(TreeItem treeItem) {
		this.treeItem = treeItem;
	}

	public void activate() {
		new PropertySheet().open();
		treeItem.select();
	}

	public Variable setType(String filter, String name) {
		activate();
		new PropertySheet().selectTab("Details");
		new PushButton("Browse...").click();
		new DefaultShell("Choose type of variable");
		new CheckBox(filter).toggle(true);
		new DefaultTableItem(name).select();
		new OkButton().click();
		new WaitWhile(new ShellIsAvailable("Choose type of a variable"));
		return this;
	}

}
