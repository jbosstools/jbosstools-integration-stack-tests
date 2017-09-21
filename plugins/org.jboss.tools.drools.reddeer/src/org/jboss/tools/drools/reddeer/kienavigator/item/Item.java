package org.jboss.tools.drools.reddeer.kienavigator.item;

import java.util.List;

import org.apache.log4j.Logger;
import org.eclipse.reddeer.common.wait.WaitWhile;
import org.eclipse.reddeer.swt.api.TreeItem;
import org.eclipse.reddeer.swt.impl.button.PushButton;
import org.eclipse.reddeer.swt.impl.menu.ContextMenuItem;
import org.jboss.tools.drools.reddeer.kienavigator.properties.Properties;
import org.jboss.tools.drools.reddeer.waitcondition.LoadingTextIsVisible;

public abstract class Item<T extends Properties> {

	private static final Logger LOGGER = Logger.getLogger(Item.class);

	private TreeItem treeItem;

	public Item(TreeItem treeItem) {
		this.treeItem = treeItem;
	}

	public void refresh() {
		selectAction("Refresh");
	}

	protected void selectAction(String actionName) {
		treeItem.select();
		new ContextMenuItem(actionName).select();
	}

	protected void selectEnabledAction(String actionName) {
		treeItem.select();
		ContextMenuItem cm = new ContextMenuItem(actionName);
		if (cm.isEnabled()) {
			cm.select();
			new PushButton("Yes").click();
		}
	}

	public abstract T properties();

	public String getName() {
		return treeItem.getText();
	}

	public List<TreeItem> getItems() {
		return treeItem.getItems();
	}

	public void expand() {
		treeItem.expand();
		try {
			refresh();
		} catch (Exception e) {
			LOGGER.debug("A problem with refresh after treeitem is expanded: " + e.getMessage());
		}
		new WaitWhile(new LoadingTextIsVisible(treeItem));
	}
}
