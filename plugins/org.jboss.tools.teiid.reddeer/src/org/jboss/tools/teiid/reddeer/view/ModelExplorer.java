package org.jboss.tools.teiid.reddeer.view;

import static org.junit.Assert.assertTrue;

import java.awt.event.KeyEvent;
import java.io.File;
import java.util.List;
import java.util.Properties;

import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.swtbot.eclipse.finder.SWTWorkbenchBot;
import org.jboss.reddeer.common.matcher.RegexMatcher;
import org.jboss.reddeer.common.wait.AbstractWait;
import org.jboss.reddeer.common.wait.TimePeriod;
import org.jboss.reddeer.common.wait.WaitUntil;
import org.jboss.reddeer.common.wait.WaitWhile;
import org.jboss.reddeer.core.condition.ShellWithTextIsActive;
import org.jboss.reddeer.core.condition.ShellWithTextIsAvailable;
import org.jboss.reddeer.core.handler.ShellHandler;
import org.jboss.reddeer.eclipse.jdt.ui.AbstractExplorer;
import org.jboss.reddeer.swt.exception.SWTLayerException;
import org.jboss.reddeer.swt.impl.button.CheckBox;
import org.jboss.reddeer.swt.impl.button.LabeledCheckBox;
import org.jboss.reddeer.swt.impl.button.OkButton;
import org.jboss.reddeer.swt.impl.button.PushButton;
import org.jboss.reddeer.swt.impl.button.RadioButton;
import org.jboss.reddeer.swt.impl.combo.DefaultCombo;
import org.jboss.reddeer.swt.impl.menu.ContextMenu;
import org.jboss.reddeer.swt.impl.menu.ShellMenu;
import org.jboss.reddeer.swt.impl.shell.DefaultShell;
import org.jboss.reddeer.swt.impl.text.DefaultText;
import org.jboss.reddeer.swt.impl.text.LabeledText;
import org.jboss.reddeer.swt.impl.tree.DefaultTreeItem;
import org.jboss.reddeer.swt.keyboard.KeyboardFactory;
import org.jboss.reddeer.workbench.impl.editor.AbstractEditor;
import org.jboss.reddeer.workbench.impl.shell.WorkbenchShell;
import org.jboss.tools.teiid.reddeer.ChildType;
import org.jboss.tools.teiid.reddeer.ModelBuilder;
import org.jboss.tools.teiid.reddeer.ModelClass;
import org.jboss.tools.teiid.reddeer.ModelProject;
import org.jboss.tools.teiid.reddeer.ModelType;
import org.jboss.tools.teiid.reddeer.Table;
import org.jboss.tools.teiid.reddeer.condition.IsInProgress;
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
import org.jboss.tools.teiid.reddeer.perspective.DatabaseDevelopmentPerspective;
import org.jboss.tools.teiid.reddeer.requirement.TeiidServerRequirement;
import org.jboss.tools.teiid.reddeer.wizard.imports.ImportProjectWizard;
import org.jboss.tools.teiid.reddeer.wizard.newWizard.MetadataModelWizard;
import org.jboss.tools.teiid.reddeer.wizard.newWizard.NewModelProjectWizard;
import org.jboss.tools.teiid.reddeer.wizard.newWizard.VdbWizard;


/**
 * This class represents a model explorer
 * 
 * @author apodhrad
 * 
 */
public class ModelExplorer extends AbstractExplorer {

	private static String CONNECTION_PROFILE_CHANGE = "Confirm Connection Profile Change";

	private static final String MODELING_MENU_ITEM = "Modeling";
	private static final String CREATE_DATA_SOURCE = "Create Data Source";
	private static final String SET_JNDI_NAME = "Set Data Source JNDI Name";

	public static class ConnectionSourceType {
		public static final String USE_MODEL_CONNECTION_INFO = "Use Model Connection Info";
		public static final String USE_CONNECTION_PROFILE_INFO = "Use Connection Profile Info";
	}

	public ModelExplorer() {
		super("Model Explorer");
		this.activate();
	}

	public ModelProject getModelProject(String modelName) {
		return new ModelProject(getProject(modelName));
	}
	
	@Deprecated // use ModelExplorer.addChildToModelItem/addSiblingToModelItem + TableDialog
	public void newTable(String tableName, Table.Type type, Properties props, String... pathToModelXmi) {
		open();

		new DefaultTreeItem(pathToModelXmi).select();
		new ContextMenu("New Child", "Table...").select();
		new Table().create(type, tableName, props);
		new RelationalModelEditor(pathToModelXmi[pathToModelXmi.length - 1]).save();// the last member is modelXmi
	}

	@Override
	public void open() {
		super.open();
		new WorkbenchShell();
	}

	@Deprecated
	public void openTransformationDiagram(String project, String... filePath) {
		open();

		getProject(project).getProjectItem(filePath).getChild("Transformation Diagram").open();
	}

