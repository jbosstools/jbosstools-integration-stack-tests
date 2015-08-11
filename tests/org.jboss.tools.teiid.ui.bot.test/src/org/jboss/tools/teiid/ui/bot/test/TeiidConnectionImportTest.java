package org.jboss.tools.teiid.ui.bot.test;

import static org.junit.Assert.assertEquals;

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
import org.jboss.tools.teiid.reddeer.requirement.TeiidServerRequirement;
import org.jboss.tools.teiid.reddeer.requirement.TeiidServerRequirement.TeiidServer;
import org.jboss.tools.teiid.reddeer.view.ModelExplorer;
import org.jboss.tools.teiid.reddeer.view.ServersViewExt;
import org.jboss.tools.teiid.reddeer.wizard.TeiidConnectionImportWizard;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * Tests functionality of Teiid connection importer
 * 
 * @author Lucie Fabrikova, lfabriko@redhat.com
 *
 */
@RunWith(RedDeerSuite.class)
@TeiidServer(state = ServerReqState.RUNNING, connectionProfiles = { ConnectionProfilesConstants.DB2_101_BQT,
		ConnectionProfilesConstants.DB2_81_BQT2, ConnectionProfilesConstants.DB2_97_BQT2,
		ConnectionProfilesConstants.ORACLE_10G_BQT2, ConnectionProfilesConstants.ORACLE_11G_BQT2,
		ConnectionProfilesConstants.ORACLE_12C_BQT, ConnectionProfilesConstants.SQL_SERVER_2005_BQT2,
		ConnectionProfilesConstants.SQL_SERVER_2008_BQT2, ConnectionProfilesConstants.SQL_SERVER_2012_BQT2,
		ConnectionProfilesConstants.DV6_DS1, ConnectionProfilesConstants.HSQLDB,
		ConnectionProfilesConstants.SQL_SERVER_2000_BQT2, ConnectionProfilesConstants.MYSQL_50_BQT2,
		ConnectionProfilesConstants.MYSQL_51_BQT2, ConnectionProfilesConstants.MYSQL_55_BQT2,
		ConnectionProfilesConstants.POSTGRESQL_84_BQT2, ConnectionProfilesConstants.POSTGRESQL_91_BQT2,
		ConnectionProfilesConstants.POSTGRESQL_92_DVQE, ConnectionProfilesConstants.SYBASE_15_BQT2,
		ConnectionProfilesConstants.INGRES_10_BQT2, ConnectionProfilesConstants.SALESFORCE,
		ConnectionProfilesConstants.SQL_SERVER_2008_BOOKS })
public class TeiidConnectionImportTest extends SWTBotTestCase {

	@InjectRequirement
	private static TeiidServerRequirement teiidServer;

	private static final String PROJECT_NAME = "TeiidConnImporter";
	private static final String projectLocation = "resources/projects/TeiidConnImporter";

	private static final String modeshapeCPName = "ModeShapeDS";

	private static final String sqlserverExistingModelName = "sqlserver";

	private static final String sqlserverCPName = ConnectionProfilesConstants.SQL_SERVER_2008_BOOKS;
	private static final String sqlserverModelName = "SqlServerImported";

	// file
	private static final String FILE_MODEL = "FileImported";

	// oracle
	private static final String oracleCPName = ConnectionProfilesConstants.ORACLE_11G_PARTS_SUPPLIER;
	private static final String oracleModelName = "OracleImported";

	// hsql
	private static final String hsqlCPName = ConnectionProfilesConstants.HSQLDB;
	private static final String hsqlModel = "HsqldbImported";

	// sybase
	private static final String sybaseCPName = ConnectionProfilesConstants.SYBASE_15_BQT2;
	private static final String sybaseModelName = "SybaseImported";

	// sybase jtds
	private static final String sybaseJtdsCP = "SybaseJtds";
	private static final String sybaseJtdsCPProps = "resources/db/sybase-jtds.properties";
	private static final String sybaseJtdsModel = "sybaseJtds";

	private static TeiidBot teiidBot = new TeiidBot();

	/**
	 * Create new Teiid Model Project
	 */
	@BeforeClass
	public static void createProject() {
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

		teiidBot.assertResource(PROJECT_NAME, hsqlModel + ".xmi", "PUBLIC.PUBLIC.CUSTOMER");
	}

