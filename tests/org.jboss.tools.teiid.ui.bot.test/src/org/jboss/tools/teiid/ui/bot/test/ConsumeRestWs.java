package org.jboss.tools.teiid.ui.bot.test;

import static org.junit.Assert.assertEquals;

import java.util.Properties;

import org.jboss.reddeer.eclipse.wst.server.ui.view.ServersView;
import org.jboss.reddeer.junit.requirement.inject.InjectRequirement;
import org.jboss.reddeer.junit.runner.RedDeerSuite;
import org.jboss.reddeer.requirements.openperspective.OpenPerspectiveRequirement.OpenPerspective;
import org.jboss.reddeer.requirements.server.ServerReqState;
import org.jboss.tools.teiid.reddeer.connection.ConnectionProfileHelper;
import org.jboss.tools.teiid.reddeer.connection.TeiidJDBCHelper;
import org.jboss.tools.teiid.reddeer.perspective.TeiidPerspective;
import org.jboss.tools.teiid.reddeer.requirement.TeiidServerRequirement;
import org.jboss.tools.teiid.reddeer.requirement.TeiidServerRequirement.TeiidServer;
import org.jboss.tools.teiid.reddeer.view.ModelExplorer;
import org.jboss.tools.teiid.reddeer.view.ServersViewExt;
import org.jboss.tools.teiid.reddeer.wizard.connectionProfiles.RestProfileWizard;
import org.jboss.tools.teiid.reddeer.wizard.imports.RestImportWizard;
import org.jboss.tools.teiid.reddeer.wizard.newWizard.VdbWizard;
import org.junit.After;
import org.junit.Before;
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
	
	private ModelExplorer modelExplorer;

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
	public void testXml() {
		Properties restCpXml = new Properties();
		restCpXml.setProperty("connectionUrl", "http://ws-dvirt.rhcloud.com/dv-test-ws/rest/xml");
		restCpXml.setProperty("type", "xml");

		new ConnectionProfileHelper().createCpRest(XML_PROFILE_NAME, restCpXml);

		RestImportWizard.openWizard()
				.setProfileName(XML_PROFILE_NAME)
				.nextPage()
				.setProject(PROJECT_NAME)
				.setSourceModelName(SOURCE_MODEL_XML)
				.setViewModelName(VIEW_MODEL_XML)
				.setProcedureName(PROCEDURE_NAME)
				.nextPage()
				.setJndiName("restXMLSource")
				.nextPage()
				.setRootPath("schedule/day/broadcasts/broadcast")
				.setColumns("pid","start","end","programme/title")
				.finish();

		VdbWizard.openVdbWizard()
				.setLocation(PROJECT_NAME)
				.setName(VDBXML)
				.addModel(PROJECT_NAME, SOURCE_MODEL_XML + ".xmi")
				.addModel(PROJECT_NAME, VIEW_MODEL_XML + ".xmi")
				.finish();

		new ServersView().open();
		new ServersViewExt().refreshServer(new ServersView().getServers().get(0).getLabel().getName());

		modelExplorer.deployVdb(PROJECT_NAME, VDBXML);

		TeiidJDBCHelper jdbchelper = new TeiidJDBCHelper(teiidServer, VDBXML);
		assertEquals(16, jdbchelper.getNumberOfResults("exec " + VIEW_MODEL_XML + "." + PROCEDURE_NAME + "()"));
	}

	@Test
	public void testJson() {
		Properties restCpJson = new Properties();
		restCpJson.setProperty("connectionUrl", "http://ws-dvirt.rhcloud.com/dv-test-ws/rest/json");
		restCpJson.setProperty("type", "json");

		new ConnectionProfileHelper().createCpRest(JSON_PROFILE_NAME, restCpJson);

		RestImportWizard.openWizard()
				.setProfileName(JSON_PROFILE_NAME)
				.nextPage()
				.setProject(PROJECT_NAME)
				.setSourceModelName(SOURCE_MODEL_JSON)
				.setViewModelName(VIEW_MODEL_JSON)
				.setProcedureName(PROCEDURE_NAME)
				.nextPage()
				.setJndiName("restJsonSource")
				.nextPage()
				.setRootPath("response/schedule/day/broadcasts")
				.setColumns("pid","start","end","programme/title")
				.finish();
		
		VdbWizard.openVdbWizard()
				.setLocation(PROJECT_NAME)
				.setName(VDBJSON)
				.addModel(PROJECT_NAME, SOURCE_MODEL_JSON + ".xmi")
				.addModel(PROJECT_NAME, VIEW_MODEL_JSON + ".xmi")
				.finish();

		new ServersView().open();
		new ServersViewExt().refreshServer(new ServersView().getServers().get(0).getLabel().getName());

		modelExplorer.deployVdb(PROJECT_NAME, VDBJSON);

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

		new ConnectionProfileHelper().createCpRest(XML_PROFILE_NAME_DIGEST, restCpXml);

		RestImportWizard.openWizard()
				.setProfileName(XML_PROFILE_NAME_DIGEST)
				.nextPage()
				.setProject(PROJECT_NAME)
				.setSourceModelName(SOURCE_MODEL_XML_DIGEST)
				.setViewModelName(VIEW_MODEL_XML_DIGEST)
				.setProcedureName(PROCEDURE_NAME)
				.nextPage()
				.setJndiName("restXMLDigestSource")
				.nextPage()
				.setRootPath("schedule/day/broadcasts/broadcast")
				.setColumns("pid","start","end","programme/title")
				.finish();

		VdbWizard.openVdbWizard()
				.setLocation(PROJECT_NAME)
				.setName(VDBXMLDIGEST)
				.addModel(PROJECT_NAME, SOURCE_MODEL_XML_DIGEST + ".xmi")
				.addModel(PROJECT_NAME, VIEW_MODEL_XML_DIGEST + ".xmi")
				.finish();

		new ServersView().open();
		new ServersViewExt().refreshServer(new ServersView().getServers().get(0).getLabel().getName());

		modelExplorer.deployVdb(PROJECT_NAME, VDBXMLDIGEST);

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

		new ConnectionProfileHelper().createCpRest(JSON_PROFILE_NAME_DIGEST, restCpJson);

		RestImportWizard.openWizard()
				.setProfileName(JSON_PROFILE_NAME_DIGEST)
				.nextPage()
				.setProject(PROJECT_NAME)
				.setSourceModelName(SOURCE_MODEL_JSON_DIGEST)
				.setViewModelName(VIEW_MODEL_JSON_DIGEST)
				.setProcedureName(PROCEDURE_NAME)
				.nextPage()
				.setJndiName("restJsonDigestSource")
				.nextPage()
				.setRootPath("response/schedule/day/broadcasts")
				.setColumns("pid","start","end","programme/title")
				.finish();

		VdbWizard.openVdbWizard()
				.setLocation(PROJECT_NAME)
				.setName(VDBJSONDIGEST)
				.addModel(PROJECT_NAME, SOURCE_MODEL_JSON_DIGEST + ".xmi")
				.addModel(PROJECT_NAME, VIEW_MODEL_JSON_DIGEST + ".xmi")
				.finish();

		new ServersView().open();
		new ServersViewExt().refreshServer(new ServersView().getServers().get(0).getLabel().getName());

		modelExplorer.deployVdb(PROJECT_NAME, VDBJSONDIGEST);

		TeiidJDBCHelper jdbchelper = new TeiidJDBCHelper(teiidServer, VDBJSONDIGEST);
		assertEquals(16, jdbchelper.getNumberOfResults("exec " + VIEW_MODEL_JSON_DIGEST + "." + PROCEDURE_NAME + "()"));
	}

}