	public void createDataSource(String connectionSourceType, String connectionProfile, String... pathToSourceModel) {
		open();
		// if last without ., set to .xmi
		if (!pathToSourceModel[pathToSourceModel.length - 1].contains(".")) {
			pathToSourceModel[pathToSourceModel.length - 1] = pathToSourceModel[pathToSourceModel.length - 1]
					.concat(".xmi");
		}
		new WorkbenchShell();
		new DefaultTreeItem(pathToSourceModel).select();
		new ContextMenu(MODELING_MENU_ITEM, CREATE_DATA_SOURCE).select();
		// wait until radio button is enabled
		new WaitUntil(new RadioButtonEnabled(connectionSourceType), TimePeriod.NORMAL);
		new RadioButton(connectionSourceType).click();
		// in case of connection profile -> choose connection profile
		if (connectionSourceType.toString().equals(ConnectionSourceType.USE_CONNECTION_PROFILE_INFO)) {
			new DefaultCombo(1).setSelection(connectionProfile);
		}
		if (!new PushButton("OK").isEnabled()) {
			System.err.println("Datasource " + pathToSourceModel[pathToSourceModel.length - 1] + "exists!");
			new PushButton("Cancel").click();
		} else {
			new PushButton("OK").click();
		}
	}
	
	
	
	
	
