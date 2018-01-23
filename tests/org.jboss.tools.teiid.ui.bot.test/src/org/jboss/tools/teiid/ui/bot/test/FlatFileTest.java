package org.jboss.tools.teiid.ui.bot.test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.util.List;

import org.eclipse.reddeer.common.wait.WaitUntil;
import org.eclipse.reddeer.common.wait.WaitWhile;
import org.eclipse.reddeer.eclipse.ui.problems.Problem;
import org.eclipse.reddeer.eclipse.ui.views.markers.ProblemsView;
import org.eclipse.reddeer.eclipse.ui.views.markers.ProblemsView.ProblemType;
import org.eclipse.reddeer.junit.requirement.inject.InjectRequirement;
import org.eclipse.reddeer.junit.runner.RedDeerSuite;
import org.eclipse.reddeer.requirements.server.ServerRequirementState;
import org.eclipse.reddeer.swt.condition.ShellIsAvailable;
import org.eclipse.reddeer.swt.impl.button.YesButton;
import org.eclipse.reddeer.swt.impl.menu.ContextMenuItem;
import org.eclipse.reddeer.swt.impl.menu.ShellMenuItem;
import org.eclipse.reddeer.workbench.handler.EditorHandler;
import org.eclipse.reddeer.workbench.impl.shell.WorkbenchShell;
import org.jboss.tools.common.reddeer.JiraClient;
import org.jboss.tools.teiid.reddeer.condition.IsInProgress;
import org.jboss.tools.teiid.reddeer.connection.TeiidJDBCHelper;
import org.jboss.tools.teiid.reddeer.editor.ModelEditor;
import org.jboss.tools.teiid.reddeer.editor.RelationalModelEditor;
import org.jboss.tools.teiid.reddeer.editor.TransformationEditor;
import org.jboss.tools.teiid.reddeer.perspective.TeiidPerspective;
import org.jboss.tools.teiid.reddeer.requirement.TeiidServerRequirement;
import org.jboss.tools.teiid.reddeer.requirement.TeiidServerRequirement.TeiidServer;
import org.jboss.tools.teiid.reddeer.view.ModelExplorer;
import org.jboss.tools.teiid.reddeer.wizard.connectionProfiles.noDatabase.FlatLocalConnectionProfileWizard;
import org.jboss.tools.teiid.reddeer.wizard.connectionProfiles.noDatabase.FlatRemoteConnectionProfileWizard;
import org.jboss.tools.teiid.reddeer.wizard.imports.FlatImportWizard;
import org.jboss.tools.teiid.reddeer.wizard.imports.FlatImportWizard.DelimiterCharacter;
import org.jboss.tools.teiid.reddeer.wizard.newWizard.VdbWizard;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * Flat file Import test
 * 
 * @author felias
 *
 */
@RunWith(RedDeerSuite.class)
@TeiidServer(state = ServerRequirementState.RUNNING)
public class FlatFileTest {

	private static final String PROJECT_NAME = "Flat_file_import";
	private static final String LOCAL_FILE_PATH = "resources/flat";
	private static final String LOCAL_FILE_NAME = "productdata_data.csv";
	private static final String CUSTOM_FIELD_FILE_NAME = "productdata_data_customField.csv";
	private static final String CUSTOM_LINE_FILE_NAME = "productdata_data_customLine.csv";

	private static final String LOCAL_PROFILE_NAME = "localFlatProfile";
	private static final String LOCAL_SOURCE_MODEL = "localSourceModel";
	private static final String LOCAL_VIEW_MODEL = "localViewModel";
	private static final String LOCAL_VIEW_TABLE = "localViewTable";
	private static final String LOCAL_VDB = "LocalFileVDB";
	
	private static final String REMOTE_PROFILE_NAME = "remoteFlatProfile";
	private static final String REMOTE_URL = "https://raw.githubusercontent.com/mmakovy/import-files/master/productdata_data.csv";
	private static final String REMOTE_SOURCE_MODEL = "remoteSourceModel";
	private static final String REMOTE_VIEW_MODEL = "remoteViewModel";
	private static final String REMOTE_VIEW_TABLE = "remoteViewTable";
	private static final String REMOTE_VDB = "RemoteFileVDB";
	private static final String JNDI_NAME_LOCAL = "LocalSource";
	private static final String JNDI_NAME_REMOTE = "RemoteSource";

