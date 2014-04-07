package org.jboss.tools.teiid.ui.bot.test;

import java.util.Properties;

import org.eclipse.swtbot.swt.finder.SWTBotTestCase;
import org.jboss.reddeer.swt.impl.menu.ShellMenu;
import org.jboss.tools.teiid.reddeer.WAR;
import org.jboss.tools.teiid.reddeer.editor.VDBEditor;
import org.jboss.tools.teiid.reddeer.manager.ConnectionProfileManager;
import org.jboss.tools.teiid.reddeer.manager.ImportManager;
import org.jboss.tools.teiid.reddeer.manager.ModelExplorerManager;
import org.jboss.tools.teiid.reddeer.manager.ServerManager;
import org.jboss.tools.teiid.reddeer.manager.VDBManager;
import org.jboss.tools.teiid.reddeer.wizard.ImportGeneralItemWizard;
import org.jboss.tools.teiid.ui.bot.test.requirement.PerspectiveRequirement.Perspective;
import org.jboss.tools.teiid.ui.bot.test.requirement.ServerRequirement.Server;
import org.junit.BeforeClass;
import org.junit.Test;
import org.jboss.tools.teiid.ui.bot.test.requirement.ServerRequirement.Server;
import org.jboss.tools.teiid.ui.bot.test.requirement.ServerRequirement.State;
import org.jboss.tools.teiid.ui.bot.test.requirement.ServerRequirement.Type;

/**
 * 
 * @author lfabriko
 *
 */
@Perspective(name = "Teiid Designer")
@Server(type = Type.ALL, state = State.NOT_RUNNING)
public class ServerOpsTest extends SWTBotTestCase {

	public static final String MODEL_PROJECT = "jdbcImportTest";

	private static TeiidBot teiidBot = new TeiidBot();
	private static final String DV6_PROPERTIES = "dv6.properties";
	private static final String DV6_SERVER = "EAP-6.1";
	private static final String oracleCP = "Oracle";
	private static final String oracleCPProps = "resources/db/oracle_books.properties";
	
	
	@BeforeClass
	public static void before(){
		new ShellMenu("Project", "Build Automatically").select();
		//new ModelExplorerManager().createProject(MODEL_PROJECT);
		//new ServerManager().addServer(DV6_PROPERTIES);
		//new ServerManager().startServer(DV6_SERVER);
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
		String projectName = "BooksWS";
		String vdbName="checkBookVdb";
		String[] pathToVDB = new String[]{projectName, vdbName+".vdb"};
		new ImportManager().importProject("resources/projects/BooksWS");
		new ConnectionProfileManager().createCPWithDriverDefinition(oracleCP, oracleCPProps);
		new ModelExplorerManager().changeConnectionProfile(oracleCP, projectName, "books.xmi");
		VDBEditor ed = new VDBManager().getVDBEditor(projectName, vdbName);
		ed.synchronizeAll();
		ed.close();
		new ServerManager().startServer(DV6_SERVER);
		
		new VDBManager().deployVDB(pathToVDB);
		new VDBManager().createVDBDataSource(pathToVDB);
		Properties warProps = new Properties();
		warProps.setProperty("type", WAR.JBOSSWS_CXF_TYPE);
		warProps.setProperty("contextName", vdbName);
		warProps.setProperty("vdbJndiName", vdbName);
		warProps.setProperty("saveLocation", teiidBot.toAbsolutePath("target"));
		//http://localhost:8080/checkBookVdb/BooksInterface?wsdl
		try{WAR war = new VDBManager().createWAR(warProps, pathToVDB);}catch(Exception e){e.printStackTrace();}
		Properties itemProps = new Properties();
		itemProps.setProperty("dirName", teiidBot.toAbsolutePath("target"));
		itemProps.setProperty("intoFolder", projectName);
		itemProps.setProperty("file", vdbName+".war");
		new ImportManager().importGeneralItem(ImportGeneralItemWizard.FILE_SYSTEM, itemProps);
		new ModelExplorerManager().getWAR(projectName, vdbName+".war").deploy();
		
		String curlNOK = "curl -H \"Content-Type: text/xml; charset=utf-8\" -H \"SOAPAction:\"  -d @"+teiidBot.toAbsolutePath("resources/wsdl/requestOracleNOK.xml")+" -X POST http://localhost:8080/checkBookVdb/BooksInterface?wsdl";
		String curlOK = "curl -H \"Content-Type: text/xml; charset=utf-8\" -H \"SOAPAction:\"  -d @"+teiidBot.toAbsolutePath("resources/wsdl/requestOracleOK.xml")+" -X POST http://localhost:8080/checkBookVdb/BooksInterface?wsdl";
		String responseNOK = teiidBot.curl(curlNOK);
		String responseOK = teiidBot.curl(curlOK);
		assertEquals(teiidBot.loadFileAsString(teiidBot.toAbsolutePath("resources/wsdl/responseOracleNOK.xml")), responseNOK);
		assertEquals(teiidBot.loadFileAsString(teiidBot.toAbsolutePath("resources/wsdl/responseOracleOK.xml")), responseOK);
	}
	

}
