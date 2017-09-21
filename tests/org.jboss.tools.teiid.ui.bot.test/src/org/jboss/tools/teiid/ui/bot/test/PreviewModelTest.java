package org.jboss.tools.teiid.ui.bot.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.util.ArrayList;

import org.eclipse.reddeer.common.matcher.RegexMatcher;
import org.eclipse.reddeer.common.platform.RunningPlatform;
import org.eclipse.reddeer.common.util.Display;
import org.eclipse.reddeer.common.wait.AbstractWait;
import org.eclipse.reddeer.common.wait.TimePeriod;
import org.eclipse.reddeer.common.wait.WaitUntil;
import org.eclipse.reddeer.common.wait.WaitWhile;
import org.eclipse.reddeer.junit.requirement.inject.InjectRequirement;
import org.eclipse.reddeer.junit.runner.RedDeerSuite;
import org.eclipse.reddeer.requirements.openperspective.OpenPerspectiveRequirement.OpenPerspective;
import org.eclipse.reddeer.requirements.server.ServerRequirementState;
import org.eclipse.reddeer.swt.condition.ShellIsActive;
import org.eclipse.reddeer.swt.condition.ShellIsAvailable;
import org.eclipse.reddeer.swt.impl.menu.ContextMenuItem;
import org.eclipse.reddeer.swt.impl.styledtext.DefaultStyledText;
import org.eclipse.reddeer.swt.impl.tree.DefaultTreeItem;
import org.eclipse.reddeer.workbench.core.condition.JobIsKilled;
import org.eclipse.reddeer.workbench.core.condition.JobIsRunning;
import org.jboss.tools.teiid.reddeer.condition.IsInProgress;
import org.jboss.tools.teiid.reddeer.connection.ConnectionProfileConstants;
import org.jboss.tools.teiid.reddeer.connection.ResourceFileHelper;
import org.jboss.tools.teiid.reddeer.perspective.DatabaseDevelopmentPerspective;
import org.jboss.tools.teiid.reddeer.perspective.TeiidPerspective;
import org.jboss.tools.teiid.reddeer.requirement.TeiidServerRequirement;
import org.jboss.tools.teiid.reddeer.requirement.TeiidServerRequirement.TeiidServer;
import org.jboss.tools.teiid.reddeer.view.ModelExplorer;
import org.jboss.tools.teiid.reddeer.view.SQLResult;
import org.jboss.tools.teiid.reddeer.wizard.newWizard.VdbWizard;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * @author mkralik
 */

@RunWith(RedDeerSuite.class)
@OpenPerspective(TeiidPerspective.class)
@TeiidServer(state = ServerRequirementState.RUNNING, connectionProfiles={
		ConnectionProfileConstants.ORACLE_11G_PARTS_SUPPLIER,
		ConnectionProfileConstants.SQL_SERVER_2008_PARTS_SUPPLIER,
})
public class PreviewModelTest {

	private static final String PROJECT_NAME = "PreviewProject";
	private static final String NAME_ORACLE_MODEL = "partsSourceOracle";
	private static final String NAME_SQL_MODEL = "partsSourceSQLServer";
	private static final String NAME_VIEW_MODEL = "partsView";
	private static final String NAME_VDB = "previewVDB";
	
	private static final String PARTS_ORACLE_CSV = "oracleSource.csv";
	private static final String PARTS_MSSQL_CSV = "mssqlSource.csv";
	private static final String VIEW_TABLE_CSV = "viewTable.csv";
	private static final String VIEW_ORDERED_TABLE_CSV = "viewOrderedTable.csv"; 
	private static final String VIEW_TWO_TABLE_CSV = "viewTableTwoSources.csv";
	private static final String VIEW_TWO_TABLE_WIN_CSV = "viewTableTwoSourcesWindows.csv"; //windows sort result otherwise
	private static final String VIEW_PROCEDURE_CSV = "queryViewProcedure.csv";
	private static final String VIEW_ACCESS_PATERN_TABLE_CSV = "viewTableWithAccessPattern.csv";

