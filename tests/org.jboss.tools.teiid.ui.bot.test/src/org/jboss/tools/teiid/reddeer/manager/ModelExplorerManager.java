package org.jboss.tools.teiid.reddeer.manager;

import java.util.List;

import org.eclipse.swtbot.eclipse.finder.SWTWorkbenchBot;
import org.eclipse.swtbot.swt.finder.keyboard.Keystrokes;
import org.jboss.reddeer.common.matcher.RegexMatcher;
import org.jboss.reddeer.common.wait.AbstractWait;
import org.jboss.reddeer.common.wait.TimePeriod;
import org.jboss.reddeer.common.wait.WaitWhile;
import org.jboss.reddeer.core.condition.ShellWithTextIsActive;
import org.jboss.reddeer.swt.impl.button.PushButton;
import org.jboss.reddeer.swt.impl.menu.ContextMenu;
import org.jboss.reddeer.swt.impl.menu.ShellMenu;
import org.jboss.reddeer.swt.impl.shell.DefaultShell;
import org.jboss.reddeer.swt.impl.text.DefaultText;
import org.jboss.reddeer.swt.impl.tree.DefaultTree;
import org.jboss.reddeer.swt.impl.tree.DefaultTreeItem;
import org.jboss.tools.teiid.reddeer.ModelBuilder;
import org.jboss.tools.teiid.reddeer.ModelClass;
import org.jboss.tools.teiid.reddeer.ModelProject;
import org.jboss.tools.teiid.reddeer.ModelType;
import org.jboss.tools.teiid.reddeer.Procedure;
import org.jboss.tools.teiid.reddeer.WAR;
import org.jboss.tools.teiid.reddeer.condition.IsInProgress;
import org.jboss.tools.teiid.reddeer.perspective.DatabaseDevelopmentPerspective;
import org.jboss.tools.teiid.reddeer.view.ModelExplorer;
import org.jboss.tools.teiid.reddeer.view.SQLResult;
import org.jboss.tools.teiid.reddeer.wizard.MetadataModelWizard;
import org.jboss.tools.teiid.reddeer.wizard.ModelProjectWizard;
import org.jboss.tools.teiid.reddeer.wizard.SaveAsDialog;

public class ModelExplorerManager {

	/**
	 * Click on the project in Model Explorer
	 * 
	 * @return
	 */
	public ModelProject selectModelProject(String modelProjectName) {
		ModelExplorer me = new ModelExplorer();
		me.open();
		return me.getModelProject(modelProjectName);
	}

	/**
	 * New Teiid Model Project
	 * 
	 * @param modelProjectName
	 */
	public void createProject(String modelProjectName, boolean viaGuides) {
		new ModelProjectWizard(0).create(modelProjectName, viaGuides);
	}

	public void createProject(String modelProjectName) {
		new ModelProjectWizard(0).create(modelProjectName);
	}

	public void createProject(String modelProjectName, boolean viaGuides, String... folders) {
		new ModelProjectWizard(0).create(modelProjectName, viaGuides, folders);
	}

	public void changeConnectionProfile(String connectionProfile, String projectName, String... projectItem) {
		new ModelExplorer().changeConnectionProfile(connectionProfile, projectName, projectItem);
	}

	public void createDataSource(String modelExplorerViewConnSourceType, String connProfile,
			String... pathToSourceModel) {
		new ModelExplorer().createDataSource(modelExplorerViewConnSourceType, connProfile, pathToSourceModel);
		;
	}

	public void openModel(String... pathToModel) {
		if (!pathToModel[pathToModel.length - 1].contains(".")) {
			pathToModel[pathToModel.length - 1] = pathToModel[pathToModel.length - 1] + ".xmi";
		}
		new ModelExplorer().open(pathToModel);
	}

	public WAR getWAR(String projectName, String warName) {
		return new WAR(projectName, warName);
	}

	public Procedure getProcedure(String project, String model, String procedure) {
		new ModelExplorer().open();
		new DefaultTreeItem(new DefaultTree(0), project, model, procedure).select();
		return new Procedure(project, model, procedure);
	}

