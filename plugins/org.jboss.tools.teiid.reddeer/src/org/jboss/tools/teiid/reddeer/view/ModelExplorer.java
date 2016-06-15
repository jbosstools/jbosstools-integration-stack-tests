package org.jboss.tools.teiid.reddeer.view;

import java.awt.event.KeyEvent;
import java.io.File;
import java.util.Properties;

import org.eclipse.core.resources.ResourcesPlugin;
import org.jboss.reddeer.common.wait.AbstractWait;
import org.jboss.reddeer.common.wait.TimePeriod;
import org.jboss.reddeer.common.wait.WaitUntil;
import org.jboss.reddeer.common.wait.WaitWhile;
import org.jboss.reddeer.core.condition.JobIsRunning;
import org.jboss.reddeer.core.condition.ShellWithTextIsActive;
import org.jboss.reddeer.core.condition.ShellWithTextIsAvailable;
import org.jboss.reddeer.core.handler.ShellHandler;
import org.jboss.reddeer.eclipse.jdt.ui.AbstractExplorer;
import org.jboss.reddeer.swt.exception.SWTLayerException;
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
import org.jboss.tools.teiid.reddeer.Procedure;
import org.jboss.tools.teiid.reddeer.Table;
import org.jboss.tools.teiid.reddeer.condition.IsInProgress;
import org.jboss.tools.teiid.reddeer.condition.RadioButtonEnabled;
import org.jboss.tools.teiid.reddeer.condition.WarIsDeployed;
import org.jboss.tools.teiid.reddeer.dialog.CreateWarDialog;
import org.jboss.tools.teiid.reddeer.dialog.CreateWebServiceDialog;
import org.jboss.tools.teiid.reddeer.dialog.SaveAsDialog;
import org.jboss.tools.teiid.reddeer.editor.ModelEditor;
import org.jboss.tools.teiid.reddeer.requirement.TeiidServerRequirement;
import org.jboss.tools.teiid.reddeer.wizard.GenerateDynamicVdbWizard;
import org.jboss.tools.teiid.reddeer.wizard.GenerateVdbArchiveWizard;
import org.jboss.tools.teiid.reddeer.wizard.ImportProjectWizard;
import org.jboss.tools.teiid.reddeer.wizard.MetadataModelWizard;
import org.jboss.tools.teiid.reddeer.wizard.ModelProjectWizard;

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

	public static class ConnectionSourceType {
		public static final String USE_MODEL_CONNECTION_INFO = "Use Model Connection Info";
		public static final String USE_CONNECTION_PROFILE_INFO = "Use Connection Profile Info";
	}

	public ModelExplorer() {
		super("Model Explorer");
		this.activate();
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

	public void changeConnectionProfile(String connectionProfile, String projectName, String... projectItem) {
		// if no extension specified, add .xmi
		if (!projectItem[projectItem.length - 1].contains(".")) {
			projectItem[projectItem.length - 1] = projectItem[projectItem.length - 1].concat(".xmi");
		}
		new DefaultShell();
		new ModelExplorer().getProject(projectName).getProjectItem(projectItem).select();
		new ContextMenu("Modeling", "Set Connection Profile").select();
		new DefaultShell("Set Connection Profile");
		new DefaultTreeItem("Database Connections", connectionProfile).select();
		new PushButton("OK").click();

		try {
			new WaitUntil(new ShellWithTextIsAvailable(CONNECTION_PROFILE_CHANGE));
			new PushButton("OK").click();
		} catch (Exception e) {
		}
	}

	public void newBaseTable(String project, String model, String tableName) {
		open();

		new DefaultTreeItem(project, model).select();
		new ContextMenu("New Child", "Table...").select();
		new DefaultShell("Create Relational View Table");
		new LabeledText("Name").setText(tableName);
		new PushButton("OK").click();
	}

	/**
	 * Create new (base) table in view model
	 * 
	 * @param project
	 * @param model
	 * @param tableName
	 * @param baseTable
	 *            true if context menu contains "Base Table"
	 */
	public void newBaseTable(String project, String model, String tableName, boolean baseTable) {
		open();

		new DefaultTreeItem(project, model).select();
		if (baseTable) {
			new ContextMenu("New Child", "Base Table...").select();
		} else {
			new ContextMenu("New Child", "Table...").select();
		}

		new DefaultShell("Create Relational View Table");
		new LabeledText("Name").setText(tableName);
		new PushButton("OK").click();
	}

	public void newTable(String tableName, Table.Type type, Properties props, String... pathToModelXmi) {
		open();

		new DefaultTreeItem(pathToModelXmi).select();
		new ContextMenu("New Child", "Table...").select();
		new Table().create(type, tableName, props);
		new ModelEditor(pathToModelXmi[pathToModelXmi.length - 1]).save();// the last member is modelXmi
	}

	public Procedure newProcedure(String project, String model, String procedure) {
		open();

		new DefaultTreeItem(project, model).select();
		new ContextMenu("New Child", "Procedure...").select();
		new LabeledText("NewProcedure").setText(procedure);

		return new Procedure(project, model, procedure);
	}

	public Procedure newProcedure(String project, String model, String procedure, boolean procedureNotFunction) {
		open();

		new DefaultTreeItem(project, model).select();
		new ContextMenu("New Child", "Procedure...").select();
		new DefaultShell("Select Procedure Type");

		if (procedureNotFunction) {
			// Procedure?/(Function) - OK
			new PushButton("OK").click();
		}

		new DefaultShell("Create Relational View Procedure");
		new LabeledText("Name").setText(procedure);
		new PushButton("OK").click();
		new WaitWhile(new ShellWithTextIsAvailable("Create Relational View Procedure"));

		return new Procedure(project, model, procedure);
	}

	public void newProcedure(String project, String modelXmi, String procedure, Properties props) {

		open();

		new DefaultTreeItem(project, modelXmi).select();
		new ContextMenu("New Child", "Procedure...").select();
		new DefaultShell("Select Procedure Type");

		new Procedure().create(procedure, props);
		new ModelEditor(modelXmi).save();
	}

	public void addTransformationSource(String project, String model, String tableName) {

		open();
		getProject(project).getProjectItem(model, tableName).select();
	}

	public void executeVDB(String project, String vdb) {
		open();
		vdb = (vdb.contains(".vdb")) ? vdb : vdb + ".vdb";

		new DefaultTreeItem(project, vdb).select();
		new ContextMenu(MODELING_MENU_ITEM, "Execute VDB").select();

		new WaitWhile(new IsInProgress(), TimePeriod.VERY_LONG);
		new WaitWhile(new JobIsRunning(), TimePeriod.LONG);
		new WorkbenchShell();
	}
	
	public void deleteTable(String project, String model, String table){
		open();
		
		new DefaultTreeItem(project, model, table).select();
		new ContextMenu("Delete").select();
		
		new WaitWhile(new IsInProgress(), TimePeriod.VERY_LONG);
		new WaitWhile(new JobIsRunning(), TimePeriod.LONG);
		new WorkbenchShell();
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
	
	
	
	// ### updated down from here
	
	
	
	/**
	 * Selects Modeling/Create Web Service and returns related dialog. 
	 * @param PathFrom - path to model from which WS will be created (<PROJECT>, ..., <ITEM>)
	 * @param IsXml - false - create from relation model 
	 * 				- true - create from XML model
	 */
	public CreateWebServiceDialog modelingWebService(boolean fromXml, String... PathFrom){
		this.selectItem(PathFrom);
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
	public void createModelProject(String projectName) {
		ModelProjectWizard wizard = new ModelProjectWizard(0);
		wizard.create(projectName);
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
		vdbpath[i] = (vdbpath[i].contains(".vdb")) ? vdbpath[i] : vdbpath[i] + ".vdb";
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
	public GenerateDynamicVdbWizard generateDynamicVDB(String... vdbpath) {
		this.selectItem(vdbpath);
		new ContextMenu("Modeling", "Generate Dynamic VDB").select();
		return new GenerateDynamicVdbWizard().activate();
	}
	
	/**
	 * Generates VDB archive from specified VDB
	 * @param vdbpath - path to VDB (<PROJECT>, ..., <VDB>)
	 */
	public GenerateVdbArchiveWizard generateVdbArchive(String... vdbpath) {
		this.selectItem(vdbpath);
		new ContextMenu("Modeling", "Generate VDB Archive and Models").select();
		return new GenerateVdbArchiveWizard().activate();
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
		MetadataModelWizard modelWizard = new MetadataModelWizard();
		modelWizard.open();
		modelWizard.activate();
		modelWizard.setLocation(newModelPath.split("/"))
				.setModelName(newModelName)
				.selectModelClass(newModelClass)
				.selectModelType(newModelType)
				.selectModelBuilder(ModelBuilder.COPY_EXISTING);
		modelWizard.next();
		modelWizard.setExistingModel(originalModelPath.split("/"));
		modelWizard.finish();
		AbstractWait.sleep(TimePeriod.SHORT);
		ShellMenu saveMenu = new ShellMenu("File","Save");
		if (saveMenu.isEnabled()){
			saveMenu.select();
			AbstractWait.sleep(TimePeriod.SHORT);
		}
	}
	
	/**
	 * Adds child to specified item of model's tree structure.
	 * @param pathToModel - path to model (<PROJECT>/.../<MODEL>)
	 * @param pathToItem - path to item (.../<ITEM>) ("" if directly to model)
	 * @param childType - org.jboss.tools.teiid.reddeer.ChildType.*
	 */
	public void addChildToModelItem(String pathToModel, String pathToItem, ChildType childType) {
		this.getProject(pathToModel.split("/")[0]).refresh();
		pathToItem = (pathToItem.equals("")) ? pathToItem : "/" + pathToItem;
		new DefaultTreeItem((pathToModel + pathToItem).split("/")).select();
		new ContextMenu("New Child",childType.getText()).select();
		AbstractWait.sleep(TimePeriod.SHORT);
		KeyboardFactory.getKeyboard().type(KeyEvent.VK_TAB);
		AbstractWait.sleep(TimePeriod.SHORT);
	}
	
	/**
	 * Adds sibling to specified item of model's tree structure.
	 * @param pathToModel - path to model (<PROJECT>/.../<MODEL>)
	 * @param pathToItem - path to item (.../<ITEM>) ("" if directly to model)
	 * @param siblingType - org.jboss.tools.teiid.reddeer.ChildType.*
	 */
	public void addSiblingToModelItem(String pathToModel, String pathToItem, ChildType siblingType){
		this.getProject(pathToModel.split("/")[0]).refresh();
		pathToItem = (pathToItem.equals("")) ? pathToItem : "/" + pathToItem;
		new DefaultTreeItem((pathToModel + pathToItem).split("/")).select();
		new ContextMenu("New Sibling",siblingType.getText()).select();
		AbstractWait.sleep(TimePeriod.SHORT);
		KeyboardFactory.getKeyboard().type(KeyEvent.VK_TAB);
		AbstractWait.sleep(TimePeriod.SHORT);
	}
	
	/**
	 * Renames specified item of model's tree structure.
	 * @param pathToModel - path to model (<PROJECT>/.../<MODEL>)
	 * @param pathToItem - path to item (.../<ITEM>)
	 */
	public void renameModelItem(String pathToModel, String pathToItem, String newName){
		this.getProject(pathToModel.split("/")[0]).refresh();
		pathToItem = (pathToItem.equals("")) ? pathToItem : "/" + pathToItem;
		new DefaultTreeItem((pathToModel + pathToItem).split("/")).select();
		new ContextMenu("Rename...").select();
		AbstractWait.sleep(TimePeriod.SHORT);
		new DefaultText().setText(newName);
		KeyboardFactory.getKeyboard().type(KeyEvent.VK_TAB);
		AbstractWait.sleep(TimePeriod.SHORT);
	}
	
	/**
	 * Renames specified model.
	 * @param pathToModel - path to model (<PROJECT>/.../<MODEL>)
	 * Note: new name must contains extension of model.
	 */
	public void renameModel(String pathToModel, String newName){
		this.getProject(pathToModel.split("/")[0]).refresh();
		new DefaultTreeItem((pathToModel).split("/")).select();
		new ContextMenu("Refactor", "Rename...").select();
		AbstractWait.sleep(TimePeriod.SHORT);
		new LabeledText("New name:").setText(newName);
		new OkButton().click();
		AbstractWait.sleep(TimePeriod.SHORT);
	}
	
	/**
	 * Deletes specified model.
	 * @param pathToModel - path to model (<PROJECT>/.../<MODEL>)
	 */
	public void deleteModel(String pathToModel){
		this.getProject(pathToModel.split("/")[0]).refresh();
		new DefaultTreeItem((pathToModel).split("/")).select();
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
		new ImportProjectWizard(new File("resources/projects/" + projectName).getAbsolutePath()).execute();
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
}