	private static final String QUERY_VIEW_TABLE = new ResourceFileHelper().getSql("PreviewModelTest/viewTable").replaceAll("\r|\n|\\s", "");
	private static final String QUERY_VIEW_TABLE_TWO_SOURCES = new ResourceFileHelper().getSql("PreviewModelTest/viewTableTwoSources").replaceAll("\r|\n|\\s", "");
	private static final String QUERY_VIEW_PROCEDURE = new ResourceFileHelper().getSql("PreviewModelTest/viewProcedure").replaceAll("\r|\n|\\s", "");
	private static final String QUERY_ACCESS_PATTERN = new ResourceFileHelper().getSql("PreviewModelTest/accessPattern").replaceAll("\r|\n|\\s", "");
	
	@InjectRequirement
	private static TeiidServerRequirement teiidServer;
		
	@BeforeClass
	public static void before() {
		ModelExplorer explorer = new ModelExplorer();
		explorer.importProject(PROJECT_NAME);
		explorer.changeConnectionProfile(ConnectionProfileConstants.ORACLE_11G_PARTS_SUPPLIER, PROJECT_NAME, NAME_ORACLE_MODEL);
		explorer.changeConnectionProfile(ConnectionProfileConstants.SQL_SERVER_2008_PARTS_SUPPLIER, PROJECT_NAME, NAME_SQL_MODEL);
	}
	
	@Before
	public void openTeiidPerspective(){
		DatabaseDevelopmentPerspective.getInstance().getSqlResultsView().clear();
		new TeiidPerspective().open();
	}
	
	@Test
	public void previewDataSourceModel(){
		String tableOracle = "STATUS";
		String tableSQL = "SHIP_VIA";
		String queryOracle = "select * from \"" + NAME_ORACLE_MODEL + "\".\""+ tableOracle +"\"";
		String querySQL = "select * from \"" + NAME_SQL_MODEL + "\".\""+ tableSQL +"\"";
		
		new ModelExplorer().previewModelItem(new ArrayList<String>(),PROJECT_NAME, NAME_ORACLE_MODEL + ".xmi", tableOracle);
		waitUntilPreview();
		new ModelExplorer().previewModelItem(new ArrayList<String>(),PROJECT_NAME, NAME_SQL_MODEL + ".xmi", tableSQL);
		waitUntilPreview();
		
		assertTrue(checkPreviewCSV(queryOracle,PARTS_ORACLE_CSV));
		assertTrue(checkPreviewCSV(querySQL,PARTS_MSSQL_CSV));
		
		assertEquals(3,getCount(queryOracle));
		assertEquals(3,getCount(querySQL));
	}
	
	@Test
	public void previewDataViewModel(){

		String table = "viewTable";
		new ModelExplorer().previewModelItem(new ArrayList<String>(),PROJECT_NAME, NAME_VIEW_MODEL + ".xmi", table);
		waitUntilPreview();

		table = "viewTableTwoSources";
		new ModelExplorer().previewModelItem(new ArrayList<String>(),PROJECT_NAME, NAME_VIEW_MODEL + ".xmi", table);
		waitUntilPreview();

		table = "infoByName";
		ArrayList<String> previewParam = new ArrayList<String>();
		previewParam.add("Park");
		new ModelExplorer().previewModelItem(previewParam,PROJECT_NAME, NAME_VIEW_MODEL + ".xmi", table);
		waitUntilPreview();
		
		table = "viewTableWithAccessPattern";
		previewParam.clear();
		previewParam.add("DHL");
		new ModelExplorer().previewModelItem(previewParam,PROJECT_NAME, NAME_VIEW_MODEL + ".xmi", table);
		waitUntilPreview();
		
		assertTrue(checkPreviewCSV(QUERY_VIEW_TABLE,VIEW_TABLE_CSV));
		if(RunningPlatform.isWindows()){ //windows sort result otherwise
			assertTrue(checkPreviewCSV(QUERY_VIEW_TABLE_TWO_SOURCES,VIEW_TWO_TABLE_WIN_CSV));
		}else{
			assertTrue(checkPreviewCSV(QUERY_VIEW_TABLE_TWO_SOURCES,VIEW_TWO_TABLE_CSV));
		}
		assertTrue(checkPreviewCSV(QUERY_VIEW_PROCEDURE,VIEW_PROCEDURE_CSV));
		assertTrue(checkPreviewCSV(QUERY_ACCESS_PATTERN,VIEW_ACCESS_PATERN_TABLE_CSV));
		
		assertEquals(227,getCount(QUERY_VIEW_TABLE));
		assertEquals(227,getCount(QUERY_VIEW_TABLE_TWO_SOURCES));
		assertEquals(15,getCount(QUERY_VIEW_PROCEDURE));
		assertEquals(75,getCount(QUERY_ACCESS_PATTERN));
	}
	
