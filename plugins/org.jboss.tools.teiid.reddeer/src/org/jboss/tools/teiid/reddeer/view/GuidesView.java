package org.jboss.tools.teiid.reddeer.view;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Widget;
import org.eclipse.swtbot.eclipse.finder.SWTWorkbenchBot;
import org.jboss.reddeer.common.wait.AbstractWait;
import org.jboss.reddeer.common.wait.TimePeriod;
import org.jboss.reddeer.common.wait.WaitWhile;
import org.jboss.reddeer.core.condition.JobIsRunning;
import org.jboss.reddeer.core.lookup.WidgetLookup;
import org.jboss.reddeer.core.matcher.TreeItemRegexMatcher;
import org.jboss.reddeer.core.util.Display;
import org.jboss.reddeer.eclipse.wst.server.ui.view.Server;
import org.jboss.reddeer.eclipse.wst.server.ui.view.ServersView;
import org.jboss.reddeer.jface.wizard.WizardDialog;
import org.jboss.reddeer.swt.impl.button.PushButton;
import org.jboss.reddeer.swt.impl.combo.DefaultCombo;
import org.jboss.reddeer.swt.impl.combo.LabeledCombo;
import org.jboss.reddeer.swt.impl.group.DefaultGroup;
import org.jboss.reddeer.swt.impl.menu.ShellMenu;
import org.jboss.reddeer.swt.impl.shell.DefaultShell;
import org.jboss.reddeer.swt.impl.table.DefaultTable;
import org.jboss.reddeer.swt.impl.text.DefaultText;
import org.jboss.reddeer.swt.impl.text.LabeledText;
import org.jboss.reddeer.swt.impl.tree.DefaultTreeItem;
import org.jboss.reddeer.swt.keyboard.KeyboardFactory;
import org.jboss.reddeer.workbench.impl.editor.DefaultEditor;
import org.jboss.reddeer.workbench.impl.view.WorkbenchView;
import org.jboss.tools.teiid.reddeer.WAR;
import org.jboss.tools.teiid.reddeer.condition.IsInProgress;
import org.jboss.tools.teiid.reddeer.condition.IsPreviewInProgress;
import org.jboss.tools.teiid.reddeer.connection.TeiidJDBCHelper;
import org.jboss.tools.teiid.reddeer.editor.VDBEditor;
import org.jboss.tools.teiid.reddeer.perspective.DatabaseDevelopmentPerspective;
import org.jboss.tools.teiid.reddeer.requirement.TeiidServerRequirement;
import org.jboss.tools.teiid.reddeer.wizard.CreateVDB;
import org.jboss.tools.teiid.reddeer.wizard.ModelProjectPage;

public class GuidesView extends WorkbenchView {

	public GuidesView() {
		super("Teiid Designer", "Guides");
	}

	/**
	 * Choose specific action from action set.
	 * 
	 * @param actionSet
	 * @param action
	 */
	@SuppressWarnings("unchecked")
	public void chooseAction(String actionSet, String action) {
		open();

		new DefaultCombo().setSelection(actionSet);
		new DefaultTreeItem(new TreeItemRegexMatcher(action + ".*")).doubleClick();
	}

	/**
	 * Preview data (table) via guides action
	 * 
	 * @param calledFirstTime
	 *            true - sets up display property of unresolvable SQL results
	 * @param path
	 *            to table (e.g. "ProjectName", "ModelName", "TABLE")
	 */
	/*
	 * public void previewData(boolean calledFirstTime, String... path){//just try-catch, no boolean param new
	 * GuidesView().chooseAction("Model JDBC Source", "Preview Data"); new SWTWorkbenchBot().button("...").click(); new
	 * DefaultTreeItem(path).select(); new SWTWorkbenchBot().button("OK").click(); new
	 * SWTWorkbenchBot().button("OK").click();
	 * 
	 * //setup display property; only 1st time try { //what property? new
	 * SWTWorkbenchBot().activeShell().bot().button("Yes").click(); } catch (Exception ex){ //do nothing }
	 * 
	 * new WaitWhile(new IsInProgress(), TimePeriod.LONG); }
	 */

