package org.jboss.tools.drools.reddeer.kienavigator.item;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.reddeer.swt.api.TreeItem;
import org.jboss.tools.drools.reddeer.kienavigator.dialog.CreateSpaceDialog;
import org.jboss.tools.drools.reddeer.kienavigator.properties.ServerProperties;

public class ServerItem extends Item<ServerProperties> {

	public ServerItem(TreeItem treeItem) {
		super(treeItem);
	}

	public ServerProperties properties() {
		selectAction("Properties");
		return new ServerProperties();
	}

	public CreateSpaceDialog createSpace() {
		selectAction("Create Space...");
		return new CreateSpaceDialog();
	}

	public List<SpaceItem> getSpaces() {
		expand();
		List<TreeItem> treeItemList = getItems();
		List<SpaceItem> spaceItemsList = new ArrayList<SpaceItem>();
		for (TreeItem item : treeItemList) {
			spaceItemsList.add(new SpaceItem(item));
		}
		return spaceItemsList;
	}

	public SpaceItem getSpace(String name) {
		expand();
		List<TreeItem> treeItemList = getItems();
		for (TreeItem item : treeItemList) {
			if (item.getText().equals(name)) {
				return new SpaceItem(item);
			}
		}
		throw new IllegalArgumentException("No such space: " + name);
	}
}
