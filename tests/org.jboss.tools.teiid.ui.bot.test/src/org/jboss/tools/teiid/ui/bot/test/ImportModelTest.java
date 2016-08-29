package org.jboss.tools.teiid.ui.bot.test;

import static org.junit.Assert.assertTrue;

import java.io.File;

import org.jboss.reddeer.eclipse.core.resources.Project;
import org.jboss.reddeer.junit.execution.annotation.RunIf;
import org.jboss.reddeer.junit.runner.RedDeerSuite;
import org.jboss.reddeer.requirements.openperspective.OpenPerspectiveRequirement.OpenPerspective;
import org.jboss.reddeer.workbench.impl.shell.WorkbenchShell;
import org.jboss.tools.common.reddeer.condition.IssueIsClosed;
import org.jboss.tools.common.reddeer.condition.IssueIsClosed.Jira;
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
		
		assertTrue(modelExplorer.getProject(PROJECT_NAME).containsItem("CustomerHsqldb.xmi"));
		assertTrue(modelExplorer.getProject(PROJECT_NAME).containsItem("CustomerHsqldb.xmi", "USER"));
		assertTrue(modelExplorer.getProject(PROJECT_NAME).containsItem("CustomerHsqldb.xmi", "ADDRESS"));
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
				.setProject(PROJECT_NAME)
				.finish();
		
		Project project = modelExplorer.getProject(PROJECT_NAME);
		assertTrue(project.containsItem(target));
		assertTrue(project.containsItem(target, "ProductSymbols"));
		assertTrue(project.containsItem(target, "ProductData"));
		assertTrue(project.containsItem(target, "getProductInfo"));
		assertTrue(project.containsItem(target, "ProductIDIndex"));
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

		Project project = modelExplorer.getProject(PROJECT_NAME);
		assertTrue(project.containsItem("Item.xmi"));
		assertTrue(project.containsItem("Item.xmi", "getTextFiles"));
		assertTrue(project.containsItem("Item.xmi",  "getTextFiles", "Result"));
		assertTrue(project.containsItem("ViewModel.xmi"));
		assertTrue(project.containsItem("ViewModel.xmi", "new_table"));
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
		
		Project project = modelExplorer.getProject(PROJECT_NAME);
		assertTrue(project.containsItem("AccountSource.xmi"));
		assertTrue(project.containsItem("AccountSource.xmi", "getTextFiles"));
		assertTrue(project.containsItem("AccountSource.xmi", "getTextFiles", "Result"));
		assertTrue(project.containsItem("AccountView.xmi"));
		assertTrue(project.containsItem("AccountView.xmi", "AccountTable"));
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
		
		assertTrue(modelExplorer.getProject(PROJECT_NAME).containsItem("EmployeesSchema.xsd"));
		assertTrue(modelExplorer.getProject(PROJECT_NAME).containsItem("BookDatatypes.xsd"));
	
		XMLSchemaImportWizard.openWizard()
				.selectRemoteImportMode()
				.nextPage()
				.setSchemaURL("http://www.jboss.org/schema/jbosscommon/jboss-common_6_0.xsd", null, null, false)
				.addDependentSchemas(false)
				.finish();
		
		assertTrue(modelExplorer.getProject(PROJECT_NAME).containsItem("jboss-common_6_0.xsd"));
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
		
		Project project = modelExplorer.getProject(PROJECT_NAME);
		assertTrue(project.containsItem("HelloService.xmi"));
		assertTrue(project.containsItem("HelloService.xmi", "invoke"));
		assertTrue(project.containsItem("HelloServiceView.xmi"));
		assertTrue(project.containsItem("HelloServiceView.xmi", "sayHello"));
		assertTrue(project.containsItem("HelloServiceView.xmi", "sayHello_request"));
		assertTrue(project.containsItem("HelloServiceView.xmi", "sayHello_response"));
	}

	@Test
	@Jira("TEIIDDES-2855")
	@RunIf(conditionClass = IssueIsClosed.class)
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

		assertTrue(modelExplorer.getProject(PROJECT_NAME).containsItem("WsdlToWS.xmi"));
		assertTrue(modelExplorer.getProject(PROJECT_NAME).containsItem("WsdlToWS2Responses.xmi"));
	}
}