	private final String EMPTY_SPACE_PATH = "resources/flat/folder empty space";

	@InjectRequirement
	private static TeiidServerRequirement teiidServer;

	@BeforeClass
	public static void beforeClass() {
		FlatLocalConnectionProfileWizard.openWizard(LOCAL_PROFILE_NAME)
				.setFile(LOCAL_FILE_PATH)
				.testConnection()
				.finish();
		FlatRemoteConnectionProfileWizard.openWizard(REMOTE_PROFILE_NAME)
				.setUrl(REMOTE_URL)
				.testConnection()
				.finish();
	}
	@Before
	public void before(){
		new ModelExplorer().deleteAllProjectsSafely();
		new ModelExplorer().createProject(PROJECT_NAME);
		new TeiidPerspective().open();
	}

	/**
	 * Creates a VDB from a file from a local computer
	 */
	@Test
	public void localFileTest() {
		FlatImportWizard.openWizard()
				.selectLocalFileImportMode()
				.nextPage()
				.selectProfile(LOCAL_PROFILE_NAME)
				.selectFile(LOCAL_FILE_NAME)
				.setSourceModel(LOCAL_SOURCE_MODEL)
				.setProject(PROJECT_NAME)
				.nextPage()
				.setJndiName(JNDI_NAME_LOCAL)
				.nextPage()
				.nextPage()
				.nextPage()
				.setViewModel(LOCAL_VIEW_MODEL)
				.setViewTable(LOCAL_VIEW_TABLE)
				.finish();
        workaround(LOCAL_SOURCE_MODEL);
		// Deploying the VDB
		VdbWizard.openVdbWizard()
				.setLocation(PROJECT_NAME)
				.setName(LOCAL_VDB)
				.addModel(PROJECT_NAME,LOCAL_VIEW_MODEL + ".xmi")
				.finish();

		new ModelExplorer().deployVdb(PROJECT_NAME, LOCAL_VDB);
        new WaitWhile(new IsInProgress());
		TeiidJDBCHelper jdbchelper = new TeiidJDBCHelper(teiidServer, LOCAL_VDB);
		assertEquals(122, jdbchelper.getNumberOfResults("SELECT * FROM "+LOCAL_VIEW_TABLE));
	}
	/*
	 * Creates a VDB using remote file
	 */
	@Test
	public void remoteFileTest() {
		FlatImportWizard.openWizard()
				.selectRemoteUrlImportMode()
				.nextPage()
				.selectProfile(REMOTE_PROFILE_NAME)
				.setSourceModel(REMOTE_SOURCE_MODEL)
				.setProject(PROJECT_NAME)
				.nextPage()
				.setJndiName(JNDI_NAME_REMOTE)
				.nextPage()
				.nextPage()
				.nextPage()
				.setViewModel(REMOTE_VIEW_MODEL)
				.setViewTable(REMOTE_VIEW_TABLE)
				.finish();
		// Deploying the VDB
		VdbWizard.openVdbWizard()
				.setLocation(PROJECT_NAME)
				.setName(REMOTE_VDB)
				.addModel(PROJECT_NAME,REMOTE_VIEW_MODEL + ".xmi")
				.finish();

		new ModelExplorer().deployVdb(PROJECT_NAME, REMOTE_VDB);
        new WaitWhile(new IsInProgress());
		TeiidJDBCHelper jdbchelper = new TeiidJDBCHelper(teiidServer, REMOTE_VDB);
		assertEquals(122, jdbchelper.getNumberOfResults("SELECT * FROM "+REMOTE_VIEW_TABLE));
	}
	
