package org.jboss.tools.teiid.ui.bot.test;

import java.io.File;
import java.io.IOException;
import java.util.Properties;

import org.eclipse.swtbot.swt.finder.SWTBotTestCase;
import org.jboss.reddeer.junit.requirement.inject.InjectRequirement;
import org.jboss.reddeer.junit.runner.RedDeerSuite;
import org.jboss.reddeer.requirements.server.ServerReqState;
import org.jboss.reddeer.swt.impl.shell.DefaultShell;
import org.jboss.tools.teiid.reddeer.connection.ConnectionProfileConstants;
import org.jboss.tools.teiid.reddeer.connection.ResourceFileHelper;
import org.jboss.tools.teiid.reddeer.connection.SimpleHttpClient;
import org.jboss.tools.teiid.reddeer.preference.TeiidDesignerPreferencePage;
import org.jboss.tools.teiid.reddeer.requirement.TeiidServerRequirement;
import org.jboss.tools.teiid.reddeer.requirement.TeiidServerRequirement.TeiidServer;
import org.jboss.tools.teiid.reddeer.view.ModelExplorer;
import org.jboss.tools.teiid.reddeer.view.ServersViewExt;
import org.jboss.tools.teiid.reddeer.wizard.VdbWizard;
import org.jboss.tools.teiid.reddeer.wizard.imports.TeiidConnectionImportWizard;
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
public class TeiidConnectionImportTest extends SWTBotTestCase {

	@InjectRequirement
	private static TeiidServerRequirement teiidServer;

	private static final String PROJECT_NAME = "TeiidConnImporter";
	private static final String SOURCE_VDB_NAME = "teiid";
	private static final String projectLocation = "TeiidConnImporter";

	private static final String modeshapeCPName = "ModeShapeDS";

	private static final String sqlserverExistingModelName = "sqlserver";

	// file
	private static final String FILE_MODEL = "FileImported";

	// hsql
	private static final String hsqlCPName = ConnectionProfileConstants.HSQLDB;
	private static final String hsqlModel = "HsqldbImported";

	/**
	 * Create new Teiid Model Project
	 */
	@BeforeClass
	public static void createProject() {
		new TeiidDesignerPreferencePage().setTeiidConnectionImporterTimeout(240);

		new ModelExplorer().importProject(projectLocation);
		new ModelExplorer().getModelProject(PROJECT_NAME).open();

	}

	@Test
	public void fileTest() {
		new ServersViewExt().deleteDatasource(teiidServer.getName(), "fileDS");

		Properties dsProps = new Properties();
		dsProps = new ResourceFileHelper().getProperties("resources/teiidImporter/file_items_ds.properties");
		String absPath = new File(dsProps.getProperty("* Parent Directory")).getAbsolutePath();
		dsProps.setProperty("* Parent Directory", absPath);
		
		TeiidConnectionImportWizard importWizard = new TeiidConnectionImportWizard();
		importWizard.setModelName(FILE_MODEL);
		importWizard.setProjectName(PROJECT_NAME);
		importWizard.setDataSourceProperties(dsProps);
		importWizard.setCreateNewDataSource(true);
		importWizard.setNewDataSourceName("fileDS");
		importWizard.setDriverName("file");
		importWizard.execute();

		assertTrue(new ModelExplorer().getProject(PROJECT_NAME).containsItem(FILE_MODEL + ".xmi", "getTextFiles"));
	}

	@Test
	public void hsqlTest() {
		new ServersViewExt().createDatasource(teiidServer.getName(), hsqlCPName);

		Properties teiidImporterProperties = new Properties();
		teiidImporterProperties.setProperty("Schema Pattern", "PUBLIC%");

		TeiidConnectionImportWizard importWizard = new TeiidConnectionImportWizard();
		importWizard.setModelName(hsqlModel);
		importWizard.setProjectName(PROJECT_NAME);
		importWizard.setTeiidImporterProperties(teiidImporterProperties);
		importWizard.setNewDataSourceName("hsqlCPName");
		importWizard.setDriverName("hsqldb.jar");
		importWizard.setTranslator("hsql");
		importWizard.execute();

		assertTrue(new ModelExplorer().getProject(PROJECT_NAME).containsItem(hsqlModel + ".xmi", "CUSTOMER"));
	}

