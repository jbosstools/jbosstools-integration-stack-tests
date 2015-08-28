package org.jboss.tools.teiid.reddeer.view;

import org.jboss.reddeer.eclipse.jdt.ui.AbstractExplorer;
import org.jboss.reddeer.core.condition.ShellWithTextIsAvailable;
import org.jboss.reddeer.swt.impl.button.PushButton;
import org.jboss.reddeer.swt.impl.menu.ContextMenu;
import org.jboss.reddeer.swt.impl.shell.DefaultShell;
import org.jboss.reddeer.swt.impl.tree.DefaultTreeItem;
import org.jboss.reddeer.common.wait.WaitUntil;
import org.jboss.tools.teiid.reddeer.ModelProject;
import org.jboss.tools.teiid.reddeer.wizard.ModelProjectWizard;

/**
 * This class represents a model explorer
 * 
 * @author apodhrad
 * 
 */
public class ModelExplorer extends AbstractExplorer {

	private static String CONNECTION_PROFILE_CHANGE = "Confirm Connection Profile Change";
	public ModelExplorer() {
		super("Model Explorer");
	}

	/**
	 * Creates a new model project
	 * 
	 * @param modelName
	 * @return
	 */
	public ModelProject createModelProject(String modelName) {
		ModelProjectWizard wizard = new ModelProjectWizard(0);
		wizard.create(modelName);
		return getModelProject(modelName);
	}

	/**
	 * Returns a model project with a given name
	 * 
	 * @param modelName
	 * @return
	 */
	public ModelProject getModelProject(String modelName) {
		return new ModelProject(getProject(modelName));
	}
	
	public void changeConnectionProfile(String connectionProfile, String projectName, String... projectItem){
		//if no extension specified, add .xmi
		if (! projectItem[projectItem.length -1].contains(".")){
			projectItem[projectItem.length -1] = projectItem[projectItem.length -1].concat(".xmi");
		}
		new DefaultShell();
		new ModelExplorer().getProject(projectName).getProjectItem(projectItem).select();
		new ContextMenu("Modeling", "Set Connection Profile").select();
		new DefaultShell("Set Connection Profile");
		new DefaultTreeItem("Database Connections", connectionProfile).select();
		new PushButton("OK").click();

		try {
			new WaitUntil(new ShellWithTextIsAvailable(CONNECTION_PROFILE_CHANGE));
			new PushButton("OK").click();
		} catch (Exception e) {
		}
	}

}