	/**
	 * 
	 * @param path
	 *            to table
	 */
	public void previewData(String... path) {// just try-catch, no boolean param
		previewDataViaActionSet("Model JDBC Source",path);
	}

	public boolean canPreviewData(String expectedErrorMessage, String[] pathToTable, String previewSQL) {// nechat
																											// tak??? OR
																											// ???guides
																											// mgr (msg,
																											// string[]
																											// pathTotable,
																											// )
		if (expectedErrorMessage != null) {// OR to guides view
			new GuidesView().chooseAction("Model JDBC Source", "Preview Data");
			new WaitWhile(new JobIsRunning(), TimePeriod.LONG);
			assertEquals(new SWTWorkbenchBot().activeShell().getText(), expectedErrorMessage);
			new SWTWorkbenchBot().activeShell().close();
			return false;
		} else {
			new GuidesView().previewData(pathToTable);
			// SQLResult result = new SQLResultView().getByOperation(previewSQL);
			SQLResult result = DatabaseDevelopmentPerspective.getInstance().getSqlResultsView()
					.getByOperation(previewSQL);// "select * from
												// \""+MODEL_NAME.substring(0,MODEL_NAME.indexOf("."))+"\".\""+tableName+"\""
			assertEquals(SQLResult.STATUS_SUCCEEDED, result.getStatus());
			return true;
		}
	}

	public boolean canPreviewData(String expectedErrorMessage, String[] pathToTable) {// nechat tak??? OR ???guides mgr
																						// (msg, string[] pathTotable, )
		String tableName = pathToTable[pathToTable.length - 1];// last
		String modelName = pathToTable[pathToTable.length - 2];
		if (modelName.contains(".")) {
			modelName = modelName.substring(0, modelName.indexOf("."));
		}
		String previewSQL = "select * from \"" + modelName + "\".\"" + tableName + "\"";
		return canPreviewData(expectedErrorMessage, pathToTable, previewSQL);
	}

	public void setDefaultTeiidInstance(String serverName) {
		chooseAction("Teiid", "Set the Default ");
		try {
			new DefaultCombo().setSelection(serverName);
			new PushButton("OK").click();
		} catch (Exception ex) {
			// dialog doesn't appear if only 1 server instance is defined
		}
		try { // if the teiid instances are different version
			new DefaultShell("Change of Teiid Version");
			new PushButton("Yes").click();
		}catch(Exception ex){
			// dialog doesn't appear if teiid instances are same version
		}
		try { // if test untestet teiid version
			new DefaultShell("Untested Teiid Version");
			new PushButton("Yes").click();
		}catch(Exception ex){
			// dialog doesn't appear 
		}
		try{ // if you wish to disconnect old instance before switching
			new DefaultShell("Disconnect Current Default Instance");
			new PushButton("Yes").click();
		}catch(Exception ex){
			//dialog doesn't appear if server is not running
		}
		try{ 
			new DefaultShell("Default Server Changed");
		}catch(Exception ex){ // if the default instance was already set 
			try{
				new DefaultShell("Default server unchanged");
			}catch(Exception ex2){
				System.err.println(ex2.getCause() + "," + ex2.getMessage());
			}
		}
		new PushButton("OK").click();
	}

	// TODO modeling actions... - the grey context menu
	// TODO support other action sets
	
