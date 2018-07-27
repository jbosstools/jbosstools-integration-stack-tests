package org.jboss.tools.drools.reddeer.kienavigator.item;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.reddeer.swt.api.TreeItem;
import org.jboss.tools.drools.reddeer.kienavigator.dialog.CreateRepositoryDialog;
import org.jboss.tools.drools.reddeer.kienavigator.dialog.Dialog;
import org.jboss.tools.drools.reddeer.kienavigator.properties.SpaceProperties;

public class SpaceItem extends Item<SpaceProperties> {

	public SpaceItem(TreeItem treeItem) {
		super(treeItem);
	}

	@Override
	public SpaceProperties properties() {
		selectAction("Properties");
		return new SpaceProperties();
	}

	public CreateRepositoryDialog createRepository() {
		selectAction("Create Repository...");
		return new CreateRepositoryDialog();
	}

	public Dialog deleteSpace() {
		selectAction("Delete Space...");
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
		throw new IllegalArgumentException("No such repository: " + name);
	}
}
