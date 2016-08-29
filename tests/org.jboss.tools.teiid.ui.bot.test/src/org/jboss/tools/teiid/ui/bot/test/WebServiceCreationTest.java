package org.jboss.tools.teiid.ui.bot.test;

import static org.junit.Assert.assertEquals;

import java.io.File;
import java.io.IOException;

import org.jboss.reddeer.common.wait.AbstractWait;
import org.jboss.reddeer.common.wait.TimePeriod;
import org.jboss.reddeer.junit.requirement.inject.InjectRequirement;
import org.jboss.reddeer.junit.runner.RedDeerSuite;
import org.jboss.reddeer.requirements.openperspective.OpenPerspectiveRequirement.OpenPerspective;
import org.jboss.reddeer.requirements.server.ServerReqState;
import org.jboss.reddeer.swt.impl.menu.ShellMenu;
import org.jboss.reddeer.workbench.impl.shell.WorkbenchShell;
import org.jboss.tools.teiid.reddeer.ChildType;
import org.jboss.tools.teiid.reddeer.ModelBuilder;
import org.jboss.tools.teiid.reddeer.ModelClass;
import org.jboss.tools.teiid.reddeer.ModelType;
import org.jboss.tools.teiid.reddeer.connection.ConnectionProfileConstants;
import org.jboss.tools.teiid.reddeer.connection.ResourceFileHelper;
import org.jboss.tools.teiid.reddeer.connection.SimpleHttpClient;
import org.jboss.tools.teiid.reddeer.dialog.CreateWebServiceDialog;
import org.jboss.tools.teiid.reddeer.dialog.XmlDocumentBuilderDialog;
import org.jboss.tools.teiid.reddeer.editor.TableEditor;
import org.jboss.tools.teiid.reddeer.editor.TransformationEditor;
import org.jboss.tools.teiid.reddeer.editor.VDBEditor;
import org.jboss.tools.teiid.reddeer.editor.WebServiceModelEditor;
import org.jboss.tools.teiid.reddeer.editor.XmlModelEditor;
import org.jboss.tools.teiid.reddeer.perspective.TeiidPerspective;
import org.jboss.tools.teiid.reddeer.requirement.TeiidServerRequirement;
import org.jboss.tools.teiid.reddeer.requirement.TeiidServerRequirement.TeiidServer;
import org.jboss.tools.teiid.reddeer.view.ModelExplorer;
import org.jboss.tools.teiid.reddeer.view.ProblemsViewEx;
import org.jboss.tools.teiid.reddeer.view.ServersViewExt;
import org.jboss.tools.teiid.reddeer.wizard.newWizard.MetadataModelWizard;
import org.jboss.tools.teiid.reddeer.wizard.newWizard.VdbWizard;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * @author skaleta
 * tested features:
 * - create WebService Model from: WSDL file, XML Document, Relational Model
 * - create and fulfill operations
 * - generate, deploy and test SOAP WAR 
 */
@RunWith(RedDeerSuite.class)
@OpenPerspective(TeiidPerspective.class)
@TeiidServer(state = ServerReqState.RUNNING, connectionProfiles = {ConnectionProfileConstants.ORACLE_11G_PRODUCTS})
public class WebServiceCreationTest {
	private static final String PROJECT_NAME = "WsCreationProject";
	private static final String WS_MODEL = "ProductsWs.xmi";
	private static final String INTERFACE_NAME = "ProductInfo";
	private static final String OPERATION_GET_ALL = "getAllProductInfo";
	private static final String OPERATION_GET = "getProductInfo";
	private static final String OPERATION_INSERT = "insertProductInfo";
	private static final String OPERATION_DELETE = "deleteProductInfo";
	private static final String DOCUMENT_PRODUCT = "productDocument";
	private static final String DOCUMENT_GOOD = "goodResultsDocument";
	private static final String DOCUMENT_BAD = "badResultsDocument";
	
	@InjectRequirement
	private static TeiidServerRequirement teiidServer;
	
	private ModelExplorer modelExplorer;
	private static ResourceFileHelper fileHelper;
	
	@BeforeClass
	public static void setUp() throws Exception {
		new WorkbenchShell().maximize();
		fileHelper = new ResourceFileHelper();
		fileHelper.copyFileToServer(new File("resources/projects/WsCreationProject/others/application-roles.properties").getAbsolutePath(), 
				teiidServer.getServerConfig().getServerBase().getHome() + "/standalone/configuration/application-roles.properties");
		new ServersViewExt().refreshServer(teiidServer.getName());
	}
	
	@Before
	public void importProject(){
		modelExplorer = new ModelExplorer();
		modelExplorer.importProject(PROJECT_NAME);
		modelExplorer.getProject(PROJECT_NAME).refresh();
		modelExplorer.changeConnectionProfile(ConnectionProfileConstants.ORACLE_11G_PRODUCTS, PROJECT_NAME, "sources", "SourceModel.xmi");
	}
	
