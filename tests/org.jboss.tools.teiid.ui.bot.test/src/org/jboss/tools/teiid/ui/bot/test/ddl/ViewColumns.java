package org.jboss.tools.teiid.ui.bot.test.ddl;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.is;

import org.eclipse.reddeer.eclipse.ui.markers.matcher.MarkerResourceMatcher;
import org.eclipse.reddeer.eclipse.ui.views.markers.ProblemsView;
import org.eclipse.reddeer.eclipse.ui.views.markers.ProblemsView.ProblemType;
import org.eclipse.reddeer.junit.requirement.inject.InjectRequirement;
import org.eclipse.reddeer.junit.runner.RedDeerSuite;
import org.eclipse.reddeer.requirements.openperspective.OpenPerspectiveRequirement.OpenPerspective;
import org.eclipse.reddeer.requirements.server.ServerRequirementState;
import org.hamcrest.core.StringContains;
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

@TeiidServer(state = ServerRequirementState.RUNNING, connectionProfiles={
		ConnectionProfileConstants.SQL_SERVER_2008_PARTS_SUPPLIER,
})
public class ViewColumns {
	@InjectRequirement
	private static TeiidServerRequirement teiidServer;
	
	@Rule
	public ErrorCollector collector = new ErrorCollector();
	public DdlHelper ddlHelper = null;
	
	private static final String PROJECT_NAME = "ViewColumns";
	private static final String NAME_SOURCE_MODEL = "sourceM";
	private static final String NAME_VIEW_MODEL = "ViewModelColumns";
	private static final String NAME_VDB = "ViewModelColumnsVDB";
	private static final String NAME_ORIGINAL_DYNAMIC_VDB = NAME_VDB + "-vdb.xml";
	
	private static final String NAME_GENERATED_DYNAMIC_VDB = "ViewModelColumnsVDBgenerated-vdb.xml";
	
	private static final String WORK_PROJECT_NAME = "workProject" ;
	
	@Before
	public void before() {
		ModelExplorer explorer = new ModelExplorer();
		explorer.deleteAllProjectsSafely();

		ddlHelper = new DdlHelper(collector);
		
		explorer.importProject("DDLtests/"+PROJECT_NAME);
		explorer.changeConnectionProfile(ConnectionProfileConstants.SQL_SERVER_2008_PARTS_SUPPLIER, PROJECT_NAME, NAME_SOURCE_MODEL);
		/* data source is needed when exported VDB will be tested to deploy on the server */
		explorer.createDataSource("Use Connection Profile Info",ConnectionProfileConstants.SQL_SERVER_2008_PARTS_SUPPLIER, PROJECT_NAME, NAME_SOURCE_MODEL);
		explorer.createProject(WORK_PROJECT_NAME);
	}
	
	@After
	public void after(){
		new ModelExplorer().deleteAllProjectsSafely();
	}
	
	@Test
	public void importDdlTest(){
		//Procedure need source model
		ddlHelper.importDdlFromSource(PROJECT_NAME, NAME_SOURCE_MODEL, WORK_PROJECT_NAME);
		ddlHelper.importDdlFromView(PROJECT_NAME, NAME_VIEW_MODEL, WORK_PROJECT_NAME);		
		checkImportedModel();
	}

