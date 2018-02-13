package org.jboss.tools.teiid.ui.bot.test;

import static org.hamcrest.Matchers.containsString;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.sql.SQLException;

import org.eclipse.reddeer.common.wait.AbstractWait;
import org.eclipse.reddeer.common.wait.TimePeriod;
import org.eclipse.reddeer.junit.requirement.inject.InjectRequirement;
import org.eclipse.reddeer.junit.runner.RedDeerSuite;
import org.eclipse.reddeer.requirements.openperspective.OpenPerspectiveRequirement.OpenPerspective;
import org.eclipse.reddeer.requirements.server.ServerRequirementState;
import org.eclipse.reddeer.swt.condition.ShellIsAvailable;
import org.eclipse.reddeer.swt.impl.button.OkButton;
import org.eclipse.reddeer.swt.impl.button.PushButton;
import org.eclipse.reddeer.swt.impl.ctab.DefaultCTabItem;
import org.eclipse.reddeer.swt.impl.group.DefaultGroup;
import org.eclipse.reddeer.swt.impl.shell.DefaultShell;
import org.eclipse.reddeer.swt.impl.styledtext.DefaultStyledText;
import org.eclipse.reddeer.swt.impl.table.DefaultTable;
import org.eclipse.reddeer.swt.impl.tree.DefaultTree;
import org.eclipse.reddeer.workbench.impl.editor.DefaultEditor;
import org.jboss.tools.teiid.reddeer.connection.ConnectionProfileConstants;
import org.jboss.tools.teiid.reddeer.connection.TeiidJDBCHelper;
import org.jboss.tools.teiid.reddeer.editor.RelationalModelEditor;
import org.jboss.tools.teiid.reddeer.editor.VdbEditor;
import org.jboss.tools.teiid.reddeer.perspective.TeiidPerspective;
import org.jboss.tools.teiid.reddeer.requirement.TeiidServerRequirement;
import org.jboss.tools.teiid.reddeer.requirement.TeiidServerRequirement.TeiidServer;
import org.jboss.tools.teiid.reddeer.view.ModelExplorer;
import org.jboss.tools.teiid.reddeer.view.ServersViewExt;
import org.jboss.tools.teiid.reddeer.wizard.imports.ImportFromFileSystemWizard;
import org.jboss.tools.teiid.reddeer.wizard.newWizard.MetadataModelWizard;
import org.jboss.tools.teiid.reddeer.wizard.newWizard.VdbWizard;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * @author mmajerni
 */
@RunWith(RedDeerSuite.class)
@OpenPerspective(TeiidPerspective.class)
@TeiidServer(state = ServerRequirementState.RUNNING, connectionProfiles = {ConnectionProfileConstants.ORACLE_11G_BOOKS,ConnectionProfileConstants.ORACLE_11G_PRODUCTS})

public class VDBEditorTest {
	
    private static final String EDITOR_PROJECT_NAME = "VDBEditorProject";
    private static final String EDITOR_SOURCE_MODEL = "Books";
    private static final String EDITOR_VIEW_MODEL = "SchemaModel";
    private static final String EDITOR_MODEL_TAB_VDB = "ModelTabVDB";
    private static final String EDITOR_VISIBILITY_VDB = "VisibilityVDB";
    private static final String EDITOR_SCHEMA_TAB_VDB = "SchemaTabVDB";
    private static final String EDITOR_OTHER_TABS_VDB = "OtherTabsVDB";
    private static final String EDITOR_FILTER_VDB = "FilterVDB";
    private static final String EDITOR_DEPLOY_VDB = "DeployVDB";
    private static final String EDITOR_TEST_BUTTON_VDB = "TestButtonVDB";
    private static final String EDITOR_SAVE_AS_XML_VDB = "SaveAsXmlVDB";

    private static final String SCHEMA = "PublisherSchema";

    private static final String UDF_PROJECT_NAME = "UDFJarsTabProject";
    private static final String UDF_SOURCE_MODEL = "Products";
    private static final String UDF_VIEW_MODEL = "UdfModel";
    private static final String UDF_VDB = "UdfVdb";
    private static final String UDF_LIB_PATH = "target/proc-udf/MyTestUdf/lib/";
    private static final String UDF_LIB = "MyTestUdf-1.0-SNAPSHOT.jar";
    private static final String OTHER_FILE = "viewModel.ddl";
	