	@After
	public void cleanUp(){
		modelExplorer.deleteAllProjectsSafely();
	}
	
	@Test
	public void testCreationFromWsdl() throws IOException{
		// 1. Create Web Service Model
		modelExplorer.deleteModel(PROJECT_NAME, "views", "XmlModel.xmi");
		modelExplorer.selectItem(PROJECT_NAME, "web_services");
		MetadataModelWizard.openWizard()
				.setModelName(WS_MODEL.substring(0,10))
				.selectModelClass(ModelClass.WEBSERVICE)
		        .selectModelType(ModelType.VIEW)
		        .selectModelBuilder(ModelBuilder.BUILD_FROM_WSDL_URL)
				.nextPage()
				.setWsdlFileFromWorkspace(PROJECT_NAME, "others", "ProductsInfo.wsdl")
				.nextPage()
				.nextPage()
				.nextPage()
				.nextPage()
				.nextPage()
				.finish();
		
		new WebServiceModelEditor(WS_MODEL).saveAndClose();
		AbstractWait.sleep(TimePeriod.SHORT);
		
		// 2. Define XML documents
		modelExplorer.renameModel("XmlModel.xmi", PROJECT_NAME, "web_services", "ProductsWsResponses.xmi");
		modelExplorer.openModelEditor(PROJECT_NAME, "web_services", "XmlModel.xmi");
		XmlModelEditor xmlEditor = new XmlModelEditor("XmlModel.xmi");
		
		xmlEditor.deleteDocument("ProductInfo_getAllProductInfo_getAllProductsInfo_NewOutput");
		new WebServiceModelEditor(WS_MODEL).close();
		xmlEditor.renameDocument("ProductInfo_getProductInfo_getProductsInfo_OutputMsg", DOCUMENT_PRODUCT);
		xmlEditor.renameDocument("ProductInfo_deleteProductInfo_deleteProductsInfo_ResultOutput", DOCUMENT_GOOD);
		xmlEditor.renameDocument("ProductInfo_insertProductInfo_insertProductsInfo_ResultOutput", DOCUMENT_BAD);
		
		xmlEditor.openDocument(DOCUMENT_PRODUCT);
		xmlEditor.openMappingClass("ProductsInfo_Output_Instance");
		TransformationEditor outputTransfEditor = xmlEditor.openTransformationEditor();
		outputTransfEditor.insertAndValidateSql("SELECT * FROM RelationalModel.ProductInfo");
		outputTransfEditor.close();
		xmlEditor.returnToMappingClassOverview();
		xmlEditor.returnToDocumentOverview();
		
		xmlEditor.openDocument(DOCUMENT_GOOD);
		xmlEditor.openMappingClass("putResults");
		outputTransfEditor = xmlEditor.openTransformationEditor();
		outputTransfEditor.insertAndValidateSql("SELECT 'Operation Successful!' AS results");
		outputTransfEditor.close();
		xmlEditor.returnToMappingClassOverview();
		xmlEditor.returnToDocumentOverview();
		
		xmlEditor.openDocument(DOCUMENT_BAD);
		xmlEditor.openMappingClass("putResults");
		outputTransfEditor = xmlEditor.openTransformationEditor();
		outputTransfEditor.insertAndValidateSql("SELECT 'Operation Failed!' AS results");
		outputTransfEditor.close();
		xmlEditor.returnToMappingClassOverview();
		xmlEditor.returnToDocumentOverview();
		
		AbstractWait.sleep(TimePeriod.SHORT);
		xmlEditor.saveAndClose();
		
		// 3. Define web service operations
		modelExplorer.openModelEditor(PROJECT_NAME, "web_services", WS_MODEL);
		WebServiceModelEditor wsEditor = new WebServiceModelEditor(WS_MODEL);
		wsEditor.openOperationEditor();
		
		wsEditor.selectOperation(INTERFACE_NAME, OPERATION_GET_ALL);
		wsEditor.setOperationProcedure(fileHelper.getSql("WebServiceCreationTest/GetAll.sql"));
		
		wsEditor.selectOperation(INTERFACE_NAME, OPERATION_GET);
		wsEditor.replaceTextInOperationProcedure("SELECT * FROM XmlModel.ProductInfo_getProductInfo_getProductsInfo_OutputMsg "
				+ "WHERE XmlModel.ProductInfo_getProductInfo_getProductsInfo_OutputMsg.REPLACE_WITH_ELEMENT_OR_COLUMN = VARIABLES.IN_INSTR_ID;",
				fileHelper.getSql("WebServiceCreationTest/Get.sql"));
		
		wsEditor.selectOperation(INTERFACE_NAME, OPERATION_INSERT);
		wsEditor.setOperationProcedure(fileHelper.getSql("WebServiceCreationTest/InsertWithDeclarations.sql"));
		
		wsEditor.selectOperation(INTERFACE_NAME, OPERATION_DELETE);
		wsEditor.replaceTextInOperationProcedure("SELECT * FROM XmlModel.ProductInfo_deleteProductInfo_deleteProductsInfo_ResultOutput "
				+ "WHERE XmlModel.ProductInfo_deleteProductInfo_deleteProductsInfo_ResultOutput.REPLACE_WITH_ELEMENT_OR_COLUMN = VARIABLES.IN_INSTR_ID;",
				fileHelper.getSql("WebServiceCreationTest/Delete.sql"));		
		
		AbstractWait.sleep(TimePeriod.SHORT);
		wsEditor.saveAndClose();
		
		new ProblemsViewEx().checkErrors();
		
		// 4. Generate WAR and test it
		generateWarAndTestIt("WsWsdlVdb");
	}
	
