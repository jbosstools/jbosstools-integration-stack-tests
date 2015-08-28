package org.jboss.tools.teiid.ui.bot.test;

import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Properties;

import org.jboss.reddeer.junit.runner.RedDeerSuite;
import org.jboss.reddeer.requirements.server.ServerReqState;
import org.jboss.reddeer.core.condition.JobIsRunning;
import org.jboss.reddeer.swt.impl.menu.ShellMenu;
import org.jboss.reddeer.common.wait.TimePeriod;
import org.jboss.reddeer.common.wait.WaitWhile;
import org.jboss.tools.teiid.reddeer.Procedure;
import org.jboss.tools.teiid.reddeer.Table;
import org.jboss.tools.teiid.reddeer.manager.ConnectionProfilesConstants;
import org.jboss.tools.teiid.reddeer.manager.ImportManager;
import org.jboss.tools.teiid.reddeer.manager.ModelExplorerManager;
import org.jboss.tools.teiid.reddeer.manager.PerspectiveAndViewManager;
import org.jboss.tools.teiid.reddeer.requirement.TeiidServerRequirement.TeiidServer;
import org.jboss.tools.teiid.reddeer.view.ModelExplorer;
import org.jboss.tools.teiid.reddeer.wizard.ImportGeneralItemWizard;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(RedDeerSuite.class)
@TeiidServer(state = ServerReqState.RUNNING, connectionProfiles = {ConnectionProfilesConstants.ORACLE_11G_PARTS_SUPPLIER})
public class ProcedurePreviewTest {

	private static final String PROJECT_NAME = "Partssupplier";
	private static final String MODEL_SRC_NAME = "hsqldbParts";
	private static final String MODEL_VIEW_NAME = "view";
	private static final String PROFILE_NAME= ConnectionProfilesConstants.ORACLE_11G_PARTS_SUPPLIER;
	private static final String UDF_LIB_PATH = "target/proc-udf/MyTestUdf/lib/";
	private static final String UDF_LIB = "MyTestUdf-1.0-SNAPSHOT.jar";
	
	private static TeiidBot teiidBot = new TeiidBot();

	@BeforeClass
	public static void createModelProject() {
		new PerspectiveAndViewManager().openTeiidDesignerPerspective();
		new ImportManager().importProject("resources/projects/" + PROJECT_NAME);
		new ModelExplorer().changeConnectionProfile(PROFILE_NAME, PROJECT_NAME, MODEL_SRC_NAME);
	}
	
	@Before
	public void beforeMethod(){
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
		
		new ModelExplorerManager().getModelExplorerView().newProcedure(PROJECT_NAME, MODEL_VIEW_NAME + ".xmi", proc, props);
		
		new WaitWhile(new JobIsRunning(), TimePeriod.LONG);
		ArrayList<String> params = new ArrayList<String>();
		params.add("P300");
		new ModelExplorerManager().previewModelObject(params, PROJECT_NAME, MODEL_VIEW_NAME + ".xmi", proc);
		String query = "select * from ( exec \"" + MODEL_VIEW_NAME + "\".\"" + proc + "\"('P300') ) AS X_X";
		new WaitWhile(new JobIsRunning(), TimePeriod.LONG);
		assertTrue(new ModelExplorerManager().checkPreviewOfModelObject(query));
	}
	
	@Test
	public void relViewUDF(){
		// import lib/MyTestUDF.jar
		Properties props = new Properties();
		props.setProperty("dirName", teiidBot.toAbsolutePath(UDF_LIB_PATH));
		props.setProperty("intoFolder", PROJECT_NAME);
		props.setProperty("file", UDF_LIB);
		props.setProperty("createTopLevel", "true");
		new ImportManager().importGeneralItem(ImportGeneralItemWizard.Type.FILE_SYSTEM, props);
		
		// create UDF
		String proc = "udfConcatNull";
		props = new Properties();
		props.setProperty("type", Procedure.Type.RELVIEW_USER_DEFINED_FUNCTION);
		props.setProperty("params", "stringLeft,stringRight");
		props.setProperty("returnParam", "concatenatedResult");
		props.setProperty("functionCategory", "MY_TESTING_FUNCTION_CATEGORY");
		props.setProperty("javaClass", "userdefinedfunctions.MyConcatNull");//see decompiled jar
		props.setProperty("javaMethod", "myConcatNull");
		props.setProperty("udfJarPath", "lib/" + UDF_LIB);
		
		new ModelExplorerManager().getModelExplorerView().newProcedure(PROJECT_NAME, MODEL_VIEW_NAME + ".xmi", proc, props);
		
		// create table to test -> use UDF in transformation
		String table = "tab";
		String query = "select udfConcatNull(hsqldbParts.PARTS.PART_NAME,hsqldbParts.PARTS.PART_WEIGHT) as NAME_WEIGHT from hsqldbParts.PARTS";
		props = new Properties();
		props.setProperty("sql", query);
		new ModelExplorerManager().getModelExplorerView().newTable(table, Table.Type.VIEW, props, PROJECT_NAME, MODEL_VIEW_NAME + ".xmi");
	
		new ModelExplorerManager().previewModelObject(null, PROJECT_NAME, MODEL_VIEW_NAME + ".xmi", table);
		String previewQuery = teiidBot.generateTablePreviewQuery(MODEL_VIEW_NAME, table);
		assertTrue(new ModelExplorerManager().checkPreviewOfModelObject(previewQuery));
		
		//TODO? test also via procedure?
	}
	
	//TODO source defined functions - test in importTest with server
}
