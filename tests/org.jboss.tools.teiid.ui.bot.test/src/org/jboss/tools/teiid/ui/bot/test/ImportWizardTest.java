package org.jboss.tools.teiid.ui.bot.test;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.Properties;

import org.eclipse.swtbot.swt.finder.SWTBotTestCase;
import org.jboss.reddeer.eclipse.datatools.ui.FlatFileProfile;
import org.jboss.reddeer.eclipse.jdt.ui.ProjectExplorer;
import org.jboss.reddeer.eclipse.jdt.ui.packageexplorer.Project;
import org.jboss.reddeer.swt.impl.button.PushButton;
import org.eclipse.swtbot.eclipse.finder.SWTWorkbenchBot;
import org.jboss.tools.teiid.reddeer.ModelProject;
import org.jboss.tools.teiid.reddeer.editor.ModelEditor;
import org.jboss.tools.teiid.reddeer.manager.ConnectionProfileManager;
import org.jboss.tools.teiid.reddeer.manager.ImportManager;
import org.jboss.tools.teiid.reddeer.manager.ImportMetadataManager;
import org.jboss.tools.teiid.reddeer.view.ModelExplorer;
import org.jboss.tools.teiid.reddeer.wizard.DDLImportWizard;
import org.jboss.tools.teiid.reddeer.wizard.FlatImportWizard;
import org.jboss.tools.teiid.reddeer.wizard.FlatImportWizard.FlatFileImportMode;
import org.jboss.tools.teiid.reddeer.wizard.HSQLDBDriverWizard;
import org.jboss.tools.teiid.reddeer.wizard.HSQLDBProfileWizard;
import org.jboss.tools.teiid.reddeer.wizard.ImportJDBCDatabaseWizard;
import org.jboss.tools.teiid.reddeer.wizard.MetadataImportWizard;
import org.jboss.tools.teiid.reddeer.wizard.MetadataImportWizard.ImportType;
import org.jboss.tools.teiid.reddeer.wizard.TeiidImportWizard;
import org.jboss.tools.teiid.reddeer.wizard.WsdlImportWizard;
import org.jboss.tools.teiid.reddeer.wizard.WsdlProfileWizard;
import org.jboss.tools.teiid.reddeer.wizard.XMLImportWizard;
import org.jboss.tools.teiid.ui.bot.test.requirement.PerspectiveRequirement.Perspective;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * Tests for importing relational models from various sources
 * 
 * @author apodhrad
 * 
 */
@Perspective(name = "Teiid Designer")
public class ImportWizardTest extends SWTBotTestCase {

	public static final String MODEL_PROJECT = "importTest";

	private static TeiidBot teiidBot = new TeiidBot();

	@BeforeClass
	public static void createModelProject() {
		new org.jboss.reddeer.swt.impl.menu.ShellMenu("Project", "Build Automatically").select();
		//teiidBot.createModelProject(MODEL_PROJECT);
	}
	
	@AfterClass
	public static void closeAllShells(){
		new SWTWorkbenchBot().closeAllShells();
	}
	
	@After
	public void afterMethod(){
		System.out.println("TEST METHOD END");
	}

	@Test
	public void jdbcImportTest() {
		
		String jdbcProfile = "HSQLDB Profile";
		String importProps = "resources/db/hsql-employees.properties";
		
		String empModel = teiidBot.getProperties(importProps).getProperty("modelName");//"Emp.xmi";

		//createHSQLDBProfile(jdbcProfile);
		new ConnectionProfileManager().createCPWithDriverDefinition(jdbcProfile, "resources/db/hsqldb.properties");

		/*ImportJDBCDatabaseWizard wizard = new ImportJDBCDatabaseWizard();
		wizard.setConnectionProfile(jdbcProfile);
		wizard.setProjectName(MODEL_PROJECT);
		wizard.setModelName(empModel);
		wizard.addItem("PUBLIC/PUBLIC/TABLE/CUSTOMER");
		wizard.addItem("PUBLIC/PUBLIC/TABLE/ORDER");
		wizard.execute();*/
		
		new ImportManager().importFromDatabase(MODEL_PROJECT, empModel, jdbcProfile, teiidBot.getProperties(importProps));

		teiidBot.checkResource(MODEL_PROJECT, empModel);
		teiidBot.checkDiagram(MODEL_PROJECT, empModel, "ORDER");
		teiidBot.checkDiagram(MODEL_PROJECT, empModel, "CUSTOMER");
	}

