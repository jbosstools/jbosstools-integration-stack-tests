package org.jboss.tools.teiid.reddeer.view;

import static org.junit.Assert.assertTrue;

import java.awt.event.KeyEvent;
import java.io.File;
import java.util.List;

import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.reddeer.common.exception.WaitTimeoutExpiredException;
import org.eclipse.reddeer.common.wait.AbstractWait;
import org.eclipse.reddeer.common.wait.TimePeriod;
import org.eclipse.reddeer.common.wait.WaitUntil;
import org.eclipse.reddeer.common.wait.WaitWhile;
import org.eclipse.reddeer.core.exception.CoreLayerException;
import org.eclipse.reddeer.eclipse.ui.navigator.resources.AbstractExplorer;
import org.eclipse.reddeer.swt.condition.ShellIsActive;
import org.eclipse.reddeer.swt.condition.ShellIsAvailable;
import org.eclipse.reddeer.swt.exception.SWTLayerException;
import org.eclipse.reddeer.swt.impl.button.CheckBox;
import org.eclipse.reddeer.swt.impl.button.LabeledCheckBox;
import org.eclipse.reddeer.swt.impl.button.OkButton;
import org.eclipse.reddeer.swt.impl.button.PushButton;
import org.eclipse.reddeer.swt.impl.button.RadioButton;
import org.eclipse.reddeer.swt.impl.combo.DefaultCombo;
import org.eclipse.reddeer.swt.impl.menu.ContextMenuItem;
import org.eclipse.reddeer.swt.impl.menu.ShellMenuItem;
import org.eclipse.reddeer.swt.impl.shell.DefaultShell;
import org.eclipse.reddeer.swt.impl.styledtext.DefaultStyledText;
import org.eclipse.reddeer.swt.impl.text.DefaultText;
import org.eclipse.reddeer.swt.impl.text.LabeledText;
import org.eclipse.reddeer.swt.impl.tree.DefaultTreeItem;
import org.eclipse.reddeer.swt.keyboard.KeyboardFactory;
import org.eclipse.reddeer.workbench.core.condition.JobIsRunning;
import org.eclipse.reddeer.workbench.handler.WorkbenchShellHandler;
import org.eclipse.reddeer.workbench.impl.editor.DefaultEditor;
import org.eclipse.reddeer.workbench.impl.shell.WorkbenchShell;
import org.jboss.tools.teiid.reddeer.condition.RadioButtonEnabled;
import org.jboss.tools.teiid.reddeer.condition.WarIsDeployed;
import org.jboss.tools.teiid.reddeer.connection.TeiidJDBCHelper;
import org.jboss.tools.teiid.reddeer.dialog.CreateWarDialog;
import org.jboss.tools.teiid.reddeer.dialog.CreateWebServiceDialog;
import org.jboss.tools.teiid.reddeer.dialog.GenerateDynamicVdbDialog;
import org.jboss.tools.teiid.reddeer.dialog.GenerateRestProcedureDialog;
import org.jboss.tools.teiid.reddeer.dialog.GenerateVdbArchiveDialog;
import org.jboss.tools.teiid.reddeer.dialog.SaveAsDialog;
import org.jboss.tools.teiid.reddeer.editor.RelationalModelEditor;
import org.jboss.tools.teiid.reddeer.requirement.TeiidServerRequirement;
import org.jboss.tools.teiid.reddeer.wizard.imports.ImportProjectWizard;
import org.jboss.tools.teiid.reddeer.wizard.newWizard.MetadataModelWizard;
import org.jboss.tools.teiid.reddeer.wizard.newWizard.NewModelProjectWizard;
import org.jboss.tools.teiid.reddeer.wizard.newWizard.VdbWizard;

public class ModelExplorer extends AbstractExplorer {

	public static class ChildType{
		public static final String XML_DOCUMENT = "XML Document";
		public static final String NAME_SPACE = "Namespace Declaration";
		public static final String SEQUENCE = "sequence";
		public static final String ELEMENT = "Element";
		public static final String TABLE = "Table...";
		public static final String OPERATION = "Operation";
		public static final String INPUT = "Input";
		public static final String OUTPUT = "Output";
		public static final String PROCEDURE = "Procedure...";
		public static final String INDEX = "Index...";
		public static final String COLUMN = "Column";
		public static final String ACCESS_PATTERN = "Access Pattern";
		public static final String PROCEDURE_RESULTSET = "Procedure ResultSet";
		public static final String PROCEDURE_PARAMETER = "Procedure Parameter";
		public static final String UNIQUE_CONSTRAINT = "Unique Constraint";
		public static final String PRIMARY_KEY = "Primary Key";
		public static final String FOREIGN_KEY = "Foreign Key";
		public static final String VIEW = "View...";
		
	}
	
