package org.jboss.tools.drools.reddeer.kienavigator.item;

import java.util.ArrayList;
import java.util.List;

import org.jboss.reddeer.swt.api.TreeItem;
import org.jboss.tools.drools.reddeer.kienavigator.dialog.AddRepositoryDialog;
import org.jboss.tools.drools.reddeer.kienavigator.dialog.CreateRepositoryDialog;
import org.jboss.tools.drools.reddeer.kienavigator.dialog.Dialog;
import org.jboss.tools.drools.reddeer.kienavigator.properties.OrgUnitProperties;

public class OrgUnitItem extends Item<OrgUnitProperties> {
	
	public OrgUnitItem(TreeItem treeItem) {
		super(treeItem);
	}
	
	@Override
	public OrgUnitProperties properties() {
		selectAction("Properties");
		return new OrgUnitProperties();
	}
	
	public AddRepositoryDialog addRepository() {
		selectAction("Add Repository...");
		return new AddRepositoryDialog();
	}
	
	public CreateRepositoryDialog createRepository() {
		selectAction("Create Repository...");
		return new CreateRepositoryDialog();
	}
	
	public Dialog deleteOrganization() {
		selectAction("Delete Organization...");
		return new Dialog();
	}
	
	public List<RepositoryItem> getRepositories() {
		expand();
		List<TreeItem> treeItemsList = getItems();
		List<RepositoryItem> repositoryItemsList = new ArrayList<RepositoryItem>();
		for (TreeItem item : treeItemsList) {
			repositoryItemsList.add(new RepositoryItem(item));
		}
		return repositoryItemsList;
	}
	
	public RepositoryItem getRepository(String name) {
		expand();
		List<TreeItem> items = getItems();
		for (TreeItem item : items) {
			if (item.getText().equals(name)) {
				return new RepositoryItem(item);
			}
		}
		throw new IllegalArgumentException("No such organization unit: " + name);
	}
}