	@Test
	public void h2Test() {
		Properties teiidImporterProperties = new Properties();		
		teiidImporterProperties.setProperty(TeiidConnectionImportWizard.IMPORT_PROPERTY_TABLE_TYPES, "SYSTEM TABLE");

		TeiidConnectionImportWizard importWizard = new TeiidConnectionImportWizard();
		importWizard.setModelName("h2Imp");
		importWizard.setProjectName(PROJECT_NAME);
		importWizard.setTeiidImporterProperties(teiidImporterProperties);
		// use default DV ds
		importWizard.setNewDataSourceName("ExampleDS");
		importWizard.setTablesToImport(new String[] {"Objects to Create/CONSTANTS"});
		importWizard.execute();
		
		assertTrue(new ModelExplorer().getProject(PROJECT_NAME).containsItem("h2Imp.xmi", "CONSTANTS"));
	}

	@Test
	public void salesforceTest() {
		new ServersViewExt().deleteDatasource(teiidServer.getName(), "sfDS");

		Properties sfProps = teiidServer.getServerConfig().getConnectionProfile("salesforce").asProperties();

		Properties dsProps = new Properties();
		dsProps.setProperty("* Password", sfProps.getProperty("db.password"));
		dsProps.setProperty("* User Name", sfProps.getProperty("db.username"));

		String model = "sfImp";
		
		TeiidConnectionImportWizard importWizard = new TeiidConnectionImportWizard();
		importWizard.setModelName(model);
		importWizard.setProjectName(PROJECT_NAME);
		importWizard.setDataSourceProperties(dsProps);
		importWizard.setCreateNewDataSource(true);
		importWizard.setNewDataSourceName("sfDS");
		importWizard.setDriverName("salesforce");
		importWizard.execute();

		checkImportedModel(model, "Account", "Vote", "Profile");
	}

	@Test
	public void teiidTest() {
		new ModelExplorer().changeConnectionProfile(ConnectionProfileConstants.SQL_SERVER_2008_BOOKS,
				PROJECT_NAME, sqlserverExistingModelName);

		VdbWizard.openVdbWizard()
				.setLocation(PROJECT_NAME)
				.setName(SOURCE_VDB_NAME)
				.addModel(PROJECT_NAME, sqlserverExistingModelName)
				.finish();
		
		new ModelExplorer().deployVdb(PROJECT_NAME, SOURCE_VDB_NAME);

		Properties teiidImporterProperties = new Properties();
		teiidImporterProperties.setProperty(TeiidConnectionImportWizard.IMPORT_PROPERTY_SCHEMA_PATTERN, "%sqlserver%");

		String model = SOURCE_VDB_NAME + "Imp";
		
		TeiidConnectionImportWizard importWizard = new TeiidConnectionImportWizard();
		importWizard.setModelName(model);
		importWizard.setProjectName(PROJECT_NAME);
		importWizard.setTeiidImporterProperties(teiidImporterProperties);
		importWizard.setNewDataSourceName(SOURCE_VDB_NAME);
		importWizard.execute();
		
		assertTrue(new ModelExplorer().getProject(PROJECT_NAME).containsItem(model + ".xmi", "AUTHORS"));
	}

	@Test
	public void db2101Test() {
		Properties teiidImporterProperties = new Properties();
		teiidImporterProperties.setProperty(TeiidConnectionImportWizard.IMPORT_PROPERTY_TABLE_NAME_PATTERN, "SMALL%");
		teiidImporterProperties.setProperty(TeiidConnectionImportWizard.IMPORT_PROPERTY_SCHEMA_PATTERN, "%BQT%");
		importModel(ConnectionProfileConstants.DB2_101_BQT, "db2101Model", teiidImporterProperties);
		checkImportedModel("db2101Model", "SMALLA", "SMALLB");

	}

