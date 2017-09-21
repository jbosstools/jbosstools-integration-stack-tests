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

@TeiidServer(state = ServerRequirementState.RUNNING, connectionProfiles={
		ConnectionProfileConstants.SQL_SERVER_2008_PARTS_SUPPLIER,
})
public class SourceProcedureSettings {
	@InjectRequirement
	private static TeiidServerRequirement teiidServer;
	
	@Rule
	public ErrorCollector collector = new ErrorCollector();
	public DdlHelper ddlHelper = null;
	
	private static final String PROJECT_NAME = "SourceProcedureSettings";
	private static final String NAME_SOURCE_MODEL = "SourceProcedureSettings";
	private static final String NAME_VDB = "SourceProcedureSettingsVDB";
	private static final String NAME_ORIGINAL_DYNAMIC_VDB = NAME_VDB + "-vdb.xml";
	
	private static final String NAME_GENERATED_DYNAMIC_VDB = "SourceProcedureSettingsVDBgenerated-vdb.xml";
	
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
		ddlHelper.importDdlFromSource(PROJECT_NAME, NAME_SOURCE_MODEL, WORK_PROJECT_NAME);		
		checkImportedModel();
	}

	@Test
	public void importVdbTest(){
		ddlHelper.importVdb(PROJECT_NAME, NAME_ORIGINAL_DYNAMIC_VDB, WORK_PROJECT_NAME);		
		checkImportedModel();
		ddlHelper.checkDeploy(NAME_SOURCE_MODEL, null, WORK_PROJECT_NAME, NAME_VDB, teiidServer);		
	}
	
	
	private void checkImportedModel(){
		RelationalModelEditor editor = new RelationalModelEditor(NAME_SOURCE_MODEL + ".xmi");
		editor.close();
		new ModelExplorer().openModelEditor(WORK_PROJECT_NAME, NAME_SOURCE_MODEL + ".xmi");
		editor = new RelationalModelEditor(NAME_SOURCE_MODEL + ".xmi");
	    TableEditor tableEditor = editor.openTableEditor();
		tableEditor.openTab(TableEditor.Tabs.PROCEDURES);
	    
	    collector.checkThat("Procedure name in source is badly set", tableEditor.getCellText(1,"myProcedure", "Name In Source"),
	    		is("myProcedureSource"));
	    if (new JiraClient().isIssueClosed("TEIIDDES-2985")){
	    	collector.checkThat("Update count is badly set",tableEditor.getCellText(1,"myProcedure", "Update Count"),
	    			is("ONE"));
	    }
	    collector.checkThat("Table description is missing", tableEditor.getCellText(1,"myProcedure", "Description"),
	    		is("procedure description"));

		tableEditor.openTab(TableEditor.Tabs.PROCEDURE_RESULTS);
		collector.checkThat("Procedure result is set wrongly", tableEditor.getCellText(0,"myProcedure", "Name"),
	    		is("myProcedure"));
	    
		tableEditor.openTab(TableEditor.Tabs.PROCEDURE_PARAMETERS);
		collector.checkThat("Procedure parameter direction is set wrongly", tableEditor.getCellText(0,"myProcedure", "Direction"),
	    		is("IN"));
		collector.checkThat("Procedure parameter datatype is set wrongly", tableEditor.getCellText(0,"myProcedure", "Datatype"),
	    		is("string"));
		collector.checkThat("Procedure parameter length is set wrongly", tableEditor.getCellText(0,"myProcedure", "Length"),
	    		is("4000"));
		
		ProblemsView problemsView = new ProblemsView();
		collector.checkThat("Errors in imported source model",
				problemsView.getProblems(ProblemType.ERROR, new MarkerResourceMatcher(NAME_SOURCE_MODEL + ".xmi")),
				empty());
		collector.checkThat("Errors in imported VDB",
				problemsView.getProblems(ProblemType.ERROR, new MarkerResourceMatcher(NAME_VDB + ".vdb")),
				empty());		
	}
	
	@Test
	public void exportVdbTest(){
		ddlHelper.createStaticVdb(NAME_VDB, PROJECT_NAME, NAME_SOURCE_MODEL);		
		String contentFile = ddlHelper.createDynamicVdb(PROJECT_NAME, NAME_VDB, NAME_GENERATED_DYNAMIC_VDB);
		checkExportedFile(contentFile);

		/*test deploy generated dynamic VDB from static VDB*/
		String status = ddlHelper.deploy(PROJECT_NAME, NAME_GENERATED_DYNAMIC_VDB, teiidServer);
		collector.checkThat("vdb is not active", status, containsString("ACTIVE"));
	}
	
	@Test
	public void exportDdlTest(){
		String ddlContent = ddlHelper.exportDDL(PROJECT_NAME, NAME_SOURCE_MODEL, WORK_PROJECT_NAME);
		checkExportedFile(ddlContent);
	}
	
	private void checkExportedFile(String contentFile){
		collector.checkThat("missing CREATE FOREIGN PROCEDURE", contentFile, new StringContains("CREATE FOREIGN PROCEDURE"));
		collector.checkThat("missing RESULT", contentFile.replaceAll("\\t|\\n|\\r", ""),
				new StringContains("RETURNSTABLE (newColumn_1 string(4000),newColumn_2 string(4000))"));
		collector.checkThat("missing IN newParameter_1 string(4000)", contentFile, new StringContains("IN newParameter_1 string(4000)"));
		collector.checkThat("missing UPDATECOUNT 'myProcedureSource'", contentFile, new StringContains("UPDATECOUNT '1'"));
		collector.checkThat("missing ANNOTATION 'Procedure description'", contentFile, new StringContains("ANNOTATION 'procedure description'"));
		collector.checkThat("missing NAMEINSOURCE 'myProcedureSource'", contentFile, new StringContains("NAMEINSOURCE 'myProcedureSource'"));		
	}
}
