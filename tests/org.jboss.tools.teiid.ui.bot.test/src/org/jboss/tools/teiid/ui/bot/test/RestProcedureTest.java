package org.jboss.tools.teiid.ui.bot.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;

import org.jboss.reddeer.common.wait.AbstractWait;
import org.jboss.reddeer.common.wait.TimePeriod;
import org.jboss.reddeer.core.exception.CoreLayerException;
import org.jboss.reddeer.junit.requirement.inject.InjectRequirement;
import org.jboss.reddeer.junit.runner.RedDeerSuite;
import org.jboss.reddeer.requirements.openperspective.OpenPerspectiveRequirement.OpenPerspective;
import org.jboss.reddeer.requirements.server.ServerReqState;
import org.jboss.reddeer.swt.impl.button.PushButton;
import org.jboss.tools.teiid.reddeer.connection.ConnectionProfileConstants;
import org.jboss.tools.teiid.reddeer.connection.ResourceFileHelper;
import org.jboss.tools.teiid.reddeer.connection.SimpleHttpClient;
import org.jboss.tools.teiid.reddeer.connection.TeiidJDBCHelper;
import org.jboss.tools.teiid.reddeer.dialog.GenerateRestProcedureDialog;
import org.jboss.tools.teiid.reddeer.dialog.ProcedureViewDialog;
import org.jboss.tools.teiid.reddeer.editor.RelationalModelEditor;
import org.jboss.tools.teiid.reddeer.editor.TableEditor;
import org.jboss.tools.teiid.reddeer.editor.VdbEditor;
import org.jboss.tools.teiid.reddeer.perspective.TeiidPerspective;
import org.jboss.tools.teiid.reddeer.requirement.TeiidServerRequirement;
import org.jboss.tools.teiid.reddeer.requirement.TeiidServerRequirement.TeiidServer;
import org.jboss.tools.teiid.reddeer.view.ModelExplorer;
import org.jboss.tools.teiid.reddeer.view.ProblemsViewEx;
import org.jboss.tools.teiid.reddeer.wizard.newWizard.NewProcedureWizard;
import org.jboss.tools.teiid.reddeer.wizard.newWizard.VdbWizard;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

/**
 * @author skaleta
 * tested features:
 * - generate procedure from relational source and view model
 *  	- checks procedure's rest-related attributes
 * 		- creates and deploys REST WAR and test it (None, HTTPBasic security)
 * 		- tests Swagger
 * 		- verifies that's not possible to add procedure into source model
 * - create insert procedure and test it
 * 		- XML request
 * 		- JSON request
 * - create procedure with GET method and procedure with POST method - available on same URI
 * - test automatic WAR generation
 */
@RunWith(RedDeerSuite.class)
@OpenPerspective(TeiidPerspective.class)
@TeiidServer(state = ServerReqState.RUNNING, connectionProfiles = {ConnectionProfileConstants.ORACLE_11G_PARTS_SUPPLIER})
public class RestProcedureTest {
	private static final String PROJECT_NAME = "RestProcedureProject";
	private static final String VIEW_MODEL = "PartsView.xmi";
	private static final String SOURCE_MODEL = "Parts.xmi";
	
	@InjectRequirement
	private static TeiidServerRequirement teiidServer;

	private ModelExplorer modelExplorer;
	private ResourceFileHelper fileHelper;
	
	@Before
	public void importProject() {
		fileHelper = new ResourceFileHelper();
		modelExplorer = new ModelExplorer();
		modelExplorer.importProject(PROJECT_NAME);
		modelExplorer.refreshProject(PROJECT_NAME);
		modelExplorer.changeConnectionProfile(ConnectionProfileConstants.ORACLE_11G_PARTS_SUPPLIER, PROJECT_NAME, SOURCE_MODEL);		
	}

	@After
	public void cleanUp() {
		modelExplorer.deleteAllProjectsSafely();
	}
	
