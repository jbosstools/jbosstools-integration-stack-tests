package org.jboss.tools.teiid.ui.bot.test;

import static org.junit.Assert.assertEquals;

import java.util.Properties;

import org.jboss.reddeer.swt.test.RedDeerTest;
import org.jboss.tools.teiid.reddeer.WAR;
import org.jboss.tools.teiid.reddeer.manager.ConnectionProfileManager;
import org.jboss.tools.teiid.reddeer.manager.ImportManager;
import org.jboss.tools.teiid.reddeer.manager.ModelExplorerManager;
import org.jboss.tools.teiid.reddeer.manager.ServerManager;
import org.jboss.tools.teiid.reddeer.manager.VDBManager;
import org.jboss.tools.teiid.reddeer.view.ModelExplorerView;
import org.jboss.tools.teiid.reddeer.wizard.ImportGeneralItemWizard;
import org.jboss.tools.teiid.ui.bot.test.requirement.PerspectiveRequirement.Perspective;
import org.jboss.tools.teiid.ui.bot.test.suite.TeiidSuite;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * TEIIDDES-1808
 * 
 * @author lfabriko
 */
@Perspective(name = "Teiid Designer")
@RunWith(TeiidSuite.class)
public class RestCallTest extends RedDeerTest {

	private static final String serverFile = "as7.properties";
	private static final String projectName = "RestCallTest";
	private static final String srcModel = "BooksSrc";
	private static final String viewModel1 = "BooksView1";
	private static final String viewModel2 = "BooksView2";
	private static final String vdbName1 = "restcall1";
	private static final String vdbName2 = "restcall2";
	private static final String archiveLocation = "resources/projects/"
			+ projectName + ".zip";
	private static final String serverName = "AS-7.1";
	private static TeiidBot teiidBot = new TeiidBot();

	private static final String oracleCP = "Oracle";
	private static final String oracleCPProps = "resources/db/oracle_books.properties";
	private static final String url1 = "http://localhost:8080/Rest1/BooksView1/book1/0201877562";
	private static final String url2 = "http://localhost:8080/Rest2/BooksView2/book2/0-201-65758-9";
	private String result = "<TheBooks><Book><ISBN>0201877562</ISBN><TITLE>Software Testing in the Real World</TITLE></Book></TheBooks>";
	private String result2 = "<TheBooks><Book><ISBN>0-201-65758-9</ISBN><TITLE>LDAP Programming with Java</TITLE></Book></TheBooks>";

	@BeforeClass
	public static void prepare() {

		new ServerManager().setDefaultTeiidInstanceTargetedVersion("8.2.x");
		// switches back to 8.4 if server as 7.1 + teiid 8.2.0 is defined

		new ServerManager().addServer(serverFile);

		Properties itemProps = new Properties();
		itemProps.setProperty("location", teiidBot.toAbsolutePath(archiveLocation));

		new ImportManager().importGeneralItem(ImportGeneralItemWizard.Type.EXISTING_PROJECTS_INTO_WORKSPACE, itemProps);
		new ConnectionProfileManager().createCPWithDriverDefinition(oracleCP, oracleCPProps);
		new ModelExplorerManager().changeConnectionProfile(oracleCP, projectName, srcModel);
		
		new ServerManager().startServer(serverName);
	}
	
	@AfterClass
	public static void stopServer() {
		
		new ServerManager().stopServer(serverName);
	}

