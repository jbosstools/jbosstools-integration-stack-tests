package org.jboss.tools.drools.reddeer.kienavigator.item;

import java.util.ArrayList;
import java.util.List;

import org.jboss.reddeer.swt.api.TreeItem;
import org.jboss.tools.drools.reddeer.kienavigator.dialog.CreateProjectDialog;
import org.jboss.tools.drools.reddeer.kienavigator.dialog.RemoveRepositoryDialog;
import org.jboss.tools.drools.reddeer.kienavigator.properties.RepositoryProperties;

public class RepositoryItem extends Item<RepositoryProperties> {

	public RepositoryItem(TreeItem treeItem) {
		super(treeItem);
	}

	@Override
	public RepositoryProperties properties() {
		selectAction("Properties");
		return new RepositoryProperties();
	}

	public void importRepository() {
		selectEnabledAction("Import Repository");
	}
	
	public CreateProjectDialog createProject() {
		selectAction("Create Project...");
		return new CreateProjectDialog();
	}
	
	public RemoveRepositoryDialog removeRepository() {
		selectAction("Remove Repository...");
		return new RemoveRepositoryDialog();
	}
	
	public void showInGitRepositoryView() {
		selectAction("Show in Git Repository View");
	}
	
	public List<ProjectItem> getProjects() {
		expand();
		List<TreeItem> treeItemsList = getItems();
		List<ProjectItem> projecItemsList = new ArrayList<ProjectItem>();
		for (TreeItem item : treeItemsList) {
			projecItemsList.add(new ProjectItem(item));
		}
		return projecItemsList;
	}
	
	public ProjectItem getProject(String name) {
		expand();
		List<TreeItem> items = getItems();
		for (TreeItem item : items) {
			if (item.getText().equals(name)) {
				return new ProjectItem(item);
			}
		}
		throw new IllegalArgumentException("No such project: " + name);
	}
}