	@Test
	public void h2Test() {
		// use default DV ds
		Properties iProps = new Properties();
		iProps.setProperty(TeiidConnectionImportWizard.DATA_SOURCE_NAME, "ExampleDS");
		String model = "h2Imp";
		String items = "Objects to Create/TEST.INFORMATION_SCHEMA.CONSTANTS";
		iProps.setProperty(TeiidConnectionImportWizard.TABLES_TO_IMPORT, items);

		new ImportMetadataManager().importFromTeiidConnection(PROJECT_NAME, model, iProps, null);
		teiidBot.assertResource(PROJECT_NAME, model + ".xmi", "TEST.INFORMATION_SCHEMA.CONSTANTS");

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

	}

	@Test
	public void teiidTest() {
		String sourceVdb = "teiid";

		new ModelExplorerManager().changeConnectionProfile(ConnectionProfilesConstants.SQL_SERVER_2008_BOOKS,
				PROJECT_NAME, sqlserverExistingModelName);

		new DefaultShell();

		String[] pathToVDB = new String[] { PROJECT_NAME, sourceVdb + ".vdb" };

		try {
			new VDBManager().getVDBEditor(PROJECT_NAME, sourceVdb + ".vdb").synchronizeAll();
		} catch (Exception e) {
			System.out.println("VDB is synchronized");
		}

		new VDBManager().deployVDB(pathToVDB);

		try {
			new VDBManager().createVDBDataSource(pathToVDB);
		} catch (Exception e) {
			System.err.println("VDB data source exists");
		}

		Properties iProps = new Properties();
		iProps.setProperty(TeiidConnectionImportWizard.DATA_SOURCE_NAME, sourceVdb);
		
		Properties teiidImporterProperties = new Properties();
		teiidImporterProperties.setProperty(TeiidConnectionImportWizard.IMPORT_PROPERTY_SCHEMA_PATTERN, "%sqlserver%");

		String model = sourceVdb + "Imp";

		new ImportMetadataManager().importFromTeiidConnection(PROJECT_NAME, model, iProps, null, teiidImporterProperties);
		teiidBot.assertResource(PROJECT_NAME, model + ".xmi", "teiid.sqlserver.AUTHORS");

	}

	@Test
	public void db2101Test() {
		Properties teiidImporterProperties = new Properties();
		teiidImporterProperties.setProperty(TeiidConnectionImportWizard.IMPORT_PROPERTY_TABLE_NAME_PATTERN, "SMALL%");
		teiidImporterProperties.setProperty(TeiidConnectionImportWizard.IMPORT_PROPERTY_SCHEMA_PATTERN, "%BQT%");
		importModel(ConnectionProfilesConstants.DB2_101_BQT, "db2101Model", teiidImporterProperties);
		checkImportedModel("db2101Model", "BQT.SMALLA", "BQT.SMALLB");

	}

	@Test
	public void db297Test() {
		Properties teiidImporterProperties = new Properties();
		teiidImporterProperties.setProperty(TeiidConnectionImportWizard.IMPORT_PROPERTY_TABLE_NAME_PATTERN, "SMALL%");
		importModel(ConnectionProfilesConstants.DB2_97_BQT2, "db297Model", teiidImporterProperties);
		checkImportedModel("db297Model", "BQT2.SMALLA", "BQT2.SMALLB");

	}

	@Test
	public void ingres10Test() {
		Properties teiidImporterProperties = new Properties();
		teiidImporterProperties.setProperty(TeiidConnectionImportWizard.IMPORT_PROPERTY_TABLE_NAME_PATTERN, "small%");
		importModel(ConnectionProfilesConstants.INGRES_10_BQT2, "ingres10Model", teiidImporterProperties);
		checkImportedModel("ingres10Model", "bqt2.smalla", "bqt2.smallb");

	}

	@Test
	public void oracle10gTest() {
		Properties teiidImporterProperties = new Properties();
		teiidImporterProperties.setProperty(TeiidConnectionImportWizard.IMPORT_PROPERTY_TABLE_NAME_PATTERN, "SMALL%");
		importModel(ConnectionProfilesConstants.ORACLE_10G_BQT2, "oracle10gModel", teiidImporterProperties);
		checkImportedModel("oracle10gModel", "SMALLA", "SMALLB");

	}

	@Test
	public void oracle11gTest() {
		Properties teiidImporterProperties = new Properties();
		teiidImporterProperties.setProperty(TeiidConnectionImportWizard.IMPORT_PROPERTY_TABLE_NAME_PATTERN, "SMALL%");
		importModel(ConnectionProfilesConstants.ORACLE_11G_BQT2, "oracle11gModel", teiidImporterProperties);
		checkImportedModel("oracle11gModel", "BQT2.SMALLA", "BQT2.SMALLB");

	}

