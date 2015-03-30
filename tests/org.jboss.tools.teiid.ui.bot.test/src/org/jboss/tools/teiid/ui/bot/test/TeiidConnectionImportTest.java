package org.jboss.tools.teiid.ui.bot.test;

import java.util.Properties;

import org.eclipse.swtbot.swt.finder.SWTBotTestCase;
import org.jboss.reddeer.junit.requirement.inject.InjectRequirement;
import org.jboss.reddeer.junit.runner.RedDeerSuite;
import org.jboss.reddeer.requirements.server.ServerReqState;
import org.jboss.reddeer.swt.impl.shell.DefaultShell;
import org.jboss.tools.teiid.reddeer.manager.ConnectionProfileManager;
import org.jboss.tools.teiid.reddeer.manager.ImportManager;
import org.jboss.tools.teiid.reddeer.manager.ImportMetadataManager;
import org.jboss.tools.teiid.reddeer.manager.ModelExplorerManager;
import org.jboss.tools.teiid.reddeer.manager.VDBManager;
import org.jboss.tools.teiid.reddeer.requirement.TeiidServerRequirement;
import org.jboss.tools.teiid.reddeer.requirement.TeiidServerRequirement.TeiidServer;
import org.jboss.tools.teiid.reddeer.view.ModelExplorer;
import org.jboss.tools.teiid.reddeer.view.ModelExplorerView;
import org.jboss.tools.teiid.reddeer.view.ServersViewExt;
import org.jboss.tools.teiid.reddeer.wizard.TeiidConnectionImportWizard;
import org.jboss.tools.teiid.reddeer.wizard.TeiidConnectionProfileWizard;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
* Tests functionality of Teiid connection importer
* @author Lucie Fabrikova, lfabriko@redhat.com
*
*/
@RunWith(RedDeerSuite.class)
@TeiidServer(state = ServerReqState.RUNNING)
public class TeiidConnectionImportTest extends SWTBotTestCase{

	@InjectRequirement
	private static TeiidServerRequirement teiidServer;

	private static final String serverFile = "dv6.properties";
	private static final String PROJECT_NAME = "TeiidConnImporter";
	private static final String archiveLocation = "resources/projects/TeiidConnImporter.zip";
	private static final String serverName = "EAP-6.1";
	
	//SQL Server
//	private static final String sqlserverCP = "SQL Server";
//	private static final String sqlserverCPProps = "resources/db/sqlserver_books.properties";
//	private static final String sqlserverDSProps = "resources/teiidImporter/sqlserver_books_ds.properties";
	
	private static final String sqlserverExistingModelName = "sqlserver";
	
	private static final String sqlserverCPName= "sqlserver_books";
	private static final String sqlserverModelName = "SqlServerImported";
	
	//file
	private static final String fileDSProps = "resources/teiidImporter/file_items_ds.properties";
	private static final String FILE_MODEL = "file";
	
	//oracle
	private static final String oracleCPName = "oracle_parts";
	private static final String oracleModelName = "OracleImported";
	
	//hsql
	private static final String hsqlCPName= "hsqldb";
	private static final String hsqlModel = "HsqldbImported";
	
	//sybase
	private static final String sybaseCP = "Sybase";
	private static final String sybaseCPProps = "resources/db/sybase.properties";
	private static final String sybaseCPName= "sybase";
	private static final String sybaseModel = "sybase";
	
	//sybase jtds
	private static final String sybaseJtdsCP = "SybaseJtds";
	private static final String sybaseJtdsCPProps = "resources/db/sybase-jtds.properties";
	private static final String sybaseJtdsModel = "sybaseJtds";
	
	private static TeiidBot teiidBot = new TeiidBot();
	/**
	 * Create new Teiid Model Project
	 */
	@BeforeClass
	public static void createProject(){
		new ImportManager().importProject(teiidBot.toAbsolutePath(archiveLocation));
		
		String[] connectionProfiles = {oracleCPName, hsqlCPName, sqlserverCPName};
		
		for (String cpName : connectionProfiles){
			Properties cpProperties = teiidServer.getServerConfig().getConnectionProfile(cpName).asProperties();
			cpProperties.setProperty(TeiidConnectionProfileWizard.KEY_CONNECT_AFTER_COMPLETING, "false");
			new ConnectionProfileManager().createCPWithDriverDefinition(cpName,
					cpProperties);
			new ServersViewExt().createDatasource(teiidServer.getName(), cpName);
		}
		new ModelExplorer().getModelProject(PROJECT_NAME).open();

	}
	
