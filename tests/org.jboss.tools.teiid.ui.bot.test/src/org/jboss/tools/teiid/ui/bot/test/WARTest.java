package org.jboss.tools.teiid.ui.bot.test;

import java.util.Properties;

import org.eclipse.swtbot.swt.finder.SWTBotTestCase;
import org.jboss.reddeer.common.wait.AbstractWait;
import org.jboss.reddeer.common.wait.TimePeriod;
import org.jboss.reddeer.junit.requirement.inject.InjectRequirement;
import org.jboss.reddeer.junit.runner.RedDeerSuite;
import org.jboss.reddeer.requirements.server.ServerReqState;
import org.jboss.tools.teiid.reddeer.WAR;
import org.jboss.tools.teiid.reddeer.manager.ConnectionProfilesConstants;
import org.jboss.tools.teiid.reddeer.manager.ImportManager;
import org.jboss.tools.teiid.reddeer.manager.ModelExplorerManager;
import org.jboss.tools.teiid.reddeer.manager.VDBManager;
import org.jboss.tools.teiid.reddeer.requirement.TeiidServerRequirement;
import org.jboss.tools.teiid.reddeer.requirement.TeiidServerRequirement.TeiidServer;
import org.jboss.tools.teiid.reddeer.util.SimpleHttpClient;
import org.jboss.tools.teiid.reddeer.wizard.ImportGeneralItemWizard;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * WAR tests (REST, JBossWS-CXF) with security None | HttpBasic
 * @author lfabriko, felias
 *
 */
@RunWith(RedDeerSuite.class)
@TeiidServer(state = ServerReqState.RUNNING, connectionProfiles = {
		ConnectionProfilesConstants.ORACLE_11G_BOOKS
		})
public class WARTest extends SWTBotTestCase {
	public static final String MODEL_PROJECT = "jdbcImportTest";
	@InjectRequirement
	private static TeiidServerRequirement teiidServer;
	private static TeiidBot teiidBot = new TeiidBot();
	private static final String projectBooksWS = "BooksWS";
	private static final String vdbCheckBook="checkBookVdb";
	private static final String[] pathToCheckBookVDB = new String[]{projectBooksWS, vdbCheckBook+".vdb"};
	private static final String projectBooksRest = "BooksRest";
	private static final String vdbBooksRest = "booksRest";
	private static final String[] pathToBooksRestVDB = new String[]{projectBooksRest, vdbBooksRest+".vdb"};
	private String resultRest = "<TheBooks><Book><ISBN>0201877562</ISBN><TITLE>Software Testing in the Real World</TITLE></Book></TheBooks>";
	
	@BeforeClass
	public static void before() {
		VDBManager vdbManager = new VDBManager();
		
		// RESTEasy war
		new ImportManager().importProject("resources/projects/BooksRest");
		new ModelExplorerManager().changeConnectionProfile(ConnectionProfilesConstants.ORACLE_11G_BOOKS,
				projectBooksRest, "BooksSrc.xmi");
		vdbManager.createVDB(projectBooksRest, vdbBooksRest);
		vdbManager.addModelsToVDB(projectBooksRest, vdbBooksRest, new String[]{"BooksView.xmi"});
		vdbManager.deployVDB(pathToBooksRestVDB);
		vdbManager.createVDBDataSource(pathToBooksRestVDB);
		

		// JBossWS-CXF war
		new ImportManager().importProject("resources/projects/BooksWS");
		new ModelExplorerManager().changeConnectionProfile(ConnectionProfilesConstants.ORACLE_11G_BOOKS,
				projectBooksWS, "books.xmi");
		vdbManager.createVDB(projectBooksWS, vdbCheckBook);
		vdbManager.addModelsToVDB(projectBooksWS, vdbCheckBook, new String[]{"checkBookWS.xmi"});
		vdbManager.deployVDB(pathToCheckBookVDB);
		vdbManager.createVDBDataSource(pathToCheckBookVDB);
		
	}

