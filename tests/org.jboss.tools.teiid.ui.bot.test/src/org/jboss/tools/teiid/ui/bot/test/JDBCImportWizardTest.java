package org.jboss.tools.teiid.ui.bot.test;

import static org.junit.Assert.assertTrue;

import java.util.List;
import java.util.Properties;

import org.jboss.reddeer.junit.execution.annotation.RunIf;
import org.jboss.reddeer.junit.requirement.inject.InjectRequirement;
import org.jboss.reddeer.junit.runner.RedDeerSuite;
import org.jboss.reddeer.requirements.server.ServerReqState;
import org.jboss.reddeer.swt.api.TableItem;
import org.jboss.reddeer.swt.impl.ctab.DefaultCTabItem;
import org.jboss.reddeer.swt.impl.tab.DefaultTabItem;
import org.jboss.reddeer.swt.impl.table.DefaultTable;
import org.jboss.tools.common.reddeer.condition.IssueIsClosed;
import org.jboss.tools.common.reddeer.condition.IssueIsClosed.Jira;
import org.jboss.tools.teiid.reddeer.manager.ConnectionProfilesConstants;
import org.jboss.tools.teiid.reddeer.manager.ImportManager;
import org.jboss.tools.teiid.reddeer.manager.ModelExplorerManager;
import org.jboss.tools.teiid.reddeer.manager.ServerManager;
import org.jboss.tools.teiid.reddeer.perspective.TeiidPerspective;
import org.jboss.tools.teiid.reddeer.requirement.TeiidServerRequirement;
import org.jboss.tools.teiid.reddeer.requirement.TeiidServerRequirement.TeiidServer;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * Tests for importing relational models from various sources
 * 
 * @author lfabriko
 */
@RunWith(RedDeerSuite.class)
@TeiidServer(state = ServerReqState.RUNNING, connectionProfiles = {
	ConnectionProfilesConstants.DB2_101_BQT,
	ConnectionProfilesConstants.DB2_97_BQT2,
	ConnectionProfilesConstants.ORACLE_11G_BQT2,
	ConnectionProfilesConstants.ORACLE_11G_BOOKS,
	ConnectionProfilesConstants.ORACLE_12C_BQT,
	ConnectionProfilesConstants.SQL_SERVER_2008_PARTS_SUPPLIER,
	ConnectionProfilesConstants.SQL_SERVER_2012_BQT2,
	ConnectionProfilesConstants.DV6_DS1,
	ConnectionProfilesConstants.MYSQL_51_BQT2,
	ConnectionProfilesConstants.MYSQL_55_BQT2,
	ConnectionProfilesConstants.POSTGRESQL_84_BQT2,
	ConnectionProfilesConstants.POSTGRESQL_92_DVQE,
	ConnectionProfilesConstants.SYBASE_15_BQT2,
	ConnectionProfilesConstants.INGRES_10_BQT2 })
public class JDBCImportWizardTest {

	@InjectRequirement
	private static TeiidServerRequirement teiidServer;

	public static final String MODEL_PROJECT = "jdbcImportTest";

	private static TeiidBot teiidBot = new TeiidBot();

	@BeforeClass
	public static void before() {

		teiidBot.uncheckBuildAutomatically();
		new ModelExplorerManager().createProject(MODEL_PROJECT);
		new ServerManager().getServersViewExt().refreshServer(teiidServer.getName());
	}

	@Before
	public void openPerspective() {
		TeiidPerspective.getInstance();
	}

	// ============== generated jdbc tests ===========================

	@Test
	public void db2101Import() {

		String model = "db2101Model";
		importModel(model, ConnectionProfilesConstants.DB2_101_BQT, "BQT/TABLE/SMALLA,BQT/TABLE/SMALLB", false);
		checkImportedTablesInModel(model, "SMALLA", "SMALLB");
	}

	@Test
	public void db297Import() {

		String model = "db297Model";
		importModel(model, ConnectionProfilesConstants.DB2_97_BQT2, "BQT2/TABLE/SMALLA,BQT2/TABLE/SMALLB", false);
		checkImportedTablesInModel(model, "SMALLA", "SMALLB");
	}

	@Test
	public void ingres10Import() {

		String model = "ingres10Model";
		importModel(model, ConnectionProfilesConstants.INGRES_10_BQT2, "bqt2/TABLE/smalla,bqt2/TABLE/smallb", false);
		checkImportedTablesInModel(model, "smalla", "smallb");
	}

	@Test
	public void oracle11gImport() {

		String model = "oracle11gModel";
		importModel(model, ConnectionProfilesConstants.ORACLE_11G_BQT2, "BQT2/TABLE/SMALLA,BQT2/TABLE/SMALLB", false);
		checkImportedTablesInModel(model, "SMALLA", "SMALLB");
	}
	
