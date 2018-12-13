package org.jboss.tools.teiid.ui.bot.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.eclipse.reddeer.common.wait.AbstractWait;
import org.eclipse.reddeer.common.wait.TimePeriod;
import org.eclipse.reddeer.common.wait.WaitWhile;
import org.eclipse.reddeer.junit.requirement.inject.InjectRequirement;
import org.eclipse.reddeer.junit.runner.RedDeerSuite;
import org.eclipse.reddeer.requirements.openperspective.OpenPerspectiveRequirement.OpenPerspective;
import org.eclipse.reddeer.requirements.server.ServerRequirementState;
import org.eclipse.reddeer.swt.impl.menu.ContextMenuItem;
import org.eclipse.reddeer.workbench.core.condition.JobIsRunning;
import org.eclipse.reddeer.workbench.impl.shell.WorkbenchShell;
import org.jboss.tools.teiid.reddeer.condition.IsInProgress;
import org.jboss.tools.teiid.reddeer.connection.ConnectionProfileConstants;
import org.jboss.tools.teiid.reddeer.connection.TeiidJDBCHelper;
import org.jboss.tools.teiid.reddeer.dialog.DataRolesDialog;
import org.jboss.tools.teiid.reddeer.dialog.DataRolesDialog.PermissionType;
import org.jboss.tools.teiid.reddeer.editor.ModelEditor.ItemType;
import org.jboss.tools.teiid.reddeer.editor.RelationalModelEditor;
import org.jboss.tools.teiid.reddeer.editor.TransformationEditor;
import org.jboss.tools.teiid.reddeer.editor.VdbEditor;
import org.jboss.tools.teiid.reddeer.perspective.TeiidPerspective;
import org.jboss.tools.teiid.reddeer.requirement.TeiidServerRequirement;
import org.jboss.tools.teiid.reddeer.requirement.TeiidServerRequirement.TeiidServer;
import org.jboss.tools.teiid.reddeer.view.ModelExplorer;
import org.jboss.tools.teiid.reddeer.wizard.imports.ImportJDBCDatabaseWizard;
import org.jboss.tools.teiid.reddeer.wizard.newWizard.MetadataModelWizard;
import org.jboss.tools.teiid.reddeer.wizard.newWizard.MetadataModelWizard.ModelBuilder;
import org.jboss.tools.teiid.reddeer.wizard.newWizard.MetadataModelWizard.ModelClass;
import org.jboss.tools.teiid.reddeer.wizard.newWizard.MetadataModelWizard.ModelType;
import org.jboss.tools.teiid.reddeer.wizard.newWizard.VdbWizard;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * @author skaleta
 * tested features:
 * 	- every data role permission
 *  - data role features:
 *  	- row filtering
 *  	- column masking
 *   - reuse VDB and override data roles
 */
@RunWith(RedDeerSuite.class)
@OpenPerspective(TeiidPerspective.class)
@TeiidServer(state = ServerRequirementState.RUNNING, connectionProfiles = {ConnectionProfileConstants.ORACLE_11G_PRODUCTS})
public class DataRolesTest {
	private static final String PROJECT_NAME = "DataRolesProject";
	private static final String VIEW_MODEL = "Products_view.xmi";
	private static final String SOURCE_MODEL = "Products_source.xmi";
	private static final String VDB_NAME = "DataRolesVdb";
	
	@InjectRequirement
	private static TeiidServerRequirement teiidServer;
	
	@Before
	public void importProject(){
		ModelExplorer modelExplorer = new ModelExplorer();
		modelExplorer.importProject(PROJECT_NAME);
		modelExplorer.refreshProject(PROJECT_NAME);
		modelExplorer.changeConnectionProfile(ConnectionProfileConstants.ORACLE_11G_PRODUCTS, PROJECT_NAME, "sources", SOURCE_MODEL);
	}
	
	@After
	public void cleanUp(){
		new ModelExplorer().deleteAllProjectsSafely();
	}
	
