package org.jboss.tools.teiid.ui.bot.test;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.util.Properties;

import org.jboss.reddeer.core.handler.ShellHandler;
import org.jboss.reddeer.junit.execution.annotation.RunIf;
import org.jboss.reddeer.junit.runner.RedDeerSuite;
import org.jboss.reddeer.requirements.openperspective.OpenPerspectiveRequirement.OpenPerspective;
import org.jboss.reddeer.requirements.server.ServerReqState;
import org.jboss.reddeer.workbench.impl.shell.WorkbenchShell;
import org.jboss.tools.common.reddeer.condition.IssueIsClosed;
import org.jboss.tools.common.reddeer.condition.IssueIsClosed.Jira;
import org.jboss.tools.teiid.reddeer.connection.ConnectionProfileHelper;
import org.jboss.tools.teiid.reddeer.editor.ModelEditor;
import org.jboss.tools.teiid.reddeer.perspective.TeiidPerspective;
import org.jboss.tools.teiid.reddeer.requirement.TeiidServerRequirement.TeiidServer;
import org.jboss.tools.teiid.reddeer.view.ModelExplorer;
import org.jboss.tools.teiid.reddeer.wizard.imports.DDLCustomImportWizard;
import org.jboss.tools.teiid.reddeer.wizard.imports.FlatImportWizard;
import org.jboss.tools.teiid.reddeer.wizard.imports.ImportFromFileSystemWizard;
import org.jboss.tools.teiid.reddeer.wizard.imports.MetadataImportWizard;
import org.jboss.tools.teiid.reddeer.wizard.imports.WsdlImportWizard;
import org.jboss.tools.teiid.reddeer.wizard.imports.WsdlWebImportWizard;
import org.jboss.tools.teiid.reddeer.wizard.imports.XMLImportWizard;
import org.jboss.tools.teiid.reddeer.wizard.imports.XMLSchemaImportWizard;
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

	@BeforeClass
	public static void createModelProject() {
		new ModelExplorer().createProject(MODEL_PROJECT);
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

		String ddl = new File("resources/ddl/hsqldb.ddl").getAbsolutePath();
		
		DDLCustomImportWizard.openWizard()
				.setPath(ddl)
				.setFolder(MODEL_PROJECT)
				.setName("CustomerHsqldb")
				.autoSelect(true)
				.setModelType(DDLCustomImportWizard.View_Type)
				.nextPage()
				.finish();
		
		assertTrue(new ModelExplorer().getProject(MODEL_PROJECT).containsItem("CustomerHsqldb.xmi"));
		assertTrue(new ModelExplorer().getProject(MODEL_PROJECT).containsItem("CustomerHsqldb.xmi", "USER"));
		assertTrue(new ModelExplorer().getProject(MODEL_PROJECT).containsItem("CustomerHsqldb.xmi", "ADDRESS"));
	}

	@Test
	public void dtfImportTest() {

		String source = new File("resources/dtf/relationalModel.xml").getAbsolutePath();
		String target = "RelationalModel.xmi";

		MetadataImportWizard.openWizard()
				.setImportType(MetadataImportWizard.TYPE_RELATIONAL_MODEL)
				.nextPage()
				.setName(target)
				.setPathToFile(source)
				.setProject(MODEL_PROJECT)
				.finish();
		
		assertTrue(new ModelExplorer().getProject(MODEL_PROJECT).containsItem(target));
		new ModelExplorer().openModelEditor(MODEL_PROJECT, target);
		ModelEditor modelEditor = new ModelEditor(target);
		assertNotNull(modelEditor.getModeDiagram("ProductSymbols"));
		assertNotNull(modelEditor.getModeDiagram("ProductData"));
		assertNotNull(modelEditor.getModeDiagram("getProductInfo"));
		assertNotNull(modelEditor.getModeDiagram("ProductIDIndex"));
	}

	@Test
	public void flatImportTest() {

		String flatProfile = "Flat Profile";
		new ConnectionProfileHelper().createCpFlatFile(flatProfile, "resources/flat");

		FlatImportWizard.openWizard()
				.selectLocalFileImportMode()
				.nextPage()
				.selectProfile(flatProfile)
				.selectFile("items.csv")
				.setSourceModel("Item")
				.setProject(MODEL_PROJECT)
				.nextPage()
				.nextPage()
				.nextPage()
				.nextPage()
				.finish();
		new WorkbenchShell();

		assertTrue(new ModelExplorer().getProject(MODEL_PROJECT).containsItem("Item.xmi"));
		assertTrue(new ModelExplorer().getProject(MODEL_PROJECT).containsItem("ViewModel.xmi"));
		new ModelExplorer().openModelEditor(MODEL_PROJECT, "Item.xmi");
		ModelEditor modelEditor = new ModelEditor("Item.xmi");
		assertNotNull(modelEditor.getModeDiagram("getTextFiles"));
		assertNotNull(modelEditor.getModeDiagram("Result"));
		assertNotNull(modelEditor.getModeDiagram("getProductInfo"));
		new ModelExplorer().openModelEditor(MODEL_PROJECT, "ViewModel.xmi");
		modelEditor = new ModelEditor("ViewModel.xmi");
		assertNotNull(modelEditor.getModeDiagram("new_table"));
	}

	@Test
	public void xmlImportTest() {

		String xmlProfile = "XML Local Profile";
		new ConnectionProfileHelper().createCpXml(xmlProfile, "resources/flat/accounts.xml");
		
		XMLImportWizard.openWizard()
				.setImportMode(XMLImportWizard.LOCAL)
				.nextPage()
				.setDataFileSource(xmlProfile)
				.setSourceModelName("AccountSource")
				.nextPage()
				.setJndiName("AccountSource")
				.nextPage()
				.setRootPath("/accounts/account")
				.addElement("accounts/account/nick")
				.addElement("accounts/account/balance")
				.nextPage()
				.setViewModelName("AccountView")
				.setViewTableName("AccountTable")
				.finish();
		
		assertTrue(new ModelExplorer().getProject(MODEL_PROJECT).containsItem("AccountSource.xmi"));
		assertTrue(new ModelExplorer().getProject(MODEL_PROJECT).containsItem("AccountView.xmi"));
		new ModelExplorer().openModelEditor(MODEL_PROJECT, "AccountSource.xmi");
		ModelEditor modelEditor = new ModelEditor("AccountSource.xmi");
		assertNotNull(modelEditor.getModeDiagram("getTextFiles"));
		assertNotNull(modelEditor.getModeDiagram("Result"));
		new ModelExplorer().openModelEditor(MODEL_PROJECT, "AccountView.xmi");
		modelEditor = new ModelEditor("AccountView.xmi");
		assertNotNull(modelEditor.getModeDiagram("AccountTable"));
	}

	@Test
	public void wsdlToSOAPImportTest() {

		String profile = "Hello Service";

		Properties wsdlCP = new Properties();
		wsdlCP.setProperty("wsdl", "resources/wsdl/Hello.wsdl");
		wsdlCP.setProperty("endPoint", "HelloPort");

		new ConnectionProfileHelper().createCpWsdl(profile, wsdlCP);

		WsdlImportWizard.openWizard()
				.setConnectionProfile(profile)
				.selectOperations("sayHello")
				.nextPage()
				.setProject(MODEL_PROJECT)
				.setSourceModelName("HelloService")
				.setViewModelName("HelloServiceView")
				.nextPage()
				.setJndiName("HelloService")
				.nextPage()
				.nextPage()
				.addRequestElement("sayHello/sequence/arg0")
				.addResponseElement("sayHello","sayHelloResponse/sequence/return")
				.finish();
		
		assertTrue(new ModelExplorer().getProject(MODEL_PROJECT).containsItem("HelloService.xmi"));
		assertTrue(new ModelExplorer().getProject(MODEL_PROJECT).containsItem("HelloServiceView.xmi"));
		new ModelExplorer().openModelEditor(MODEL_PROJECT, "HelloService.xmi");
		ModelEditor modelEditor = new ModelEditor("HelloService.xmi");
		assertNotNull(modelEditor.getModeDiagram("invoke"));
		new ModelExplorer().openModelEditor(MODEL_PROJECT, "HelloServiceView.xmi");
		 modelEditor = new ModelEditor("HelloServiceView.xmi");
		assertNotNull(modelEditor.getModeDiagram("sayHello"));
		assertNotNull(modelEditor.getModeDiagram("sayHello_request"));
		assertNotNull(modelEditor.getModeDiagram("sayHello_response"));
	}

	@Test
	@Jira("TEIIDDES-2855")
	@RunIf(conditionClass = IssueIsClosed.class)
	public void wsdlToWSImportTest() {

		// import wsdl
		ImportFromFileSystemWizard.openWizard()
				.setPath("resources/wsdl")
				.setFolder(MODEL_PROJECT)
				.selectFile("Hello.wsdl")
				.finish();

		// import from workspace
		WsdlWebImportWizard.openWizard()
				.setModelName("WsdlToWS")
				.setProject(MODEL_PROJECT)
				.importFromWorkspace(MODEL_PROJECT,"Hello.wsdl")
				.nextPage()
				.nextPage()
				.nextPage()
				.nextPage()
				.finish();
		
		new WorkbenchShell();
		// import from url
		WsdlWebImportWizard.openWizard()
				.setModelName("WsdlToWS2")
				.setProject(MODEL_PROJECT)
				.importFromURL("http://www.webservicex.com/globalweather.asmx?WSDL", null, null, false)
				.nextPage()
				.nextPage()
				.nextPage()
				.nextPage()
				.finish();

		assertTrue(new ModelExplorer().getProject(MODEL_PROJECT).containsItem("WsdlToWS.xmi"));
		assertTrue(new ModelExplorer().getProject(MODEL_PROJECT).containsItem("WsdlToWS2Responses.xmi"));
	}

	@Test
	public void xmlSchemaImportTest() {
		// local xsd		
		XMLSchemaImportWizard.openWizard()
				.selectLocalImportMode()
				.nextPage()
				.setFromDirectory(new File("resources/xsd").getAbsolutePath())
				.setToDirectory(MODEL_PROJECT)
				.selectSchema("EmployeesSchema.xsd", "BookDatatypes.xsd")
				.finish();
		
		assertTrue(new ModelExplorer().getProject(MODEL_PROJECT).containsItem("EmployeesSchema.xsd"));
		assertTrue(new ModelExplorer().getProject(MODEL_PROJECT).containsItem("BookDatatypes.xsd"));
		// remote URI		
		XMLSchemaImportWizard.openWizard()
				.selectRemoteImportMode()
				.nextPage()
				.setSchemaURL("http://www.jboss.org/schema/jbosscommon/jboss-common_6_0.xsd", null, null, false)
				.addDependentSchemas(false)
				.finish();
		
		assertTrue(new ModelExplorer().getProject(MODEL_PROJECT).containsItem("jboss-common_6_0.xsd"));
	}
}