	public ModelExplorer() {
		super("Model Explorer");
		open();
		activate();
	}
	
	/**
	 * Selects and refreshes specified project.
	 */
	public void refreshProject(String projectName){
		selectItem(projectName);
		new ContextMenuItem("Refresh").select();
		new WaitWhile(new JobIsRunning(), TimePeriod.LONG);
		activate();
	}
	
	/**
	 * Changes connection profile of specified Source Model
	 * @param connectionProfile - ConnectionProfileConstants.*
	 * @param modelPath - path to the model (<PROJECT>, ..., <MODEL>) 
	 */
	public void changeConnectionProfile(String connectionProfile, String... modelPath) {
		int n = modelPath.length -1;
		modelPath[n] = (modelPath[n].contains(".xmi")) ? modelPath[n] : modelPath[n] + ".xmi";
		String modelName = modelPath[n];
		new WorkbenchShell();
		this.selectItem(modelPath);
		new ContextMenuItem("Modeling", "Set Connection Profile").select();
		new DefaultShell("Set Connection Profile");
		new DefaultTreeItem("Database Connections", connectionProfile).select();
		new PushButton("OK").click();
		if (new ShellIsAvailable("Confirm Connection Profile Change").test()){ //if test untestet teiid version
			new PushButton("OK").click();
		}
		if (new ShellIsAvailable("Set JBoss Data Source JNDI Name").test()){ //if test untestet teiid version
			new WaitUntil(new ShellIsAvailable("Set JBoss Data Source JNDI Name"));
			String jndiName = modelName;
			new DefaultText(0).setText(jndiName.replace(".xmi", ""));
			new PushButton("OK").click();
		}
		new RelationalModelEditor(modelName).save();
	}
	
	/**
	 * Simple check if model is correct. Creates VDB with model and query.
	 */
	public void simulateTablesPreview(TeiidServerRequirement teiidServer, String project, String model, String[] tables) {
		String vdb_name = "Check_" + model;	
		VdbWizard.openVdbWizard()
				.setLocation(project)
				.setName(vdb_name)
				.addModel(project, model + ".xmi")
				.finish();
		this.deployVdb(project, vdb_name);

		TeiidJDBCHelper jdbcHelper = new TeiidJDBCHelper(teiidServer, vdb_name);
		// try simple select for every table
		for (int i = 0; i < tables.length; i++) {
			String previewSQL = "select * from \"" + model + "\".\"" + tables[i] + "\"";
			assertTrue(jdbcHelper.isQuerySuccessful(previewSQL,true));
		}
	}
	
	/**
	 * Performs preview of specified model's item .
	 * @param params - parameters for sql (access pattern or preview function with parameters)
	 * @param customSql - custom sql for preview
	 * @param itemPath - path to item (<PROJECT>, ..., <ITEM>)
	 */
	public void customPreviewModelItem(List<String> params, String customSql, String... itemPath) {
		new WorkbenchShell();
		this.selectItem(itemPath);
		new ContextMenuItem("Modeling", "Preview Data").select();
		try {
			new PushButton("Yes").click();
		} catch (Exception e) { }
		if ((params != null) && (!params.isEmpty())) {
			new DefaultShell("Preview Data");
			int i = 0;
			for (String paramName : params) {// expects the params are sorted
				new DefaultText(i).setText(paramName);
				i++;
			}
			new PushButton("OK").click();
		}
		if(customSql!=null){
			DefaultStyledText styledText = new DefaultStyledText();
			String removedText = styledText.getText();
			styledText.selectText(removedText);
			styledText.insertText(customSql);
		}
		
		if (new ShellIsAvailable("Data Sources Missing").test()){
			new PushButton("Yes").click();
		}
		
		new PushButton("OK").click();
	}
	
