package org.jboss.tools.teiid.ui.bot.test.ddl;

import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.is;

import org.hamcrest.core.StringContains;
import org.jboss.reddeer.eclipse.ui.problems.ProblemsView;
import org.jboss.reddeer.eclipse.ui.problems.ProblemsView.ProblemType;
import org.jboss.reddeer.eclipse.ui.problems.matcher.ProblemsResourceMatcher;
import org.jboss.reddeer.eclipse.ui.views.properties.PropertiesView;
import org.jboss.reddeer.junit.requirement.inject.InjectRequirement;
import org.jboss.reddeer.junit.runner.RedDeerSuite;
import org.jboss.reddeer.requirements.openperspective.OpenPerspectiveRequirement.OpenPerspective;
import org.jboss.reddeer.requirements.server.ServerReqState;
import org.jboss.tools.teiid.reddeer.DdlHelper;
import org.jboss.tools.teiid.reddeer.dialog.GenerateVdbArchiveDialog;
import org.jboss.tools.teiid.reddeer.editor.VdbEditor;
import org.jboss.tools.teiid.reddeer.perspective.TeiidPerspective;
import org.jboss.tools.teiid.reddeer.requirement.TeiidServerRequirement;
import org.jboss.tools.teiid.reddeer.requirement.TeiidServerRequirement.TeiidServer;
import org.jboss.tools.teiid.reddeer.view.ModelExplorer;
import org.jboss.tools.teiid.reddeer.view.PropertiesViewExt;
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

@TeiidServer(state = ServerReqState.RUNNING)
public class ViewUdf {
	@InjectRequirement
	private static TeiidServerRequirement teiidServer;
	
	@Rule
	public ErrorCollector collector = new ErrorCollector();
	public DdlHelper ddlHelper = null;
	
	private static final String PROJECT_NAME = "ViewUdf";
	private static final String NAME_VIEW_MODEL = "viewUdfModel";
	private static final String NAME_UDF_FUNCTION = "udfConcatNull";

	private static final String NAME_VDB = "viewUdfVDB";
	private static final String NAME_ORIGINAL_DYNAMIC_VDB  = NAME_VDB + "-vdb.xml";
	
	private static final String NAME_GENERATED_DYNAMIC_VDB = "viewUdfVDB-vdb.xml";
	
	private static final String WORK_PROJECT_NAME = "workProject" ;
	
	private static final String UDF_LIB_PATH = "target/proc-udf/MyTestUdf/lib/";
	private static final String UDF_LIB = "MyTestUdf-1.0-SNAPSHOT.jar";
	
	@Before
	public void before() {
		ModelExplorer explorer = new ModelExplorer();
		explorer.deleteAllProjectsSafely();

		ddlHelper = new DdlHelper(collector);
		explorer.importProject("DDLtests/"+PROJECT_NAME);
		PropertiesViewExt.setUdf(PROJECT_NAME, NAME_VIEW_MODEL, NAME_UDF_FUNCTION, UDF_LIB, UDF_LIB_PATH);

		explorer.createProject(WORK_PROJECT_NAME);
	}
	
	@After
	public void after(){
		new ModelExplorer().deleteAllProjectsSafely();
	}
	
	@Test
	public void importDdl(){
		DDLTeiidImportWizard.openWizard()
				.setPath("resources/projects/DDLtests/"+PROJECT_NAME+"/"+ NAME_VIEW_MODEL +".ddl")
				.setFolder(WORK_PROJECT_NAME)
				.setName(NAME_VIEW_MODEL)
				.setModelType(DDLTeiidImportWizard.View_Type)
				.generateValidDefaultSQL(true)
				.nextPage()
				.finish();
		PropertiesViewExt.setUdf(WORK_PROJECT_NAME, NAME_VIEW_MODEL, NAME_UDF_FUNCTION, UDF_LIB, UDF_LIB_PATH);
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
		PropertiesViewExt.setUdf(WORK_PROJECT_NAME, NAME_VIEW_MODEL, NAME_UDF_FUNCTION, UDF_LIB, UDF_LIB_PATH);
		new ModelExplorer().openModelEditor(WORK_PROJECT_NAME,NAME_VIEW_MODEL+".xmi");
		new ModelExplorer().openModelEditor(WORK_PROJECT_NAME,NAME_VDB+".vdb");
		VdbEditor staticVdb = VdbEditor.getInstance(NAME_VDB);
		staticVdb.synchronizeAll();
		staticVdb.saveAndClose();
		checkImportedModel();
		
		/*all models must be opened before synchronize VDB*//* TODO generated static vdb from dynamic vdb is different
		String status = ddlHelper.deploy(WORK_PROJECT_NAME, NAME_VDB, teiidServer);
		collector.checkThat("vdb is not active", status, containsString("ACTIVE"));*/
	}
	
	private void checkImportedModel(){
		new ModelExplorer().selectItem(WORK_PROJECT_NAME, NAME_VIEW_MODEL + ".xmi", "udfConcatNull");

		PropertiesView propertiesView = new PropertiesView();
		collector.checkThat("wrong function category",
				propertiesView.getProperty("Extension", "relational:Function Category").getPropertyValue(),
				is("MY_TESTING_FUNCTION_CATEGORY"));
		collector.checkThat("wrong java class",
				propertiesView.getProperty("Extension", "relational:Java Class").getPropertyValue(),
				is("userdefinedfunctions.MyConcatNull"));
		collector.checkThat("wrong java method",
				propertiesView.getProperty("Extension", "relational:Java Method").getPropertyValue(),
				is("myConcatNull"));
		
		ProblemsView problemsView = new ProblemsView();
		collector.checkThat("Errors in imported source model",
				problemsView.getProblems(ProblemType.ERROR, new ProblemsResourceMatcher(NAME_VIEW_MODEL + ".xmi")),
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
				.addModel(PROJECT_NAME, NAME_VIEW_MODEL)
				.finish();
		String dynamicVdbContent = ddlHelper.createDynamicVdb(PROJECT_NAME, NAME_VDB, NAME_GENERATED_DYNAMIC_VDB);
		checkExportedFile(dynamicVdbContent);

		/*test deploy generated dynamic VDB from static VDB*//*//TODO dynamic VDB doesn't support UDF function
		String status = ddlHelper.deploy(PROJECT_NAME, NAME_GENERATED_DYNAMIC_VDB, teiidServer);
		collector.checkThat("vdb is not active", status, containsString("ACTIVE"));*/
	}
	
	@Test
	public void exportDdl(){
		String ddlContent = ddlHelper.exportDDL(PROJECT_NAME, NAME_VIEW_MODEL, WORK_PROJECT_NAME);
		checkExportedFile(ddlContent);
	}
	
	private void checkExportedFile(String contentFile){
		collector.checkThat("wrong function category", contentFile , new StringContains("\"FUNCTION-CATEGORY\" 'MY_TESTING_FUNCTION_CATEGORY'"));
		collector.checkThat("wrong java class", contentFile , new StringContains("JAVA_CLASS 'userdefinedfunctions.MyConcatNull'"));
		collector.checkThat("wrong function category", contentFile , new StringContains("JAVA_METHOD 'myConcatNull'"));
	}
}