	@Test // NOK -- causes NPE for this test and all remaining, logged as TEIIDDES-2456
	@Ignore
	public void fileTest(){
		Properties iProps = new Properties();
		iProps.setProperty(TeiidConnectionImportWizard.CREATE_NEW_DATA_SOURCE, "true");
		iProps.setProperty(TeiidConnectionImportWizard.NEW_DATA_SOURCE_NAME, "fileDS");
		iProps.setProperty(TeiidConnectionImportWizard.DRIVER_NAME, "file");
		
		new ServersViewExt().deleteDatasource(teiidServer.getName(), iProps.getProperty(TeiidConnectionImportWizard.NEW_DATA_SOURCE_NAME));
		
		Properties dsProps = new Properties();
		dsProps = teiidBot.getProperties("resources/teiidImporter/file_items_ds.properties");
		String absPath = teiidBot.toAbsolutePath(dsProps.getProperty("* Parent Directory"));
		dsProps.setProperty("* Parent Directory", absPath);
		
		new ImportMetadataManager().importFromTeiidConnection(PROJECT_NAME, FILE_MODEL, iProps, dsProps);

		teiidBot.assertResource(PROJECT_NAME, FILE_MODEL + ".xmi", "PARTS");
	}
	
	@Test
	public void oracleTest(){
		Properties iProps = new Properties();
		iProps.setProperty(TeiidConnectionImportWizard.DATA_SOURCE_NAME, oracleCPName);
		iProps.setProperty(TeiidConnectionImportWizard.DRIVER_NAME, "ojdbc6.jar");//??
		
		Properties teiidImporterProperties = new Properties();
		teiidImporterProperties.setProperty("Schema Pattern", "%PARTSSUPPLIER%");
		teiidImporterProperties.setProperty("Table Name Pattern", "PARTS");
		teiidImporterProperties.setProperty("SomeOtherProperty", "someValue");
		
		new ImportMetadataManager().importFromTeiidConnection(PROJECT_NAME, oracleModelName, iProps, null, teiidImporterProperties);

		teiidBot.assertResource(PROJECT_NAME, oracleModelName + ".xmi", "PARTS");
		
		// FIXME: check the imported model
	}

	@Test
	public void hsqlTest(){
		Properties iProps = new Properties();
		iProps.setProperty(TeiidConnectionImportWizard.DATA_SOURCE_NAME, hsqlCPName);
		iProps.setProperty(TeiidConnectionImportWizard.DRIVER_NAME, "hsqldb.jar");
		iProps.setProperty("translator", "hsql");

		Properties teiidImporterProperties = new Properties();
		teiidImporterProperties.setProperty("Schema Pattern", "PUBLIC%");
		
		new ImportMetadataManager().importFromTeiidConnection(PROJECT_NAME,
			hsqlModel, iProps, null, teiidImporterProperties);
		

		teiidBot.assertResource(PROJECT_NAME, hsqlModel + ".xmi", "CUSTOMER");
		// FIXME: check the imported model
	}
	
	//@Test OK
//	public void sqlserverCreateNewDSTest() {
//		new ConnectionProfileManager().createCPWithDriverDefinition(sqlserverCP, sqlserverCPProps);
//		new ModelProjectManager().changeConnectionProfile(sqlserverCP, PROJECT_NAME, sqlserverModel);
//		new ServerManager().startServer(serverName);
//		
//		String sqlserverDSProps = "resources/teiidImporter/sqlserver_books_ds.properties";
//		
//		// sql server
//		Properties iProps = new Properties();
//		iProps.setProperty(
//				TeiidConnectionImportWizard.CREATE_NEW_DATA_SOURCE, "true");
//		iProps.setProperty(
//				TeiidConnectionImportWizard.NEW_DATA_SOURCE_NAME, "sqlDS");
//		iProps.setProperty(
//				TeiidConnectionImportWizard.DRIVER_NAME, "sqljdbc4.jar");
//		iProps.setProperty(
//				TeiidConnectionImportWizard.TABLES_TO_IMPORT,
//				"Objects to Create/BOOKS,Objects to Create/AUTHORS");// path separated by /, tables by ,
//		//create new source
//		//import from it
//		new ImportMetadataManager().importFromTeiidConnection(PROJECT_NAME, sqlserverModel+"Imp", iProps, new TeiidBot().getProperties(sqlserverDSProps));
//		teiidBot.checkResource(PROJECT_NAME, sqlserverModel+"Imp.xmi","BOOKS");
//		teiidBot.checkResource(PROJECT_NAME, sqlserverModel+"Imp.xmi","AUTHORS");
//	}
	
	@Test //OK
	public void sqlserverTest() {
		Properties sqlserverImportProps = new Properties();
		sqlserverImportProps.setProperty(TeiidConnectionImportWizard.DATA_SOURCE_NAME, sqlserverCPName);
		sqlserverImportProps.setProperty(
				TeiidConnectionImportWizard.DRIVER_NAME, "sqljdbc4.jar");

		new ImportMetadataManager().importFromTeiidConnection(PROJECT_NAME,
				sqlserverModelName, sqlserverImportProps, null);
		
		new DefaultShell();
		teiidBot.assertResource(PROJECT_NAME, sqlserverModelName + ".xmi","AUTHORS");
		
		// FIXME: check the imported model
	}
	