	@Test
	public void testCreationFromRelationalModel() throws IOException{
		// 1. Create Web Service Model
		modelExplorer.deleteModel(PROJECT_NAME, "views", "XmlModel.xmi");
		CreateWebServiceDialog createWsDialog = modelExplorer.modelingWebService(false, PROJECT_NAME, "views", "RelationalModel.xmi");
		createWsDialog.setLocation(PROJECT_NAME, "web_services")
				.setModelName(WS_MODEL)
				.setInputSchemaName("InputSchema")
				.setOutputSchemaName("OutputSchema");
		createWsDialog.finish();
	
		// 2. Define XML documents
		new WebServiceModelEditor(WS_MODEL).close();
		modelExplorer.renameModel("XmlModel.xmi", PROJECT_NAME, "web_services", "OutputSchema_View.xmi");		
		
		String[] xmlModelPath = new String[]{PROJECT_NAME, "web_services", "XmlModel.xmi"};
		modelExplorer.openModelEditor(xmlModelPath);
		XmlModelEditor xmlEditor = new XmlModelEditor("XmlModel.xmi");
		
		xmlEditor.renameDocument("ProductInfo_OutputView", DOCUMENT_PRODUCT);
		
		modelExplorer.addChildToModelItem(ChildType.XML_DOCUMENT, xmlModelPath);
		XmlDocumentBuilderDialog xmlDocumentBuilder = new XmlDocumentBuilderDialog();
		xmlDocumentBuilder.setSchema(PROJECT_NAME,"schemas","ProductsSchema.xsd")
				.addElement("putResults : putResultsType");
		xmlDocumentBuilder.finish();
		AbstractWait.sleep(TimePeriod.getCustom(3));
		
		xmlEditor.openMappingClass("putResults");
		TransformationEditor outputTransfEditor = xmlEditor.openTransformationEditor();
		outputTransfEditor.insertAndValidateSql("SELECT 'Operation Successful!' AS results");
		outputTransfEditor.close();
		xmlEditor.returnToMappingClassOverview();
		xmlEditor.returnToDocumentOverview();
		xmlEditor.renameDocument("putResultsDocument", DOCUMENT_GOOD);
		
		modelExplorer.addChildToModelItem(ChildType.XML_DOCUMENT, xmlModelPath);
		xmlDocumentBuilder = new XmlDocumentBuilderDialog();
		xmlDocumentBuilder.setSchema(PROJECT_NAME,"schemas","ProductsSchema.xsd")
				.addElement("putResults : putResultsType");
		xmlDocumentBuilder.finish();
		AbstractWait.sleep(TimePeriod.getCustom(3));
		
		xmlEditor.openMappingClass("putResults");
		outputTransfEditor = xmlEditor.openTransformationEditor();
		outputTransfEditor.insertAndValidateSql("SELECT 'Operation Failed!' AS results");
		outputTransfEditor.close();
		xmlEditor.returnToMappingClassOverview();
		xmlEditor.returnToDocumentOverview();
		xmlEditor.renameDocument("putResultsDocument", DOCUMENT_BAD);
		
		AbstractWait.sleep(TimePeriod.SHORT);
		xmlEditor.saveAndClose();				
		
		//3. Define web service operations
		String wsModelPath = PROJECT_NAME + "/web_services/" + WS_MODEL;
		modelExplorer.renameModelItem(INTERFACE_NAME, (wsModelPath+"/"+"RelationalModel_ProductInfo").split("/"));
		modelExplorer.openModelEditor(PROJECT_NAME, "web_services", WS_MODEL);
		WebServiceModelEditor wsEditor = new WebServiceModelEditor(WS_MODEL);
		
		// 3.2. Define get all operation
		modelExplorer.addChildToModelItem(ChildType.OPERATION, (wsModelPath+"/"+INTERFACE_NAME).split("/"));
		modelExplorer.renameModelItem(OPERATION_GET_ALL, (wsModelPath+"/"+INTERFACE_NAME+"/NewOperation").split("/"));
		
		modelExplorer.addChildToModelItem(ChildType.INPUT, (wsModelPath+"/"+INTERFACE_NAME+"/"+OPERATION_GET_ALL).split("/") );
		TableEditor tableEditor = wsEditor.openTableEditor();
		tableEditor.openTab(TableEditor.Tabs.INPUTS);
		tableEditor.setCellTextViaProperties(OPERATION_GET_ALL, 
				"ProductsInfo_AllProducts (Path=/WsCreationProject/schemas/ProductsSchema.xsd/ProductsSchema.xsd)",
				"Misc", "Content via Element");
		tableEditor.close();
		
		wsEditor.selectOperation(INTERFACE_NAME, OPERATION_GET_ALL);
		wsEditor.setOperationProcedure(fileHelper.getSql("WebServiceCreationTest/GetAll.sql"));
		
		// 3.3. Define insert operation	
		modelExplorer.addChildToModelItem(ChildType.OPERATION, (wsModelPath+"/"+INTERFACE_NAME).split("/") );
		modelExplorer.renameModelItem(OPERATION_INSERT, (wsModelPath+"/"+INTERFACE_NAME+"/NewOperation").split("/"));
		modelExplorer.addChildToModelItem(ChildType.INPUT, (wsModelPath+"/"+INTERFACE_NAME+"/"+OPERATION_INSERT).split("/"));
		modelExplorer.addChildToModelItem(ChildType.OUTPUT, (wsModelPath+"/"+INTERFACE_NAME+"/"+OPERATION_INSERT).split("/"));
		
		tableEditor = wsEditor.openTableEditor();
		tableEditor.openTab(TableEditor.Tabs.INPUTS);
		tableEditor.setCellTextViaProperties(OPERATION_INSERT, 
				"ProductsInfo_New_Input (Path=/WsCreationProject/schemas/ProductsSchema.xsd/ProductsSchema.xsd)",
				"Misc", "Content via Element");
		tableEditor.openTab(TableEditor.Tabs.OUTPUTS);
		tableEditor.setCellTextViaProperties(OPERATION_INSERT, 
				"putResults (Path=/WsCreationProject/schemas/ProductsSchema.xsd/ProductsSchema.xsd)",
				"Misc", "Content via Element");
		tableEditor.openTab(TableEditor.Tabs.OUTPUTS);
		tableEditor.setCellTextViaProperties(OPERATION_INSERT, 
				"goodResultsDocument (Path=/WsCreationProject/web_services/XmlModel.xmi)",
				"Misc", "XML Document");
		tableEditor.close();
		
		wsEditor.selectOperation(INTERFACE_NAME, OPERATION_INSERT);
		wsEditor.replaceTextInOperationProcedure("SELECT * FROM XmlModel.goodResultsDocument;", 
				fileHelper.getSql("WebServiceCreationTest/Insert.sql"));
		
		// 3.4. Define delete operation
		modelExplorer.addChildToModelItem(ChildType.OPERATION, (wsModelPath+"/"+INTERFACE_NAME).split("/"));
		modelExplorer.renameModelItem(OPERATION_DELETE, (wsModelPath+"/"+INTERFACE_NAME+"/NewOperation").split("/"));
		modelExplorer.addChildToModelItem(ChildType.INPUT, (wsModelPath+"/"+INTERFACE_NAME+"/"+OPERATION_DELETE).split("/"));
		modelExplorer.addChildToModelItem(ChildType.OUTPUT, (wsModelPath+"/"+INTERFACE_NAME+"/"+OPERATION_DELETE).split("/"));
		
		tableEditor = wsEditor.openTableEditor();
		tableEditor.openTab(TableEditor.Tabs.INPUTS);
		tableEditor.setCellTextViaProperties(OPERATION_DELETE, 
				"ProductInfo_Input (Path=/WsCreationProject/web_services/InputSchema.xsd/InputSchema.xsd)",
				"Misc", "Content via Element");
		tableEditor.openTab(TableEditor.Tabs.OUTPUTS);
		tableEditor.setCellTextViaProperties(OPERATION_DELETE, 
				"putResults (Path=/WsCreationProject/schemas/ProductsSchema.xsd/ProductsSchema.xsd)",
				"Misc", "Content via Element");
		tableEditor.openTab(TableEditor.Tabs.OUTPUTS);
		tableEditor.setCellTextViaProperties(OPERATION_DELETE, 
				"goodResultsDocument (Path=/WsCreationProject/web_services/XmlModel.xmi)",
				"Misc", "XML Document");
		tableEditor.close();
		
		wsEditor.selectOperation(INTERFACE_NAME, OPERATION_DELETE);
		wsEditor.replaceTextInOperationProcedure("SELECT * FROM XmlModel.goodResultsDocument;", 
				fileHelper.getSql("WebServiceCreationTest/Delete.sql"));
		
		AbstractWait.sleep(TimePeriod.SHORT);
		wsEditor.saveAndClose();
		
		new ProblemsViewEx().checkErrors();	
		
		// 4. Generate WAR and test it
		generateWarAndTestIt("WsRelVdb");
	}
	
