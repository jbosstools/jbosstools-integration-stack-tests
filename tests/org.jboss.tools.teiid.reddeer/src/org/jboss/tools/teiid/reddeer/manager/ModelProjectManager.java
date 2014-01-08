package org.jboss.tools.teiid.reddeer.manager;

import org.jboss.tools.teiid.reddeer.ModelProject;
import org.jboss.tools.teiid.reddeer.view.ModelExplorer;
import org.jboss.tools.teiid.reddeer.wizard.ModelProjectWizard;

public class ModelProjectManager {

	/**
	 * Click on the project in Model Explorer  
	 * @return
	 */
	public ModelProject selectModelProject(String modelProjectName){
		ModelExplorer me = new ModelExplorer();
		me.open();
		return me.getModelProject(modelProjectName); 
	}
	
	public void createNewModelProject(String modelProjectName){
		new ModelProjectWizard().create(modelProjectName);
	}
}