	/**
	 * Create new project via guides
	 * @param actionSet - actionSet name through that trigger new project wizard
	 * @param projectName - project name
	 */
	public void createProjectViaGuides(String actionSet,String projectName){
		chooseAction(actionSet, "Define Teiid ");
		new PushButton("New...").click();
		new ModelProjectPage().setProjectName(projectName);
		new WizardDialog().finish();
		new DefaultShell("Define Model Project");
		new PushButton("OK").click();
	}
	/**
	 * previewData, can set actionset
	 * @param actionSet - actionSet name through that trigger preview
	 * @param path - path
	 */
	public void previewDataViaActionSet(String actionSet,String... path){//just try-catch, no boolean param
		this.previewDataViaActionSetWithParam(actionSet,null,path);
	}
	/**
	 * previewData, can set actionset, with param
	 * @param actionSet - actionSet name through that trigger preview
	 * @param param - parameters to preview
	 * @param path - path
	 */
	public void previewDataViaActionSetWithParam(String actionSet, String param, String... path){
		new GuidesView().chooseAction(actionSet, "Preview Data");
		new DefaultShell("Preview Data");
		new PushButton("...").click();
		new DefaultShell("Table or Procedure Selection");
		new DefaultTreeItem(path).select();
		new PushButton("OK").click();
		new DefaultShell("Preview Data");
		new PushButton("OK").click();
		if(param!=null){
			new WaitWhile(new IsInProgress(), TimePeriod.NORMAL);
			new DefaultShell("Preview Data");
//			new LabeledText(nameParam).setText(param);
			new DefaultText(0).setText(param);
			new PushButton("OK").click();
		}

		//setup display property; only 1st time
		try {
			//what property?
			new DefaultShell("Change Property");
			new PushButton("Yes").click();
		} catch (Exception ex){
			//do nothing
		}
		
		new WaitWhile(new IsInProgress(), TimePeriod.VERY_LONG);
		AbstractWait.sleep(TimePeriod.SHORT);
		new WaitWhile(new IsPreviewInProgress(), TimePeriod.VERY_LONG);
	}
	/**
	 * Define VDB via guides
	 * @param actionSet - name action set 
	 * @param projectName - project name
	 * @param vdbName - VDB name
	 * @param models - all models which we want add to VDB
	 */
	public void defineVDB(String actionSet,String projectName,String vdbName, String... models){
		chooseAction(actionSet, "Define VDB");
		new DefaultShell("Define VDB");
		new PushButton("New...").click();
		CreateVDB vdb = new CreateVDB();
		vdb.setFolder(projectName);
		vdb.setName(vdbName);
		vdb.fillFirstPageForGuides(); // TODO -> private function
		new WaitWhile(new IsInProgress(), TimePeriod.LONG);
		vdb.finish();
		new DefaultShell("Define VDB");
		new PushButton("OK").click();
		VDBEditor vdbEditor = new VDBEditor(vdbName+".vdb");
		vdbEditor.addModelsToVDB(projectName, models);
		try{
			DefaultShell mm = new DefaultShell("Add File(s) to VDB");
			mm.close();
		}catch(Exception ex){
			return;
		}
	}
	/*
	 *  Check when VDB editor was opened via guides
	 */
	public boolean editVDB(String actionSet,String projectName, String vdbName){
		new DefaultEditor(vdbName+".vdb").close();
		chooseAction(actionSet, "Edit VDB");
		try{
			new DefaultEditor(vdbName+".vdb");  //check when editor was opened 
		}catch(Exception ex){
			System.err.println(ex);
			return false;
		}
		return true;
	}
	/* 
	 * @return list with the number of columns from results for each query 
	 */
	public List<Integer> executeVDB(String actionSet,TeiidServerRequirement teiidServer, String vdbName,String... testSql){
		chooseAction(actionSet, "Execute VDB");	
		new PushButton("OK").click();
		new WaitWhile(new IsInProgress(), TimePeriod.LONG);
		List<Integer> result = new ArrayList<Integer>();
		TeiidJDBCHelper jdbchelper = new TeiidJDBCHelper(teiidServer, vdbName);
		for (String sql : testSql) {
			result.add(jdbchelper.getNumberOfResults(sql));
		}
		return result;
	}