	@Test
	public void testCreationFromXmlDocument() throws IOException{
		// 1. Create Web Service Model
		CreateWebServiceDialog createWsDialog = modelExplorer.modelingWebService(true, PROJECT_NAME, "views", "XmlModel.xmi", DOCUMENT_PRODUCT);
		createWsDialog.setLocation(PROJECT_NAME, "web_services")
				.setModelName(WS_MODEL.substring(0,10))
				.setInterfaceName(INTERFACE_NAME)
				.setOperationName(OPERATION_GET_ALL)
				.setInputMsgElement(PROJECT_NAME, "schemas", "ProductsSchema.xsd", "ProductsSchema.xsd", "ProductsInfo_AllProducts")
				.setInputMsgName("Input")
				.setOutputMsgName("Output");
		createWsDialog.finish();
		
		// 2. Define web service operations
		String wsModelPath = PROJECT_NAME + "/web_services/" + WS_MODEL;
		modelExplorer.openModelEditor(wsModelPath.split("/"));
		WebServiceModelEditor wsEditor = new WebServiceModelEditor(WS_MODEL);	
		
		// 2.1. Define get operation
		modelExplorer.addChildToModelItem(ChildType.OPERATION, (wsModelPath+"/"+INTERFACE_NAME).split("/"));
		modelExplorer.renameModelItem(OPERATION_GET, (wsModelPath+"/"+INTERFACE_NAME+"/NewOperation").split("/"));
		modelExplorer.addChildToModelItem(ChildType.INPUT, (wsModelPath+"/"+INTERFACE_NAME+"/"+OPERATION_GET).split("/"));
		modelExplorer.addChildToModelItem(ChildType.OUTPUT, (wsModelPath+"/"+INTERFACE_NAME+"/"+OPERATION_GET).split("/"));
		
		TableEditor tableEditor = wsEditor.openTableEditor();
		tableEditor.openTab(TableEditor.Tabs.INPUTS);
		tableEditor.setCellTextViaProperties(OPERATION_GET, 
				"ProductsInfo_Input (Path=/WsCreationProject/schemas/ProductsSchema.xsd/ProductsSchema.xsd)",
				"Misc", "Content via Element");
		tableEditor.openTab(TableEditor.Tabs.OUTPUTS);
		tableEditor.setCellTextViaProperties(OPERATION_GET, 
				"ProductsInfo_Output (Path=/WsCreationProject/schemas/ProductsSchema.xsd/ProductsSchema.xsd)",
				"Misc", "Content via Element");
		tableEditor.openTab(TableEditor.Tabs.OUTPUTS);
		tableEditor.setCellTextViaProperties(OPERATION_GET, 
				"productDocument (Path=/WsCreationProject/views/XmlModel.xmi)",
				"Misc", "XML Document");
		tableEditor.close();
		
		wsEditor.selectOperation(INTERFACE_NAME, OPERATION_GET);
		wsEditor.replaceTextInOperationProcedure("SELECT * FROM XmlModel.productDocument;", 
				fileHelper.getSql("WebServiceCreationTest/Get.sql"));	

		// 2.2. Define insert operation
		modelExplorer.addChildToModelItem(ChildType.OPERATION, (wsModelPath+"/"+INTERFACE_NAME).split("/"));
		modelExplorer.renameModelItem(OPERATION_INSERT, (wsModelPath+"/"+INTERFACE_NAME+"/NewOperation").split("/"));
		modelExplorer.addChildToModelItem(ChildType.INPUT, (wsModelPath+"/"+INTERFACE_NAME+"/"+OPERATION_INSERT).split("/"));
		modelExplorer.addChildToModelItem(ChildType.OUTPUT, (wsModelPath+"/"+INTERFACE_NAME+"/"+OPERATION_INSERT).split("/"));
		
		tableEditor = wsEditor.openTableEditor();
		tableEditor.openTab(TableEditor.Tabs.INPUTS);
		tableEditor.setCellTextViaProperties(OPERATION_INSERT, 
				"ProductsInfo_New_Input (Path=/WsCreationProject/schemas/ProductsSchema.xsd/ProductsSchema.xsd)",
				"Misc", "Content via Element");
		tableEditor.openTab(TableEditor.Tabs.OUTPUTS);
		tableEditor.setCellTextViaProperties(OPERATION_INSERT, 
				"putResults (Path=/WsCreationProject/schemas/ProductsSchema.xsd/ProductsSchema.xsd)",
				"Misc", "Content via Element");
		tableEditor.openTab(TableEditor.Tabs.OUTPUTS);
		tableEditor.setCellTextViaProperties(OPERATION_INSERT, 
				"goodResultsDocument (Path=/WsCreationProject/views/XmlModel.xmi)",
				"Misc", "XML Document");
		tableEditor.close();
		
		wsEditor.selectOperation(INTERFACE_NAME, OPERATION_INSERT);
		wsEditor.replaceTextInOperationProcedure("SELECT * FROM XmlModel.goodResultsDocument;", 
				fileHelper.getSql("WebServiceCreationTest/Insert.sql"));	
		
		// 2.3. Define delete operation
		modelExplorer.addChildToModelItem(ChildType.OPERATION, (wsModelPath+"/"+INTERFACE_NAME).split("/"));
		modelExplorer.renameModelItem(OPERATION_DELETE, (wsModelPath+"/"+INTERFACE_NAME+"/NewOperation").split("/"));
		modelExplorer.addChildToModelItem(ChildType.INPUT, (wsModelPath+"/"+INTERFACE_NAME+"/"+OPERATION_DELETE).split("/"));
		modelExplorer.addChildToModelItem(ChildType.OUTPUT, (wsModelPath+"/"+INTERFACE_NAME+"/"+OPERATION_DELETE).split("/"));
		
		tableEditor = wsEditor.openTableEditor();
		tableEditor.openTab(TableEditor.Tabs.INPUTS);
		tableEditor.setCellTextViaProperties(OPERATION_DELETE, 
				"ProductsInfo_Input (Path=/WsCreationProject/schemas/ProductsSchema.xsd/ProductsSchema.xsd)",
				"Misc", "Content via Element");
		tableEditor.openTab(TableEditor.Tabs.OUTPUTS);
		tableEditor.setCellTextViaProperties(OPERATION_DELETE, 
				"putResults (Path=/WsCreationProject/schemas/ProductsSchema.xsd/ProductsSchema.xsd)",
				"Misc", "Content via Element");
		tableEditor.openTab(TableEditor.Tabs.OUTPUTS);
		tableEditor.setCellTextViaProperties(OPERATION_DELETE, 
				"goodResultsDocument (Path=/WsCreationProject/views/XmlModel.xmi)",
				"Misc", "XML Document");
		tableEditor.close();
		
		wsEditor.selectOperation(INTERFACE_NAME, OPERATION_DELETE);
		wsEditor.replaceTextInOperationProcedure("SELECT * FROM XmlModel.goodResultsDocument;", 
				fileHelper.getSql("WebServiceCreationTest/Delete.sql"));
		
		AbstractWait.sleep(TimePeriod.SHORT);
		wsEditor.saveAndClose();
		
		new ProblemsViewEx().checkErrors();	
		
		// 3. Generate WAR and test it
		generateWarAndTestIt("WsXmlVdb");
	}