	@Test
	public void testDataRoles(){
		new ModelExplorer().selectItem(PROJECT_NAME);
		VdbWizard.openVdbWizard()
				.setName(VDB_NAME)
				.addModel(PROJECT_NAME, "views", VIEW_MODEL)
				.finish();
		
		VdbEditor vdbEditor = VdbEditor.getInstance(VDB_NAME);
		
		vdbEditor.addDataRole()
				.setName("Readers")
				.addRole("readers")
				.finish();
		
		vdbEditor.addDataRole()
				.setName("Executors")
				.addRole("executors")
				.setModelPermission(PermissionType.EXECUTE, true, VIEW_MODEL)
				.setModelPermission(PermissionType.EXECUTE, true, SOURCE_MODEL)
				.setModelPermission(PermissionType.READ, false, VIEW_MODEL)
				.setModelPermission(PermissionType.READ, false, SOURCE_MODEL)
				.finish();
		
		vdbEditor.addDataRole()
				.setName("Inserters")
				.addRole("inserters")
				.setModelPermission(PermissionType.CREATE, true, VIEW_MODEL)
				.setModelPermission(PermissionType.CREATE, true, SOURCE_MODEL)
				.setModelPermission(PermissionType.READ, false, VIEW_MODEL)
				.setModelPermission(PermissionType.READ, false, SOURCE_MODEL)
				.finish();
		
		vdbEditor.addDataRole()
				.setName("Deleters")
				.addRole("deleters")
				.setModelPermission(PermissionType.DELETE, true, VIEW_MODEL)
				.setModelPermission(PermissionType.DELETE, true, SOURCE_MODEL)
				.setModelPermission(PermissionType.READ, false, VIEW_MODEL)
				.setModelPermission(PermissionType.READ, false, SOURCE_MODEL)
				.setModelPermission(PermissionType.READ, true, VIEW_MODEL, "PRODUCTDATA", "INSTR_ID : string(10)")
				.finish();
		
		vdbEditor.addDataRole()
				.setName("Updaters")
				.addRole("updaters")
				.setModelPermission(PermissionType.UPDATE, true, VIEW_MODEL)
				.setModelPermission(PermissionType.UPDATE, true, SOURCE_MODEL)
				.setModelPermission(PermissionType.READ, false, VIEW_MODEL)
				.setModelPermission(PermissionType.READ, false, SOURCE_MODEL)
				.setModelPermission(PermissionType.READ, true, VIEW_MODEL, "PRODUCTDATA", "INSTR_ID : string(10)")
				.finish();
		
		vdbEditor.addDataRole()
				.setName("Admins")
				.addRole("admins")
				.setModelPermission(PermissionType.ALTER, true, SOURCE_MODEL)
				.setModelPermission(PermissionType.ALTER, true, VIEW_MODEL)
				.setModelPermission(PermissionType.CREATE, true, SOURCE_MODEL)
				.setModelPermission(PermissionType.CREATE, true, VIEW_MODEL)
				.setModelPermission(PermissionType.DELETE, true, SOURCE_MODEL)
				.setModelPermission(PermissionType.DELETE, true, VIEW_MODEL)
				.setModelPermission(PermissionType.EXECUTE, true, SOURCE_MODEL)
				.setModelPermission(PermissionType.EXECUTE, true, VIEW_MODEL)
				.setModelPermission(PermissionType.UPDATE, true, SOURCE_MODEL)
				.setModelPermission(PermissionType.UPDATE, true, VIEW_MODEL)
				.finish();

		vdbEditor.save();
		
		new ModelExplorer().deployVdb(PROJECT_NAME, VDB_NAME);
		
		executeQueriesForUser(VDB_NAME, "Products_view", "admin", new boolean[]{true, true, true, true, true});
		executeQueriesForUser(VDB_NAME, "Products_view", "reader", new boolean[]{true, false, false, false, false});
		executeQueriesForUser(VDB_NAME, "Products_view", "executor", new boolean[]{false, false, false, false, true});
		executeQueriesForUser(VDB_NAME, "Products_view", "updater", new boolean[]{false, true, false, false, false});
		executeQueriesForUser(VDB_NAME, "Products_view", "inserter", new boolean[]{false, false, true, false, false});
		executeQueriesForUser(VDB_NAME, "Products_view", "deleter", new boolean[]{false, false, false, true, false});
		executeQueriesForUser(VDB_NAME, "Products_view", "user", new boolean[]{false, false, false, false, false});
	}
	
