package org.jboss.tools.teiid.ui.bot.test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Properties;

import org.eclipse.swtbot.eclipse.finder.SWTWorkbenchBot;
import org.eclipse.swtbot.eclipse.finder.widgets.SWTBotEditor;
import org.jboss.reddeer.common.wait.AbstractWait;
import org.jboss.reddeer.common.wait.TimePeriod;
import org.jboss.reddeer.core.handler.ShellHandler;
import org.jboss.reddeer.eclipse.core.resources.Project;
import org.jboss.reddeer.eclipse.jdt.ui.ProjectExplorer;
import org.jboss.reddeer.swt.impl.menu.ShellMenu;
import org.jboss.reddeer.swt.impl.shell.DefaultShell;
import org.jboss.reddeer.workbench.impl.editor.AbstractEditor;
import org.jboss.reddeer.workbench.impl.shell.WorkbenchShell;
import org.jboss.tools.teiid.reddeer.ModelProject;
import org.jboss.tools.teiid.reddeer.VDB;
import org.jboss.tools.teiid.reddeer.connection.TeiidJDBCHelper;
import org.jboss.tools.teiid.reddeer.editor.ModelEditor;
import org.jboss.tools.teiid.reddeer.editor.VDBEditor;
import org.jboss.tools.teiid.reddeer.requirement.TeiidServerRequirement;
import org.jboss.tools.teiid.reddeer.view.ModelExplorer;
import org.jboss.tools.teiid.reddeer.view.TeiidView;
import org.jboss.tools.teiid.reddeer.wizard.CreateVDB;
import org.jboss.tools.teiid.reddeer.wizard.HSQLDBDriverWizard;
import org.jboss.tools.teiid.reddeer.wizard.HSQLDBProfileWizard;

/**
 * Bot operations specific for Teiid Designer.
 * 
 * @author apodhrad
 * 
 */
public class TeiidBot {

	@Deprecated // use ModelExplorer.createModelProject()
	public void createModelProject(String name) {
		ModelExplorer modelExplorer = new ModelExplorer();
		modelExplorer.open();
		modelExplorer.createModelProject(name);
	}

	public ModelExplorer modelExplorer() {
		ModelExplorer modelExplorer = new ModelExplorer();
		modelExplorer.open();
		return modelExplorer;
	}

	public ModelEditor modelEditor(String title) {// -> editor mgr
		SWTBotEditor editor = new SWTWorkbenchBot().editorByTitle(title);
		ModelEditor modelEditor = new ModelEditor(editor.getReference(), new SWTWorkbenchBot());
		return modelEditor;
	}

	/**
	 * Create connection profile to HSQL database
	 * 
	 * @param properties
	 *            path to properties file (e.g. resources/db/mydb.properties)
	 * @param jdbcProfile
	 *            name of profile (e.g. HSQLDB Profile)
	 * @param addDriver
	 *            true if driver should be added
	 * @param setLocksFalse
	 *            true if locks on database shouldn't be created
	 */ // cp
	public void createHsqlProfile(String properties, String jdbcProfile, boolean addDriver, boolean setLocksFalse) {
		// load properties
		Properties props = new Properties();
		try {
			props.load(new FileReader(properties));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return;
		} catch (IOException e) {
			e.printStackTrace();
			return;
		}
		if (addDriver) {
			// create new generic driver for HSQL
			new HSQLDBDriverWizard(props.getProperty("db.jdbc_path")).create();
		}
		// create new connection profile to HSQLDB
		new HSQLDBProfileWizard("HSQLDB Driver", props.getProperty("db.name"), props.getProperty("db.hostname"))
				.setUser(props.getProperty("db.username")).setPassword(props.getProperty("db.password"))
				.setName(jdbcProfile).create(setLocksFalse);
	}

	public String toAbsolutePath(String path) {
		return new File(path).getAbsolutePath();
	}

	/**
	 * Save all
	 */
	public void saveAll() {
		new ShellMenu("File", "Save All").select();
	}

