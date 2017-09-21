package org.jboss.tools.drools.reddeer.kienavigator.item;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.reddeer.swt.api.TreeItem;
import org.jboss.tools.drools.reddeer.kienavigator.dialog.CreateOrgUnitDialog;
import org.jboss.tools.drools.reddeer.kienavigator.properties.ServerProperties;

public class ServerItem extends Item<ServerProperties> {

	public ServerItem(TreeItem treeItem) {
		super(treeItem);
	}

	public ServerProperties properties() {
		selectAction("Properties");
		return new ServerProperties();
	}

	public CreateOrgUnitDialog createOrgUnit() {
		selectAction("Create Organization...");
		return new CreateOrgUnitDialog();
	}

	public List<OrgUnitItem> getOrgUnits() {
		expand();
		List<TreeItem> treeItemList = getItems();
		List<OrgUnitItem> orgUnitItemsList = new ArrayList<OrgUnitItem>();
		for (TreeItem item : treeItemList) {
			orgUnitItemsList.add(new OrgUnitItem(item));
		}
		return orgUnitItemsList;
	}

	public OrgUnitItem getOrgUnit(String name) {
		expand();
		List<TreeItem> treeItemList = getItems();
		for (TreeItem item : treeItemList) {
			if (item.getText().equals(name)) {
				return new OrgUnitItem(item);
			}
		}
		throw new IllegalArgumentException("No such organization unit: " + name);
	}
}
