package org.jboss.tools.teiid.ui.bot.test;

import java.util.Properties;

import org.eclipse.swtbot.eclipse.finder.SWTWorkbenchBot;
import org.jboss.reddeer.swt.impl.menu.ShellMenu;
import org.jboss.reddeer.swt.test.RedDeerTest;
import org.jboss.tools.teiid.reddeer.manager.ConnectionProfileManager;
import org.jboss.tools.teiid.reddeer.manager.ImportManager;
import org.jboss.tools.teiid.reddeer.manager.ImportMetadataManager;
import org.jboss.tools.teiid.reddeer.manager.ModelExplorerManager;
import org.jboss.tools.teiid.reddeer.wizard.FlatImportWizard;
import org.jboss.tools.teiid.reddeer.wizard.ImportGeneralItemWizard;
import org.jboss.tools.teiid.reddeer.wizard.MetadataImportWizard.ImportType;
import org.jboss.tools.teiid.reddeer.wizard.WsdlWebImportWizard;
import org.jboss.tools.teiid.ui.bot.test.requirement.PerspectiveRequirement.Perspective;
import org.jboss.tools.teiid.ui.bot.test.suite.TeiidSuite;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * Tests for importing relational models from various sources
 * 
 * @author apodhrad
 */
@Perspective(name = "Teiid Designer")
@RunWith(TeiidSuite.class)
public class ImportWizardTest extends RedDeerTest {

	public static final String MODEL_PROJECT = "importTest";

	private static TeiidBot teiidBot = new TeiidBot();

	@BeforeClass
	public static void createModelProject() {

		new ShellMenu("Project", "Build Automatically").select();
		new ModelExplorerManager().createProject(MODEL_PROJECT);
	}

	@AfterClass
	public static void closeAllShells() {

		new SWTWorkbenchBot().closeAllShells();
	}

	@After
	public void afterMethod() {

		System.out.println("TEST METHOD END");
	}

	@Test
	public void ddlImportTest() {

		String ddl = teiidBot.toAbsolutePath("resources/ddl/hsqldb.ddl");

		/*
		 * DDLImportWizard importWizard = new DDLImportWizard();
		 * importWizard.setDdlPath(ddl);
		 * importWizard.setModelName("CustomerHsqldb");
		 * importWizard.setAutoselectDialect(true);
		 * 
		 * ImportManager.importModel(MODEL_PROJECT, importWizard);
		 */

		Properties props = new Properties();
		props.setProperty("autoselectDialect", "true");
		new ImportMetadataManager().importFromDDL(MODEL_PROJECT,
				"CustomerHsqldb", ddl, props);

		teiidBot.assertResource(MODEL_PROJECT, "CustomerHsqldb.xmi");
		teiidBot.checkDiagram(MODEL_PROJECT, "CustomerHsqldb.xmi", "USER");
		teiidBot.checkDiagram(MODEL_PROJECT, "CustomerHsqldb.xmi", "ADDRESS");
	}

	@Test
	public void dtfImportTest() {

		String source = teiidBot.toAbsolutePath("resources/dtf/relationalModel.xml");
		String target = "RelationalModel.xmi";

		/*
		 * MetadataImportWizard importWizard = new MetadataImportWizard();
		 * importWizard.setImportType(ImportType.RELATIONAL_MODEL);
		 * importWizard.setSource(source); importWizard.setTarget(target);
		 * 
		 * ImportManager.importModel(MODEL_PROJECT, importWizard);
		 */

		Properties props = new Properties();
		props.setProperty("source", source);
		props.setProperty("target", target);
		props.setProperty("importType", ImportType.RELATIONAL_MODEL);

		new ImportMetadataManager().importFromDesignerTextFile(MODEL_PROJECT, props);

		teiidBot.assertResource(MODEL_PROJECT, target);
		teiidBot.checkDiagram(MODEL_PROJECT, target, "ProductSymbols");
		teiidBot.checkDiagram(MODEL_PROJECT, target, "ProductData");
		teiidBot.checkDiagram(MODEL_PROJECT, target, "getProductInfo");
		teiidBot.checkDiagram(MODEL_PROJECT, target, "ProductIDIndex");
	}