	@InjectRequirement
	private static TeiidServerRequirement teiidServer;	
	private ModelExplorer modelExplorer;
	private TeiidJDBCHelper jdbcHelper;
	
	@Before
	public void importProject() {
		modelExplorer = new ModelExplorer();
		modelExplorer.importProject(EDITOR_PROJECT_NAME);
		modelExplorer.getProject(EDITOR_PROJECT_NAME).refresh();
		modelExplorer.changeConnectionProfile(ConnectionProfileConstants.ORACLE_11G_BOOKS, EDITOR_PROJECT_NAME, "sources", EDITOR_SOURCE_MODEL + ".xmi");
	}
	
	@After
	public void cleanUp(){
		new ModelExplorer().deleteAllProjectsSafely();
	}
	
	@Test
	public void modelTabTest(){
		modelExplorer.selectItem(EDITOR_PROJECT_NAME);
		
		VdbWizard.openVdbWizard()		
				.setName(EDITOR_MODEL_TAB_VDB)		
				.finish();
		
		VdbEditor vdbeditor= new VdbEditor(EDITOR_MODEL_TAB_VDB + ".vdb");
		vdbeditor.addModel(EDITOR_PROJECT_NAME,"views", EDITOR_VIEW_MODEL+ ".xmi");		
		vdbeditor.save();		
		vdbeditor.activate();
				
		assertEquals(EDITOR_SOURCE_MODEL + ".xmi",new DefaultTable(0).getItem(0).getText(0));		
		assertEquals(EDITOR_VIEW_MODEL + ".xmi",new DefaultTable(0).getItem(1).getText(0));
		
		new DefaultCTabItem("Schemas").activate();
		assertEquals(SCHEMA + ".xsd",new DefaultTable().getItem(0).getText(0));			
		
		vdbeditor.removeModel( EDITOR_PROJECT_NAME, EDITOR_SOURCE_MODEL + ".xmi");
		new PushButton("OK").click();		
		vdbeditor.save();
		assertEquals(0,new DefaultTable(0).rowCount());			

//		new DefaultCTabItem("Schemas").activate();
//		assertEquals(0,new DefaultTable(0).rowCount());	
																// TODO schema should be delete too
		
		RelationalModelEditor editor = new RelationalModelEditor(EDITOR_SOURCE_MODEL + ".xmi");
		editor.save();
		vdbeditor.save();
	}
	
	@Test
	public void visibilityTest() throws Exception{
		modelExplorer.selectItem(EDITOR_PROJECT_NAME);
		
		VdbWizard.openVdbWizard()		
				.setName(EDITOR_VISIBILITY_VDB)	
				.addModel(EDITOR_PROJECT_NAME,"views",EDITOR_VIEW_MODEL + ".xmi")
				.finish();		
			
 		modelExplorer.deployVdb(EDITOR_PROJECT_NAME, EDITOR_VISIBILITY_VDB); 		
 		jdbcHelper = new TeiidJDBCHelper(teiidServer, EDITOR_VISIBILITY_VDB);

		if (!jdbcHelper.isQuerySuccessful("SELECT * FROM StagingDocument ORDER BY publisherId, isbn", true)){
			fail("SQL should be successful");
		}
		
		VdbEditor vdbeditor= new VdbEditor(EDITOR_VISIBILITY_VDB + ".vdb");
		vdbeditor.activate();	
		
		new DefaultTable(0).getItem(0).click(2);
		vdbeditor.save();
		modelExplorer.deployVdb(EDITOR_PROJECT_NAME, EDITOR_VISIBILITY_VDB);
		jdbcHelper = new TeiidJDBCHelper(teiidServer, EDITOR_VISIBILITY_VDB);
		if (!jdbcHelper.isQuerySuccessful("SELECT * FROM StagingDocument ORDER BY publisherId, isbn", true)){
			fail("SQL should be successful");
		}
		try {
			jdbcHelper.executeQueryWithXmlStringResult("SELECT * FROM BOOKS");
			fail("SQL should not be successful");
		} catch (SQLException e) {
			String mess=e.getMessage();
			assertEquals("Remote org.teiid.api.exception.query.QueryResolverException: Group does not exist: BOOKS", mess); 
		}
		
		vdbeditor.activate();
		
		new DefaultTable(0).getItem(0).click(2);
		new DefaultTable(0).getItem(1).click(2);
		vdbeditor.save();		
			
		modelExplorer.deployVdb(EDITOR_PROJECT_NAME, EDITOR_VISIBILITY_VDB);
		jdbcHelper = new TeiidJDBCHelper(teiidServer, EDITOR_VISIBILITY_VDB);
		try {
			jdbcHelper.executeQueryWithXmlStringResult("SELECT * FROM StagingDocument ORDER BY publisherId, isbn");
			throw new Exception("Should not work");	
		} catch (SQLException e) {
			String mess=e.getMessage();
			assertEquals("Remote org.teiid.api.exception.query.QueryResolverException: Group does not exist: StagingDocument", mess); 
		}
		RelationalModelEditor editor = new RelationalModelEditor(EDITOR_SOURCE_MODEL + ".xmi");
		editor.save();
		vdbeditor.save();
	}
	