	/**
	 * Generate and test JBossWS-CXF WAR with security type None
	 */
	@Test
	public void jbossWSCXFNoneWarTest(){
		
		Properties warProps = new Properties();
		warProps.setProperty("type", WAR.JBOSSWS_CXF_TYPE);
		warProps.setProperty("contextName", vdbCheckBook);
		warProps.setProperty("vdbJndiName", vdbCheckBook);
		warProps.setProperty("saveLocation", teiidBot.toAbsolutePath("target"));
		//http://localhost:8080/checkBookVdb/BooksInterface?wsdl
		new VDBManager().createWAR(warProps, pathToCheckBookVDB);
		
		Properties itemProps = new Properties();
		itemProps.setProperty("dirName", teiidBot.toAbsolutePath("target"));
		itemProps.setProperty("intoFolder", projectBooksWS);
		itemProps.setProperty("file", vdbCheckBook+".war");
		new ImportManager().importGeneralItem(ImportGeneralItemWizard.Type.FILE_SYSTEM, itemProps);
		new ModelExplorerManager().getWAR(projectBooksWS, vdbCheckBook+".war").deploy(teiidServer.getName());
		

		String nok = new SimpleHttpClient("http://localhost:8080/" + vdbCheckBook + "/BooksInterface?wsdl")
			.addHeader("Content-Type", "text/xml; charset=utf-8")
			.addHeader("SOAPAction","")
			.post(teiidBot.loadFileAsString(teiidBot.toAbsolutePath("resources/wsdl/requestOracleNOK.xml")));
		assertEquals(teiidBot.loadFileAsString(teiidBot.toAbsolutePath("resources/wsdl/responseOracleNOK.xml")), nok);

		String ok = new SimpleHttpClient("http://localhost:8080/" + vdbCheckBook + "/BooksInterface?wsdl")
			.addHeader("Content-Type", "text/xml; charset=utf-8")
			.addHeader("SOAPAction", "")
			.post(teiidBot.loadFileAsString(teiidBot.toAbsolutePath("resources/wsdl/requestOracleOK.xml")));
		assertEquals(teiidBot.loadFileAsString(teiidBot.toAbsolutePath("resources/wsdl/responseOracleOK.xml")), ok);
	}
	
	/**
	 * Generate and test JBossWS-CXF WAR with security type Http Basic
	 */
	@Test
	public void jbossWSCXFHttpBasicWarTest(){
		String warCheckBookBasic = vdbCheckBook+"Basic";
		Properties warProps = new Properties();
		warProps.setProperty("type", WAR.JBOSSWS_CXF_TYPE);
		warProps.setProperty("contextName", warCheckBookBasic);
		warProps.setProperty("vdbJndiName", vdbCheckBook);
		warProps.setProperty("saveLocation", teiidBot.toAbsolutePath("target"));
		warProps.setProperty("securityType", WAR.HTTPBasic_SECURITY);
		warProps.setProperty("realm", "teiid-security");
		warProps.setProperty("role", "user");///this has to be set also in teiid-security-users,roles
		//http://localhost:8080/checkBookVdbBasic/BooksInterface?wsdl

		new VDBManager().createWAR(warProps, pathToCheckBookVDB);
		
		Properties itemProps = new Properties();
		itemProps.setProperty("dirName", teiidBot.toAbsolutePath("target"));
		itemProps.setProperty("intoFolder", projectBooksWS);
		itemProps.setProperty("file", warCheckBookBasic+".war");
		new ImportManager().importGeneralItem(ImportGeneralItemWizard.Type.FILE_SYSTEM, itemProps);
		new ModelExplorerManager().getWAR(projectBooksWS, warCheckBookBasic+".war").deploy(teiidServer.getName());
		
		String username = teiidServer.getServerConfig().getServerBase().getProperty("teiidUser");
		String password = teiidServer.getServerConfig().getServerBase().getProperty("teiidPassword");
		
		String nok = new SimpleHttpClient("http://localhost:8080/" + warCheckBookBasic + "/BooksInterface?wsdl")
			.setBasicAuth(username, password)
			.addHeader("Content-Type", "text/xml; charset=utf-8")
			.addHeader("SOAPAction","")
			.post(teiidBot.loadFileAsString(teiidBot.toAbsolutePath("resources/wsdl/requestOracleNOK.xml")));
		assertEquals(teiidBot.loadFileAsString(teiidBot.toAbsolutePath("resources/wsdl/responseOracleNOK.xml")), nok);
		
		String ok = new SimpleHttpClient("http://localhost:8080/" + warCheckBookBasic + "/BooksInterface?wsdl")
			.setBasicAuth(username, password)
			.addHeader("Content-Type", "text/xml; charset=utf-8")
			.addHeader("SOAPAction", "")
			.post(teiidBot.loadFileAsString(teiidBot.toAbsolutePath("resources/wsdl/requestOracleOK.xml")));
		assertEquals(teiidBot.loadFileAsString(teiidBot.toAbsolutePath("resources/wsdl/responseOracleOK.xml")), ok);
	}
	