	@Test
	public void db297Test() {
		Properties teiidImporterProperties = new Properties();
		teiidImporterProperties.setProperty(TeiidConnectionImportWizard.IMPORT_PROPERTY_TABLE_NAME_PATTERN, "SMALL%");
		importModel(ConnectionProfileConstants.DB2_97_BQT2, "db297Model", teiidImporterProperties);
		checkImportedModel("db297Model", "SMALLA", "SMALLB");

	}

	@Test
	public void ingres10Test() {
		Properties teiidImporterProperties = new Properties();
		teiidImporterProperties.setProperty(TeiidConnectionImportWizard.IMPORT_PROPERTY_TABLE_NAME_PATTERN, "small%");
		importModel(ConnectionProfileConstants.INGRES_10_BQT2, "ingres10Model", teiidImporterProperties);
		checkImportedModel("ingres10Model", "smalla", "smallb");

	}

	@Test
	public void oracle11gTest() {
		Properties teiidImporterProperties = new Properties();
		teiidImporterProperties.setProperty(TeiidConnectionImportWizard.IMPORT_PROPERTY_TABLE_NAME_PATTERN, "SMALL%");
		teiidImporterProperties.setProperty(TeiidConnectionImportWizard.IMPORT_PROPERTY_SCHEMA_PATTERN, "BQT2");
		importModel(ConnectionProfileConstants.ORACLE_11G_BQT2, "oracle11gModel", teiidImporterProperties);
		checkImportedModel("oracle11gModel", "SMALLA", "SMALLB");

	}

	@Test
	public void oracle12cTest() {
		Properties teiidImporterProperties = new Properties();
		teiidImporterProperties.setProperty(TeiidConnectionImportWizard.IMPORT_PROPERTY_TABLE_NAME_PATTERN, "SMALL%");
		importModel(ConnectionProfileConstants.ORACLE_12C_BQT, "oracle12cModel", teiidImporterProperties);
		checkImportedModel("oracle12cModel", "SMALLA", "SMALLB");

	}

	@Test
	public void sqlServer2008Test() {
		Properties teiidImporterProperties = new Properties();
		teiidImporterProperties.setProperty(TeiidConnectionImportWizard.IMPORT_PROPERTY_TABLE_NAME_PATTERN, "Small%");
		importModel(ConnectionProfileConstants.SQL_SERVER_2008_BQT2, "sqlServer2008Model", teiidImporterProperties);
		checkImportedModel("sqlServer2008Model", "SmallA", "SmallB");

	}

	@Test
	public void sqlServer2012Test() {
		Properties teiidImporterProperties = new Properties();
		teiidImporterProperties.setProperty(TeiidConnectionImportWizard.IMPORT_PROPERTY_TABLE_NAME_PATTERN, "Small%");
		importModel(ConnectionProfileConstants.SQL_SERVER_2012_BQT2, "sqlServer2012Model", teiidImporterProperties);
		checkImportedModel("sqlServer2012Model", "SmallA", "SmallB");

	}

	@Test
	public void sybaseTest() {
		Properties teiidImporterProperties = new Properties();
		teiidImporterProperties.setProperty(TeiidConnectionImportWizard.IMPORT_PROPERTY_TABLE_NAME_PATTERN, "Small%");
		importModel(ConnectionProfileConstants.SYBASE_15_BQT2, "sybaseModel", teiidImporterProperties);
		checkImportedModel("sybaseModel", "SmallA", "SmallB");

	}

	@Test
	public void mysql51Test() {
		Properties teiidImporterProperties = new Properties();
		teiidImporterProperties.setProperty(TeiidConnectionImportWizard.IMPORT_PROPERTY_TABLE_NAME_PATTERN, "small%");
		importModel(ConnectionProfileConstants.MYSQL_51_BQT2, "mysql51Model", teiidImporterProperties);
		checkImportedModel("mysql51Model", "smalla", "smallb");

	}

	@Test
	public void mysql55Test() {
		Properties teiidImporterProperties = new Properties();
		teiidImporterProperties.setProperty(TeiidConnectionImportWizard.IMPORT_PROPERTY_TABLE_NAME_PATTERN, "small%");
		importModel(ConnectionProfileConstants.MYSQL_55_BQT2, "mysql55Model", teiidImporterProperties);
		checkImportedModel("mysql55Model", "smalla", "smallb");

	}

