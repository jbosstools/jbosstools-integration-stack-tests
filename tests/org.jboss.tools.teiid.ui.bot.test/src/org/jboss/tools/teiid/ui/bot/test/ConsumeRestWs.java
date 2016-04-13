package org.jboss.tools.teiid.ui.bot.test;

import static org.junit.Assert.assertEquals;

import java.util.Properties;

import org.jboss.reddeer.eclipse.wst.server.ui.view.ServersView;
import org.jboss.reddeer.junit.requirement.inject.InjectRequirement;
import org.jboss.reddeer.junit.runner.RedDeerSuite;
import org.jboss.reddeer.requirements.openperspective.OpenPerspectiveRequirement.OpenPerspective;
import org.jboss.reddeer.requirements.server.ServerReqState;
import org.jboss.reddeer.workbench.handler.EditorHandler;
import org.jboss.tools.teiid.reddeer.connection.TeiidJDBCHelper;
import org.jboss.tools.teiid.reddeer.manager.ConnectionProfileManager;
import org.jboss.tools.teiid.reddeer.manager.ModelExplorerManager;
import org.jboss.tools.teiid.reddeer.manager.VDBManager;
import org.jboss.tools.teiid.reddeer.perspective.TeiidPerspective;
import org.jboss.tools.teiid.reddeer.requirement.TeiidServerRequirement;
import org.jboss.tools.teiid.reddeer.requirement.TeiidServerRequirement.TeiidServer;
import org.jboss.tools.teiid.reddeer.view.ModelExplorer;
import org.jboss.tools.teiid.reddeer.view.ServersViewExt;
import org.jboss.tools.teiid.reddeer.wizard.RestImportWizard;
import org.jboss.tools.teiid.reddeer.wizard.RestProfileWizard;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * Test for consuming REST WS - XML and JSON
 * 
 * @author mmakovy
 * 
 */

@RunWith(RedDeerSuite.class)
@OpenPerspective(TeiidPerspective.class)
@TeiidServer(state = ServerReqState.RUNNING)
public class ConsumeRestWs {

	private static final String PROJECT_NAME = "REST";
	private static final String XML_PROFILE_NAME = "REST_XML";
	private static final String JSON_PROFILE_NAME = "REST_JSON";
	private static final String XML_PROFILE_NAME_DIGEST = "REST_XML_DIGEST";
	private static final String JSON_PROFILE_NAME_DIGEST = "REST_JSON_DIGEST";
	private static final String PROCEDURE_NAME = "getProgrammes";
	private static final String VDBXML = "RESTXML";
	private static final String VDBJSON = "RESTJSON";
	private static final String VDBXMLDIGEST = "RESTXMLDIGEST";
	private static final String VDBJSONDIGEST = "RESTJSONDIGEST";
	private static final String SOURCE_MODEL_XML = "RestXMLSource";
	private static final String VIEW_MODEL_XML = "RestXMLView";
	private static final String SOURCE_MODEL_JSON = "RestJSONSource";
	private static final String VIEW_MODEL_JSON = "RestJSONView";
	private static final String SOURCE_MODEL_XML_DIGEST = "RestXMLSourceDigest";
	private static final String VIEW_MODEL_XML_DIGEST = "RestXMLViewDigest";
	private static final String SOURCE_MODEL_JSON_DIGEST = "RestJSONSourceDigest";
	private static final String VIEW_MODEL_JSON_DIGEST = "RestJSONViewDigest";

	@InjectRequirement
	private static TeiidServerRequirement teiidServer;

	@BeforeClass
	public static void before() {
		new ModelExplorerManager().createProject(PROJECT_NAME, false);
	}

	@Before
	public void openPerspective() {
		TeiidPerspective.getInstance();
	}

	@After
	public void closeAll() {
		EditorHandler.getInstance().closeAll(false);
	}

