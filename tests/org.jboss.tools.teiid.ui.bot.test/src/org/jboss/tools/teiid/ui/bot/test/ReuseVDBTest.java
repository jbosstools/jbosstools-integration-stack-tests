package org.jboss.tools.teiid.ui.bot.test;

import static org.junit.Assert.assertEquals;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.jboss.reddeer.common.wait.AbstractWait;
import org.jboss.reddeer.common.wait.TimePeriod;
import org.jboss.reddeer.common.wait.WaitWhile;
import org.jboss.reddeer.core.condition.JobIsRunning;
import org.jboss.reddeer.junit.requirement.inject.InjectRequirement;
import org.jboss.reddeer.junit.runner.RedDeerSuite;
import org.jboss.reddeer.requirements.openperspective.OpenPerspectiveRequirement.OpenPerspective;
import org.jboss.reddeer.requirements.server.ServerReqState;
import org.jboss.reddeer.swt.impl.button.PushButton;
import org.jboss.reddeer.swt.impl.menu.ContextMenu;
import org.jboss.reddeer.swt.impl.menu.ShellMenu;
import org.jboss.reddeer.swt.impl.tree.DefaultTreeItem;
import org.jboss.reddeer.workbench.impl.shell.WorkbenchShell;
import org.jboss.tools.common.reddeer.JiraClient;
import org.jboss.tools.teiid.reddeer.ModelBuilder;
import org.jboss.tools.teiid.reddeer.ModelClass;
import org.jboss.tools.teiid.reddeer.ModelType;
import org.jboss.tools.teiid.reddeer.condition.IsInProgress;
import org.jboss.tools.teiid.reddeer.connection.ConnectionProfileConstants;
import org.jboss.tools.teiid.reddeer.connection.TeiidJDBCHelper;
import org.jboss.tools.teiid.reddeer.editor.RelationalModelEditor;
import org.jboss.tools.teiid.reddeer.editor.TransformationEditor;
import org.jboss.tools.teiid.reddeer.editor.VDBEditor;
import org.jboss.tools.teiid.reddeer.matcher.ModelEditorItemMatcher;
import org.jboss.tools.teiid.reddeer.perspective.TeiidPerspective;
import org.jboss.tools.teiid.reddeer.requirement.TeiidServerRequirement;
import org.jboss.tools.teiid.reddeer.requirement.TeiidServerRequirement.TeiidServer;
import org.jboss.tools.teiid.reddeer.view.ModelExplorer;
import org.jboss.tools.teiid.reddeer.wizard.imports.ImportJDBCDatabaseWizard;
import org.jboss.tools.teiid.reddeer.wizard.newWizard.MetadataModelWizard;
import org.jboss.tools.teiid.reddeer.wizard.newWizard.VdbWizard;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * @author mkralik
 */

@RunWith(RedDeerSuite.class)
@OpenPerspective(TeiidPerspective.class)
@TeiidServer(state = ServerReqState.RUNNING, connectionProfiles={
		ConnectionProfileConstants.SQL_SERVER_2008_PARTS_SUPPLIER,
})

public class ReuseVDBTest {
	private static final String PROJECT_NAME = "reuseVDB";
	private static final String NAME_ORACLE_MODEL = "sourceModel";
	private static final String VIEW_SOURCE_MODEL = "viewFromSource";
	private static final String SOURCE_VDB = "sourceVDB";
	
	private static final String PROJECT_NAME_REUSE = "reuseVDBProject";
	private static final String VIEW_REUSE_MODEL = "viewReuse";
	private static final String REUSE_VDB = "reuseVDB";

	@InjectRequirement
	private static TeiidServerRequirement teiidServer;
	
	private static ModelExplorer modelExplorer;
	
	@BeforeClass
	public static void before() {
		modelExplorer = new ModelExplorer();
		modelExplorer.importProject(PROJECT_NAME);
		modelExplorer.changeConnectionProfile(ConnectionProfileConstants.SQL_SERVER_2008_PARTS_SUPPLIER, PROJECT_NAME, NAME_ORACLE_MODEL);
	}
	
