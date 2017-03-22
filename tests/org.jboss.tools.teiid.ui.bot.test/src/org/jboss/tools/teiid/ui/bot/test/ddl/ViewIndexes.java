package org.jboss.tools.teiid.ui.bot.test.ddl;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.is;

import org.hamcrest.core.StringContains;
import org.jboss.reddeer.eclipse.ui.problems.ProblemsView;
import org.jboss.reddeer.eclipse.ui.problems.ProblemsView.ProblemType;
import org.jboss.reddeer.eclipse.ui.problems.matcher.ProblemsResourceMatcher;
import org.jboss.reddeer.junit.requirement.inject.InjectRequirement;
import org.jboss.reddeer.junit.runner.RedDeerSuite;
import org.jboss.reddeer.requirements.openperspective.OpenPerspectiveRequirement.OpenPerspective;
import org.jboss.reddeer.requirements.server.ServerReqState;
import org.jboss.tools.common.reddeer.JiraClient;
import org.jboss.tools.teiid.reddeer.DdlHelper;
import org.jboss.tools.teiid.reddeer.connection.ConnectionProfileConstants;
import org.jboss.tools.teiid.reddeer.editor.RelationalModelEditor;
import org.jboss.tools.teiid.reddeer.editor.TableEditor;
import org.jboss.tools.teiid.reddeer.perspective.TeiidPerspective;
import org.jboss.tools.teiid.reddeer.requirement.TeiidServerRequirement;
import org.jboss.tools.teiid.reddeer.requirement.TeiidServerRequirement.TeiidServer;
import org.jboss.tools.teiid.reddeer.view.ModelExplorer;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ErrorCollector;
import org.junit.runner.RunWith;

@RunWith(RedDeerSuite.class)
@OpenPerspective(TeiidPerspective.class)

@TeiidServer(state = ServerReqState.RUNNING, connectionProfiles={
		ConnectionProfileConstants.SQL_SERVER_2008_PARTS_SUPPLIER,
})
public class ViewIndexes {
	@InjectRequirement
	private static TeiidServerRequirement teiidServer;
	
	@Rule
	public ErrorCollector collector = new ErrorCollector();
	public DdlHelper ddlHelper = null;
	
	private static final String PROJECT_NAME = "ViewIndexes";
	private static final String NAME_VIEW_MODEL = "ViewIndexes";
	private static final String NAME_VDB = "ViewIndexesVDB";
	private static final String NAME_ORIGINAL_DYNAMIC_VDB = NAME_VDB + "-vdb.xml";
	
	private static final String NAME_GENERATED_DYNAMIC_VDB = "ViewIndexesVDBgenerated-vdb.xml";
	
	private static final String WORK_PROJECT_NAME = "workProject" ;
	
	@Before
	public void before() {
		ModelExplorer explorer = new ModelExplorer();
		explorer.deleteAllProjectsSafely();

		ddlHelper = new DdlHelper(collector);
		
		explorer.importProject("DDLtests/"+PROJECT_NAME);
		explorer.createProject(WORK_PROJECT_NAME);
	}
	
	@After
	public void after(){
		new ModelExplorer().deleteAllProjectsSafely();
	}
	
	@Test
	public void importDdlTest(){
		ddlHelper.importDdlFromView(PROJECT_NAME, NAME_VIEW_MODEL, WORK_PROJECT_NAME);
		checkImportedModel();
	}

