package org.jboss.tools.teiid.ui.bot.test;

import org.eclipse.swtbot.swt.finder.SWTBotTestCase;
import org.eclipse.swtbot.swt.finder.utils.SWTBotPreferences;
import org.jboss.reddeer.eclipse.jdt.ui.packageexplorer.PackageExplorer;
import org.jboss.reddeer.junit.requirement.inject.InjectRequirement;
import org.jboss.reddeer.junit.runner.RedDeerSuite;
import org.jboss.reddeer.requirements.server.ServerReqState;
import org.jboss.reddeer.core.condition.JobIsRunning;
import org.jboss.reddeer.common.wait.TimePeriod;
import org.jboss.reddeer.common.wait.WaitWhile;
import org.jboss.tools.teiid.reddeer.VDB;
import org.jboss.tools.teiid.reddeer.editor.SQLScrapbookEditor;
import org.jboss.tools.teiid.reddeer.editor.VDBEditor;
import org.jboss.tools.teiid.reddeer.manager.ConnectionProfileManager;
import org.jboss.tools.teiid.reddeer.perspective.DatabaseDevelopmentPerspective;
import org.jboss.tools.teiid.reddeer.perspective.TeiidPerspective;
import org.jboss.tools.teiid.reddeer.requirement.TeiidServerRequirement;
import org.jboss.tools.teiid.reddeer.requirement.TeiidServerRequirement.TeiidServer;
import org.jboss.tools.teiid.reddeer.view.GuidesView;
import org.jboss.tools.teiid.reddeer.view.ModelExplorer;
import org.jboss.tools.teiid.reddeer.view.SQLResult;
import org.jboss.tools.teiid.reddeer.view.TeiidInstanceView;
import org.jboss.tools.teiid.reddeer.wizard.CreateVDB;
import org.jboss.tools.teiid.reddeer.wizard.ImportProjectWizard;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * Test server management use cases start with -Pprofiles - this will load and
 * install all required servers
 * 
 * @author lfabriko
 * (linux fedora 18, 64bit locally - 8 min)
 */
@RunWith(RedDeerSuite.class)
@TeiidServer(state = ServerReqState.PRESENT)
public class ServerManagementSimpleTest extends SWTBotTestCase {

	@InjectRequirement
	private TeiidServerRequirement teiidServer;
	
	private static final String PROJECT_NAME = "ServerMgmtTest";
	private static final String MODEL_NAME = "partssupModel1.xmi";
	private static final String HSQLDB_PROFILE = "HSQLDB Profile";
	private static final String VDB = "vdb";
	private static int n = -1;
	private static final String[] pathToVDB_EAP6 = {
			"EAP-6.1  [Started, Synchronized]", "Teiid Instance Configuration",
			"mm://localhost:9999  [default]", "VDBs" };
	
	private static String TEST_SQL1 = "select * from \"partssupModel1\".\"PARTS\"";
	private static String EAP6_URL = "mm://localhost:9999::admin (EAP-6.1)";

	@BeforeClass
	public static void createModelProject() {
		new org.jboss.reddeer.swt.impl.menu.ShellMenu("Project", "Build Automatically").select();
		if (System.getProperty("swtbot.PLAYBACK_DELAY") == null) {
			SWTBotPreferences.PLAYBACK_DELAY = 1000;
		} else {
			SWTBotPreferences.PLAYBACK_DELAY = new Integer(
					System.getProperty("swtbot.PLAYBACK_DELAY"));// -Dswtbot.PLAYBACK_DELAY
		}

		// create HSQL profile
		//teiidBot.createHsqlProfile("resources/db/ds1.properties", HSQLDB_PROFILE, true, true);
		new ConnectionProfileManager().createCPWithDriverDefinition(HSQLDB_PROFILE, "resources/db/ds1.properties");
		
		new ImportProjectWizard("resources/projects/ServerMgmtTest.zip").execute(); //incorrect connection profile
		//set connection profile
		new ModelExplorer().changeConnectionProfile(HSQLDB_PROFILE, PROJECT_NAME, MODEL_NAME);
		
	}