	@Test
	public void testDataRoleFeatures() throws SQLException {
		new ModelExplorer().selectItem(PROJECT_NAME);
		VdbWizard.openVdbWizard()
				.setName(VDB_NAME)
				.addModel(PROJECT_NAME, "views", VIEW_MODEL)
				.finish();
		
		VdbEditor vdbEditor = VdbEditor.getInstance(VDB_NAME);
		
		vdbEditor.addDataRole()
				.setName("Readers")
				.addRole("readers")
				.addColumnMask("", "CASE WHEN TRUE THEN 'SECRET' END", 0, VIEW_MODEL, "PRODUCTDATA", "INSTR_ID : string(10)")
				.finish();
		
		vdbEditor.addDataRole()
				.setName("Updaters")
				.addRole("updaters")
				.setModelPermission(PermissionType.UPDATE, true, VIEW_MODEL)
				.setModelPermission(PermissionType.UPDATE, true, SOURCE_MODEL)
				.setModelPermission(PermissionType.READ, false, SOURCE_MODEL)
				.addRowFilter("Products_view.PRODUCTDATA.ISAMEXINT=1", true, VIEW_MODEL, "PRODUCTDATA")
				.finish();
		
		vdbEditor.save();
		
		new ModelExplorer().deployVdb(PROJECT_NAME, VDB_NAME);
		
		String query = "SELECT * FROM Products_view.PRODUCTDATA";
		TeiidJDBCHelper jdbcHelper = new TeiidJDBCHelper(teiidServer, VDB_NAME);
		
        jdbcHelper.setUser("reader", "dvdvdv0#");
		ResultSet readerRs = jdbcHelper.executeQueryWithResultSet(query);
		while (readerRs.next()){
			if (!readerRs.getString("INSTR_ID").equals("SECRET")){
				fail("Column is not masked! row='"+ readerRs.getRow() +"' value='" + readerRs.getString("INSTR_ID") + "'");
			}
		}
		
        jdbcHelper.setUser("updater", "dvdvdv0#");
		ResultSet updaterRs = jdbcHelper.executeQueryWithResultSet(query);
		while (updaterRs.next()){
			if (!updaterRs.getString("ISAMEXINT").equals("1")){
				fail("Row is not filtered! row='"+ updaterRs.getRow() +"' value='" + updaterRs.getString("ISAMEXINT") + "'");
			}
		}		
	}
	
