package org.jboss.tools.teiid.ui.bot.test;

import org.jboss.reddeer.junit.requirement.inject.InjectRequirement;
import org.jboss.reddeer.junit.runner.RedDeerSuite;
import org.jboss.reddeer.requirements.server.ServerReqState;
import org.jboss.reddeer.swt.impl.menu.ShellMenu;
import org.jboss.tools.teiid.reddeer.manager.ConnectionProfileManager;
import org.jboss.tools.teiid.reddeer.manager.ServerManager;
import org.jboss.tools.teiid.reddeer.manager.VDBManager;
import org.jboss.tools.teiid.reddeer.perspective.TeiidPerspective;
import org.jboss.tools.teiid.reddeer.requirement.TeiidServerRequirement;
import org.jboss.tools.teiid.reddeer.requirement.TeiidServerRequirement.TeiidServer;
import org.jboss.tools.teiid.reddeer.view.GuidesView;
import org.jboss.tools.teiid.reddeer.view.ModelExplorer;
import org.jboss.tools.teiid.reddeer.wizard.ImportProjectWizard;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * @author lfabriko
 */
@RunWith(RedDeerSuite.class)
@TeiidServer(state = ServerReqState.PRESENT)
public class ServerManagementSimpleDV6Test {

	@InjectRequirement
	private TeiidServerRequirement teiidServer;

	private static final String DV6_SERVER = "EAP-6.1";
	private static final String PROJECT_NAME = "ServerMgmtTest";
	private static final String MODEL_NAME = "partssupModel1.xmi";
	private static final String HSQLDB_PROFILE = "Generic HSQLDB Profile";
	private static final String VDB = "vdb";
	private static String TEST_SQL1 = "select * from \"partssupModel1\".\"PARTS\"";
	private static final String DS1_DATASOURCE = "resources/db/dv6-ds1.properties";
	private static final String PROJECT_ZIP = "resources/projects/ServerMgmtTest.zip";

	@BeforeClass
	public static void createModelProject() {

		new ShellMenu("Project", "Build Automatically").select();
		new ConnectionProfileManager().createCPWithDriverDefinition(HSQLDB_PROFILE, DS1_DATASOURCE);
		new ImportProjectWizard(PROJECT_ZIP).execute();
		new ModelExplorer().changeConnectionProfile(HSQLDB_PROFILE, PROJECT_NAME, MODEL_NAME);
		new ServerManager().startServer(DV6_SERVER);
	}

	@AfterClass
	public static void stopServer() {

		TeiidPerspective.getInstance();
		new ServerManager().stopServer(DV6_SERVER);
	}

	@Test
	public void test() {

		// TODO: Fix setDefaultTeiidInstance
		// new ServerManager().setDefaultTeiidInstance(DV6_SERVER, ServerType.DV6);
		Assert.assertTrue(new GuidesView().canPreviewData(null, new String[] { PROJECT_NAME, MODEL_NAME, "PARTS" }));

		TeiidPerspective.getInstance();
		new VDBManager().createVDB(PROJECT_NAME, VDB);
		new VDBManager().addModelsToVDB(PROJECT_NAME, VDB, new String[] { MODEL_NAME });
		Assert.assertTrue(new VDBManager().isVDBCreated(PROJECT_NAME, VDB));

		TeiidPerspective.getInstance();
		new VDBManager().deployVDB(new String[] { PROJECT_NAME, VDB });
		// TODO: Fix isVDBDeployed
		// Assert.assertTrue(new VDBManager().isVDBDeployed(DV6_SERVER, ServerType.DV6, VDB));

		new VDBManager().executeVDB(true, PROJECT_NAME, VDB);
		Assert.assertTrue(new VDBManager().queryPassed(VDB, TEST_SQL1));
	}
}