	private void generateWarAndTestIt(String vdbName) throws IOException{
		// 1. Create VDB and deploy it
		VdbWizard.openVdbWizard()
				.setLocation(PROJECT_NAME)
				.setName(vdbName)
				.addModel(PROJECT_NAME, "web_services", WS_MODEL)
				.finish();
		
		AbstractWait.sleep(TimePeriod.SHORT); 
		String translator = VDBEditor.getInstance(vdbName + ".vdb").getTranslatorName("SourceModel.xmi");
		assertEquals("Translator: " + translator, "oracle", translator);
		
		modelExplorer.deployVdb(PROJECT_NAME, vdbName);
		
		// 2. create WAR, deploy, send requests and check responses (HTTPBasic security)
		String warName = vdbName + "HttpBasicWar";
		modelExplorer.generateWar(true, PROJECT_NAME, vdbName)
				.setVdbJndiName(vdbName)
				.setContextName(warName)
				.setWarFileLocation(modelExplorer.getProjectPath(PROJECT_NAME) + "/others")
				.setHttpBasicSecurity("teiid-security", "products")
				.finish();
		
		modelExplorer.deployWar(teiidServer, PROJECT_NAME, "others", warName);

		postRequestHttpBasicSecurity("getAllProductInfo", 
				"http://localhost:8080/" + warName + "/" + INTERFACE_NAME + "?wsdl",
				fileHelper.getXmlNoHeader("WebServiceCreationTest/GetAllRequest.xml"), 
				fileHelper.getXmlNoHeader("WebServiceCreationTest/GetAllResponse.xml"));

		try {
			postRequestNoneSecurity("getAllProductInfo", 
					"http://localhost:8080/" + warName + "/" + INTERFACE_NAME + "?wsdl",
					fileHelper.getXmlNoHeader("WebServiceCreationTest/GetAllRequest.xml"), null);
		} catch (IOException e){
			// expected
		}
		
		try {
			postRequestHttpBasicSecurity("insertProductInfo", 
					"http://localhost:8080/" + warName + "/" + INTERFACE_NAME + "?wsdl",
					fileHelper.getXmlNoHeader("WebServiceCreationTest/InsertRequest.xml"), 
					fileHelper.getXmlNoHeader("WebServiceCreationTest/ResponseSuccessful.xml"));
		} catch (AssertionError e){
			System.err.println("PREVIOUSLY INSERTED DATA ARE NOT REMOVED FROM DATABASE!!!");
			throw new AssertionError(e);
		} 
		
		try {
			postRequestHttpBasicSecurity("insertProductInfo", 
					"http://localhost:8080/" + warName + "/" + INTERFACE_NAME + "?wsdl",
					fileHelper.getXmlNoHeader("WebServiceCreationTest/InsertRequest.xml"), 
					fileHelper.getXmlNoHeader("WebServiceCreationTest/ResponseFailed.xml"));
		} catch (AssertionError e){
			System.err.println("PREVIOUSLY INSERTED DATA ARE NOT REMOVED FROM DATABASE!!!");
			throw new AssertionError(e);
		} catch (IOException e) {
			System.err.println("PREVIOUSLY INSERTED DATA ARE NOT REMOVED FROM DATABASE!!!");
			throw new IOException(e);
		}
		
		try {
			postRequestHttpBasicSecurity("getProductInfo",
					"http://localhost:8080/" + warName + "/" + INTERFACE_NAME + "?wsdl",
					fileHelper.getXmlNoHeader("WebServiceCreationTest/GetRequest.xml"),
					fileHelper.getXmlNoHeader("WebServiceCreationTest/GetResponse.xml"));
		} catch (AssertionError e){
			System.err.println("PREVIOUSLY INSERTED DATA ARE NOT REMOVED FROM DATABASE!!!");
			throw new AssertionError(e);
		} catch (IOException e) {
			System.err.println("PREVIOUSLY INSERTED DATA ARE NOT REMOVED FROM DATABASE!!!");
			throw new IOException(e);
		}
		
		try {
			postRequestHttpBasicSecurity("deleteProductInfo",
					"http://localhost:8080/" + warName + "/" + INTERFACE_NAME + "?wsdl",
					fileHelper.getXmlNoHeader("WebServiceCreationTest/DeleteRequest.xml"),
					fileHelper.getXmlNoHeader("WebServiceCreationTest/ResponseSuccessful.xml"));
		} catch (AssertionError e){
			System.err.println("PREVIOUSLY INSERTED DATA ARE NOT REMOVED FROM DATABASE!!!");
			throw new AssertionError(e);
		} catch (IOException e) {
			System.err.println("PREVIOUSLY INSERTED DATA ARE NOT REMOVED FROM DATABASE!!!");
			throw new IOException(e);
		}
		
		postRequestHttpBasicSecurity("deleteProductInfo",
				"http://localhost:8080/" + warName + "/" + INTERFACE_NAME + "?wsdl",
				fileHelper.getXmlNoHeader("WebServiceCreationTest/DeleteRequest.xml"),
				fileHelper.getXmlNoHeader("WebServiceCreationTest/ResponseFailed.xml"));
		
		postRequestHttpBasicSecurity("getProductInfo",
				"http://localhost:8080/" + warName + "/" + INTERFACE_NAME + "?wsdl",
				fileHelper.getXmlNoHeader("WebServiceCreationTest/GetRequest.xml"),
				fileHelper.getXmlNoHeader("WebServiceCreationTest/GetResponseNotFound.xml"));
		
		// 3. create WAR, deploy, send requests and check responses (None security)
		warName = vdbName + "NoneWar";
		modelExplorer.generateWar(true, PROJECT_NAME, vdbName)
				.setVdbJndiName(vdbName)
				.setContextName(warName)
				.setWarFileLocation(modelExplorer.getProjectPath(PROJECT_NAME) + "/others")
				.setNoneSecurity()
				.finish();
		
		modelExplorer.deployWar(teiidServer, PROJECT_NAME, "others", warName);
		
		postRequestNoneSecurity("getAllProductInfo",
				"http://localhost:8080/" + warName + "/" + INTERFACE_NAME + "?wsdl",
				fileHelper.getXmlNoHeader("WebServiceCreationTest/GetAllRequest.xml"), 
				fileHelper.getXmlNoHeader("WebServiceCreationTest/GetAllResponse.xml"));

		try {
			postRequestNoneSecurity("insertProductInfo", 
					"http://localhost:8080/" + warName + "/" + INTERFACE_NAME + "?wsdl",
					fileHelper.getXmlNoHeader("WebServiceCreationTest/InsertRequest.xml"), 
					fileHelper.getXmlNoHeader("WebServiceCreationTest/ResponseSuccessful.xml"));
		} catch (AssertionError e){
			System.err.println("PREVIOUSLY INSERTED DATA ARE NOT REMOVED FROM DATABASE!!!");
			throw new AssertionError(e);
		}
		
		try {
			postRequestNoneSecurity("insertProductInfo", 
					"http://localhost:8080/" + warName + "/" + INTERFACE_NAME + "?wsdl",
					fileHelper.getXmlNoHeader("WebServiceCreationTest/InsertRequest.xml"), 
					fileHelper.getXmlNoHeader("WebServiceCreationTest/ResponseFailed.xml"));
		} catch (AssertionError e){
			System.err.println("PREVIOUSLY INSERTED DATA ARE NOT REMOVED FROM DATABASE!!!");
			throw new AssertionError(e);
		} catch (IOException e) {
			System.err.println("PREVIOUSLY INSERTED DATA ARE NOT REMOVED FROM DATABASE!!!");
			throw new IOException(e);
		}
		
		try {
			postRequestNoneSecurity("getProductInfo",
					"http://localhost:8080/" + warName + "/" + INTERFACE_NAME + "?wsdl",
					fileHelper.getXmlNoHeader("WebServiceCreationTest/GetRequest.xml"),
					fileHelper.getXmlNoHeader("WebServiceCreationTest/GetResponse.xml"));
		} catch (AssertionError e){
			System.err.println("PREVIOUSLY INSERTED DATA ARE NOT REMOVED FROM DATABASE!!!");
			throw new AssertionError(e);
		} catch (IOException e) {
			System.err.println("PREVIOUSLY INSERTED DATA ARE NOT REMOVED FROM DATABASE!!!");
			throw new IOException(e);
		}
		
		try {
			postRequestNoneSecurity("deleteProductInfo",
					"http://localhost:8080/" + warName + "/" + INTERFACE_NAME + "?wsdl",
					fileHelper.getXmlNoHeader("WebServiceCreationTest/DeleteRequest.xml"),
					fileHelper.getXmlNoHeader("WebServiceCreationTest/ResponseSuccessful.xml"));
		} catch (AssertionError e){
			System.err.println("PREVIOUSLY INSERTED DATA ARE NOT REMOVED FROM DATABASE!!!");
			throw new AssertionError(e);
		} catch (IOException e) {
			System.err.println("PREVIOUSLY INSERTED DATA ARE NOT REMOVED FROM DATABASE!!!");
			throw new IOException(e);
		}
		
		postRequestNoneSecurity("deleteProductInfo",
				"http://localhost:8080/" + warName + "/" + INTERFACE_NAME + "?wsdl",
				fileHelper.getXmlNoHeader("WebServiceCreationTest/DeleteRequest.xml"),
				fileHelper.getXmlNoHeader("WebServiceCreationTest/ResponseFailed.xml"));
		
		postRequestNoneSecurity("getProductInfo",
				"http://localhost:8080/" + warName + "/" + INTERFACE_NAME + "?wsdl",
				fileHelper.getXmlNoHeader("WebServiceCreationTest/GetRequest.xml"),
				fileHelper.getXmlNoHeader("WebServiceCreationTest/GetResponseNotFound.xml"));		
	}
	
	private void postRequestHttpBasicSecurity(String soapAction, String uri, String request, String expected) throws IOException{
		String username = teiidServer.getServerConfig().getServerBase().getProperty("teiidUser");
		String password = teiidServer.getServerConfig().getServerBase().getProperty("teiidPassword");
		System.out.println("Using HTTPBasic security with username '" + username + "' and password '" + password + "'");
		String response = new SimpleHttpClient(uri)
				.setBasicAuth(username, password)
				.addHeader("Content-Type", "text/xml; charset=utf-8")
				.addHeader("SOAPAction", soapAction)
				.post(request);
		assertEquals(expected, response);
	}
	
	private void postRequestNoneSecurity(String soapAction, String uri, String request, String expected) throws IOException{
		String response = new SimpleHttpClient(uri)
					.addHeader("Content-Type", "text/xml; charset=utf-8")
					.addHeader("SOAPAction", soapAction)
					.post(request);
		assertEquals(expected, response);
	}
}