	@Test
	public void flatImportTest() {

		String flatProfile = "Flat Profile";
		new ConnectionProfileManager().createCPFlatFile(flatProfile, "resources/flat");

		FlatImportWizard importWizard = new FlatImportWizard();
		importWizard.open();
		importWizard.selectLocalFileImportMode();
		importWizard.next();
		importWizard.selectProfile(flatProfile);
		importWizard.selectFile("items.csv     <<<<");
		importWizard.setSourceModel("Item");
		importWizard.setProject(MODEL_PROJECT);
		importWizard.next();
		importWizard.next();
		importWizard.next();
		importWizard.finish();

		teiidBot.assertResource(MODEL_PROJECT, "ItemSource.xmi");
		teiidBot.assertResource(MODEL_PROJECT, "ItemView.xmi");
		teiidBot.checkDiagram(MODEL_PROJECT, "ItemSource.xmi", "getTextFiles");
		teiidBot.checkDiagram(MODEL_PROJECT, "ItemSource.xmi", "Result");
		teiidBot.checkDiagram(MODEL_PROJECT, "ItemView.xmi", "ItemTable");
	}

	@Test
	public void xmlImportTest() {

		String xmlProfile = "XML Local Profile";
		new ConnectionProfileManager().createCPXml(xmlProfile, "resources/flat/accounts.xml");

		/*
		 * XMLImportWizard importWizard = new XMLImportWizard();
		 * importWizard.setLocal(true); importWizard.setName("Account");
		 * importWizard.setProfileName(xmlProfile);
		 * importWizard.setRootPath("/accounts/account");
		 * importWizard.addElement("accounts/account/nick");
		 * importWizard.addElement("accounts/account/balance");
		 * 
		 * ImportManager.importModel(MODEL_PROJECT, importWizard);
		 */

		Properties props = new Properties();
		props.setProperty("local", "true");
		props.setProperty("rootPath", "/accounts/account");
		props.setProperty("elements", "accounts/account/nick,accounts/account/balance");

		new ImportMetadataManager().importFromXML(MODEL_PROJECT, "Account", xmlProfile, props);

		teiidBot.assertResource(MODEL_PROJECT, "AccountSource.xmi");
		teiidBot.assertResource(MODEL_PROJECT, "AccountView.xmi");
		teiidBot.checkDiagram(MODEL_PROJECT, "AccountSource.xmi", "getTextFiles");
		teiidBot.checkDiagram(MODEL_PROJECT, "AccountSource.xmi", "Result");
		teiidBot.checkDiagram(MODEL_PROJECT, "AccountView.xmi", "AccountTable");
	}

	@Test
	public void wsdlToSOAPImportTest() {

		String profile = "Hello Service";

		/*
		 * String wsdl = teiidBot.toAbsolutePath("resources/wsdl/Hello.wsdl");
		 * 
		 * // Create wsdl profile WsdlProfileWizard profileWizard = new
		 * WsdlProfileWizard(); profileWizard.setName(profile);
		 * profileWizard.setWsdl("file:" + wsdl);
		 * profileWizard.setEndPoint("HelloPort");//!!!PROBLEM - reddeer
		 * profileWizard.execute();
		 */

		Properties wsdlCP = new Properties();
		wsdlCP.setProperty("wsdl", "resources/wsdl/Hello.wsdl");
		wsdlCP.setProperty("endPoint", "HelloPort");

		new ConnectionProfileManager().createCPWSDL(profile, wsdlCP);

		// Import wsdl as model
		/*
		 * WsdlImportWizard importWizard = new WsdlImportWizard();
		 * importWizard.setProfile(profile);
		 * importWizard.addRequestElement("sayHello/sequence/arg0");
		 * importWizard.addResponseElement("sayHelloResponse/sequence/return");
		 * 
		 * ImportManager.importModel(MODEL_PROJECT, importWizard);
		 */

		Properties props = new Properties();
		props.setProperty("requestElements", "sayHello/sequence/arg0");
		props.setProperty("responseElements", "sayHelloResponse/sequence/return");
		new ImportMetadataManager().importFromWSDLToSrcView(MODEL_PROJECT, profile, props);

		teiidBot.assertResource(MODEL_PROJECT, "HelloService.xmi");
		teiidBot.assertResource(MODEL_PROJECT, "HelloServiceView.xmi");
		teiidBot.checkDiagram(MODEL_PROJECT, "HelloService.xmi", "invoke");
		teiidBot.checkDiagram(MODEL_PROJECT, "HelloServiceView.xmi", "sayHello");
		teiidBot.checkDiagram(MODEL_PROJECT, "HelloServiceView.xmi", "sayHello_request");
		teiidBot.checkDiagram(MODEL_PROJECT, "HelloServiceView.xmi", "sayHello_response");
	}

