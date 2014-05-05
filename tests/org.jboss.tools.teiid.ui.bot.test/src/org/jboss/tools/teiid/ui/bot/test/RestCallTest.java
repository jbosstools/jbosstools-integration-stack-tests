package org.jboss.tools.teiid.ui.bot.test;

import java.util.Properties;

import org.eclipse.swtbot.swt.finder.SWTBotTestCase;
import org.jboss.tools.teiid.reddeer.WAR;
import org.jboss.tools.teiid.reddeer.manager.ConnectionProfileManager;
import org.jboss.tools.teiid.reddeer.manager.ImportManager;
import org.jboss.tools.teiid.reddeer.manager.ModelExplorerManager;
import org.jboss.tools.teiid.reddeer.manager.ServerManager;
import org.jboss.tools.teiid.reddeer.manager.VDBManager;
import org.jboss.tools.teiid.reddeer.view.ModelExplorerView;
import org.jboss.tools.teiid.reddeer.wizard.ImportGeneralItemWizard;
import org.jboss.tools.teiid.ui.bot.test.requirement.PerspectiveRequirement.Perspective;
import org.junit.BeforeClass;
import org.junit.Test;
/**
 * TEIIDDES-1808
 * @author lfabriko
 *
 */
@Perspective(name = "Teiid Designer")
public class RestCallTest extends SWTBotTestCase {

	private static final String serverFile = "as7.properties";
	private static final String projectName = "RestCallTest";
	private static final String srcModel = "BooksSrc";
	private static final String viewModel1 = "BooksView1";
	private static final String viewModel2 = "BooksView2";
	private static final String vdbName1 = "restcall1";
	private static final String vdbName2 = "restcall2";
	private static final String archiveLocation = "resources/projects/"+projectName+".zip";
	private static final String serverName = "AS-7.1";
	private static TeiidBot teiidBot = new TeiidBot();
	
	private static final String oracleCP = "Oracle";
	private static final String oracleCPProps = "resources/db/oracle_books.properties";
	private static final String url1 = "http://localhost:8080/Rest1/BooksView1/book1/0201877562";
	private static final String url2 = "http://localhost:8080/Rest2/BooksView2/book2/0-201-65758-9";
	private String result = "<TheBooks><Book><ISBN>0201877562</ISBN><TITLE>Software Testing in the Real World</TITLE></Book></TheBooks>";
	private String result2 = "<TheBooks><Book><ISBN>0-201-65758-9</ISBN><TITLE>LDAP Programming with Java</TITLE></Book></TheBooks>";

	@BeforeClass
	public static void prepare(){
		try{new ServerManager().setDefaultTeiidInstanceTargetedVersion("8.2.x");} catch(Exception ex){}//switches back to 8.4 if server as 7.1 + teiid 8.2.0 is defined
		
		try{new ServerManager().addServer(serverFile);} catch(Exception ex){}
		
		Properties itemProps = new Properties();
		itemProps.setProperty("location", teiidBot.toAbsolutePath(archiveLocation));
		
		try{new ImportManager().importGeneralItem(ImportGeneralItemWizard.Type.EXISTING_PROJECTS_INTO_WORKSPACE, itemProps);}catch(Exception e){}
		
		try{new ConnectionProfileManager().createCPWithDriverDefinition(oracleCP, oracleCPProps);}catch(Exception e){}
		try{new ModelExplorerManager().changeConnectionProfile(oracleCP, projectName, srcModel);}catch(Exception e){}
	}
	