	@Test
	public void testAutomaticWarGeneration() throws IOException{
		modelExplorer.addChildToModelItem(ModelExplorer.ChildType.PROCEDURE, PROJECT_NAME, VIEW_MODEL);
		NewProcedureWizard.createViewProcedure()
				.setName("GetPartGet")
				.toggleRest(true)
				.setRestMethod(ProcedureViewDialog.RestMethod.GET)
				.setRestUri("part/{id}")
				.addParameter("id", "string", "4", "IN")
				.setTransformationSql(fileHelper.getSql("RestProcedureTest/getProcedureGet"))
				.finish();
		new RelationalModelEditor(VIEW_MODEL).save();
		ProblemsViewEx.checkErrors();
		
		String vdb = "RestAutoWarVdb";
		VdbWizard.openVdbWizard()
				.setLocation(PROJECT_NAME)
				.setName(vdb)
				.addModel(PROJECT_NAME, VIEW_MODEL)
				.finish();
		
		VdbEditor.getInstance(vdb).setGenerateRestWar(true).save();		
		
		modelExplorer.deployVdb(PROJECT_NAME, vdb);
		AbstractWait.sleep(TimePeriod.getCustom(5));
		
		String response = new SimpleHttpClient("http://localhost:8080/" + vdb + "_1/PartsView/part/P305")
				.setBasicAuth(teiidServer.getServerConfig().getServerBase().getProperty("teiidUser"),
						teiidServer.getServerConfig().getServerBase().getProperty("teiidPassword"))
				.get();
		assertEquals(fileHelper.getXmlNoHeader("RestProcedureTest/getResponse"), response);
	}

	@Test
	public void testCreateInsertProcedure() throws IOException, SQLException {
		modelExplorer.addChildToModelItem(ModelExplorer.ChildType.PROCEDURE, PROJECT_NAME, VIEW_MODEL);
		NewProcedureWizard.createViewProcedure()
				.setName("AddPart")
				.toggleRest(true)
				.setRestMethod(ProcedureViewDialog.RestMethod.POST)
				.setRestUri("part/")
				.addParameter("id", "string", "4", "IN")
				.addParameter("name", "string", "255", "IN")
				.addParameter("color", "string", "30", "IN")
				.addParameter("weight", "string", "255", "IN")
				.setTransformationSql(fileHelper.getSql("RestProcedureTest/insertProcedure"))
				.finish();
		new RelationalModelEditor(VIEW_MODEL).save();
		ProblemsViewEx.checkErrors();
		
		String vdb = "RestInsertVdb";
		VdbWizard.openVdbWizard()
				.setLocation(PROJECT_NAME)
				.setName(vdb)
				.addModel(PROJECT_NAME, VIEW_MODEL)
				.finish();
		modelExplorer.deployVdb(PROJECT_NAME, vdb);

		String war = vdb + "War";
		modelExplorer.generateWar(false, PROJECT_NAME, vdb)
				.setVdbJndiName(vdb)
				.setContextName(war)
				.setWarFileLocation(modelExplorer.getProjectPath(PROJECT_NAME))
				.setHttpBasicSecurity("teiid-security", "rest")
				.finish();		
		modelExplorer.deployWar(teiidServer, PROJECT_NAME, war);
		
		try {
			String response = new SimpleHttpClient("http://localhost:8080/" + war + "/PartsView/part/")
					.setBasicAuth(teiidServer.getServerConfig().getServerBase().getProperty("teiidUser"),
							teiidServer.getServerConfig().getServerBase().getProperty("teiidPassword"))
					.addHeader("Content-Type", "application/xml")
					.addHeader("Accept", "application/xml; charset=UTF-8")
					.post(fileHelper.getXmlNoHeader("RestProcedureTest/insertRequest"));
			assertEquals("<response>Operation Successful!</response>", response);
			
			response = new SimpleHttpClient("http://localhost:8080/" + war + "/PartsView/json/part/")
					.setBasicAuth(teiidServer.getServerConfig().getServerBase().getProperty("teiidUser"),
							teiidServer.getServerConfig().getServerBase().getProperty("teiidPassword"))
					.addHeader("Content-Type", "application/json")
					.addHeader("Accept", "application/json; charset=UTF-8")
					.post(fileHelper.getFlatFile("RestProcedureTest/insertRequest.json"));
			assertEquals("{\"response\": \"Operation Successful!\"}", response);
		} finally {
			new TeiidJDBCHelper(teiidServer, vdb).executeQueryNoResultSet("DELETE FROM Parts.PARTS WHERE PART_ID LIKE 'RPT%'");
		}
	}
	