	@Test
	public void disconnectedTeiid() {

		String vdbJndiName1 = "RestTest1";
		String warCtxName1 = "Rest1";

		// disconnect teiid instance
		new ServerManager().getServersViewExt().disconnectTeiidInstance(serverName);

		// create vdb
		new VDBManager().createVDB(projectName, vdbName1);
		new VDBManager().addModelsToVDB(projectName, vdbName1, new String[] { viewModel1 });
		new VDBManager().getVDBEditor(projectName, vdbName1).close();

		// generate rest war
		Properties warProps = new Properties();
		warProps.setProperty("type", WAR.RESTEASY_TYPE);
		warProps.setProperty("contextName", warCtxName1);
		warProps.setProperty("vdbJndiName", vdbJndiName1);
		warProps.setProperty("saveLocation", teiidBot.toAbsolutePath("target"));
		warProps.setProperty("securityType", WAR.NONE_SECURITY);
		warProps.setProperty("serverName", "AS-7.1");

		String[] pathToVDB = new String[] { projectName, vdbName1 };

		WAR war = new VDBManager().createWAR(warProps, pathToVDB);

		// import created war
		Properties itemProps = new Properties();
		itemProps.setProperty("dirName", teiidBot.toAbsolutePath("target"));
		itemProps.setProperty("file", warCtxName1 + ".war");
		itemProps.setProperty("intoFolder", projectName);

		new ImportManager().importGeneralItem(ImportGeneralItemWizard.Type.FILE_SYSTEM, itemProps);

		// connect teiid instance
		new ServerManager().getServersViewExt().connectTeiidInstance(serverName);

		// testing purposes WAR
		// war = new ModelExplorerManager().getWAR(projectName, warCtxName1 + ".war");
		war.deploy();

		// create data source for BooksSrc
		new ModelExplorerManager().createDataSource(ModelExplorerView.ConnectionSourceType.USE_CONNECTION_PROFILE_INFO, oracleCP, projectName, srcModel);

		// synchronize vdb before deploying
		new VDBManager().getVDBEditor(projectName, vdbName1).synchronizeAll();
		new VDBManager().getVDBEditor(projectName, vdbName1).close();

		// create src model for vdb, deploy vdb
		new VDBManager().createVDBDataSource(pathToVDB, vdbJndiName1, false);
		new VDBManager().deployVDB(pathToVDB);

		// run wget
		assertEquals(result, teiidBot.curl(url1));
	}

	@Test
	public void connectedTeiid() {

		String vdbJndiName1 = "RestTest2";
		String warCtxName1 = "Rest2";

		// create vdb
		new VDBManager().createVDB(projectName, vdbName2);
		new VDBManager().addModelsToVDB(projectName, vdbName2, new String[] { viewModel2 });
		new VDBManager().getVDBEditor(projectName, vdbName2).close();

		// generate rest war
		Properties warProps = new Properties();
		warProps.setProperty("type", WAR.RESTEASY_TYPE);
		warProps.setProperty("contextName", warCtxName1);
		warProps.setProperty("vdbJndiName", vdbJndiName1);
		warProps.setProperty("saveLocation", teiidBot.toAbsolutePath("target"));
		warProps.setProperty("securityType", WAR.NONE_SECURITY);
		warProps.setProperty("serverName", "AS-7.1");

		String[] pathToVDB = new String[] { projectName, vdbName2 };

		WAR war = new VDBManager().createWAR(warProps, pathToVDB);

		// import created war
		Properties itemProps = new Properties();
		itemProps.setProperty("dirName", teiidBot.toAbsolutePath("target"));
		itemProps.setProperty("file", warCtxName1 + ".war");
		itemProps.setProperty("intoFolder", projectName);

		new ImportManager().importGeneralItem(ImportGeneralItemWizard.Type.FILE_SYSTEM, itemProps);

		war.deploy();

		// create data source for BooksSrc
		new ModelExplorerManager().createDataSource(ModelExplorerView.ConnectionSourceType.USE_CONNECTION_PROFILE_INFO,
				oracleCP, projectName, srcModel);

		// synchronize vdb before deploying
		new VDBManager().getVDBEditor(projectName, vdbName2).synchronizeAll();

		new VDBManager().getVDBEditor(projectName, vdbName2).close();

		// create src model for vdb, deploy vdb
		new VDBManager().createVDBDataSource(pathToVDB, vdbJndiName1, false);
		new VDBManager().deployVDB(pathToVDB);

		// run wget
		assertEquals(result2, teiidBot.curl(url2));
	}
}
