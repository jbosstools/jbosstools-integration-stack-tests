package org.jboss.tools.teiid.ui.bot.test;

import java.util.Properties;

import org.eclipse.swtbot.swt.finder.SWTBotTestCase;
import org.jboss.reddeer.junit.runner.RedDeerSuite;
import org.jboss.reddeer.requirements.server.ServerReqState;
import org.jboss.reddeer.swt.impl.menu.ShellMenu;
import org.jboss.tools.teiid.reddeer.WAR;
import org.jboss.tools.teiid.reddeer.editor.VDBEditor;
import org.jboss.tools.teiid.reddeer.manager.ConnectionProfileManager;
import org.jboss.tools.teiid.reddeer.manager.ImportManager;
import org.jboss.tools.teiid.reddeer.manager.ModelExplorerManager;
import org.jboss.tools.teiid.reddeer.manager.ServerManager;
import org.jboss.tools.teiid.reddeer.manager.VDBManager;
import org.jboss.tools.teiid.reddeer.requirement.TeiidServerRequirement.TeiidServer;
import org.jboss.tools.teiid.reddeer.wizard.ImportGeneralItemWizard;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * WAR tests (REST, JBossWS-CXF) with security None | HttpBasic
 * @author lfabriko
 *
 */
@RunWith(RedDeerSuite.class)
@TeiidServer(state = ServerReqState.PRESENT)
public class WARTest extends SWTBotTestCase {

	public static final String MODEL_PROJECT = "jdbcImportTest";

	private static TeiidBot teiidBot = new TeiidBot();
	private static final String DV6_SERVER = "EAP-6.1";
	private static final String oracleCP = "Oracle";
	private static final String oracleCPProps = "resources/db/oracle_books.properties";
	private static final String projectBooksWS = "BooksWS";
	private static final String vdbCheckBook="checkBookVdb";
	private static final String[] pathToCheckBookVDB = new String[]{projectBooksWS, vdbCheckBook+".vdb"};
	private static final String projectBooksRest = "BooksRest";
	private static final String vdbBooksRest = "booksRest";
	private static final String[] pathToBooksRestVDB = new String[]{projectBooksRest, vdbBooksRest+".vdb"};
	private String resultRest = "<TheBooks><Book><ISBN>0201877562</ISBN><TITLE>Software Testing in the Real World</TITLE></Book></TheBooks>";
	
	@BeforeClass
	public static void before(){
		new ShellMenu("Project", "Build Automatically").select();
		
		//JBossWS-CXF war
		new ImportManager().importProject("resources/projects/BooksWS");
		new ConnectionProfileManager().createCPWithDriverDefinition(oracleCP, oracleCPProps);
		new ModelExplorerManager().changeConnectionProfile(oracleCP, projectBooksWS, "books.xmi");
		VDBEditor ed = new VDBManager().getVDBEditor(projectBooksWS, vdbCheckBook);
		ed.synchronizeAll();
		ed.close();
		
		//RESTEasy war
		new ImportManager().importProject("resources/projects/BooksRest");
		new ModelExplorerManager().changeConnectionProfile(oracleCP, projectBooksRest, "BooksSrc.xmi");
		ed = new VDBManager().getVDBEditor(projectBooksRest, vdbBooksRest);
		ed.synchronizeAll();
		ed.close();
		
		new ServerManager().startServer(DV6_SERVER);
		
		
		new VDBManager().deployVDB(pathToCheckBookVDB);
		new VDBManager().createVDBDataSource(pathToCheckBookVDB);
		
		new VDBManager().deployVDB(pathToBooksRestVDB);
		new VDBManager().createVDBDataSource(pathToBooksRestVDB);
		
		new ServerManager().stopServer(DV6_SERVER);
	}
	
	//@Test
	public void teiidTest(){//TODO --> move to "server - smoke tests": create source model from VDB, run DV6 and SOA5 simple mgmt tests 
			//firstly create teiid vdb
		//create source model from teiid vdb
	}
	