	@Test
	public void testXml() {

		Properties restCpXml = new Properties();
		restCpXml.setProperty("connectionUrl", "http://ws-dvirt.rhcloud.com/dv-test-ws/rest/xml");
		restCpXml.setProperty("type", "xml");

		new ConnectionProfileManager().createCPREST(XML_PROFILE_NAME, restCpXml);

		RestImportWizard restWizardXML = new RestImportWizard();

		restWizardXML.setProfileName(XML_PROFILE_NAME);
		restWizardXML.setProjectName(PROJECT_NAME);
		restWizardXML.setSourceModelName(SOURCE_MODEL_XML);
		restWizardXML.setViewModelName(VIEW_MODEL_XML);
		restWizardXML.setProcedureName(PROCEDURE_NAME);
		restWizardXML.setRootPath("schedule/day/broadcasts/broadcast");

		restWizardXML.addColumn("pid");
		restWizardXML.addColumn("start");
		restWizardXML.addColumn("end");
		restWizardXML.addColumn("programme/title");

		restWizardXML.execute();

		new VDBManager().createVDB(PROJECT_NAME, VDBXML);
		new VDBManager().addModelsToVDB(PROJECT_NAME, VDBXML, new String[] { SOURCE_MODEL_XML, VIEW_MODEL_XML });

		new ServersView().open();
		new ServersViewExt().refreshServer(new ServersView().getServers().get(0).getLabel().getName());

		new ModelExplorer().executeVDB(PROJECT_NAME, VDBXML + ".vdb");

		TeiidJDBCHelper jdbchelper = new TeiidJDBCHelper(teiidServer, VDBXML);
		assertEquals(16, jdbchelper.getNumberOfResults("exec " + VIEW_MODEL_XML + "." + PROCEDURE_NAME + "()"));

	}

	@Test
	public void testJson() {

		Properties restCpJson = new Properties();
		restCpJson.setProperty("connectionUrl", "http://ws-dvirt.rhcloud.com/dv-test-ws/rest/json");
		restCpJson.setProperty("type", "json");

		new ConnectionProfileManager().createCPREST(JSON_PROFILE_NAME, restCpJson);

		RestImportWizard restWizardJson = new RestImportWizard();

		restWizardJson.setProfileName(JSON_PROFILE_NAME);
		restWizardJson.setProjectName(PROJECT_NAME);
		restWizardJson.setSourceModelName(SOURCE_MODEL_JSON);
		restWizardJson.setViewModelName(VIEW_MODEL_JSON);
		restWizardJson.setProcedureName(PROCEDURE_NAME);
		restWizardJson.setRootPath("response/schedule/day/broadcasts");

		restWizardJson.addColumn("pid");
		restWizardJson.addColumn("start");
		restWizardJson.addColumn("end");
		restWizardJson.addColumn("programme/title");

		restWizardJson.execute();

		new VDBManager().createVDB(PROJECT_NAME, VDBJSON);
		new VDBManager().addModelsToVDB(PROJECT_NAME, VDBJSON, new String[] { SOURCE_MODEL_JSON, VIEW_MODEL_JSON });

		new ServersView().open();
		new ServersViewExt().refreshServer(new ServersView().getServers().get(0).getLabel().getName());

		new ModelExplorer().executeVDB(PROJECT_NAME, VDBJSON + ".vdb");

		TeiidJDBCHelper jdbchelper = new TeiidJDBCHelper(teiidServer, VDBJSON);
		assertEquals(16, jdbchelper.getNumberOfResults("exec " + VIEW_MODEL_JSON + "." + PROCEDURE_NAME + "()"));

	}
	