	@Test
	public void flatImportTest() {
		String flatProfile = "Flat Profile";
		String propsFile = "resources/db/csv-items.properties";
		//FlatFileProfile flatFileProfile = teiidBot.createFlatFileProfile(flatProfile, "resources/flat");
		new ConnectionProfileManager().createCPFlatFile(flatProfile, "resources/flat");
		
		/*FlatImportWizard importWizard = new FlatImportWizard();
		importWizard.setProfile(flatProfile);
		importWizard.setName("Item");
		importWizard.setFile("items.csv     <<<<");
		importWizard.setImportMode(FlatFileImportMode.FLAT_FILE_ON_LOCAL_FILE_SYSTEM);
		
		ImportManager.importModel(MODEL_PROJECT, importWizard);*/
		new ImportMetadataManager().importFromFlatFile(MODEL_PROJECT, "Item", flatProfile, teiidBot.getProperties(propsFile));

		teiidBot.checkResource(MODEL_PROJECT, "ItemSource.xmi");
		teiidBot.checkResource(MODEL_PROJECT, "ItemView.xmi");
		teiidBot.checkDiagram(MODEL_PROJECT, "ItemSource.xmi", "getTextFiles");
		teiidBot.checkDiagram(MODEL_PROJECT, "ItemSource.xmi", "Result");
		teiidBot.checkDiagram(MODEL_PROJECT, "ItemView.xmi", "ItemTable");
	}

	@Test
	public void xmlImportTest() {
		String xmlProfile = "XML Local Profile";
		//teiidBot.createXmlProfile(xmlProfile, "resources/flat/accounts.xml");
		new ConnectionProfileManager().createCPXml(xmlProfile,  "resources/flat/accounts.xml");

		/*XMLImportWizard importWizard = new XMLImportWizard();
		importWizard.setLocal(true);
		importWizard.setName("Account");
		importWizard.setProfileName(xmlProfile);
		importWizard.setRootPath("/accounts/account");
		importWizard.addElement("accounts/account/nick");
		importWizard.addElement("accounts/account/balance");

		ImportManager.importModel(MODEL_PROJECT, importWizard);*/
		Properties props = new Properties();
		props.setProperty("local", "true");
		props.setProperty("RootPath", "/accounts/account");
		props.setProperty("elements", "accounts/account/nick,accounts/account/balance");
		
		new ImportMetadataManager().importFromXML(MODEL_PROJECT, "Account", xmlProfile, props);

		teiidBot.checkResource(MODEL_PROJECT, "AccountSource.xmi");
		teiidBot.checkResource(MODEL_PROJECT, "AccountView.xmi");
		teiidBot.checkDiagram(MODEL_PROJECT, "AccountSource.xmi", "getTextFiles");
		teiidBot.checkDiagram(MODEL_PROJECT, "AccountSource.xmi", "Result");
		teiidBot.checkDiagram(MODEL_PROJECT, "AccountView.xmi", "AccountTable");
	}

	//@Test//!!!
	public void wsdlImportTest() {
		String profile = "Hello Service";
		/*String wsdl = teiidBot.toAbsolutePath("resources/wsdl/Hello.wsdl");

		// Create wsdl profile
		WsdlProfileWizard profileWizard = new WsdlProfileWizard();
		profileWizard.setName(profile);
		profileWizard.setWsdl("file:" + wsdl);
		profileWizard.setEndPoint("HelloPort");//!!!PROBLEM - reddeer
		profileWizard.execute();*/
		
		Properties wsdlCP = new Properties();
		wsdlCP.setProperty("wsdl", "resources/wsdl/Hello.wsdl");
		wsdlCP.setProperty("endPoint", "HelloPort");
		
		new ConnectionProfileManager().createCPWSDL(profile, wsdlCP);

		// Import wsdl as model
		/*WsdlImportWizard importWizard = new WsdlImportWizard();
		importWizard.setProfile(profile);
		importWizard.addRequestElement("sayHello/sequence/arg0");
		importWizard.addResponseElement("sayHelloResponse/sequence/return");

		ImportManager.importModel(MODEL_PROJECT, importWizard);*/
		Properties props = new Properties();
		props.setProperty("requestElement", "sayHello/sequence/arg0");
		props.setProperty("responseElement", "sayHelloResponse/sequence/return");
		new ImportMetadataManager().importFromWSDLToSrcView(MODEL_PROJECT, profile, props);

		teiidBot.checkResource(MODEL_PROJECT, "HelloService.xmi");
		teiidBot.checkResource(MODEL_PROJECT, "HelloServiceView.xmi");
		teiidBot.checkDiagram(MODEL_PROJECT, "HelloService.xmi", "invoke");
		teiidBot.checkDiagram(MODEL_PROJECT, "HelloServiceView.xmi", "sayHello");
		teiidBot.checkDiagram(MODEL_PROJECT, "HelloServiceView.xmi", "sayHello_request");
		teiidBot.checkDiagram(MODEL_PROJECT, "HelloServiceView.xmi", "sayHello_response");
	}