	@Test
	public void oracle12cTest() {
		Properties teiidImporterProperties = new Properties();
		teiidImporterProperties.setProperty(TeiidConnectionImportWizard.IMPORT_PROPERTY_TABLE_NAME_PATTERN, "SMALL%");
		importModel(ConnectionProfilesConstants.ORACLE_12C_BQT, "oracle12cModel", teiidImporterProperties);
		checkImportedModel("oracle12cModel", "DV.SMALLA", "DV.SMALLB");

	}

	@Test
	public void sqlServer2005Test() {
		Properties teiidImporterProperties = new Properties();
		teiidImporterProperties.setProperty(TeiidConnectionImportWizard.IMPORT_PROPERTY_TABLE_NAME_PATTERN, "Small%");
		importModel(ConnectionProfilesConstants.SQL_SERVER_2005_BQT2, "sqlServer2005Model", teiidImporterProperties);
		checkImportedModel("sqlServer2005Model", "bqt2.BQT2.SmallA", "bqt2.BQT2.SmallB");

	}

	@Test
	public void sqlServer2008Test() {
		Properties teiidImporterProperties = new Properties();
		teiidImporterProperties.setProperty(TeiidConnectionImportWizard.IMPORT_PROPERTY_TABLE_NAME_PATTERN, "Small%");
		importModel(ConnectionProfilesConstants.SQL_SERVER_2008_BQT2, "sqlServer2008Model", teiidImporterProperties);
		checkImportedModel("sqlServer2008Model", "bqt2.dbo.SmallA", "bqt2.dbo.SmallB");

	}

	@Test
	public void sqlServer2012Test() {
		Properties teiidImporterProperties = new Properties();
		teiidImporterProperties.setProperty(TeiidConnectionImportWizard.IMPORT_PROPERTY_TABLE_NAME_PATTERN, "Small%");
		importModel(ConnectionProfilesConstants.SQL_SERVER_2012_BQT2, "sqlServer2012Model", teiidImporterProperties);
		checkImportedModel("sqlServer2012Model", "bqt2.dbo.SmallA", "bqt2.dbo.SmallB");

	}

	@Test
	public void sybaseTest() {
		Properties teiidImporterProperties = new Properties();
		teiidImporterProperties.setProperty(TeiidConnectionImportWizard.IMPORT_PROPERTY_TABLE_NAME_PATTERN, "Small%");
		importModel(ConnectionProfilesConstants.SYBASE_15_BQT2, "sybaseModel", teiidImporterProperties);
		checkImportedModel("sybaseModel", "bqt2.dbo.SmallA", "bqt2.dbo.SmallB");

	}

	@Test
	public void mysql50Test() {
		Properties teiidImporterProperties = new Properties();
		teiidImporterProperties.setProperty(TeiidConnectionImportWizard.IMPORT_PROPERTY_TABLE_NAME_PATTERN, "small%");
		importModel(ConnectionProfilesConstants.MYSQL_50_BQT2, "mysql50Model", teiidImporterProperties);
		checkImportedModel("mysql50Model", "bqt2.smalla", "bqt2.smallb");

	}

	@Test
	public void mysql51Test() {
		Properties teiidImporterProperties = new Properties();
		teiidImporterProperties.setProperty(TeiidConnectionImportWizard.IMPORT_PROPERTY_TABLE_NAME_PATTERN, "small%");
		importModel(ConnectionProfilesConstants.MYSQL_51_BQT2, "mysql51Model", teiidImporterProperties);
		checkImportedModel("mysql51Model", "bqt2.smalla", "bqt2.smallb");

	}

	@Test
	public void mysql55Test() {
		Properties teiidImporterProperties = new Properties();
		teiidImporterProperties.setProperty(TeiidConnectionImportWizard.IMPORT_PROPERTY_TABLE_NAME_PATTERN, "small%");
		importModel(ConnectionProfilesConstants.MYSQL_55_BQT2, "mysql55Model", teiidImporterProperties);
		checkImportedModel("mysql55Model", "bqt2.smalla", "bqt2.smallb");

	}