	@Test 
	public void testCreateProcedureWithGetPostMethods() throws IOException, SQLException{
		modelExplorer.addChildToModelItem(ModelExplorer.ChildType.PROCEDURE, PROJECT_NAME, VIEW_MODEL);
		NewProcedureWizard.createViewProcedure()
				.setName("GetPartPost")
				.toggleRest(true)
				.setRestMethod(ProcedureViewDialog.RestMethod.POST)
				.setRestUri("part/{id}")
				.addParameter("id", "string", "4", "IN")
				.setTransformationSql(fileHelper.getSql("RestProcedureTest/getProcedurePost"))
				.finish();
		modelExplorer.addChildToModelItem(ModelExplorer.ChildType.PROCEDURE, PROJECT_NAME, VIEW_MODEL);
		NewProcedureWizard.createViewProcedure()
				.setName("GetPartGet")
				.toggleRest(true)
				.setRestMethod(ProcedureViewDialog.RestMethod.GET)
				.setRestUri("part/{id}")
				.addParameter("id", "string", "4", "IN")
				.setTransformationSql(fileHelper.getSql("RestProcedureTest/getProcedureGet"))
				.finish();
		new RelationalModelEditor(VIEW_MODEL).save();
		ProblemsViewEx.checkErrors();
		
		String vdb = "RestGetPostVdb";
		VdbWizard.openVdbWizard()
				.setLocation(PROJECT_NAME)
				.setName(vdb)
				.addModel(PROJECT_NAME, VIEW_MODEL)
				.finish();
		modelExplorer.deployVdb(PROJECT_NAME, vdb);

		String war = vdb + "War";
		modelExplorer.generateWar(false, PROJECT_NAME, vdb)
				.setVdbJndiName(vdb)
				.setContextName(war)
				.setWarFileLocation(modelExplorer.getProjectPath(PROJECT_NAME))
				.setHttpBasicSecurity("teiid-security", "rest")
				.finish();		
		modelExplorer.deployWar(teiidServer, PROJECT_NAME, war);
		
		String response = new SimpleHttpClient("http://localhost:8080/" + war + "/PartsView/part/P305")
				.setBasicAuth(teiidServer.getServerConfig().getServerBase().getProperty("teiidUser"),
						teiidServer.getServerConfig().getServerBase().getProperty("teiidPassword"))
				.post("");
		assertEquals(fileHelper.getXmlNoHeader("RestProcedureTest/getResponse"), response);
		
		response = new SimpleHttpClient("http://localhost:8080/" + war + "/PartsView/part/P305")
				.setBasicAuth(teiidServer.getServerConfig().getServerBase().getProperty("teiidUser"),
						teiidServer.getServerConfig().getServerBase().getProperty("teiidPassword"))
				.get();
		assertEquals(fileHelper.getXmlNoHeader("RestProcedureTest/getResponse"), response);
	}
	
	@Test
	public void testGenerateProceduresFromSourceModel() throws IOException {
		String targetModel = "ProcedureView.xmi";
		modelExplorer.modelingRestProcedure(PROJECT_NAME, SOURCE_MODEL)
				.setTargetModelLocation(PROJECT_NAME)
				.setNewTargetModel(targetModel)
				.selectTables("PARTS", "SUPPLIER")
				.finish();
		
		ProblemsViewEx.checkErrors();
		
		checkGeneratedProcedures(targetModel, "RestSourceVdb");
	}