	public Procedure createProcedure(String project, String model, String procedure) {
		return new ModelExplorer().newProcedure(project, model, procedure, true);
	}

	public ModelExplorer getModelExplorerView() {
		return new ModelExplorer();
	}

	public void previewModelObject(List<String> params, String... pathToObject) {
		new ModelExplorer().open();
		new DefaultTreeItem(new DefaultTree(0), pathToObject).select();
		new ContextMenu("Modeling", "Preview Data").select();
		try {
			new PushButton("Yes").click();
		} catch (Exception e) {

		}

		if ((params != null) && (!params.isEmpty())) {
			new DefaultShell("Preview Data");
			int i = 0;
			for (String paramName : params) {// expects the params are sorted
				new SWTWorkbenchBot().text(i).setText(paramName);
				i++;
			}
			new PushButton("OK").click();
		}

	}

	public boolean checkPreviewOfModelObject(String previewSQL) {
		// wait while is in progress
		new WaitWhile(new IsInProgress(), TimePeriod.LONG);
		// wait while dialog Preview data... is active
		new WaitWhile(new ShellWithTextIsActive(new RegexMatcher("Preview.*")), TimePeriod.LONG);
		SQLResult result = DatabaseDevelopmentPerspective.getInstance().getSqlResultsView().getByOperation(previewSQL);
		return result.getStatus().equals(SQLResult.STATUS_SUCCEEDED);
	}
	
	/**
	 * Performs save model as action. 
	 * @param originalModelPath - <PROJECT_NAME>/<FOLDER(S)>/<NAME>.<EXT> 
	 * 		- where the original model is located including it's name
	 * @param newModelName - name of the new model
	 * @param newModelPath - <PROJECT_NAME>/<FOLDER(S)> - where the new model will be located
	 * @param isSchemaModel - whether original model is XML Schema Model or not (for dialog purposes)
	 */
	public void saveModelAs(String originalModelPath, String newModelName, String newModelPath, 
			boolean isSchemaModel){
		this.getModelExplorerView().open(originalModelPath.split("/"));
		SaveAsDialog saveAsDialog = new SaveAsDialog(isSchemaModel);
		saveAsDialog.open();
		saveAsDialog.activate();
		saveAsDialog.setLocation(newModelPath.split("/"));
		saveAsDialog.setModelName(newModelName);
		saveAsDialog.ok();
	}
	
	/**
	 * Performs copy model action. 
	 * @param String originalModelPath - <PROJECT_NAME>/<FOLDER(S)>/<NAME>.<EXT> 
	 * 		- where the original model is located and its name
	 * @param newModelName - name of the new model
	 * @param newModelPath - <PROJECT_NAME>/<FOLDER(S)> - where the new model will be located
	 * @param newModelClass - model class of the new model
	 * @param newModelType - model type of the new model
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
		ShellMenu saveAllMenu = new ShellMenu("File","Save All");
		if (saveAllMenu.isEnabled()){
			saveAllMenu.select();
		}
	}
	
	/**
	 * Adds child to specified item of model's tree structure.
	 * @param itemPath - path to item as array
	 * @param childType - type of new child
	 */
	public void addChildToItem(String[] itemPath, String childType) {
		this.getModelExplorerView().getProject(itemPath[0]).refresh();
		new DefaultTreeItem(itemPath).select();
		new ContextMenu("New Child",childType).select();
		new SWTWorkbenchBot().activeShell().pressShortcut(Keystrokes.TAB);
	}
	
	/**
	 * Renames specified item of model's tree structure.
	 * @param itemPath - path to item as array
	 * @param newName - value of new name
	 */
	public void renameItem(String[] itemPath, String newName){
		this.getModelExplorerView().getProject(itemPath[0]).refresh();
		new DefaultTreeItem(itemPath).select();
		new ContextMenu("Rename...").select();
		new DefaultText().setText(newName);
		new SWTWorkbenchBot().activeShell().pressShortcut(Keystrokes.TAB);
	}
}
