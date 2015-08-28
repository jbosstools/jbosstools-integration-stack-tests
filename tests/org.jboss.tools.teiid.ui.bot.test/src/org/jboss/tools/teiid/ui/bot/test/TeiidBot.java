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
import org.jboss.reddeer.eclipse.jdt.ui.ProjectExplorer;
import org.jboss.reddeer.eclipse.core.resources.Project;
import org.jboss.reddeer.swt.impl.menu.ShellMenu;
import org.jboss.reddeer.swt.impl.shell.DefaultShell;
import org.jboss.reddeer.common.wait.AbstractWait;
import org.jboss.reddeer.common.wait.TimePeriod;
import org.jboss.tools.teiid.reddeer.ModelProject;
import org.jboss.tools.teiid.reddeer.editor.ModelEditor;
import org.jboss.tools.teiid.reddeer.view.ModelExplorer;
import org.jboss.tools.teiid.reddeer.view.TeiidView;
import org.jboss.tools.teiid.reddeer.wizard.HSQLDBDriverWizard;
import org.jboss.tools.teiid.reddeer.wizard.HSQLDBProfileWizard;

/**
 * Bot operations specific for Teiid Designer.
 * 
 * @author apodhrad
 * 
 */
public class TeiidBot {

	public TeiidView showTeiidView() {//-> perspective + view mgr
		TeiidView teiidView = new TeiidView();
		teiidView.open();
		return teiidView;
	}

	public ModelProject createModelProject(String name) {
		ModelExplorer modelExplorer = new ModelExplorer();
		modelExplorer.open();
		return modelExplorer.createModelProject(name);
	}

	public ModelExplorer modelExplorer() {
		ModelExplorer modelExplorer = new ModelExplorer();
		modelExplorer.open();
		return modelExplorer;
	}

	public ModelEditor modelEditor(String title) {//-> editor mgr
		SWTBotEditor editor = new SWTWorkbenchBot().editorByTitle(title);
		ModelEditor modelEditor = new ModelEditor(editor.getReference(), new SWTWorkbenchBot());
		return modelEditor;
	}

	/*public FlatFileProfile createFlatFileProfile(String name, String folder) {//cp mgr
		FlatFileProfile flatProfile = new FlatFileProfile();
		flatProfile.setName(name);
		flatProfile.setFolder(folder);
		flatProfile.setCharset("UTF-8");
		flatProfile.setStyle("CSV");

		ConnectionProfileWizard connWizard = new ConnectionProfileWizardExt();
		connWizard.open();
		connWizard.createFlatFileProfile(flatProfile);
		return flatProfile;
	}*/

	/*public void createXmlProfile(String name, String path) {//cp
		String xmlProfile = "XML Local File Source";
		if (path.startsWith("http")) {
			xmlProfile = "XML File URL Source";
		}

		TeiidConnectionProfileWizard wizard = new TeiidConnectionProfileWizard();
		wizard.open();

		ConnectionProfileSelectPage selectPage = wizard.getFirstPage();
		selectPage.setConnectionProfile(xmlProfile);
		selectPage.setName(name);

		wizard.next();

		ConnectionProfileXmlPage xmlPage = (ConnectionProfileXmlPage) wizard.getSecondPage();
		xmlPage.setPath(toAbsolutePath(path));

		wizard.finish();
	}*/


	/*public void createDatabaseProfile(String name, String fileName) {//cp mgr
		Properties props = new Properties();
		try {
			props.load(new FileReader(fileName));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return;
		} catch (IOException e) {
			e.printStackTrace();
			return;
		}
		createDatabaseProfile(name, props);

	}*/

