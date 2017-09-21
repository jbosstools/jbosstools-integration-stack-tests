package org.jboss.tools.teiid.ui.bot.test;

import org.eclipse.reddeer.common.wait.TimePeriod;
import org.eclipse.reddeer.common.wait.WaitWhile;
import org.eclipse.reddeer.junit.requirement.inject.InjectRequirement;
import org.eclipse.reddeer.junit.runner.RedDeerSuite;
import org.eclipse.reddeer.requirements.server.ServerRequirementState;
import org.eclipse.reddeer.workbench.core.condition.JobIsRunning;
import org.jboss.tools.teiid.reddeer.connection.ConnectionProfileConstants;
import org.jboss.tools.teiid.reddeer.connection.TeiidJDBCHelper;
import org.jboss.tools.teiid.reddeer.dialog.TableDialog;
import org.jboss.tools.teiid.reddeer.editor.RelationalModelEditor;
import org.jboss.tools.teiid.reddeer.perspective.TeiidPerspective;
import org.jboss.tools.teiid.reddeer.requirement.TeiidServerRequirement;
import org.jboss.tools.teiid.reddeer.requirement.TeiidServerRequirement.TeiidServer;
import org.jboss.tools.teiid.reddeer.view.ModelExplorer;
import org.jboss.tools.teiid.reddeer.wizard.imports.ImportFromFileSystemWizard;
import org.jboss.tools.teiid.reddeer.wizard.newWizard.NewProcedureWizard;
import org.jboss.tools.teiid.reddeer.wizard.newWizard.VdbWizard;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(RedDeerSuite.class)
@TeiidServer(state = ServerRequirementState.RUNNING, connectionProfiles = {
	ConnectionProfileConstants.ORACLE_11G_PARTS_SUPPLIER })

public class UDFTest {
	
	private static final String PROJECT_NAME = "Partssupplier";
	private static final String MODEL_SRC_NAME = "hsqldbParts";
	private static final String MODEL_VIEW_NAME = "view";
	private static final String PROFILE_NAME = ConnectionProfileConstants.ORACLE_11G_PARTS_SUPPLIER;
	private static final String UDF_LIB_PATH = "target/proc-udf/MyTestUdf/lib/";
	private static final String UDF_LIB = "MyTestUdf-1.0-SNAPSHOT.jar";
	private static final String VDB_NAME = "UDF_VDB";
	
	@InjectRequirement
	private static TeiidServerRequirement teiidServer;
	
	@BeforeClass
	public static void createModelProject() {
		TeiidPerspective.activate();
		new ModelExplorer().importProject(PROJECT_NAME);
		new ModelExplorer().changeConnectionProfile(PROFILE_NAME, PROJECT_NAME, MODEL_SRC_NAME);
	}

	@Before
	public void beforeMethod() {
		new WaitWhile(new JobIsRunning(), TimePeriod.LONG);
	}
	
	/**
	 * Note: Jar with UDF must be created and imported into "target/proc-udf/MyTestUdf/lib/" before execution.
	 * TODO how to obtain this jar
	 */
	@Test
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

		new ModelExplorer().addChildToModelItem(ModelExplorer.ChildType.TABLE, PROJECT_NAME, MODEL_VIEW_NAME + ".xmi");
		new TableDialog(true)
				.setName("tab")
				.setTransformationSql("select udfConcatNull(hsqldbParts.PARTS.PART_NAME,hsqldbParts.PARTS.PART_WEIGHT) as NAME_WEIGHT from hsqldbParts.PARTS")
				.finish();	
		
		new RelationalModelEditor(MODEL_VIEW_NAME + ".xmi").save();

		VdbWizard.openVdbWizard()
				.setLocation(PROJECT_NAME)
				.setName(VDB_NAME)
				.addModel(PROJECT_NAME, MODEL_SRC_NAME + ".xmi")
				.addModel(PROJECT_NAME, MODEL_VIEW_NAME + ".xmi")
				.finish();
		
		new ModelExplorer().deployVdb(PROJECT_NAME, VDB_NAME);
		
		TeiidJDBCHelper jdbchelper = new TeiidJDBCHelper(teiidServer, VDB_NAME);
		jdbchelper.isQuerySuccessful("SELECT * FROM tab", true);
		jdbchelper.isQuerySuccessful("SELECT udfConcatNull('direct','call') AS c1", true);
	}
}