	/**
	 * Confirm active shell, if appears
	 */
	public void closeActiveShell() {
		try {
			new SWTWorkbenchBot().activeShell().bot().button("Yes").click();
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}

	public Properties getProperties(String fileName) {
		Properties props = new Properties();
		try {
			props.load(new FileReader(fileName));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return props;
	}

	public void assertResource(String projectName, String... path) {// -> teiidbot
		AbstractWait.sleep(TimePeriod.SHORT);
		new DefaultShell();
		ModelProject modelproject = new ModelExplorer().getModelProject(projectName);
		assertTrue(Arrays.toString(path) + " not created!", modelproject.containsItem(path));
	}

	public boolean checkResource(String projectName, String... path) {// -> teiidbot
		new SWTWorkbenchBot().sleep(500);
		ModelProject modelproject = new ModelExplorer().getModelProject(projectName);
		return modelproject.containsItem(path);
	}

	public void assertFailResource(String projectName, String... path) {// -> teiidbot
		new SWTWorkbenchBot().sleep(500);
		ModelProject modelproject = new ModelExplorer().getModelProject(projectName);
		assertFalse(Arrays.toString(path) + " is created but should not be!", modelproject.containsItem(path));
	}

	public boolean checkResourceNotPresent(String projectName, String... path) {// -> teiidbot
		new SWTWorkbenchBot().sleep(500);
		ModelProject modelproject = new ModelExplorer().getModelProject(projectName);
		return modelproject.containsItem(path);
	}

	public void checkDiagram(String projectName, String file, String label) {// -> teiidbot
		new SWTWorkbenchBot().sleep(500);
		Project project = new ProjectExplorer().getProject(projectName);
		project.getProjectItem(file).open();
		ModelEditor modelEditor = this.modelEditor(file);
		assertNotNull(file + " is not opened!", modelEditor);
		assertNotNull("Diagram '" + label + "' not found!", modelEditor.getModeDiagram(label));
	}

	public void uncheckBuildAutomatically() {
		if (new ShellMenu("Project", "Build Automatically").isSelected()) {
			new ShellMenu("Project", "Build Automatically").select();// ie unselect
		}
	}

	@Deprecated // use ResourceFileHelper
	public String loadFileAsString(String fileName) {
		String result = "";
		File f = new File(fileName);
		try (BufferedReader in = new BufferedReader(new FileReader(f))) {
			;
			String line = null;

			while ((line = in.readLine()) != null) {
				result = result.concat(line);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	public String generateProcedurePreviewQuery(String modelName, String procedureName,
			ArrayList<String> procedureInputParams) {
		String params = "";
		for (int i = 0; i < procedureInputParams.size(); i++) {
			params += "'" + procedureInputParams.get(i) + "',";
		}
		params = params.substring(0, params.length() - 1);

		return "exec \"" + modelName + "\".\"" + procedureName + "\"(" + params + ")";
	}

	public String generateTablePreviewQuery(String modelName, String tableName) {
		return "select * from \"" + modelName + "\".\"" + tableName + "\"";
	}

	/**
	 * Method for simple check if model is correct. This method creates VDB with supplied model and submits simple
	 * query.
	 * 
	 * @param teiidServer
	 *            running teiid instance
	 * @param project
	 *            project with desired model
	 * @param model
	 *            supplied model for testing
	 * @param tables
	 *            tables to be queried
	 */
	public void simulateTablesPreview(TeiidServerRequirement teiidServer, String project, String model, String[] tables) {

		String vdb_name = "Check_" + model;

		// create VDB
		CreateVDB createVDB = new CreateVDB();
		createVDB.setFolder(project);
		createVDB.setName(vdb_name);
		createVDB.execute(true);

		// add models to the vdb
		VDBEditor editor = VDBEditor.getInstance(vdb_name + ".vdb");
		editor.show();
		editor.addModel(project, model);
		editor.save();

		VDB vdb = new ModelExplorer().getModelProject(project).getVDB(vdb_name + ".vdb");
		vdb.executeVDB();

		TeiidJDBCHelper jdbcHelper = new TeiidJDBCHelper(teiidServer, vdb_name);

		// try simple select for every table
		for (int i = 0; i < tables.length; i++) {
			String previewSQL = generateTablePreviewQuery(model, tables[i]);
			assertTrue(jdbcHelper.isQuerySuccessful(previewSQL,true));
		}

	}
	
	public void simulateProcedurePreview(TeiidServerRequirement teiidServer, String project, String model, String procedure, String...parameters) {

		String vdb_name = "Check_" + model;

		// create VDB
		CreateVDB createVDB = new CreateVDB();
		createVDB.setFolder(project);
		createVDB.setName(vdb_name);
		createVDB.execute(true);

		// add models to the vdb
		VDBEditor editor = VDBEditor.getInstance(vdb_name + ".vdb");
		editor.show();
		editor.addModel(project, model);
		editor.save();

		VDB vdb = new ModelExplorer().getModelProject(project).getVDB(vdb_name + ".vdb");
		vdb.executeVDB();

		TeiidJDBCHelper jdbcHelper = new TeiidJDBCHelper(teiidServer, vdb_name);
		
		ArrayList<String> parametersList = new ArrayList<String>(Arrays.asList(parameters));

		// try simple query
		String previewSQL = generateProcedurePreviewQuery(model, procedure, parametersList);
		assertTrue(jdbcHelper.isQuerySuccessful(previewSQL,false));

	}

	public ModelEditor openModelEditor(String projectName, String modelName) {
		ModelExplorer modelExplorer = new ModelExplorer();
		modelExplorer.open();
		Project project = modelExplorer.getProject(projectName);
		project.getProjectItem(modelName + ".xmi").open();

		ModelEditor editor = modelEditor(modelName + ".xmi");
		return editor;
	}

	public void deleteProjectSafely(String projectName) {
		/*
		 * Saving and closing all editors sometimes does not work for some reason until we focus an editor. However, we
		 * are not sure an editor is even open, so we first try to focus one, then save all and then close all. This
		 * will hopefully ensure that all editors are closed before attempting to delete the project.
		 */
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
		new ModelExplorer();

		Project project = modelExplorer().getProject(projectName);
		project.select();
		project.refresh();
		project.delete(true);

	}

	// TODO generate mapping class preview - input set editor params
}
