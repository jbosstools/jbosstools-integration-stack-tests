package org.jboss.tools.teiid.ui.bot.test;

import static org.junit.Assert.assertEquals;

import java.util.Properties;

import org.jboss.reddeer.eclipse.wst.server.ui.view.ServersView;
import org.jboss.reddeer.junit.runner.RedDeerSuite;
import org.jboss.reddeer.requirements.openperspective.OpenPerspectiveRequirement.OpenPerspective;
import org.jboss.reddeer.requirements.server.ServerReqState;
import org.jboss.reddeer.swt.impl.shell.WorkbenchShell;
import org.jboss.tools.runtime.reddeer.requirement.ServerReqType;
import org.jboss.tools.runtime.reddeer.requirement.ServerRequirement.Server;
import org.jboss.tools.teiid.reddeer.editor.SQLScrapbookEditor;
import org.jboss.tools.teiid.reddeer.manager.ConnectionProfileManager;
import org.jboss.tools.teiid.reddeer.manager.ModelExplorerManager;
import org.jboss.tools.teiid.reddeer.manager.VDBManager;
import org.jboss.tools.teiid.reddeer.perspective.DatabaseDevelopmentPerspective;
import org.jboss.tools.teiid.reddeer.perspective.TeiidPerspective;
import org.jboss.tools.teiid.reddeer.view.ModelExplorerView;
import org.jboss.tools.teiid.reddeer.view.SQLResult;
import org.jboss.tools.teiid.reddeer.view.ServersViewExt;
import org.jboss.tools.teiid.reddeer.wizard.RestImportWizard;
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
@Server(type = ServerReqType.ANY, state = ServerReqState.RUNNING)
public class ConsumeRestWs {

	private static final String PROJECT_NAME = "REST";
	private static final String XML_PROFILE_NAME = "REST_XML";
	private static final String JSON_PROFILE_NAME = "REST_JSON";
	private static final String PROCEDURE_NAME = "getProgrammes";
	private static final String VDBXML = "RESTXML";
	private static final String VDBJSON = "RESTJSON";
	private static final String SOURCE_MODEL_XML = "RestXMLSource";
	private static final String VIEW_MODEL_XML = "RestXMLView";
	private static final String SOURCE_MODEL_JSON = "RestJSONSource";
	private static final String VIEW_MODEL_JSON = "RestJSONView";

	@BeforeClass
	public static void before() {
		new WorkbenchShell().maximize();
		new TeiidBot().uncheckBuildAutomatically();
		new ModelExplorerManager().createProject(PROJECT_NAME, false);
	}

	@Test
	public void testXml() {

		Properties restCpXml = new Properties();
		restCpXml.setProperty("connectionUrl",
				"http://ws-dvirt.rhcloud.com/dv-test-ws/rest/xml");
		restCpXml.setProperty("type", "xml");

		new ConnectionProfileManager()
				.createCPREST(XML_PROFILE_NAME, restCpXml);

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
		new VDBManager().addModelsToVDB(PROJECT_NAME, VDBXML, new String[] {
				SOURCE_MODEL_XML, VIEW_MODEL_XML });

		new ServersView().open();
		new ServersViewExt().refreshServer(new ServersView().getServers()
				.get(0).getLabel().getName());

		new ModelExplorerView().executeVDB(PROJECT_NAME, VDBXML + ".vdb");

		executeSQL("exec " + VIEW_MODEL_XML + "." + PROCEDURE_NAME + "()",
				VDBXML);

	}

	@Test
	public void testJson() {
		Properties restCpJson = new Properties();
		restCpJson.setProperty("connectionUrl",
				"http://ws-dvirt.rhcloud.com/dv-test-ws/rest/json");
		restCpJson.setProperty("type", "json");

		new ConnectionProfileManager().createCPREST(JSON_PROFILE_NAME,
				restCpJson);

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
		new VDBManager().addModelsToVDB(PROJECT_NAME, VDBJSON, new String[] {
				SOURCE_MODEL_JSON, VIEW_MODEL_JSON });

		new ServersView().open();
		new ServersViewExt().refreshServer(new ServersView().getServers()
				.get(0).getLabel().getName());

		new ModelExplorerView().executeVDB(PROJECT_NAME, VDBJSON + ".vdb");

		executeSQL("exec " + VIEW_MODEL_JSON + "." + PROCEDURE_NAME + "()",
				VDBJSON);

	}

	private void executeSQL(String sql, String vdb) {

		SQLScrapbookEditor editor = new SQLScrapbookEditor("SQL Scrapbook0");
		editor.show();
		editor.setDatabase(vdb);

		editor.setText(sql);
		editor.executeAll();

		SQLResult result = DatabaseDevelopmentPerspective.getInstance()
				.getSqlResultsView().getByOperation(sql);
		assertEquals(SQLResult.STATUS_SUCCEEDED, result.getStatus());

	}

}