	/**
	 * Generate and test JBossWS-CXF WAR with security type None
	 */
	@Test
	public void jbossWSCXFNoneWarTest(){
		try {
			new ServerManager().startServer(DV6_SERVER);
		Properties warProps = new Properties();
		warProps.setProperty("type", WAR.JBOSSWS_CXF_TYPE);
		warProps.setProperty("contextName", vdbCheckBook);
		warProps.setProperty("vdbJndiName", vdbCheckBook);
		warProps.setProperty("saveLocation", teiidBot.toAbsolutePath("target"));
		//http://localhost:8080/checkBookVdb/BooksInterface?wsdl
		try{WAR war = new VDBManager().createWAR(warProps, pathToCheckBookVDB);}catch(Exception e){e.printStackTrace();}
		
		Properties itemProps = new Properties();
		itemProps.setProperty("dirName", teiidBot.toAbsolutePath("target"));
		itemProps.setProperty("intoFolder", projectBooksWS);
		itemProps.setProperty("file", vdbCheckBook+".war");
		new ImportManager().importGeneralItem(ImportGeneralItemWizard.Type.FILE_SYSTEM, itemProps);
		new ModelExplorerManager().getWAR(projectBooksWS, vdbCheckBook+".war").deploy();
		
		String curlNOK = "curl -H \"Content-Type: text/xml; charset=utf-8\" -H \"SOAPAction:\"  -d @"+teiidBot.toAbsolutePath("resources/wsdl/requestOracleNOK.xml")+" -X POST http://localhost:8080/checkBookVdb/BooksInterface?wsdl";
		String curlOK = "curl -H \"Content-Type: text/xml; charset=utf-8\" -H \"SOAPAction:\"  -d @"+teiidBot.toAbsolutePath("resources/wsdl/requestOracleOK.xml")+" -X POST http://localhost:8080/checkBookVdb/BooksInterface?wsdl";
		String responseNOK = teiidBot.curl(curlNOK);
		String responseOK = teiidBot.curl(curlOK);
		assertEquals(teiidBot.loadFileAsString(teiidBot.toAbsolutePath("resources/wsdl/responseOracleNOK.xml")), responseNOK);//sometimes fails even though via command line it works; curl -u testuser:testpassword -d @resources/wsdl/requestOracleNOK.xml -X POST http://localhost:8080/checkBookVdbBasic/BooksInterface?wsdl
		assertEquals(teiidBot.loadFileAsString(teiidBot.toAbsolutePath("resources/wsdl/responseOracleOK.xml")), responseOK);
		} catch (Exception e){
			e.printStackTrace();
		}
		new ServerManager().stopServer(DV6_SERVER);
	}
	
	/**
	 * Generate and test JBossWS-CXF WAR with security type Http Basic
	 */
	@Test
	public void jbossWSCXFHttpBasicWarTest(){
		try {
			new ServerManager().startServer(DV6_SERVER);
		String warCheckBookBasic = vdbCheckBook+"Basic";
		Properties warProps = new Properties();
		warProps.setProperty("type", WAR.JBOSSWS_CXF_TYPE);
		warProps.setProperty("contextName", warCheckBookBasic);
		warProps.setProperty("vdbJndiName", vdbCheckBook);
		warProps.setProperty("saveLocation", teiidBot.toAbsolutePath("target"));
		warProps.setProperty("securityType", WAR.HTTPBasic_SECURITY);
		warProps.setProperty("realm", "teiid-security");
		warProps.setProperty("role", "MySecureWebRole");///this has to be set also in teiid-security-users,roles
		//http://localhost:8080/checkBookVdbBasic/BooksInterface?wsdl
		try{WAR war = new VDBManager().createWAR(warProps, pathToCheckBookVDB);}catch(Exception e){e.printStackTrace();}
		
		Properties itemProps = new Properties();
		itemProps.setProperty("dirName", teiidBot.toAbsolutePath("target"));
		itemProps.setProperty("intoFolder", projectBooksWS);
		itemProps.setProperty("file", warCheckBookBasic+".war");
		new ImportManager().importGeneralItem(ImportGeneralItemWizard.Type.FILE_SYSTEM, itemProps);
		new ModelExplorerManager().getWAR(projectBooksWS, warCheckBookBasic+".war").deploy();
		
		String curlNOK = "curl -u testuser:testpassword -H \"Content-Type: text/xml; charset=utf-8\" -H \"SOAPAction:\"  -d @"+teiidBot.toAbsolutePath("resources/wsdl/requestOracleNOK.xml")+" -X POST http://localhost:8080/checkBookVdbBasic/BooksInterface?wsdl";
		String curlOK = "curl -u testuser:testpassword -H \"Content-Type: text/xml; charset=utf-8\" -H \"SOAPAction:\"  -d @"+teiidBot.toAbsolutePath("resources/wsdl/requestOracleOK.xml")+" -X POST http://localhost:8080/checkBookVdbBasic/BooksInterface?wsdl";
		String responseNOK = teiidBot.curl(curlNOK);
		String responseOK = teiidBot.curl(curlOK);
		assertEquals(teiidBot.loadFileAsString(teiidBot.toAbsolutePath("resources/wsdl/responseOracleNOK.xml")), responseNOK);
		assertEquals(teiidBot.loadFileAsString(teiidBot.toAbsolutePath("resources/wsdl/responseOracleOK.xml")), responseOK);
		} catch (Exception e){
			e.printStackTrace();
		}
		new ServerManager().stopServer(DV6_SERVER);
	}
	
