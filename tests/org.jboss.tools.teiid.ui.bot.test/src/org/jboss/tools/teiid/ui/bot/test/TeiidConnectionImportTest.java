package org.jboss.tools.teiid.ui.bot.test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.jboss.reddeer.common.wait.TimePeriod;
import org.jboss.reddeer.junit.requirement.inject.InjectRequirement;
import org.jboss.reddeer.junit.runner.RedDeerSuite;
import org.jboss.reddeer.requirements.server.ServerReqState;
import org.jboss.reddeer.swt.impl.shell.DefaultShell;
import org.jboss.tools.teiid.reddeer.connection.ConnectionProfileConstants;
import org.jboss.tools.teiid.reddeer.connection.SimpleHttpClient;
import org.jboss.tools.teiid.reddeer.dialog.CreateDataSourceDialog;
import org.jboss.tools.teiid.reddeer.preference.TeiidDesignerPreferencePage;
import org.jboss.tools.teiid.reddeer.requirement.TeiidServerRequirement;
import org.jboss.tools.teiid.reddeer.requirement.TeiidServerRequirement.TeiidServer;
import org.jboss.tools.teiid.reddeer.view.ModelExplorer;
import org.jboss.tools.teiid.reddeer.view.ServersViewExt;
import org.jboss.tools.teiid.reddeer.wizard.imports.TeiidConnectionImportWizard;
import org.jboss.tools.teiid.reddeer.wizard.newWizard.VdbWizard;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * Tests functionality of Teiid connection importer
 * 
 * @author Lucie Fabrikova, lfabriko@redhat.com
 *
 */
@RunWith(RedDeerSuite.class)
@TeiidServer(state = ServerReqState.RUNNING, connectionProfiles = {
		ConnectionProfileConstants.DB2_101_BQT,
		ConnectionProfileConstants.DB2_97_BQT2,
		ConnectionProfileConstants.ORACLE_11G_BQT2,
		ConnectionProfileConstants.ORACLE_12C_BQT,
		ConnectionProfileConstants.SQL_SERVER_2008_BQT2,
		ConnectionProfileConstants.SQL_SERVER_2012_BQT2,
		ConnectionProfileConstants.HSQLDB,
		ConnectionProfileConstants.MYSQL_51_BQT2,
		ConnectionProfileConstants.MYSQL_55_BQT2,
		ConnectionProfileConstants.POSTGRESQL_84_BQT2,
		ConnectionProfileConstants.POSTGRESQL_92_DVQE,
		ConnectionProfileConstants.SYBASE_15_BQT2,
		ConnectionProfileConstants.INGRES_10_BQT2,
		ConnectionProfileConstants.SALESFORCE,
		ConnectionProfileConstants.SQL_SERVER_2008_BOOKS,
		ConnectionProfileConstants.SAP_HANA })
public class TeiidConnectionImportTest {

	@InjectRequirement
	private static TeiidServerRequirement teiidServer;

	private static final String PROJECT_NAME = "TeiidConnImporter";
	private static final String SOURCE_VDB_NAME = "teiid";
	private static final String modeshapeCPName = "ModeShapeDS";
	private static final String sqlserverExistingModelName = "sqlserver";

	/**
	 * Create new Teiid Model Project
	 */
	@BeforeClass
	public static void createProject() {
		new TeiidDesignerPreferencePage().setTeiidConnectionImporterTimeout(240);
		new ModelExplorer().importProject(PROJECT_NAME);
		new ModelExplorer().selectItem(PROJECT_NAME);

	}
	
	@Test
	public void db2101Test() {
		Map<String,String> teiidImporterProperties = new HashMap<String, String>();
		teiidImporterProperties.put(TeiidConnectionImportWizard.IMPORT_PROPERTY_TABLE_NAME_PATTERN, "SMALL%");
		teiidImporterProperties.put(TeiidConnectionImportWizard.IMPORT_PROPERTY_SCHEMA_PATTERN, "%BQT%");
		importModel(ConnectionProfileConstants.DB2_101_BQT, "db2101Model", teiidImporterProperties);
		checkImportedModel("db2101Model", "SMALLA", "SMALLB");

	}