	@Test
	public void wsdlToWSImportTest() {
	
		// import wsdl
		Properties iProps = new Properties();
		iProps.setProperty("dirName", teiidBot.toAbsolutePath("resources/wsdl"));
		iProps.setProperty("file", "Hello.wsdl");
		iProps.setProperty("intoFolder", MODEL_PROJECT);
		new ImportManager().importGeneralItem(ImportGeneralItemWizard.Type.FILE_SYSTEM, iProps);

		// import from workspace
		iProps = new Properties();
		iProps.setProperty("modelName", "WsdlToWS");
		iProps.setProperty("project", MODEL_PROJECT);
		iProps.setProperty("wsdlName", "Hello.wsdl");
		new ImportMetadataManager().importFromWSDLToWebService(iProps,
				WsdlWebImportWizard.IMPORT_WSDL_FROM_WORKSPACE);// generates
																// Hello.xsd,
																// WsdlToWS.xmi
																// - Hello

		// import from URL
		iProps = new Properties();
		iProps.setProperty("modelName", "WsdlToWS2");
		iProps.setProperty("project", MODEL_PROJECT);
		iProps.setProperty("wsdlUrl", "http://www.webservicex.com/globalweather.asmx?WSDL");
		iProps.setProperty("securityType", "None");
		new ImportMetadataManager().importFromWSDLToWebService(iProps, WsdlWebImportWizard.IMPORT_WSDL_FROM_URL);

		teiidBot.assertResource(MODEL_PROJECT, "WsdlToWS.xmi");
		teiidBot.assertResource(MODEL_PROJECT, "WsdlToWS2Responses.xmi");
	}

	@Test
	// throws exception! remote && addDependentSchemas=true (JBDS exception)
	public void xmlSchemaImportTest() {

		// local xsd
		Properties iProps = new Properties();
		iProps.setProperty("local", "true");
		iProps.setProperty("rootPath", teiidBot.toAbsolutePath("resources/xsd"));
		iProps.setProperty("schemas", "EmployeesSchema.xsd,BookDatatypes.xsd");
		new ImportMetadataManager().importXMLSchema(MODEL_PROJECT, iProps);
		teiidBot.assertResource(MODEL_PROJECT, "EmployeesSchema.xsd");
		teiidBot.assertResource(MODEL_PROJECT, "BookDatatypes.xsd");

		// remote URI
		iProps = new Properties();
		iProps.setProperty("local", "false");
		iProps.setProperty("xmlSchemaURL", "http://www.jboss.org/schema/jbosscommon/jboss-common_6_0.xsd");
		iProps.setProperty("verifyHostname", "false");
		iProps.setProperty("addDependentSchemas", "false");
		new ImportMetadataManager().importXMLSchema(MODEL_PROJECT, iProps);
		teiidBot.assertResource(MODEL_PROJECT, "jboss-common_6_0.xsd");
	}