	@Test
	public void postgresql84Test() {
		Properties teiidImporterProperties = new Properties();
		teiidImporterProperties.setProperty(TeiidConnectionImportWizard.IMPORT_PROPERTY_TABLE_NAME_PATTERN, "small%");
		teiidImporterProperties.setProperty(TeiidConnectionImportWizard.IMPORT_PROPERTY_SCHEMA_PATTERN, "%public%");
		importModel(ConnectionProfilesConstants.POSTGRESQL_84_BQT2, "postgresql84Model", teiidImporterProperties);
		checkImportedModel("postgresql84Model", "bqt2.smalla", "bqt2.smallb");

	}

	@Test
	public void postgresql91Test() {
		Properties teiidImporterProperties = new Properties();
		teiidImporterProperties.setProperty(TeiidConnectionImportWizard.IMPORT_PROPERTY_TABLE_NAME_PATTERN, "small%");
		teiidImporterProperties.setProperty(TeiidConnectionImportWizard.IMPORT_PROPERTY_SCHEMA_PATTERN, "public");
		importModel(ConnectionProfilesConstants.POSTGRESQL_91_BQT2, "postgresql91Model", teiidImporterProperties);
		checkImportedModel("postgresql91Model", "public.smalla", "public.smallb");

	}

	@Test
	public void postgresql92Test() {
		Properties teiidImporterProperties = new Properties();
		teiidImporterProperties.setProperty(TeiidConnectionImportWizard.IMPORT_PROPERTY_TABLE_NAME_PATTERN, "small%");
		teiidImporterProperties.setProperty(TeiidConnectionImportWizard.IMPORT_PROPERTY_SCHEMA_PATTERN, "public");
		teiidImporterProperties.setProperty(TeiidConnectionImportWizard.IMPORT_PROPERTY_CATALOG, "dvqe");
		importModel(ConnectionProfilesConstants.POSTGRESQL_92_DVQE, "postgresql92Model", teiidImporterProperties);
		checkImportedModel("postgresql92Model", "public.smalla", "public.smallb");

	}

	@Test
	public void modeshapeTest() {
		Properties teiidImporterProperties = new Properties();
		teiidImporterProperties
				.setProperty(TeiidConnectionImportWizard.IMPORT_PROPERTY_TABLE_NAME_PATTERN, "mix:title");
		teiidImporterProperties.setProperty(TeiidConnectionImportWizard.IMPORT_PROPERTY_USE_FULL_SCHEMA_NAME, "false");
	
		Properties iProps = new Properties();
		iProps.setProperty(TeiidConnectionImportWizard.DATA_SOURCE_NAME, modeshapeCPName);
		
		
		// initialize modeshape
		String userPass = teiidServer.getServerConfig().getServerBase().getProperty("modeshapeUser") + ':'
				+ teiidServer.getServerConfig().getServerBase().getProperty("modeshapePassword");
		String curlArgs = "-u " + userPass + " http://localhost:8080/modeshape-rest/dv/";
		teiidBot.curl(curlArgs);
		
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
		dsProps.put(TeiidConnectionImportWizard.DATASOURCE_PROPERTY_PARENT_DIR,
				teiidBot.toAbsolutePath("resources/excel"));

		Properties teiidImporterProps = new Properties();
		teiidImporterProps.setProperty(TeiidConnectionImportWizard.IMPORT_PROPERTY_EXCEL_FILENAME, "smalla.xlsx");
		teiidImporterProps.setProperty(TeiidConnectionImportWizard.IMPORT_PROPERTY_HEADER_ROW_NUMBER, "1");

		new ImportMetadataManager().importFromTeiidConnection(PROJECT_NAME, modelName, iProps, dsProps,
				teiidImporterProps);

		teiidBot.assertResource(PROJECT_NAME, modelName + ".xmi", "Sheet1");
		teiidBot.assertResource(PROJECT_NAME, modelName + ".xmi", "Sheet1", "ROW_ID : int");
		teiidBot.assertResource(PROJECT_NAME, modelName + ".xmi", "Sheet1", "StringNum : string(10)");
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

	private void checkImportedModel(String modelName, String... tables) {
		new DefaultShell();
		for (String table : tables) {
			teiidBot.assertResource(PROJECT_NAME, modelName + ".xmi", table);
		}
	}

	private void importModel(String cpName, String modelName, Properties teiidImporterProperties) {
		new DefaultShell();
		new ServersViewExt().createDatasource(teiidServer.getName(), cpName);

		Properties iProps = new Properties();
		iProps.setProperty(TeiidConnectionImportWizard.DATA_SOURCE_NAME, cpName);

		new ImportMetadataManager().importFromTeiidConnection(PROJECT_NAME, modelName, iProps, null,
				teiidImporterProperties);
	}

}
