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
public class ViewForeignKey {
	@InjectRequirement
	private static TeiidServerRequirement teiidServer;
	
	@Rule
	public ErrorCollector collector = new ErrorCollector();
	public DdlHelper ddlHelper = null;
	
	private static final String PROJECT_NAME = "ViewForeignKey";
	private static final String NAME_VIEW_MODEL = "ViewForeignKey";
	private static final String NAME_VDB = "ViewForeignKeyVDB";
	private static final String NAME_ORIGINAL_DYNAMIC_VDB = NAME_VDB + "-vdb.xml";
	
	private static final String NAME_GENERATED_DYNAMIC_VDB = "ViewForeignKeyVDBgenerated-vdb.xml";
	
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
				
		tableEditor.openTab(TableEditor.Tabs.FOREIGN_KEYS);
		
		collector.checkThat("Foreign key 1 name in source is badly set", tableEditor.getCellText(1,"ForeignKey1", "Name In Source"),
				is("ForeignKey1Source"));
		collector.checkThat("Foreign key 2 name in source is badly set", tableEditor.getCellText(1,"ForeignKey2", "Name In Source"),
				is("ForeignKey2Source"));
		
		collector.checkThat("Column of Foreign key 1 is badly set", tableEditor.getCellText(1,"ForeignKey1", "Columns"),
				is("fk_Column1 : string(4000)")); 
		collector.checkThat("Column of Foreign key 2 is badly set", tableEditor.getCellText(1,"ForeignKey2", "Columns"),
				is("fk_Column2 : string(4000)")); 
		
		collector.checkThat("Unique Key of Foreign key 1 is badly set", tableEditor.getCellText(1,"ForeignKey1", "Unique Key"),
				containsString("PrimaryKey")); 
		collector.checkThat("Unique Key of Foreign key 2 is badly set", tableEditor.getCellText(1,"ForeignKey2", "Unique Key"),
				containsString("UniqueConstraint")); 
		
		ProblemsView problemsView = new ProblemsView();
		collector.checkThat("Errors in imported view model",
				problemsView.getProblems(ProblemType.ERROR, new ProblemsResourceMatcher(NAME_VIEW_MODEL + ".xmi")),
				empty());
		collector.checkThat("Errors in imported VDB",
				problemsView.getProblems(ProblemType.ERROR, new ProblemsResourceMatcher(NAME_VDB + ".vdb")),
				empty());		
	}
	
	@Test
	public void exportVdb(){
		ddlHelper.createStaticVdb(NAME_VDB, PROJECT_NAME, NAME_VIEW_MODEL);		
		String contentFile = ddlHelper.createDynamicVdb(PROJECT_NAME, NAME_VDB, NAME_GENERATED_DYNAMIC_VDB);
		checkExportedFile(contentFile);

		/*test deploy generated dynamic VDB from static VDB*/
		String status = ddlHelper.deploy(PROJECT_NAME, NAME_GENERATED_DYNAMIC_VDB, teiidServer);
		collector.checkThat("vdb is not active", status, containsString("ACTIVE"));
	}
	
	@Test
	public void exportDdl(){
		String ddlContent = ddlHelper.exportDDL(PROJECT_NAME, NAME_VIEW_MODEL, WORK_PROJECT_NAME);
		checkExportedFile(ddlContent);
	}
	
	private void checkExportedFile(String contentFile){
		collector.checkThat("Wrong set foreign key 1", contentFile,
				new StringContains("CONSTRAINT ForeignKey1 FOREIGN KEY(fk_Column1) REFERENCES helpTable(Column_1)"));
		collector.checkThat("Wrong set foreign key 2", contentFile,
				new StringContains("CONSTRAINT ForeignKey2 FOREIGN KEY(fk_Column2) REFERENCES helpTable(Column_2)"));
		
	}
}
