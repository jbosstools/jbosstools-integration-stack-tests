package org.jboss.tools.modeshape.reddeer.view;

import org.jboss.reddeer.eclipse.jdt.ui.ProjectExplorer;
import org.jboss.reddeer.swt.impl.menu.ContextMenu;
import org.jboss.reddeer.swt.impl.shell.DefaultShell;
import org.jboss.reddeer.swt.impl.shell.WorkbenchShell;
import org.jboss.tools.modeshape.reddeer.shell.PublishedLocations;
import org.jboss.tools.modeshape.reddeer.wizard.ModeshapePublishWizard;

/**
 * ModeShape Explorer is an imaginary explorer (doesn't exist) which can used for
 * publishing/unpublish to the repository.
 * 
 * @author apodhrad
 * 
 */
public class ModeshapeExplorer extends ProjectExplorer {

	public ModeshapePublishWizard publish(String project, String... item) {
		selectItem(project, item);
		getProject(project).getProjectItem(item).select();
		new ContextMenu("ModeShape", "Publish").select();
		new DefaultShell("Publish to ModeShape");
		return new ModeshapePublishWizard();
	}

	public ModeshapePublishWizard unpublish(String project, String... item) {
		selectItem(project, item);
		getProject(project).getProjectItem(item).select();
		new ContextMenu("ModeShape", "Unpublish").select();
		new DefaultShell("Unpublish from ModeShape");
		return new ModeshapePublishWizard();
	}

	public PublishedLocations showPublishedLocations(String project, String... item) {
		selectItem(project, item);
		new ContextMenu("ModeShape", "Show Published Locations").select();
		new DefaultShell("Published Locations");
		return new PublishedLocations();
	}
	
	public void selectItem(String project, String... item) {
		new WorkbenchShell();
		open();
		getProject(project).getProjectItem(item).select();
	}
	
}
