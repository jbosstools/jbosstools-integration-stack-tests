package org.jboss.tools.teiid.ui.bot.test;

import static org.junit.Assert.assertTrue;

import java.util.ArrayList;

import org.jboss.reddeer.common.matcher.RegexMatcher;
import org.jboss.reddeer.common.wait.TimePeriod;
import org.jboss.reddeer.common.wait.WaitWhile;
import org.jboss.reddeer.core.condition.JobIsRunning;
import org.jboss.reddeer.core.condition.ShellWithTextIsActive;
import org.jboss.reddeer.junit.execution.annotation.RunIf;
import org.jboss.reddeer.junit.runner.RedDeerSuite;
import org.jboss.reddeer.requirements.server.ServerReqState;
import org.jboss.reddeer.swt.impl.menu.ShellMenu;
import org.jboss.tools.common.reddeer.condition.IssueIsClosed;
import org.jboss.tools.common.reddeer.condition.IssueIsClosed.Jira;
import org.jboss.tools.teiid.reddeer.condition.IsInProgress;
import org.jboss.tools.teiid.reddeer.connection.ConnectionProfileConstants;
import org.jboss.tools.teiid.reddeer.dialog.TableDialog;
import org.jboss.tools.teiid.reddeer.editor.RelationalModelEditor;
import org.jboss.tools.teiid.reddeer.perspective.DatabaseDevelopmentPerspective;
import org.jboss.tools.teiid.reddeer.perspective.TeiidPerspective;
import org.jboss.tools.teiid.reddeer.requirement.TeiidServerRequirement.TeiidServer;
import org.jboss.tools.teiid.reddeer.view.ModelExplorer;
import org.jboss.tools.teiid.reddeer.view.SQLResult;
import org.jboss.tools.teiid.reddeer.wizard.imports.ImportFromFileSystemWizard;
import org.jboss.tools.teiid.reddeer.wizard.newWizard.NewProcedureWizard;
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

	@BeforeClass
	public static void createModelProject() {
		TeiidPerspective.activate();	
		ModelExplorer explorer = new ModelExplorer();
		
		explorer.importProject(PROJECT_NAME);
		explorer.changeConnectionProfile(PROFILE_NAME, PROJECT_NAME, MODEL_SRC_NAME);
		explorer.createDataSource("Use Connection Profile Info", PROFILE_NAME, PROJECT_NAME, MODEL_SRC_NAME);
		explorer.setJndiName(MODEL_SRC_NAME,PROJECT_NAME, MODEL_SRC_NAME);
		new ShellMenu("File", "Save All").select();
	}

	@Before
	public void beforeMethod() {
		new WaitWhile(new JobIsRunning(), TimePeriod.LONG);
	}

	@Test
	public void relViewProcedure() {
		String procedureName = "proc";
		new ModelExplorer().addChildToModelItem(ModelExplorer.ChildType.PROCEDURE, PROJECT_NAME, MODEL_VIEW_NAME + ".xmi");
		NewProcedureWizard.createViewProcedure()
				.setName(procedureName)
				.setTransformationSql("CREATE VIRTUAL PROCEDURE BEGIN select hsqldbParts.PARTS.PART_COLOR AS color from hsqldbParts.PARTS where hsqldbParts.PARTS.PART_ID=view.proc.id; END")
				.toggleResultSet(true)
				.setResultSetName("rs")
				.addResultSetColumn("color", "string", "400")
				.addParameter("id", "string", "400", "IN")
				.finish();
		
		new RelationalModelEditor(MODEL_VIEW_NAME + ".xmi").save();

		new WaitWhile(new JobIsRunning(), TimePeriod.LONG);
		ArrayList<String> params = new ArrayList<String>();
		params.add("P300");
		new ModelExplorer().previewModelItem(params, PROJECT_NAME, MODEL_VIEW_NAME + ".xmi", procedureName);
		String query = "select * from ( exec \"" + MODEL_VIEW_NAME + "\".\"" + procedureName + "\"('P300') ) AS X_X";
		new WaitWhile(new JobIsRunning(), TimePeriod.LONG);
		assertTrue(checkPreviewOfModelObject(query));
	}

	/**
	 * Note: Jar with UDF must be created and imported into "target/proc-udf/MyTestUdf/lib/" before execution.
	 * TODO how to obtain this jar
	 */
	@Test
	@Jira("TEIIDDES-2677")
	@RunIf(conditionClass = IssueIsClosed.class)
	public void relViewUDF() {
		// import lib/MyTestUDF.jar
		ImportFromFileSystemWizard.openWizard()
				.setPath(UDF_LIB_PATH)
				.setFolder(PROJECT_NAME)
				.selectFile(UDF_LIB)
				.setCreteTopLevelFolder(true)
				.finish();

		new ModelExplorer().addChildToModelItem(ModelExplorer.ChildType.PROCEDURE, PROJECT_NAME, MODEL_VIEW_NAME + ".xmi");
		NewProcedureWizard.createUserDefinedFunction()
				.setName("udfConcatNull")
				.addParameter("stringLeft", "string", "400", "IN")
				.addParameter("stringRight", "string", "400", "IN")
				.addParameter("concatenatedResult", "string", "400", "RETURN")
				.setFunctionCategory("MY_TESTING_FUNCTION_CATEGORY")
				.setJavaClass("userdefinedfunctions.MyConcatNull")
				.setJavaMethod("myConcatNull")
				.setUdfJarPath("lib/" + UDF_LIB)
				.finish();

		String tableName = "tab";
		new ModelExplorer().addChildToModelItem(ModelExplorer.ChildType.TABLE, PROJECT_NAME, MODEL_VIEW_NAME + ".xmi");
		new TableDialog(true)
				.setName("tab")
				.setTransformationSql("select udfConcatNull(hsqldbParts.PARTS.PART_NAME,hsqldbParts.PARTS.PART_WEIGHT) as NAME_WEIGHT from hsqldbParts.PARTS")
				.finish();	
		
		new RelationalModelEditor(MODEL_VIEW_NAME + ".xmi").save();

		new ModelExplorer().previewModelItem(null, PROJECT_NAME, MODEL_VIEW_NAME + ".xmi", tableName);
		String previewQuery = "select * from \"" + MODEL_VIEW_NAME + "\".\"" + tableName + "\"";
		assertTrue(checkPreviewOfModelObject(previewQuery));
	}
	
	/**
	 * Checks that preview succeed.
	 * @param previewSQL - specifies preview record
	 */
	private boolean checkPreviewOfModelObject(String previewSQL) {
		// wait while is in progress
		new WaitWhile(new IsInProgress(), TimePeriod.LONG);
		// wait while dialog Preview data... is active
		new WaitWhile(new ShellWithTextIsActive(new RegexMatcher("Preview.*")), TimePeriod.LONG);
		SQLResult result = DatabaseDevelopmentPerspective.getInstance().getSqlResultsView().getByOperation(previewSQL);
		return result.getStatus().equals(SQLResult.STATUS_SUCCEEDED);
	}
}
