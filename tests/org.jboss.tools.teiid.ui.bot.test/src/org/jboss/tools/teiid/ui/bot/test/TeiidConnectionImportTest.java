package org.jboss.tools.teiid.ui.bot.test;

import java.util.Properties;

import org.eclipse.swtbot.swt.finder.SWTBotTestCase;
import org.jboss.tools.teiid.reddeer.manager.ConnectionProfileManager;
import org.jboss.tools.teiid.reddeer.manager.ImportManager;
import org.jboss.tools.teiid.reddeer.manager.ImportMetadataManager;
import org.jboss.tools.teiid.reddeer.manager.ModelExplorerManager;
import org.jboss.tools.teiid.reddeer.manager.ServerManager;
import org.jboss.tools.teiid.reddeer.manager.VDBManager;
import org.jboss.tools.teiid.reddeer.view.ModelExplorerView;
import org.jboss.tools.teiid.reddeer.wizard.TeiidConnectionImportWizard;
import org.jboss.tools.teiid.ui.bot.test.requirement.PerspectiveRequirement.Perspective;
import org.junit.BeforeClass;
import org.junit.Test;

/**
* Tests functionality of Teiid connection importer
* @author Lucie Fabrikova, lfabriko@redhat.com
*
*/
@Perspective(name = "Teiid Designer")
public class TeiidConnectionImportTest extends SWTBotTestCase{

	private static final String serverFile = "dv6.properties";
	private static final String PROJECT_NAME = "TeiidConnImporter";
	private static final String archiveLocation = "resources/projects/TeiidConnImporter.zip";
	private static final String serverName = "EAP-6.1";
	
	//SQL Server
	private static final String sqlserverCP = "SQL Server";
	private static final String sqlserverCPProps = "resources/db/sqlserver_books.properties";
	private static final String sqlserverDSProps = "resources/teiidImporter/sqlserver_books_ds.properties";
	private static final String sqlserverModel = "sqlserver";
	
	//file
	private static final String fileDSProps = "resources/teiidImporter/file_items_ds.properties";
	private static final String FILE_MODEL = "file";
	
	//oracle
	private static final String oracleCP = "Oracle";
	private static final String oracleCPProps = "resources/db/oracle_parts.properties";
	private static final String oracleModel = "oracle";
	
	//hsql
	private static final String hsqlCP = "Hsql";
	//private static final String hsqlCPProps = "resources/db/ds1.properties";
	private static final String hsqlCPProps = "resources/db/hsqldb.properties";
	private static final String hsqlModel = "hsqldb";
	
	//sybase
	private static final String sybaseCP = "Sybase";
	private static final String sybaseCPProps = "resources/db/sybase.properties";
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
		new ServerManager().addServer(serverFile);
		new ImportManager().importProject(teiidBot.toAbsolutePath(archiveLocation));
		
		//create all connection profiles
		//TEMP new ConnectionProfileManager().createCPWithDriverDefinition(sqlserverCP, sqlserverCPProps);
		//TEMP new ConnectionProfileManager().createCPWithDriverDefinition(oracleCP, oracleCPProps);
		//TEMP new ConnectionProfileManager().createCPWithDriverDefinition(hsqlCP, hsqlCPProps);
		//TEMP new ConnectionProfileManager().createCPWithDriverDefinition(sybaseCP, sybaseCPProps);
		//TEMP new ConnectionProfileManager().createCPWithDriverDefinition(sybaseJtdsCP, sybaseJtdsCPProps);
		
		//change connection profile of model
		//TEMP new ModelProjectManager().changeConnectionProfile(sqlserverCP, PROJECT_NAME, SQLSERVER_MODEL);
		//TEMP new ModelProjectManager().changeConnectionProfile(oracleCP, PROJECT_NAME, oracleModel);
		//TEMP new ModelProjectManager().changeConnectionProfile(hsqlCP, PROJECT_NAME, hsqlModel);
		//TEMP new ModelProjectManager().changeConnectionProfile(sybaseCP, PROJECT_NAME, sybaseModel);
		//TEMP new ModelProjectManager().changeConnectionProfile(sybaseJtdsCP, PROJECT_NAME, sybaseJtdsModel);
		
		setup(oracleCP, oracleCPProps, oracleModel);
		setup(hsqlCP, hsqlCPProps, hsqlModel);
		setup(sqlserverCP, sqlserverCPProps, sqlserverModel);
		
		
		//^ with stopped server
		
		//TEMP 
		new ServerManager().startServer("EAP-6.1");
		
