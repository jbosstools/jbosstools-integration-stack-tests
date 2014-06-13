package org.jboss.tools.teiid.ui.bot.test;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.eclipse.swtbot.eclipse.finder.SWTWorkbenchBot;
import org.eclipse.swtbot.swt.finder.SWTBotTestCase;
import org.jboss.reddeer.swt.impl.shell.WorkbenchShell;
import org.jboss.reddeer.swt.impl.tree.DefaultTreeItem;
import org.jboss.reddeer.swt.wait.TimePeriod;
import org.jboss.reddeer.swt.wait.WaitWhile;
import org.jboss.tools.teiid.reddeer.Procedure;
import org.jboss.tools.teiid.reddeer.VDB;
import org.jboss.tools.teiid.reddeer.condition.IsInProgress;
import org.jboss.tools.teiid.reddeer.editor.CriteriaBuilder;
import org.jboss.tools.teiid.reddeer.editor.ModelEditor;
import org.jboss.tools.teiid.reddeer.editor.VDBEditor;
import org.jboss.tools.teiid.reddeer.manager.ConnectionProfileManager;
import org.jboss.tools.teiid.reddeer.manager.ModelExplorerManager;
import org.jboss.tools.teiid.reddeer.manager.ServerManager;
import org.jboss.tools.teiid.reddeer.perspective.DatabaseDevelopmentPerspective;
import org.jboss.tools.teiid.reddeer.perspective.TeiidPerspective;
import org.jboss.tools.teiid.reddeer.view.GuidesView;
import org.jboss.tools.teiid.reddeer.view.ModelExplorer;
import org.jboss.tools.teiid.reddeer.view.ModelExplorerView;
import org.jboss.tools.teiid.reddeer.view.SQLResult;
import org.jboss.tools.teiid.reddeer.widget.TeiidStyledText;
import org.jboss.tools.teiid.reddeer.wizard.CreateMetadataModel;
import org.jboss.tools.teiid.reddeer.wizard.CreateVDB;
import org.jboss.tools.teiid.reddeer.wizard.ImportJDBCDatabaseWizard;
import org.jboss.tools.teiid.ui.bot.test.requirement.PerspectiveRequirement.Perspective;
import org.jboss.tools.teiid.ui.bot.test.requirement.ServerRequirement.Server;
import org.jboss.tools.teiid.ui.bot.test.requirement.ServerRequirement.State;
import org.jboss.tools.teiid.ui.bot.test.requirement.ServerRequirement.Type;
import org.jboss.tools.teiid.ui.bot.test.suite.TeiidSuite;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * Test functions described in testscript E2eVirtualGroupTutorial. In contrast
 * to VirtualGroupTutorialTest, it uses Modelling action sets and creates
 * preview.
 * 
 * @author Lucie Fabrikova, lfabriko@redhat.com
 * 
 */
@Perspective(name = "Teiid Designer")
@Server(type = Type.ALL, state = State.RUNNING)
@RunWith(TeiidSuite.class)
public class VirtualGroupTutorialUpdatedTest extends SWTBotTestCase {

	private static final String PROJECT_NAME = "MyFirstProject";

	private String jdbcProfile = "Generic JDBC Profile";
	private String jdbcProfile2 = "Generic JDBC Profile 2";

	private static String SOURCE_MODEL_1 = "partssupModel1.xmi";
	private static String SOURCE_MODEL_2 = "partssupModel2.xmi";

	private String props1 = "resources/db/dv6-ds1.properties";
	private String props2 = "resources/db/dv6-ds2.properties";

	private static String VIRTUAL_MODEL_NAME = "PartsVirtual.xmi";
	private static final String VIRTUAL_TABLE_NAME = "OnHand";
	private static final String PROCEDURE_NAME = "getOnHandByQuantity";

	private static final String VDB_NAME = "MyFirstVDB";
	private static final String VDB_FILE_NAME = VDB_NAME + ".vdb";