	@Test
	@Ignore
	public void oracle11gPackageImport() {
		String model = "oracle11gModelPackage";
		importModel(model, ConnectionProfilesConstants.ORACLE_11G_BOOKS, "BOOKS/procedure/REMOVE_AUTHOR2", true);
		checkImportedProcedureInModel(model,"REMOVE_AUTHOR2","90");
	}

	@Test
	public void oracle12cImport() {

		String model = "oracle12cModel";
		importModel(model, ConnectionProfilesConstants.ORACLE_12C_BQT, "DV/TABLE/SMALLA,DV/TABLE/SMALLB", false);
		checkImportedTablesInModel(model, "SMALLA", "SMALLB");
	}

	@Test
	public void sqlServer2008Import() {
		String model = "sqlServer2008Model";
		importModel(model, ConnectionProfilesConstants.SQL_SERVER_2008_PARTS_SUPPLIER,"partssupplier/dbo/TABLE/SHIP_VIA,partssupplier/dbo/TABLE/PARTS", false);
		assertTrue(checkNameInTable("\"AVERAGE TIME DELIVERY\"",6,2));
		checkImportedTablesInModel(model, "SHIP_VIA", "PARTS");
	}

	@Test
	public void sqlServer2012Import() {

		String model = "sqlServer2012Model";
		importModel(model, ConnectionProfilesConstants.SQL_SERVER_2012_BQT2,
				"bqt2/dbo/TABLE/SmallA,bqt2/dbo/TABLE/SmallB", false);
		checkImportedTablesInModel(model, "SmallA", "SmallB");
	}

	@Test
	@Jira("TEIIDDES-2458")
	@RunIf(conditionClass = IssueIsClosed.class)
	public void sybaseImport() {
		String model = "sybaseModel";
		importModel(model, ConnectionProfilesConstants.SYBASE_15_BQT2, "bqt2/TABLE/SmallA,bqt2/TABLE/SmallB", false);
		checkImportedTablesInModel(model, "SmallA", "SmallB");
	}

	@Test
	public void dv6Import() {

		String model = "dv6Model";
		importModel(model, ConnectionProfilesConstants.DV6_DS1, "PUBLIC/PUBLIC/TABLE/STATUS,PUBLIC/PUBLIC/TABLE/PARTS", false);
		checkImportedTablesInModel(model, "STATUS", "PARTS");
	}

	@Test
	public void mysql51Import() {
		String model = "mysql51Model";
		importModel(model, ConnectionProfilesConstants.MYSQL_51_BQT2, "bqt2/TABLE/smalla,bqt2/TABLE/smallb", false);
		checkImportedTablesInModel(model, "smalla", "smallb");
	}

	@Test
	public void mysql55Import() {

		String model = "mysql55Model";
		importModel(model, ConnectionProfilesConstants.MYSQL_55_BQT2, "bqt2/TABLE/smalla,bqt2/TABLE/smallb", false);
		checkImportedTablesInModel(model, "smalla", "smallb");
	}

	@Test
	public void postgresql84Import() {

		String model = "postgresql84Model";
		importModel(model, ConnectionProfilesConstants.POSTGRESQL_84_BQT2, "public/TABLE/smalla,public/TABLE/smallb", false);
		checkImportedTablesInModel(model, "smalla", "smallb");
	}

	@Test
	public void postgresql92Import() {

		String model = "postgresql92Model";
		importModel(model, ConnectionProfilesConstants.POSTGRESQL_92_DVQE, "public/TABLE/smalla,public/TABLE/smallb", false);
		checkImportedTablesInModel(model, "smalla", "smallb");
	}

	private void importModel(String modelName, String connectionProfile, String itemList, boolean importProcedures) {
		Properties iProps = new Properties();
		iProps.setProperty("itemList", itemList);
		new ImportManager().importFromDatabase(MODEL_PROJECT, modelName, connectionProfile, iProps, importProcedures);
	}

	private void checkImportedTablesInModel(String model, String tableA, String tableB) {
		teiidBot.assertResource(MODEL_PROJECT, model + ".xmi", tableA);
		teiidBot.assertResource(MODEL_PROJECT, model + ".xmi", tableB);
		teiidBot.simulateTablesPreview(teiidServer, MODEL_PROJECT, model, new String[] { tableA, tableB });

	}
	
	private void checkImportedProcedureInModel(String model, String procedure, String...parameters) {
		teiidBot.assertResource(MODEL_PROJECT, model + ".xmi", procedure);
		teiidBot.simulateProcedurePreview(teiidServer, MODEL_PROJECT, model, procedure, parameters);
	}
	/**
	 * true if the text in the table (the intersection of the row and column) is equal to the argument
	 */
	private boolean checkNameInTable(String value,int row, int column){
		new DefaultCTabItem("Table Editor").activate();
		new DefaultTabItem("Columns").activate();
		List<TableItem> items = new DefaultTable().getItems();
		String nis = items.get(row).getText(column);
		return value.equals(nis);
	}
}