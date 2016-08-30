package org.jboss.tools.teiid.ui.bot.test;

import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.jboss.reddeer.junit.execution.annotation.RunIf;
import org.jboss.reddeer.junit.requirement.inject.InjectRequirement;
import org.jboss.reddeer.junit.runner.RedDeerSuite;
import org.jboss.reddeer.requirements.server.ServerReqState;
import org.jboss.reddeer.swt.api.TableItem;
import org.jboss.reddeer.swt.impl.ctab.DefaultCTabItem;
import org.jboss.reddeer.swt.impl.menu.ShellMenu;
import org.jboss.reddeer.swt.impl.tab.DefaultTabItem;
import org.jboss.reddeer.swt.impl.table.DefaultTable;
import org.jboss.tools.common.reddeer.condition.IssueIsClosed;
import org.jboss.tools.common.reddeer.condition.IssueIsClosed.Jira;
import org.jboss.tools.teiid.reddeer.connection.ConnectionProfileConstants;
import org.jboss.tools.teiid.reddeer.connection.TeiidJDBCHelper;
import org.jboss.tools.teiid.reddeer.editor.VdbEditor;
import org.jboss.tools.teiid.reddeer.perspective.TeiidPerspective;
import org.jboss.tools.teiid.reddeer.requirement.TeiidServerRequirement;
import org.jboss.tools.teiid.reddeer.requirement.TeiidServerRequirement.TeiidServer;
import org.jboss.tools.teiid.reddeer.view.ModelExplorer;
import org.jboss.tools.teiid.reddeer.view.ServersViewExt;
import org.jboss.tools.teiid.reddeer.wizard.imports.ImportJDBCDatabaseWizard;
import org.jboss.tools.teiid.reddeer.wizard.newWizard.VdbWizard;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * Tests for importing relational models from various sources
 * 
 * @author lfabriko
 */
@RunWith(RedDeerSuite.class)
@TeiidServer(state = ServerReqState.RUNNING, connectionProfiles = {
		ConnectionProfileConstants.DB2_101_BQT,
		ConnectionProfileConstants.DB2_97_BQT2,
		ConnectionProfileConstants.ORACLE_11G_BQT2,
		ConnectionProfileConstants.ORACLE_11G_BOOKS,
		ConnectionProfileConstants.ORACLE_12C_BQT,
		ConnectionProfileConstants.SQL_SERVER_2008_PARTS_SUPPLIER,
		ConnectionProfileConstants.SQL_SERVER_2012_BQT2,
		ConnectionProfileConstants.DV6_DS1,
		ConnectionProfileConstants.MYSQL_51_BQT2,
		ConnectionProfileConstants.MYSQL_55_BQT2,
		ConnectionProfileConstants.POSTGRESQL_84_BQT2,
		ConnectionProfileConstants.POSTGRESQL_92_DVQE,
		ConnectionProfileConstants.SYBASE_15_BQT2,
		ConnectionProfileConstants.INGRES_10_BQT2,
		ConnectionProfileConstants.SAP_HANA})
public class JDBCImportWizardTest {

	@InjectRequirement
	private static TeiidServerRequirement teiidServer;

	public static final String MODEL_PROJECT = "jdbcImportTest";

	@BeforeClass
	public static void before() {
		if (new ShellMenu("Project", "Build Automatically").isSelected()) {
			new ShellMenu("Project", "Build Automatically").select();
		}
		new ModelExplorer().createProject(MODEL_PROJECT);
		new ServersViewExt().refreshServer(teiidServer.getName());
	}

	@Before
	public void openPerspective() {
		TeiidPerspective.activate();
	}

	// ============== generated jdbc tests ===========================

	@Test
	public void db2101Import() {
		String model = "db2101Model";
		importModel(model, ConnectionProfileConstants.DB2_101_BQT, "BQT/TABLE/SMALLA,BQT/TABLE/SMALLB", false);
		checkImportedTablesInModel(model, "SMALLA", "SMALLB");
	}

