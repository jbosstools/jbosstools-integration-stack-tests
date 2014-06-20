package org.jboss.tools.teiid.ui.bot.test;

import static org.junit.Assert.assertTrue;

import org.jboss.reddeer.swt.impl.menu.ShellMenu;
import org.jboss.reddeer.swt.test.RedDeerTest;
import org.jboss.tools.teiid.reddeer.manager.ConnectionProfileManager;
import org.jboss.tools.teiid.reddeer.manager.ServerManager;
import org.jboss.tools.teiid.reddeer.manager.VDBManager;
import org.jboss.tools.teiid.reddeer.perspective.TeiidPerspective;
import org.jboss.tools.teiid.reddeer.view.GuidesView;
import org.jboss.tools.teiid.reddeer.view.ModelExplorer;
import org.jboss.tools.teiid.reddeer.view.ServersViewExt.ServerType;
import org.jboss.tools.teiid.reddeer.wizard.ImportProjectWizard;
import org.jboss.tools.teiid.ui.bot.test.requirement.PerspectiveRequirement.Perspective;
import org.jboss.tools.teiid.ui.bot.test.requirement.ServerRequirement.Server;
import org.jboss.tools.teiid.ui.bot.test.requirement.ServerRequirement.State;
import org.jboss.tools.teiid.ui.bot.test.requirement.ServerRequirement.Type;
import org.jboss.tools.teiid.ui.bot.test.suite.TeiidSuite;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * @author lfabriko
 */
@Perspective(name = "Teiid Designer")
@Server(type = Type.ALL, state = State.NOT_RUNNING)
@RunWith(TeiidSuite.class)
public class ServerManagementSimpleDV6Test extends RedDeerTest {

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

			new ServerManager().setDefaultTeiidInstance(DV6_SERVER, ServerType.DV6);
			assertTrue(new GuidesView().canPreviewData(null, new String[] {PROJECT_NAME, MODEL_NAME, "PARTS" }));

			TeiidPerspective.getInstance();
			new VDBManager().createVDB(PROJECT_NAME, VDB);
			new VDBManager().addModelsToVDB(PROJECT_NAME, VDB, new String[] { MODEL_NAME });
			assertTrue(new VDBManager().isVDBCreated(PROJECT_NAME, VDB));

			TeiidPerspective.getInstance();
			new VDBManager().deployVDB(new String[] { PROJECT_NAME, VDB });
			assertTrue(new VDBManager().isVDBDeployed(DV6_SERVER, ServerType.DV6, VDB));

			new VDBManager().executeVDB(true, PROJECT_NAME, VDB);
			assertTrue(new VDBManager().queryPassed(VDB, TEST_SQL1));
	}
}
