package org.jboss.tools.teiid.reddeer.manager;

import org.jboss.tools.teiid.reddeer.ModelProject;
import org.jboss.tools.teiid.reddeer.wizard.FlatImportWizard;
import org.jboss.tools.teiid.reddeer.wizard.TeiidImportWizard;

public class ImportManager {

	/**
	 * <ul>
	 * <li>Firstly, you have to create FlatImportWizard and set properties to it<br /></li>
	 * <li>Secondly, you have to select the project to which you want to import - e.g. via ModelProjectManager</li>
	 * </ul> 
	 * Import source and view model from flat file data source into the project
	 */
	public void importModel(FlatImportWizard wizard, String project){
		new ModelProjectManager().selectModelProject(project);
		wizard.execute2();
	}
	
	
	/**
	 * <ul>
	 * <li>Firstly, you have to create TeiidImportWizard (parent to FlatImportWizard,...) and set properties to it<br /></li>
	 * <li>Secondly, you have to select the project to which you want to import</li>
	 * </ul> 
	 * Import source and view model from flat file data source into the project
	 */
	public void importModel(TeiidImportWizard wizard, ModelProject project){
		project.importModel(wizard);
	}
}
