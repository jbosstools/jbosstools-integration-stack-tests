package org.jboss.tools.teiid.ui.bot.test;

import java.util.Properties;

import org.eclipse.swtbot.swt.finder.SWTBotTestCase;
import org.jboss.reddeer.junit.requirement.inject.InjectRequirement;
import org.jboss.reddeer.junit.runner.RedDeerSuite;
import org.jboss.reddeer.requirements.server.ServerReqState;
import org.jboss.reddeer.swt.impl.shell.DefaultShell;
import org.jboss.tools.teiid.reddeer.manager.ConnectionProfilesConstants;
import org.jboss.tools.teiid.reddeer.manager.ImportManager;
import org.jboss.tools.teiid.reddeer.manager.ImportMetadataManager;
import org.jboss.tools.teiid.reddeer.manager.ModelExplorerManager;
import org.jboss.tools.teiid.reddeer.manager.VDBManager;
import org.jboss.tools.teiid.reddeer.preference.TeiidDesignerPreferencePage;
import org.jboss.tools.teiid.reddeer.requirement.TeiidServerRequirement;
import org.jboss.tools.teiid.reddeer.requirement.TeiidServerRequirement.TeiidServer;
import org.jboss.tools.teiid.reddeer.util.SimpleHttpClient;
import org.jboss.tools.teiid.reddeer.view.ModelExplorer;
import org.jboss.tools.teiid.reddeer.view.ServersViewExt;
import org.jboss.tools.teiid.reddeer.wizard.ImportGeneralItemWizard;
import org.jboss.tools.teiid.reddeer.wizard.TeiidConnectionImportWizard;
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
	ConnectionProfilesConstants.DB2_101_BQT,
	ConnectionProfilesConstants.DB2_97_BQT2,
	ConnectionProfilesConstants.ORACLE_11G_BQT2,
	ConnectionProfilesConstants.ORACLE_12C_BQT,
	ConnectionProfilesConstants.SQL_SERVER_2008_BQT2,
	ConnectionProfilesConstants.SQL_SERVER_2012_BQT2,
	ConnectionProfilesConstants.HSQLDB,
	ConnectionProfilesConstants.MYSQL_51_BQT2,
	ConnectionProfilesConstants.MYSQL_55_BQT2,
	ConnectionProfilesConstants.POSTGRESQL_84_BQT2,
	ConnectionProfilesConstants.POSTGRESQL_92_DVQE,
	ConnectionProfilesConstants.SYBASE_15_BQT2,
	ConnectionProfilesConstants.INGRES_10_BQT2,
	ConnectionProfilesConstants.SALESFORCE,
	ConnectionProfilesConstants.SQL_SERVER_2008_BOOKS,
	ConnectionProfilesConstants.SAP_HANA })
public class TeiidConnectionImportTest extends SWTBotTestCase {

	@InjectRequirement
	private static TeiidServerRequirement teiidServer;

	private static final String PROJECT_NAME = "TeiidConnImporter";
	private static final String SOURCE_VDB_NAME = "teiid";
	private static final String projectLocation = "resources/projects/TeiidConnImporter";

	private static final String modeshapeCPName = "ModeShapeDS";

	private static final String sqlserverExistingModelName = "sqlserver";

	// file
	private static final String FILE_MODEL = "FileImported";

	// hsql
	private static final String hsqlCPName = ConnectionProfilesConstants.HSQLDB;
	private static final String hsqlModel = "HsqldbImported";

	private static TeiidBot teiidBot = new TeiidBot();

	/**
	 * Create new Teiid Model Project
	 */
	@BeforeClass
	public static void createProject() {
		new TeiidDesignerPreferencePage().setTeiidConnectionImporterTimeout(240);

		new ImportManager().importProject(teiidBot.toAbsolutePath(projectLocation));
		new ModelExplorer().getModelProject(PROJECT_NAME).open();

	}

