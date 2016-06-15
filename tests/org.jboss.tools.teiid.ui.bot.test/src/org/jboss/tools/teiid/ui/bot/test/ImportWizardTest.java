package org.jboss.tools.teiid.ui.bot.test;

import java.util.Properties;

import org.jboss.reddeer.core.handler.ShellHandler;
import org.jboss.reddeer.junit.execution.annotation.RunIf;
import org.jboss.reddeer.junit.runner.RedDeerSuite;
import org.jboss.reddeer.requirements.openperspective.OpenPerspectiveRequirement.OpenPerspective;
import org.jboss.reddeer.requirements.server.ServerReqState;
import org.jboss.reddeer.workbench.impl.shell.WorkbenchShell;
import org.jboss.tools.common.reddeer.condition.IssueIsClosed;
import org.jboss.tools.common.reddeer.condition.IssueIsClosed.Jira;
import org.jboss.tools.teiid.reddeer.manager.ConnectionProfileManager;
import org.jboss.tools.teiid.reddeer.manager.ImportManager;
import org.jboss.tools.teiid.reddeer.manager.ImportMetadataManager;
import org.jboss.tools.teiid.reddeer.manager.ModelExplorerManager;
import org.jboss.tools.teiid.reddeer.perspective.TeiidPerspective;
import org.jboss.tools.teiid.reddeer.requirement.TeiidServerRequirement.TeiidServer;
import org.jboss.tools.teiid.reddeer.wizard.DDLCustomImportWizard;
import org.jboss.tools.teiid.reddeer.wizard.FlatImportWizard;
import org.jboss.tools.teiid.reddeer.wizard.ImportGeneralItemWizard;
import org.jboss.tools.teiid.reddeer.wizard.MetadataImportWizard.ImportType;
import org.jboss.tools.teiid.reddeer.wizard.WsdlImportWizard;
import org.jboss.tools.teiid.reddeer.wizard.WsdlWebImportWizard;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * Tests for importing relational models from various sources
 * 
 * @author apodhrad
 */
@RunWith(RedDeerSuite.class)
@OpenPerspective(TeiidPerspective.class)
@TeiidServer(state = ServerReqState.PRESENT)
public class ImportWizardTest {

	public static final String MODEL_PROJECT = "importTest";

	private static TeiidBot teiidBot = new TeiidBot();

	@BeforeClass
	public static void createModelProject() {
		new ModelExplorerManager().createProject(MODEL_PROJECT);
	}

	@AfterClass
	public static void closeAllShells() {

		ShellHandler.getInstance().closeAllNonWorbenchShells();
	}

	@Before
	public void setFocus() {
		new WorkbenchShell();
	}

	@Test
	public void ddlImportTest() {

		String ddl = teiidBot.toAbsolutePath("resources/ddl/hsqldb.ddl");
		
		DDLCustomImportWizard wizard = new DDLCustomImportWizard();
		wizard.open();
		wizard.setPath(ddl)
			  .setFolder(MODEL_PROJECT)
			  .setName("CustomerHsqldb")
			  .autoSelect(true)
			  .setModelType(DDLCustomImportWizard.View_Type)
			  .next();
		wizard.finish();
		
		teiidBot.assertResource(MODEL_PROJECT, "CustomerHsqldb.xmi");
		teiidBot.checkDiagram(MODEL_PROJECT, "CustomerHsqldb.xmi", "USER");
		teiidBot.checkDiagram(MODEL_PROJECT, "CustomerHsqldb.xmi", "ADDRESS");
	}

	@Test
	public void dtfImportTest() {

		String source = teiidBot.toAbsolutePath("resources/dtf/relationalModel.xml");
		String target = "RelationalModel.xmi";

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
		importWizard.selectFile("items.csv");
		importWizard.setSourceModel("Item");
		importWizard.setProject(MODEL_PROJECT);
		importWizard.next();
		importWizard.next();
		importWizard.next();
		importWizard.finish();
		new WorkbenchShell();

		teiidBot.assertResource(MODEL_PROJECT, "Item.xmi");
		teiidBot.assertResource(MODEL_PROJECT, "ViewModel.xmi");
		teiidBot.checkDiagram(MODEL_PROJECT, "Item.xmi", "getTextFiles");
		teiidBot.checkDiagram(MODEL_PROJECT, "Item.xmi", "Result");
		teiidBot.checkDiagram(MODEL_PROJECT, "ViewModel.xmi", "new_table");
	}

	@Test
	public void xmlImportTest() {

		String xmlProfile = "XML Local Profile";
		new ConnectionProfileManager().createCPXml(xmlProfile, "resources/flat/accounts.xml");

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

		Properties wsdlCP = new Properties();
		wsdlCP.setProperty("wsdl", "resources/wsdl/Hello.wsdl");
		wsdlCP.setProperty("endPoint", "HelloPort");

		new ConnectionProfileManager().createCPWSDL(profile, wsdlCP);

		WsdlImportWizard wsdlWizard = new WsdlImportWizard();

		wsdlWizard.setProfile(profile);

		wsdlWizard.setSourceModelName("HelloService.xmi");
		wsdlWizard.setViewModelName("HelloServiceView.xmi");

		wsdlWizard.addOperation("sayHello");
		wsdlWizard.addRequestElement("sayHello/sequence/arg0");
		wsdlWizard.addResponseElement("sayHelloResponse/sequence/return");

		wsdlWizard.execute();

		teiidBot.assertResource(MODEL_PROJECT, "HelloService.xmi");
		teiidBot.assertResource(MODEL_PROJECT, "HelloServiceView.xmi");
		teiidBot.checkDiagram(MODEL_PROJECT, "HelloService.xmi", "invoke");
		teiidBot.checkDiagram(MODEL_PROJECT, "HelloServiceView.xmi", "sayHello");
		teiidBot.checkDiagram(MODEL_PROJECT, "HelloServiceView.xmi", "sayHello_request");
		teiidBot.checkDiagram(MODEL_PROJECT, "HelloServiceView.xmi", "sayHello_response");
	}

	@Test
	@Jira("TEIIDDES-2855")
	@RunIf(conditionClass = IssueIsClosed.class)
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
		new ImportMetadataManager().importFromWSDLToWebService(iProps, WsdlWebImportWizard.IMPORT_WSDL_FROM_WORKSPACE); // generates
																														// Hello.xsd
																														// WsdlToWS.xmi
																														// -
																														// Hello
		new WorkbenchShell();

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
}
