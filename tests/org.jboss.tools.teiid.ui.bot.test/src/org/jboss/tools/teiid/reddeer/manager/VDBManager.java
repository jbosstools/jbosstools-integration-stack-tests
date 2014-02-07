package org.jboss.tools.teiid.reddeer.manager;

import org.jboss.tools.teiid.reddeer.VDB;
import org.jboss.tools.teiid.reddeer.editor.VDBEditor;
import org.jboss.tools.teiid.reddeer.view.ModelExplorer;
import org.jboss.tools.teiid.reddeer.wizard.CreateVDB;

/**
 * <ul>
 * <li>1. Create VDB</li>
 * <li>2. Add models via VDB editor</li>
 * <li>3. Deploy and execute VDB</li>
 * </ul>
 */
public class VDBManager {

	/**
	 * <ul>
	 * <li><b>1. Create VDB</b></li>
	 * <li>2. Add models via VDB editor</li>
	 * <li>3. Deploy and execute VDB</li>
	 * </ul>
	 * @param projectName destination project
	 * @param vdbName
	 */
	public void createVDB(String projectName, String vdbName){
		CreateVDB createVDB = new CreateVDB();
		createVDB.setFolder(projectName);
		createVDB.setName(vdbName);
		createVDB.execute(true);
	}
	
	/**
	 * <ul>
	 * <li>1. Create VDB</li>
	 * <li><b>Get VDB Editor -- </b>2. Add models via VDB editor</li>
	 * <li>3. Deploy and execute VDB</li>
	 * </ul>
	 * @param vdbName
	 * @return
	 */
	public VDBEditor openVDBEditor(String vdbName){
		return VDBEditor.getInstance(vdbName + ".vdb");
	}
	
	/**
	 * /**
	 * <ul>
	 * <li>1. Create VDB</li>
	 * <li>2. Add models to VDB</li>
	 * <li><b>Get VDB -- </b>3. Deploy and execute VDB</li>
	 * </ul>
	 * @param projectName
	 * @param vdbName
	 * @return
	 */
	public VDB getVDB(String projectName, String vdbName){
		return new ModelExplorer().getModelProject(projectName).getVDB(vdbName + ".vdb");
	}
}