	@Test
	public void fileTest() {
		Properties iProps = new Properties();
		iProps.setProperty(TeiidConnectionImportWizard.CREATE_NEW_DATA_SOURCE, "true");
		iProps.setProperty(TeiidConnectionImportWizard.NEW_DATA_SOURCE_NAME, "fileDS");
		iProps.setProperty(TeiidConnectionImportWizard.DRIVER_NAME, "file");

		new ServersViewExt().deleteDatasource(teiidServer.getName(), "fileDS");

		Properties dsProps = new Properties();
		dsProps = teiidBot.getProperties("resources/teiidImporter/file_items_ds.properties");
		String absPath = teiidBot.toAbsolutePath(dsProps.getProperty("* Parent Directory"));
		dsProps.setProperty("* Parent Directory", absPath);

		new ImportMetadataManager().importFromTeiidConnection(PROJECT_NAME, FILE_MODEL, iProps, dsProps);

		teiidBot.assertResource(PROJECT_NAME, FILE_MODEL + ".xmi", "getTextFiles");
	}

	@Test
	public void hsqlTest() {
		new ServersViewExt().createDatasource(teiidServer.getName(), hsqlCPName);

		Properties iProps = new Properties();
		iProps.setProperty(TeiidConnectionImportWizard.DATA_SOURCE_NAME, hsqlCPName);
		iProps.setProperty(TeiidConnectionImportWizard.DRIVER_NAME, "hsqldb.jar");
		iProps.setProperty("translator", "hsql");

		Properties teiidImporterProperties = new Properties();
		teiidImporterProperties.setProperty("Schema Pattern", "PUBLIC%");

		new ImportMetadataManager().importFromTeiidConnection(PROJECT_NAME, hsqlModel, iProps, null,
				teiidImporterProperties);

		teiidBot.assertResource(PROJECT_NAME, hsqlModel + ".xmi", "CUSTOMER");
	}

	@Test
	public void h2Test() {
		// use default DV ds
		Properties iProps = new Properties();
		iProps.setProperty(TeiidConnectionImportWizard.DATA_SOURCE_NAME, "ExampleDS");
		String model = "h2Imp";
		String items = "Objects to Create/CONSTANTS";
		iProps.setProperty(TeiidConnectionImportWizard.TABLES_TO_IMPORT, items);
		
		Properties teiidImporterProperties = new Properties();		
		teiidImporterProperties.setProperty(TeiidConnectionImportWizard.IMPORT_PROPERTY_TABLE_TYPES, "SYSTEM TABLE");

		new ImportMetadataManager().importFromTeiidConnection(PROJECT_NAME, model, iProps, null, teiidImporterProperties);
		teiidBot.assertResource(PROJECT_NAME, model + ".xmi", "CONSTANTS");

	}

	@Test
	public void salesforceTest() {
		Properties iProps = new Properties();
		iProps.setProperty(TeiidConnectionImportWizard.CREATE_NEW_DATA_SOURCE, "true");
		iProps.setProperty(TeiidConnectionImportWizard.NEW_DATA_SOURCE_NAME, "sfDS");
		iProps.setProperty(TeiidConnectionImportWizard.DRIVER_NAME, "salesforce");

		new ServersViewExt().deleteDatasource(teiidServer.getName(), "sfDS");

		Properties sfProps = teiidServer.getServerConfig().getConnectionProfile("salesforce").asProperties();

		Properties dsProps = new Properties();
		dsProps.setProperty("* Password", sfProps.getProperty("db.password"));
		dsProps.setProperty("* User Name", sfProps.getProperty("db.username"));

		String model = "sfImp";

		new ImportMetadataManager().importFromTeiidConnection(PROJECT_NAME, model, iProps, dsProps);

		checkImportedModel(model, "Account", "Vote", "Profile");
	}

	@Test
	public void teiidTest() {
		new ModelExplorerManager().changeConnectionProfile(ConnectionProfilesConstants.SQL_SERVER_2008_BOOKS,
				PROJECT_NAME, sqlserverExistingModelName);

		VDBManager vdbManager = new VDBManager();
		vdbManager.createVDB(PROJECT_NAME, SOURCE_VDB_NAME);
		vdbManager.addModelsToVDB(PROJECT_NAME, SOURCE_VDB_NAME, new String[] { sqlserverExistingModelName });

		new DefaultShell();

		String[] pathToVDB = new String[] { PROJECT_NAME, SOURCE_VDB_NAME + ".vdb" };

		vdbManager.deployVDB(pathToVDB);

		try {
			vdbManager.createVDBDataSource(pathToVDB);
		} catch (Exception e) {
			System.err.println("VDB data source exists");
		}

		Properties iProps = new Properties();
		iProps.setProperty(TeiidConnectionImportWizard.DATA_SOURCE_NAME, SOURCE_VDB_NAME);

		Properties teiidImporterProperties = new Properties();
		teiidImporterProperties.setProperty(TeiidConnectionImportWizard.IMPORT_PROPERTY_SCHEMA_PATTERN, "%sqlserver%");

		String model = SOURCE_VDB_NAME + "Imp";

		new ImportMetadataManager().importFromTeiidConnection(PROJECT_NAME, model, iProps, null,
				teiidImporterProperties);
		teiidBot.assertResource(PROJECT_NAME, model + ".xmi", "AUTHORS");

	}