	/**
	 * Performs preview of specified model's item.
	 * @param params - parameters for sql (access pattern or preview function with parameters)
	 * @param itemPath - path to item (<PROJECT>, ..., <ITEM>)
	 */
	public void previewModelItem(List<String> params, String... itemPath) {
		customPreviewModelItem(params,null,itemPath);
	}
	
	/**
	 * Creates data source for specified VDB.
	 */
	public void createVdbDataSource(String[] pathToVDB, String jndiName, boolean passThruAuth) {
		int n = pathToVDB.length - 1;
		pathToVDB[n] = (pathToVDB[n].contains(".vdb")) ? pathToVDB[n] : pathToVDB[n] + ".vdb";
		new WorkbenchShell();
		this.selectItem(pathToVDB);
		new ContextMenuItem("Modeling", "Create VDB Data Source").select();
		try {
			new DefaultShell("VDB Not Yet Deployed ");
			new PushButton("Yes").click();// create ds anyway
		} catch (Exception e) {		}
		new WorkbenchShell();
		new DefaultText(1).setText(jndiName);
		if (passThruAuth) {
			new CheckBox("Pass Thru Authentication").click();
		}
		new PushButton("OK").click();
	}
	
	/**
	 * Creates data source for specified Source Model.
	 */
	public void createDataSource(String connectionSourceType, String connectionProfile, String... pathToSourceModel) {
		activate();
		int n = pathToSourceModel.length - 1;
		pathToSourceModel[n] = (pathToSourceModel[n].contains(".xmi")) ? pathToSourceModel[n] : pathToSourceModel[n] + ".xmi";
		new WorkbenchShell();
		this.selectItem(pathToSourceModel);
		new ContextMenuItem("Modeling", "Create Data Source").select();
		// wait until radio button is enabled
		new WaitUntil(new RadioButtonEnabled(connectionSourceType), TimePeriod.DEFAULT);
		new RadioButton(connectionSourceType).click();
		// in case of connection profile -> choose connection profile
		if (connectionSourceType.toString().equals("Use Connection Profile Info")) {
			new DefaultCombo(1).setSelection(connectionProfile);
		}
		if (!new PushButton("OK").isEnabled()) {
			System.err.println("Datasource " + pathToSourceModel[pathToSourceModel.length - 1] + "exists!");
			new PushButton("Cancel").click();
		} else {
			new PushButton("OK").click();
		}
	}
	
	/**
	 * Selects Modeling/'Generate REST Virtual Procedures' and returns related dialog. 
	 * @param PathFrom - path to model from which WS will be created (<PROJECT>, ..., <MODEL>)
	 */
	public GenerateRestProcedureDialog modelingRestProcedure(String... pathFrom){
		this.selectItem(pathFrom);
		new ContextMenuItem("Modeling", "Generate REST Virtual Procedures").select();
		AbstractWait.sleep(TimePeriod.SHORT);
		return new GenerateRestProcedureDialog();
	}
	
	/**
	 * Selects Modeling/'Create Web Service' and returns related dialog. 
	 * @param PathFrom - path to model from which WS will be created (<PROJECT>, ..., <MODEL>)
	 * @param IsXml - false - create from relation model 
	 * 				- true - create from XML model
	 */
	public CreateWebServiceDialog modelingWebService(boolean fromXml, String... pathFrom){
		this.selectItem(pathFrom);
		new ContextMenuItem("Modeling", "Create Web Service").select();
		AbstractWait.sleep(TimePeriod.SHORT);
		return new CreateWebServiceDialog(fromXml);
	}
	
	/**
	 * Selects specified item of tree structure. 
	 * @param path (<PROJECT>, ..., <ITEM>)
	 */
	public void selectItem(String... path){
		activate();
		new DefaultTreeItem(path).select();
	}
	
	/**
	 * Creates a new model project with specified name.
	 */
	public void createProject(String projectName) {
		NewModelProjectWizard.openWizard()
				.setProjectName(projectName)
				.finish();
	}
	
	/**
	 * Opens editor of specified model. 
	 * @param pathToModel - path to model (<PROJECT>, ..., <MODEL>)
	 * Note: path can end with every item of model which is able to be opened.
	 */
	public void openModelEditor(String... pathToModel) {
		activate();
		new DefaultTreeItem(pathToModel).doubleClick();		
		AbstractWait.sleep(TimePeriod.getCustom(3));
	}
	
