package org.jboss.tools.teiid.ui.bot.test;

import java.util.ArrayList;
import java.util.Properties;

import org.eclipse.swtbot.swt.finder.SWTBotTestCase;
import org.jboss.reddeer.swt.condition.JobIsRunning;
import org.jboss.reddeer.swt.impl.menu.ShellMenu;
import org.jboss.reddeer.swt.wait.TimePeriod;
import org.jboss.reddeer.swt.wait.WaitWhile;
import org.jboss.tools.teiid.reddeer.Procedure;
import org.jboss.tools.teiid.reddeer.Table;
import org.jboss.tools.teiid.reddeer.manager.ConnectionProfileManager;
import org.jboss.tools.teiid.reddeer.manager.ImportManager;
import org.jboss.tools.teiid.reddeer.manager.ModelExplorerManager;
import org.jboss.tools.teiid.reddeer.manager.PerspectiveAndViewManager;
import org.jboss.tools.teiid.reddeer.manager.ServerManager;
import org.jboss.tools.teiid.reddeer.view.ModelExplorer;
import org.jboss.tools.teiid.reddeer.wizard.ImportGeneralItemWizard;
import org.jboss.tools.teiid.ui.bot.test.requirement.PerspectiveRequirement.Perspective;
import org.jboss.tools.teiid.ui.bot.test.requirement.ServerRequirement.Server;
import org.jboss.tools.teiid.ui.bot.test.requirement.ServerRequirement.State;
import org.jboss.tools.teiid.ui.bot.test.requirement.ServerRequirement.Type;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

@Perspective(name = "Teiid Designer")
@Server(type = Type.ALL, state = State.NOT_RUNNING)
public class ProcedurePreviewTest extends SWTBotTestCase{

	private static final String PROJECT_NAME = "Partssupplier";
	private static final String MODEL_SRC_NAME = "hsqldbParts";
	private static final String MODEL_VIEW_NAME = "view";
	private static final String DS1_DATASOURCE = "resources/db/dv6-ds1.properties";
	private static final String HSQLDB_PROFILE = "Generic HSQLDB Profile";
	private static final String SERVER_NAME = new ServerManager().getServerName("swtbot.properties");
	private static final String UDF_LIB_PATH = "resources/projects/java/MyTestUdf/lib/";
	private static final String UDF_LIB = "MyTestUdf-1.0-SNAPSHOT.jar";
	
	private static TeiidBot teiidBot = new TeiidBot();

	@BeforeClass
	public static void createModelProject() {
		new ShellMenu("Project", "Build Automatically").select();
		new ConnectionProfileManager().createCPWithDriverDefinition(HSQLDB_PROFILE, DS1_DATASOURCE);
		new ImportManager().importProject("resources/projects/"+PROJECT_NAME);
		new ModelExplorer().changeConnectionProfile(HSQLDB_PROFILE, PROJECT_NAME, MODEL_SRC_NAME);
		new ServerManager().startServer(SERVER_NAME);
	}
	
	@AfterClass
	public static void shutdown(){
		new ServerManager().stopServer(SERVER_NAME);
	}
	
	@Before
	public void beforeMethod(){
		new PerspectiveAndViewManager().openTeiidDesignerPerspective();
		new WaitWhile(new JobIsRunning(), TimePeriod.LONG);
	}
	
	@Test
	public void relViewProcedure(){
		String proc = "proc";
		Properties props = new Properties();
		props.setProperty("type", Procedure.Type.RELVIEW_PROCEDURE);
		props.setProperty("includeResultSet", "true");
		props.setProperty("resultSetName", "rs");
		props.setProperty("cols", "color");
		props.setProperty("params", "id");
		props.setProperty("sql", "CREATE VIRTUAL PROCEDURE BEGIN select hsqldbParts.PARTS.PART_COLOR AS color from hsqldbParts.PARTS where hsqldbParts.PARTS.PART_ID=view.proc.id; END");
		
		new ModelExplorerManager().getModelExplorerView().newProcedure(PROJECT_NAME, MODEL_VIEW_NAME+".xmi", proc, props);
		
		new WaitWhile(new JobIsRunning(), TimePeriod.LONG);
		ArrayList<String> params = new ArrayList<String>();
		params.add("P300");
		new ModelExplorerManager().previewModelObject(params, PROJECT_NAME, MODEL_VIEW_NAME+".xmi", proc);
		String query = "select * from ( exec \"" + MODEL_VIEW_NAME+"\".\""+proc+"\"('P300') ) AS X_X";
		new WaitWhile(new JobIsRunning(), TimePeriod.LONG);
		assertTrue(new ModelExplorerManager().checkPreviewOfModelObject(query));
	}
	
	@Test
	public void relViewUDF(){
		//import lib/TestUDF.jar
		Properties props = new Properties();
		props.setProperty("dirName", teiidBot.toAbsolutePath(UDF_LIB_PATH));
		props.setProperty("intoFolder", PROJECT_NAME);
		props.setProperty("file", UDF_LIB);
		props.setProperty("createTopLevel", "true");
		new ImportManager().importGeneralItem(ImportGeneralItemWizard.Type.FILE_SYSTEM, props);
		
		//create UDF
		String proc = "udfConcatNull";
		props = new Properties();
		props.setProperty("type", Procedure.Type.RELVIEW_USER_DEFINED_FUNCTION);
		props.setProperty("params", "stringLeft,stringRight");
		props.setProperty("returnParam", "concatenatedResult");
		props.setProperty("functionCategory", "MY_TESTING_FUNCTION_CATEGORY");
		props.setProperty("javaClass", "userdefinedfunctions.MyConcatNull");//see decompiled jar
		props.setProperty("javaMethod", "myConcatNull");
		props.setProperty("udfJarPath", "lib/"+UDF_LIB);
		
		new ModelExplorerManager().getModelExplorerView().newProcedure(PROJECT_NAME, MODEL_VIEW_NAME+".xmi", proc, props);
		
		//create table to test -> use UDF in transformation
		String table = "tab";
		String query = "select udfConcatNull(hsqldbParts.PARTS.PART_NAME,hsqldbParts.PARTS.PART_WEIGHT) as NAME_WEIGHT from hsqldbParts.PARTS";
		props = new Properties();
		props.setProperty("sql", query);
		new ModelExplorerManager().getModelExplorerView().newTable(table, Table.Type.VIEW, props, PROJECT_NAME, MODEL_VIEW_NAME+".xmi");
	
		new ModelExplorerManager().previewModelObject(null, PROJECT_NAME, MODEL_VIEW_NAME+".xmi", table);
		String previewQuery = teiidBot.generateTablePreviewQuery(MODEL_VIEW_NAME, table);
		assertTrue(new ModelExplorerManager().checkPreviewOfModelObject(previewQuery));
		
		//TODO? test also via procedure?
	}
	
	//TODO source defined functions - test in importTest with server
}
