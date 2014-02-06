package org.jboss.tools.teiid.reddeer.wizard;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;
import java.util.Properties;

import org.eclipse.swtbot.swt.finder.widgets.SWTBotTable;
import org.jboss.reddeer.eclipse.jface.wizard.ImportWizardDialog;
import org.jboss.reddeer.swt.api.Table;
import org.jboss.reddeer.swt.impl.button.PushButton;
import org.jboss.reddeer.swt.impl.table.DefaultTable;
import org.jboss.reddeer.swt.impl.text.LabeledText;
import org.eclipse.swtbot.eclipse.finder.SWTWorkbenchBot;
import org.jboss.reddeer.swt.wait.TimePeriod;
import org.jboss.reddeer.swt.wait.WaitWhile;
import org.jboss.tools.teiid.reddeer.condition.IsInProgress;


/**
 * Teiid connection import wizard
 * @author lfabriko
 *
 */
public class TeiidConnectionImportWizard extends ImportWizardDialog{

	private Properties dataSourceProperties;
	//dataSourceName, driverName, <other vendor specific properties -- key: Name, value: Value>
	private Boolean createNewDataSource;
	private Boolean deleteDataSource;
	private Boolean editDataSource;
	private Boolean copyDataSource;//if true then name of copied ds required
	private String copiedDataSourceName;
	private String newDataSourceName;
	private String driverName;
	private String modelName;
	private String dataSourceName;
	private String[] tablesToImport;//e.g. "Object to Create/ACCOUNT", "Object to Create/CUSTOMER"
	
	// type of data source to be imported
	public static final String SQLSERVER_TYPE = "SQLSERVER";
	
	/**
	 * Invoke teiid connection importer
	 */
	public TeiidConnectionImportWizard() {
		super("Teiid Designer", "Teiid Connection >> Source Model");
	}
	
	public void execute(){
		open();
		fillFirstPage();//select ds or create new, edit,...
		next();
		fillSecondPage();//set target name
		new WaitWhile(new IsInProgress(), TimePeriod.LONG);
		next();
		fillThirdPage();//generate ddl
		next();
		fillFourthPage();//choose items to be imported
		finish();
	}
	
	public void fillFirstPage(){
		if (createNewDataSource != null){
			createNewDataSource();
		}
		if (dataSourceName != null){
			Table table = new DefaultTable(0);
			int originalRowCount = table.rowCount();
			for (int i = 0; i < originalRowCount; i++){
				if (table.getItem(i).getText(0).equals(dataSourceName)){
					table.select(i);
					break;
				}
			}
			
			//do something with selected datasource
			if (deleteDataSource){
				deleteDataSource();
			}
			else if (editDataSource){
				editDataSource();
			}
			else if (copyDataSource){
				copyDataSource();
			}	
		}
	}
	
	private void deleteDataSource(){
		new PushButton("Delete").click();
		//TODO
	}
	
	private void editDataSource(){
		new PushButton("Edit...").click();
		//TODO
	}
	
	private void copyDataSource(){
		new PushButton("Copy...").click();
		//TODO
	}
	
	private void createNewDataSource(){
		new PushButton("New...").click();
		new LabeledText("Name:").setText(newDataSourceName);
		
		//set driver
		Table table = new DefaultTable(0);
		int originalRowCount = table.rowCount();
		
		//default drivers
		for (int i = 0; i < originalRowCount; i++){
			if (table.getItem(i).getText(0).equals(driverName)){
				table.select(i);
				break;
			}
		}
		
		//TODO add driver --> add before
		
		new WaitWhile(new IsInProgress(), TimePeriod.NORMAL);
		
		addDataSource();
		
		new PushButton("OK").click();
	}
	
	private void addDataSource(){
		Table table = new DefaultTable(1);
		
		for (int i = 0; i < table.rowCount(); i++){
			if (dataSourceProperties.keySet().contains(table.getItem(i).getText(0))){
				table.select(i);
				SWTBotTable t = new SWTWorkbenchBot().table(1);
				t.doubleClick(i, 1);
				new SWTWorkbenchBot().text(1).setText(dataSourceProperties.getProperty(table.getItem(i).getText(0)));
			}
		}
	}
	
	public void fillSecondPage(){
		//TODO translator
		new LabeledText("Name:").setText(modelName);
	}
	
	public void fillThirdPage(){
		//TODO
	}
	
	public void fillFourthPage(){
		if (tablesToImport != null){
			new PushButton("Unselect All").click();
			checkTablesToImport(tablesToImport);
		}
	}
	