	/**
	 * Servers both defined and started
	 */
	@Test
	public void test() {
		SWTBotPreferences.PLAYBACK_DELAY = 1000;
		n++;
		
		SWTBotPreferences.PLAYBACK_DELAY = 200;
		// start server EAP-6.1
		
		TeiidInstanceView teiidInstanceView = new TeiidInstanceView(true);
		try {
		teiidServer.getServerConfig().getServerBase().setState(ServerReqState.RUNNING);

		//specify the default teiid instance
		teiidInstanceView.setDefaultTeiidInstance(EAP6_URL);
		} catch (Exception ex){
			//do it manually
		}
		assertTrue(canPreviewData(null, "PARTS"));

		// switch back to Teiid Designer Perspective
		try{
		TeiidPerspective.getInstance();
		} catch (Exception ex){
			//do it manually
		}
		
		// create VDB - pass
		try{
		assertTrue(canCreateVDB(VDB + n, MODEL_NAME));
		} catch (Exception ex){
			//do it manually
		}
		
		// deploy VDB - pass
		try{
		assertTrue(canDeployVDB(null, VDB + n, createPathToVDB(VDB + n, pathToVDB_EAP6)));
		} catch (Exception ex){
			//do it manually
		}
		
		// execute VDB - pass
		try{
		assertTrue(canExecuteVDB(null, VDB + n, TEST_SQL1));
		} catch (Exception ex){
			//do it manually
		}
		
		// switch back to teiid designer perspective
		try{
		TeiidPerspective.getInstance();
		} catch (Exception ex){
			//do it manually
		}
		
		// stop server EAP-6.1
		try{
		teiidServer.getServerConfig().getServerBase().setState(ServerReqState.STOPPED);
		} catch (Exception ex){
			//do it manually
		}
		
		new WaitWhile(new JobIsRunning(), TimePeriod.LONG);

	}

	

	private boolean canPreviewData(String message, String tableName) {
		if (message != null) {
			new GuidesView().chooseAction("Model JDBC Source", "Preview Data");
			new WaitWhile(new JobIsRunning(), TimePeriod.LONG);
			assertEquals(bot.activeShell().getText(), message);
			bot.activeShell().close();
			return false;
		} else {
			//new GuidesView().previewData(true, PROJECT_NAME, MODEL_NAME,tableName);
			new GuidesView().previewData(PROJECT_NAME, MODEL_NAME,tableName);
			SQLResult result = DatabaseDevelopmentPerspective.getInstance()
					.getSqlResultsView().getByOperation(TEST_SQL1);// "select * from \""+MODEL_NAME.substring(0,MODEL_NAME.indexOf("."))+"\".\""+tableName+"\""
			assertEquals(SQLResult.STATUS_SUCCEEDED, result.getStatus());
			return true;
		}
	}

	private boolean canCreateVDB(String vdb, String model) {
		CreateVDB createVDB = new CreateVDB();
		createVDB.setFolder(PROJECT_NAME);
		createVDB.setName(vdb);
		createVDB.execute(true);

		VDBEditor editor = VDBEditor.getInstance(vdb + ".vdb");
		editor.show();
		editor.addModel(PROJECT_NAME, model);
		editor.save();

		// check if VDB is in project
		return new PackageExplorer().getProject(PROJECT_NAME).containsItem(
				vdb + ".vdb");
	}

	private boolean canDeployVDB(String message, String vdb,
			String... pathToVDB) {
		VDB vdbItem = new ModelExplorer().getModelProject(PROJECT_NAME).getVDB(
				vdb + ".vdb");
		vdbItem.deployVDB();
		if (message != null) {
			assertEquals(bot.activeShell().getText(), message);
			bot.activeShell().close();
			return false;
		} else {

			// check if servers view contains deployed vdb
			return new TeiidInstanceView(true)
					.containsVDB(true, pathToVDB);
		}
	}

	private boolean canExecuteVDB(String message, String vdbName, String sql) {
		VDB vdb = new ModelExplorer().getModelProject(PROJECT_NAME).getVDB(
				vdbName + ".vdb");

		if (message != null) {
			new GuidesView().chooseAction("Model JDBC Source", "Execute VDB");//just select action to execute VDB and confirm fail message
			assertEquals(bot.activeShell().getText(), message);
			bot.activeShell().close();
			return false;
		}
		vdb.executeVDB(true);
		
		SQLScrapbookEditor editor = new SQLScrapbookEditor("SQL Scrapbook0");
		editor.show();

		// TESTSQL_1
		editor.setText(sql);
		editor.executeAll();

		new WaitWhile(new JobIsRunning(), TimePeriod.LONG);
		
		SQLResult result = DatabaseDevelopmentPerspective.getInstance()
				.getSqlResultsView().getByOperation(sql);
		assertEquals(SQLResult.STATUS_SUCCEEDED, result.getStatus());

		editor.close();
		
		return true;
	}
	
	private String[] createPathToVDB(String vdb, String... path){
		String[] array = new String[path.length+1];
		System.arraycopy(path, 0, array, 0, array.length-1);
		array[array.length-1] = vdb;
		return array;
	}
	//TODO remove

}
