package org.jboss.tools.drools.reddeer.kienavigator.item;

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
		selectAction("Import Repository");
	}
	
	public CreateProjectDialog createProject() {
		selectAction("Create Project");
		return new CreateProjectDialog();
	}
	
	public RemoveRepositoryDialog removeRepository() {
		selectAction("Remove Repository...");
		return new RemoveRepositoryDialog();
	}
	
	public void showInGitRepositoryView() {
		selectAction("Show in Git Repository View");
	}
}