	/**
	 * Deploys specified VDB
	 * @param vdbpath - path to VDB (<PROJECT>, ..., <VDB>)
	 */
	public void deployVdb(/*boolean passThruAuth, */String... vdbpath) {
		new WorkbenchShell();
		int i = vdbpath.length -1;
		vdbpath[i] = (vdbpath[i].contains(".vdb") || vdbpath[i].contains(".xml")) ? vdbpath[i] : vdbpath[i] + ".vdb";
		this.selectItem(vdbpath);
		new ContextMenuItem("Modeling", "Deploy").select();
		AbstractWait.sleep(TimePeriod.getCustom(3));
		
		if (new ShellIsActive("VDB is not Synchronized").test()){
			new PushButton("Yes").click();
		}
		
		if (new ShellIsActive("Create Missing Teiid Data Sources Confirmation").test()){
			new PushButton("Yes").click();
		}
		
		if (new ShellIsActive("Server is not connected").test()){
			new PushButton("OK").click();
			throw new Error("Server is not connected");
		}
		try {
			new DefaultShell("Create VDB Data Source");
			new LabeledCheckBox("JNDI Name >>    java:/").toggle(true/*passThruAuth*/);
			new PushButton("Create Source").click();
			new WaitWhile(new ShellIsActive("Create VDB Data Source"), TimePeriod.DEFAULT);
		} catch (SWTLayerException e) {	
			// shell not opened -> continue
		}
	}
	
	/**
	 * Generates dynamic VDB from specified VDB
	 * @param vdbpath - path to VDB (<PROJECT>, ..., <VDB>)
	 */
	public GenerateDynamicVdbDialog generateDynamicVDB(String... vdbPath) {
		int i = vdbPath.length -1;
		vdbPath[i] = (vdbPath[i].contains(".vdb")) ? vdbPath[i] : vdbPath[i] + ".vdb";
		this.selectItem(vdbPath);
		new ContextMenuItem("Modeling", "Generate Dynamic VDB").select();
		return new GenerateDynamicVdbDialog();
	}
	
	/**
	 * Generates VDB archive from dynamic VDB
	 * @param vdbpath - path to VDB (<PROJECT>, ..., <VDB>)
	 */
	public GenerateVdbArchiveDialog generateVdbArchive(String... vdbPath) {
		int i = vdbPath.length -1;
		vdbPath[i] = (vdbPath[i].contains(".xml")) ? vdbPath[i] : vdbPath[i] + ".xml";
		this.selectItem(vdbPath);
		new ContextMenuItem("Modeling", "Generate VDB Archive and Models").select();
		return new GenerateVdbArchiveDialog();
	}
	
	/**
	 * Generates SOAP WAR file from specified VDB.
	 * @param vdbPath - path to VDB (<PROJECT>, ..., <VDB>)
	 * @param soap - whether generate SOAP WAR or REST WAR 
	 */
	public CreateWarDialog generateWar(boolean soap, String... vdbPath){
		new WorkbenchShell();
		int i = vdbPath.length -1;
		vdbPath[i] = (vdbPath[i].contains(".vdb")) ? vdbPath[i] : vdbPath[i] + ".vdb";
		this.selectItem(vdbPath);
		new ContextMenuItem("Modeling", soap ? "Generate SOAP War" : "Generate REST War").select();
		AbstractWait.sleep(TimePeriod.getCustom(3));
		return new CreateWarDialog(soap);
	}

	/**
	 * Deploys specified WAR.
	 * @param - name of the server where WAR will be deployed (TeiidServerRequirement.getName())
	 * @param warPath - path to WAR (<PROJECT>, ..., <WAR>)
	 */
	public void deployWar(TeiidServerRequirement teiidServer, String... warPath){
		new WorkbenchShell();
		int iWar = warPath.length -1;
		warPath[iWar] = (warPath[iWar].contains(".war")) ? warPath[iWar] : warPath[iWar] + ".war";
		refreshProject(warPath[0]);
		this.selectItem(warPath);
		new ContextMenuItem("Mark as Deployable").select();
		AbstractWait.sleep(TimePeriod.SHORT);
		if (new ShellIsActive("No deployable servers found").test()){
			new PushButton("OK").click();
			throw new Error("Server is not connected");
		}
		try {
			new WaitUntil(new WarIsDeployed(teiidServer.getName(), warPath[iWar]), TimePeriod.LONG);
		} catch (WaitTimeoutExpiredException ex){ 
			new ServersViewExt().restartWar(teiidServer.getName(), warPath[iWar]);
			new WaitUntil(new WarIsDeployed(teiidServer.getName(), warPath[iWar]), TimePeriod.LONG);
		}
		AbstractWait.sleep(TimePeriod.getCustom(5));
	}
	