		//create data sources
		//TEMP 
		new ModelExplorerManager().createDataSource(ModelExplorerView.ConnectionSourceType.USE_CONNECTION_PROFILE_INFO, sqlserverCP, PROJECT_NAME, sqlserverModel);
		//TEMP 
		new ModelExplorerManager().createDataSource(ModelExplorerView.ConnectionSourceType.USE_CONNECTION_PROFILE_INFO, oracleCP, PROJECT_NAME, oracleModel);
		//TEMP
		new ModelExplorerManager().createDataSource(ModelExplorerView.ConnectionSourceType.USE_CONNECTION_PROFILE_INFO, hsqlCP, PROJECT_NAME, hsqlModel);
		//TEMP new ModelProjectManager().createDataSource(ModelExplorerView.ConnectionSourceType.USE_CONNECTION_PROFILE_INFO, sybaseCP, PROJECT_NAME, sybaseModel);
		//TEMP new ModelProjectManager().createDataSource(ModelExplorerView.ConnectionSourceType.USE_CONNECTION_PROFILE_INFO, sybaseJtdsCP, PROJECT_NAME, sybaseJtdsModel);
	}
	
	@Test //NOK - swtbot/teiiddes import fails
	public void fileTest(){
		Properties iProps = new Properties();
		iProps.setProperty(TeiidConnectionImportWizard.CREATE_NEW_DATA_SOURCE, "true");
		iProps.setProperty(TeiidConnectionImportWizard.NEW_DATA_SOURCE_NAME, "fileDS");
		iProps.setProperty(TeiidConnectionImportWizard.DRIVER_NAME, "file");
		
		Properties dsProps = new Properties();
		dsProps = teiidBot.getProperties("resources/teiidImporter/file_items_ds.properties");
		String absPath = teiidBot.toAbsolutePath(dsProps.getProperty("* Parent Directory"));
		dsProps.setProperty("* Parent Directory", absPath);
		
		try {
			new ImportMetadataManager().importFromTeiidConnection(PROJECT_NAME, FILE_MODEL, iProps, dsProps);
		} catch (Exception e){
			System.err.println("File test failed, " + e.getMessage()); e.printStackTrace();
		}
	}
	
	@Test //NOK timeout
	public void oracleTest(){
		Properties iProps = new Properties();
		iProps.setProperty(TeiidConnectionImportWizard.DATA_SOURCE_NAME, oracleModel);
		iProps.setProperty(TeiidConnectionImportWizard.DRIVER_NAME, "ojdbc6.jar");//??
		
		try {
			new ImportMetadataManager().importFromTeiidConnection(PROJECT_NAME, oracleModel+"Imported", iProps, null);
		} catch (Exception e){
			System.err.println("Oracle test failed, " + e.getMessage()); e.printStackTrace();
		}
	}

	@Test //NOK import fails - ddl invalid - timeout
	public void hsqlTest(){
		Properties iProps = new Properties();
		iProps.setProperty(TeiidConnectionImportWizard.DATA_SOURCE_NAME, hsqlModel);
		iProps.setProperty(
				TeiidConnectionImportWizard.DRIVER_NAME, "hsqldb.jar");
		
		try {
			new ImportMetadataManager().importFromTeiidConnection(PROJECT_NAME,
					hsqlModel+"Imported", iProps, null);
		} catch (Exception e){
			System.err.println("Hsql test failed, " + e.getMessage()); e.printStackTrace();
		}
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
		sqlserverImportProps.setProperty(TeiidConnectionImportWizard.DATA_SOURCE_NAME, sqlserverModel);
		sqlserverImportProps.setProperty(
				TeiidConnectionImportWizard.DRIVER_NAME, "sqljdbc4.jar");

		try {
			new ImportMetadataManager().importFromTeiidConnection(PROJECT_NAME,
					sqlserverModel+"Imp", sqlserverImportProps, null);
		} catch (Exception e){
			System.err.println("SQL server test failed, " + e.getMessage()); e.printStackTrace();
		}
		
		teiidBot.assertResource(PROJECT_NAME, sqlserverModel+"Imp.xmi","AUTHORS");
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
		String items = "Object to Create/CONSTANTS";
		iProps.setProperty(TeiidConnectionImportWizard.TABLES_TO_IMPORT, items);
		
		try {
			new ImportMetadataManager().importFromTeiidConnection(PROJECT_NAME, model, iProps, null);
		} catch (Exception e){
			System.err.println("H2 test failed, " + e.getMessage()); e.printStackTrace();
		}
	}
	
	@Test
	public void salesforceTest(){
		//create new source
		Properties iProps = new Properties();
		iProps.setProperty(TeiidConnectionImportWizard.CREATE_NEW_DATA_SOURCE, "true");
		iProps.setProperty(TeiidConnectionImportWizard.NEW_DATA_SOURCE_NAME, "sfDS");
		iProps.setProperty(TeiidConnectionImportWizard.DRIVER_NAME, "salesforce");
		
		Properties sfProps = teiidBot.getProperties(teiidBot.toAbsolutePath("resources/db/salesforce.properties"));
		
		Properties dsProps = new Properties();
		dsProps.setProperty("* Password", sfProps.getProperty("db.password"));
		dsProps.setProperty("* User Name", sfProps.getProperty("db.username"));
		//import from it
		
		String model = "sfImp";
		try {
			new ImportMetadataManager().importFromTeiidConnection(PROJECT_NAME, model, iProps, dsProps);
		} catch (Exception e){
			System.err.println("Salesforce test failed, " + e.getMessage()); e.printStackTrace();
		}
	}
	
	@Test
	public void teiidTest(){
		String sourceVdb = "teiid";
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
		
		try {
			new ImportMetadataManager().importFromTeiidConnection(PROJECT_NAME, model, iProps, null);
		} catch (Exception e){
			System.err.println("Teiid test failed, " + e.getMessage()); e.printStackTrace();
		}
		
	}
	
	private static void setup(String cp, String cpProps, String model) {
		try {
			new ConnectionProfileManager().createCPWithDriverDefinition(cp, cpProps);
			new ModelExplorerManager().changeConnectionProfile(cp, PROJECT_NAME, model);
		} catch (Exception e){
			System.err.println("Setup failed, " + e.getMessage()); e.printStackTrace();
		}
	}
	//TODO add resources which SHOULD work
}
