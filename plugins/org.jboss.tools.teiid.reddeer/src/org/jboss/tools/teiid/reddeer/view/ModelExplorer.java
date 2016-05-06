package org.jboss.tools.teiid.reddeer.view;

import java.awt.event.KeyEvent;
import java.io.File;
import java.util.Properties;

import org.eclipse.swtbot.eclipse.finder.SWTWorkbenchBot;
import org.jboss.reddeer.common.wait.AbstractWait;
import org.jboss.reddeer.common.wait.TimePeriod;
import org.jboss.reddeer.common.wait.WaitUntil;
import org.jboss.reddeer.common.wait.WaitWhile;
import org.jboss.reddeer.core.condition.JobIsRunning;
import org.jboss.reddeer.core.condition.ShellWithTextIsAvailable;
import org.jboss.reddeer.core.handler.ShellHandler;
import org.jboss.reddeer.eclipse.jdt.ui.AbstractExplorer;
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
import org.jboss.tools.teiid.reddeer.VDB;
import org.jboss.tools.teiid.reddeer.condition.IsInProgress;
import org.jboss.tools.teiid.reddeer.condition.RadioButtonEnabled;
import org.jboss.tools.teiid.reddeer.editor.ModelEditor;
import org.jboss.tools.teiid.reddeer.wizard.ImportProjectWizard;
import org.jboss.tools.teiid.reddeer.wizard.MetadataModelWizard;
import org.jboss.tools.teiid.reddeer.wizard.ModelProjectWizard;
import org.jboss.tools.teiid.reddeer.wizard.SaveAsDialog;

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
	 * Creates a new model project
	 * 
	 * @param modelName
	 * @return
	 */
	public ModelProject createModelProject(String modelName) {
		ModelProjectWizard wizard = new ModelProjectWizard(0);
		wizard.create(modelName);
		return getModelProject(modelName);
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

		new DefaultTreeItem(project, vdb).select();
		new ContextMenu(MODELING_MENU_ITEM, "Execute VDB").select();

		new WaitWhile(new IsInProgress(), TimePeriod.VERY_LONG);
		new WaitWhile(new JobIsRunning(), TimePeriod.LONG);
		new WorkbenchShell();
	}

	

	@Override
	public void open() {
		super.open();
		new WorkbenchShell();
	}

	public void open(String... filePath) {
		open();

		new SWTWorkbenchBot().tree(0).expandNode(filePath).doubleClick();
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
	
	
	
	

	
	
	public void deployVdb(String project, String vdb) {
		vdb = (vdb.contains(".vdb")) ? vdb : vdb + ".vdb";
		new VDB(this.getProject(project).getProjectItem(vdb)).deployVDB();
	}
	
	/**
	 * Performs save model as action. 
	 * 
	 * @param originalModelPath - full path to original model (starts with project name) 
	 * @param newModelName - name of the new model
	 * @param newModelPath - full path to folder where new model will be located (starts with project name) 
	 * @param isSchemaModel - whether original model is XML Schema Model or not (for dialog purposes)
	 */
	public void saveModelAs(String originalModelPath, String newModelName, String newModelPath, boolean isSchemaModel){
		this.open(originalModelPath.split("/"));
		SaveAsDialog saveAsDialog = new SaveAsDialog(isSchemaModel);
		saveAsDialog.open();
		saveAsDialog.activate();
		saveAsDialog.setLocation(newModelPath.split("/"));
		saveAsDialog.setModelName(newModelName);
		saveAsDialog.ok();
		AbstractWait.sleep(TimePeriod.SHORT);
	}
	
	/**
	 * Performs copy model action. 
	 * 
	 * @param originalModelPath - full path to original model (starts with project name) 
	 * @param newModelName - name of the new model
	 * @param newModelPath - full path to folder where new model will be located (starts with project name) 
	 * @param newModelClass - org.jboss.tools.teiid.reddeer.ModelClass.*
	 * @param newModelType - org.jboss.tools.teiid.reddeer.ModelType.*
	 */
	public void copyModel(String originalModelPath, String newModelName, String newModelPath, 
		ModelClass newModelClass, ModelType newModelType){
		MetadataModelWizard modelWizard = new MetadataModelWizard();
		modelWizard.open();
		modelWizard.activate();
		modelWizard.setLocation(newModelPath.split("/"));
		modelWizard.setModelName(newModelName);
		modelWizard.selectModelClass(newModelClass);
		modelWizard.selectModelType(newModelType);
		modelWizard.selectModelBuilder(ModelBuilder.COPY_EXISTING);
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
	 * 
	 * @param pathToModel - path to model (starts with project name)
	 * @param pathToItem - path to item (in model) ("" if directly to model)
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
	 * Renames specified item of model's tree structure.
	 * 
	 * @param pathToModel - path to model (starts with project name)
	 * @param pathToItem - path to item (in model)
	 * @param newName - value of new name
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
	 * Imports project into workspace (directory or archive)
	 * 
	 * @param location - resource URL (resources/...)
	 */
	public void importProject(String location) {
		new ImportProjectWizard(new File(location).getAbsolutePath()).execute();
	}
	
	/**
	 * Saving and closing all editors sometimes does not work for some reason until we focus an editor. However, we
	 * are not sure an editor is even open, so we first try to focus one, then save all and then close all. This
	 * will hopefully ensure that all editors are closed before attempting to delete the project.
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
		} catch (Exception ex) {
			// no editors open, ignore
		}
		new WorkbenchShell();
		this.activate();
		this.deleteAllProjects();
	}
}