	@Test
	public void testGenerateProceduresFromViewModel() throws IOException {
		modelExplorer.modelingRestProcedure(PROJECT_NAME, VIEW_MODEL)
				.setTargetModelLocation(PROJECT_NAME)
				.selectTables("PARTS", "SUPPLIER")
				.finish();

		ProblemsViewEx.checkErrors();
			
		checkGeneratedProcedures(VIEW_MODEL, "RestViewVdb");
	}

	@Test
	public void testCannotSetProcedureTargetToSourceModel() {
		GenerateRestProcedureDialog dialog = modelExplorer.modelingRestProcedure(PROJECT_NAME, SOURCE_MODEL);
		dialog.setTargetModelLocation(PROJECT_NAME);
		try {
			dialog.setExistingTargetModel(PROJECT_NAME, SOURCE_MODEL);
			fail("Should not allow selecting a source model as the target");
		} catch (CoreLayerException ex) {
			new PushButton("Cancel").click();
			dialog.cancel();
		}
		
		dialog = modelExplorer.modelingRestProcedure(PROJECT_NAME, SOURCE_MODEL);
		dialog.setTargetModelLocation(PROJECT_NAME);
		try {
			dialog.setNewTargetModel(SOURCE_MODEL)
					.selectTables("PARTS", "SUPPLIER")
					.finish();
			fail("Should not allow typing name of a source model with xmi extension as the target");
		} catch (Exception ex) {
			new PushButton("Cancel").click();
		}
		
		dialog = modelExplorer.modelingRestProcedure(PROJECT_NAME, SOURCE_MODEL);
		dialog.setTargetModelLocation(PROJECT_NAME);
		try {
			dialog.setNewTargetModel(SOURCE_MODEL.substring(0,5))
					.selectTables("PARTS", "SUPPLIER")
					.finish();
			fail("Should not allow typing name of a source model without xmi extension as the target");
		} catch (Exception ex) {
			new PushButton("Cancel").click();
		}
	}

	private void checkGeneratedProcedures(String modelName, String vdb) throws IOException {
		TableEditor editor = new RelationalModelEditor(modelName).openTableEditor();
		editor.openTab("Procedures");
		
		assertEquals("Wrong REST URI", "PARTS/{pk_PART_ID_in}", editor.getCellText(1, "PARTSRestProc", "REST:URI"));
		assertEquals("Wrong REST Method", "GET", editor.getCellText(1, "PARTSRestProc", "REST:Rest Method"));
		assertEquals("Wrong REST URI", "SUPPLIER/{pk_SUPPLIER_ID_in}", editor.getCellText(1, "SUPPLIERRestProc", "REST:URI"));
		assertEquals("Wrong REST Method", "GET", editor.getCellText(1, "SUPPLIERRestProc", "REST:Rest Method"));

		VdbWizard.openVdbWizard()
				.setLocation(PROJECT_NAME)
				.setName(vdb)
				.addModel(PROJECT_NAME, modelName)
				.finish();
		modelExplorer.deployVdb(PROJECT_NAME, vdb);
		
		String war = vdb + "HttpBasicWar";
		modelExplorer.generateWar(false, PROJECT_NAME, vdb)
				.setVdbJndiName(vdb)
				.setContextName(war)
				.setWarFileLocation(modelExplorer.getProjectPath(PROJECT_NAME))
				.setHttpBasicSecurity("teiid-security", "rest")
				.finish();		
		modelExplorer.deployWar(teiidServer, PROJECT_NAME, war);
		
		checkSwagger(modelName.replaceAll(".xmi", ""), war);
		 
		checkWarHttpBasicSecurity("http://localhost:8080/" + war + '/' + modelName.replaceAll(".xmi", "") + "/PARTS/P305",
				fileHelper.getXmlNoHeader("RestProcedureTest/partsResponse"));
		checkWarHttpBasicSecurity("http://localhost:8080/" + war + '/' + modelName.replaceAll(".xmi", "") + "/SUPPLIER/S108",
				fileHelper.getXmlNoHeader("RestProcedureTest/supplierResponse"));
		try {
			checkWarNoneSecurity("http://localhost:8080/" + war + '/' + modelName.replaceAll(".xmi", "") + "/PARTS/P305",
					fileHelper.getXmlNoHeader("RestProcedureTest/partsResponse"));
		} catch (IOException e){
			// expected
		}
		
		war = vdb + "NoneWar";
		modelExplorer.generateWar(false, PROJECT_NAME, vdb)
				.setVdbJndiName(vdb)
				.setContextName(war)
				.setWarFileLocation(modelExplorer.getProjectPath(PROJECT_NAME))
				.setNoneSecurity()
				.finish();		
		modelExplorer.deployWar(teiidServer, PROJECT_NAME, war);

		checkWarNoneSecurity("http://localhost:8080/" + war + '/' + modelName.replaceAll(".xmi", "") + "/PARTS/P305",
				fileHelper.getXmlNoHeader("RestProcedureTest/partsResponse"));
		checkWarNoneSecurity("http://localhost:8080/" + war + '/' + modelName.replaceAll(".xmi", "") + "/SUPPLIER/S108",
				fileHelper.getXmlNoHeader("RestProcedureTest/supplierResponse"));
	}
	
