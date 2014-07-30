package org.jboss.tools.fuse.reddeer.projectexplorer;

import org.jboss.reddeer.eclipse.jdt.ui.ProjectExplorer;
import org.jboss.reddeer.eclipse.jdt.ui.packageexplorer.Project;
import org.jboss.reddeer.eclipse.jdt.ui.packageexplorer.ProjectItem;
import org.jboss.reddeer.swt.exception.SWTLayerException;
import org.jboss.reddeer.swt.impl.menu.ContextMenu;
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
	
	public void selectCamelContext(String name) {
		project.getProjectItem("src/main/resources", "META-INF", "spring", name).select();
	}
	
	public void runCamelContext(String name) {
		project.getProjectItem("src/main/resources", "META-INF", "spring", name).select();
		try {
			new ContextMenu("Run As", "2 Local Camel Context").select();
		} catch (SWTLayerException ex) {
			new ContextMenu("Run As", "1 Local Camel Context").select();
		}
	}
	
	public void runCamelContextWithoutTests(String name) {
		project.getProjectItem("src/main/resources", "META-INF", "spring", name).select();
		new ContextMenu("Run As", "3 Local Camel Context (without tests)").select();
	}

	public CamelProject(Project project) {
		this.project = project;
	}

	public Project getProject() {
		return project;
	}
	
	public void deleteProject() {	
		new ProjectExplorer().getProject(project.getName()).delete(true);
	}
}
