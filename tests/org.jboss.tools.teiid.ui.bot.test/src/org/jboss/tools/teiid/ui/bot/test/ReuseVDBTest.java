package org.jboss.tools.teiid.ui.bot.test;

import static org.junit.Assert.*;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Properties;

import org.jboss.reddeer.common.wait.AbstractWait;
import org.jboss.reddeer.common.wait.TimePeriod;
import org.jboss.reddeer.junit.requirement.inject.InjectRequirement;
import org.jboss.reddeer.junit.runner.RedDeerSuite;
import org.jboss.reddeer.requirements.openperspective.OpenPerspectiveRequirement.OpenPerspective;
import org.jboss.reddeer.requirements.server.ServerReqState;
import org.jboss.reddeer.swt.impl.button.PushButton;
import org.jboss.tools.common.reddeer.JiraClient;
import org.jboss.tools.teiid.reddeer.ModelBuilder;
import org.jboss.tools.teiid.reddeer.ModelClass;
import org.jboss.tools.teiid.reddeer.ModelType;
import org.jboss.tools.teiid.reddeer.connection.ConnectionProfileConstants;
import org.jboss.tools.teiid.reddeer.connection.TeiidJDBCHelper;
import org.jboss.tools.teiid.reddeer.editor.ModelEditor;
import org.jboss.tools.teiid.reddeer.editor.VDBEditor;
import org.jboss.tools.teiid.reddeer.manager.ImportManager;
import org.jboss.tools.teiid.reddeer.manager.ModelExplorerManager;
import org.jboss.tools.teiid.reddeer.perspective.TeiidPerspective;
import org.jboss.tools.teiid.reddeer.requirement.TeiidServerRequirement;
import org.jboss.tools.teiid.reddeer.requirement.TeiidServerRequirement.TeiidServer;
import org.jboss.tools.teiid.reddeer.view.ModelExplorer;
import org.jboss.tools.teiid.reddeer.wizard.MetadataModelWizard;
import org.jboss.tools.teiid.reddeer.wizard.VdbWizard;
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
	private static final String PROJECT_LOCATION = "resources/projects/"+ PROJECT_NAME;
	private static final String NAME_ORACLE_MODEL = "sourceModel";
	private static final String VIEW_SOURCE_MODEL = "viewFromSource";
	private static final String SOURCE_VDB = "sourceVDB";
	
	private static final String PROJECT_NAME_REUSE = "reuseVDBProject";
	private static final String VIEW_REUSE_MODEL = "viewReuse";
	private static final String REUSE_VDB = "reuseVDB";

	@InjectRequirement
	private static TeiidServerRequirement teiidServer;
		
	@BeforeClass
	public static void before() {
		new ModelExplorer().importProject(PROJECT_LOCATION);
		new ModelExplorerManager().changeConnectionProfile(ConnectionProfileConstants.SQL_SERVER_2008_PARTS_SUPPLIER, PROJECT_NAME, NAME_ORACLE_MODEL);
	}
	
	@Test
	public void reuseVDBtest(){
		VdbWizard wizardVDB = new VdbWizard();
		wizardVDB.open();
		wizardVDB.setLocation(PROJECT_NAME)
				 .setName(SOURCE_VDB)
				 .addModel(PROJECT_NAME,VIEW_SOURCE_MODEL + ".xmi")
				 .finish();
		new ModelExplorer().executeVDB(PROJECT_NAME, SOURCE_VDB);
		TeiidPerspective.getInstance();
		
		new ModelExplorerManager().createProject(PROJECT_NAME_REUSE);
		Properties iProps = new Properties();
		iProps.setProperty("itemList", VIEW_SOURCE_MODEL);
		new ImportManager().importFromDatabase(PROJECT_NAME_REUSE,VIEW_SOURCE_MODEL, SOURCE_VDB + " - localhost - Teiid Connection", iProps, false);
		
		MetadataModelWizard modelWizard = new MetadataModelWizard();
		modelWizard.open();
		modelWizard.activate();
		modelWizard.setLocation(PROJECT_NAME_REUSE);
		modelWizard.setModelName(VIEW_REUSE_MODEL);
		modelWizard.selectModelClass(ModelClass.RELATIONAL);
		modelWizard.selectModelType(ModelType.VIEW);
		modelWizard.selectModelBuilder(ModelBuilder.TRANSFORM_EXISTING);
		modelWizard.next();
		try{
			new PushButton("OK").click();
		}catch(Exception ex){
			
		}
		modelWizard.finish();
		new ModelEditor(VIEW_REUSE_MODEL + ".xmi").save();

		wizardVDB = new VdbWizard();
		wizardVDB.open();
		wizardVDB.setLocation(PROJECT_NAME_REUSE)
				 .setName(REUSE_VDB)
				 .addModel(PROJECT_NAME_REUSE,VIEW_REUSE_MODEL + ".xmi")
				 .finish();
		new ModelExplorer().deployVdb(PROJECT_NAME_REUSE, REUSE_VDB);
		
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
		new ModelExplorer().openModelEditor(PROJECT_NAME,VIEW_SOURCE_MODEL+".xmi","version");
		ModelEditor editor = new ModelEditor(VIEW_SOURCE_MODEL+".xmi");
		editor.showTransformation();
		editor.setTransformation("SELECT 'version2'");
		editor.saveAndValidateSql();
		AbstractWait.sleep(TimePeriod.NORMAL);
		new TeiidBot().saveAll();
		VDBEditor vdb = new VDBEditor(SOURCE_VDB + ".vdb");
		vdb.show();
		vdb.synchronizeAll();
		vdb.setVersion(2);
		new ModelEditor(SOURCE_VDB + ".vdb").save();		
		new ModelExplorer().deployVdb(PROJECT_NAME, SOURCE_VDB);
		
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
		new ModelEditor(REUSE_VDB + ".vdb").save();
		new ModelExplorer().deployVdb(PROJECT_NAME_REUSE, REUSE_VDB);
		
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
}