	//@Test //NOK
//	public void sybaseTest(){//requires module jconn3
//		setup(sybaseCP, sybaseCPProps, sybaseModel);
//		
//		Properties iProps = new Properties();
//		iProps.setProperty(TeiidConnectionImportWizard.DATA_SOURCE_NAME, sybaseModel);
//		iProps.setProperty(
//				TeiidConnectionImportWizard.DRIVER_NAME, "jconn3.jar");
//
//		try {
//			new ImportMetadataManager().importFromTeiidConnection(PROJECT_NAME,
//					sybaseModel+"Imp", iProps, null);
//		} catch (Exception e){
//			System.err.println("Sybase jconn3 test failed, " + e.getMessage()); e.printStackTrace();
//		}
//	}
	
	//@Test //NOK
//	public void sybaseJtdsTest(){//requires module jtds
//		setup(sybaseJtdsCP, sybaseJtdsCPProps, sybaseJtdsModel);
//		
//		Properties iProps = new Properties();
//		iProps.setProperty(TeiidConnectionImportWizard.DATA_SOURCE_NAME, sybaseJtdsModel);
//		iProps.setProperty(
//				TeiidConnectionImportWizard.DRIVER_NAME, "jtds-1.3.1.jar");
//
//		try {
//			new ImportMetadataManager().importFromTeiidConnection(PROJECT_NAME,
//					sybaseJtdsModel+"Imp", iProps, null);
//		} catch (Exception e){
//			System.err.println("Sybase jtds test failed, " + e.getMessage()); e.printStackTrace();
//		}
//	}
	
	@Test
	public void h2Test(){
		//use default DV ds
		Properties iProps = new Properties();
		iProps.setProperty(TeiidConnectionImportWizard.DATA_SOURCE_NAME, "ExampleDS");
		String model = "h2Imp";
		String items = "Objects to Create/CONSTANTS";
		iProps.setProperty(TeiidConnectionImportWizard.TABLES_TO_IMPORT, items);
		
		
		new ImportMetadataManager().importFromTeiidConnection(PROJECT_NAME, model, iProps, null);
		teiidBot.assertResource(PROJECT_NAME, model + ".xmi","CONSTANTS");

		// FIXME: check the imported model
	}
	
	@Test // NOK -- causes NPE for this test and all remaining, logged as TEIIDDES-2456
	@Ignore
	public void salesforceTest(){
		//create new source
		Properties iProps = new Properties();
		iProps.setProperty(TeiidConnectionImportWizard.CREATE_NEW_DATA_SOURCE, "true");
		iProps.setProperty(TeiidConnectionImportWizard.NEW_DATA_SOURCE_NAME, "sfDS");
		iProps.setProperty(TeiidConnectionImportWizard.DRIVER_NAME, "salesforce");
		
		new ServersViewExt().deleteDatasource(teiidServer.getName(), iProps.getProperty(TeiidConnectionImportWizard.NEW_DATA_SOURCE_NAME));
		
//		Properties sfProps = teiidBot.getProperties(teiidBot.toAbsolutePath("resources/db/salesforce.properties"));
		Properties sfProps = teiidServer.getServerConfig().getConnectionProfile("salesforce").asProperties();
		
		Properties dsProps = new Properties();
		dsProps.setProperty("* Password", sfProps.getProperty("db.password"));
		dsProps.setProperty("* User Name", sfProps.getProperty("db.username"));
		//import from it
		
		String model = "sfImp";
		
		new ImportMetadataManager().importFromTeiidConnection(PROJECT_NAME, model, iProps, dsProps);

		// FIXME: check the imported model
	}
	
	@Test
	public void teiidTest(){
		String sourceVdb = "teiid";

		new ModelExplorerManager().changeConnectionProfile(sqlserverCPName, PROJECT_NAME, sqlserverExistingModelName);
		
		new DefaultShell();
		
		String[] pathToVDB = new String[]{PROJECT_NAME, sourceVdb+".vdb"};
		
		try {
			new VDBManager().getVDBEditor(PROJECT_NAME, sourceVdb + ".vdb").synchronizeAll();
		} catch (Exception e){
			System.out.println("VDB is synchronized");
		}
		
		new VDBManager().deployVDB(pathToVDB);
		
		try {
			new VDBManager().createVDBDataSource(pathToVDB);
		} catch (Exception e){
			System.err.println("VDB data source exists");
		}
		
		Properties iProps = new Properties();
		iProps.setProperty(TeiidConnectionImportWizard.DATA_SOURCE_NAME, sourceVdb);
		
		String model = sourceVdb+"Imp";
		
		new ImportMetadataManager().importFromTeiidConnection(PROJECT_NAME, model, iProps, null);
		teiidBot.assertResource(PROJECT_NAME, model + ".xmi","AUTHORS");

		// FIXME: check the imported model
	}
	
	//TODO add resources which SHOULD work
}
