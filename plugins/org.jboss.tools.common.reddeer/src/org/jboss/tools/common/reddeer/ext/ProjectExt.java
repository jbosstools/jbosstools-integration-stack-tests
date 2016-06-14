package org.jboss.tools.common.reddeer.ext;

import org.eclipse.core.resources.ResourcesPlugin;
import org.jboss.reddeer.direct.project.Project;

/**
 * @author tsedmik
 */
public class ProjectExt extends Project {

	public String getLocation(String projectName) {
		return ResourcesPlugin.getWorkspace().getRoot().getProject(projectName).getLocation().toString();
	}
}
