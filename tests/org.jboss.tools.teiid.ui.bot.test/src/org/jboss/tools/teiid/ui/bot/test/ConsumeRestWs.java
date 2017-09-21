package org.jboss.tools.teiid.ui.bot.test;

import static org.junit.Assert.assertEquals;

import org.eclipse.reddeer.junit.requirement.inject.InjectRequirement;
import org.eclipse.reddeer.junit.runner.RedDeerSuite;
import org.eclipse.reddeer.requirements.openperspective.OpenPerspectiveRequirement.OpenPerspective;
import org.eclipse.reddeer.requirements.server.ServerRequirementState;
import org.jboss.tools.teiid.reddeer.connection.TeiidJDBCHelper;
import org.jboss.tools.teiid.reddeer.perspective.TeiidPerspective;
import org.jboss.tools.teiid.reddeer.requirement.TeiidServerRequirement;
import org.jboss.tools.teiid.reddeer.requirement.TeiidServerRequirement.TeiidServer;
import org.jboss.tools.teiid.reddeer.view.ModelExplorer;
import org.jboss.tools.teiid.reddeer.view.ServersViewExt;
import org.jboss.tools.teiid.reddeer.wizard.connectionProfiles.noDatabase.RestConnectionProfileWizard;
import org.jboss.tools.teiid.reddeer.wizard.imports.RestImportWizard;
import org.jboss.tools.teiid.reddeer.wizard.newWizard.VdbWizard;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * @author mmakovy, skaleta
 * tested features:
 * - consume REST WS - XML and JSON (+ Digest access authentication)
 */

@RunWith(RedDeerSuite.class)
@OpenPerspective(TeiidPerspective.class)
@TeiidServer(state = ServerRequirementState.RUNNING)
public class ConsumeRestWs {
	private static final String PROJECT_NAME = "ConsumeRest";
	private static final String PROCEDURE_NAME = "getProgrammes";

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
		String cp = "REST_XML";
		RestConnectionProfileWizard.openWizard(cp)
				.setConnectionUrl("http://ws-dvirt.rhcloud.com/dv-test-ws/rest/xml")
				.setType(RestConnectionProfileWizard.TYPE_XML)
				.testConnection()
				.finish();
		
		String sourceModel = "RestXmlSource";
		String viewModel = "RestXmlView";
		RestImportWizard.openWizard()
				.setProfileName(cp)
				.nextPage()
				.setProject(PROJECT_NAME)
				.setSourceModelName(sourceModel)
				.setViewModelName(viewModel)
				.setProcedureName(PROCEDURE_NAME)
				.nextPage()
				.setJndiName(sourceModel + "DS")
				.nextPage()
				.setRootPath("schedule/day/broadcasts/broadcast")
				.setColumns("pid","start","end","programme/title")
				.finish();

		String vdb = "RestXmlVdb";
		VdbWizard.openVdbWizard()
				.setLocation(PROJECT_NAME)
				.setName(vdb)
				.addModel(PROJECT_NAME, viewModel)
				.finish();

		ServersViewExt.getInstance().refreshServer(teiidServer.getName());

		modelExplorer.deployVdb(PROJECT_NAME, vdb);