	// @Test
	// public void jdbcImportTest() {
	//
	// String jdbcProfile = "HSQLDB Profile";
	// String importProps = "resources/db/hsql-employees.properties";
	//
	// String empModel =
	// teiidBot.getProperties(importProps).getProperty("modelName");//"Emp.xmi";
	//
	// //createHSQLDBProfile(jdbcProfile);
	// new ConnectionProfileManager().createCPWithDriverDefinition(jdbcProfile,
	// "resources/db/hsqldb.properties");
	//
	// /*ImportJDBCDatabaseWizard wizard = new ImportJDBCDatabaseWizard();
	// wizard.setConnectionProfile(jdbcProfile);
	// wizard.setProjectName(MODEL_PROJECT);
	// wizard.setModelName(empModel);
	// wizard.addItem("PUBLIC/PUBLIC/TABLE/CUSTOMER");
	// wizard.addItem("PUBLIC/PUBLIC/TABLE/ORDER");
	// wizard.execute();*/
	//
	// new ImportManager().importFromDatabase(MODEL_PROJECT, empModel,
	// jdbcProfile, teiidBot.getProperties(importProps));
	//
	// teiidBot.checkResource(MODEL_PROJECT, empModel);
	// teiidBot.checkDiagram(MODEL_PROJECT, empModel, "ORDER");
	// teiidBot.checkDiagram(MODEL_PROJECT, empModel, "CUSTOMER");
	// }

	//
	// @Test
	// public void metadataTableImportTest() {
	// String source =
	// teiidBot.toAbsolutePath("resources/dtf/relationalTable.csv");
	// String target = "RelationalTable.xmi";
	//
	// /*MetadataImportWizard importWizard = new MetadataImportWizard();
	// importWizard.setImportType(ImportType.RELATIONAL_TABLE);
	// importWizard.setSource(source);
	// importWizard.setTarget(target);
	//
	// ImportManager.importModel(MODEL_PROJECT, importWizard);*/
	// Properties props = new Properties();
	// props.setProperty("source", source);
	// props.setProperty("target", target);
	// props.setProperty("importType", ImportType.RELATIONAL_TABLE);
	//
	// new ImportMetadataManager().importFromDesignerTextFile(MODEL_PROJECT,
	// props);
	//
	// teiidBot.checkResource(MODEL_PROJECT, target);
	// teiidBot.checkDiagram(MODEL_PROJECT, target, "USER");
	// teiidBot.checkDiagram(MODEL_PROJECT, target, "ADDRESS");
	// }
	//
	// @Test
	// public void metadataVirtualTableImportTest() {
	// String source =
	// teiidBot.toAbsolutePath("resources/dtf/relationalVirtualTable.csv");
	// String target = "RelationalVirtualTable.xmi";
	//
	// /*MetadataImportWizard importWizard = new MetadataImportWizard();
	// importWizard.setImportType(ImportType.RELATIONAL_VIRTUAL_TABLE);
	// importWizard.setSource(source);
	// importWizard.setTarget(target);
	//
	// ImportManager.importModel(MODEL_PROJECT, importWizard);*/
	//
	// Properties props = new Properties();
	// props.setProperty("source", source);
	// props.setProperty("target", target);
	// props.setProperty("importType", ImportType.RELATIONAL_VIRTUAL_TABLE);
	//
	// new ImportMetadataManager().importFromDesignerTextFile(MODEL_PROJECT,
	// props);
	//
	// teiidBot.checkResource(MODEL_PROJECT, target);
	// teiidBot.checkDiagram(MODEL_PROJECT, target, "VTable1");
	// teiidBot.checkDiagram(MODEL_PROJECT, target, "VTable2");
	// teiidBot.checkDiagram(MODEL_PROJECT, target, "VTable3");
	// teiidBot.checkDiagram(MODEL_PROJECT, target, "VTable4");
	// teiidBot.checkDiagram(MODEL_PROJECT, target, "VTable5");
	// teiidBot.checkDiagram(MODEL_PROJECT, target, "VTable6");
	// teiidBot.checkDiagram(MODEL_PROJECT, target, "VTable7");
	// teiidBot.checkDiagram(MODEL_PROJECT, target, "VTable8");
	// teiidBot.checkDiagram(MODEL_PROJECT, target, "VTable9");
	// teiidBot.checkDiagram(MODEL_PROJECT, target, "VTable10");
	// teiidBot.checkDiagram(MODEL_PROJECT, target, "VProc1");
	// teiidBot.checkDiagram(MODEL_PROJECT, target, "NewProcedureResult");
	// }

	// TODO wsdl >> ws model, xml schema
}