	/**
	 * Create RESTEasy WAR with security type None
	 */
	@Test
	public void restWarNoneTest(){
	
		String rbWar = "restBooks";

		Properties warProps = new Properties();
		warProps.setProperty("type", WAR.RESTEASY_TYPE);
		warProps.setProperty("contextName", rbWar);
		warProps.setProperty("vdbJndiName", vdbBooksRest);
		warProps.setProperty("saveLocation", teiidBot.toAbsolutePath("target"));
		warProps.setProperty("securityType", WAR.NONE_SECURITY);
		new VDBManager().createWAR(warProps, pathToBooksRestVDB);
	
		//import created war
		Properties itemProps = new Properties();
		itemProps.setProperty("dirName", teiidBot.toAbsolutePath("target"));
		itemProps.setProperty("file", rbWar + ".war");
		itemProps.setProperty("intoFolder", projectBooksRest);


		new ImportManager().importGeneralItem(ImportGeneralItemWizard.Type.FILE_SYSTEM, itemProps);

		
		new ModelExplorerManager().getWAR(projectBooksRest, rbWar+".war").deploy(teiidServer.getName());
		String url = "http://localhost:8080/"+rbWar+"/BooksView/book1/0201877562";
		
		assertEquals(resultRest, new SimpleHttpClient(url).get());
		
	}
	
	/**
	 * Create RESTEasy WAR with security type Http Basic
	 */
	@Test
	public void restWarBasicTest(){

		String rbWar = "restBooksBasic";

		Properties warProps = new Properties();
		warProps.setProperty("type", WAR.RESTEASY_TYPE);
		warProps.setProperty("contextName", rbWar);
		warProps.setProperty("vdbJndiName", vdbBooksRest);
		warProps.setProperty("saveLocation", teiidBot.toAbsolutePath("target"));
		warProps.setProperty("securityType", WAR.HTTPBasic_SECURITY);
		warProps.setProperty("realm", "teiid-security");
		warProps.setProperty("role", "user");

		new VDBManager().createWAR(warProps, pathToBooksRestVDB);

		//import created war
		Properties itemProps = new Properties();
		itemProps.setProperty("dirName", teiidBot.toAbsolutePath("target"));
		itemProps.setProperty("file", rbWar + ".war");
		itemProps.setProperty("intoFolder", projectBooksRest);
		new ImportManager().importGeneralItem(ImportGeneralItemWizard.Type.FILE_SYSTEM, itemProps);
	
		
		new ModelExplorerManager().getWAR(projectBooksRest, rbWar+".war").deploy(teiidServer.getName());
		
		String username = teiidServer.getServerConfig().getServerBase().getProperty("teiidUser");
		String password = teiidServer.getServerConfig().getServerBase().getProperty("teiidPassword");
		SimpleHttpClient client = new SimpleHttpClient("http://localhost:8080/" + rbWar + "/BooksView/book1/0201877562")
				.setBasicAuth(username, password);
		assertEquals(resultRest, client.get());
	}

}