	private void checkWarHttpBasicSecurity(String uri, String expected) throws IOException {
		String response = new SimpleHttpClient(uri)
				.setBasicAuth(teiidServer.getServerConfig().getServerBase().getProperty("teiidUser"),
						teiidServer.getServerConfig().getServerBase().getProperty("teiidPassword"))
				.get();
		assertEquals(expected, response);
	}
	
	
	private void checkWarNoneSecurity(String uri, String expected) throws IOException{
		String response = new SimpleHttpClient(uri)
				.get();
		assertEquals(expected, response);
	}

	/**
	 * Checks if the main page is available
	 * Checks that paths to procedures are correct
	 */
	private void checkSwagger(String modelName, String war) throws IOException {
		new SimpleHttpClient("http://localhost:8080/" + war)
				.setBasicAuth(teiidServer.getServerConfig().getServerBase().getProperty("teiidUser"),
						teiidServer.getServerConfig().getServerBase().getProperty("teiidPassword"))
				.get();
		
		ArrayList<String> allowedPaths = new ArrayList<String>();
		allowedPaths.add("\"/"+ modelName +"/PARTS/{pk_PART_ID_in}\"");
		allowedPaths.add("\"/"+ modelName +"/SUPPLIER/{pk_SUPPLIER_ID_in}\"");
		allowedPaths.add("\"/"+ modelName +"/json/SUPPLIER/{pk_SUPPLIER_ID_in}\"");
		allowedPaths.add("\"/"+ modelName +"/json/PARTS/{pk_PART_ID_in}\"");

		String response = new SimpleHttpClient("http://localhost:8080/" + war + "/api-docs/" + modelName)
				.setBasicAuth(teiidServer.getServerConfig().getServerBase().getProperty("teiidUser"),
						teiidServer.getServerConfig().getServerBase().getProperty("teiidPassword"))
				.get();
		
		JsonObject wholeJson = new JsonParser().parse(response).getAsJsonObject();
		JsonArray apisArray = wholeJson.get("apis").getAsJsonArray();

		for (int i = 0; i < apisArray.size(); i++) {
			JsonElement element = apisArray.get(i);
			String path = element.getAsJsonObject().get("path").toString();
			assertTrue(allowedPaths.contains(path));
			allowedPaths.remove(path);
		}
	}

}