	@Test
	public void ddlImportTest() {
		String ddl = teiidBot.toAbsolutePath("resources/ddl/hsqldb.ddl");

		/*DDLImportWizard importWizard = new DDLImportWizard();
		importWizard.setDdlPath(ddl);
		importWizard.setModelName("CustomerHsqldb");
		importWizard.setAutoselectDialect(true);

		ImportManager.importModel(MODEL_PROJECT, importWizard);*/
		Properties props = new Properties();
		props.setProperty("autoselectDialect", "true");
		new ImportMetadataManager().importFromDDL(MODEL_PROJECT, "CustomerHsqldb", ddl, props);

		teiidBot.checkResource(MODEL_PROJECT, "CustomerHsqldb.xmi");
		teiidBot.checkDiagram(MODEL_PROJECT, "CustomerHsqldb.xmi", "USER");
		teiidBot.checkDiagram(MODEL_PROJECT, "CustomerHsqldb.xmi", "ADDRESS");
	}

	@Test
	public void metadataModelImportTest() {
		String source = teiidBot.toAbsolutePath("resources/dtf/relationalModel.xml");
		String target = "RelationalModel.xmi";

		/*MetadataImportWizard importWizard = new MetadataImportWizard();
		importWizard.setImportType(ImportType.RELATIONAL_MODEL);
		importWizard.setSource(source);
		importWizard.setTarget(target);

		ImportManager.importModel(MODEL_PROJECT, importWizard);*/
		
		Properties props = new Properties();
		props.setProperty("source", source);
		props.setProperty("target", target);
		props.setProperty("importType", ImportType.RELATIONAL_MODEL);
		
		new ImportMetadataManager().importFromDesignerTextFile(MODEL_PROJECT, props);

		teiidBot.checkResource(MODEL_PROJECT, target);
		teiidBot.checkDiagram(MODEL_PROJECT, target, "ProductSymbols");
		teiidBot.checkDiagram(MODEL_PROJECT, target, "ProductData");
		teiidBot.checkDiagram(MODEL_PROJECT, target, "getProductInfo");
		teiidBot.checkDiagram(MODEL_PROJECT, target, "ProductIDIndex");
	}

	@Test
	public void metadataTableImportTest() {
		String source = teiidBot.toAbsolutePath("resources/dtf/relationalTable.csv");
		String target = "RelationalTable.xmi";

		/*MetadataImportWizard importWizard = new MetadataImportWizard();
		importWizard.setImportType(ImportType.RELATIONAL_TABLE);
		importWizard.setSource(source);
		importWizard.setTarget(target);

		ImportManager.importModel(MODEL_PROJECT, importWizard);*/
		Properties props = new Properties();
		props.setProperty("source", source);
		props.setProperty("target", target);
		props.setProperty("importType", ImportType.RELATIONAL_TABLE);
		
		new ImportMetadataManager().importFromDesignerTextFile(MODEL_PROJECT, props);

		teiidBot.checkResource(MODEL_PROJECT, target);
		teiidBot.checkDiagram(MODEL_PROJECT, target, "USER");
		teiidBot.checkDiagram(MODEL_PROJECT, target, "ADDRESS");
	}