	@Test
	public void db297Test() {
		Map<String,String> teiidImporterProperties = new HashMap<String, String>();
		teiidImporterProperties.put(TeiidConnectionImportWizard.IMPORT_PROPERTY_TABLE_NAME_PATTERN, "SMALL%");
		importModel(ConnectionProfileConstants.DB2_97_BQT2, "db297Model", teiidImporterProperties);
		checkImportedModel("db297Model", "SMALLA", "SMALLB");

	}

	@Test
	public void ingres10Test() {
		Map<String,String> teiidImporterProperties = new HashMap<String, String>();
		teiidImporterProperties.put(TeiidConnectionImportWizard.IMPORT_PROPERTY_TABLE_NAME_PATTERN, "small%");
		importModel(ConnectionProfileConstants.INGRES_10_BQT2, "ingres10Model", teiidImporterProperties);
		checkImportedModel("ingres10Model", "smalla", "smallb");

	}

	@Test
	public void oracle11gTest() {
		Map<String,String> teiidImporterProperties = new HashMap<String, String>();
		teiidImporterProperties.put(TeiidConnectionImportWizard.IMPORT_PROPERTY_TABLE_NAME_PATTERN, "SMALL%");
		teiidImporterProperties.put(TeiidConnectionImportWizard.IMPORT_PROPERTY_SCHEMA_PATTERN, "BQT2");
		importModel(ConnectionProfileConstants.ORACLE_11G_BQT2, "oracle11gModel", teiidImporterProperties);
		checkImportedModel("oracle11gModel", "SMALLA", "SMALLB");

	}

	@Test
	public void oracle12cTest() {
		Map<String,String> teiidImporterProperties = new HashMap<String, String>();
		teiidImporterProperties.put(TeiidConnectionImportWizard.IMPORT_PROPERTY_TABLE_NAME_PATTERN, "SMALL%");
		importModel(ConnectionProfileConstants.ORACLE_12C_BQT, "oracle12cModel", teiidImporterProperties);
		checkImportedModel("oracle12cModel", "SMALLA", "SMALLB");

	}

	@Test
	public void sqlServer2008Test() {
		Map<String,String> teiidImporterProperties = new HashMap<String, String>();
		teiidImporterProperties.put(TeiidConnectionImportWizard.IMPORT_PROPERTY_TABLE_NAME_PATTERN, "Small%");
		importModel(ConnectionProfileConstants.SQL_SERVER_2008_BQT2, "sqlServer2008Model", teiidImporterProperties);
		checkImportedModel("sqlServer2008Model", "SmallA", "SmallB");

	}

	@Test
	public void sqlServer2012Test() {
		Map<String,String> teiidImporterProperties = new HashMap<String, String>();
		teiidImporterProperties.put(TeiidConnectionImportWizard.IMPORT_PROPERTY_TABLE_NAME_PATTERN, "Small%");
		importModel(ConnectionProfileConstants.SQL_SERVER_2012_BQT2, "sqlServer2012Model", teiidImporterProperties);
		checkImportedModel("sqlServer2012Model", "SmallA", "SmallB");

	}

	@Test
	public void sybaseTest() {
		Map<String,String> teiidImporterProperties = new HashMap<String, String>();
		teiidImporterProperties.put(TeiidConnectionImportWizard.IMPORT_PROPERTY_TABLE_NAME_PATTERN, "Small%");
		importModel(ConnectionProfileConstants.SYBASE_15_BQT2, "sybaseModel", teiidImporterProperties);
		checkImportedModel("sybaseModel", "SmallA", "SmallB");

	}

	@Test
	public void mysql51Test() {
		Map<String,String> teiidImporterProperties = new HashMap<String, String>();
		teiidImporterProperties.put(TeiidConnectionImportWizard.IMPORT_PROPERTY_TABLE_NAME_PATTERN, "small%");
		importModel(ConnectionProfileConstants.MYSQL_51_BQT2, "mysql51Model", teiidImporterProperties);

		checkImportedModel("mysql51Model", "smalla", "smallb");

	}