	private static String TESTSQL_1 = "select * from \"partssupModel1\".\"PARTS\"";
	private static String TESTSQL_2 = "SELECT * FROM PartsVirtual.OnHand";
	private static final String TESTSQL_3 = "SELECT * FROM PartsVirtual.OnHand WHERE QUANTITY > 200";

	private static final String TESTSQL_4 = "SELECT " + "O.SUPPLIER_NAME, " + "O.PART_ID, " + "P.PART_NAME " +

	"FROM " + "\"partssupModel1\".PARTS AS P, " + "PartsVirtual.OnHand AS O " + "WHERE "
			+ "(P.PART_ID = O.PART_ID) and " + "(O.SUPPLIER_NAME LIKE '%la%') " + "ORDER BY PART_NAME";

	// SELECT
	// O.SUPPLIER_NAME,
	// O.PART_ID,
	// P.PART_NAME
	// FROM
	// "partssupModel1".PARTS
	// AS
	// P,
	// PartsVirtual.OnHand
	// AS
	// O
	// WHERE
	// (P.PART_ID
	// =
	// O.PART_ID)
	// and
	// (O.SUPPLIER_NAME
	// LIKE
	// '%la%')
	// ORDER
	// BY
	// PART_NAME

	private static final String TESTSQL_5 = "EXEC PartsVirtual.getOnHandByQuantity( 200 )";

	private static final String PROCEDURE_SQL = "CREATE VIRTUAL PROCEDURE\n" + "BEGIN\n\t"
			+ "SELECT * FROM PartsVirtual.OnHand " + "WHERE PartsVirtual.OnHand.QUANTITY = "
			+ "PartsVirtual.getOnHandByQuantity.qtyIn;\n" + "END";

	private static final String VIRTUAL_PROCEDURE_SQL = "CREATE VIRTUAL PROCEDURE\n" + "BEGIN\n\t"
			+ "SELECT * FROM PartsVirtual.OnHand;" + "\nEND";

	@BeforeClass
	public static void maximizeWorkbench() {
		new WorkbenchShell().maximize();
	}

	@Test
	public void test() throws Exception {
		createProject();
		refreshServer();
		createConnProfiles();
		createSources();
		previewData();
		createViewModel();
		createTransformation();
		createVDB();
		executeVDB();
		executeSqlQueries();
		createProcedure();
		updateVDB();
		deployUpdatedVDB();
		executeProcedureQuery();
	}

	public void refreshServer() {
		String serverName = new ServerManager().getServerName("swtbot.properties");
		new ServerManager().getServersViewExt().refreshServer(serverName);
	}

	/**
	 * Create new Teiid Model Project
	 */

	public void createProject() {
		new ModelExplorerManager().createProject(PROJECT_NAME, true);
	}

	/**
	 * Create connection profiles to HSQL databases
	 */

	public void createConnProfiles() {
		new ConnectionProfileManager().createCPWithDriverDefinition(jdbcProfile, props1);
		new ConnectionProfileManager().createCPWithoutDriverDefinition(jdbcProfile2, props2);
	}

	/**
	 * Create data sources for connection profiles
	 */

	public void createSources() {
		// datasource ds1
		importFromHsql(jdbcProfile, SOURCE_MODEL_1);

		// datasource ds2
		importFromHsql(jdbcProfile2, SOURCE_MODEL_2);// import the same
														// tables, other
														// datasource

	}

	/**
	 * Preview data from model
	 */

	public void previewData() {
		new GuidesView().previewData(PROJECT_NAME, SOURCE_MODEL_1, "PARTS");

		SQLResult result = DatabaseDevelopmentPerspective.getInstance().getSqlResultsView().getByOperation(TESTSQL_1);
		assertEquals(SQLResult.STATUS_SUCCEEDED, result.getStatus());

		new WaitWhile(new IsInProgress(), TimePeriod.NORMAL);
		TeiidPerspective.getInstance().open();
		ModelExplorer modelExplorer = new ModelExplorer();
		modelExplorer.open();
		modelExplorer.getProject(PROJECT_NAME).getProjectItem(SOURCE_MODEL_1, "PARTS").select();
	}