	public WAR createWAR(String actionSet, String warName, String warType, String JndiName, String path, String[] pathVDB){
		Properties warProps = new Properties();
		warProps.setProperty("contextName", warName);
		warProps.setProperty("vdbJndiName", JndiName);
		warProps.setProperty("securityType", warType);
		warProps.setProperty("saveLocation", path);
		WAR war = new WAR(warProps,pathVDB);
		chooseAction(actionSet, "Generate REST");
		new PushButton("OK").click();
		war.setupRESTWAR();  //private -> public
		new PushButton("OK").click();
		try{
			new DefaultShell("Overwrite existing WAR file?");
			new PushButton("Yes").click();

		}catch(Exception ex){
			
		}
		new PushButton("OK").click();
		return war; 
	}
	
	public void newServer(String serverName){
		chooseAction("Teiid", "Configure new JBoss Server");
		new LabeledText("Server name:").setText(serverName);
		new PushButton("Next >").click();
		new DefaultCombo().setSelection(0);
		new PushButton("Finish").click();
		new WaitWhile(new IsInProgress(), TimePeriod.SHORT);
	}
	
	public boolean editServer(String serverName){
		chooseAction("Teiid", "Edit JBoss");
		new DefaultShell("Server Selection");
		new DefaultCombo().setSelection(serverName);
		new PushButton("OK").click();
		try{
			new DefaultEditor(serverName);  
		}catch(Exception ex){
			System.err.println(ex);
			return false;
		}
		return true;
	}
	
	public void startAndRefreshServer(String serverName){
		ServersView view = new ServersView();
		view.open();
		List <Server> servers = view.getServers();
		servers.get(0).stop();
		servers.get(1).start();
		new WaitWhile(new IsInProgress(), TimePeriod.SHORT);
		chooseAction("Teiid", "Refresh ");
		new DefaultCombo().setSelection(serverName);
		new PushButton("OK").click();
		new DefaultShell("Notification");
		new PushButton("OK").click();
		new ShellMenu("File", "Save All").select();
		new WaitWhile(new IsInProgress(), TimePeriod.SHORT);
	}
	
	public void createDataSource(String name, String connectionProfile){
		chooseAction("Model Teiid Data Source", "Create Data Source");
		new LabeledText("Data Source Name:").setText(name);
		new LabeledCombo("Connection Profile").setSelection(connectionProfile);
		new PushButton("OK").click();
		new WaitWhile(new IsInProgress(), TimePeriod.SHORT);
	}
	
	public void createSourceModelFromTeiid(String name, String sourceName, String tableName, String schema){
		chooseAction("Model Teiid Data Source", "Create source model from Teiid ");
		new DefaultTable(new DefaultGroup("Data Sources"),0).getItem(sourceName).click();
		new PushButton("Next >").click();
		DefaultTable importProperties = new DefaultTable(new DefaultGroup("Import Properties"),0);
		if(tableName!=null){
			importProperties.getItem("Table Name Pattern").click(1);
			this.fillTextTable(tableName);
		}
		if(schema!=null){
			importProperties.getItem("Schema Pattern").click(1);
			this.fillTextTable(schema);
		}
		importProperties.getItem("Table Types").click(1);
		this.fillTextTable("TABLE");
		importProperties.getItem("Use Full Schema Name").click(1);
		this.fillTextTable("false");
		new PushButton("Next >").click();
		new LabeledText("Name:").setText(name);
		new PushButton("Next >").click();
		new WaitWhile(new IsInProgress(), TimePeriod.LONG);
		new PushButton("Next >").click();
		new WaitWhile(new IsInProgress(), TimePeriod.NORMAL);
		new PushButton("Finish").click();
	}
	
	private void fillTextTable(final String text){;
	final Widget w = WidgetLookup.getInstance().getFocusControl();
	Display.syncExec(new Runnable() {
		
		@Override
		public void run() {
			((Text) w).setText(text);
			KeyboardFactory.getKeyboard().type(SWT.CR);				
		}
	});
}
	
}
