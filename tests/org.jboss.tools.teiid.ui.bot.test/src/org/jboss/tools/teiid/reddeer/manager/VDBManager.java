package org.jboss.tools.teiid.reddeer.manager;

import static org.junit.Assert.assertEquals;

import java.util.Properties;

import org.eclipse.swtbot.eclipse.finder.SWTWorkbenchBot;
import org.jboss.reddeer.eclipse.jdt.ui.packageexplorer.PackageExplorer;
import org.jboss.reddeer.common.logging.Logger;
import org.jboss.reddeer.swt.condition.JobIsRunning;
import org.jboss.reddeer.swt.impl.button.CheckBox;
import org.jboss.reddeer.swt.impl.button.PushButton;
import org.jboss.reddeer.swt.impl.menu.ContextMenu;
import org.jboss.reddeer.swt.impl.shell.WorkbenchShell;
import org.jboss.reddeer.swt.impl.text.DefaultText;
import org.jboss.reddeer.swt.wait.TimePeriod;
import org.jboss.reddeer.swt.wait.WaitWhile;
import org.jboss.tools.teiid.reddeer.VDB;
import org.jboss.tools.teiid.reddeer.WAR;
import org.jboss.tools.teiid.reddeer.editor.SQLScrapbookEditor;
import org.jboss.tools.teiid.reddeer.editor.VDBEditor;
import org.jboss.tools.teiid.reddeer.view.DataSourceExplorer;
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
	
	// TODO CHECK 
	public void removeModelFromVDB(String projectName, String vdbName, String[] models){
		VDBEditor editor = getVDBEditor(projectName, vdbName);
		editor.show();
		String model = "";
		try {
			for (int i = 0; i < models.length; i++) {
				model = models[i];
				editor.removeModel(projectName, model);
			}
		} catch (Exception ex) {
			log.warn("Cannot add model " + model);
		}
		editor.save();
	}
	
	public void openVDBEditor(String projectName, String vdbName){
		if (vdbName.contains(".vdb")){
			new ModelExplorer().getModelProject(projectName).open(vdbName);
		} else {
			new ModelExplorer().getModelProject(projectName).open(vdbName + ".vdb");
		}
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

		new WorkbenchShell();
		new DataSourceExplorer().openSQLScrapbook(vdb);
		SQLScrapbookEditor editor = new SQLScrapbookEditor();
		editor.show();
		editor.setText(sql);
		editor.executeAll();

		new WaitWhile(new JobIsRunning(), TimePeriod.LONG);
		
		SQLResultView resView = new SQLResultView();
		resView.open();
		SQLResult result = resView.getByOperation(sql);
		
		assertEquals(SQLResult.STATUS_SUCCEEDED, result.getStatus());

		editor.close();
		
		return true;
	}
	
	public void createVDBDataSource(String[] pathToVDB){
		String vdb =  pathToVDB[pathToVDB.length-1];
		if (pathToVDB[pathToVDB.length-1].contains(".vdb")){
			vdb = pathToVDB[pathToVDB.length-1].substring(0, pathToVDB[pathToVDB.length-1].indexOf("."));
		}
		
		createVDBDataSource(pathToVDB, vdb, false);
	}
	
	public void createVDBDataSource(String[] pathToVDB, String jndiName, boolean passThruAuth){
		
		if (! pathToVDB[pathToVDB.length-1].contains(".vdb")){
			pathToVDB[pathToVDB.length-1] = pathToVDB[pathToVDB.length-1].concat(".vdb");
		}
		new ModelExplorer().getProject(pathToVDB[0]).getProjectItem(pathToVDB[1]).select();
		new ContextMenu("Modeling", "Create VDB Data Source").select();
		try{
			if (new SWTWorkbenchBot().activeShell().getText().equals("VDB Not Yet Deployed ")){
				new PushButton("Yes").click();//create ds anyway
			}
		} catch (Exception e){
			
		}
		new DefaultText(1).setText(jndiName);
		if (passThruAuth){
			new CheckBox("Pass Thru Authentication").click();
		}
		new PushButton("OK").click();
	}
	
	/*public void synchronizeAll(String... pathToVDB){
		// TODO
		this.getVDBEditor(pathToVDB[0], pathToVDB[1]);
		new PushButton("Synchronize All").click();
	}*/
	
	/*public WAR operateWAR(String... pathToWar){
		return new WAR(new ModelExplorer().getProject(pathToWar[0]).getProjectItem(pathToWar[1]));
	}*/
	
	/*public WAR createWAR(Properties warProps, String... pathToVDB){
		return new WAR(warProps, new ModelExplorer().getProject(pathToVDB[0]).getProjectItem(pathToVDB[1]));
	}*/
	
	public WAR createWAR(Properties warProps, String... pathToVDB){
		WAR war =new WAR(warProps, pathToVDB);
		war.createWAR();
		return war;
	}
}
