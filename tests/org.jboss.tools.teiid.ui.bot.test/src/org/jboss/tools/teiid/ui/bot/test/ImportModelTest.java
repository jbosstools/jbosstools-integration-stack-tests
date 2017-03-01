package org.jboss.tools.teiid.ui.bot.test;

import static org.junit.Assert.assertTrue;

import java.io.File;

import org.jboss.reddeer.junit.runner.RedDeerSuite;
import org.jboss.reddeer.requirements.openperspective.OpenPerspectiveRequirement.OpenPerspective;
import org.jboss.reddeer.workbench.impl.shell.WorkbenchShell;
import org.jboss.tools.common.reddeer.JiraClient;
import org.jboss.tools.teiid.reddeer.perspective.TeiidPerspective;
import org.jboss.tools.teiid.reddeer.view.ModelExplorer;
import org.jboss.tools.teiid.reddeer.wizard.connectionProfiles.noDatabase.FlatLocalConnectionProfileWizard;
import org.jboss.tools.teiid.reddeer.wizard.connectionProfiles.noDatabase.WsdlConnectionProfileWizard;
import org.jboss.tools.teiid.reddeer.wizard.connectionProfiles.noDatabase.XmlLocalConnectionProfileWizard;
import org.jboss.tools.teiid.reddeer.wizard.imports.DDLCustomImportWizard;
import org.jboss.tools.teiid.reddeer.wizard.imports.FlatImportWizard;
import org.jboss.tools.teiid.reddeer.wizard.imports.ImportFromFileSystemWizard;
import org.jboss.tools.teiid.reddeer.wizard.imports.MetadataImportWizard;
import org.jboss.tools.teiid.reddeer.wizard.imports.WsdlImportWizard;
import org.jboss.tools.teiid.reddeer.wizard.imports.WsdlWebImportWizard;
import org.jboss.tools.teiid.reddeer.wizard.imports.XMLImportWizard;
import org.jboss.tools.teiid.reddeer.wizard.imports.XMLSchemaImportWizard;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * @author apodhrad, skaleta
 * tested features: 
 * - import relational models from various sources using import wizards
 */
@RunWith(RedDeerSuite.class)
@OpenPerspective(TeiidPerspective.class)
public class ImportModelTest {
	public static final String PROJECT_NAME = "ImportModelProject";
	
	private ModelExplorer modelExplorer;
	
	@BeforeClass
	public static void before(){
		new WorkbenchShell().maximize();
	}
	
	@Before
	public void createProject() {
		modelExplorer = new ModelExplorer();
		modelExplorer.createProject(PROJECT_NAME);
	}

	@After
	public void cleanUp(){
		modelExplorer.deleteAllProjectsSafely();
	}

	@Test
	public void ddlImportTest() {
		String ddl = new File("resources/ddl/hsqldb.ddl").getAbsolutePath();
		
		DDLCustomImportWizard.openWizard()
				.setPath(ddl)
				.setFolder(PROJECT_NAME)
				.setName("CustomerHsqldb")
				.autoSelect(true)
				.setModelType(DDLCustomImportWizard.View_Type)
				.nextPage()
				.finish();
		
		assertTrue(modelExplorer.containsItem(PROJECT_NAME,"CustomerHsqldb.xmi"));
		assertTrue(modelExplorer.containsItem(PROJECT_NAME,"CustomerHsqldb.xmi", "USER"));
		assertTrue(modelExplorer.containsItem(PROJECT_NAME,"CustomerHsqldb.xmi", "ADDRESS"));
	}

	@Test
	public void dtfImportTest() {
		String source = new File("resources/dtf/relationalModel.xml").getAbsolutePath();
		String target = "RelationalModel.xmi";

		MetadataImportWizard.openWizard()
				.setImportType(MetadataImportWizard.TYPE_RELATIONAL_MODEL)
				.nextPage()
				.setPathToFile(source)
				.setProject(PROJECT_NAME, target)
				.finish();
		
		assertTrue(modelExplorer.containsItem(PROJECT_NAME,target));
		assertTrue(modelExplorer.containsItem(PROJECT_NAME,target, "ProductSymbols"));
		assertTrue(modelExplorer.containsItem(PROJECT_NAME,target, "ProductData"));
		assertTrue(modelExplorer.containsItem(PROJECT_NAME,target, "getProductInfo"));
		assertTrue(modelExplorer.containsItem(PROJECT_NAME,target, "ProductIDIndex"));
	}

	@Test
	public void flatImportTest() {
		String flatProfile = "Flat Profile";
		FlatLocalConnectionProfileWizard.openWizard(flatProfile)
				.setFile("resources/flat")
				.testConnection()
				.finish();

		FlatImportWizard.openWizard()
				.selectLocalFileImportMode()
				.nextPage()
				.selectProfile(flatProfile)
				.selectFile("items.csv")
				.setSourceModel("Item")
				.setProject(PROJECT_NAME)
				.nextPage()
				.nextPage()
				.nextPage()
				.nextPage()
				.finish();

		assertTrue(modelExplorer.containsItem(PROJECT_NAME,"Item.xmi"));
		assertTrue(modelExplorer.containsItem(PROJECT_NAME,"Item.xmi", "getTextFiles"));
		assertTrue(modelExplorer.containsItem(PROJECT_NAME,"Item.xmi",  "getTextFiles", "Result"));
		assertTrue(modelExplorer.containsItem(PROJECT_NAME,"ViewModel.xmi"));
		assertTrue(modelExplorer.containsItem(PROJECT_NAME,"ViewModel.xmi", "new_table"));
	}