	@Test
	public void metadataVirtualTableImportTest() {
		String source = teiidBot.toAbsolutePath("resources/dtf/relationalVirtualTable.csv");
		String target = "RelationalVirtualTable.xmi";

		/*MetadataImportWizard importWizard = new MetadataImportWizard();
		importWizard.setImportType(ImportType.RELATIONAL_VIRTUAL_TABLE);
		importWizard.setSource(source);
		importWizard.setTarget(target);

		ImportManager.importModel(MODEL_PROJECT, importWizard);*/
		
		Properties props = new Properties();
		props.setProperty("source", source);
		props.setProperty("target", target);
		props.setProperty("importType", ImportType.RELATIONAL_VIRTUAL_TABLE);
		
		new ImportMetadataManager().importFromDesignerTextFile(MODEL_PROJECT, props);

		teiidBot.checkResource(MODEL_PROJECT, target);
		teiidBot.checkDiagram(MODEL_PROJECT, target, "VTable1");
		teiidBot.checkDiagram(MODEL_PROJECT, target, "VTable2");
		teiidBot.checkDiagram(MODEL_PROJECT, target, "VTable3");
		teiidBot.checkDiagram(MODEL_PROJECT, target, "VTable4");
		teiidBot.checkDiagram(MODEL_PROJECT, target, "VTable5");
		teiidBot.checkDiagram(MODEL_PROJECT, target, "VTable6");
		teiidBot.checkDiagram(MODEL_PROJECT, target, "VTable7");
		teiidBot.checkDiagram(MODEL_PROJECT, target, "VTable8");
		teiidBot.checkDiagram(MODEL_PROJECT, target, "VTable9");
		teiidBot.checkDiagram(MODEL_PROJECT, target, "VTable10");
		teiidBot.checkDiagram(MODEL_PROJECT, target, "VProc1");
		teiidBot.checkDiagram(MODEL_PROJECT, target, "NewProcedureResult");
	}
	
	@Test
	public void salesforceImportTest(){
		String cpProps = teiidBot.toAbsolutePath("resources/db/salesforce.properties");
		String importProps = teiidBot.toAbsolutePath("resources/importWizard/sf.properties");
		String cpName =  "SF profile";
		new ConnectionProfileManager().createCPSalesForce(cpName, cpProps);
		new ImportManager().importFromSalesForce(MODEL_PROJECT, "SFModel", cpName, teiidBot.getProperties(importProps));
	}
	
	@Test
	public void sqlserverImportTest(){
		String cpProps = teiidBot.toAbsolutePath("resources/db/sqlserver_books.properties");
		String importProps = teiidBot.toAbsolutePath("resources/importWizard/sqlserver.properties");
		String cpName = "Books sql server";
		new ConnectionProfileManager().createCPWithDriverDefinition(cpName, cpProps);
		new ImportManager().importFromDatabase(MODEL_PROJECT, "SQLModel", cpName, teiidBot.getProperties(importProps));
	}
	
	@Test
	public void hsqlImportTest(){
		String cpProps = teiidBot.toAbsolutePath("resources/db/ds1.properties");
		String importProps = teiidBot.toAbsolutePath("resources/importWizard/hsql-ds1.properties");
		String cpName = "HSQLDB cp";
		new ConnectionProfileManager().createCPWithDriverDefinition(cpName, cpProps);
		new ImportManager().importFromDatabase(MODEL_PROJECT, "HSQLModel", cpName, teiidBot.getProperties(importProps));
	}

	/*private static void ImportManager.importModel(MODEL_PROJECT, TeiidImportWizard importWizard) {//import mgr
		ModelProject modelProject = teiidBot.modelExplorer().getModelProject(MODEL_PROJECT);
		modelProject.ImportManager.importModel(MODEL_PROJECT, importWizard);
	}*/

	/*private static void teiidBot.checkResource(MODEL_PROJECT, String... path) {//-> teiidbot
		new SWTWorkbenchBot().sleep(500);
		ModelProject modelproject = new ModelExplorer().getModelProject(MODEL_PROJECT);
		assertTrue(Arrays.toString(path) + " not created!", modelproject.containsItem(path));
	}*/

	/*private static void teiidBot.checkDiagram(MODEL_PROJECT, String file, String label) {//-> teiidbot
		new SWTWorkbenchBot().sleep(500);
		Project project = new ProjectExplorer().getProject(MODEL_PROJECT);
		project.getProjectItem(file).open();
		ModelEditor modelEditor = teiidBot.modelEditor(file);
		assertNotNull(file + " is not opened!", modelEditor);
		assertNotNull("Diagram '" + label + "' not found!", modelEditor.getModeDiagram(label));
	}*/

	/*private static void createHSQLDBProfile(String name) {//remove
		Properties props = new Properties();
		try {
			props.load(new FileReader("resources/db/hsqldb.properties"));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return;
		} catch (IOException e) {
			e.printStackTrace();
			return;
		}
		new HSQLDBDriverWizard(props.getProperty("db.jdbc_path")).create();
		new HSQLDBProfileWizard("HSQLDB Driver", props.getProperty("db.name"),
				props.getProperty("db.hostname")).setUser(props.getProperty("db.username"))
				.setPassword(props.getProperty("db.password")).setName(name).create();
	}*/
}