	/**
	 * 
	 * @param name of connection profile (e.g. My oracle profile)
	 * @param props
	 */
	/*public void createDatabaseProfile(String name, Properties props) {//cp ext
		DriverTemplate drvTemp = new DriverTemplate(props.getProperty("db.template"),
				props.getProperty("db.version"));

		DriverDefinition driverDefinition = new DriverDefinition();
		driverDefinition.setDriverName(name + "Driver");
		driverDefinition.setDriverTemplate(drvTemp);
		String driverPath = new File(props.getProperty("db.jdbc_path")).getAbsolutePath();
		driverDefinition.setDriverLibrary(driverPath);

		DriverDefinitionPreferencePageExt prefPage = new DriverDefinitionPreferencePageExt();
		prefPage.open();
		prefPage.addDriverDefinition(driverDefinition);
		prefPage.ok();

		DatabaseProfile dbProfile = new DatabaseProfile();
		dbProfile.setDriverDefinition(driverDefinition);
		dbProfile.setName(name);
		dbProfile.setDatabase(props.getProperty("db.name"));
		dbProfile.setHostname(props.getProperty("db.hostname"));
		dbProfile.setUsername(props.getProperty("db.username"));
		dbProfile.setPassword(props.getProperty("db.password"));
		dbProfile.setVendor(props.getProperty("db.vendor"));
		dbProfile.setPort(props.getProperty("db.port"));

		TeiidConnectionProfileWizard wizard = new TeiidConnectionProfileWizard();
		wizard.open();
		wizard.createDatabaseProfile(dbProfile);
	}*/
	
	/**
	 * Create connection profile to HSQL database
	 * @param properties path to properties file (e.g. resources/db/mydb.properties)
	 * @param jdbcProfile name of profile (e.g. HSQLDB Profile)
	 * @param addDriver true if driver should be added
	 * @param setLocksFalse true if locks on database shouldn't be created
	 */ //cp
	public void createHsqlProfile(String properties, String jdbcProfile, boolean addDriver, boolean setLocksFalse){
		//load properties
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
		//create new connection profile to HSQLDB 
		new HSQLDBProfileWizard("HSQLDB Driver", props.getProperty("db.name"),
				props.getProperty("db.hostname")).setUser(props.getProperty("db.username"))
				.setPassword(props.getProperty("db.password")).setName(jdbcProfile).create(setLocksFalse);
	}
	
	

	public String toAbsolutePath(String path) {
		return new File(path).getAbsolutePath();
	}

	//private class ConnectionProfileWizardExt extends ConnectionProfileWizard {//remove, cp

		/*@Override
		public void createFlatFileProfile(FlatFileProfile flatProfile) {
			ConnectionProfileSelectPage selectPage = getFirstPage();
			selectPage.setConnectionProfile("Flat File Data Source");
			selectPage.setName(flatProfile.getName());

			next();

			ConnectionProfileFlatFilePage flatPage = (ConnectionProfileFlatFilePage) getSecondPage();

			// TODO: LabeledText
			// flatPage.setHomeFolder(flatProfile.getFolder());
			new SWTWorkbenchBot().text().setText(new File(flatProfile.getFolder()).getAbsolutePath());//should be absolute path!
			
			//switch off validation of home folder
			new CheckBox("Validate home folder").click();
			
			flatPage.setCharset(flatProfile.getCharset());
			flatPage.setStyle(flatProfile.getStyle());

			finish();
		}*/

	//}
	
	/*public void createFlatFileProfileExt(FlatFileProfileExt flatProfile) {
		ConnectionProfileSelectPage selectPage = getFirstPage();
		selectPage.setConnectionProfile(FLAT_FILE_DATA_SOURCE);
		selectPage.setName(flatProfile.getName());

		next();

		ConnectionProfileFlatFilePage flatPage = (ConnectionProfileFlatFilePage) getSecondPage();

		if (flatProfile.getFolder() != null) {
			// TODO: LabeledText
			flatPage.setHomeFolder(flatProfile.getFolder());
		} else if (flatProfile.getURI() != null){
			new SWTWorkbenchBot().text().setText(new File(flatProfile.getFolder()).getAbsolutePath());//should be absolute path!						
		}
		if (flatProfile.isValidateHomeFolder() == false) {
			// switch off validation of home folder
			new CheckBox("Validate home folder").click();
		}

		flatPage.setCharset(flatProfile.getCharset());//not null, set in constructor
		flatPage.setStyle(flatProfile.getStyle());

		finish();
	}*/
	