	@Test
	public void postgresql84Test() {
		Properties teiidImporterProperties = new Properties();
		teiidImporterProperties.setProperty(TeiidConnectionImportWizard.IMPORT_PROPERTY_TABLE_NAME_PATTERN, "small%");
		teiidImporterProperties.setProperty(TeiidConnectionImportWizard.IMPORT_PROPERTY_SCHEMA_PATTERN, "%public%");
		importModel(ConnectionProfileConstants.POSTGRESQL_84_BQT2, "postgresql84Model", teiidImporterProperties);
		checkImportedModel("postgresql84Model", "smalla", "smallb");

	}

	@Test
	public void postgresql92Test() {
		Properties teiidImporterProperties = new Properties();
		teiidImporterProperties.setProperty(TeiidConnectionImportWizard.IMPORT_PROPERTY_TABLE_NAME_PATTERN, "small%");
		teiidImporterProperties.setProperty(TeiidConnectionImportWizard.IMPORT_PROPERTY_SCHEMA_PATTERN, "public");
		teiidImporterProperties.setProperty(TeiidConnectionImportWizard.IMPORT_PROPERTY_CATALOG, "dvqe");
		importModel(ConnectionProfileConstants.POSTGRESQL_92_DVQE, "postgresql92Model", teiidImporterProperties);
		checkImportedModel("postgresql92Model", "smalla", "smallb");

	}

	@Test
	public void modeshapeTest() throws IOException {
		Properties teiidImporterProperties = new Properties();
		teiidImporterProperties.setProperty(TeiidConnectionImportWizard.IMPORT_PROPERTY_TABLE_NAME_PATTERN,
				"mix:title");
		teiidImporterProperties.setProperty(TeiidConnectionImportWizard.IMPORT_PROPERTY_USE_FULL_SCHEMA_NAME, "false");

		// initialize modeshape
		String resp = new SimpleHttpClient("http://localhost:8080/modeshape-rest/dv/")
				.setBasicAuth(teiidServer.getServerConfig().getServerBase().getProperty("modeshapeUser"),
						teiidServer.getServerConfig().getServerBase().getProperty("modeshapePassword"))
				.get();

		assertFalse("initializing modeshape failed", resp.isEmpty());
		
		TeiidConnectionImportWizard importWizard = new TeiidConnectionImportWizard();
		importWizard.setModelName("ModeshapeModel");
		importWizard.setProjectName(PROJECT_NAME);
		importWizard.setTeiidImporterProperties(teiidImporterProperties);
		importWizard.setNewDataSourceName(modeshapeCPName);
		importWizard.execute();
		
		checkImportedModel("ModeshapeModel", "mix:title");
	}

	@Test
	public void excelTest() {
		String modelName = "ExcelModel";

		Properties dsProps = new Properties();
		Properties excelDsProperties = teiidServer.getServerConfig()
				.getConnectionProfile(ConnectionProfileConstants.EXCEL_SMALLA).asProperties();
		dsProps.put(TeiidConnectionImportWizard.DATASOURCE_PROPERTY_PARENT_DIR, excelDsProperties.getProperty("path"));

		Properties teiidImporterProps = new Properties();
		teiidImporterProps.setProperty(TeiidConnectionImportWizard.IMPORT_PROPERTY_EXCEL_FILENAME,
				excelDsProperties.getProperty("filename"));
		teiidImporterProps.setProperty(TeiidConnectionImportWizard.IMPORT_PROPERTY_HEADER_ROW_NUMBER, "1");
		
		TeiidConnectionImportWizard importWizard = new TeiidConnectionImportWizard();
		importWizard.setModelName(modelName);
		importWizard.setProjectName(PROJECT_NAME);
		importWizard.setTeiidImporterProperties(teiidImporterProps);
		importWizard.setDataSourceProperties(dsProps);
		importWizard.setCreateNewDataSource(true);
		importWizard.setNewDataSourceName("excelDS");
		importWizard.setDriverName("file");
		importWizard.setTranslator("excel");
		importWizard.execute();
		

		assertTrue(new ModelExplorer().getProject(PROJECT_NAME).containsItem(modelName + ".xmi", "Sheet1"));
		assertTrue(new ModelExplorer().getProject(PROJECT_NAME).containsItem(modelName + ".xmi", "Sheet1", "ROW_ID : int"));
		assertTrue(new ModelExplorer().getProject(PROJECT_NAME).containsItem(modelName + ".xmi", "Sheet1", "StringNum : string"));
	}