		TeiidJDBCHelper jdbchelper = new TeiidJDBCHelper(teiidServer, vdb);
		assertEquals(16, jdbchelper.getNumberOfResults("exec " + viewModel + "." + PROCEDURE_NAME + "()"));
	}

	@Test
	public void testJson() {
		String cp = "REST_JSON";
		RestConnectionProfileWizard.openWizard(cp)
				.setConnectionUrl("http://ws-dvirt.rhcloud.com/dv-test-ws/rest/json")
				.setType(RestConnectionProfileWizard.TYPE_JSON)
				.testConnection()
				.finish();
		
		String sourceModel = "RestJsonSource";
		String viewModel = "RestJsonView";
		RestImportWizard.openWizard()
				.setProfileName(cp)
				.nextPage()
				.setProject(PROJECT_NAME)
				.setSourceModelName(sourceModel)
				.setViewModelName(viewModel)
				.setProcedureName(PROCEDURE_NAME)
				.nextPage()
				.setJndiName(sourceModel + "DS")
				.nextPage()
				.setRootPath("response/schedule/day/broadcasts")
				.setColumns("pid","start","end","programme/title")
				.finish();
		
		String vdb = "RestJsonVdb";
		VdbWizard.openVdbWizard()
				.setLocation(PROJECT_NAME)
				.setName(vdb)
				.addModel(PROJECT_NAME, viewModel)
				.finish();

		ServersViewExt.getInstance().refreshServer(teiidServer.getName());

		modelExplorer.deployVdb(PROJECT_NAME, vdb);

		TeiidJDBCHelper jdbchelper = new TeiidJDBCHelper(teiidServer, vdb);
		assertEquals(16, jdbchelper.getNumberOfResults("exec " + viewModel + "." + PROCEDURE_NAME + "()"));
	}
	
	@Test
	public void testXmlHttpDigest() {
		String cp = "REST_XML_DIGEST";
		RestConnectionProfileWizard.openWizard(cp)
				.setConnectionUrl("http://ws-dvirt.rhcloud.com/dv-test-ws-digest/rest/xml")
				.setType(RestConnectionProfileWizard.TYPE_XML)
				.setAuth(RestConnectionProfileWizard.AUTH_TYPE_DIGEST, "digest", "digest")
				.testConnection()
				.finish();
		
		String sourceModel = "RestXmlDigestSource";
		String viewModel = "RestXmlDigestView";
		RestImportWizard.openWizard()
				.setProfileName(cp)
				.nextPage()
				.setProject(PROJECT_NAME)
				.setSourceModelName(sourceModel)
				.setViewModelName(viewModel)
				.setProcedureName(PROCEDURE_NAME)
				.nextPage()
				.setJndiName(sourceModel + "DS")
				.nextPage()
				.setRootPath("schedule/day/broadcasts/broadcast")
				.setColumns("pid","start","end","programme/title")
				.finish();

		String vdb = "RestXmlDigestVdb";
		VdbWizard.openVdbWizard()
				.setLocation(PROJECT_NAME)
				.setName(vdb)
				.addModel(PROJECT_NAME, viewModel)
				.finish();

		ServersViewExt.getInstance().refreshServer(teiidServer.getName());

		modelExplorer.deployVdb(PROJECT_NAME, vdb);

		TeiidJDBCHelper jdbchelper = new TeiidJDBCHelper(teiidServer, vdb);
		assertEquals(16, jdbchelper.getNumberOfResults("exec " + viewModel + "." + PROCEDURE_NAME + "()"));
	}
	
	@Test
	public void testHttpDigestJson() {
		String cp = "REST_JSON_DIGEST";
		RestConnectionProfileWizard.openWizard(cp)
				.setConnectionUrl("http://ws-dvirt.rhcloud.com/dv-test-ws-digest/rest/json")
				.setType(RestConnectionProfileWizard.TYPE_JSON)
				.setAuth(RestConnectionProfileWizard.AUTH_TYPE_DIGEST, "digest", "digest")
				.testConnection()
				.finish();
		
		String sourceModel = "RestJsonDigestSource";
		String viewModel = "RestJsonDigestView";
		RestImportWizard.openWizard()
				.setProfileName(cp)
				.nextPage()
				.setProject(PROJECT_NAME)
				.setSourceModelName(sourceModel)
				.setViewModelName(viewModel)
				.setProcedureName(PROCEDURE_NAME)
				.nextPage()
				.setJndiName(sourceModel + "DS")
				.nextPage()
				.setRootPath("response/schedule/day/broadcasts")
				.setColumns("pid","start","end","programme/title")
				.finish();

		String vdb = "RestJsonDigestVdb";
		VdbWizard.openVdbWizard()
				.setLocation(PROJECT_NAME)
				.setName(vdb)
				.addModel(PROJECT_NAME, viewModel)
				.finish();

		ServersViewExt.getInstance().refreshServer(teiidServer.getName());

		modelExplorer.deployVdb(PROJECT_NAME, vdb);

		TeiidJDBCHelper jdbchelper = new TeiidJDBCHelper(teiidServer, vdb);
		assertEquals(16, jdbchelper.getNumberOfResults("exec " + viewModel + "." + PROCEDURE_NAME + "()"));
	}

}