	@Test
	public void testDataRoleVdbReuse(){ 
		ModelExplorer modelExplorer = new ModelExplorer();
		modelExplorer.openModelEditor(PROJECT_NAME, "PreparedVdb.vdb");
		VdbEditor.getInstance("PreparedVdb.vdb").synchronizeAll();
		AbstractWait.sleep(TimePeriod.SHORT);
		modelExplorer.selectItem(PROJECT_NAME, "PreparedVdb.vdb");
 		new ContextMenuItem("Modeling", "Execute VDB").select();
 		new WaitWhile(new IsInProgress(), TimePeriod.VERY_LONG);
 		new WaitWhile(new JobIsRunning(), TimePeriod.LONG);
 		new WorkbenchShell();
 		TeiidPerspective.activate();
 		
 		executeQueriesForUser("PreparedVdb", "Products_view", "user", new boolean[]{false, false, false, false, false});
 		
 		String project = "DataRolesReuseProject";
 		modelExplorer.createProject(project);
 		modelExplorer.selectItem(project);
 		
 		ImportJDBCDatabaseWizard.openWizard()
 				.setConnectionProfile("PreparedVdb - localhost - Teiid Connection")
 				.nextPage()
 				.procedures(true)
 				.nextPage()
 				.setTables("Products_view")
 				.nextPage()
 				.finish();
 		
 		modelExplorer.selectItem(project);
 		MetadataModelWizard.openWizard()
 				.setModelName("VdbReuseView")
 				.selectModelClass(ModelClass.RELATIONAL)
 				.selectModelType(ModelType.VIEW)
 				.selectModelBuilder(ModelBuilder.TRANSFORM_EXISTING)
 				.nextPage()
 				.setExistingModel(project, "Products_view.xmi")
 				.finish();
 		
 		modelExplorer.openModelEditor(project, "VdbReuseView.xmi");
 		RelationalModelEditor editor = new RelationalModelEditor("VdbReuseView.xmi");
 		editor.openTransformationDiagram(ItemType.TABLE, "PRODUCTDATA").supportsUpdate(true);
 		editor.returnToParentDiagram();
 		TransformationEditor tEditor = editor.openTransformationDiagram(ItemType.PROCEDURE, "test");
 		tEditor.insertAndValidateSql("CREATE VIRTUAL PROCEDURE BEGIN " + 
 				"SELECT Products_view.PRODUCTDATA.NAME, Products_view.PRODUCTDATA.PRIBUSINESS FROM "
 				+ "Products_view.PRODUCTDATA WHERE VdbReuseView.test.param = Products_view.PRODUCTDATA.PRIBUSINESS; END");
 		editor.saveAndClose();
 		
 		VdbWizard.openVdbWizard()
 				.setName("ReuseVdb")
 				.addModel(project, "VdbReuseView.xmi")
 				.finish();
 		
        modelExplorer.deployVdb(project, "ReuseVdb.vdb");
 		
 		executeQueriesForUser("ReuseVdb", "VdbReuseView", "user", new boolean[]{true, true, true, true, true});
 		
 		VdbEditor vdbEditor = VdbEditor.getInstance("ReuseVdb");
 		vdbEditor.addDataRole()
 				.setName("Customers")
 				.addRole("customers")
 				.finish();
 		vdbEditor.save();
 		
 		modelExplorer.deployVdb(project, "ReuseVdb.vdb");
 		
 		executeQueriesForUser("ReuseVdb", "VdbReuseView", "user", new boolean[]{true, false, false, false, false});
	}
	
	@Test
    public void testClearPermissions() {
        ModelExplorer modelExplorer = new ModelExplorer();
        modelExplorer.openModelEditor(PROJECT_NAME, "PreparedVdb.vdb");
        VdbEditor vdbEditor = VdbEditor.getInstance("PreparedVdb.vdb");
        vdbEditor.synchronizeAll();
        AbstractWait.sleep(TimePeriod.SHORT);

        DataRolesDialog dialog = vdbEditor.getDataRole("user");

        dialog.setModelPermission(PermissionType.CREATE, true, SOURCE_MODEL)
            .setModelPermission(PermissionType.UPDATE, true, VIEW_MODEL)
            .setModelPermission(PermissionType.DELETE, true, SOURCE_MODEL)
            .setModelPermission(PermissionType.EXECUTE, true, VIEW_MODEL)
            .setModelPermission(PermissionType.EXECUTE, false, SOURCE_MODEL)
            .finish();

        dialog = vdbEditor.getDataRole("user");

        assertEquals(dialog.getModelPermission(DataRolesDialog.PermissionType.CREATE, SOURCE_MODEL), true);
        assertEquals(dialog.getModelPermission(DataRolesDialog.PermissionType.CREATE, VIEW_MODEL), false);
        assertEquals(dialog.getModelPermission(DataRolesDialog.PermissionType.READ, SOURCE_MODEL), true);
        assertEquals(dialog.getModelPermission(DataRolesDialog.PermissionType.READ, VIEW_MODEL), true);
        assertEquals(dialog.getModelPermission(DataRolesDialog.PermissionType.UPDATE, SOURCE_MODEL), false);
        assertEquals(dialog.getModelPermission(DataRolesDialog.PermissionType.UPDATE, VIEW_MODEL), true);
        assertEquals(dialog.getModelPermission(DataRolesDialog.PermissionType.DELETE, SOURCE_MODEL), true);
        assertEquals(dialog.getModelPermission(DataRolesDialog.PermissionType.DELETE, VIEW_MODEL), false);
        assertEquals(dialog.getModelPermission(DataRolesDialog.PermissionType.EXECUTE, SOURCE_MODEL), false);
        assertEquals(dialog.getModelPermission(DataRolesDialog.PermissionType.EXECUTE, VIEW_MODEL), true);

        dialog.cancel();

        vdbEditor.save();
        modelExplorer.deployVdb(PROJECT_NAME, "PreparedVdb.vdb");
        executeQueriesForUser("PreparedVdb", "Products_source", "teiidUser", new boolean[]{true, false, true, true, false});
        executeQueriesForUser("PreparedVdb", "Products_view", "teiidUser", new boolean[]{true, true, false, false, true});

        vdbEditor.activate();
        dialog = vdbEditor.getDataRole("user");
        dialog.clearAllPermissions();

        assertEquals(dialog.getModelPermission(DataRolesDialog.PermissionType.CREATE, SOURCE_MODEL), false);
        assertEquals(dialog.getModelPermission(DataRolesDialog.PermissionType.CREATE, VIEW_MODEL), false);
        assertEquals(dialog.getModelPermission(DataRolesDialog.PermissionType.READ, SOURCE_MODEL), false);
        assertEquals(dialog.getModelPermission(DataRolesDialog.PermissionType.READ, VIEW_MODEL), false);
        assertEquals(dialog.getModelPermission(DataRolesDialog.PermissionType.UPDATE, SOURCE_MODEL), false);
        assertEquals(dialog.getModelPermission(DataRolesDialog.PermissionType.UPDATE, VIEW_MODEL), false);
        assertEquals(dialog.getModelPermission(DataRolesDialog.PermissionType.DELETE, SOURCE_MODEL), false);
        assertEquals(dialog.getModelPermission(DataRolesDialog.PermissionType.DELETE, VIEW_MODEL), false);
        assertEquals(dialog.getModelPermission(DataRolesDialog.PermissionType.EXECUTE, SOURCE_MODEL), false);
        assertEquals(dialog.getModelPermission(DataRolesDialog.PermissionType.EXECUTE, VIEW_MODEL), false);

        dialog.setModelPermission(PermissionType.READ, true, SOURCE_MODEL)
            .setModelPermission(PermissionType.READ, true, VIEW_MODEL)
            .finish();

        vdbEditor.save();
        modelExplorer.deployVdb(PROJECT_NAME, "PreparedVdb.vdb");
        executeQueriesForUser("PreparedVdb", "Products_source", "teiidUser", new boolean[]{true, false, false, false, false});
        executeQueriesForUser("PreparedVdb", "Products_view", "teiidUser", new boolean[]{true, false, false, false, false});
	}