	@Test
	public void mysql55Test() {
		Map<String,String> teiidImporterProperties = new HashMap<String, String>();
		teiidImporterProperties.put(TeiidConnectionImportWizard.IMPORT_PROPERTY_TABLE_NAME_PATTERN, "small%");
		importModel(ConnectionProfileConstants.MYSQL_55_BQT2, "mysql55Model", teiidImporterProperties);
		checkImportedModel("mysql55Model", "smalla", "smallb");

	}

	@Test
	public void postgresql84Test() {
		Map<String,String> teiidImporterProperties = new HashMap<String, String>();
		teiidImporterProperties.put(TeiidConnectionImportWizard.IMPORT_PROPERTY_TABLE_NAME_PATTERN, "small%");
		teiidImporterProperties.put(TeiidConnectionImportWizard.IMPORT_PROPERTY_SCHEMA_PATTERN, "%public%");
		importModel(ConnectionProfileConstants.POSTGRESQL_84_BQT2, "postgresql84Model", teiidImporterProperties,TimePeriod.VERY_LONG);
		checkImportedModel("postgresql84Model", "smalla", "smallb");

	}

	@Test
	public void postgresql92Test() {
		Map<String,String> teiidImporterProperties = new HashMap<String, String>();
		teiidImporterProperties.put(TeiidConnectionImportWizard.IMPORT_PROPERTY_TABLE_NAME_PATTERN,  "small%");
		teiidImporterProperties.put(TeiidConnectionImportWizard.IMPORT_PROPERTY_SCHEMA_PATTERN, "public");
		teiidImporterProperties.put(TeiidConnectionImportWizard.IMPORT_PROPERTY_CATALOG, "dvqe");
		importModel(ConnectionProfileConstants.POSTGRESQL_92_DVQE, "postgresql92Model", teiidImporterProperties,TimePeriod.VERY_LONG);
		checkImportedModel("postgresql92Model", "smalla", "smallb");

	}

	@Test
	public void hsqlTest() {
		String modelName = "HsqldbImported";
		new ServersViewExt().createDatasource(teiidServer.getName(), ConnectionProfileConstants.HSQLDB);
	
		TeiidConnectionImportWizard.openWizard()
				.selectDataSource(ConnectionProfileConstants.HSQLDB)
				.nextPage()
				.setTranslator("hsql")
				.setImportPropertie(TeiidConnectionImportWizard.IMPORT_PROPERTY_SCHEMA_PATTERN, "PUBLIC%")
				.nextPage()
				.setModelName(modelName)
				.setProject(PROJECT_NAME)
				.nextPageWithWait()
				.nextPageWithWait()
				.finish();
		
		checkImportedModel(modelName, "CUSTOMER");

	}
	
	@Test
	public void fileTest() {		
		String modelName = "FileImported";
		new ServersViewExt().deleteDatasource(teiidServer.getName(), "fileDS");
		
		TeiidConnectionImportWizard.openWizard()
				.createNewDataSource()
						.setName("fileDS")
						.setDriver("file")
						.setImportPropertie(CreateDataSourceDialog.DATASOURCE_PROPERTY_PARENT_DIR, "resources/flat/")
						.finish();
		TeiidConnectionImportWizard.getInstance()
				.nextPage()
				.nextPage()
				.setModelName(modelName)
				.setProject(PROJECT_NAME)
				.nextPageWithWait()
				.nextPageWithWait()
				.finish();
		
		checkImportedModel(modelName, "getTextFiles");
	}
	
	@Test
	public void salesforceTest() {	
		String modelName = "sfImp";
		new ServersViewExt().deleteDatasource(teiidServer.getName(), "sfDS");
		
		Properties sfProps = teiidServer.getServerConfig().getConnectionProfile("salesforce").asProperties();

		TeiidConnectionImportWizard.openWizard()
				.createNewDataSource()
						.setName("sfDS")
						.setDriver("salesforce")
						.setImportPropertie(CreateDataSourceDialog.DATASOURCE_PROPERTY_PASSWORD, sfProps.getProperty("db.password"))
						.setImportPropertie(CreateDataSourceDialog.DATASOURCE_PROPERTY_USER_NAME, sfProps.getProperty("db.username"))
						.finish();
		TeiidConnectionImportWizard.getInstance()
				.nextPage()
				.nextPage()
				.setModelName(modelName)
				.setProject(PROJECT_NAME)
				.nextPageWithWait()
				.nextPageWithWait()
				.finish();
		
		checkImportedModel(modelName, "Account", "Vote", "Profile");
	}