	@Test
	public void db2101Test() {
		Properties teiidImporterProperties = new Properties();
		teiidImporterProperties.setProperty(TeiidConnectionImportWizard.IMPORT_PROPERTY_TABLE_NAME_PATTERN, "SMALL%");
		teiidImporterProperties.setProperty(TeiidConnectionImportWizard.IMPORT_PROPERTY_SCHEMA_PATTERN, "%BQT%");
		importModel(ConnectionProfilesConstants.DB2_101_BQT, "db2101Model", teiidImporterProperties);
		checkImportedModel("db2101Model", "SMALLA", "SMALLB");

	}

	@Test
	public void db297Test() {
		Properties teiidImporterProperties = new Properties();
		teiidImporterProperties.setProperty(TeiidConnectionImportWizard.IMPORT_PROPERTY_TABLE_NAME_PATTERN, "SMALL%");
		importModel(ConnectionProfilesConstants.DB2_97_BQT2, "db297Model", teiidImporterProperties);
		checkImportedModel("db297Model", "SMALLA", "SMALLB");

	}

	@Test
	public void ingres10Test() {
		Properties teiidImporterProperties = new Properties();
		teiidImporterProperties.setProperty(TeiidConnectionImportWizard.IMPORT_PROPERTY_TABLE_NAME_PATTERN, "small%");
		importModel(ConnectionProfilesConstants.INGRES_10_BQT2, "ingres10Model", teiidImporterProperties);
		checkImportedModel("ingres10Model", "smalla", "smallb");

	}

	@Test
	public void oracle11gTest() {
		Properties teiidImporterProperties = new Properties();
		teiidImporterProperties.setProperty(TeiidConnectionImportWizard.IMPORT_PROPERTY_TABLE_NAME_PATTERN, "SMALL%");
		teiidImporterProperties.setProperty(TeiidConnectionImportWizard.IMPORT_PROPERTY_SCHEMA_PATTERN, "BQT2");
		importModel(ConnectionProfilesConstants.ORACLE_11G_BQT2, "oracle11gModel", teiidImporterProperties);
		checkImportedModel("oracle11gModel", "SMALLA", "SMALLB");

	}

	@Test
	public void oracle12cTest() {
		Properties teiidImporterProperties = new Properties();
		teiidImporterProperties.setProperty(TeiidConnectionImportWizard.IMPORT_PROPERTY_TABLE_NAME_PATTERN, "SMALL%");
		importModel(ConnectionProfilesConstants.ORACLE_12C_BQT, "oracle12cModel", teiidImporterProperties);
		checkImportedModel("oracle12cModel", "SMALLA", "SMALLB");

	}

	@Test
	public void sqlServer2008Test() {
		Properties teiidImporterProperties = new Properties();
		teiidImporterProperties.setProperty(TeiidConnectionImportWizard.IMPORT_PROPERTY_TABLE_NAME_PATTERN, "Small%");
		importModel(ConnectionProfilesConstants.SQL_SERVER_2008_BQT2, "sqlServer2008Model", teiidImporterProperties);
		checkImportedModel("sqlServer2008Model", "SmallA", "SmallB");

	}

	@Test
	public void sqlServer2012Test() {
		Properties teiidImporterProperties = new Properties();
		teiidImporterProperties.setProperty(TeiidConnectionImportWizard.IMPORT_PROPERTY_TABLE_NAME_PATTERN, "Small%");
		importModel(ConnectionProfilesConstants.SQL_SERVER_2012_BQT2, "sqlServer2012Model", teiidImporterProperties);
		checkImportedModel("sqlServer2012Model", "SmallA", "SmallB");

	}

