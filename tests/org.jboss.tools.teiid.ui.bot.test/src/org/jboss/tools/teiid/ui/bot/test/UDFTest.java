package org.jboss.tools.teiid.ui.bot.test;

import java.util.Properties;

import org.jboss.reddeer.common.wait.TimePeriod;
import org.jboss.reddeer.common.wait.WaitWhile;
import org.jboss.reddeer.core.condition.JobIsRunning;
import org.jboss.reddeer.junit.requirement.inject.InjectRequirement;
import org.jboss.reddeer.junit.runner.RedDeerSuite;
import org.jboss.reddeer.requirements.server.ServerReqState;
import org.jboss.tools.teiid.reddeer.Procedure;
import org.jboss.tools.teiid.reddeer.Table;
import org.jboss.tools.teiid.reddeer.connection.ConnectionProfileConstants;
import org.jboss.tools.teiid.reddeer.connection.TeiidJDBCHelper;
import org.jboss.tools.teiid.reddeer.manager.PerspectiveAndViewManager;
import org.jboss.tools.teiid.reddeer.requirement.TeiidServerRequirement;
import org.jboss.tools.teiid.reddeer.requirement.TeiidServerRequirement.TeiidServer;
import org.jboss.tools.teiid.reddeer.view.ModelExplorer;
import org.jboss.tools.teiid.reddeer.wizard.VdbWizard;
import org.jboss.tools.teiid.reddeer.wizard.imports.ImportFromFileSystemWizard;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(RedDeerSuite.class)
@TeiidServer(state = ServerReqState.RUNNING, connectionProfiles = {
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
		new PerspectiveAndViewManager().openTeiidDesignerPerspective();
		new ModelExplorer().importProject(PROJECT_NAME);
		new ModelExplorer().changeConnectionProfile(PROFILE_NAME, PROJECT_NAME, MODEL_SRC_NAME);
	}

	@Before
	public void beforeMethod() {
		new WaitWhile(new JobIsRunning(), TimePeriod.LONG);
	}
	
	@Test
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
		props.setProperty("javaClass", "userdefinedfunctions.MyConcatNull");// see decompiled jar
		props.setProperty("javaMethod", "myConcatNull");
		props.setProperty("udfJarPath", "lib/" + UDF_LIB);

		new ModelExplorer().newProcedure(PROJECT_NAME, MODEL_VIEW_NAME + ".xmi", proc, props);

		// create table to test -> use UDF in transformation
		String table = "tab";
		String query = "select udfConcatNull(hsqldbParts.PARTS.PART_NAME,hsqldbParts.PARTS.PART_WEIGHT) as NAME_WEIGHT from hsqldbParts.PARTS";
		props = new Properties();
		props.setProperty("sql", query);
		new ModelExplorer().newTable(table, Table.Type.VIEW, props, PROJECT_NAME,
				MODEL_VIEW_NAME + ".xmi");

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