	@Test
	public void odataTest() {
		String modelName = "OdataModel";

		Properties dsProps = new Properties();
		dsProps.put(TeiidConnectionImportWizard.DATASOURCE_PROPERTY_URL,
				"http://services.odata.org/Northwind/Northwind.svc");
		
		TeiidConnectionImportWizard importWizard = new TeiidConnectionImportWizard();
		importWizard.setModelName(modelName);
		importWizard.setProjectName(PROJECT_NAME);
		importWizard.setDataSourceProperties(dsProps);
		importWizard.setCreateNewDataSource(true);
		importWizard.setNewDataSourceName("odataDS");
		importWizard.setDriverName("webservice");
		importWizard.setTranslator("odata");
		importWizard.execute();

		assertTrue(new ModelExplorer().getProject(PROJECT_NAME).containsItem(modelName + ".xmi", "Customers"));
		assertTrue(new ModelExplorer().getProject(PROJECT_NAME).containsItem(modelName + ".xmi", "Customers", "CustomerID : string(5)"));
		assertTrue(new ModelExplorer().getProject(PROJECT_NAME).containsItem(modelName + ".xmi", "Employees", "EmployeeID : int"));
	}
	
	@Test
	public void sapHanaTest() {
		String modelName = "sapHanaModel";
		
		Properties teiidImportProps = new Properties();
		teiidImportProps.setProperty(TeiidConnectionImportWizard.IMPORT_PROPERTY_SCHEMA_PATTERN, "BQT1");
		teiidImportProps.setProperty(TeiidConnectionImportWizard.IMPORT_PROPERTY_TABLE_NAME_PATTERN, "SMALL%");
		
		// TODO temp till hana translator is not set automatically (updated importModel method)
		// THEN -> importModel(ConnectionProfilesConstants.SAP_HANA, modelName, teiidImportProps);
		new DefaultShell();
		new ServersViewExt().createDatasource(teiidServer.getName(), ConnectionProfileConstants.SAP_HANA);
		Properties importProps = new Properties();
		importProps.setProperty(TeiidConnectionImportWizard.DATA_SOURCE_NAME, ConnectionProfileConstants.SAP_HANA);
		importProps.setProperty(TeiidConnectionImportWizard.TRANSLATOR, "hana");
		TeiidConnectionImportWizard importWizard = new TeiidConnectionImportWizard();
		importWizard.setModelName(modelName);
		importWizard.setProjectName(PROJECT_NAME);
		importWizard.setTeiidImporterProperties(teiidImportProps);
		importWizard.setNewDataSourceName(ConnectionProfileConstants.SAP_HANA);
		importWizard.setTranslator("hana");
		importWizard.execute();
		// END
		
		checkImportedModel(modelName, "SMALLA", "SMALLB");
	}

	private void checkImportedModel(String modelName, String... tables) {
		for (String table : tables) {
			assertTrue(new ModelExplorer().getProject(PROJECT_NAME).containsItem(modelName + ".xmi", table));
		}
	}

	private void importModel(String cpName, String modelName, Properties teiidImporterProperties) {
		new DefaultShell();
		new ServersViewExt().createDatasource(teiidServer.getName(), cpName);
		
		TeiidConnectionImportWizard importWizard = new TeiidConnectionImportWizard();
		importWizard.setModelName(modelName);
		importWizard.setProjectName(PROJECT_NAME);
		importWizard.setTeiidImporterProperties(teiidImporterProperties);
		importWizard.setNewDataSourceName(cpName);
		importWizard.execute();
	}

}