	@Test
	public void testEmptySpace(){
        String sourceModel = LOCAL_SOURCE_MODEL + "EmptySpace";
        String viewModel = LOCAL_VIEW_MODEL + "EmptySpace";

        String emptySpaceCp = "emptySpace";
        FlatLocalConnectionProfileWizard.openWizard(emptySpaceCp)
            .setFile(EMPTY_SPACE_PATH)
            .testConnection()
            .finish();
        
        FlatImportWizard.openWizard()
            .selectLocalFileImportMode().nextPage()
            .selectProfile(emptySpaceCp)
            .selectFile(LOCAL_FILE_NAME)
            .setSourceModel(sourceModel)
            .setProject(PROJECT_NAME).nextPage()
            .setJndiName(JNDI_NAME_LOCAL + "EmptySpace")
            .nextPage().nextPage().nextPage()
            .setViewModel(viewModel).setViewTable(LOCAL_VIEW_TABLE + "EmptySpace")
            .finish();
        VdbWizard.openVdbWizard().setLocation(PROJECT_NAME).setName(LOCAL_VDB + "EmptySpace")
            .addModel(PROJECT_NAME, viewModel)
            .finish();
        EditorHandler.getInstance().closeAll(false); //! vdbeditor contains problem view too
        List<Problem> problems = new ProblemsView().getProblems(ProblemType.WARNING);
        String expectedDescription = "Connection URL property "
                + new File("resources/flat/folder empty space").getAbsolutePath().toString() + " for source model "
                + sourceModel
                + " contains one or more spaces. Some operating systems may require replacement characters in URL.";
        assertThat(problems.stream().anyMatch(item -> expectedDescription.equals(item.getDescription())), is(true));
        assertThat(problems.stream().filter(item -> expectedDescription.equals(item.getDescription())).count(), is(2L));
	}
	/**
	 * Test import csv with custom line separator
	 */
	@Test
	public void testCustomLineSeparator(){
		FlatImportWizard.openWizard()
				.selectLocalFileImportMode()
				.nextPage()
				.selectProfile(LOCAL_PROFILE_NAME)
				.selectFile(CUSTOM_LINE_FILE_NAME)
				.setSourceModel(LOCAL_SOURCE_MODEL+"customLine")
				.setProject(PROJECT_NAME)
				.nextPage()
				.setJndiName(JNDI_NAME_LOCAL+"customLine")
				.nextPage()
				.setFixedWidth()
				.nextPage()
				.setConfigureDelimiters("#");
		addColumnToWizard(FlatImportWizard.getInstance());
		FlatImportWizard.getInstance()
				.nextPage()
				.setViewModel(LOCAL_VIEW_MODEL+"customLine")
				.setViewTable(LOCAL_VIEW_TABLE)
				.finish();
		
		new ModelExplorer().openModelEditor(PROJECT_NAME,LOCAL_VIEW_MODEL+"customLine.xmi");		
		TransformationEditor transformation = new RelationalModelEditor(LOCAL_VIEW_MODEL+"customLine.xmi").openTransformationDiagram(ModelEditor.ItemType.TABLE, LOCAL_VIEW_TABLE);
		assertTrue(transformation.getTransformation().contains("ROW DELIMITER '#'"));

        workaround(LOCAL_SOURCE_MODEL + "customLine");
		// Deploying the VDB
		VdbWizard.openVdbWizard()
				.setLocation(PROJECT_NAME)
				.setName(LOCAL_VDB+"customLine")
				.addModel(PROJECT_NAME,LOCAL_VIEW_MODEL + "customLine.xmi")
				.finish();

		new ModelExplorer().deployVdb(PROJECT_NAME, LOCAL_VDB+"customLine");
        new WaitWhile(new IsInProgress());
		TeiidJDBCHelper jdbchelper = new TeiidJDBCHelper(teiidServer, LOCAL_VDB+"customLine");
		assertEquals(3, jdbchelper.getNumberOfResults("SELECT * FROM "+LOCAL_VIEW_TABLE));
	}
	