	@Test 
	public void schemasTabTest(){
		modelExplorer.selectItem(EDITOR_PROJECT_NAME);
		VdbWizard.openVdbWizard()		
				.setName(EDITOR_SCHEMA_TAB_VDB)	
				.addModel(EDITOR_PROJECT_NAME,"views",EDITOR_VIEW_MODEL + ".xmi")
				.finish();
		
		VdbEditor vdbeditor= new VdbEditor(EDITOR_SCHEMA_TAB_VDB + ".vdb");
		vdbeditor.activate();
		
		vdbeditor.removeSchema(SCHEMA);
		// TODO check vdb is not valid TEIIDDES-2895
		assertEquals(0,new DefaultTable(0).rowCount());			
		
		vdbeditor.addSchema(EDITOR_PROJECT_NAME,"schemas",SCHEMA + ".xsd");
		
		assertEquals(SCHEMA + ".xsd",new DefaultTable().getItem(0).getText(0));
		RelationalModelEditor editor = new RelationalModelEditor(EDITOR_SOURCE_MODEL + ".xmi");
		editor.save();
		vdbeditor.save();
	}
	
	@Test
	public void UDFJarsTabTest() throws Exception{	
		modelExplorer.importProject(UDF_PROJECT_NAME);
		ImportFromFileSystemWizard.openWizard()
            .setPath(UDF_LIB_PATH)
            .setFolder(UDF_PROJECT_NAME)
            .selectFile(UDF_LIB)
            .setCreteTopLevelFolder(true)
            .finish();
		modelExplorer.getProject(UDF_PROJECT_NAME).refresh();
		modelExplorer.changeConnectionProfile(ConnectionProfileConstants.ORACLE_11G_PRODUCTS, UDF_PROJECT_NAME, UDF_SOURCE_MODEL + ".xmi");		
		modelExplorer.selectItem(UDF_PROJECT_NAME);
		
		VdbWizard.openVdbWizard()		
				.setName(UDF_VDB)	
				.addModel(UDF_PROJECT_NAME,UDF_VIEW_MODEL + ".xmi")
				.finish();
		
		VdbEditor vdbeditor= new VdbEditor(UDF_VDB + ".vdb");
		vdbeditor.activate();		
		
		new DefaultCTabItem("UDF Jars").activate();
		assertEquals(UDF_LIB,new DefaultTable().getItem(0).getText(0));
		
		vdbeditor.removeUDFJar(UDF_LIB);		
		vdbeditor.save();
		
		// TODO check vdb is not valid TEIIDDES-2895
		
//		modelExplorer.selectItem(UDF_PROJECT_NAME,UDF_VDB + ".vdb");			
//		modelExplorer.deployVdb(UDF_PROJECT_NAME, UDF_VDB);					
//	 	new WaitWhile(new JobIsRunning(), TimePeriod.VERY_LONG);
//			new PushButton("OK").click();
//		}catch(Exception e){
//			String mess=e.getMessage();
//			assertEquals("Cannot Execute VDB UdfVdb.vdb - still loading.", mess); 			
//			throw new Exception("Should show window: Cannot Execute VDB UdfVdb.vdb - still loading.");	

		vdbeditor.activate();
		vdbeditor.addUDFJar(UDF_PROJECT_NAME,"lib",UDF_LIB);			
		
		RelationalModelEditor booksEditor = new RelationalModelEditor(EDITOR_SOURCE_MODEL + ".xmi");
		booksEditor.save();
		RelationalModelEditor productsEditor = new RelationalModelEditor(UDF_SOURCE_MODEL + ".xmi");
		productsEditor.save();		
		vdbeditor.synchronizeAll();
		vdbeditor.save();
		
		modelExplorer.deployVdb(UDF_PROJECT_NAME, UDF_VDB);
		jdbcHelper = new TeiidJDBCHelper(teiidServer, UDF_VDB);
		assertEquals(4,jdbcHelper.getNumberOfResults("SELECT * FROM UdfView WHERE namebusiness LIKE 'General%'"));
	}
	