	/**
	 * Save all
	 */
	public void saveAll(){
		new ShellMenu("File", "Save All").select();
	}

	/**
	 * Confirm active shell, if appears
	 */
	public void closeActiveShell(){
		try {
			new SWTWorkbenchBot().activeShell().bot().button("Yes").click();
		} catch (Exception e){
			System.out.println(e.getMessage());
		}
	}
	
	public Properties getProperties(String fileName){
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
	
	public void assertResource(String projectName, String... path) {//-> teiidbot
		AbstractWait.sleep(TimePeriod.SHORT);
		new DefaultShell();
		ModelProject modelproject = new ModelExplorer().getModelProject(projectName);
		assertTrue(Arrays.toString(path) + " not created!", modelproject.containsItem(path));
	}
	
	public boolean checkResource(String projectName, String... path) {//-> teiidbot
		new SWTWorkbenchBot().sleep(500);
		ModelProject modelproject = new ModelExplorer().getModelProject(projectName);
		return modelproject.containsItem(path);
	}
	
	public void assertFailResource(String projectName, String... path) {//-> teiidbot
		new SWTWorkbenchBot().sleep(500);
		ModelProject modelproject = new ModelExplorer().getModelProject(projectName);
		assertFalse(Arrays.toString(path) + " is created but should not be!", modelproject.containsItem(path));
	}
	
	public boolean checkResourceNotPresent(String projectName, String... path) {//-> teiidbot
		new SWTWorkbenchBot().sleep(500);
		ModelProject modelproject = new ModelExplorer().getModelProject(projectName);
		return modelproject.containsItem(path);
	}

	public void checkDiagram(String projectName, String file, String label) {//-> teiidbot
		new SWTWorkbenchBot().sleep(500);
		Project project = new ProjectExplorer().getProject(projectName);
		project.getProjectItem(file).open();
		ModelEditor modelEditor = this.modelEditor(file);
		assertNotNull(file + " is not opened!", modelEditor);
		assertNotNull("Diagram '" + label + "' not found!", modelEditor.getModeDiagram(label));
	}
	
	public String curl(String url) {
		Process pr = null;
		String result = "";
		Runtime run = Runtime.getRuntime();
		try {
			pr = run.exec("curl "+url);
			BufferedReader in = new BufferedReader(new InputStreamReader(
					pr.getInputStream()));
			String line = null;
			
			while ((line = in.readLine()) != null) {
				result=result.concat(line);
			}
		} catch (Exception ex) {
			// throw new RuntimeException("Executing " + cmdline, ex);
		} finally {
			try {
				// close all those bloody streams
				pr.getErrorStream().close();
				pr.getInputStream().close();
				pr.getOutputStream().close();
			} catch (Exception ex) {
				// Log.get().exception(Log.Level.Error, "Closing stream: ", ex);
			}
		}
		System.out.println("-----Curl returned: "+result);
		return result;
	}
	
	public void uncheckBuildAutomatically(){
		if (new ShellMenu("Project", "Build Automatically").isSelected()){
			new ShellMenu("Project", "Build Automatically").select();//ie unselect
		}
	}
	
	public String loadFileAsString(String fileName){
		String result = "";
		File f = new File(fileName);
		try {
			BufferedReader in = new BufferedReader(new FileReader(f));
			String line = null;
			
			while ((line = in.readLine()) != null) {
				result=result.concat(line);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}
	
	public String generateProcedurePreviewQuery(String modelName, String procedureName, ArrayList<String> procedureInputParams){
		String params = "";
		for (int i = 0; i < procedureInputParams.size(); i++){
			params+="'"+procedureInputParams.get(i)+"',";
		}
		params=params.substring(0, params.length() - 1);
		
		return "select * from ( exec \"" +modelName+"\".\""+procedureName+"\"("+params+") ) AS X_X";
	}
	
	public String generateTablePreviewQuery(String modelName, String tableName){
		return "select * from \""+modelName+"\".\""+tableName+"\"";
	}
	
	//TODO generate mapping class preview - input set editor params
}