	@Test
	/* Test if execute vdb and SQL scrapbook work correctly */
	public void executeVDB(){
		new ModelExplorer().getProject(PROJECT_NAME).refresh();
		VdbWizard.openVdbWizard()
				.setName(NAME_VDB)
				.addModel(PROJECT_NAME,NAME_VIEW_MODEL)
				.finish();
		
		new ModelExplorer().open();
		new DefaultTreeItem(PROJECT_NAME, NAME_VDB+".vdb").select();
 		new ContextMenuItem("Modeling", "Execute VDB").select();
 		new WaitWhile(new IsInProgress(), TimePeriod.VERY_LONG);
 		new WaitWhile(new JobIsRunning(), TimePeriod.LONG);
 		
 		setTextToScrapbookAndExecute(QUERY_VIEW_TABLE);
 		setTextToScrapbookAndExecute(QUERY_VIEW_TABLE_TWO_SOURCES + " order by PART_ID");
 		setTextToScrapbookAndExecute(QUERY_VIEW_PROCEDURE);
 		setTextToScrapbookAndExecute(QUERY_ACCESS_PATTERN);
 		
 		assertTrue(checkPreviewCSV(QUERY_VIEW_TABLE,VIEW_TABLE_CSV));
		assertTrue(checkPreviewCSV(QUERY_VIEW_TABLE_TWO_SOURCES + " order by PART_ID",VIEW_TWO_TABLE_CSV));
		assertTrue(checkPreviewCSV(QUERY_VIEW_PROCEDURE,VIEW_PROCEDURE_CSV));
		assertTrue(checkPreviewCSV(QUERY_ACCESS_PATTERN,VIEW_ACCESS_PATERN_TABLE_CSV));
		
		assertEquals(227,getCount(QUERY_VIEW_TABLE));
		assertEquals(227,getCount(QUERY_VIEW_TABLE_TWO_SOURCES + " order by PART_ID"));
		assertEquals(15,getCount(QUERY_VIEW_PROCEDURE));
		assertEquals(75,getCount(QUERY_ACCESS_PATTERN));
	}
	
	@Test
	/* Test preview which contains custom sql */
	public void customPreview(){
		String table = "viewTable";
		String sql = QUERY_VIEW_TABLE + " order by SUPPLIER_NAME";
		new ModelExplorer().customPreviewModelItem(new ArrayList<String>(),sql,PROJECT_NAME, NAME_VIEW_MODEL + ".xmi", table);
		waitUntilPreview();
		
 		assertTrue(checkPreviewCSV(sql,VIEW_ORDERED_TABLE_CSV));
		assertEquals(227,getCount(sql));
	}
	
	private boolean checkPreviewCSV(String previewSQL,String csv){
		SQLResult result = DatabaseDevelopmentPerspective.getInstance().getSqlResultsView().getByOperation(previewSQL);	
		return result.compareCSVQueryResults(new File("resources/preview/"+csv));
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
		new WaitWhile(new ShellIsActive(new RegexMatcher("Preview.*")), TimePeriod.LONG);
	}
	
	/**
	 * Set text to the scrapbook
	 * @param text
	 */
	private void setTextToScrapbookAndExecute(String text){
		DefaultStyledText styledText = new DefaultStyledText();
		String removedText = styledText.getText();
		styledText.selectText(removedText);
		styledText.insertText(text);
		new WaitUntil(new JobIsKilled("Refreshing server adapter list"), TimePeriod.DEFAULT, false);
		
 		Display.syncExec(new Runnable() {
			@Override
			public void run() {
				new DefaultStyledText().getSWTWidget().setFocus(); //because context menu would lose focus
		 		new ContextMenuItem("Execute All").select();
			}
		});
 		
		AbstractWait.sleep(TimePeriod.SHORT); // Shell hasn't already opened after execute
 		new WaitWhile(new ShellIsAvailable("SQL Statement Execution"), TimePeriod.LONG);
	}
}
