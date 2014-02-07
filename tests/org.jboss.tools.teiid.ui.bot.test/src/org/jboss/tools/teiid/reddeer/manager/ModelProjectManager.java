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
	
	/**
	 * New Teiid Model Project
	 * @param modelProjectName
	 */
	public void create(String modelProjectName, boolean viaGuides){
		new ModelProjectWizard(0).create(modelProjectName, viaGuides);
	}
	
	public void create(String modelProjectName){
		new ModelProjectWizard(0).create(modelProjectName);
	}
	
	public void create(String modelProjectName, boolean viaGuides, String... folders){
		new ModelProjectWizard(0).create(modelProjectName, viaGuides, folders);
	}
	
	
}