	/**
	 * auxiliary functions for clarifying code
	 */
	private void addColumnToWizard(FlatImportWizard flatWizard){
		flatWizard.addColumn()
				.setName("INSTR_ID")
				.setType("biginteger")
				.setWidth("11")
				.finish();
		flatWizard.addColumn()
				.setName("NAME")
				.setWidth("27")
				.finish();
		flatWizard.addColumn()
				.setName("BALANCE")
				.setType("float")
				.setWidth("8")
				.finish();
		flatWizard.addColumn()
				.setName("ISDJI")
				.setType("boolean")
				.setWidth("6")
				.finish();
		flatWizard.addColumn()
				.setName("ISAMEXINT")
				.setType("boolean")
				.setWidth("10")
				.finish();
		flatWizard.addColumn()
				.setName("PRIBUSINESS")
				.setWidth("20")
				.finish();
	}
	
	/**
	 * Test import csv with custom field separator
	 */
	@Test
	public void testCustomFieldSeparator(){
		FlatImportWizard.openWizard()
				.selectLocalFileImportMode()
				.nextPage()
				.selectProfile(LOCAL_PROFILE_NAME)
				.selectFile(CUSTOM_FIELD_FILE_NAME)
				.setSourceModel(LOCAL_SOURCE_MODEL+"customField")
				.setProject(PROJECT_NAME)
				.nextPage()
				.setJndiName(JNDI_NAME_LOCAL+"customField")
				.nextPage()
				.nextPage()
				.selectDelimiterCharacter(DelimiterCharacter.other("#"))
				.nextPage()
				.setViewModel(LOCAL_VIEW_MODEL+"customField")
				.setViewTable(LOCAL_VIEW_TABLE)
				.finish();
		
		new ModelExplorer().openModelEditor(PROJECT_NAME,LOCAL_VIEW_MODEL+"customField.xmi");		
		TransformationEditor transformation = new RelationalModelEditor(LOCAL_VIEW_MODEL+"customField.xmi").openTransformationDiagram(ModelEditor.ItemType.TABLE, LOCAL_VIEW_TABLE);
		assertTrue(transformation.getTransformation().contains("DELIMITER '#'"));
		
        workaround(LOCAL_SOURCE_MODEL + "customField");
		// Deploying the VDB
		VdbWizard.openVdbWizard()
				.setLocation(PROJECT_NAME)
				.setName(LOCAL_VDB+"customField")
				.addModel(PROJECT_NAME,LOCAL_VIEW_MODEL + "customField.xmi")
				.finish();

		new ModelExplorer().deployVdb(PROJECT_NAME, LOCAL_VDB+"customField");
        new WaitWhile(new IsInProgress());
		TeiidJDBCHelper jdbchelper = new TeiidJDBCHelper(teiidServer, LOCAL_VDB+"customField");
		assertEquals(122, jdbchelper.getNumberOfResults("SELECT * FROM "+LOCAL_VIEW_TABLE));
	}

    /**
     * TEIIDDES-3107 Workaround for issue with number of parameters
     */
    public void workaround(String modelName) {
        if (!new JiraClient().isIssueClosed("TEIIDDES-3107")) {
            ModelExplorer explorer = new ModelExplorer();
            explorer.selectItem(PROJECT_NAME, modelName + ".xmi", "getTextFiles", "Result", "lastModified : timestamp");
            new ContextMenuItem("Delete").select();
            new WaitUntil(new ShellIsAvailable("Dependent Models Detected"));
            new YesButton().click();
            new WaitWhile(new IsInProgress(), false);
            explorer.selectItem(PROJECT_NAME, modelName + ".xmi", "getTextFiles", "Result", "created : timestamp");
            new ContextMenuItem("Delete").select();
            new WaitUntil(new ShellIsAvailable("Dependent Models Detected"));
            new YesButton().click();
            new WaitWhile(new IsInProgress(), false);
            explorer.selectItem(PROJECT_NAME, modelName + ".xmi", "getTextFiles", "Result", "size : int");
            new ContextMenuItem("Delete").select();
            new WaitUntil(new ShellIsAvailable("Dependent Models Detected"));
            new YesButton().click();
            new WaitWhile(new IsInProgress(), false);
            new ShellMenuItem(new WorkbenchShell(), "File", "Save All").select();
        }
    }
}