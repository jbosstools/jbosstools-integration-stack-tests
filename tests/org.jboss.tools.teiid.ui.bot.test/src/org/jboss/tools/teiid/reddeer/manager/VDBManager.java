package org.jboss.tools.teiid.reddeer.manager;

import static org.junit.Assert.assertEquals;

import org.jboss.reddeer.eclipse.jdt.ui.packageexplorer.PackageExplorer;
import org.jboss.reddeer.junit.logging.Logger;
import org.jboss.reddeer.swt.condition.JobIsRunning;
import org.jboss.reddeer.swt.wait.TimePeriod;
import org.jboss.reddeer.swt.wait.WaitWhile;
import org.jboss.tools.teiid.reddeer.VDB;
import org.jboss.tools.teiid.reddeer.editor.SQLScrapbookEditor;
import org.jboss.tools.teiid.reddeer.editor.VDBEditor;
import org.jboss.tools.teiid.reddeer.perspective.DatabaseDevelopmentPerspective;
import org.jboss.tools.teiid.reddeer.view.ModelExplorer;
import org.jboss.tools.teiid.reddeer.view.SQLResult;
import org.jboss.tools.teiid.reddeer.view.SQLResultView;
import org.jboss.tools.teiid.reddeer.view.ServersViewExt.ServerType;
import org.jboss.tools.teiid.reddeer.wizard.CreateVDB;

public class VDBManager {

	private static final Logger log = Logger.getLogger(VDBManager.class);
	
	public void createVDB(String projectName, String vdbName){
		CreateVDB createVDB = new CreateVDB();
		createVDB.setFolder(projectName);
		createVDB.setName(vdbName);
		createVDB.execute(true);
	}
	
	
	public VDBEditor getVDBEditor(String projectName, String vdbName){
		//double click to vdb
		openVDBEditor(projectName, vdbName);
		
		if (vdbName.contains(".vdb")){
			return VDBEditor.getInstance(vdbName);
		}
		return VDBEditor.getInstance(vdbName + ".vdb");
	}
	
	public void addModelsToVDB(String projectName, String vdbName, String[] models){//move to vdb editor ?
		VDBEditor editor = getVDBEditor(projectName, vdbName);
		editor.show();
		String model = "";
		try {
			for (int i = 0; i < models.length; i++) {
				model = models[i];
				editor.addModel(projectName, model);
			}
		} catch (Exception ex) {
			log.warn("Cannot add model " + model);
		}
		editor.save();
	}
	
	
	
	public VDB openVDBEditor(String projectName, String vdbName){
		if (vdbName.contains(".vdb")){
			return new ModelExplorer().getModelProject(projectName).getVDB(vdbName);
		}
		return new ModelExplorer().getModelProject(projectName).getVDB(vdbName + ".vdb");
	}
	
	public boolean isVDBCreated(String projectName, String vdbName){
		if (vdbName.contains(".vdb")){
			return new PackageExplorer().getProject(projectName).containsItem(vdbName);
		}
		return new PackageExplorer().getProject(projectName).containsItem(vdbName + ".vdb");
	}
	
	/**
	 * 
	 * @param pathToVDB [project_name, vdb_name]
	 */
	public void deployVDB(String[] pathToVDB){
		String vdb = pathToVDB[pathToVDB.length-1];
		if (!vdb.contains(".vdb")){
			vdb = vdb + ".vdb";
		}
		VDB vdbItem = new ModelExplorer().getModelProject(pathToVDB[0]).getVDB(vdb);
		vdbItem.deployVDB();
	}
	
	public boolean isVDBDeployed(String serverName, ServerType serverType, String vdbName){
		return new ServerManager().isVDBDeployed(serverName, serverType, vdbName);
	}


	public void executeVDB(Boolean viaGuides, String projectName, String vdbName) {
		VDB vdb = new ModelExplorer().getModelProject(projectName).getVDB(vdbName + ".vdb");
		vdb.executeVDB(viaGuides);
	}


	public boolean queryPassed(String vdb, String sql) {
		SQLScrapbookEditor editor = new SQLScrapbookEditor("SQL Scrapbook0");
		editor.show();
		editor.setText(sql);
		editor.executeAll(true);

		new WaitWhile(new JobIsRunning(), TimePeriod.LONG);
		
		SQLResult result = DatabaseDevelopmentPerspective.getInstance().getSqlResultsView().getByOperation(sql);
		//SQLResult result = new SQLResultView().getByOperation(sql);
		assertEquals(SQLResult.STATUS_SUCCEEDED, result.getStatus());

		editor.close();
		
		return true;//false would fail on assert
	}
}
