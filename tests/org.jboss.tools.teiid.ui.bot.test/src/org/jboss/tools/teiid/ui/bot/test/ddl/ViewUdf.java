package org.jboss.tools.teiid.ui.bot.test.ddl;

import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertEquals;

import org.eclipse.reddeer.common.wait.TimePeriod;
import org.eclipse.reddeer.common.wait.WaitUntil;
import org.eclipse.reddeer.eclipse.ui.markers.matcher.MarkerResourceMatcher;
import org.eclipse.reddeer.eclipse.ui.views.markers.ProblemsView;
import org.eclipse.reddeer.eclipse.ui.views.markers.ProblemsView.ProblemType;
import org.eclipse.reddeer.eclipse.ui.views.properties.PropertySheet;
import org.eclipse.reddeer.junit.requirement.inject.InjectRequirement;
import org.eclipse.reddeer.junit.runner.RedDeerSuite;
import org.eclipse.reddeer.requirements.openperspective.OpenPerspectiveRequirement.OpenPerspective;
import org.eclipse.reddeer.requirements.server.ServerRequirementState;
import org.eclipse.reddeer.swt.condition.ShellIsAvailable;
import org.eclipse.reddeer.swt.impl.button.OkButton;
import org.eclipse.reddeer.swt.impl.label.DefaultLabel;
import org.eclipse.reddeer.swt.impl.menu.ContextMenuItem;
import org.eclipse.reddeer.workbench.handler.EditorHandler;
import org.hamcrest.core.StringContains;
import org.jboss.tools.common.reddeer.JiraClient;
import org.jboss.tools.teiid.reddeer.DdlHelper;
import org.jboss.tools.teiid.reddeer.dialog.GenerateDynamicVdbDialog;
import org.jboss.tools.teiid.reddeer.perspective.TeiidPerspective;
import org.jboss.tools.teiid.reddeer.requirement.TeiidServerRequirement;
import org.jboss.tools.teiid.reddeer.requirement.TeiidServerRequirement.TeiidServer;
import org.jboss.tools.teiid.reddeer.view.ModelExplorer;
import org.jboss.tools.teiid.reddeer.view.PropertiesViewExt;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ErrorCollector;
import org.junit.runner.RunWith;

@RunWith(RedDeerSuite.class)
@OpenPerspective(TeiidPerspective.class)

@TeiidServer(state = ServerRequirementState.RUNNING)
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
	public void importDdlTest(){
		ddlHelper.importDdlFromView(PROJECT_NAME, NAME_VIEW_MODEL, WORK_PROJECT_NAME);		
		PropertiesViewExt.setUdf(WORK_PROJECT_NAME, NAME_VIEW_MODEL, NAME_UDF_FUNCTION, UDF_LIB, UDF_LIB_PATH);
		checkImportedModel();
	}

	@Test
	public void importVdbTest(){
		ddlHelper.importVdb(PROJECT_NAME, NAME_ORIGINAL_DYNAMIC_VDB, WORK_PROJECT_NAME);
		
		PropertiesViewExt.setUdf(WORK_PROJECT_NAME, NAME_VIEW_MODEL, NAME_UDF_FUNCTION, UDF_LIB, UDF_LIB_PATH);
		ddlHelper.synchronizeAllModelsInVdb(WORK_PROJECT_NAME, NAME_VDB);
		checkImportedModel();
        /*
         * TODO generated static vdb from dynamic vdb is different String status = ddlHelper.deploy(WORK_PROJECT_NAME,
         * NAME_VDB, teiidServer); collector.checkThat("vdb is not active", status, containsString("ACTIVE"));
         */
	}
	
	private void checkImportedModel(){
		new ModelExplorer().selectItem(WORK_PROJECT_NAME, NAME_VIEW_MODEL + ".xmi", "udfConcatNull");

		PropertySheet propertiesView = new PropertySheet();
		collector.checkThat("wrong function category",
				propertiesView.getProperty("Extension", "relational:Function Category").getPropertyValue(),
				is("MY_TESTING_FUNCTION_CATEGORY"));
		collector.checkThat("wrong java class",
				propertiesView.getProperty("Extension", "relational:Java Class").getPropertyValue(),
				is("userdefinedfunctions.MyConcatNull"));
		collector.checkThat("wrong java method",
				propertiesView.getProperty("Extension", "relational:Java Method").getPropertyValue(),
				is("myConcatNull"));
		
        EditorHandler.getInstance().closeAll(false);// vdb editor contains problems view too, it must be closed
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
        new ModelExplorer().selectItem(PROJECT_NAME, NAME_VDB + ".vdb");
        new ContextMenuItem("Modeling", "Generate VDB XML").select();
        new WaitUntil(new ShellIsAvailable("Generate Dynamic VDB Status "), TimePeriod.DEFAULT);
        DefaultLabel warning = new DefaultLabel(1);
        warning.setFocus(); // on macOS the warning is not selected
        assertEquals(
            "This VDB contains 1 or more UDF jar files. A \"lib\" property has been added\n"
                    + " to the VDB xml which references these jars.\n\n"
                    + " Note that the actual UDF jars must be deployed for this VDB to be active.\n\n"
                    + " If you defined UDF as a module in a server, change \"lib\" property according to\n"
                    + " name of that module.\n\n For more information, see \"Support for User-Defined Features\""
                    + " in the documentation.",
            warning.getText());
        new OkButton().click();
        GenerateDynamicVdbDialog wizard = new GenerateDynamicVdbDialog();
        if (new JiraClient().isIssueClosed("TEIIDDES-3100")) {
            wizard.setName(NAME_GENERATED_DYNAMIC_VDB)
                .setFileName(NAME_GENERATED_DYNAMIC_VDB);
        }
        wizard.setLocation(PROJECT_NAME);
        String contents = wizard.getContents();
        wizard.finish();
        checkExportedFile(contents);

		/*test deploy generated dynamic VDB from static VDB*//*//TODO dynamic VDB doesn't support UDF function
		String status = ddlHelper.deploy(PROJECT_NAME, NAME_GENERATED_DYNAMIC_VDB, teiidServer);
		collector.checkThat("vdb is not active", status, containsString("ACTIVE"));*/
	}
	
	@Test
	public void exportDdlTest(){
		String ddlContent = ddlHelper.exportDDL(PROJECT_NAME, NAME_VIEW_MODEL, WORK_PROJECT_NAME);
		checkExportedFile(ddlContent);
	}
	
	private void checkExportedFile(String contentFile){
		collector.checkThat("wrong function category", contentFile , new StringContains("\"FUNCTION-CATEGORY\" 'MY_TESTING_FUNCTION_CATEGORY'"));
		collector.checkThat("wrong java class", contentFile , new StringContains("JAVA_CLASS 'userdefinedfunctions.MyConcatNull'"));
		collector.checkThat("wrong function category", contentFile , new StringContains("JAVA_METHOD 'myConcatNull'"));
	}
}