	@Test
	public void db297Import() {
		String model = "db297Model";
		importModel(model, ConnectionProfileConstants.DB2_97_BQT2, "BQT2/TABLE/SMALLA,BQT2/TABLE/SMALLB", false);
		checkImportedTablesInModel(model, "SMALLA", "SMALLB");
	}
	
	@Test
	public void ingres10Import() {
		String model = "ingres10Model";
		importModel(model, ConnectionProfileConstants.INGRES_10_BQT2, "bqt2/TABLE/smalla,bqt2/TABLE/smallb", false);
		checkImportedTablesInModel(model, "smalla", "smallb");
	}

	@Test
	public void oracle11gImport() {
		String model = "oracle11gModel";
		importModel(model, ConnectionProfileConstants.ORACLE_11G_BQT2, "BQT2/TABLE/SMALLA,BQT2/TABLE/SMALLB", false);
		checkImportedTablesInModel(model, "SMALLA", "SMALLB");
	}
	
	@Test
	public void oracle11gPackageImport() {
		String model = "oracle11gModelPackage";
		importModel(model, ConnectionProfileConstants.ORACLE_11G_BOOKS, "BOOKS/procedure/REMOVE_AUTHOR2", true);
		checkImportedProcedureInModel(model,"REMOVE_AUTHOR2","90");
	}

	@Test
	public void oracle12cImport() {
		String model = "oracle12cModel";
		importModel(model, ConnectionProfileConstants.ORACLE_12C_BQT, "DV/TABLE/SMALLA,DV/TABLE/SMALLB", false);
		checkImportedTablesInModel(model, "SMALLA", "SMALLB");
	}

	@Test
	public void sqlServer2008Import() {
		String model = "sqlServer2008Model";
		importModel(model, ConnectionProfileConstants.SQL_SERVER_2008_PARTS_SUPPLIER,"partssupplier/dbo/TABLE/SHIP_VIA,partssupplier/dbo/TABLE/PARTS", false);
		assertTrue(checkNameInTable("\"AVERAGE TIME DELIVERY\"",6,2));
		checkImportedTablesInModel(model, "SHIP_VIA", "PARTS");
	}

	@Test
	public void sqlServer2012Import() {
		String model = "sqlServer2012Model";
		importModel(model, ConnectionProfileConstants.SQL_SERVER_2012_BQT2,
				"bqt2/dbo/TABLE/SmallA,bqt2/dbo/TABLE/SmallB", false);
		checkImportedTablesInModel(model, "SmallA", "SmallB");
	}

	@Test
	@Jira("TEIIDDES-2458")
	@RunIf(conditionClass = IssueIsClosed.class)
	public void sybaseImport() {
		String model = "sybaseModel";
		importModel(model, ConnectionProfileConstants.SYBASE_15_BQT2, "bqt2/TABLE/SmallA,bqt2/TABLE/SmallB", false);
		checkImportedTablesInModel(model, "SmallA", "SmallB");
	}

	@Test
	public void dv6Import() {
		String model = "dv6Model";
		importModel(model, ConnectionProfileConstants.DV6_DS1, "PUBLIC/PUBLIC/TABLE/STATUS,PUBLIC/PUBLIC/TABLE/PARTS", false);
		checkImportedTablesInModel(model, "STATUS", "PARTS");
	}

	@Test
	public void mysql51Import() {
		String model = "mysql51Model";
		importModel(model, ConnectionProfileConstants.MYSQL_51_BQT2, "bqt2/TABLE/smalla,bqt2/TABLE/smallb", false);
		checkImportedTablesInModel(model, "smalla", "smallb");
	}

	@Test
	public void mysql55Import() {
		String model = "mysql55Model";
		importModel(model, ConnectionProfileConstants.MYSQL_55_BQT2, "bqt2/TABLE/smalla,bqt2/TABLE/smallb", false);
		checkImportedTablesInModel(model, "smalla", "smallb");
	}

	@Test
	public void postgresql84Import() {
		String model = "postgresql84Model";
		importModel(model, ConnectionProfileConstants.POSTGRESQL_84_BQT2, "public/TABLE/smalla,public/TABLE/smallb", false);
		checkImportedTablesInModel(model, "smalla", "smallb");
	}