	@Test 
	public void restTabsTest(){
		modelExplorer.selectItem(EDITOR_PROJECT_NAME);

		VdbWizard.openVdbWizard()		
				.setName(EDITOR_OTHER_TABS_VDB)	
				.addModel(EDITOR_PROJECT_NAME,"views",EDITOR_VIEW_MODEL + ".xmi")
				.finish();
		
		VdbEditor vdbeditor= new VdbEditor(EDITOR_OTHER_TABS_VDB + ".vdb");
		vdbeditor.activate();			
		
		vdbeditor.addOtherFile(EDITOR_PROJECT_NAME,"ddl",OTHER_FILE);
		vdbeditor.addProjectDescription("This is test of Description tab");
		vdbeditor.addModelDescription(EDITOR_SOURCE_MODEL, "This is test of selected model Description tab");
		
		vdbeditor.addDefaultMultiSource(EDITOR_SOURCE_MODEL);		
		assertEquals(2,new DefaultTable(1).rowCount() );
		vdbeditor.deleteMultiSourceModel(EDITOR_SOURCE_MODEL, "Name_1");
		assertEquals(1,new DefaultTable(1).rowCount() );
		
		vdbeditor.save();		

		vdbeditor.addModelProperty(EDITOR_SOURCE_MODEL, "PropertyName", "10");		
		assertEquals("PropertyName",new DefaultTable(1).getItem(0).getText(0) );
		assertEquals("10",new DefaultTable(1).getItem(0).getText(1) );
		vdbeditor.deleteModelProperty(EDITOR_SOURCE_MODEL, "PropertyName");		
		assertEquals(0,new DefaultTable(1).rowCount() );
		
		vdbeditor.save();
		vdbeditor.close();		
		new ModelExplorer().openModelEditor(EDITOR_PROJECT_NAME, EDITOR_OTHER_TABS_VDB + ".vdb");	
		vdbeditor= new VdbEditor(EDITOR_OTHER_TABS_VDB + ".vdb");
		vdbeditor.activate();		
		
		new DefaultCTabItem("Other Files").activate();
		assertEquals(OTHER_FILE,new DefaultTable(0).getItem(0).getText() );		
		
		new DefaultCTabItem("Description").activate();	
		assertEquals("This is test of Description tab" ,new DefaultStyledText(new DefaultGroup("Description")).getText());		
		
		new DefaultCTabItem("Models").activate();		
		new DefaultTable(0).getItem(EDITOR_SOURCE_MODEL + ".xmi").click();		
		new DefaultCTabItem("Description").activate();
		assertEquals("This is test of selected model Description tab" ,new DefaultStyledText().getText());		

		vdbeditor.addTranslatorOverride("oracle2", "oracle");		
		vdbeditor.save();
		vdbeditor.close();
		new ModelExplorer().openModelEditor(EDITOR_PROJECT_NAME, EDITOR_OTHER_TABS_VDB + ".vdb");	
		vdbeditor= new VdbEditor(EDITOR_OTHER_TABS_VDB + ".vdb");
		vdbeditor.activate();
		
		new DefaultCTabItem("Translator Overrides").activate();
		assertEquals("oracle2",new DefaultTable(0).getItem(0).getText() );
		
		vdbeditor.editTranslatorOverride("oracle2", "oracle3");		
		vdbeditor.save();
		vdbeditor.close();
		new ModelExplorer().openModelEditor(EDITOR_PROJECT_NAME, EDITOR_OTHER_TABS_VDB + ".vdb");	
		vdbeditor= new VdbEditor(EDITOR_OTHER_TABS_VDB + ".vdb");
		vdbeditor.activate();		
		
		new DefaultCTabItem("Translator Overrides").activate();
		assertEquals("oracle3",new DefaultTable(0).getItem(0).getText() );				

		vdbeditor.addTranslatorOverrideProperty("oracle3", "Aproperty", "10");					
		vdbeditor.save();
		vdbeditor.close();
		new ModelExplorer().openModelEditor(EDITOR_PROJECT_NAME, EDITOR_OTHER_TABS_VDB + ".vdb");	
		vdbeditor= new VdbEditor(EDITOR_OTHER_TABS_VDB + ".vdb");
		vdbeditor.activate();
		
		new DefaultCTabItem("Translator Overrides").activate();
		new DefaultTable(0).getItem("oracle3").select();
		assertEquals("Aproperty",new DefaultTable(1).getItem(0).getText(0));

		try {
			vdbeditor.editTranslatorOverrideProperty("oracle3", "Aproperty", "Bproperty");		
		} catch (Exception e) {			
			e.printStackTrace();
		}
		
		assertNotEquals("Aproperty",new DefaultTable(1).getItem(0).getText(0) );		
		
		vdbeditor.save();		

		try {
			vdbeditor.deleteTranslatorOverrideProperty("oracle3", "Bproperty");
		} catch (Exception e) {			
			e.printStackTrace();
		}
		
		vdbeditor.save();
		vdbeditor.close();
		new ModelExplorer().openModelEditor(EDITOR_PROJECT_NAME, EDITOR_OTHER_TABS_VDB + ".vdb");	
		vdbeditor= new VdbEditor(EDITOR_OTHER_TABS_VDB + ".vdb");
		vdbeditor.activate();		
		
		new DefaultCTabItem("Translator Overrides").activate();
		new DefaultTable(0).getItem("oracle3").select();
		assertNotEquals("Bproperty",new DefaultTable(1).getItem(0).getText(0) );		

		vdbeditor.deleteTranslatorOverride("oracle3"); 									
		vdbeditor.save();
		vdbeditor.close();
		new ModelExplorer().openModelEditor(EDITOR_PROJECT_NAME, EDITOR_OTHER_TABS_VDB + ".vdb");	
		vdbeditor= new VdbEditor(EDITOR_OTHER_TABS_VDB + ".vdb");
		vdbeditor.activate();		
		
		new DefaultCTabItem("Translator Overrides").activate();
		assertEquals(0,new DefaultTable(0).rowCount());		
		
		RelationalModelEditor editor = new RelationalModelEditor(EDITOR_SOURCE_MODEL + ".xmi");
		editor.save();
		vdbeditor.save();
	}
    @Test
    public void filterTest() {
        final String SOURCE_MODEL1 = "SourceModel1";
        final String SOURCE_MODEL2 = "xxSourceModel2";
        final String SOURCE_MODEL3 = "SourceModel3";
        final String VIEW_MODEL1 = "testView1";
        final String VIEW_MODEL2 = "testView2";

        createSourceViewModel(SOURCE_MODEL1, true);
        createSourceViewModel(SOURCE_MODEL2, true);
        createSourceViewModel(SOURCE_MODEL3, true);
        createSourceViewModel(VIEW_MODEL1, false);
        createSourceViewModel(VIEW_MODEL2, false);

        VdbWizard.openVdbWizard()
            .setName(EDITOR_FILTER_VDB)
            .addModel(EDITOR_PROJECT_NAME, SOURCE_MODEL1 + ".xmi")
            .addModel(EDITOR_PROJECT_NAME, SOURCE_MODEL2 + ".xmi")
            .addModel(EDITOR_PROJECT_NAME, SOURCE_MODEL3 + ".xmi")
            .addModel(EDITOR_PROJECT_NAME, VIEW_MODEL1 + ".xmi")
            .addModel(EDITOR_PROJECT_NAME, VIEW_MODEL2 + ".xmi")
            .finish();

        VdbEditor vdbeditor= new VdbEditor(EDITOR_FILTER_VDB + ".vdb");
        vdbeditor.activate();
        vdbeditor.save();

        vdbeditor.setFilterModelsType("SOURCE");
        assertEquals(3, new DefaultTable(0).getItems().size());

        vdbeditor.setFilterModelsType("VIEW");
        assertEquals(2, new DefaultTable(0).getItems().size());

        vdbeditor.setFilterModelsType("ALL");
        vdbeditor.setFilterCondition("test");
        assertEquals(2, new DefaultTable(0).getItems().size());

        vdbeditor.setFilterModelsType("SOURCE");
        vdbeditor.setFilterCondition("xx");
        assertEquals(1, new DefaultTable(0).getItems().size());

        vdbeditor.clearFilterCondition();
        assertEquals(5, new DefaultTable(0).getItems().size());

        //test filter in data roles tab
        vdbeditor.addDataRole();

        vdbeditor.setFilterModelsType("SOURCE");
        assertEquals(3, new DefaultTree().getItems().size());

        vdbeditor.setFilterModelsType("VIEW");
        assertEquals(2, new DefaultTree().getItems().size());

        vdbeditor.setFilterModelsType("ALL");
        vdbeditor.setFilterCondition("test");
        assertEquals(2, new DefaultTree().getItems().size());

        vdbeditor.setFilterModelsType("SOURCE");
        vdbeditor.setFilterCondition("xx");
        assertEquals(1, new DefaultTree().getItems().size());

        vdbeditor.clearFilterCondition();
        assertEquals(5, new DefaultTree().getItems().size());
    }

