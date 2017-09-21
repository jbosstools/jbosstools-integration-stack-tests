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
public class ViewPrimaryKey {
	@InjectRequirement
	private static TeiidServerRequirement teiidServer;
	
	@Rule
	public ErrorCollector collector = new ErrorCollector();
	public DdlHelper ddlHelper = null;
	
	private static final String PROJECT_NAME = "ViewPrimaryKey";
	private static final String NAME_VIEW_MODEL = "ViewModelPrimaryKey";
	private static final String NAME_VDB = "ViewModelPrimaryKeyVDB";
	private static final String NAME_ORIGINAL_DYNAMIC_VDB = NAME_VDB + "-vdb.xml";
	
	private static final String NAME_GENERATED_DYNAMIC_VDB = "ViewModelPrimaryKeyVDBgenerated-vdb.xml";
	
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
	    		
		tableEditor.openTab(TableEditor.Tabs.PRIMARY_KEYS);

    	collector.checkThat("Primary key name in source is badly set", tableEditor.getCellText(1,"PrimaryKey", "Name In Source"),
    			is("PrimaryKeySource"));
	    
	    collector.checkThat("Columns of Primary key is badly set", tableEditor.getCellText(1,"PrimaryKey", "Columns"),
	    		is("Column1 : bigdecimal(1)")); 
	    
    	collector.checkThat("Description of Primary key is badly set", tableEditor.getCellText(1,"PrimaryKey", "Description"),
    			is("PrimaryKey description")); 
	    
		
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
		collector.checkThat("Wrong set primary key", contentFile,
				new StringContains("CONSTRAINT PrimaryKey PRIMARY KEY(Column1)"));
		collector.checkThat("wrong set description", contentFile, new StringContains("ANNOTATION 'PrimaryKey description'"));
		collector.checkThat("wrong set name in source", contentFile, new StringContains("NAMEINSOURCE 'PrimaryKeySource'"));
	}
}

