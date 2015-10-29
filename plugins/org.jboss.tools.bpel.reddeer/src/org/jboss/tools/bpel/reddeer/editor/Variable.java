package org.jboss.tools.bpel.reddeer.editor;

import org.jboss.reddeer.common.wait.WaitWhile;
import org.jboss.reddeer.core.condition.ShellWithTextIsAvailable;
import org.jboss.reddeer.eclipse.ui.views.properties.PropertiesView;
import org.jboss.reddeer.swt.api.TreeItem;
import org.jboss.reddeer.swt.impl.button.CheckBox;
import org.jboss.reddeer.swt.impl.button.OkButton;
import org.jboss.reddeer.swt.impl.button.PushButton;
import org.jboss.reddeer.swt.impl.shell.DefaultShell;
import org.jboss.reddeer.swt.impl.table.DefaultTableItem;

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
		new PropertiesView().open();
		treeItem.select();
	}

	public Variable setType(String filter, String name) {
		activate();
		new PropertiesView().selectTab("Details");
		new PushButton("Browse...").click();
		new DefaultShell("Choose type of variable");
		new CheckBox(filter).toggle(true);
		new DefaultTableItem(name).select();
		new OkButton().click();
		new WaitWhile(new ShellWithTextIsAvailable("Choose type of a variable"));
		return this;
	}

}
