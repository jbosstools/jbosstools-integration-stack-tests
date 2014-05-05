package org.jboss.tools.teiid.ui.bot.test;

import org.eclipse.swtbot.swt.finder.SWTBotTestCase;
import org.eclipse.swtbot.swt.finder.exceptions.WidgetNotFoundException;
import org.jboss.reddeer.swt.exception.SWTLayerException;
import org.jboss.reddeer.swt.impl.shell.DefaultShell;
import org.jboss.reddeer.swt.impl.tree.DefaultTreeItem;
import org.jboss.reddeer.swt.wait.TimePeriod;
import org.jboss.reddeer.swt.wait.WaitWhile;
import org.jboss.tools.teiid.reddeer.Procedure;
import org.jboss.tools.teiid.reddeer.VDB;
import org.jboss.tools.teiid.reddeer.condition.IsInProgress;
import org.jboss.tools.teiid.reddeer.editor.CriteriaBuilder;
import org.jboss.tools.teiid.reddeer.editor.ModelEditor;
import org.jboss.tools.teiid.reddeer.editor.SQLScrapbookEditor;
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
import org.junit.AfterClass;
import org.junit.Test;

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
public class VirtualGroupTutorialUpdatedTest extends SWTBotTestCase {

	private static final String PROJECT_NAME = "MyFirstProject";

	private static TeiidBot teiidBot = new TeiidBot();

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
+ "(P.PART_ID = O.PART_ID) and " + "(O.SUPPLIER_NAME LIKE '%la%') " + "ORDER BY PART_NAME"; //SELECT O.SUPPLIER_NAME, O.PART_ID, P.PART_NAME FROM "partssupModel1".PARTS AS P, PartsVirtual.OnHand AS O WHERE (P.PART_ID = O.PART_ID) and (O.SUPPLIER_NAME LIKE '%la%') ORDER BY PART_NAME

	private static final String TESTSQL_5 = "EXEC PartsVirtual.getOnHandByQuantity( 200 )";
	
	private static final String PROCEDURE_SQL = "CREATE VIRTUAL PROCEDURE\n" + "BEGIN\n\t"
			+ "SELECT * FROM PartsVirtual.OnHand " + "WHERE PartsVirtual.OnHand.QUANTITY = "
			+ "PartsVirtual.getOnHandByQuantity.qtyIn;\n" + "END";

	private static final String VIRTUAL_PROCEDURE_SQL = "CREATE VIRTUAL PROCEDURE\n" + "BEGIN\n\t"
			+ "SELECT * FROM PartsVirtual.OnHand;" + "\nEND";
	
	@Test
	public void test() {
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
		closeScrapbookEditor();
	}

