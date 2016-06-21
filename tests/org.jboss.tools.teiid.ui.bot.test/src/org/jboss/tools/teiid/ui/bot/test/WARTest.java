package org.jboss.tools.teiid.ui.bot.test;

import java.util.Properties;

import org.eclipse.swtbot.swt.finder.SWTBotTestCase;
import org.jboss.reddeer.junit.requirement.inject.InjectRequirement;
import org.jboss.reddeer.junit.runner.RedDeerSuite;
import org.jboss.reddeer.requirements.server.ServerReqState;
import org.jboss.tools.teiid.reddeer.connection.ConnectionProfileConstants;
import org.jboss.tools.teiid.reddeer.connection.SimpleHttpClient;
import org.jboss.tools.teiid.reddeer.manager.ImportManager;
import org.jboss.tools.teiid.reddeer.manager.ModelExplorerManager;
import org.jboss.tools.teiid.reddeer.requirement.TeiidServerRequirement;
import org.jboss.tools.teiid.reddeer.requirement.TeiidServerRequirement.TeiidServer;
import org.jboss.tools.teiid.reddeer.wizard.ImportGeneralItemWizard;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * WAR tests (REST, JBossWS-CXF) with security None | HttpBasic
 * 
 * @author lfabriko, felias
 *
 */
@RunWith(RedDeerSuite.class)
@TeiidServer(state = ServerReqState.RUNNING, connectionProfiles = { ConnectionProfileConstants.ORACLE_11G_BOOKS })
@Deprecated // duplicate(WsCreation,CreateRestProc.) - TODO remove resources too
public class WARTest extends SWTBotTestCase {
	public static final String MODEL_PROJECT = "jdbcImportTest";
	@InjectRequirement
	private static TeiidServerRequirement teiidServer;
	private static TeiidBot teiidBot = new TeiidBot();
	private static final String projectBooksWS = "BooksWS";
	private static final String vdbCheckBook = "checkBookVdb";
	private static final String[] pathToCheckBookVDB = new String[] { projectBooksWS, vdbCheckBook + ".vdb" };
	private static final String projectBooksRest = "BooksRest";
	private static final String vdbBooksRest = "booksRest";
	private static final String[] pathToBooksRestVDB = new String[] { projectBooksRest, vdbBooksRest + ".vdb" };
	private String resultRest = "<TheBooks><Book><ISBN>0201877562</ISBN><TITLE>Software Testing in the Real World</TITLE></Book></TheBooks>";

	@BeforeClass
	public static void before() {
//		VDBManager vdbManager = new VDBManager();
//
//		// RESTEasy war
//		new ImportManager().importProject("resources/projects/BooksRest");
//		new ModelExplorerManager().changeConnectionProfile(ConnectionProfileConstants.ORACLE_11G_BOOKS,
//				projectBooksRest, "BooksSrc.xmi");
//		vdbManager.createVDB(projectBooksRest, vdbBooksRest);
//		vdbManager.addModelsToVDB(projectBooksRest, vdbBooksRest, new String[] { "BooksView.xmi" });
//		vdbManager.deployVDB(pathToBooksRestVDB);
//		vdbManager.createVDBDataSource(pathToBooksRestVDB);
//
//		// JBossWS-CXF war
//		new ImportManager().importProject("resources/projects/BooksWS");
//		new ModelExplorerManager().changeConnectionProfile(ConnectionProfileConstants.ORACLE_11G_BOOKS, projectBooksWS,
//				"books.xmi");
//		vdbManager.createVDB(projectBooksWS, vdbCheckBook);
//		vdbManager.addModelsToVDB(projectBooksWS, vdbCheckBook, new String[] { "checkBookWS.xmi" });
//		vdbManager.deployVDB(pathToCheckBookVDB);
//		vdbManager.createVDBDataSource(pathToCheckBookVDB);

	}

	/**
	 * Create RESTEasy WAR with security type None
	 */
	@Test
	@Deprecated
	public void restWarNoneTest() {

//		String rbWar = "restBooks";
//
//		Properties warProps = new Properties();
//		warProps.setProperty("type", WAR.RESTEASY_TYPE);
//		warProps.setProperty("contextName", rbWar);
//		warProps.setProperty("vdbJndiName", vdbBooksRest);
//		warProps.setProperty("saveLocation", teiidBot.toAbsolutePath("target"));
//		warProps.setProperty("securityType", WAR.NONE_SECURITY);
//		//new VDBManager().createWAR(warProps, pathToBooksRestVDB);
//
//		// import created war
//		Properties itemProps = new Properties();
//		itemProps.setProperty("dirName", teiidBot.toAbsolutePath("target"));
//		itemProps.setProperty("file", rbWar + ".war");
//		itemProps.setProperty("intoFolder", projectBooksRest);
//
//		new ImportManager().importGeneralItem(ImportGeneralItemWizard.Type.FILE_SYSTEM, itemProps);
//
//		new ModelExplorerManager().getWAR(projectBooksRest, rbWar + ".war").deploy(teiidServer.getName());
//		String url = "http://localhost:8080/" + rbWar + "/BooksView/book1/0201877562";
//
//		assertEquals(resultRest, new SimpleHttpClient(url).get());

	}

	/**
	 * Create RESTEasy WAR with security type Http Basic
	 */
	@Test
	@Deprecated
	public void restWarBasicTest() {

//		String rbWar = "restBooksBasic";
//
//		Properties warProps = new Properties();
//		warProps.setProperty("type", WAR.RESTEASY_TYPE);
//		warProps.setProperty("contextName", rbWar);
//		warProps.setProperty("vdbJndiName", vdbBooksRest);
//		warProps.setProperty("saveLocation", teiidBot.toAbsolutePath("target"));
//		warProps.setProperty("securityType", WAR.HTTPBasic_SECURITY);
//		warProps.setProperty("realm", "teiid-security");
//		warProps.setProperty("role", "user");
//
//		//new VDBManager().createWAR(warProps, pathToBooksRestVDB);
//
//		// import created war
//		Properties itemProps = new Properties();
//		itemProps.setProperty("dirName", teiidBot.toAbsolutePath("target"));
//		itemProps.setProperty("file", rbWar + ".war");
//		itemProps.setProperty("intoFolder", projectBooksRest);
//		new ImportManager().importGeneralItem(ImportGeneralItemWizard.Type.FILE_SYSTEM, itemProps);
//
//		new ModelExplorerManager().getWAR(projectBooksRest, rbWar + ".war").deploy(teiidServer.getName());
//
//		String username = teiidServer.getServerConfig().getServerBase().getProperty("teiidUser");
//		String password = teiidServer.getServerConfig().getServerBase().getProperty("teiidPassword");
//		SimpleHttpClient client = new SimpleHttpClient("http://localhost:8080/" + rbWar + "/BooksView/book1/0201877562")
//				.setBasicAuth(username, password);
//		assertEquals(resultRest, client.get());
	}

}
