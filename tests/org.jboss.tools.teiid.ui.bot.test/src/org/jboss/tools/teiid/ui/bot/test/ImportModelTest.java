package org.jboss.tools.teiid.ui.bot.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

import java.io.File;

import org.eclipse.reddeer.eclipse.ui.views.properties.PropertySheet;
import org.eclipse.reddeer.junit.runner.RedDeerSuite;
import org.eclipse.reddeer.requirements.openperspective.OpenPerspectiveRequirement.OpenPerspective;
import org.eclipse.reddeer.workbench.impl.shell.WorkbenchShell;
import org.hamcrest.core.StringContains;
import org.jboss.tools.common.reddeer.JiraClient;
import org.jboss.tools.teiid.reddeer.editor.RelationalModelEditor;
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
	public static final String VIRTUAL_TABLES_PROJECT = "VirtualTablesImport";
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
		
		assertTrue(modelExplorer.containsItem(PROJECT_NAME,target, "ProductSymbols", "ProductID : string(10)"));
		assertTrue(modelExplorer.containsItem(PROJECT_NAME,target, "ProductSymbols", "SymboldType : biginteger(10)"));
		assertTrue(modelExplorer.containsItem(PROJECT_NAME,target, "ProductSymbols", "FK_ProductID"));
		
		assertTrue(modelExplorer.containsItem(PROJECT_NAME,target, "getProductInfo", "ID : decimal(10)"));
		assertTrue(modelExplorer.containsItem(PROJECT_NAME,target, "getProductInfo", "productInfo : string(980)"));
		
		//assertTrue(modelExplorer.containsItem(PROJECT_NAME,target, "getProductInfo", "InfoResult")); should be imported???
		
		org.jboss.tools.teiid.reddeer.editor.TableEditor editor = new RelationalModelEditor(target).openTableEditor();	
		assertEquals("Basic stocks or bond", editor.getCellText(0, "Description"));		
		editor.openTab("Columns");
		assertEquals("Unique ID for this product", editor.getCellText(0, "Description"));
		assertEquals("Unique ID for this product", editor.getCellText(2, "Description"));		
		
		PropertySheet properties = new PropertySheet();
		
		modelExplorer.selectItem(PROJECT_NAME, target, "ProductData");		
		assertEquals("100", properties.getProperty("Misc","Cardinality").getPropertyValue()); 
		assertEquals("false", properties.getProperty("Misc","Materialized").getPropertyValue()); 
		assertEquals("true", properties.getProperty("Misc","Supports Update").getPropertyValue()); 
		assertEquals("false", properties.getProperty("Misc","System").getPropertyValue()); 
		assertEquals("dbo.products.ProductData", properties.getProperty("Misc","Name In Source").getPropertyValue());
		
		modelExplorer.selectItem(PROJECT_NAME, target, "ProductSymbols");
		assertEquals("100", properties.getProperty("Misc","Cardinality").getPropertyValue()); 
		assertEquals("true", properties.getProperty("Misc","Supports Update").getPropertyValue()); 
		assertEquals("false", properties.getProperty("Misc","System").getPropertyValue()); 
		assertEquals("dbo.products.ProductSymbols", properties.getProperty("Misc","Name In Source").getPropertyValue());
		
		modelExplorer.selectItem(PROJECT_NAME, target, "getProductInfo");
		assertEquals("true", properties.getProperty("Misc","Function").getPropertyValue()); 
		assertEquals("dbo.products.getProductInfo", properties.getProperty("Misc","Name In Source").getPropertyValue());
		
		modelExplorer.selectItem(PROJECT_NAME, target, "ProductIDIndex");
		assertEquals("false", properties.getProperty("Misc","Auto Update").getPropertyValue()); 
		assertEquals("ProductID : string(10)", properties.getProperty("Misc","Columns").getPropertyValue()); 
		
		modelExplorer.selectItem(PROJECT_NAME, target, "ProductData", "ProductID : string(10)");	
		assertThat(properties.getProperty("Type","Datatype").getPropertyValue(), new StringContains("string"));
		assertEquals("10", properties.getProperty("Type Detail","Length").getPropertyValue()); 
		assertEquals("NO_NULLS", properties.getProperty("Type","Nullable").getPropertyValue()); 
		
		modelExplorer.selectItem(PROJECT_NAME, target, "ProductData", "PK_ProductID");	
		assertEquals("dbo.products.ProductData.PK_ProductID", properties.getProperty("Misc","Name In Source").getPropertyValue()); 
		
		modelExplorer.selectItem(PROJECT_NAME, target, "ProductSymbols", "SymboldType : biginteger(10)");
		assertEquals("10", properties.getProperty("Type Detail","Length").getPropertyValue()); 
		assertEquals("10", properties.getProperty("Type Detail","Radix").getPropertyValue()); 
		assertEquals("10", properties.getProperty("Type Detail","Numeric Precision").getPropertyValue());
		assertEquals("NULLABLE", properties.getProperty("Type","Nullable").getPropertyValue()); 
		
		modelExplorer.selectItem(PROJECT_NAME, target, "getProductInfo", "ID : decimal(10)");
		assertEquals("IN", properties.getProperty("Type","Direction").getPropertyValue());
		assertEquals("NO_NULLS", properties.getProperty("Type","Nullable").getPropertyValue()); 
		assertThat(properties.getProperty("Type","Datatype").getPropertyValue(), new StringContains("decimal"));
		assertEquals("10", properties.getProperty("Type Detail","Length").getPropertyValue()); 
		assertEquals("10", properties.getProperty("Type Detail","Radix").getPropertyValue()); 
		assertEquals("10", properties.getProperty("Type Detail","Precision").getPropertyValue());
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
            .setJndiName("java:/HelloService")
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
	
	@Test
	public void tableImportCsvTest(){
		String source = new File("resources/dtf/tableCSV.csv").getAbsolutePath();
		String target = "RelationalModel.xmi";

		MetadataImportWizard.openWizard()
				.setImportType(MetadataImportWizard.TYPE_RELATIONAL_TABLE)
				.nextPage()
				.setPathToFile(source)
				.setProject(PROJECT_NAME, target)
				.finish();
		
		assertTrue(modelExplorer.containsItem(PROJECT_NAME,target));
		assertTrue(modelExplorer.containsItem(PROJECT_NAME,target, "Table_1"));
		assertTrue(modelExplorer.containsItem(PROJECT_NAME,target, "Table_2"));
		assertTrue(modelExplorer.containsItem(PROJECT_NAME,target, "TestIndex1"));
		assertTrue(modelExplorer.containsItem(PROJECT_NAME,target, "TestIndex2"));
		
		assertTrue(modelExplorer.containsItem(PROJECT_NAME,target, "Table_2", "Column_5 : string(255)"));
		assertTrue(modelExplorer.containsItem(PROJECT_NAME,target, "Table_1", "Column_4 : date"));
		assertTrue(modelExplorer.containsItem(PROJECT_NAME,target, "Table_1", "Column_3 : bigdecimal"));
		
		org.jboss.tools.teiid.reddeer.editor.TableEditor editor = new RelationalModelEditor(target).openTableEditor();
		
		assertEquals("Table_1 Description", editor.getCellText(0, "Description")); 
		assertEquals("Table_2 Description", editor.getCellText(1, "Description"));
		
		editor.openTab("Columns");
		
		assertEquals("Column_1 Description", editor.getCellText(0, "Description")); 
		assertEquals("Column_2 Description", editor.getCellText(1, "Description")); 
		assertEquals("Column_3 Description", editor.getCellText(2, "Description")); 
		assertEquals("Column_4 Description", editor.getCellText(3, "Description")); 
		assertEquals("Column_5 Description", editor.getCellText(4, "Description")); 
		assertEquals("Column_6 Description", editor.getCellText(5, "Description")); 
		
		assertEquals("TestIndex1", editor.getCellText(1, "Indexes")); 
		assertEquals("TestIndex2", editor.getCellText(4, "Indexes")); 
	}
	
	@Test
	public void virtualTableImportCsvTest(){
		String source = new File("resources/dtf/virtualTableCSV.csv").getAbsolutePath();
		String target = "RelationalModel.xmi";
		
		modelExplorer.importProject(VIRTUAL_TABLES_PROJECT);
		
		MetadataImportWizard.openWizard()
				.setImportType(MetadataImportWizard.TYPE_RELATIONAL_VIRTUAL_TABLE)
				.nextPage()
				.setPathToFile(source)
				.setProject(VIRTUAL_TABLES_PROJECT, target)
				.finish();
		
		assertTrue(modelExplorer.containsItem(VIRTUAL_TABLES_PROJECT,target));
		assertTrue(modelExplorer.containsItem(VIRTUAL_TABLES_PROJECT,target, "VTable1"));
		assertTrue(modelExplorer.containsItem(VIRTUAL_TABLES_PROJECT,target, "VTable2"));
		assertTrue(modelExplorer.containsItem(VIRTUAL_TABLES_PROJECT,target, "VTable10"));
		
		assertTrue(modelExplorer.containsItem(VIRTUAL_TABLES_PROJECT,target, "VTable1", "Column_1 : string(255)"));
		assertTrue(modelExplorer.containsItem(VIRTUAL_TABLES_PROJECT,target, "VTable1", "Column_2 : date"));
		assertTrue(modelExplorer.containsItem(VIRTUAL_TABLES_PROJECT,target, "VTable1", "Column_3 : bigdecimal(1)"));
		assertTrue(modelExplorer.containsItem(VIRTUAL_TABLES_PROJECT,target, "VTable1", "Column_4 : biginteger(4000)"));
		
		assertTrue(modelExplorer.containsItem(VIRTUAL_TABLES_PROJECT,target, "VTable2", "Column_4 : biginteger(4000)"));
		assertTrue(modelExplorer.containsItem(VIRTUAL_TABLES_PROJECT,target, "VTable10", "Column_5 : double"));
		
		assertTrue(modelExplorer.containsItem(VIRTUAL_TABLES_PROJECT,target, "VProc1"));
		
		org.jboss.tools.teiid.reddeer.editor.TableEditor editor = new RelationalModelEditor(target).openTableEditor();
		
		assertEquals("Descrip, no embedded quotes", editor.getCellText(0, "Description")); 
		assertEquals("Descrip 2 with quotes \"xxx\"", editor.getCellText(1, "Description"));
		assertEquals("Descrip 10", editor.getCellText(2, "Description")); 
		assertEquals("Descrip VProc1", editor.getCellText(3, "Description")); 
	}
}