	/**
	 * Undeploys specified WAR.
	 * @param warPath - path to WAR (<PROJECT>, ..., <WAR>)
	 */
	public void undeployWar(String... warPath){
		new WorkbenchShell();
		int iWar = warPath.length -1;
		warPath[iWar] = (warPath[iWar].contains(".war")) ? warPath[iWar] : warPath[iWar] + ".war";
		this.selectItem(warPath);
		new ContextMenuItem("Unmark as Deployable").select();
		AbstractWait.sleep(TimePeriod.getCustom(5));
	}
	
	/**
	 * Returns absolute path of specified project (/.../<WORKSPACE>/<PROJECT>)
	 */
	public String getProjectPath(String project){
		return ResourcesPlugin.getWorkspace().getRoot().getLocationURI().getPath() + "/" + project;
	}
	
	/**
	 * Performs save model as action. 
	 * @param originalModelPath - full path to original model (<PROJECT>/.../<MODEL>) 
	 * @param newModelPath - full path to folder where new model will be located (<PROJECT>/.../<FOLDER>) 
	 * @param isSchemaModel - whether original model is XML Schema Model or not (for dialog purposes)
	 */
	public void saveModelAs(String originalModelPath, String newModelName, String newModelPath, boolean isSchemaModel){
		this.openModelEditor(originalModelPath.split("/"));
		new ShellMenuItem(new WorkbenchShell(), "File", "Save As...").select();
		SaveAsDialog saveAsDialog = new SaveAsDialog(isSchemaModel);
		saveAsDialog.setLocation(newModelPath.split("/"));
		saveAsDialog.setModelName(newModelName);
		saveAsDialog.finish();
		AbstractWait.sleep(TimePeriod.SHORT);
	}
	
	/**
	 * Performs copy model action. 
	 * @param originalModelPath - full path to original model (<PROJECT>/.../<MODEL>) 
	 * @param newModelPath - full path to folder where new model will be located (<PROJECT>/.../<FOLDER>) 
	 * @param newModelClass - MetadataModelWizard.ModelClass.*
	 * @param newModelType - MetadataModelWizard.ModelType.*
	 */
	public void copyModel(String originalModelPath, String newModelName, String newModelPath, 
		String newModelClass, String newModelType){
		MetadataModelWizard.openWizard()
				.setLocation(newModelPath.split("/"))
				.setModelName(newModelName)
				.selectModelClass(newModelClass)
				.selectModelType(newModelType)
				.selectModelBuilder(MetadataModelWizard.ModelBuilder.COPY_EXISTING)
				.nextPage()
				.setExistingModel(originalModelPath.split("/"))
				.finish();
		AbstractWait.sleep(TimePeriod.SHORT);
		ShellMenuItem saveMenu = new ShellMenuItem(new WorkbenchShell(), "File", "Save");
		if (saveMenu.isEnabled()){
			saveMenu.select();
			AbstractWait.sleep(TimePeriod.SHORT);
		}
	}
	
	/**
	 * Adds child to specified item of model's tree structure.
	 * @param childType -ModelExplorer.ChildType.*
	 * @param itemPath - path to model (<PROJECT>, ..., <MODEL/ITEM>)
	 */
	public void addChildToModelItem(String childType, String... itemPath) {
		refreshProject(itemPath[0]);
		new DefaultTreeItem(itemPath).select();
		new ContextMenuItem("New Child", childType).select();
		AbstractWait.sleep(TimePeriod.SHORT);
		KeyboardFactory.getKeyboard().type(KeyEvent.VK_TAB);
		AbstractWait.sleep(TimePeriod.SHORT);
	}
	
	/**
	 * Adds sibling to specified item of model's tree structure.
	 * @param siblingType - ModelExplorer.ChildType.*
	 * @param itemPath - path to model (<PROJECT>, ..., <MODEL/ITEM>)
	 */
	public void addSiblingToModelItem(String siblingType, String... itemPath){
		refreshProject(itemPath[0]);
		new DefaultTreeItem(itemPath).select();
		new ContextMenuItem("New Sibling",siblingType).select();
		AbstractWait.sleep(TimePeriod.SHORT);
		KeyboardFactory.getKeyboard().type(KeyEvent.VK_TAB);
		AbstractWait.sleep(TimePeriod.SHORT);
	}
	