	public void refreshServer() {
		String serverName = new ServerManager()
				.getServerName("swtbot.properties");
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
		try {
			new ConnectionProfileManager().createCPWithDriverDefinition(
					jdbcProfile, props1);
			new ConnectionProfileManager().createCPWithoutDriverDefinition(
					jdbcProfile2, props2);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Create data sources for connection profiles
	 */

	public void createSources() {
		try {
			// datasource ds1
			importFromHsql(jdbcProfile, SOURCE_MODEL_1);

			// datasource ds2
			importFromHsql(jdbcProfile2, SOURCE_MODEL_2);// import the same
															// tables, other
															// datasource
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	/**
	 * Preview data from model
	 */

	public void previewData() {
		try {
			new GuidesView().previewData(PROJECT_NAME, SOURCE_MODEL_1, "PARTS");

			SQLResult result = DatabaseDevelopmentPerspective.getInstance()
					.getSqlResultsView().getByOperation(TESTSQL_1);
			assertEquals(SQLResult.STATUS_SUCCEEDED, result.getStatus());

			new WaitWhile(new IsInProgress(), TimePeriod.NORMAL);
			TeiidPerspective.getInstance().open();
			new DefaultTreeItem(0, PROJECT_NAME, SOURCE_MODEL_1, "PARTS")
					.select();

		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	/**
	 * Create virtual view model
	 */

	public void createViewModel() {
		try {

			CreateMetadataModel newModel = new CreateMetadataModel();
			newModel.setLocation(PROJECT_NAME);
			newModel.setName(VIRTUAL_MODEL_NAME);
			newModel.setClass(CreateMetadataModel.ModelClass.RELATIONAL);
			newModel.setType(CreateMetadataModel.ModelType.VIEW);
			newModel.execute();

			teiidBot.saveAll();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	/**
	 * Create transformation for virtual view model
	 */

	public void createTransformation() {
		try {
			// create table OnHand
			ModelExplorerView modelView = TeiidPerspective.getInstance()
					.getModelExplorerView();
			modelView.newBaseTable(PROJECT_NAME, VIRTUAL_MODEL_NAME,
					VIRTUAL_TABLE_NAME, false);
			modelView.openTransformationDiagram(PROJECT_NAME,
					VIRTUAL_MODEL_NAME, VIRTUAL_TABLE_NAME);
			modelView.addTransformationSource(PROJECT_NAME, SOURCE_MODEL_1,
					"SUPPLIER");
			modelView.addTransformationSource(PROJECT_NAME, SOURCE_MODEL_2,
					"SUPPLIER_PARTS");

			ModelEditor editor = new ModelEditor(VIRTUAL_MODEL_NAME);
			editor.show();
			editor.showTransformation();

			CriteriaBuilder criteriaBuilder = editor.criteriaBuilder();
			criteriaBuilder.selectRightAttribute(
					SOURCE_MODEL_1.substring(0, SOURCE_MODEL_1.indexOf('.'))
							.concat(".SUPPLIER"), "SUPPLIER_ID");
			criteriaBuilder.selectLeftAttribute(
					SOURCE_MODEL_2.substring(0, SOURCE_MODEL_2.indexOf('.'))
							.concat(".SUPPLIER_PARTS"), "SUPPLIER_ID");
			criteriaBuilder.apply();
			criteriaBuilder.finish();

			editor.save();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	/**
	 * Create VDB
	 */

	public void createVDB() {
		try {
			CreateVDB createVDB = new CreateVDB();
			createVDB.setFolder(PROJECT_NAME);
			createVDB.setName(VDB_NAME);
			createVDB.execute(true);

			VDBEditor editor = VDBEditor.getInstance(VDB_FILE_NAME);
			editor.show();
			editor.addModel(PROJECT_NAME, VIRTUAL_MODEL_NAME);
			editor.save();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	/**
	 * Deploy, execute VDB
	 */

	public void executeVDB() {
		try {
			ModelExplorer modelExplorer = new ModelExplorer();
			modelExplorer.open();
			VDB vdb = modelExplorer.getModelProject(PROJECT_NAME).getVDB(
					VDB_FILE_NAME);
			vdb.deployVDB();
			vdb.executeVDB(true);

		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	/**
	 * Execute test queries in SQL Scrapbook
	 */

	public void executeSqlQueries() {

		SQLScrapbookEditor editor = new SQLScrapbookEditor("SQL Scrapbook0");
		SQLResult result;
		try {
			editor.show();
			editor.setDatabase(VDB_NAME);

			// TESTSQL_1
			editor.setText(TESTSQL_1);
			editor.executeAll();

			result = DatabaseDevelopmentPerspective.getInstance()
					.getSqlResultsView().getByOperation(TESTSQL_1);
			assertEquals(SQLResult.STATUS_SUCCEEDED, result.getStatus());

		} catch (Exception ex) {
			ex.printStackTrace();
		}

		try {
			// TESTSQL_2
			editor.show();
			editor.setText(TESTSQL_2);
			editor.executeAll();

			result = DatabaseDevelopmentPerspective.getInstance()
					.getSqlResultsView().getByOperation(TESTSQL_2);
			assertEquals(SQLResult.STATUS_SUCCEEDED, result.getStatus());

		} catch (Exception ex) {
			ex.printStackTrace();
		}

		try {
			// TESTSQL_3
			editor.show();
			editor.setText(TESTSQL_3);
			editor.executeAll();

			result = DatabaseDevelopmentPerspective.getInstance()
					.getSqlResultsView().getByOperation(TESTSQL_3);
			assertEquals(SQLResult.STATUS_SUCCEEDED, result.getStatus());

		} catch (Exception ex) {
			ex.printStackTrace();
		}

		try {
			// TESTSQL_4
			editor.show();
			editor.setText(TESTSQL_4);
			editor.executeAll();

			result = DatabaseDevelopmentPerspective.getInstance()
					.getSqlResultsView().getByOperation(TESTSQL_4);
			assertEquals(SQLResult.STATUS_SUCCEEDED, result.getStatus());
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	/**
	 * Create procedure
	 */

	public void createProcedure() {
		ModelExplorerView modelView = TeiidPerspective.getInstance()
				.getModelExplorerView();
		ModelEditor editor;
		try {
			modelView = TeiidPerspective.getInstance().getModelExplorerView();
			Procedure procedure = modelView.newProcedure(PROJECT_NAME,
					VIRTUAL_MODEL_NAME, PROCEDURE_NAME, true);
			ModelExplorerView mev = new ModelExplorerView();
			mev.open();
			new WaitWhile(new IsInProgress(), TimePeriod.LONG);
			new DefaultTreeItem(0, PROJECT_NAME, VIRTUAL_MODEL_NAME,
					PROCEDURE_NAME).select();

			procedure.addParameter2("qtyIn", "short : xs:int");

		} catch (Exception ex) {
			ex.printStackTrace();
		}

		try {
			modelView = TeiidPerspective.getInstance().getModelExplorerView();
			modelView.openTransformationDiagram(PROJECT_NAME,
					VIRTUAL_MODEL_NAME, PROCEDURE_NAME);

			editor = new ModelEditor(VIRTUAL_MODEL_NAME);
			editor.show();
			editor.showTransformation();// opens the actual transformation
										// (procedure)
			editor.setTransformationProcedureBody(VIRTUAL_PROCEDURE_SQL, true);// "SELECT * FROM PartsVirtual.OnHand;"
			editor.save();
		} catch (Exception ex) {
			ex.printStackTrace();
		}

		try {
			editor = new ModelEditor(VIRTUAL_MODEL_NAME);
			// click at T
			editor.showTransformation();

			// click before ending ;
			TeiidStyledText styledText = new TeiidStyledText(0);
			styledText.navigateTo(2, 2);
			styledText.mouseClickOnCaret();// clicks on the cursor position
		} catch (Exception ex) {
			ex.printStackTrace();
		}

		try {
			editor = new ModelEditor(VIRTUAL_MODEL_NAME);
			CriteriaBuilder criteriaBuilder = editor.criteriaBuilder();
			criteriaBuilder.selectLeftAttribute("PartsVirtual."
					+ VIRTUAL_TABLE_NAME, "QUANTITY");
			criteriaBuilder.selectRightAttribute("PartsVirtual."
					+ PROCEDURE_NAME, "qtyIn");
			criteriaBuilder.apply();
			criteriaBuilder.finish();

			editor.save();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	public void updateVDB() {
		try {
			TeiidPerspective.getInstance().getModelExplorerView()
					.open(PROJECT_NAME, VDB_FILE_NAME);

			VDBEditor editor = VDBEditor.getInstance(VDB_FILE_NAME);
			editor.show();
			editor.synchronizeAll();
			editor.save();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	public void deployUpdatedVDB() {
		try {
			ModelExplorer modelExplorer = new ModelExplorer();
			modelExplorer.open();
			VDB vdb = modelExplorer.getModelProject(PROJECT_NAME).getVDB(
					VDB_FILE_NAME);
			vdb.deployVDB();
			vdb.executeVDB();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	public void executeProcedureQuery() {
		try {
			SQLScrapbookEditor editor = new SQLScrapbookEditor("SQL Scrapbook1");
			editor.show();
			editor.setDatabase(VDB_NAME);
			editor.setText(TESTSQL_5);
			editor.executeAll();

			SQLResult result = DatabaseDevelopmentPerspective.getInstance()
					.getSqlResultsView().getByOperation(TESTSQL_5);
			assertEquals(SQLResult.STATUS_SUCCEEDED, result.getStatus());
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	@AfterClass
	public static void closeScrapbookEditor() {
		closeScrapbook();
		closeVDBEditor();
		closeModelEditor(VIRTUAL_MODEL_NAME);
		closeModelEditor(SOURCE_MODEL_1);
		closeModelEditor(SOURCE_MODEL_2);
	}

	private static void closeScrapbook() {
		try {
			SQLScrapbookEditor editor = new SQLScrapbookEditor("SQL Scrapbook0");
			editor.show();
			editor.close();
			editor = new SQLScrapbookEditor("SQL Scrapbook1");
			editor.show();
			editor.close();
		} catch (WidgetNotFoundException e) {
			e.printStackTrace();
		}
	}

	private static void closeVDBEditor() {
		try {
			VDBEditor editor = VDBEditor.getInstance(VDB_NAME + ".vdb");
			editor.close();
		} catch (WidgetNotFoundException e) {
			e.printStackTrace();
		}
	}

	private static void closeModelEditor(String name) {
		try {
			ModelEditor editor = new ModelEditor(name);
			editor.close();
		} catch (WidgetNotFoundException e) {
			e.printStackTrace();
		}
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
