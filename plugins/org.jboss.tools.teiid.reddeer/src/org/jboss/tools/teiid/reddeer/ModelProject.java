package org.jboss.tools.teiid.reddeer;

import java.io.File;

import org.eclipse.core.resources.ResourcesPlugin;
import org.jboss.reddeer.eclipse.core.resources.Project;

/**
 * This class represents a model project.
 * 
 * @author apodhrad
 * 
 */
public class ModelProject {

	private Project project;

	public ModelProject(Project project) {
		this.project = project;
	}

	public void open(String... path) {
		project.getProjectItem(path).open();
	}

	public File getFile() {
		String wsPath = ResourcesPlugin.getWorkspace().getRoot().getLocationURI().getPath();
		return new File(wsPath, project.getName());
	}

}
