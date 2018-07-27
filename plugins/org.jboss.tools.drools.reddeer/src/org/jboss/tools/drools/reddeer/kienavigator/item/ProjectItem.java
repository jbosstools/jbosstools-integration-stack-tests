package org.jboss.tools.drools.reddeer.kienavigator.item;

import org.eclipse.reddeer.swt.api.TreeItem;
import org.jboss.tools.drools.reddeer.kienavigator.properties.ProjectProperties;

public class ProjectItem extends Item<ProjectProperties> {

	public ProjectItem(TreeItem treeItem) {
		super(treeItem);
	}

	@Override
	public ProjectProperties properties() {
		selectAction("Properties");
		return new ProjectProperties();
	}

	public void importProject() {
		selectAction("Import Project");
	}
}
