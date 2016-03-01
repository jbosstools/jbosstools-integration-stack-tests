package org.jboss.tools.drools.reddeer.kienavigator.item;

import java.util.ArrayList;
import java.util.List;

import org.jboss.reddeer.swt.api.Shell;
import org.jboss.reddeer.swt.api.TreeItem;
import org.jboss.reddeer.swt.impl.button.PushButton;
import org.jboss.reddeer.swt.impl.shell.DefaultShell;
import org.jboss.tools.drools.reddeer.kienavigator.dialog.CreateOrgUnitDialog;
import org.jboss.tools.drools.reddeer.kienavigator.properties.ServerProperties;

public class ServerItem extends Item<ServerProperties> {

	public ServerItem(TreeItem treeItem) {
		super(treeItem);
	}

	public ServerProperties properties() {
		selectAction("Properties");
		
		//DROOLS-1076 workaround
		Shell shell = new DefaultShell("Could Not Accept Changes ");
		if (shell.isVisible()) {
			new PushButton("OK").click();
		}
		
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