	@Test
	public void importVdbTest(){
		ddlHelper.importVdb(PROJECT_NAME, NAME_ORIGINAL_DYNAMIC_VDB, WORK_PROJECT_NAME);		
		checkImportedModel();
		ddlHelper.checkDeploy(NAME_SOURCE_MODEL, NAME_VIEW_MODEL, WORK_PROJECT_NAME, NAME_VDB, teiidServer);		
	}
	
	
	private void checkImportedModel(){				
		RelationalModelEditor editor = new RelationalModelEditor(NAME_SOURCE_MODEL + ".xmi");
		editor.close();
		new ModelExplorer().openModelEditor(WORK_PROJECT_NAME, NAME_VIEW_MODEL + ".xmi");	
		editor = new RelationalModelEditor(NAME_VIEW_MODEL + ".xmi");
	    TableEditor tableEditor = editor.openTableEditor();
	    		
		tableEditor.openTab(TableEditor.Tabs.COLUMNS);
		// check tColumn1
		collector.checkThat("tColumn1 name in source is badly set", tableEditor.getCellText(1,"tColumn1", "Name In Source"),
		    	is("tColumn1Source"));
		collector.checkThat("Native type for tColumn1 is badly set", tableEditor.getCellText(1,"tColumn1", "Native Type"),
		    	is("tColumn1NT"));
		collector.checkThat("Nullable of tColumn1 is badly set", tableEditor.getCellText(1,"tColumn1", "Nullable"),
	    		is("NO_NULLS"));
	    collector.checkThat("Datatype of tColumn1 is badly set", tableEditor.getCellText(1,"tColumn1", "Datatype"),
	    		is("biginteger : xs:decimal"));
	    collector.checkThat("tColumn1 description is badly set", tableEditor.getCellText(1,"tColumn1", "Description"),
	    		is("tColumn1 description"));
	    
	    // check tColumn2
 		collector.checkThat("tColumn2 name in source is badly set", tableEditor.getCellText(1,"tColumn2", "Name In Source"),
 		    	is("tColumn2Source"));
 		collector.checkThat("Native type for tColumn2 is badly set", tableEditor.getCellText(1,"tColumn2", "Native Type"),
 		    	is("tColumn2NT"));
 		collector.checkThat("Nullable of tColumn2 is badly set", tableEditor.getCellText(1,"tColumn2", "Nullable"),
 	    		is("NULLABLE"));
 		collector.checkThat("Length of tColumn2 is badly set", tableEditor.getCellText(1,"tColumn2", "Length"),
 	    		is("123"));
 	    collector.checkThat("Datatype of tColumn2 is badly set", tableEditor.getCellText(1,"tColumn2", "Datatype"),
 	    		is("string"));
 	    collector.checkThat("tColumn2 description is badly set", tableEditor.getCellText(1,"tColumn2", "Description"),
 	    		is("tColumn2 description"));
 	    
 	    // check tColumn3
 		collector.checkThat("tColumn3 name in source is badly set", tableEditor.getCellText(1,"tColumn3", "Name In Source"),
 		    	is("tColumn3Source"));
 		collector.checkThat("Native type for tColumn3 is badly set", tableEditor.getCellText(1,"tColumn3", "Native Type"),
 		    	is("tColumn3NT"));
 		collector.checkThat("Nullable of tColumn3 is badly set", tableEditor.getCellText(1,"tColumn3", "Nullable"),
 	    		is("NULLABLE"));
 	    collector.checkThat("Datatype of tColumn3 is badly set", tableEditor.getCellText(1,"tColumn3", "Datatype"),
 	    		is("short : xs:int"));
 	    collector.checkThat("tColumn3 description is badly set", tableEditor.getCellText(1,"tColumn3", "Description"),
 	    		is("tColumn3 description")); 
 	    
 	    // check pColumn1
		collector.checkThat("pColumn1 name in source is badly set", tableEditor.getCellText(1,"pColumn1", "Name In Source"),
		    	is("pColumn1Source"));
		collector.checkThat("Native type for pColumn1 is badly set", tableEditor.getCellText(1,"pColumn1", "Native Type"),
		    	is("pColumn1NT"));
		collector.checkThat("Nullable of pColumn1 is badly set", tableEditor.getCellText(1,"pColumn1", "Nullable"),
	    		is("NULLABLE"));
 		collector.checkThat("Length of pColumn1 is badly set", tableEditor.getCellText(1,"pColumn1", "Length"),
 	    		is("1234"));
	    collector.checkThat("Datatype of pColumn1 is badly set", tableEditor.getCellText(1,"pColumn1", "Datatype"),
	    		is("string"));
	    collector.checkThat("pColumn1 description is badly set", tableEditor.getCellText(1,"pColumn1", "Description"),
	    		is("pColumn1 description"));
	    
	    // check pColumn2
		collector.checkThat("pColumn2 name in source is badly set", tableEditor.getCellText(1,"pColumn2", "Name In Source"),
		    	is("pColumn2Source"));
		collector.checkThat("Native type for pColumn2 is badly set", tableEditor.getCellText(1,"pColumn2", "Native Type"),
		    	is("pColumn2NT"));
		collector.checkThat("Nullable of pColumn2 is badly set", tableEditor.getCellText(1,"pColumn2", "Nullable"),
	    		is("NO_NULLS"));
	    collector.checkThat("Datatype of pColumn2 is badly set", tableEditor.getCellText(1,"pColumn2", "Datatype"),
	    		is("biginteger : xs:decimal"));
	    collector.checkThat("pColumn2 description is badly set", tableEditor.getCellText(1,"pColumn2", "Description"),
	    		is("pColumn2 description"));
		
		ProblemsView problemsView = new ProblemsView();
		collector.checkThat("Errors in imported source model",
				problemsView.getProblems(ProblemType.ERROR, new MarkerResourceMatcher(NAME_SOURCE_MODEL + ".xmi")),
				empty());
		collector.checkThat("Errors in imported view model",
				problemsView.getProblems(ProblemType.ERROR, new MarkerResourceMatcher(NAME_VIEW_MODEL + ".xmi")),
				empty());
		collector.checkThat("Errors in imported VDB",
				problemsView.getProblems(ProblemType.ERROR, new MarkerResourceMatcher(NAME_VDB + ".vdb")),
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
		collector.checkThat("tColumn1 is badly set", contentFile,
				new StringContains("tColumn1 biginteger(4000) NOT NULL OPTIONS(NAMEINSOURCE 'tColumn1Source', NATIVE_TYPE 'tColumn1NT', ANNOTATION 'tColumn1 description')"));
		collector.checkThat("tColumn2 is badly set", contentFile,
				new StringContains("tColumn2 string(123) OPTIONS(NAMEINSOURCE 'tColumn2Source', NATIVE_TYPE 'tColumn2NT', ANNOTATION 'tColumn2 description')"));
		collector.checkThat("tColumn3 is badly set", contentFile,
				new StringContains("tColumn3 short OPTIONS(NAMEINSOURCE 'tColumn3Source', NATIVE_TYPE 'tColumn3NT', ANNOTATION 'tColumn3 description')"));
		collector.checkThat("pColumn1 is badly set", contentFile, 
				new StringContains("pColumn1 string(1234) OPTIONS(NAMEINSOURCE 'pColumn1Source', NATIVE_TYPE 'pColumn1NT', ANNOTATION 'pColumn1 description')"));
		collector.checkThat("pColumn2 is badly set", contentFile, 
				new StringContains("pColumn2 biginteger(4000) NOT NULL OPTIONS(NAMEINSOURCE 'pColumn2Source', NATIVE_TYPE 'pColumn2NT', ANNOTATION 'pColumn2 description')"));				
	}
}