	@Test
	public void postgresql92Import() {
		String model = "postgresql92Model";
		importModel(model, ConnectionProfileConstants.POSTGRESQL_92_DVQE, "public/TABLE/smalla,public/TABLE/smallb", false);
		checkImportedTablesInModel(model, "smalla", "smallb");
	}
	

	
	/**
	 * Note: start database before this test! (AWS)
	 */
	@Test
	public void sapHanaImport() {
		String model = "sapHanaModel";
		importModel(model, ConnectionProfileConstants.SAP_HANA, "BQT1/TABLE/SMALLA,BQT1/TABLE/SMALLB", false);
		
		// TODO temp till hana translator is not set automatically (updated checkImportedTablesInModel method)
		assertTrue(new ModelExplorer().containsItem(MODEL_PROJECT,model + ".xmi", "SMALLA"));
		assertTrue(new ModelExplorer().containsItem(MODEL_PROJECT,model + ".xmi", "SMALLB"));
		
		String vdb_name = "Check_" + model;
		VdbWizard.openVdbWizard()
				.setLocation(MODEL_PROJECT)
				.setName(vdb_name)
				.addModel(MODEL_PROJECT, model)
				.finish();
		
		VdbEditor.getInstance(vdb_name + ".vdb").setModelTranslator(model + ".xmi", model, "hana");
		VdbEditor.getInstance(vdb_name + ".vdb").save();
		
		new ModelExplorer().deployVdb(MODEL_PROJECT, vdb_name);

		TeiidJDBCHelper jdbcHelper = new TeiidJDBCHelper(teiidServer, vdb_name);
		String[] tables = new String[] { "SMALLA", "SMALLB" };
		for (int i = 0; i < tables.length; i++) {
			String previewSQL = "select * from \"" + model + "\".\"" + tables[i] + "\"";
			assertTrue(jdbcHelper.isQuerySuccessful(previewSQL,true));
		}		
		//checkImportedTablesInModel(model, "SMALLA", "SMALLB");
	}

	private void importModel(String modelName, String connectionProfile, String itemList, boolean importProcedures) {
		String[] splitList = itemList.split(",");
		ImportJDBCDatabaseWizard.openWizard()
				.setConnectionProfile(connectionProfile)
				.nextPage()
				.setTableTypes(false, true, false)
				.procedures(importProcedures)
				.nextPage()
				.setTables(splitList)
				.nextPage()
				.setFolder(MODEL_PROJECT)
				.setModelName(modelName)
				.finish();
	}

	private void checkImportedTablesInModel(String model, String tableA, String tableB) {
		assertTrue(new ModelExplorer().containsItem(MODEL_PROJECT,model + ".xmi", tableA));
		assertTrue(new ModelExplorer().containsItem(MODEL_PROJECT,model + ".xmi", tableB));
		new ModelExplorer().simulateTablesPreview(teiidServer, MODEL_PROJECT, model, new String[] { tableA, tableB });

	}
	
	private void checkImportedProcedureInModel(String model, String procedure, String...parameters) {
		assertTrue(new ModelExplorer().containsItem(MODEL_PROJECT,model + ".xmi", procedure));

		String vdb_name = "Check_" + model;	
		VdbWizard.openVdbWizard()
				.setLocation(MODEL_PROJECT)
				.setName(vdb_name)
				.addModel(MODEL_PROJECT, model + ".xmi")
				.finish();
		new ModelExplorer().deployVdb(MODEL_PROJECT, vdb_name);
		
		TeiidJDBCHelper jdbcHelper = new TeiidJDBCHelper(teiidServer, vdb_name);
		ArrayList<String> parametersList = new ArrayList<String>(Arrays.asList(parameters));
		String params = "";
		for (int i = 0; i < parametersList.size(); i++) {
			params += "'" + parametersList.get(i) + "',";
		}
		params = params.substring(0, params.length() - 1);

		String previewSQL =  "exec \"" + model + "\".\"" + procedure + "\"(" + params + ")";
		assertTrue(jdbcHelper.isQuerySuccessful(previewSQL,false));
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