	@Test
	public void importVdbTest(){
		ddlHelper.importVdb(PROJECT_NAME, NAME_ORIGINAL_DYNAMIC_VDB, WORK_PROJECT_NAME);		
		checkImportedModel();
		ddlHelper.checkDeploy(null, NAME_VIEW_MODEL, WORK_PROJECT_NAME, NAME_VDB, teiidServer);		
	}
	
	
	private void checkImportedModel(){
		new ModelExplorer().openModelEditor(WORK_PROJECT_NAME, NAME_VIEW_MODEL + ".xmi");	
		RelationalModelEditor editor = new RelationalModelEditor(NAME_VIEW_MODEL + ".xmi");
		TableEditor tableEditor = editor.openTableEditor();
				
		tableEditor.openTab(TableEditor.Tabs.INDEXES);
		
		if (new JiraClient().isIssueClosed("TEIIDDES-3024")){
			collector.checkThat("Index1 name in source is badly set", tableEditor.getCellText(1,"Index1", "Name In Source"),
					is("Index1Source"));
			collector.checkThat("Index2 name in source is badly set", tableEditor.getCellText(1,"Index2", "Name In Source"),
					is("Index2Source"));
			
			collector.checkThat("Index1 Nullable checkbox is badly set", tableEditor.getCellText(1,"Index1", "Nullable"),
		    		is("true")); 
			collector.checkThat("Index2 Nullable checkbox is badly set", tableEditor.getCellText(1,"Index2", "Nullable"),
		    		is("true")); 
			
			collector.checkThat("Index1 AutoUpdate checkbox is badly set", tableEditor.getCellText(1,"Index1", "Auto Update"),
		    		is("true")); 
			collector.checkThat("Index2 AutoUpdate checkbox is badly set", tableEditor.getCellText(1,"Index2", "Auto Update"),
		    		is("true")); 
			
			collector.checkThat("Index1 Unique checkbox is badly set", tableEditor.getCellText(1,"Index1", "Unique"),
		    		is("true")); 
			collector.checkThat("Index2 Unique checkbox is badly set", tableEditor.getCellText(1,"Index2", "Unique"),
		    		is("true")); 
		}
		collector.checkThat("Column of Foreign key 1 is badly set", tableEditor.getCellText(1,"Index1", "Columns"),
				is("column1 : string(4000)")); 
		collector.checkThat("Column of Foreign key 2 is badly set", tableEditor.getCellText(1,"Index2", "Columns"),
				is("column2 : string(4000)")); 
		
		collector.checkThat("Index1 description is badly set", tableEditor.getCellText(1,"Index1", "Description"),
				is("Index 1 description")); 
		collector.checkThat("Index2 description is badly set", tableEditor.getCellText(1,"Index2", "Description"),
				is("Index 2 description")); 
		
		
		
		ProblemsView problemsView = new ProblemsView();
		collector.checkThat("Errors in imported view model",
				problemsView.getProblems(ProblemType.ERROR, new ProblemsResourceMatcher(NAME_VIEW_MODEL + ".xmi")),
				empty());
		collector.checkThat("Errors in imported VDB",
				problemsView.getProblems(ProblemType.ERROR, new ProblemsResourceMatcher(NAME_VDB + ".vdb")),
				empty());		
	}
	
	@Test
	public void exportVdbTest(){
		ddlHelper.createStaticVdb(NAME_VDB, PROJECT_NAME, NAME_VIEW_MODEL);		
		String contentFile = ddlHelper.createDynamicVdb(PROJECT_NAME, NAME_VDB, NAME_GENERATED_DYNAMIC_VDB);
		checkExportedFile(contentFile);

		/*test deploy generated dynamic VDB from static VDB*/
		String status = ddlHelper.deploy(PROJECT_NAME, NAME_GENERATED_DYNAMIC_VDB, teiidServer);
		collector.checkThat("vdb is not active", status, containsString("ACTIVE"));
	}
	
	@Test
	public void exportDdlTest(){
		String ddlContent = ddlHelper.exportDDL(PROJECT_NAME, NAME_VIEW_MODEL, WORK_PROJECT_NAME);
		checkExportedFile(ddlContent);
	}
	
	private void checkExportedFile(String contentFile){
		collector.checkThat("Wrong set index 1", contentFile,
				new StringContains("CONSTRAINT Index1 INDEX(column1) OPTIONS(ANNOTATION 'Index 1 description')"));
		collector.checkThat("Wrong set Index 2", contentFile,
				new StringContains("CONSTRAINT Index2 INDEX(column2) OPTIONS(ANNOTATION 'Index 2 description')"));
		
	}
}