	@Test
	public void testXmlHttpDigest() {

		Properties restCpXml = new Properties();
		restCpXml.setProperty("connectionUrl", "http://ws-dvirt.rhcloud.com/dv-test-ws-digest/rest/xml");
		restCpXml.setProperty("type", "xml");
		restCpXml.setProperty("authType", RestProfileWizard.AUTH_TYPE_DIGEST);
		restCpXml.setProperty("username", "digest");
		restCpXml.setProperty("password", "digest");

		new ConnectionProfileManager().createCPREST(XML_PROFILE_NAME_DIGEST, restCpXml);

		RestImportWizard restWizardXML = new RestImportWizard();

		restWizardXML.setProfileName(XML_PROFILE_NAME_DIGEST);
		restWizardXML.setProjectName(PROJECT_NAME);
		restWizardXML.setSourceModelName(SOURCE_MODEL_XML_DIGEST);
		restWizardXML.setViewModelName(VIEW_MODEL_XML_DIGEST);
		restWizardXML.setProcedureName(PROCEDURE_NAME);
		restWizardXML.setRootPath("schedule/day/broadcasts/broadcast");

		restWizardXML.addColumn("pid");
		restWizardXML.addColumn("start");
		restWizardXML.addColumn("end");
		restWizardXML.addColumn("programme/title");

		restWizardXML.execute();

		new VDBManager().createVDB(PROJECT_NAME, VDBXMLDIGEST);
		new VDBManager().addModelsToVDB(PROJECT_NAME, VDBXMLDIGEST, new String[] { SOURCE_MODEL_XML_DIGEST, VIEW_MODEL_XML_DIGEST });

		new ServersView().open();
		new ServersViewExt().refreshServer(new ServersView().getServers().get(0).getLabel().getName());

		new ModelExplorer().executeVDB(PROJECT_NAME, VDBXMLDIGEST + ".vdb");

		TeiidJDBCHelper jdbchelper = new TeiidJDBCHelper(teiidServer, VDBXMLDIGEST);
		assertEquals(16, jdbchelper.getNumberOfResults("exec " + VIEW_MODEL_XML_DIGEST + "." + PROCEDURE_NAME + "()"));

	}
	
	@Test
	public void testHttpDigestJson() {

		Properties restCpJson = new Properties();
		restCpJson.setProperty("connectionUrl", "http://ws-dvirt.rhcloud.com/dv-test-ws-digest/rest/json");
		restCpJson.setProperty("type", "json");
		restCpJson.setProperty("authType", RestProfileWizard.AUTH_TYPE_DIGEST);
		restCpJson.setProperty("username", "digest");
		restCpJson.setProperty("password", "digest");

		new ConnectionProfileManager().createCPREST(JSON_PROFILE_NAME_DIGEST, restCpJson);

		RestImportWizard restWizardJson = new RestImportWizard();

		restWizardJson.setProfileName(JSON_PROFILE_NAME_DIGEST);
		restWizardJson.setProjectName(PROJECT_NAME);
		restWizardJson.setSourceModelName(SOURCE_MODEL_JSON_DIGEST);
		restWizardJson.setViewModelName(VIEW_MODEL_JSON_DIGEST);
		restWizardJson.setProcedureName(PROCEDURE_NAME);
		restWizardJson.setRootPath("response/schedule/day/broadcasts");

		restWizardJson.addColumn("pid");
		restWizardJson.addColumn("start");
		restWizardJson.addColumn("end");
		restWizardJson.addColumn("programme/title");

		restWizardJson.execute();

		new VDBManager().createVDB(PROJECT_NAME, VDBJSONDIGEST);
		new VDBManager().addModelsToVDB(PROJECT_NAME, VDBJSONDIGEST, new String[] { SOURCE_MODEL_JSON_DIGEST, VIEW_MODEL_JSON_DIGEST });

		new ServersView().open();
		new ServersViewExt().refreshServer(new ServersView().getServers().get(0).getLabel().getName());

		new ModelExplorer().executeVDB(PROJECT_NAME, VDBJSONDIGEST + ".vdb");

		TeiidJDBCHelper jdbchelper = new TeiidJDBCHelper(teiidServer, VDBJSONDIGEST);
		assertEquals(16, jdbchelper.getNumberOfResults("exec " + VIEW_MODEL_JSON_DIGEST + "." + PROCEDURE_NAME + "()"));

	}

}
