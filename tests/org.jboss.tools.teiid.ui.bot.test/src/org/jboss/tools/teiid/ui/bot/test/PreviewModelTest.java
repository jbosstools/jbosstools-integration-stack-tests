package org.jboss.tools.teiid.ui.bot.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.util.ArrayList;

import org.jboss.tools.teiid.reddeer.manager.ConnectionProfilesConstants;
import org.jboss.tools.teiid.reddeer.manager.ImportManager;
import org.jboss.tools.teiid.reddeer.manager.ModelExplorerManager;
import org.jboss.tools.teiid.reddeer.manager.VDBManager;
import org.jboss.reddeer.common.matcher.RegexMatcher;
import org.jboss.reddeer.common.wait.TimePeriod;
import org.jboss.reddeer.common.wait.WaitWhile;
import org.jboss.reddeer.core.condition.ShellWithTextIsActive;
import org.jboss.reddeer.junit.requirement.inject.InjectRequirement;
import org.jboss.reddeer.junit.runner.RedDeerSuite;
import org.jboss.reddeer.requirements.openperspective.OpenPerspectiveRequirement.OpenPerspective;
import org.jboss.reddeer.requirements.server.ServerReqState;
import org.jboss.tools.teiid.reddeer.condition.IsInProgress;
import org.jboss.tools.teiid.reddeer.connection.TeiidJDBCHelper;
import org.jboss.tools.teiid.reddeer.perspective.DatabaseDevelopmentPerspective;
import org.jboss.tools.teiid.reddeer.perspective.TeiidPerspective;
import org.jboss.tools.teiid.reddeer.requirement.TeiidServerRequirement;
import org.jboss.tools.teiid.reddeer.requirement.TeiidServerRequirement.TeiidServer;
import org.jboss.tools.teiid.reddeer.view.SQLResult;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(RedDeerSuite.class)
@OpenPerspective(TeiidPerspective.class)
@TeiidServer(state = ServerReqState.RUNNING, connectionProfiles={
		ConnectionProfilesConstants.ORACLE_11G_PARTS_SUPPLIER,
		ConnectionProfilesConstants.SQL_SERVER_2008_PARTS_SUPPLIER,
})
public class PreviewModelTest {

	private static final String PROJECT_NAME = "PreviewProject";
	private static TeiidBot teiidBot = new TeiidBot();
	private static final String PROJECT_LOCATION = "resources/projects/"+ PROJECT_NAME;
	private static final String NAME_ORACLE_MODEL = "partsSourceOracle";
	private static final String NAME_SQL_MODEL = "partsSourceSQLServer";
	private static final String NAME_VIEW_MODEL = "partsView";
	private static final String VDB_NAME = "previewVDB";
	private static final String PATH_PARTS_ORACLE_CSV = teiidBot.toAbsolutePath("resources/preview/oracleSource.csv") ;
	private static final String PATH_PARTS_MSSQL_CSV = teiidBot.toAbsolutePath("resources/preview/mssqlSource.csv") ;
	private static final String PATH_VIEW_TABLE_CSV = teiidBot.toAbsolutePath("resources/preview/viewTable.csv") ;
	private static final String PATH_VIEW_TWO_TABLE_CSV = teiidBot.toAbsolutePath("resources/preview/viewTableTwoSources.csv") ;
	private static final String PATH_VIEW_PROCEDURE_CSV = teiidBot.toAbsolutePath("resources/preview/queryViewProcedure.csv") ;
	private static final String PATH_VIEW_ACCESS_PATERN_TABLE_CSV = teiidBot.toAbsolutePath("resources/preview/viewTableWithAccessPattern.csv") ;
	
	@InjectRequirement
	private static TeiidServerRequirement teiidServer;
		
	@BeforeClass
	public static void before() {
		new ImportManager().importProject(PROJECT_LOCATION);
		new ModelExplorerManager().changeConnectionProfile(ConnectionProfilesConstants.ORACLE_11G_PARTS_SUPPLIER, PROJECT_NAME, NAME_ORACLE_MODEL);
		new ModelExplorerManager().changeConnectionProfile(ConnectionProfilesConstants.SQL_SERVER_2008_PARTS_SUPPLIER, PROJECT_NAME, NAME_SQL_MODEL);

	}
	
	@Before
	public void openTeiidPerspective(){
		new TeiidPerspective().open();
	}
	
