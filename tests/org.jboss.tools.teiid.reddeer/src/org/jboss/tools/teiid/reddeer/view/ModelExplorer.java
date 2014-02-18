package org.jboss.tools.teiid.reddeer.view;

import org.eclipse.swtbot.eclipse.finder.SWTWorkbenchBot;
import org.jboss.reddeer.eclipse.jdt.ui.AbstractExplorer;
import org.jboss.reddeer.swt.impl.button.PushButton;
import org.jboss.reddeer.swt.impl.menu.ContextMenu;
import org.jboss.reddeer.swt.impl.tree.DefaultTreeItem;
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
		wizard.open();
		wizard.getWizardPage().fillWizardPage(modelName);
		wizard.finish();
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
		new ModelExplorer().getProject(projectName).getProjectItem(projectItem).select();
		new ContextMenu("Modeling", "Set Connection Profile").select();
		new DefaultTreeItem("Database Connections", connectionProfile).select();
		new PushButton("OK").click();
		//Confirm Connection Profile Change (it will change also the model import settings)
		if (new SWTWorkbenchBot().activeShell().getText().equals(CONNECTION_PROFILE_CHANGE)){
			new PushButton("OK").click();
		}
	}

}