	/**
	 * Create new data source of specified type
	 * @param dataSourcename
	 * @param modelName
	 * @param type of data source (e.g. SQLSERVER)
	 * @param properties path to properties file of data source
	 * @param projectName 
	 * @param tables to be imported
	 */
	/*public void createNewDataSource(String dataSourcename, String modelName, String type, String properties, String projectName, List<String> tables){
		//-----first page
		
		//Create datasource
		new PushButton("New...").click();
		new LabeledText("Name:").setText(dataSourcename);
		Properties props = loadProperties(properties);
		//set driver
		Table table = new DefaultTable(0);
		int originalRowCount = table.rowCount();
		
		//default drivers
		for (int i = 0; i < originalRowCount; i++){
			if (table.getItem(i).getText(0).equals(props.getProperty("db.driver"))){
				table.select(i);
				break;
			}
		}
		
		//TODO add driver --> add before
		
		new WaitWhile(new IsInProgress(), TimePeriod.NORMAL);
		
		
		if (type.equals(SQLSERVER_TYPE)){
			addDataSourceSQLServer(props);
		}//TODO extendedly
		
		new PushButton("OK").click();		
		new PushButton("&Next >").click();
		
		//-----second page
		
		
		//Select (the translator) and target model for the import
		new LabeledText("Name:").setText(modelName);
		new PushButton("&Next >").click();
		new WaitWhile(new IsInProgress(), TimePeriod.LONG);
		
		//Get DDL for the Import
		new PushButton("&Next >").click();
		
		//Review Model Updates
		new PushButton("Unselect All").click();
		//choose tables to import
		checkTablesToImport(projectName, modelName, tables);
		new PushButton("Finish").click();
	}*/
	
	/**
	 * Add data source of type: SQL Server
	 * @param props
	 */
	/*public void addDataSourceSQLServer(Properties props) {		
		//set connection info
		Table table = new DefaultTable(1);
		
		for (int i = 0; i < table.rowCount(); i++){
			//connection url
			if (table.getItem(i).getText(0).equals("* connection-url")){
				table.select(i);
				SWTBotTable t = new SWTWorkbenchBot().table(1);
				t.doubleClick(i, 1);
				String url = "jdbc:sqlserver://"+props.getProperty("db.hostname")
						+":1433;databasename="+props.getProperty("db.name");
				new SWTWorkbenchBot().text(1).setText(url);			
			}
			//username
			if (table.getItem(i).getText(0).equals("user-name")){
				table.select(i);
				SWTBotTable t = new SWTWorkbenchBot().table(1);
				t.doubleClick(i, 1);
				new SWTWorkbenchBot().text(1).setText(props.getProperty("db.username"));
			}
			//password
			if (table.getItem(i).getText(0).equals("password")){
				table.select(i);
				SWTBotTable t = new SWTWorkbenchBot().table(1);
				t.doubleClick(i, 1);
				new SWTWorkbenchBot().text(1).setText(props.getProperty("db.password"));
				
				//click somewhere else
				table.select(i+1);
			}
		}
	}*/
	
	/**
	 * 
	 * @param properties path to file with properties of data source
	 * @return properties of data source
	 */
	/*public Properties loadProperties(String properties){
		// load properties
		Properties props = new Properties();
		try {
			props.load(new FileReader(properties));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return props;
	}*/
	
	/**
	 * Review Updated Model - select the tables to be imported
	 * @param projectName
	 * @param modelName
	 * @param tables to be imported
	 */
	/*public void checkTablesToImport(String projectName, String modelName, String[] tables){
		String wholeModelName = "file:/" + projectName + "/" + modelName + ".xmi";
		for (String table : tables){
			new SWTWorkbenchBot().tree().expandNode(wholeModelName, table).check();
		}
	}*/
	
	private void checkTablesToImport(String[] pathsToTables){
		for (String path : pathsToTables){
			String[] parts = path.split("/");
			new SWTWorkbenchBot().tree().expandNode(parts).check();//CHECK
		}
	}

	public Properties getDataSourceProperties() {
		return dataSourceProperties;
	}

	public void setDataSourceProperties(Properties dataSourceProperties) {
		this.dataSourceProperties = dataSourceProperties;
	}

	public Boolean getCreateNewDataSource() {
		return createNewDataSource;
	}

	public void setCreateNewDataSource(Boolean createNewDataSource) {
		this.createNewDataSource = createNewDataSource;
	}

	public Boolean getDeleteDataSource() {
		return deleteDataSource;
	}

	public void setDeleteDataSource(Boolean deleteDataSource) {
		this.deleteDataSource = deleteDataSource;
	}

	public Boolean getEditDataSource() {
		return editDataSource;
	}

	public void setEditDataSource(Boolean editDataSource) {
		this.editDataSource = editDataSource;
	}

	public Boolean getCopyDataSource() {
		return copyDataSource;
	}

	public void setCopyDataSource(Boolean copyDataSource) {
		this.copyDataSource = copyDataSource;
	}

	public String getCopiedDataSourceName() {
		return copiedDataSourceName;
	}

	public void setCopiedDataSourceName(String copiedDataSourceName) {
		this.copiedDataSourceName = copiedDataSourceName;
	}

	public String getNewDataSourceName() {
		return newDataSourceName;
	}

	public void setNewDataSourceName(String newDataSourceName) {
		this.newDataSourceName = newDataSourceName;
	}

	public String getDriverName() {
		return driverName;
	}

	public void setDriverName(String driverName) {
		this.driverName = driverName;
	}

	public String getModelName() {
		return modelName;
	}

	public void setModelName(String modelName) {
		this.modelName = modelName;
	}

	public String getDataSourceName() {
		return dataSourceName;
	}

	public void setDataSourceName(String dataSourceName) {
		this.dataSourceName = dataSourceName;
	}

	public String[] getTablesToImport() {
		return tablesToImport;
	}

	public void setTablesToImport(String[] tablesToImport) {
		this.tablesToImport = tablesToImport;
	}

}