	/**
	 * Renames specified item of model's tree structure.
	 * @param itemPath - path to item (<PROJECT>, ..., <ITEM>)
	 */
	public void renameModelItem(String newName, String... itemPath){
		refreshProject(itemPath[0]);
		new DefaultTreeItem(itemPath).select();
		new ContextMenuItem("Rename...").select();
		AbstractWait.sleep(TimePeriod.SHORT);
		new DefaultText().setText(newName);
		KeyboardFactory.getKeyboard().type(KeyEvent.VK_TAB);
		AbstractWait.sleep(TimePeriod.SHORT);
	}
	
	/**
	 * Renames specified model.
	 * @param pathToModel - path to model (<PROJECT>, ..., <MODEL>)
	 * Note: new name must contains extension of model.
	 */
	public void renameModel(String newName, String... modelPath){
		refreshProject(modelPath[0]);
		new DefaultTreeItem(modelPath).select();
		new ContextMenuItem("Refactor", "Rename...").select();
		AbstractWait.sleep(TimePeriod.SHORT);
		new LabeledText("New name:").setText(newName);
		new OkButton().click();
		AbstractWait.sleep(TimePeriod.SHORT);
	}
	
	/**
	 * Deletes specified model.
	 * @param pathToModel - path to model (<PROJECT>, ... ,<MODEL>)
	 */
	public void deleteModel(String... modelPath){
		refreshProject(modelPath[0]);
		new DefaultTreeItem(modelPath).select();
		new ContextMenuItem("Delete").select();
		AbstractWait.sleep(TimePeriod.SHORT);
		try {
			new DefaultShell("Delete Resources");
			new OkButton().click();
			new WaitWhile(new ShellIsActive("Delete Resources"), TimePeriod.DEFAULT);
		} catch (SWTLayerException e) {	
			// shell not opened -> continue
		}
	}
	
	/**
	 * Imports specified project into workspace (directory or archive)
	 * @param projectName = name of folder in 'resources/projects/' folder
	 */
	public void importProject(String projectName) {
		ImportProjectWizard.openWizard()
						   .setPath(new File("resources/projects/" + projectName).getAbsolutePath())
						   .finish();
	}
	
	/**
	 * Saves and closes all editors, then removes specified project
	 * @param project
	 */
	public void deleteAllProjectsSafely() {
		WorkbenchShellHandler.getInstance().closeAllNonWorbenchShells();
		try {
			new DefaultEditor();
		} catch (Exception ex) {
			// no editor, ignore
		}
		try {
			new ShellMenuItem(new WorkbenchShell(), "File", "Save All").select();
			AbstractWait.sleep(TimePeriod.getCustom(3));
		} catch (Exception ex) {
			// no editors need saving, ignore
		}
		try {
			new ShellMenuItem(new WorkbenchShell(), "File", "Close All").select();
			AbstractWait.sleep(TimePeriod.getCustom(3));
		} catch (Exception ex) {
			// no editors open, ignore
		}
		new WorkbenchShell();
		this.activate();
		this.deleteAllProjects();
	}
	/**
	 * Sets JNDI name for source model
	 */
	public void setJndiName(String jndiName, String... pathToSourceModel) {
		new WorkbenchShell();
		int iModel = pathToSourceModel.length -1;
		pathToSourceModel[iModel] = (pathToSourceModel[iModel].contains(".")) ? pathToSourceModel[iModel] : pathToSourceModel[iModel] + ".xmi";
		this.selectItem(pathToSourceModel);
		new ContextMenuItem("Modeling", "Set Data Source JNDI Name").select();
		new DefaultShell("Set JBoss Data Source JNDI Name");
		new DefaultText(0).setText(jndiName);
		new PushButton("OK").click();
	}
	
	/**
	 * Check if item is in the project
	 * @param path - path to the item (<PROJECT>, ..., <ITEM>) 
	 */
	public boolean containsItem(String... path){
		refreshProject(path[0]);
		try{
			new DefaultTreeItem(path);
			return true;
		}catch(CoreLayerException ex){
			return false;
		}
	}
}