	@Test
	public void reuseVDBtest(){
		VdbWizard.openVdbWizard()
				.setLocation(PROJECT_NAME)
				.setName(SOURCE_VDB)
				.addModel(PROJECT_NAME,VIEW_SOURCE_MODEL + ".xmi")
				.finish();
		executeVDB(PROJECT_NAME, SOURCE_VDB);
		
		modelExplorer.createProject(PROJECT_NAME_REUSE);
		ImportJDBCDatabaseWizard.openWizard()
				.setConnectionProfile(SOURCE_VDB + " - localhost - Teiid Connection")
				.nextPage()
				.setTableTypes(false, true, false)
				.nextPage()
				.setTables(VIEW_SOURCE_MODEL)
				.nextPage()
				.setFolder(PROJECT_NAME_REUSE)
				.finish();
		
		MetadataModelWizard.openWizard()
				.setLocation(PROJECT_NAME_REUSE)
				.setModelName(VIEW_REUSE_MODEL)
				.selectModelClass(ModelClass.RELATIONAL)
				.selectModelType(ModelType.VIEW)
				.selectModelBuilder(ModelBuilder.TRANSFORM_EXISTING)
				.nextPage();
		try{
			new PushButton("OK").click();
		}catch(Exception ex){
			
		}
		MetadataModelWizard.getInstance().finish();
		new RelationalModelEditor(VIEW_REUSE_MODEL + ".xmi").save();

		VdbWizard.openVdbWizard()
				.setLocation(PROJECT_NAME_REUSE)
				.setName(REUSE_VDB)
				.addModel(PROJECT_NAME_REUSE,VIEW_REUSE_MODEL + ".xmi")
				.finish();
		modelExplorer.deployVdb(PROJECT_NAME_REUSE, REUSE_VDB);

		/* test version 1 */
		TeiidJDBCHelper jdbchelper = new TeiidJDBCHelper(teiidServer, REUSE_VDB);
		try {
			ResultSet rs = jdbchelper.executeQueryWithResultSet("SELECT * FROM "+VIEW_REUSE_MODEL+".version");
			rs.next();
			assertEquals("version1", rs.getString(1));
			assertEquals(16,jdbchelper.getNumberOfResults("SELECT * FROM "+VIEW_REUSE_MODEL+".PARTS"));
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			jdbchelper.closeConnection();
		}
		/*change sourceVDB version to 2*/
		modelExplorer.openModelEditor(PROJECT_NAME,VIEW_SOURCE_MODEL+".xmi","version");		
		TransformationEditor transformation = new RelationalModelEditor(VIEW_SOURCE_MODEL + ".xmi").openTransformationDiagram(ModelEditorItemMatcher.TABLE, "version");
		transformation.setTransformation("SELECT 'version2'");
		transformation.saveAndValidateSql();
		
		AbstractWait.sleep(TimePeriod.NORMAL);
		new ShellMenu("File", "Save All").select();
		VDBEditor vdb = new VDBEditor(SOURCE_VDB + ".vdb");
		vdb.show();
		vdb.synchronizeAll();
		vdb.setVersion(2);
		new RelationalModelEditor(SOURCE_VDB + ".vdb").save();
		modelExplorer.deployVdb(PROJECT_NAME, SOURCE_VDB);
		
		/* test version 1 */
		if(new JiraClient().isIssueClosed("TEIIDDES-2848")){
			jdbchelper = new TeiidJDBCHelper(teiidServer, REUSE_VDB);
			try {
				ResultSet rs = jdbchelper.executeQueryWithResultSet("SELECT * FROM "+VIEW_REUSE_MODEL+".version");
				rs.next();
				assertEquals("version1", rs.getString(1));  
				assertEquals(17,jdbchelper.getNumberOfResults("SELECT * FROM "+VIEW_REUSE_MODEL+".PARTS"));
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		/* change and test reuseVDB with version 2 */
		vdb = new VDBEditor(REUSE_VDB + ".vdb");
		vdb.show();
		vdb.setImportVDB(SOURCE_VDB,2,false);
		new RelationalModelEditor(REUSE_VDB + ".vdb").save();
		
		modelExplorer.deployVdb(PROJECT_NAME_REUSE, REUSE_VDB);
		
		jdbchelper = new TeiidJDBCHelper(teiidServer, REUSE_VDB);
		try {
			ResultSet rs = jdbchelper.executeQueryWithResultSet("SELECT * FROM "+VIEW_REUSE_MODEL+".version");
			rs.next();
			assertEquals("version2", rs.getString(1));  
			assertEquals(16,jdbchelper.getNumberOfResults("SELECT * FROM "+VIEW_REUSE_MODEL+".PARTS"));
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * deploy vdb and create connection profile (modelExplorer.deployVDB() doesn't create CP)
	 */
	private void executeVDB(String project, String vdb) {
 		modelExplorer.open();
 		vdb = (vdb.contains(".vdb")) ? vdb : vdb + ".vdb";
 		
 		new DefaultTreeItem(project, vdb).select();
 		new ContextMenu("Modeling", "Execute VDB").select();
 
 		new WaitWhile(new IsInProgress(), TimePeriod.VERY_LONG);
 		new WaitWhile(new JobIsRunning(), TimePeriod.LONG);
 		new WorkbenchShell();
 		TeiidPerspective.getInstance();
 	}
}
