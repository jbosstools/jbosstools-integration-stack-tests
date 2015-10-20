package org.jboss.tools.drools.reddeer.kienavigator.item;

import org.jboss.reddeer.swt.api.TreeItem;
import org.jboss.reddeer.swt.impl.menu.ContextMenu;
import org.jboss.tools.drools.reddeer.kienavigator.properties.Properties;

public abstract class Item<T extends Properties> {
	
	protected TreeItem treeItem;

	public Item(TreeItem treeItem) {
		this.treeItem = treeItem;
	}

	public void refresh() {
		selectAction("Refresh");
	}
	
	protected void selectAction(String actionName) {
		treeItem.select();
		new ContextMenu(actionName).select();
	}
	
	public abstract T properties();
	
	public String getName() {
		return treeItem.getText();
	}
}