	// ###############################
	// ### updated down from here
	// ###############################
	

	
	/**
	 * Changes connection profile of specified Source Model
	 * @param connectionProfile - ConnectionProfileConstants.*
	 * @param modelPath - path to the model (<PROJECT>, ..., <MODEL>) 
	 */
	public void changeConnectionProfile(String connectionProfile, String... modelPath) {
		int n = modelPath.length -1;
		modelPath[n] = (modelPath[n].contains(".xmi")) ? modelPath[n] : modelPath[n] + ".xmi";
		new WorkbenchShell();
		this.selectItem(modelPath);
		new ContextMenu("Modeling", "Set Connection Profile").select();
		new DefaultShell("Set Connection Profile");
		new DefaultTreeItem("Database Connections", connectionProfile).select();
		new PushButton("OK").click();
		try {
			new WaitUntil(new ShellWithTextIsAvailable(CONNECTION_PROFILE_CHANGE));
			new PushButton("OK").click();
		} catch (Exception e) {}
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
	 * Performs preview of specified model's item.
	 * @param params (TODO describe)
	 * @param itemPath - path to item (<PROJECT>, ..., <ITEM>)
	 */
	public void previewModelItem(List<String> params, String... itemPath) {
		new WorkbenchShell();
		this.selectItem(itemPath);
		new ContextMenu("Modeling", "Preview Data").select();
		try {
			new PushButton("Yes").click();
		} catch (Exception e) { }
		if ((params != null) && (!params.isEmpty())) {
			new DefaultShell("Preview Data");
			int i = 0;
			for (String paramName : params) {// expects the params are sorted
				new SWTWorkbenchBot().text(i).setText(paramName);
				i++;
			}
			new PushButton("OK").click();
		}
		new PushButton("OK").click();
	}
	
	/**
	 * Checks that preview succeed.
	 * @param previewSQL - specifies preview record
	 */
	public boolean checkPreviewOfModelObject(String previewSQL) {
		// wait while is in progress
		new WaitWhile(new IsInProgress(), TimePeriod.LONG);
		// wait while dialog Preview data... is active
		new WaitWhile(new ShellWithTextIsActive(new RegexMatcher("Preview.*")), TimePeriod.LONG);
		SQLResult result = DatabaseDevelopmentPerspective.getInstance().getSqlResultsView().getByOperation(previewSQL);
		return result.getStatus().equals(SQLResult.STATUS_SUCCEEDED);
	}
	
	/**
	 * Creates data source for specified VDB.
	 */
	public void createVdbDataSource(String[] pathToVDB, String jndiName, boolean passThruAuth) {
		int n = pathToVDB.length -1;
		pathToVDB[n] = (pathToVDB[n].contains(".vdb")) ? pathToVDB[n] : pathToVDB[n] + ".vdb";
		new WorkbenchShell();
		this.selectItem(pathToVDB);
		new ContextMenu("Modeling", "Create VDB Data Source").select();
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
	 * Selects Modeling/'Generate REST Virtual Procedures' and returns related dialog. 
	 * @param PathFrom - path to model from which WS will be created (<PROJECT>, ..., <MODEL>)
	 */
	public GenerateRestProcedureDialog modelingRestProcedure(String... pathFrom){
		this.selectItem(pathFrom);
		new ContextMenu("Modeling", "Generate REST Virtual Procedures").select();
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
		new ContextMenu("Modeling", "Create Web Service").select();
		AbstractWait.sleep(TimePeriod.SHORT);
		return new CreateWebServiceDialog(fromXml);
	}
	
	/**
	 * Selects specified item of tree structure. 
	 * @param path (<PROJECT>, ..., <ITEM>)
	 */
	public void selectItem(String... path){
		getProject(path[0]).refresh();
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
		open();
		this.getProject(pathToModel[0]).refresh();
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
		new ContextMenu("Modeling", "Deploy").select();
		AbstractWait.sleep(TimePeriod.getCustom(3));
		if (new ShellWithTextIsActive("Server is not connected").test()){
			new PushButton("OK").click();
			throw new Error("Server is not connected");
		}
		try {
			new DefaultShell("Create VDB Data Source");
			new LabeledCheckBox("JNDI Name >>    java:/").toggle(true/*passThruAuth*/);
			new PushButton("Create Source").click();
			new WaitWhile(new ShellWithTextIsActive("Create VDB Data Source"), TimePeriod.NORMAL);
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
		new ContextMenu("Modeling", "Generate Dynamic VDB").select();
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
		new ContextMenu("Modeling", "Generate VDB Archive and Models").select();
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
		new ContextMenu("Modeling", soap ? "Generate SOAP War" : "Generate REST War").select();
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
		this.selectItem(warPath);
		new ContextMenu("Mark as Deployable").select();
		AbstractWait.sleep(TimePeriod.SHORT);
		if (new ShellWithTextIsActive("No deployable servers found").test()){
			new PushButton("OK").click();
			throw new Error("Server is not connected");
		}
		new WaitUntil(new WarIsDeployed(teiidServer.getName(), warPath[iWar]), TimePeriod.LONG);
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
		new ContextMenu("Unmark as Deployable").select();
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
		new ShellMenu("File","Save As...").select();
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
	 * @param newModelClass - org.jboss.tools.teiid.reddeer.ModelClass.*
	 * @param newModelType - org.jboss.tools.teiid.reddeer.ModelType.*
	 */
	public void copyModel(String originalModelPath, String newModelName, String newModelPath, 
		ModelClass newModelClass, ModelType newModelType){
		MetadataModelWizard.openWizard()
				.setLocation(newModelPath.split("/"))
				.setModelName(newModelName)
				.selectModelClass(newModelClass)
				.selectModelType(newModelType)
				.selectModelBuilder(ModelBuilder.COPY_EXISTING)
				.nextPage()
				.setExistingModel(originalModelPath.split("/"))
				.finish();
		AbstractWait.sleep(TimePeriod.SHORT);
		ShellMenu saveMenu = new ShellMenu("File","Save");
		if (saveMenu.isEnabled()){
			saveMenu.select();
			AbstractWait.sleep(TimePeriod.SHORT);
		}
	}
	
	/**
	 * Adds child to specified item of model's tree structure.
	 * @param childType - org.jboss.tools.teiid.reddeer.ChildType.*
	 * @param itemPath - path to model (<PROJECT>, ..., <MODEL/ITEM>)
	 */
	public void addChildToModelItem(ChildType childType, String... itemPath) {
		this.getProject(itemPath[0]).refresh();
		new DefaultTreeItem(itemPath).select();
		new ContextMenu("New Child",childType.getText()).select();
		AbstractWait.sleep(TimePeriod.SHORT);
		KeyboardFactory.getKeyboard().type(KeyEvent.VK_TAB);
		AbstractWait.sleep(TimePeriod.SHORT);
	}
	
	/**
	 * Adds sibling to specified item of model's tree structure.
	 * @param siblingType - org.jboss.tools.teiid.reddeer.ChildType.*
	 * @param itemPath - path to model (<PROJECT>, ..., <MODEL/ITEM>)
	 */
	public void addSiblingToModelItem(ChildType siblingType, String... itemPath){
		this.getProject(itemPath[0]).refresh();
		new DefaultTreeItem(itemPath).select();
		new ContextMenu("New Sibling",siblingType.getText()).select();
		AbstractWait.sleep(TimePeriod.SHORT);
		KeyboardFactory.getKeyboard().type(KeyEvent.VK_TAB);
		AbstractWait.sleep(TimePeriod.SHORT);
	}
	
	/**
	 * Renames specified item of model's tree structure.
	 * @param itemPath - path to item (<PROJECT>, ..., <ITEM>)
	 */
	public void renameModelItem(String newName, String... itemPath){
		this.getProject(itemPath[0]).refresh();
		new DefaultTreeItem(itemPath).select();
		new ContextMenu("Rename...").select();
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
		this.getProject(modelPath[0]).refresh();
		new DefaultTreeItem(modelPath).select();
		new ContextMenu("Refactor", "Rename...").select();
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
		this.getProject(modelPath[0]).refresh();
		new DefaultTreeItem(modelPath).select();
		new ContextMenu("Delete").select();
		AbstractWait.sleep(TimePeriod.SHORT);
		try {
			new DefaultShell("Delete Resources");
			new OkButton().click();
			new WaitWhile(new ShellWithTextIsActive("Delete Resources"), TimePeriod.NORMAL);
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
		ShellHandler.getInstance().closeAllNonWorbenchShells();
		try {
			new AbstractEditor();
		} catch (Exception ex) {
			// no editor, ignore
		}
		try {
			new ShellMenu("File", "Save All").select();
			AbstractWait.sleep(TimePeriod.getCustom(3));
		} catch (Exception ex) {
			// no editors need saving, ignore
		}
		try {
			new ShellMenu("File", "Close All").select();
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
		new ContextMenu(MODELING_MENU_ITEM, SET_JNDI_NAME).select();
		new DefaultShell("Set JBoss Data Source JNDI Name");
		new DefaultText(0).setText(jndiName);
		new PushButton("OK").click();
	}
}
