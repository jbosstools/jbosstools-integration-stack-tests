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
import org.jboss.tools.teiid.reddeer.dialog.GenerateVdbArchiveDialog;
import org.jboss.tools.teiid.reddeer.editor.RelationalModelEditor;
import org.jboss.tools.teiid.reddeer.editor.TableEditor;
import org.jboss.tools.teiid.reddeer.editor.VdbEditor;
import org.jboss.tools.teiid.reddeer.perspective.TeiidPerspective;
import org.jboss.tools.teiid.reddeer.requirement.TeiidServerRequirement;
import org.jboss.tools.teiid.reddeer.requirement.TeiidServerRequirement.TeiidServer;
import org.jboss.tools.teiid.reddeer.view.ModelExplorer;
import org.jboss.tools.teiid.reddeer.wizard.imports.DDLTeiidImportWizard;
import org.jboss.tools.teiid.reddeer.wizard.imports.ImportFromFileSystemWizard;
import org.jboss.tools.teiid.reddeer.wizard.newWizard.VdbWizard;
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
public class SourceUniqueConstraint {
	@InjectRequirement
	private static TeiidServerRequirement teiidServer;
	
	@Rule
	public ErrorCollector collector = new ErrorCollector();
	public DdlHelper ddlHelper = null;
	
	private static final String PROJECT_NAME = "SourceUniqueConstraint";
	private static final String NAME_SOURCE_MODEL = "UniqueConstraintSource";
	private static final String NAME_VDB = "UniqueConstraintSourceVDB";
	private static final String NAME_ORIGINAL_DYNAMIC_VDB = NAME_VDB + "-vdb.xml";
	
	private static final String NAME_GENERATED_DYNAMIC_VDB = "UniqueConstraintSourceVDBgenerated-vdb.xml";
	
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
	public void importDdl(){
		DDLTeiidImportWizard.openWizard()
				.setPath("resources/projects/DDLtests/"+PROJECT_NAME+"/"+ NAME_SOURCE_MODEL +".ddl")
				.setFolder(WORK_PROJECT_NAME)
				.setName(NAME_SOURCE_MODEL)
				.setModelType(DDLTeiidImportWizard.Source_Type)
				.nextPage()
				.finish();
		checkImportedModel();
	}

	@Test
	public void importVdb(){
		ImportFromFileSystemWizard.openWizard()
				.setPath("resources/projects/DDLtests/"+PROJECT_NAME)
				.setFolder(WORK_PROJECT_NAME)
				.selectFile(NAME_ORIGINAL_DYNAMIC_VDB)
				.setCreteTopLevelFolder(false)
				.finish();
		GenerateVdbArchiveDialog wizard = new ModelExplorer().generateVdbArchive(WORK_PROJECT_NAME, NAME_ORIGINAL_DYNAMIC_VDB);
		wizard.next()
				.generate()
				.finish();
		checkImportedModel();
		
		/*all models must be opened before synchronize VDB*/
		new ModelExplorer().openModelEditor(WORK_PROJECT_NAME,NAME_SOURCE_MODEL+".xmi");
		new ModelExplorer().openModelEditor(WORK_PROJECT_NAME,NAME_VDB+".vdb");
		new ModelExplorer().changeConnectionProfile(ConnectionProfileConstants.SQL_SERVER_2008_PARTS_SUPPLIER, WORK_PROJECT_NAME, NAME_SOURCE_MODEL);
		VdbEditor staticVdb = VdbEditor.getInstance(NAME_VDB);
		staticVdb.synchronizeAll();
		staticVdb.saveAndClose();
		/*test deploy generated VDB from dynamic VDB*/
		String status = ddlHelper.deploy(WORK_PROJECT_NAME, NAME_VDB, teiidServer);
		collector.checkThat("vdb is not active", status, containsString("ACTIVE"));
	}
	
	
	private void checkImportedModel(){
		new ModelExplorer().selectItem(WORK_PROJECT_NAME, NAME_SOURCE_MODEL + ".xmi", "myTable");		
		
		RelationalModelEditor editor = new RelationalModelEditor(NAME_SOURCE_MODEL + ".xmi");
		editor.close();
		new ModelExplorer().openModelEditor(WORK_PROJECT_NAME, NAME_SOURCE_MODEL + ".xmi");	
		editor = new RelationalModelEditor(NAME_SOURCE_MODEL + ".xmi");
	    TableEditor tableEditor = editor.openTableEditor();
	    
		// check table description
		tableEditor.openTab(TableEditor.Tabs.UNIQUE_CONSTRAINTS);
		collector.checkThat("Columns is not set correctly", tableEditor.getCellText(0,"myTable", "Columns"),
	    		is("Column1 : string(4000)"));
		collector.checkThat("Name in source is not set correctly", tableEditor.getCellText(0,"myTable", "Name In Source"),
	    		is("UniqueConstraintSource"));
		collector.checkThat("Description is not set correctly", tableEditor.getCellText(0,"myTable", "Description"),
	    		is("UniqueConstraint description"));
		
		ProblemsView problemsView = new ProblemsView();
		collector.checkThat("Errors in imported source model",
				problemsView.getProblems(ProblemType.ERROR, new ProblemsResourceMatcher(NAME_SOURCE_MODEL + ".xmi")),
				empty());
		collector.checkThat("Errors in imported VDB",
				problemsView.getProblems(ProblemType.ERROR, new ProblemsResourceMatcher(NAME_VDB + ".vdb")),
				empty());
	}
	
	@Test
	public void exportVdb(){
		VdbWizard.openVdbWizard()
				.setName(NAME_VDB)
				.setLocation(PROJECT_NAME)
				.addModel(PROJECT_NAME, NAME_SOURCE_MODEL)				
				.finish();
		String contentFile = ddlHelper.createDynamicVdb(PROJECT_NAME, NAME_VDB, NAME_GENERATED_DYNAMIC_VDB);
		checkExportedFile(contentFile);

		/*test deploy generated dynamic VDB from static VDB*/
		String status = ddlHelper.deploy(PROJECT_NAME, NAME_GENERATED_DYNAMIC_VDB, teiidServer);
		collector.checkThat("vdb is not active", status, containsString("ACTIVE"));
	}
	
	@Test
	public void exportDdl(){
		String ddlContent = ddlHelper.exportDDL(PROJECT_NAME, NAME_SOURCE_MODEL, WORK_PROJECT_NAME);
		checkExportedFile(ddlContent);
	}
	
	private void checkExportedFile(String contentFile){
		collector.checkThat("wrong set unique constraint", contentFile, new StringContains("CONSTRAINT UniqueConstraint UNIQUE(Column1)"));
		collector.checkThat("wrong set description", contentFile, new StringContains("ANNOTATION 'UniqueConstraint description'"));
		collector.checkThat("wrong set name in source", contentFile, new StringContains("NAMEINSOURCE 'UniqueConstraintSource'"));
	}
}