	@Test
	public void xmlImportTest() {
		String xmlProfile = "XML Local Profile";

		XmlLocalConnectionProfileWizard.openWizard(xmlProfile)
				.setFile("resources/flat/accounts.xml")
				.finish();
		
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
		
		assertTrue(modelExplorer.containsItem(PROJECT_NAME,"AccountSource.xmi"));
		assertTrue(modelExplorer.containsItem(PROJECT_NAME,"AccountSource.xmi", "getTextFiles"));
		assertTrue(modelExplorer.containsItem(PROJECT_NAME,"AccountSource.xmi", "getTextFiles", "Result"));
		assertTrue(modelExplorer.containsItem(PROJECT_NAME,"AccountView.xmi"));
		assertTrue(modelExplorer.containsItem(PROJECT_NAME,"AccountView.xmi", "AccountTable"));
	}
	
	@Test
	public void xmlSchemaImportTest() {		
		XMLSchemaImportWizard.openWizard()
				.selectLocalImportMode()
				.nextPage()
				.setFromDirectory(new File("resources/xsd").getAbsolutePath())
				.setToDirectory(PROJECT_NAME)
				.selectSchema("EmployeesSchema.xsd", "BookDatatypes.xsd")
				.finish();
		
		assertTrue(modelExplorer.containsItem(PROJECT_NAME,"EmployeesSchema.xsd"));
		assertTrue(modelExplorer.containsItem(PROJECT_NAME,"BookDatatypes.xsd"));
	
		XMLSchemaImportWizard.openWizard()
				.selectRemoteImportMode()
				.nextPage()
				.setSchemaURL("http://www.jboss.org/schema/jbosscommon/jboss-common_6_0.xsd", null, null, false)
				.addDependentSchemas(false)
				.finish();
		
		assertTrue(modelExplorer.containsItem(PROJECT_NAME,"jboss-common_6_0.xsd"));
	}

	@Test
	public void wsdlToSoapImportTest() {
		String profile = "Hello Service";

		WsdlConnectionProfileWizard.openWizard(profile)
				.setWsdl("resources/wsdl/Hello.wsdl")
				.testConnection()
				.nextPage()
				.setEndPoint("HelloPort")
				.finish();

		if (!new JiraClient().isIssueClosed("TEIIDDES-2912")){
			modelExplorer.selectItem(PROJECT_NAME);
		}
		WsdlImportWizard.openWizard()
				.setConnectionProfile(profile)
				.selectOperations("sayHello")
				.nextPage()
				.setProject(PROJECT_NAME)
 				.setSourceModelName("HelloService")
				.setViewModelName("HelloServiceView")
				.nextPage()
				.setJndiName("HelloService")
				.nextPage()
				.nextPage()
				.addRequestElement("sayHello/sequence/arg0")
				.addResponseElement("sayHello","sayHelloResponse/sequence/return")
				.finish();
		
		assertTrue(modelExplorer.containsItem(PROJECT_NAME,"HelloService.xmi"));
		assertTrue(modelExplorer.containsItem(PROJECT_NAME,"HelloService.xmi", "invoke"));
		assertTrue(modelExplorer.containsItem(PROJECT_NAME,"HelloServiceView.xmi"));
		assertTrue(modelExplorer.containsItem(PROJECT_NAME,"HelloServiceView.xmi", "sayHello"));
		assertTrue(modelExplorer.containsItem(PROJECT_NAME,"HelloServiceView.xmi", "sayHello_request"));
		assertTrue(modelExplorer.containsItem(PROJECT_NAME,"HelloServiceView.xmi", "sayHello_response"));
	}

	@Test
	public void wsdlToWsImportTest() {
		// import wsdl
		ImportFromFileSystemWizard.openWizard()
				.setPath("resources/wsdl")
				.setFolder(PROJECT_NAME)
				.selectFile("Hello.wsdl")
				.finish();
		// import from workspace
		WsdlWebImportWizard.openWizard()
				.setModelName("WsdlToWS")
				.setProject(PROJECT_NAME)
				.importFromWorkspace(PROJECT_NAME,"Hello.wsdl")
				.nextPage()
				.nextPage()
				.nextPage()
				.nextPage()
				.finish();		
		new WorkbenchShell();
		// import from url
		WsdlWebImportWizard.openWizard()
				.setModelName("WsdlToWS2")
				.setProject(PROJECT_NAME)
				.importFromURL("http://www.webservicex.com/globalweather.asmx?WSDL", null, null, false)
				.nextPage()
				.nextPage()
				.nextPage()
				.nextPage()
				.finish();

		assertTrue(modelExplorer.containsItem(PROJECT_NAME,"WsdlToWS.xmi"));
		assertTrue(modelExplorer.containsItem(PROJECT_NAME,"WsdlToWS2Responses.xmi"));
	}
}