	@Test
	public void odataTest() {
		String modelName = "OdataModel";
		new ServersViewExt().deleteDatasource(teiidServer.getName(), "odataDS");
		
		TeiidConnectionImportWizard.openWizard()
				.createNewDataSource()
						.setName("odataDS")
						.setDriver("webservice")
						.setImportPropertie(CreateDataSourceDialog.DATASOURCE_PROPERTY_URL, "http://services.odata.org/Northwind/Northwind.svc")
						.finish();
		TeiidConnectionImportWizard.getInstance()
				.nextPage()
				.setTranslator("odata")
				.nextPage()
				.setModelName(modelName)
				.setProject(PROJECT_NAME)
				.nextPageWithWait()
				.nextPageWithWait()
				.finish();
		
		assertTrue(new ModelExplorer().getProject(PROJECT_NAME).containsItem(modelName + ".xmi", "Customers"));
		assertTrue(new ModelExplorer().getProject(PROJECT_NAME).containsItem(modelName + ".xmi", "Customers", "CustomerID : string(5)"));
		assertTrue(new ModelExplorer().getProject(PROJECT_NAME).containsItem(modelName + ".xmi", "Employees", "EmployeeID : int"));
	}

	@Test
	public void excelTest() {
		String modelName = "ExcelModel";		
		new ServersViewExt().deleteDatasource(teiidServer.getName(), "excelDS");

		Properties excelDsProperties = teiidServer.getServerConfig()
				.getConnectionProfile(ConnectionProfileConstants.EXCEL_SMALLA).asProperties();
		
		TeiidConnectionImportWizard.openWizard()
				.createNewDataSource()
						.setName("excelDS")
						.setDriver("file")
						.setImportPropertie(CreateDataSourceDialog.DATASOURCE_PROPERTY_PARENT_DIR, excelDsProperties.getProperty("path"))
						.finish();
		TeiidConnectionImportWizard.getInstance()
				.nextPage()
				.setTranslator("excel")
				.setImportPropertie(TeiidConnectionImportWizard.IMPORT_PROPERTY_EXCEL_FILENAME, excelDsProperties.getProperty("filename"))
				.setImportPropertie(TeiidConnectionImportWizard.IMPORT_PROPERTY_HEADER_ROW_NUMBER, "1")
				.nextPage()
				.setModelName(modelName)
				.setProject(PROJECT_NAME)
				.nextPageWithWait()
				.nextPageWithWait()
				.finish();

		assertTrue(new ModelExplorer().getProject(PROJECT_NAME).containsItem(modelName + ".xmi", "Sheet1"));
		assertTrue(new ModelExplorer().getProject(PROJECT_NAME).containsItem(modelName + ".xmi", "Sheet1", "ROW_ID : int"));
		assertTrue(new ModelExplorer().getProject(PROJECT_NAME).containsItem(modelName + ".xmi", "Sheet1", "StringNum : string"));
	}
	
	@Test
	public void modeshapeTest() throws IOException {
    	// initialize modeshape
		String resp = new SimpleHttpClient("http://localhost:8080/modeshape-rest/dv/")
				.setBasicAuth(teiidServer.getServerConfig().getServerBase().getProperty("modeshapeUser"),
						teiidServer.getServerConfig().getServerBase().getProperty("modeshapePassword"))
				.get();

		assertFalse("initializing modeshape failed", resp.isEmpty());
		
		String modelName = "ModeshapeModel";
		
		TeiidConnectionImportWizard.openWizard()
				.selectDataSource(modeshapeCPName)
				.nextPage()
				.setImportPropertie(TeiidConnectionImportWizard.IMPORT_PROPERTY_TABLE_NAME_PATTERN, "mix:title")
				.setImportPropertie(TeiidConnectionImportWizard.IMPORT_PROPERTY_USE_FULL_SCHEMA_NAME, "false")
				.nextPage()
				.setModelName(modelName)
				.setProject(PROJECT_NAME)
				.nextPageWithWait()
				.nextPageWithWait()
				.finish();
		
		checkImportedModel(modelName, "mix:title");
	}
	
