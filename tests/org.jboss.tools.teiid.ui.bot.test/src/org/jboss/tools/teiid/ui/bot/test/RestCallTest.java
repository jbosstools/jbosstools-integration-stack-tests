package org.jboss.tools.teiid.ui.bot.test;

import java.util.Properties;

import org.apache.log4j.Logger;
import org.jboss.reddeer.junit.requirement.inject.InjectRequirement;
import org.jboss.reddeer.junit.runner.RedDeerSuite;
import org.jboss.reddeer.requirements.server.ServerReqState;
import org.jboss.reddeer.swt.impl.shell.DefaultShell;
import org.jboss.reddeer.swt.wait.AbstractWait;
import org.jboss.reddeer.swt.wait.TimePeriod;
import org.jboss.tools.teiid.reddeer.WAR;
import org.jboss.tools.teiid.reddeer.manager.ConnectionProfilesConstants;
import org.jboss.tools.teiid.reddeer.manager.ImportManager;
import org.jboss.tools.teiid.reddeer.manager.ModelExplorerManager;
import org.jboss.tools.teiid.reddeer.manager.VDBManager;
import org.jboss.tools.teiid.reddeer.requirement.TeiidServerRequirement;
import org.jboss.tools.teiid.reddeer.requirement.TeiidServerRequirement.TeiidServer;
import org.jboss.tools.teiid.reddeer.util.SimpleHttpClient;
import org.jboss.tools.teiid.reddeer.view.ModelExplorerView;
import org.jboss.tools.teiid.reddeer.view.ServersViewExt;
import org.jboss.tools.teiid.reddeer.wizard.ImportGeneralItemWizard;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * TEIIDDES-1808
 * 
 * @author lfabriko + jstastny
 */
@RunWith(RedDeerSuite.class)
@TeiidServer(
		state = ServerReqState.PRESENT
		,connectionProfiles = {ConnectionProfilesConstants.ORACLE_11G_BOOKS}
)
public class RestCallTest {
	
	@InjectRequirement
	private static TeiidServerRequirement teiidServer;
	
	private static Logger log = Logger.getLogger(RestCallTest.class);

	private static final String PROJECT_NAME = "RestCallTest";
	private static final String SRC_MODEL = "BooksSrc";
	private static final String VIEW_MODEL_1 = "BooksView1";
	private static final String VIEW_MODEL_2 = "BooksView2";
	private static final String VDB_NAME_1 = "restcall1";
	private static final String VDB_NAME_2 = "restcall2";
	private static final String PROJECT_LOCATION = "resources/projects/"+ PROJECT_NAME;
	private static final TeiidBot TEIID_BOT = new TeiidBot();

	private static final String URL_1 = "http://localhost:8080/Rest1/BooksView1/book1/0201877562";
	private static final String URL_2 = "http://localhost:8080/Rest2/BooksView2/book2/0-201-65758-9";
	private static final String RESULT_1 = "<TheBooks><Book><ISBN>0201877562</ISBN><TITLE>Software Testing in the Real World</TITLE></Book></TheBooks>";
	private static final String RESULT_2 = "<TheBooks><Book><ISBN>0-201-65758-9</ISBN><TITLE>LDAP Programming with Java</TITLE></Book></TheBooks>";

	@BeforeClass
	public static void prepare() {
		new ImportManager().importProject(PROJECT_LOCATION);

		log.info("After import");
		new DefaultShell();
		
		new ModelExplorerManager().changeConnectionProfile(ConnectionProfilesConstants.ORACLE_11G_BOOKS, PROJECT_NAME, SRC_MODEL);
	}

