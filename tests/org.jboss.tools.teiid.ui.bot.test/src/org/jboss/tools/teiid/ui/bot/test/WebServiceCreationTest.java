package org.jboss.tools.teiid.ui.bot.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.fail;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;

import org.jboss.reddeer.common.wait.AbstractWait;
import org.jboss.reddeer.common.wait.TimePeriod;
import org.jboss.reddeer.junit.requirement.inject.InjectRequirement;
import org.jboss.reddeer.junit.runner.RedDeerSuite;
import org.jboss.reddeer.requirements.openperspective.OpenPerspectiveRequirement.OpenPerspective;
import org.jboss.reddeer.requirements.server.ServerReqState;
import org.jboss.reddeer.swt.impl.button.OkButton;
import org.jboss.reddeer.swt.impl.ctab.DefaultCTabItem;
import org.jboss.reddeer.swt.impl.menu.ShellMenu;
import org.jboss.reddeer.swt.impl.styledtext.DefaultStyledText;
import org.jboss.reddeer.swt.impl.text.DefaultText;
import org.jboss.tools.runtime.reddeer.preference.JBossRuntimeDetection;
import org.jboss.tools.teiid.reddeer.connection.ConnectionProfileConstants;
import org.jboss.tools.teiid.reddeer.connection.ResourceFileHelper;
import org.jboss.tools.teiid.reddeer.connection.SimpleHttpClient;
import org.jboss.tools.teiid.reddeer.connection.TeiidJDBCHelper;
import org.jboss.tools.teiid.reddeer.dialog.CreateWarDialog;
import org.jboss.tools.teiid.reddeer.dialog.XmlDocumentBuilderDialog;
import org.jboss.tools.teiid.reddeer.editor.ModelEditor;
import org.jboss.tools.teiid.reddeer.editor.RelationalModelEditor;
import org.jboss.tools.teiid.reddeer.editor.TableEditor;
import org.jboss.tools.teiid.reddeer.editor.TransformationEditor;
import org.jboss.tools.teiid.reddeer.editor.VdbEditor;
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
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * @author skaleta
 * tested features:
 * - create WebService Model:
 * 		- from WSDL file, complete generated XML documents, generate SOAP WAR (HTTP-Basic/None security) and test it 
 * 		- from XML document + test generated operation
 * 		- from source/view table/procedure + test generated operation
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
	private static final String DOCUMENT_PRODUCT = "ProductDocument";
	private static final String DOCUMENT_OK = "OkResultDocument";
	private static final String DOCUMENT_FAILED = "FailedResultDocument";
	
	@InjectRequirement
	private static TeiidServerRequirement teiidServer;
	
	private ModelExplorer modelExplorer;
	private static ResourceFileHelper fileHelper;
	
	@BeforeClass
	public static void setUp() throws Exception {
		fileHelper = new ResourceFileHelper();
		fileHelper.copyFileToServer(new File("resources/flat/WsCreationTest/application-roles.properties").getAbsolutePath(), 
				teiidServer.getServerConfig().getServerBase().getHome() + "/standalone/configuration/application-roles.properties");
		new ServersViewExt().restartServer(teiidServer.getName());
	}
	
	@Before
	public void importProject(){
		modelExplorer = new ModelExplorer();
		modelExplorer.importProject(PROJECT_NAME);
		modelExplorer.refreshProject(PROJECT_NAME);;
		modelExplorer.changeConnectionProfile(ConnectionProfileConstants.ORACLE_11G_PRODUCTS, PROJECT_NAME, "sources", "ProductsSource.xmi");
	}
	
	@After
	public void cleanUp(){
		modelExplorer.deleteAllProjectsSafely();
	}
	
	@Test
	public void testCreationFromWsdl() throws Exception{
		// 1. Create Web Service Model
		modelExplorer.selectItem(PROJECT_NAME, "web_services");
		MetadataModelWizard.openWizard()
				.setModelName(WS_MODEL.substring(0,10))
				.selectModelClass(MetadataModelWizard.ModelClass.WEBSERVICE)
		        .selectModelType(MetadataModelWizard.ModelType.VIEW)
		        .selectModelBuilder(MetadataModelWizard.ModelBuilder.BUILD_FROM_WSDL_URL)
				.nextPage()
				.setWsdlFileFromWorkspace(PROJECT_NAME, "others", "ProductInfo.wsdl")
				.nextPage()
				.nextPage()
				.nextPage()
				.nextPage()
				.nextPage()
				.finish();
		
		new WebServiceModelEditor(WS_MODEL).save();
		AbstractWait.sleep(TimePeriod.SHORT);
		
		// 2. Define XML documents
		modelExplorer.openModelEditor(PROJECT_NAME, "web_services", "ProductsWsResponses.xmi");
		XmlModelEditor xmlEditor = new XmlModelEditor("ProductsWsResponses.xmi");
		
		xmlEditor.deleteDocument("ProductInfo_getAllProductInfo_getAllProductInfo_Output");
		xmlEditor.renameDocument("ProductInfo_getProductInfo_getProductInfo_Output", DOCUMENT_PRODUCT);
		xmlEditor.renameDocument("ProductInfo_deleteProductInfo_deleteProductInfo_Output", DOCUMENT_OK);
		xmlEditor.renameDocument("ProductInfo_insertProductInfo_insertProductInfo_Output", DOCUMENT_FAILED);
		
		xmlEditor.openDocument(DOCUMENT_PRODUCT);
		xmlEditor.openMappingClass("ProductOutput_Instance");
		TransformationEditor transformationEditor = xmlEditor.openTransformationEditor();
		transformationEditor.insertAndValidateSql("SELECT * FROM ProductsView.ProductInfo");
		xmlEditor.returnToParentDiagram();
		xmlEditor.returnToParentDiagram();
		
		xmlEditor.openDocument(DOCUMENT_OK);
		xmlEditor.openMappingClass("ResultOutput");
		transformationEditor = xmlEditor.openTransformationEditor();
		transformationEditor.insertAndValidateSql("SELECT 'Operation Successful!' AS results");
		xmlEditor.returnToParentDiagram();
		xmlEditor.returnToParentDiagram();
		
		xmlEditor.openDocument(DOCUMENT_FAILED);
		xmlEditor.openMappingClass("ResultOutput");
		transformationEditor = xmlEditor.openTransformationEditor();
		transformationEditor.insertAndValidateSql("SELECT 'Operation Failed!' AS results");
		xmlEditor.returnToParentDiagram();
		xmlEditor.returnToParentDiagram();
		
		AbstractWait.sleep(TimePeriod.SHORT);
		xmlEditor.saveAndClose();
		
		// 3. Define web service operations
		WebServiceModelEditor wsEditor = new WebServiceModelEditor(WS_MODEL);

		wsEditor.replaceTextInOperationProcedure(INTERFACE_NAME, OPERATION_GET_ALL, 
				"ProductInfo_getAllProductInfo_getAllProductInfo_Output", DOCUMENT_PRODUCT);
		
		wsEditor.replaceAllTextInOperationProcedure(INTERFACE_NAME, OPERATION_GET, 
				"ProductInfo_getProductInfo_getProductInfo_Output", DOCUMENT_PRODUCT);
		wsEditor.replaceTextInOperationProcedure(INTERFACE_NAME, OPERATION_GET, 
				"REPLACE_WITH_ELEMENT_OR_COLUMN", "ProductOutput.ProductOutput_Instance.INSTR_ID");		

		wsEditor.setOperationProcedure(INTERFACE_NAME, OPERATION_INSERT, fileHelper.getSql("WebServiceCreationTest/Insert.sql"));
		
		wsEditor.setOperationProcedure(INTERFACE_NAME, OPERATION_DELETE, fileHelper.getSql("WebServiceCreationTest/Delete.sql"));
		
		wsEditor.saveAndClose();
		AbstractWait.sleep(TimePeriod.SHORT);
		
		ProblemsViewEx.checkErrors();

		// 4. Create VDB and deploy 
		String vdbName = "WsWsdlVdb";
		createAndDeployVdb(vdbName);

		// 5. Create WAR, deploy, send requests and check responses (HTTP-Basic security)
		String warName = vdbName + "WarHttpBasic";
		createAndDeployWar(warName, vdbName, true);
		
		String url = "http://localhost:8080/" + warName + "/" + INTERFACE_NAME + "?wsdl";
		String request = fileHelper.getXmlNoHeader("WebServiceCreationTest/GetAllRequest.xml");
		String response = SimpleHttpClient.postSoapRequest(teiidServer, url, "getAllProductInfo", request);
		String expected = fileHelper.getXmlNoHeader("WebServiceCreationTest/GetAllResponse.xml");
		assertEquals(expected, response);
		
		try {
			SimpleHttpClient.postSoapRequest(url, "getAllProductInfo", request);
		} catch (IOException e){/*expected*/}
		
		request = fileHelper.getXmlNoHeader("WebServiceCreationTest/InsertRequest.xml");
		response = SimpleHttpClient.postSoapRequest(teiidServer, url, "insertProductInfo", request);
		expected = fileHelper.getXmlNoHeader("WebServiceCreationTest/ResponseSuccessful.xml");
		assertEquals(expected, response);
		
		try {
			response = SimpleHttpClient.postSoapRequest(teiidServer, url, "insertProductInfo", request);
			expected = fileHelper.getXmlNoHeader("WebServiceCreationTest/ResponseFailed.xml");
			assertEquals(expected, response);
			
			request = fileHelper.getXmlNoHeader("WebServiceCreationTest/GetRequest.xml");
			response = SimpleHttpClient.postSoapRequest(teiidServer, url, "getProductInfo", request);
			expected = fileHelper.getXmlNoHeader("WebServiceCreationTest/GetResponse.xml");
			assertEquals(expected, response);
			
			request = fileHelper.getXmlNoHeader("WebServiceCreationTest/DeleteRequest.xml");
			response = SimpleHttpClient.postSoapRequest(teiidServer, url, "deleteProductInfo", request);
			expected = fileHelper.getXmlNoHeader("WebServiceCreationTest/ResponseSuccessful.xml");
			assertEquals(expected, response);
		} catch (IOException e){
			TeiidJDBCHelper jdbcHelper = new TeiidJDBCHelper(teiidServer, vdbName);
			jdbcHelper.executeQueryNoResultSet("DELETE FROM PRODUCTSYMBOLS WHERE INSTR_ID = 'XXX1234';");
			jdbcHelper.executeQueryNoResultSet("DELETE FROM PRODUCTDATA WHERE INSTR_ID = 'XXX1234';");	
		}
		
		response = SimpleHttpClient.postSoapRequest(teiidServer, url, "deleteProductInfo", request);
		expected = fileHelper.getXmlNoHeader("WebServiceCreationTest/ResponseFailed.xml");
		assertEquals(expected, response);
		
		request = fileHelper.getXmlNoHeader("WebServiceCreationTest/GetRequest.xml");
		response = SimpleHttpClient.postSoapRequest(teiidServer, url, "getProductInfo", request);
		expected = fileHelper.getXmlNoHeader("WebServiceCreationTest/ResponseNotFound.xml");
		assertEquals(expected, response);
		
		// 6. Create WAR, deploy, send requests and check responses (None security)
		warName = vdbName + "WarNone";
		createAndDeployWar(warName, vdbName, false);
		
		url = "http://localhost:8080/" + warName + "/" + INTERFACE_NAME + "?wsdl";
		request = fileHelper.getXmlNoHeader("WebServiceCreationTest/GetAllRequest.xml");
		response = SimpleHttpClient.postSoapRequest(url, "getAllProductInfo", request);
		expected = fileHelper.getXmlNoHeader("WebServiceCreationTest/GetAllResponse.xml");
		assertEquals(expected, response);
		
		request = fileHelper.getXmlNoHeader("WebServiceCreationTest/InsertRequest.xml");
		response = SimpleHttpClient.postSoapRequest(url, "insertProductInfo", request);
		expected = fileHelper.getXmlNoHeader("WebServiceCreationTest/ResponseSuccessful.xml");
		assertEquals(expected, response);
		
		try {
			response = SimpleHttpClient.postSoapRequest(url, "insertProductInfo", request);
			expected = fileHelper.getXmlNoHeader("WebServiceCreationTest/ResponseFailed.xml");
			assertEquals(expected, response);
			
			request = fileHelper.getXmlNoHeader("WebServiceCreationTest/GetRequest.xml");
			response = SimpleHttpClient.postSoapRequest(url, "getProductInfo", request);
			expected = fileHelper.getXmlNoHeader("WebServiceCreationTest/GetResponse.xml");
			assertEquals(expected, response);
			
			request = fileHelper.getXmlNoHeader("WebServiceCreationTest/DeleteRequest.xml");
			response = SimpleHttpClient.postSoapRequest(url, "deleteProductInfo", request);
			expected = fileHelper.getXmlNoHeader("WebServiceCreationTest/ResponseSuccessful.xml");
			assertEquals(expected, response);
		} catch (IOException e){
			TeiidJDBCHelper jdbcHelper = new TeiidJDBCHelper(teiidServer, vdbName);
			jdbcHelper.executeQueryNoResultSet("DELETE FROM PRODUCTSYMBOLS WHERE INSTR_ID = 'XXX1234';");
			jdbcHelper.executeQueryNoResultSet("DELETE FROM PRODUCTDATA WHERE INSTR_ID = 'XXX1234';");	
		}
		
		response = SimpleHttpClient.postSoapRequest(url, "deleteProductInfo", request);
		expected = fileHelper.getXmlNoHeader("WebServiceCreationTest/ResponseFailed.xml");
		assertEquals(expected, response);
		
		request = fileHelper.getXmlNoHeader("WebServiceCreationTest/GetRequest.xml");
		response = SimpleHttpClient.postSoapRequest(url, "getProductInfo", request);
		expected = fileHelper.getXmlNoHeader("WebServiceCreationTest/ResponseNotFound.xml");
		assertEquals(expected, response);
	}
	
	@Test
	public void testCreationFromXmlDocument() throws IOException{
		modelExplorer.modelingWebService(true, PROJECT_NAME, "views", "XmlDocuments.xmi", DOCUMENT_PRODUCT)
				.setLocation(PROJECT_NAME, "web_services")
				.setModelName(WS_MODEL.substring(0,10))
				.setInterfaceName(INTERFACE_NAME)
				.setOperationName(OPERATION_GET_ALL)
				.setInputMsgElement(PROJECT_NAME, "schemas", "ProductSchema.xsd", "ProductSchema.xsd", "EmptyInput")
				.setInputMsgName("getAllProductInfo_Input")
				.setOutputMsgName("getAllProductInfo_Output")
				.finish();
		
		new WebServiceModelEditor(WS_MODEL).saveAndClose();
		
		ProblemsViewEx.checkErrors();
		
		String vdbName = "WsXmlVdb";
		createAndDeployVdb(vdbName);

		String warName = vdbName + "War";
		createAndDeployWar(warName, vdbName, true);
		
		String response = SimpleHttpClient.postSoapRequest(teiidServer, 
				"http://localhost:8080/" + warName + "/" + INTERFACE_NAME + "?wsdl", 
				"getAllProductInfo", 
				fileHelper.getXmlNoHeader("WebServiceCreationTest/GetAllRequest.xml"));
		String expected = fileHelper.getXmlNoHeader("WebServiceCreationTest/GetAllResponse.xml");
		assertEquals(expected, response);
	}

	@Test
	public void testCreationFromViewTable() throws IOException{
		modelExplorer.modelingWebService(false, PROJECT_NAME, "views", "ProductsView.xmi", "ProductInfo")
				.setLocation(PROJECT_NAME, "web_services")
				.setModelName(WS_MODEL)
				.setInputSchemaName("InputSchema")
				.setOutputSchemaName("OutputSchema")
				.finish();
		
		new WebServiceModelEditor(WS_MODEL).saveAndClose();
		
		ProblemsViewEx.checkErrors();
		
		String vdbName = "WsViewTableVdb";
		createAndDeployVdb(vdbName);

		String warName = vdbName + "War";
		createAndDeployWar(warName, vdbName, true);
		
		String response = SimpleHttpClient.postSoapRequest(teiidServer, 
				"http://localhost:8080/" + warName + "/ProductsView_" + INTERFACE_NAME + "?wsdl", 
				"getProductInfo", 
				fileHelper.getXmlNoHeader("WebServiceCreationTest/GetRequest2.xml"));
		String expected = fileHelper.getXmlNoHeader("WebServiceCreationTest/GetResponse2.xml");
		assertEquals(expected, response);
	}
	
	@Test@Ignore
	public void testCreationFromViewProcedure(){
		// TODO to be decided
	}
	
	@Test@Ignore
	public void testCreationFromSourceTable(){
		// TODO to be decided
	}
	
	@Test@Ignore
	public void testCreationFromSourceProcedure(){
		// TODO to be decided
	}
	
	private void createAndDeployVdb(String vdbName){
		VdbWizard.openVdbWizard()
				.setLocation(PROJECT_NAME)
				.setName(vdbName)
				.addModel(PROJECT_NAME, "web_services", WS_MODEL)
				.finish();
		modelExplorer.deployVdb(PROJECT_NAME, vdbName);
	}
	
	private void createAndDeployWar(String warName, String vdbName, boolean isHttpBasic){
		modelExplorer.generateWar(true, PROJECT_NAME, vdbName)
				.setVdbJndiName(vdbName)
				.setContextName(warName)
				.setWarFileLocation(modelExplorer.getProjectPath(PROJECT_NAME) + "/others");
		if (isHttpBasic){
			CreateWarDialog.getSoapInstance().setHttpBasicSecurity("teiid-security", "products");
		} else {
			CreateWarDialog.getSoapInstance().setNoneSecurity();
		}		
		CreateWarDialog.getSoapInstance().finish();
		modelExplorer.deployWar(teiidServer, PROJECT_NAME, "others", warName); 
	}

}
