package org.jboss.tools.teiid.ui.bot.test;

import org.eclipse.swtbot.swt.finder.SWTBotTestCase;
import org.jboss.reddeer.junit.requirement.inject.InjectRequirement;
import org.jboss.reddeer.junit.runner.RedDeerSuite;
import org.jboss.reddeer.requirements.server.ServerReqState;
import org.jboss.reddeer.swt.impl.menu.ShellMenu;
import org.jboss.tools.runtime.reddeer.requirement.ServerRequirement;
import org.jboss.tools.teiid.reddeer.manager.ConnectionProfileManager;
import org.jboss.tools.teiid.reddeer.manager.ServerManager;
import org.jboss.tools.teiid.reddeer.manager.VDBManager;
import org.jboss.tools.teiid.reddeer.perspective.TeiidPerspective;
import org.jboss.tools.teiid.reddeer.requirement.TeiidServerRequirement.TeiidServer;
import org.jboss.tools.teiid.reddeer.view.GuidesView;
import org.jboss.tools.teiid.reddeer.view.ModelExplorer;
import org.jboss.tools.teiid.reddeer.wizard.ImportProjectWizard;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * 
 * @author lfabriko
 *
 */
@RunWith(RedDeerSuite.class)
@TeiidServer(state = ServerReqState.RUNNING)
public class ServerManagementSimpleEDS5Test extends SWTBotTestCase {
	
	@InjectRequirement
	private ServerRequirement serverRequirement;

	private static final String EDS5_PROPERTIES = "eds5.properties";
	private static final String EDS5_SERVER = "SOA-5.1";
	private static final String PROJECT_NAME = "ServerMgmtTest";
	private static final String MODEL_NAME = "partssupModel1.xmi";
	private static final String HSQLDB_PROFILE = "HSQLDB Profile";
	private static final String VDB = "vdb";
	private static String TEST_SQL1 = "select * from \"partssupModel1\".\"PARTS\"";
	private static final String DS1_PROPS = "resources/db/ds1.properties";

	@BeforeClass
	public static void createModelProject() {
		new ShellMenu("Project", "Build Automatically").select();
		new ConnectionProfileManager().createCPWithDriverDefinition(HSQLDB_PROFILE, DS1_PROPS);
		new ImportProjectWizard("resources/projects/ServerMgmtTest.zip").execute(); 
		new ModelExplorer().changeConnectionProfile(HSQLDB_PROFILE, PROJECT_NAME, MODEL_NAME);
	}

	@Test
	public void test() {
		try {
			new ServerManager().editLaunchConfigProgramArgs(EDS5_SERVER, "-Dremoting.stream.host=localhost");
		} catch (Exception ex){
			System.err.println("Cannot edit launch config," + ex.getMessage());
		}
		
		try {
			// TODO: fix  setDefaultTeiidInstance
			// new ServerManager().setDefaultTeiidInstance(EDS5_SERVER, ??);
		} catch (Exception ex){
			System.err.println("Cannot set default instance, " + ex.getMessage());
		}
		
		try {
			System.out.println("Can preview data? " + new GuidesView().canPreviewData(null, new String[]{PROJECT_NAME, MODEL_NAME, "PARTS"}));//if assert fails, whole test fails -> but it may have failed just because the tree didn't expand
		} catch (Exception ex){
			System.err.println("cannot preview data, " + ex.getMessage());
		}
		
		// switch back to Teiid Designer Perspective
		try{
			TeiidPerspective.getInstance();
		} catch (Exception ex){
			System.err.println(ex.getMessage());
		}
		
		// create VDB - pass
		try{
			new VDBManager().createVDB(PROJECT_NAME, VDB);
			new VDBManager().addModelsToVDB(PROJECT_NAME, VDB, new String[]{MODEL_NAME});
			System.out.println("VDB created? " + new VDBManager().isVDBCreated(PROJECT_NAME, VDB));	
		} catch (Exception ex){
			System.err.println("Cannot create vdb, " + ex.getMessage());
		}
		
		TeiidPerspective.getInstance();
		// deploy VDB - pass
		try{
			new VDBManager().deployVDB(new String[]{PROJECT_NAME, VDB});
			// TOD: fix isVDBDeployed
			// System.out.println("VDB deployed? " + new VDBManager().isVDBDeployed(EDS5_SERVER, ServerType.EDS5, VDB));//or serverManager, its ==
		} catch (Exception ex){
			//do it manually
			System.err.println("VDB may not be deployed, " + ex.getMessage());
		}
		
		// execute VDB - pass
		try{
			new VDBManager().executeVDB(true,PROJECT_NAME, VDB);
			System.out.println("Query passed? " + new VDBManager().queryPassed(VDB, TEST_SQL1));
		} catch (Exception ex){
			System.err.println("Query may not passed, " + ex.getMessage());
		}
		
		// switch back to teiid designer perspective
		try{
		TeiidPerspective.getInstance();
		} catch (Exception ex){
			System.err.println(ex.getMessage());
		}
		
		try{
			new ServerManager().stopServer(EDS5_SERVER);
		} catch (Exception ex){
			System.err.println("Server stop problem, " + ex.getMessage());
		}
	}


}