	@Test
	public void disconnectedTeiid() {

		String vdbJndiName1 = "RestTest1";
		String warCtxName1 = "Rest1";

		teiidServer.getServerConfig().getServerBase().setState(ServerReqState.STOPPED);
		
		// create vdb
		new VDBManager().createVDB(PROJECT_NAME, VDB_NAME_1);
		new VDBManager().addModelsToVDB(PROJECT_NAME, VDB_NAME_1, new String[] { VIEW_MODEL_1 });
		new VDBManager().getVDBEditor(PROJECT_NAME, VDB_NAME_1).close();

		// generate rest war
		Properties warProps = new Properties();
		warProps.setProperty("type", WAR.RESTEASY_TYPE);
		warProps.setProperty("contextName", warCtxName1);
		warProps.setProperty("vdbJndiName", vdbJndiName1);
		warProps.setProperty("saveLocation", TEIID_BOT.toAbsolutePath("target"));
		warProps.setProperty("securityType", WAR.NONE_SECURITY);
		warProps.setProperty("serverName", "AS-7.1");

		String[] pathToVDB = new String[] { PROJECT_NAME, VDB_NAME_1 };

		WAR war = new VDBManager().createWAR(warProps, pathToVDB);

		// import created war
		Properties itemProps = new Properties();
		itemProps.setProperty("dirName", TEIID_BOT.toAbsolutePath("target"));
		itemProps.setProperty("file", warCtxName1 + ".war");
		itemProps.setProperty("intoFolder", PROJECT_NAME);

		new ImportManager().importGeneralItem(ImportGeneralItemWizard.Type.FILE_SYSTEM, itemProps);

		// connect teiid instance
		teiidServer.getServerConfig().getServerBase().setState(ServerReqState.RUNNING);
		AbstractWait.sleep(TimePeriod.getCustom(30));
		new ServersViewExt().refreshServer(teiidServer.getName());
		
		
		AbstractWait.sleep(TimePeriod.getCustom(30));
		// testing purposes WAR
		war.deploy();

		// create data source for BooksSrc
		new ModelExplorerManager().createDataSource(ModelExplorerView.ConnectionSourceType.USE_CONNECTION_PROFILE_INFO, ConnectionProfilesConstants.ORACLE_11G_BOOKS, PROJECT_NAME, SRC_MODEL);

		// synchronize vdb before deploying
		new VDBManager().getVDBEditor(PROJECT_NAME, VDB_NAME_1).synchronizeAll();
		new VDBManager().getVDBEditor(PROJECT_NAME, VDB_NAME_1).close();

		// create src model for vdb, deploy vdb
		new VDBManager().createVDBDataSource(pathToVDB, vdbJndiName1, false);
		new VDBManager().deployVDB(pathToVDB);

		// run wget
		Assert.assertEquals(RESULT_1, new SimpleHttpClient(URL_1).get());
	}

	@Test
	public void connectedTeiid() {
		teiidServer.getServerConfig().getServerBase().setState(ServerReqState.RUNNING);
		new ServersViewExt().refreshServer(teiidServer.getName());
		
		String vdbJndiName1 = "RestTest2";
		String warCtxName1 = "Rest2";

		// create vdb
		new VDBManager().createVDB(PROJECT_NAME, VDB_NAME_2);
		new VDBManager().addModelsToVDB(PROJECT_NAME, VDB_NAME_2, new String[] { VIEW_MODEL_2 });
		new VDBManager().getVDBEditor(PROJECT_NAME, VDB_NAME_2).close();

		// generate rest war
		Properties warProps = new Properties();
		warProps.setProperty("type", WAR.RESTEASY_TYPE);
		warProps.setProperty("contextName", warCtxName1);
		warProps.setProperty("vdbJndiName", vdbJndiName1);
		warProps.setProperty("saveLocation", TEIID_BOT.toAbsolutePath("target"));
		warProps.setProperty("securityType", WAR.NONE_SECURITY);
		warProps.setProperty("serverName", "AS-7.1");

		String[] pathToVDB = new String[] { PROJECT_NAME, VDB_NAME_2 };

		WAR war = new VDBManager().createWAR(warProps, pathToVDB);

		// import created war
		Properties itemProps = new Properties();
		itemProps.setProperty("dirName", TEIID_BOT.toAbsolutePath("target"));
		itemProps.setProperty("file", warCtxName1 + ".war");
		itemProps.setProperty("intoFolder", PROJECT_NAME);

		new ImportManager().importGeneralItem(ImportGeneralItemWizard.Type.FILE_SYSTEM, itemProps);

		war.deploy();

		// create data source for BooksSrc
		new ModelExplorerManager().createDataSource(ModelExplorerView.ConnectionSourceType.USE_CONNECTION_PROFILE_INFO, ConnectionProfilesConstants.ORACLE_11G_BOOKS, PROJECT_NAME, SRC_MODEL);


		// synchronize vdb before deploying
		new VDBManager().getVDBEditor(PROJECT_NAME, VDB_NAME_2).synchronizeAll();

		new VDBManager().getVDBEditor(PROJECT_NAME, VDB_NAME_2).close();

		// create src model for vdb, deploy vdb
		new VDBManager().createVDBDataSource(pathToVDB, vdbJndiName1, false);
		new VDBManager().deployVDB(pathToVDB);

		// run wget
		Assert.assertEquals(RESULT_2, new SimpleHttpClient(URL_2).get());
	}
}