	@Test
	public void previewDataSourceModel(){
		String tableOracle = "STATUS";
		String tableSQL = "SUPPLIER";
		
		String queryOracle = "select * from \"" + NAME_ORACLE_MODEL + "\".\""+ tableOracle +"\"";
		String querySQL = "select * from \"" + NAME_SQL_MODEL + "\".\""+ tableSQL +"\"";
		
		new ModelExplorerManager().previewModelObject(new ArrayList<String>(),PROJECT_NAME, NAME_ORACLE_MODEL + ".xmi", tableOracle);
		waitUntilPreview();
		new ModelExplorerManager().previewModelObject(new ArrayList<String>(),PROJECT_NAME, NAME_SQL_MODEL + ".xmi", tableSQL);
		waitUntilPreview();
		
		assertTrue(checkPreviewCSV(queryOracle,PATH_PARTS_ORACLE_CSV));
		assertTrue(checkPreviewCSV(querySQL,PATH_PARTS_MSSQL_CSV));
		
		assertEquals(3,getCount(queryOracle));
		assertEquals(16,getCount(querySQL));
	}
	
	@Test
	public void previewDataViewModel(){

		String table = "viewTable";
		String queryViewTable = "select * from \"" + NAME_VIEW_MODEL + "\".\""+ table +"\"";
		new ModelExplorerManager().previewModelObject(new ArrayList<String>(),PROJECT_NAME, NAME_VIEW_MODEL + ".xmi", table);
		waitUntilPreview();

		table = "viewTableTwoSources";
		String queryViewTableTwoSources = "select * from \"" + NAME_VIEW_MODEL + "\".\""+ table +"\"";
		new ModelExplorerManager().previewModelObject(new ArrayList<String>(),PROJECT_NAME, NAME_VIEW_MODEL + ".xmi", table);
		waitUntilPreview();

		table = "infoByName";
		String queryViewProcedure = "select * from ( exec \"" + NAME_VIEW_MODEL + "\".\""+ table +"\"('Park') ) AS X_X";
		ArrayList<String> previewParam = new ArrayList<String>();
		previewParam.add("Park");
		new ModelExplorerManager().previewModelObject(previewParam,PROJECT_NAME, NAME_VIEW_MODEL + ".xmi", table);
		waitUntilPreview();
		
		table = "viewTableWithAccessPattern";
     	String queryAccessPattern = "select * from \"" + NAME_VIEW_MODEL + "\".\""+ table +
					"\" where \"" + NAME_VIEW_MODEL + "\".\""+ table + "\".\"shipper_name\" = 'DHL'";
		previewParam.clear();
		previewParam.add("DHL");
		new ModelExplorerManager().previewModelObject(previewParam,PROJECT_NAME, NAME_VIEW_MODEL + ".xmi", table);
		waitUntilPreview();
		
		assertTrue(checkPreviewCSV(queryViewTable,PATH_VIEW_TABLE_CSV));
		assertTrue(checkPreviewCSV(queryViewTableTwoSources,PATH_VIEW_TWO_TABLE_CSV));
		assertTrue(checkPreviewCSV(queryViewProcedure,PATH_VIEW_PROCEDURE_CSV));
		assertTrue(checkPreviewCSV(queryAccessPattern,PATH_VIEW_ACCESS_PATERN_TABLE_CSV));
		
		assertEquals(227,getCount(queryViewTable));
		assertEquals(227,getCount(queryViewTableTwoSources));
		assertEquals(15,getCount(queryViewProcedure));
		assertEquals(75,getCount(queryAccessPattern));
	}
	
	private boolean checkPreviewCSV(String previewSQL,String pathCSV){
		SQLResult result = DatabaseDevelopmentPerspective.getInstance().getSqlResultsView().getByOperation(previewSQL);	
		return result.compareCSVQueryResults(new File(pathCSV));
	}
	
	private int getCount(String previewSQL){
		SQLResult result = DatabaseDevelopmentPerspective.getInstance().getSqlResultsView().getByOperation(previewSQL);	
		return result.getCount();
	}
	/*
	 * Wait until preview is done
	 */
	private void waitUntilPreview(){
		new WaitWhile(new IsInProgress(), TimePeriod.LONG);
		new WaitWhile(new ShellWithTextIsActive(new RegexMatcher("Preview.*")), TimePeriod.LONG);
	}
}