	@Test
	public void sapHanaTest() {
		String modelName = "sapHanaModel";
		
		new ServersViewExt().createDatasource(teiidServer.getName(), ConnectionProfileConstants.SAP_HANA);
		
		TeiidConnectionImportWizard.openWizard()
				.selectDataSource(ConnectionProfileConstants.SAP_HANA)
				.nextPage()
				.setTranslator("hana")
				.setImportPropertie(TeiidConnectionImportWizard.IMPORT_PROPERTY_SCHEMA_PATTERN, "BQT1")
				.setImportPropertie(TeiidConnectionImportWizard.IMPORT_PROPERTY_TABLE_NAME_PATTERN, "SMALL%")
				.nextPage()
				.setModelName(modelName)
				.setProject(PROJECT_NAME)
				.nextPageWithWait()
				.nextPageWithWait()
				.finish();
		
		checkImportedModel(modelName, "SMALLA", "SMALLB");
	}
	
	@Test
	public void h2Test() {
		String modelName = "h2Imp";

		TeiidConnectionImportWizard.openWizard()
				.selectDataSource("ExampleDS")
				.nextPage()
				.setImportPropertie(TeiidConnectionImportWizard.IMPORT_PROPERTY_TABLE_TYPES, "SYSTEM TABLE")
				.nextPage()
				.setModelName(modelName)
				.setProject(PROJECT_NAME)
				.nextPageWithWait()
				.nextPageWithWait()
				.setTablesToImport("Objects to Create/CONSTANTS")
				.finish();
		
		checkImportedModel(modelName, "CONSTANTS");
	}
	
	@Test
	public void teiidTest() {
		new ModelExplorer().changeConnectionProfile(ConnectionProfileConstants.SQL_SERVER_2008_BOOKS,
				PROJECT_NAME, sqlserverExistingModelName);
	
		VdbWizard.openVdbWizard()
				.setLocation(PROJECT_NAME)
				.setName(SOURCE_VDB_NAME)
				.addModel(PROJECT_NAME, sqlserverExistingModelName+".xmi")
				.finish();
		
		new ModelExplorer().deployVdb(PROJECT_NAME, SOURCE_VDB_NAME);
	
		String modelName = SOURCE_VDB_NAME + "Imp";
		
		TeiidConnectionImportWizard.openWizard()
				.selectDataSource(SOURCE_VDB_NAME)
				.nextPage()
				.setImportPropertie(TeiidConnectionImportWizard.IMPORT_PROPERTY_SCHEMA_PATTERN, "%sqlserver%")
				.nextPage()
				.setModelName(modelName)
				.setProject(PROJECT_NAME)
				.nextPageWithWait()
				.nextPageWithWait()
				.finish();
		
		assertTrue(new ModelExplorer().getProject(PROJECT_NAME).containsItem(modelName + ".xmi", "AUTHORS"));
	}

	private void checkImportedModel(String modelName, String... tables) {
		for (String table : tables) {
			assertTrue(new ModelExplorer().getProject(PROJECT_NAME).containsItem(modelName + ".xmi", table));
		}
	}

	private void importModel(String cpName, String modelName, Map<String,String> teiidImporterProperties) {
		importModel(cpName,modelName,teiidImporterProperties, null);
	}
	
	private void importModel(String cpName, String modelName, Map<String,String> teiidImporterProperties, TimePeriod timePeriod) {
		new DefaultShell();
		new ServersViewExt().createDatasource(teiidServer.getName(), cpName);
		
		TeiidConnectionImportWizard wizard = TeiidConnectionImportWizard.openWizard();
		wizard.selectDataSource(cpName)
				.nextPage();
		
		for (Map.Entry<String,String> entry : teiidImporterProperties.entrySet())
		{
			wizard.setImportPropertie(entry.getKey(), entry.getValue());
		}
		
		wizard.nextPage()
				.setProject(PROJECT_NAME)
				.setModelName(modelName);
		if(timePeriod!=null){
			wizard.nextPageWithWait(timePeriod);
		}else{
			wizard.nextPageWithWait();
		}
		wizard.nextPageWithWait()
				.finish();
	}


}