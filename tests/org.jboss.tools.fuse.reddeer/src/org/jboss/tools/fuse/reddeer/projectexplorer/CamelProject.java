package org.jboss.tools.fuse.reddeer.projectexplorer;

import org.jboss.reddeer.eclipse.jdt.ui.ProjectExplorer;
import org.jboss.reddeer.eclipse.jdt.ui.packageexplorer.Project;
import org.jboss.reddeer.eclipse.jdt.ui.packageexplorer.ProjectItem;
import org.jboss.tools.fuse.reddeer.wizard.CamelXmlFileWizard;

/**
 * 
 * @author apodhrad
 * 
 */
public class CamelProject {

	private Project project;

	public CamelProject(String name) {
		project = new ProjectExplorer().getProject(name);
	}

	public void openCamelContext(String name) {
		ProjectItem item = project.getProjectItem("src/main/resources", "META-INF", "spring");
		item.getChild(name).open();
	}

	public void deleteCamelContext(String name) {
		project.getProjectItem("src/main/resources", "META-INF", "spring", name).delete();
	}

	public void createCamelContext(String name) {
		project.getProjectItem("src/main/resources", "META-INF", "spring").select();
		new CamelXmlFileWizard().openWizard().setName(name).finish();
	}

	public CamelProject(Project project) {
		this.project = project;
	}

	public Project getProject() {
		return project;
	}
}