	/**
	 * Create virtual view model
	 */

	public void createViewModel() {
		CreateMetadataModel newModel = new CreateMetadataModel();
		newModel.setLocation(PROJECT_NAME);
		newModel.setName(VIRTUAL_MODEL_NAME);
		newModel.setClass(CreateMetadataModel.ModelClass.RELATIONAL);
		newModel.setType(CreateMetadataModel.ModelType.VIEW);
		newModel.execute();
	}

	/**
	 * Create transformation for virtual view model
	 */

	public void createTransformation() {
		// create table OnHand
		ModelExplorerView modelView = TeiidPerspective.getInstance().getModelExplorerView();
		modelView.newBaseTable(PROJECT_NAME, VIRTUAL_MODEL_NAME, VIRTUAL_TABLE_NAME, false);
		modelView.openTransformationDiagram(PROJECT_NAME, VIRTUAL_MODEL_NAME, VIRTUAL_TABLE_NAME);
		modelView.addTransformationSource(PROJECT_NAME, SOURCE_MODEL_1, "SUPPLIER");
		modelView.addTransformationSource(PROJECT_NAME, SOURCE_MODEL_2, "SUPPLIER_PARTS");

		ModelEditor editor = new ModelEditor(VIRTUAL_MODEL_NAME);
		editor.show();
		editor.showTransformation();

		CriteriaBuilder criteriaBuilder = editor.criteriaBuilder();
		criteriaBuilder.selectRightAttribute(
				SOURCE_MODEL_1.substring(0, SOURCE_MODEL_1.indexOf('.')).concat(".SUPPLIER"), "SUPPLIER_ID");
		criteriaBuilder.selectLeftAttribute(
				SOURCE_MODEL_2.substring(0, SOURCE_MODEL_2.indexOf('.')).concat(".SUPPLIER_PARTS"), "SUPPLIER_ID");
		criteriaBuilder.apply();
		criteriaBuilder.finish();

		editor.save();
	}

	/**
	 * Create VDB
	 */

	public void createVDB() {
		CreateVDB createVDB = new CreateVDB();
		createVDB.setFolder(PROJECT_NAME);
		createVDB.setName(VDB_NAME);
		createVDB.execute(true);

		VDBEditor editor = VDBEditor.getInstance(VDB_FILE_NAME);
		editor.show();
		editor.addModel(PROJECT_NAME, VIRTUAL_MODEL_NAME);
		editor.save();
	}

	/**
	 * Deploy, execute VDB
	 */

	public void executeVDB() {
		ModelExplorer modelExplorer = new ModelExplorer();
		modelExplorer.open();
		VDB vdb = modelExplorer.getModelProject(PROJECT_NAME).getVDB(VDB_FILE_NAME);
		vdb.deployVDB();
		vdb.executeVDB(true);
	}

	/**
	 * Execute test queries in SQL Scrapbook
	 * 
	 * @throws SQLException
	 */

	public void executeSqlQueries() throws SQLException {
		checkSql(TESTSQL_1);
		checkSql(TESTSQL_2);
		checkSql(TESTSQL_3);
		checkSql(TESTSQL_4);
	}

	private void checkSql(String sql) throws SQLException {
		// register teiid driver
		DriverManager.registerDriver(TeiidSuite.getTeiidDriver());
		// create connection
		Connection conn = DriverManager.getConnection("jdbc:teiid:" + VDB_NAME + "@mm://localhost:31000", "user",
				"user");
		Statement stmt = conn.createStatement();
		ResultSet rs = stmt.executeQuery(sql);
		conn.close();
	}

	/**
	 * Create procedure
	 */