    @Test
    public void deployButtonTest() {
        VdbWizard.openVdbWizard()
            .setName(EDITOR_DEPLOY_VDB)
            .addModel(EDITOR_PROJECT_NAME, "sources", EDITOR_SOURCE_MODEL + ".xmi")
            .finish();

        VdbEditor vdbeditor= new VdbEditor(EDITOR_DEPLOY_VDB + ".vdb");
        vdbeditor.activate();
        vdbeditor.deployVdb();

        ServersViewExt serversView = new ServersViewExt();
        String status = serversView.getVdbStatus(teiidServer.getName(), EDITOR_DEPLOY_VDB);
        assertEquals("ACTIVE", status);
    }

    @Test
    public void testButtonTest() {
        VdbWizard.openVdbWizard()
            .setName(EDITOR_TEST_BUTTON_VDB)
            .addModel(EDITOR_PROJECT_NAME, "sources", EDITOR_SOURCE_MODEL + ".xmi")
            .finish();

        VdbEditor vdbeditor= new VdbEditor(EDITOR_TEST_BUTTON_VDB + ".vdb");
        vdbeditor.activate();
        vdbeditor.testVdb();

        assertTrue(new DefaultEditor(containsString("SQL Scrapbook")).isActive());
        TeiidPerspective.activate();
    }