	/**
	 * Create RESTEasy WAR with security type None
	 */
	@Test
	public void restWarNoneTest(){
		try {
			new ServerManager().startServer(DV6_SERVER);
		String rbWar = "restBooks";

		Properties warProps = new Properties();
		warProps.setProperty("type", WAR.RESTEASY_TYPE);
		warProps.setProperty("contextName", rbWar);
		warProps.setProperty("vdbJndiName", vdbBooksRest);
		warProps.setProperty("saveLocation", teiidBot.toAbsolutePath("target"));
		warProps.setProperty("securityType", WAR.NONE_SECURITY);
		try{
			new VDBManager().createWAR(warProps, pathToBooksRestVDB);
		}catch(Exception e){
			e.printStackTrace();
		}
		
		//import created war
		Properties itemProps = new Properties();
		itemProps.setProperty("dirName", teiidBot.toAbsolutePath("target"));
		itemProps.setProperty("file", rbWar + ".war");
		itemProps.setProperty("intoFolder", projectBooksRest);

		try {
			new ImportManager().importGeneralItem(ImportGeneralItemWizard.Type.FILE_SYSTEM, itemProps);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		new ModelExplorerManager().getWAR(projectBooksRest, rbWar+".war").deploy();
		
		String url = "http://localhost:8080/"+rbWar+"/BooksView/book1/0201877562";
		
		assertEquals(resultRest, teiidBot.curl(url));
		} catch (Exception e){
			e.printStackTrace();
		}
		new ServerManager().stopServer(DV6_SERVER);
	}
	
	/**
	 * Create RESTEasy WAR with security type Http Basic
	 */
	@Test
	public void restWarBasicTest(){
		try {
			new ServerManager().startServer(DV6_SERVER);
		String rbWar = "restBooksBasic";

		Properties warProps = new Properties();
		warProps.setProperty("type", WAR.RESTEASY_TYPE);
		warProps.setProperty("contextName", rbWar);
		warProps.setProperty("vdbJndiName", vdbBooksRest);
		warProps.setProperty("saveLocation", teiidBot.toAbsolutePath("target"));
		warProps.setProperty("securityType", WAR.HTTPBasic_SECURITY);
		warProps.setProperty("realm", "teiid-security");
		warProps.setProperty("role", "MySecureWebRole");
		try{
			new VDBManager().createWAR(warProps, pathToBooksRestVDB);
		}catch(Exception e){
			e.printStackTrace();
		}
		
		//import created war
		Properties itemProps = new Properties();
		itemProps.setProperty("dirName", teiidBot.toAbsolutePath("target"));
		itemProps.setProperty("file", rbWar + ".war");
		itemProps.setProperty("intoFolder", projectBooksRest);

		try {
			new ImportManager().importGeneralItem(ImportGeneralItemWizard.Type.FILE_SYSTEM, itemProps);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		new ModelExplorerManager().getWAR(projectBooksRest, rbWar+".war").deploy();
		
		String url = "-u testuser:testpassword http://localhost:8080/"+rbWar+"/BooksView/book1/0201877562";
		
		assertEquals(resultRest, teiidBot.curl(url));
		} catch (Exception e){
			e.printStackTrace();
		}
		new ServerManager().stopServer(DV6_SERVER);
	}

}