	@Test
	public void disconnectedTeiid(){
		String vdbJndiName1 = "RestTest1";
		String warCtxName1 = "Rest1";
		
		try{new ServerManager().startServer(serverName);}catch(Exception e){}
		
		//disconnect teiid instance
		try{new ServerManager().getServersViewExt().disconnectTeiidInstance(serverName);}catch(Exception e){}
		
		//create vdb
		try{new VDBManager().createVDB(projectName, vdbName1); }catch(Exception e){}
		try{new VDBManager().addModelsToVDB(projectName, vdbName1, new String[]{viewModel1});}catch(Exception e){}
		try{new VDBManager().getVDBEditor(projectName, vdbName1).close();}catch(Exception e){}
		
		//generate rest war
		Properties warProps = new Properties();
		warProps.setProperty("type", WAR.RESTEASY_TYPE);
		warProps.setProperty("contextName", warCtxName1);
		warProps.setProperty("vdbJndiName", vdbJndiName1);
		warProps.setProperty("saveLocation", teiidBot.toAbsolutePath("target"));
		warProps.setProperty("securityType", WAR.NONE_SECURITY);
		
		String[] pathToVDB = new String[]{projectName, vdbName1};
		
		try{WAR war = new VDBManager().createWAR(warProps, pathToVDB);}catch(Exception e){}
		
		//import created war
		Properties itemProps = new Properties();
		itemProps.setProperty("dirName", teiidBot.toAbsolutePath("target"));
		itemProps.setProperty("file", warCtxName1+".war");
		itemProps.setProperty("intoFolder", projectName);
		
		try{new ImportManager().importGeneralItem(ImportGeneralItemWizard.Type.FILE_SYSTEM, itemProps);}catch(Exception e){}
		
		//connect teiid instance
		try{new ServerManager().getServersViewExt().connectTeiidInstance(serverName);}catch(Exception e){}
		
		//testing purposes WAR 
		try{
			WAR war = new ModelExplorerManager().getWAR(projectName, warCtxName1+".war");
			war.deploy();}catch(Exception e){}
		
		//create data source for BooksSrc
		try{new ModelExplorerManager().createDataSource(ModelExplorerView.ConnectionSourceType.USE_CONNECTION_PROFILE_INFO, oracleCP, projectName, srcModel);}catch(Exception e){}
		//synchronize vdb before deploying
		try{new VDBManager().getVDBEditor(projectName, vdbName1).synchronizeAll();}catch(Exception e){}
		try{new VDBManager().getVDBEditor(projectName, vdbName1).close();}catch(Exception e){}
		
		//create src model for vdb, deploy vdb
		try{new VDBManager().createVDBDataSource(pathToVDB, vdbJndiName1, false);}catch(Exception e){}
		try{new VDBManager().deployVDB(pathToVDB);}catch(Exception e){}
		
		//run wget
		assertEquals(result, teiidBot.curl(url1));
		
		//stop server
		try{new ServerManager().stopServer(serverName);}catch(Exception e){}
	}
	
	@Test
	public void connectedTeiid(){
		String vdbJndiName1 = "RestTest2";
		String warCtxName1 = "Rest2";
		
		try{new ServerManager().startServer(serverName);}catch(Exception e){}
		
		//create vdb
		try{new VDBManager().createVDB(projectName, vdbName2); }catch(Exception e){}
		try{new VDBManager().addModelsToVDB(projectName, vdbName2, new String[]{viewModel2});}catch(Exception e){}
		try{new VDBManager().getVDBEditor(projectName, vdbName2).close();}catch(Exception e){}
		
		//generate rest war
		Properties warProps = new Properties();
		warProps.setProperty("type", WAR.RESTEASY_TYPE);
		warProps.setProperty("contextName", warCtxName1);
		warProps.setProperty("vdbJndiName", vdbJndiName1);
		warProps.setProperty("saveLocation", teiidBot.toAbsolutePath("target"));
		warProps.setProperty("securityType", WAR.NONE_SECURITY);
		
		String[] pathToVDB = new String[]{projectName, vdbName2};
		
		try{WAR war = new VDBManager().createWAR(warProps, pathToVDB);}catch(Exception e){}
		
		//import created war
		Properties itemProps = new Properties();
		itemProps.setProperty("dirName", teiidBot.toAbsolutePath("target"));
		itemProps.setProperty("file", warCtxName1+".war");
		itemProps.setProperty("intoFolder", projectName);
		
		try{new ImportManager().importGeneralItem(ImportGeneralItemWizard.Type.FILE_SYSTEM, itemProps);}catch(Exception e){}

		try{WAR war = new ModelExplorerManager().getWAR(projectName, warCtxName1+".war");
		war.deploy();}catch(Exception e){}
		
		//create data source for BooksSrc
		try{new ModelExplorerManager().createDataSource(ModelExplorerView.ConnectionSourceType.USE_CONNECTION_PROFILE_INFO, oracleCP, projectName, srcModel);}catch(Exception e){}
		
		//synchronize vdb before deploying
		try{new VDBManager().getVDBEditor(projectName, vdbName2).synchronizeAll();}catch(Exception e){}
		try{new VDBManager().getVDBEditor(projectName, vdbName2).close();}catch(Exception e){}
		
		//create src model for vdb, deploy vdb
		try{new VDBManager().createVDBDataSource(pathToVDB, vdbJndiName1, false);}catch(Exception e){}
		try{new VDBManager().deployVDB(pathToVDB);}catch(Exception e){}
		
		//run wget
		assertEquals(result2, teiidBot.curl(url2));
		
		//stop server
		try{new ServerManager().stopServer(serverName);}catch(Exception e){}
	}
}