	@Test
	public void sybaseTest() {
		Properties teiidImporterProperties = new Properties();
		teiidImporterProperties.setProperty(TeiidConnectionImportWizard.IMPORT_PROPERTY_TABLE_NAME_PATTERN, "Small%");
		importModel(ConnectionProfilesConstants.SYBASE_15_BQT2, "sybaseModel", teiidImporterProperties);
		checkImportedModel("sybaseModel", "SmallA", "SmallB");

	}

	@Test
	public void mysql51Test() {
		Properties teiidImporterProperties = new Properties();
		teiidImporterProperties.setProperty(TeiidConnectionImportWizard.IMPORT_PROPERTY_TABLE_NAME_PATTERN, "small%");
		importModel(ConnectionProfilesConstants.MYSQL_51_BQT2, "mysql51Model", teiidImporterProperties);
		checkImportedModel("mysql51Model", "smalla", "smallb");

	}

	@Test
	public void mysql55Test() {
		Properties teiidImporterProperties = new Properties();
		teiidImporterProperties.setProperty(TeiidConnectionImportWizard.IMPORT_PROPERTY_TABLE_NAME_PATTERN, "small%");
		importModel(ConnectionProfilesConstants.MYSQL_55_BQT2, "mysql55Model", teiidImporterProperties);
		checkImportedModel("mysql55Model", "smalla", "smallb");

	}

	@Test
	public void postgresql84Test() {
		Properties teiidImporterProperties = new Properties();
		teiidImporterProperties.setProperty(TeiidConnectionImportWizard.IMPORT_PROPERTY_TABLE_NAME_PATTERN, "small%");
		teiidImporterProperties.setProperty(TeiidConnectionImportWizard.IMPORT_PROPERTY_SCHEMA_PATTERN, "%public%");
		importModel(ConnectionProfilesConstants.POSTGRESQL_84_BQT2, "postgresql84Model", teiidImporterProperties);
		checkImportedModel("postgresql84Model", "smalla", "smallb");

	}

	@Test
	public void postgresql92Test() {
		Properties teiidImporterProperties = new Properties();
		teiidImporterProperties.setProperty(TeiidConnectionImportWizard.IMPORT_PROPERTY_TABLE_NAME_PATTERN, "small%");
		teiidImporterProperties.setProperty(TeiidConnectionImportWizard.IMPORT_PROPERTY_SCHEMA_PATTERN, "public");
		teiidImporterProperties.setProperty(TeiidConnectionImportWizard.IMPORT_PROPERTY_CATALOG, "dvqe");
		importModel(ConnectionProfilesConstants.POSTGRESQL_92_DVQE, "postgresql92Model", teiidImporterProperties);
		checkImportedModel("postgresql92Model", "smalla", "smallb");

	}

	@Test
	public void modeshapeTest() {
		Properties teiidImporterProperties = new Properties();
		teiidImporterProperties.setProperty(TeiidConnectionImportWizard.IMPORT_PROPERTY_TABLE_NAME_PATTERN,
				"mix:title");
		teiidImporterProperties.setProperty(TeiidConnectionImportWizard.IMPORT_PROPERTY_USE_FULL_SCHEMA_NAME, "false");

		Properties iProps = new Properties();
		iProps.setProperty(TeiidConnectionImportWizard.DATA_SOURCE_NAME, modeshapeCPName);

		// initialize modeshape
		String resp = new SimpleHttpClient("http://localhost:8080/modeshape-rest/dv/")
				.setBasicAuth(teiidServer.getServerConfig().getServerBase().getProperty("modeshapeUser"),
						teiidServer.getServerConfig().getServerBase().getProperty("modeshapePassword"))
				.get();

		assertFalse("initializing modeshape failed", resp.isEmpty());

		new ImportMetadataManager().importFromTeiidConnection(PROJECT_NAME, "ModeshapeModel", iProps, null,
				teiidImporterProperties);
		checkImportedModel("ModeshapeModel", "mix:title");
	}