	public void createProcedure() {
		ModelExplorerView modelView = TeiidPerspective.getInstance().getModelExplorerView();
		ModelEditor editor;
		modelView = TeiidPerspective.getInstance().getModelExplorerView();
		Procedure procedure = modelView.newProcedure(PROJECT_NAME, VIRTUAL_MODEL_NAME, PROCEDURE_NAME, true);
		ModelExplorerView mev = new ModelExplorerView();
		mev.open();
		new WaitWhile(new IsInProgress(), TimePeriod.LONG);
		new DefaultTreeItem(0, PROJECT_NAME, VIRTUAL_MODEL_NAME, PROCEDURE_NAME).select();

		procedure.addParameter2("qtyIn", "short : xs:int");

		modelView = TeiidPerspective.getInstance().getModelExplorerView();
		modelView.openTransformationDiagram(PROJECT_NAME, VIRTUAL_MODEL_NAME, PROCEDURE_NAME);

		editor = new ModelEditor(VIRTUAL_MODEL_NAME);
		editor.show();
		editor.showTransformation();// opens the actual transformation
									// (procedure)
		editor.setTransformationProcedureBody(VIRTUAL_PROCEDURE_SQL, true);// "SELECT * FROM PartsVirtual.OnHand;"
		editor.save();

		editor = new ModelEditor(VIRTUAL_MODEL_NAME);
		// click at T
		editor.showTransformation();

		// click before ending ;
		TeiidStyledText styledText = new TeiidStyledText(0);
		styledText.navigateTo(2, 2);
		styledText.mouseClickOnCaret();// clicks on the cursor position

		editor = new ModelEditor(VIRTUAL_MODEL_NAME);
		CriteriaBuilder criteriaBuilder = editor.criteriaBuilder();
		criteriaBuilder.selectLeftAttribute("PartsVirtual." + VIRTUAL_TABLE_NAME, "QUANTITY");
		criteriaBuilder.selectRightAttribute("PartsVirtual." + PROCEDURE_NAME, "qtyIn");
		criteriaBuilder.apply();
		criteriaBuilder.finish();

		editor.save();
	}

	public void updateVDB() {
		ModelExplorer modelExplorer = new ModelExplorer();
		modelExplorer.open();
		modelExplorer.getProject(PROJECT_NAME).getProjectItem(VDB_FILE_NAME).open();

		VDBEditor editor = VDBEditor.getInstance(VDB_FILE_NAME);
		editor.show();
		editor.synchronizeAll();
		editor.save();
	}

	public void deployUpdatedVDB() {
		new WorkbenchShell();
		ModelExplorer modelExplorer = new ModelExplorer();
		modelExplorer.open();
		VDB vdb = modelExplorer.getModelProject(PROJECT_NAME).getVDB(VDB_FILE_NAME);
		vdb.deployVDB();
		vdb.executeVDB();
	}

	public void executeProcedureQuery() throws SQLException {
		checkSql(TESTSQL_5);
	}

	@AfterClass
	public static void closeAllEditors() {
		new SWTWorkbenchBot().closeAllEditors();
	}

	/**
	 * Import tables from HSQL database
	 * 
	 * @param connProfile
	 *            name of connection profile (e.g. HSQLDB Profile)
	 * @param modelName
	 *            name of model (e.g. partssupplier.xmi)
	 */
	public void importFromHsql(String connProfile, String modelName) {
		ImportJDBCDatabaseWizard wizard = new ImportJDBCDatabaseWizard();
		wizard.setConnectionProfile(connProfile);
		wizard.setProjectName(PROJECT_NAME);
		wizard.setModelName(modelName);
		wizard.addItem("PUBLIC/PUBLIC/TABLE/PARTS");
		wizard.addItem("PUBLIC/PUBLIC/TABLE/SHIP_VIA");
		wizard.addItem("PUBLIC/PUBLIC/TABLE/STATUS");
		wizard.addItem("PUBLIC/PUBLIC/TABLE/SUPPLIER");
		wizard.addItem("PUBLIC/PUBLIC/TABLE/SUPPLIER_PARTS");

		wizard.execute(true);
	}

}
