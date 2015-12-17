package org.jboss.tools.teiid.reddeer.view;

import java.util.Properties;

import org.eclipse.swtbot.eclipse.finder.SWTWorkbenchBot;
import org.jboss.reddeer.eclipse.jdt.ui.AbstractExplorer;
import org.jboss.reddeer.core.condition.JobIsRunning;
import org.jboss.reddeer.core.condition.ShellWithTextIsAvailable;
import org.jboss.reddeer.swt.impl.button.PushButton;
import org.jboss.reddeer.swt.impl.button.RadioButton;
import org.jboss.reddeer.swt.impl.combo.DefaultCombo;
import org.jboss.reddeer.swt.impl.menu.ContextMenu;
import org.jboss.reddeer.swt.impl.shell.DefaultShell;
import org.jboss.reddeer.swt.impl.text.LabeledText;
import org.jboss.reddeer.swt.impl.tree.DefaultTreeItem;
import org.jboss.reddeer.workbench.impl.shell.WorkbenchShell;
import org.jboss.reddeer.common.wait.TimePeriod;
import org.jboss.reddeer.common.wait.WaitUntil;
import org.jboss.reddeer.common.wait.WaitWhile;
import org.jboss.tools.teiid.reddeer.ModelProject;
import org.jboss.tools.teiid.reddeer.Procedure;
import org.jboss.tools.teiid.reddeer.Table;
import org.jboss.tools.teiid.reddeer.condition.IsInProgress;
import org.jboss.tools.teiid.reddeer.condition.RadioButtonEnabled;
import org.jboss.tools.teiid.reddeer.editor.ModelEditor;
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
	
	public void changeConnectionProfile(String connectionProfile, String projectName, String... projectItem){
		//if no extension specified, add .xmi
		if (! projectItem[projectItem.length -1].contains(".")){
			projectItem[projectItem.length -1] = projectItem[projectItem.length -1].concat(".xmi");
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
	 * @param project
	 * @param model
	 * @param tableName
	 * @param baseTable true if context menu contains "Base Table"
	 */
	public void newBaseTable(String project, String model, String tableName, boolean baseTable) {
		open();

		new DefaultTreeItem(project, model).select();
		if (baseTable){
			new ContextMenu("New Child", "Base Table...").select();
		} else {
			new ContextMenu("New Child", "Table...").select();
		}
		
		new DefaultShell("Create Relational View Table");
		new LabeledText("Name").setText(tableName);
		new PushButton("OK").click();
	}
	
	public void newTable(String tableName, Table.Type type, Properties props, String... pathToModelXmi){
		open();

		new DefaultTreeItem(pathToModelXmi).select();
		new ContextMenu("New Child", "Table...").select();
		new Table().create(type, tableName, props);
		new ModelEditor(pathToModelXmi[pathToModelXmi.length - 1]).save();//the last member is modelXmi
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
		
		if (procedureNotFunction){
			//Procedure?/(Function) - OK
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

	public void deployVDB(String project, String vdb) {
		open();

		new DefaultTreeItem(project, vdb).select();
		new ContextMenu(MODELING_MENU_ITEM, "Deploy").select();

		new WaitWhile(new IsInProgress(), TimePeriod.VERY_LONG);
		new WaitWhile(new JobIsRunning(), TimePeriod.LONG);
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

	public void openTransformationDiagram(String project, String... filePath) {
		open();

		getProject(project).getProjectItem(filePath).getChild("Transformation Diagram").open();
	}
	
	public void createDataSource(String connectionSourceType, String connectionProfile, String... pathToSourceModel){
		open();
		//if last without ., set to .xmi
		if (! pathToSourceModel[pathToSourceModel.length-1].contains(".")){
			pathToSourceModel[pathToSourceModel.length-1] = pathToSourceModel[pathToSourceModel.length-1].concat(".xmi");
		}
		new WorkbenchShell();
		new DefaultTreeItem(pathToSourceModel).select();
		new ContextMenu(MODELING_MENU_ITEM, CREATE_DATA_SOURCE).select();
		//wait until radio button is enabled
		new WaitUntil(new RadioButtonEnabled(connectionSourceType), TimePeriod.NORMAL);
		new RadioButton(connectionSourceType).click();
		//in case of connection profile -> choose connection profile
		if (connectionSourceType.toString().equals(ConnectionSourceType.USE_CONNECTION_PROFILE_INFO)){
			new DefaultCombo(1).setSelection(connectionProfile);
		}
		if (! new PushButton("OK").isEnabled()){
			System.err.println("Datasource " + pathToSourceModel[pathToSourceModel.length-1] + "exists!");
			new PushButton("Cancel").click();
		} else {
			new PushButton("OK").click();
		}
		
	}
}