	@Test
	public void excelTest() {
		String modelName = "ExcelModel";

		Properties iProps = new Properties();
		iProps.setProperty(TeiidConnectionImportWizard.CREATE_NEW_DATA_SOURCE, "true");
		iProps.setProperty(TeiidConnectionImportWizard.NEW_DATA_SOURCE_NAME, "excelDS");
		iProps.setProperty(TeiidConnectionImportWizard.DRIVER_NAME, "file");
		iProps.setProperty(TeiidConnectionImportWizard.TRANSLATOR, "excel");

		Properties dsProps = new Properties();
		Properties excelDsProperties = teiidServer.getServerConfig()
				.getConnectionProfile(ConnectionProfilesConstants.EXCEL_SMALLA).asProperties();
		dsProps.put(TeiidConnectionImportWizard.DATASOURCE_PROPERTY_PARENT_DIR, excelDsProperties.getProperty("path"));

		Properties teiidImporterProps = new Properties();
		teiidImporterProps.setProperty(TeiidConnectionImportWizard.IMPORT_PROPERTY_EXCEL_FILENAME,
				excelDsProperties.getProperty("filename"));
		teiidImporterProps.setProperty(TeiidConnectionImportWizard.IMPORT_PROPERTY_HEADER_ROW_NUMBER, "1");

		new ImportMetadataManager().importFromTeiidConnection(PROJECT_NAME, modelName, iProps, dsProps,
				teiidImporterProps);

		teiidBot.assertResource(PROJECT_NAME, modelName + ".xmi", "Sheet1");
		teiidBot.assertResource(PROJECT_NAME, modelName + ".xmi", "Sheet1", "ROW_ID : int");
		teiidBot.assertResource(PROJECT_NAME, modelName + ".xmi", "Sheet1", "StringNum : string");
	}

	@Test
	public void odataTest() {
		String modelName = "OdataModel";

		Properties iProps = new Properties();
		iProps.setProperty(TeiidConnectionImportWizard.CREATE_NEW_DATA_SOURCE, "true");
		iProps.setProperty(TeiidConnectionImportWizard.NEW_DATA_SOURCE_NAME, "odataDS");
		iProps.setProperty(TeiidConnectionImportWizard.DRIVER_NAME, "webservice");
		iProps.setProperty(TeiidConnectionImportWizard.TRANSLATOR, "odata");

		Properties dsProps = new Properties();
		dsProps.put(TeiidConnectionImportWizard.DATASOURCE_PROPERTY_URL,
				"http://services.odata.org/Northwind/Northwind.svc");

		new ImportMetadataManager().importFromTeiidConnection(PROJECT_NAME, modelName, iProps, dsProps);

		teiidBot.assertResource(PROJECT_NAME, modelName + ".xmi", "Customers");
		teiidBot.assertResource(PROJECT_NAME, modelName + ".xmi", "Customers", "CustomerID : string(5)");
		teiidBot.assertResource(PROJECT_NAME, modelName + ".xmi", "Employees", "EmployeeID : int");
	}
	
	@Test
	public void sapHanaTest() {
		String modelName = "sapHanaModel";
		
		Properties teiidImportProps = new Properties();
		teiidImportProps.setProperty(TeiidConnectionImportWizard.IMPORT_PROPERTY_SCHEMA_PATTERN, "BQT1");
		teiidImportProps.setProperty(TeiidConnectionImportWizard.IMPORT_PROPERTY_TABLE_NAME_PATTERN, "SMALL%");
		
		// TODO temp till hana translator is not set automatically (updated importModel method)
		Properties importProps = new Properties();
		importProps.setProperty(TeiidConnectionImportWizard.DATA_SOURCE_NAME, ConnectionProfilesConstants.SAP_HANA);
		importProps.setProperty(TeiidConnectionImportWizard.TRANSLATOR, "hana");
		ImportMetadataManager importMgr = new ImportMetadataManager();
		importMgr.importFromTeiidConnection(PROJECT_NAME, modelName, importProps, null, teiidImportProps);
//		importModel(ConnectionProfilesConstants.SAP_HANA, modelName, teiidImportProps);
		
		checkImportedModel(modelName, "SMALLA", "SMALLB");
	}

	private void checkImportedModel(String modelName, String... tables) {
		for (String table : tables) {
			teiidBot.assertResource(PROJECT_NAME, modelName + ".xmi", table);
		}
	}

	private void importModel(String cpName, String modelName, Properties teiidImporterProperties) {
		new DefaultShell();
		new ServersViewExt().createDatasource(teiidServer.getName(), cpName);

		Properties iProps = new Properties();
		iProps.setProperty(TeiidConnectionImportWizard.DATA_SOURCE_NAME, cpName);

		ImportMetadataManager importMgr = new ImportMetadataManager();
		importMgr.importFromTeiidConnection(PROJECT_NAME, modelName, iProps, null, teiidImporterProperties);
	}

}