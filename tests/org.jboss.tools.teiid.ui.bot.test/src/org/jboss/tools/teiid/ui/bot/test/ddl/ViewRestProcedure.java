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
		ConnectionProfileConstants.ORACLE_11G_PARTS_SUPPLIER,
})
public class ViewRestProcedure {
	@InjectRequirement
	private static TeiidServerRequirement teiidServer;
	
	@Rule
	public ErrorCollector collector = new ErrorCollector();
	public DdlHelper ddlHelper = null;
	
	private static final String PROJECT_NAME = "ViewRestProcedure";
	private static final String NAME_SOURCE_MODEL = "viewRestProcedureSource";
	private static final String NAME_VIEW_MODEL = "viewRestProcedureView";
	private static final String NAME_VDB = "viewRestProcedureVDB";
	private static final String NAME_ORIGINAL_DYNAMIC_VDB = NAME_VDB + "-vdb.xml";
	
	private static final String NAME_GENERATED_DYNAMIC_VDB = "ViewProcedureSettingsVDBgenerated-vdb.xml";
	
	private static final String WORK_PROJECT_NAME = "workProject" ;
	
	@Before
	public void before() {
		ModelExplorer explorer = new ModelExplorer();
		explorer.deleteAllProjectsSafely();

		ddlHelper = new DdlHelper(collector);
		
		explorer.importProject("DDLtests/"+PROJECT_NAME);
		explorer.changeConnectionProfile(ConnectionProfileConstants.ORACLE_11G_PARTS_SUPPLIER, PROJECT_NAME, NAME_SOURCE_MODEL);
		/* data source is needed when exported VDB will be tested to deploy on the server */
		explorer.createDataSource("Use Connection Profile Info",ConnectionProfileConstants.ORACLE_11G_PARTS_SUPPLIER, PROJECT_NAME, NAME_SOURCE_MODEL);
		explorer.createProject(WORK_PROJECT_NAME);
	}
	
	@After
	public void after(){
		new ModelExplorer().deleteAllProjectsSafely();
	}
	
	@Test
	public void importDdlTest(){
		if (new JiraClient().isIssueClosed("TEIIDTOOLS-30")){
			ddlHelper.importDdlFromView(PROJECT_NAME, NAME_VIEW_MODEL, WORK_PROJECT_NAME);			
			checkImportedModel();
		}
	}

	@Test
	public void importVdbTest(){
		if (new JiraClient().isIssueClosed("TEIIDTOOLS-30")){
			ddlHelper.importVdb(PROJECT_NAME, NAME_ORIGINAL_DYNAMIC_VDB, WORK_PROJECT_NAME);			
			checkImportedModel();
			ddlHelper.checkDeploy(NAME_SOURCE_MODEL, null, WORK_PROJECT_NAME, NAME_VDB, teiidServer);			
		}
	}
	
	private void checkImportedModel(){
		RelationalModelEditor editor = new RelationalModelEditor(NAME_SOURCE_MODEL + ".xmi");
		editor.close();
		new ModelExplorer().openModelEditor(WORK_PROJECT_NAME, NAME_VIEW_MODEL + ".xmi");
		editor = new RelationalModelEditor(NAME_VIEW_MODEL + ".xmi");
	    TableEditor tableEditor = editor.openTableEditor();
		tableEditor.openTab(TableEditor.Tabs.PROCEDURES);
	    
	    collector.checkThat("Procedure method is not set to get", tableEditor.getCellText(1,"getProcedure", "REST:Rest Method"),
	    		is("GET"));
	    collector.checkThat("Get procedure URI is badly set",tableEditor.getCellText(1,"myProcedure", "REST:URI"),
	    		is("product/{instr_id}"));
	    
	    collector.checkThat("Procedure method is not set to post", tableEditor.getCellText(1,"addProduct", "REST:Rest Method"),
	    		is("POST"));
	    collector.checkThat("Post procedure URI is badly set",tableEditor.getCellText(1,"getProduct", "REST:URI"),
	    		is("product/"));
		
		ProblemsView problemsView = new ProblemsView();
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
		collector.checkThat("missing REST:URI in options in getProduct", contentFile, new StringContains("\"REST:URI\" 'product/{instr_id}'"));
		collector.checkThat("missing REST:METHOD in options in getProduct", contentFile, new StringContains("\"REST:METHOD\" 'GET'"));
		collector.checkThat("missing REST:URI in options in addProduct", contentFile, new StringContains("\"REST:URI\" 'product/'"));
		collector.checkThat("missing REST:METHOD in options in addProduct", contentFile, new StringContains("\"REST:METHOD\" 'POST'"));	
	}
}
