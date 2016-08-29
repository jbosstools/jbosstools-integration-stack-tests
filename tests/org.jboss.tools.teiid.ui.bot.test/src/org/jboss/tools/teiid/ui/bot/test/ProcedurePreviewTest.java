package org.jboss.tools.teiid.ui.bot.test;

import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Properties;

import org.jboss.reddeer.common.wait.TimePeriod;
import org.jboss.reddeer.common.wait.WaitWhile;
import org.jboss.reddeer.core.condition.JobIsRunning;
import org.jboss.reddeer.junit.execution.annotation.RunIf;
import org.jboss.reddeer.junit.runner.RedDeerSuite;
import org.jboss.reddeer.requirements.server.ServerReqState;
import org.jboss.tools.common.reddeer.condition.IssueIsClosed;
import org.jboss.tools.common.reddeer.condition.IssueIsClosed.Jira;
import org.jboss.tools.teiid.reddeer.Procedure;
import org.jboss.tools.teiid.reddeer.Table;
import org.jboss.tools.teiid.reddeer.connection.ConnectionProfileConstants;
import org.jboss.tools.teiid.reddeer.manager.PerspectiveAndViewManager;
import org.jboss.tools.teiid.reddeer.requirement.TeiidServerRequirement.TeiidServer;
import org.jboss.tools.teiid.reddeer.view.ModelExplorer;
import org.jboss.tools.teiid.reddeer.wizard.imports.ImportFromFileSystemWizard;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(RedDeerSuite.class)
@TeiidServer(state = ServerReqState.RUNNING, connectionProfiles = {
		ConnectionProfileConstants.ORACLE_11G_PARTS_SUPPLIER })
public class ProcedurePreviewTest {

	private static final String PROJECT_NAME = "Partssupplier";
	private static final String MODEL_SRC_NAME = "hsqldbParts";
	private static final String MODEL_VIEW_NAME = "view";
	private static final String PROFILE_NAME = ConnectionProfileConstants.ORACLE_11G_PARTS_SUPPLIER;
	private static final String UDF_LIB_PATH = "target/proc-udf/MyTestUdf/lib/";
	private static final String UDF_LIB = "MyTestUdf-1.0-SNAPSHOT.jar";

	private static TeiidBot teiidBot = new TeiidBot();

	@BeforeClass
	public static void createModelProject() {
		new PerspectiveAndViewManager().openTeiidDesignerPerspective();		
		ModelExplorer explorer = new ModelExplorer();
		
		explorer.importProject(PROJECT_NAME);
		explorer.changeConnectionProfile(PROFILE_NAME, PROJECT_NAME, MODEL_SRC_NAME);
		explorer.createDataSource(ModelExplorer.ConnectionSourceType.USE_CONNECTION_PROFILE_INFO, PROFILE_NAME, PROJECT_NAME, MODEL_SRC_NAME);
		explorer.setJndiName(MODEL_SRC_NAME,PROJECT_NAME, MODEL_SRC_NAME);
		teiidBot.saveAll();
	}

	@Before
	public void beforeMethod() {
		new WaitWhile(new JobIsRunning(), TimePeriod.LONG);
	}

	@Test
	public void relViewProcedure() {
		String proc = "proc";
		Properties props = new Properties();
		props.setProperty("type", Procedure.Type.RELVIEW_PROCEDURE);
		props.setProperty("includeResultSet", "true");
		props.setProperty("resultSetName", "rs");
		props.setProperty("cols", "color");
		props.setProperty("params", "id");
		props.setProperty("sql",
				"CREATE VIRTUAL PROCEDURE BEGIN select hsqldbParts.PARTS.PART_COLOR AS color from hsqldbParts.PARTS where hsqldbParts.PARTS.PART_ID=view.proc.id; END");

		new ModelExplorer().newProcedure(PROJECT_NAME, MODEL_VIEW_NAME + ".xmi", proc, props);

		new WaitWhile(new JobIsRunning(), TimePeriod.LONG);
		ArrayList<String> params = new ArrayList<String>();
		params.add("P300");
		new ModelExplorer().previewModelItem(params, PROJECT_NAME, MODEL_VIEW_NAME + ".xmi", proc);
		String query = "select * from ( exec \"" + MODEL_VIEW_NAME + "\".\"" + proc + "\"('P300') ) AS X_X";
		new WaitWhile(new JobIsRunning(), TimePeriod.LONG);
		assertTrue(new ModelExplorer().checkPreviewOfModelObject(query));
	}

	@Test
	@Jira("TEIIDDES-2677")
	@RunIf(conditionClass = IssueIsClosed.class)
	public void relViewUDF() {
		// import lib/MyTestUDF.jar
		ImportFromFileSystemWizard wizard = new ImportFromFileSystemWizard();
		wizard.open();
		wizard.setPath(UDF_LIB_PATH)
			  .setFolder(PROJECT_NAME)
			  .selectFile(UDF_LIB)
			  .setCreteTopLevelFolder(true)
			  .finish();

		// create UDF
		String proc = "udfConcatNull";
		Properties props = new Properties();
		props.setProperty("type", Procedure.Type.RELVIEW_USER_DEFINED_FUNCTION);
		props.setProperty("params", "stringLeft,stringRight");
		props.setProperty("returnParam", "concatenatedResult");
		props.setProperty("functionCategory", "MY_TESTING_FUNCTION_CATEGORY");
		props.setProperty("javaClass", "userdefinedfunctions.MyConcatNull");
		props.setProperty("javaMethod", "myConcatNull");
		props.setProperty("udfJarPath", "lib/" + UDF_LIB);

		new ModelExplorer().newProcedure(PROJECT_NAME, MODEL_VIEW_NAME + ".xmi", proc, props);

		// create table to test -> use UDF in transformation
		String table = "tab";
		String query = "select udfConcatNull(hsqldbParts.PARTS.PART_NAME,hsqldbParts.PARTS.PART_WEIGHT) as NAME_WEIGHT from hsqldbParts.PARTS";
		props = new Properties();
		props.setProperty("sql", query);
		new ModelExplorer().newTable(table, Table.Type.VIEW, props, PROJECT_NAME, MODEL_VIEW_NAME + ".xmi");

		new ModelExplorer().previewModelItem(null, PROJECT_NAME, MODEL_VIEW_NAME + ".xmi", table);
		String previewQuery = teiidBot.generateTablePreviewQuery(MODEL_VIEW_NAME, table);
		assertTrue(new ModelExplorer().checkPreviewOfModelObject(previewQuery));

	}
}