	private void executeQueriesForUser(String vdbName, String model, String username, boolean[] expectedResult){
        TeiidJDBCHelper jdbcHelper = new TeiidJDBCHelper(teiidServer, vdbName, username, "dvdvdv0#");

		assertTrue("READ query " + ((expectedResult[0]) ? "failed" : "succed") + " for user " + username,
				expectedResult[0] == jdbcHelper.isQuerySuccessful("SELECT * FROM " + model +".PRODUCTDATA", true));
		
		assertTrue("UPDATE query " + ((expectedResult[1]) ? "failed" : "succed") + " for user " + username,
				expectedResult[1] == jdbcHelper.isQuerySuccessful("UPDATE " + model +".PRODUCTDATA SET NAME='New Name' WHERE INSTR_ID='PRD01096'", false));
		
		assertTrue("INSERT query " + ((expectedResult[2]) ? "failed" : "succed") + " for user " + username,
				expectedResult[2] == jdbcHelper.isQuerySuccessful("INSERT INTO " + model +".PRODUCTDATA(INSTR_ID,NAME,\"TYPE\",ISSUER,EXCHANGE,ISDJI,ISSP500,"
						+ "ISNAS100,ISAMEXINT,PRIBUSINESS) VALUES('PRD02000','RedHat','Stock',NULL,'NYSE',1,1,1,1,'IT')", false));
		
		assertTrue("DELETE query " + ((expectedResult[3]) ? "failed" : "succed") + " for user " + username,
				expectedResult[3] == jdbcHelper.isQuerySuccessful("DELETE FROM " + model +".PRODUCTDATA WHERE INSTR_ID='PRD02000'", false));
		
		assertTrue("EXECUTE query " + ((expectedResult[4]) ? "failed" : "succed") + " for user " + username,
				expectedResult[4] == jdbcHelper.isQuerySuccessful("exec " + model +".test('insurance')", true));
	}
}