    @Test
    public void saveAsXmlButtonTest() {
        VdbWizard.openVdbWizard()
            .setName(EDITOR_SAVE_AS_XML_VDB)
            .finish();

        VdbEditor vdbeditor= new VdbEditor(EDITOR_SAVE_AS_XML_VDB + ".vdb");
        vdbeditor.activate();
        vdbeditor.saveAsXml();

        if(new ShellIsAvailable("Generate Dynamic VDB Status ").test()) {
            DefaultShell ds = new DefaultShell("Generate Dynamic VDB Status ");
            new OkButton(ds).click();
            AbstractWait.sleep(TimePeriod.MEDIUM);
        }

        assertTrue(new ShellIsAvailable("Save as VDB XML file").test() );
        new OkButton(new DefaultShell("Save as VDB XML file")).click();
    }

    private void createSourceViewModel(String modelName, Boolean isSourceModel) {
        MetadataModelWizard wizard = MetadataModelWizard.openWizard();
        wizard.setLocation(EDITOR_PROJECT_NAME)
            .setModelName(modelName)
            .selectModelClass(MetadataModelWizard.ModelClass.RELATIONAL);
        if(isSourceModel) {
            wizard.selectModelType(MetadataModelWizard.ModelType.SOURCE)
                .finish();
        }else {
            wizard.selectModelType(MetadataModelWizard.ModelType.VIEW)
                .finish();
        }

        RelationalModelEditor editor = new RelationalModelEditor(modelName + ".xmi");
        editor.save();
    }
}
