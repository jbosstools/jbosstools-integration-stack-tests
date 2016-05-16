package org.jboss.tools.teiid.ui.bot.test;

import static org.junit.Assert.*;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Properties;

import org.jboss.reddeer.common.wait.TimePeriod;
import org.jboss.reddeer.common.wait.WaitWhile;
import org.jboss.reddeer.junit.requirement.inject.InjectRequirement;
import org.jboss.reddeer.junit.runner.RedDeerSuite;
import org.jboss.reddeer.requirements.server.ServerReqState;
import org.jboss.reddeer.swt.api.TableItem;
import org.jboss.reddeer.swt.impl.button.PushButton;
import org.jboss.reddeer.swt.impl.menu.ContextMenu;
import org.jboss.reddeer.swt.impl.menu.ShellMenu;
import org.jboss.reddeer.swt.impl.shell.DefaultShell;
import org.jboss.reddeer.swt.impl.tab.DefaultTabItem;
import org.jboss.reddeer.swt.impl.table.DefaultTable;
import org.jboss.reddeer.swt.impl.text.DefaultText;
import org.jboss.tools.teiid.reddeer.ModelBuilder;
import org.jboss.tools.teiid.reddeer.ModelClass;
import org.jboss.tools.teiid.reddeer.ModelType;
import org.jboss.tools.teiid.reddeer.condition.IsInProgress;
import org.jboss.tools.teiid.reddeer.connection.TeiidJDBCHelper;
import org.jboss.tools.teiid.reddeer.editor.ModelEditor;
import org.jboss.tools.teiid.reddeer.manager.ConnectionProfilesConstants;
import org.jboss.tools.teiid.reddeer.manager.ImportManager;
import org.jboss.tools.teiid.reddeer.manager.ImportMetadataManager;
import org.jboss.tools.teiid.reddeer.manager.ModelExplorerManager;
import org.jboss.tools.teiid.reddeer.manager.VDBManager;
import org.jboss.tools.teiid.reddeer.requirement.TeiidServerRequirement;
import org.jboss.tools.teiid.reddeer.requirement.TeiidServerRequirement.TeiidServer;
import org.jboss.tools.teiid.reddeer.view.ModelExplorer;
import org.jboss.tools.teiid.reddeer.view.ServersViewExt;
import org.jboss.tools.teiid.reddeer.wizard.MetadataModelWizard;
import org.jboss.tools.teiid.reddeer.wizard.TeiidConnectionImportWizard;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
/**
 * @author mkralik
 */

@RunWith(RedDeerSuite.class)
@TeiidServer(state = ServerReqState.RUNNING, connectionProfiles = {
	ConnectionProfilesConstants.ORACLE_11G_BQT2,})
public class GeometryTypeTest {
	
	@InjectRequirement
	private static TeiidServerRequirement teiidServer;
	
	public static final String PROJECT_NAME = "GeometryType";
	public static final String SOURCE_MODEL_NAME = "geometrySourceModel";
	public static final String SOURCE_TEIID_MODEL_NAME = "geometrySourceTeiidModel";
	public static final String VIEW_MODEL_NAME = "geometryViewModel";
	public static final String VIEW_TEIID_MODEL_NAME = "geometryViewTeiidModel";
	public static final String VDB_NAME = "geometryVDB";
	public static final String VDB_TEIID_NAME = "geometryTeiidVDB";

	
	@BeforeClass
	public static void before() {
		new ModelExplorerManager().createProject(PROJECT_NAME);
	}
	
	@Test
	public void importFromJDBC(){
		Properties iProps = new Properties();
		iProps.setProperty("itemList", "BQT2/TABLE/BUILDINGS");
		new ImportManager().importFromDatabase(PROJECT_NAME, SOURCE_MODEL_NAME, ConnectionProfilesConstants.ORACLE_11G_BQT2, iProps,false);
		//TEIIDDES-2799
		setGeometryDatatype(PROJECT_NAME,SOURCE_MODEL_NAME+".xmi","BUILDINGS","POSITION : object(1)");
		setGeometryDatatype(PROJECT_NAME,SOURCE_MODEL_NAME+".xmi","BUILDINGS","FOOTPRINT : object(1)");
		new ShellMenu("File","Save All").select();

		assertTrue(testSetDatatype(SOURCE_MODEL_NAME,"geometry : xs:base64Binary","SDO_GEOMETRY"));
		
		createView(SOURCE_MODEL_NAME,VIEW_MODEL_NAME);
		
		assertTrue(testSetDatatype(VIEW_MODEL_NAME,"geometry : xs:base64Binary","SDO_GEOMETRY"));
		
		VDBManager vdb = new VDBManager(); 
		vdb.createVDB(PROJECT_NAME, VDB_NAME);
		vdb.addModelsToVDB(PROJECT_NAME, VDB_NAME, new String[]{SOURCE_MODEL_NAME + ".xmi", VIEW_MODEL_NAME + ".xmi"});
		
		vdb.deployVDB(new String[]{PROJECT_NAME, VDB_NAME});
		
		testVDB(VIEW_MODEL_NAME,VDB_NAME);
	}
	@Test
	public void importViaTeiid(){		
		new ServersViewExt().createDatasource(teiidServer.getName(), ConnectionProfilesConstants.ORACLE_11G_BQT2);
		Properties iProps = new Properties();
		iProps.setProperty(TeiidConnectionImportWizard.DATA_SOURCE_NAME, ConnectionProfilesConstants.ORACLE_11G_BQT2);
		Properties teiidImporterProperties = new Properties();
		teiidImporterProperties.setProperty(TeiidConnectionImportWizard.IMPORT_PROPERTY_TABLE_NAME_PATTERN, "BUILDINGS");
		teiidImporterProperties.setProperty(TeiidConnectionImportWizard.IMPORT_PROPERTY_SCHEMA_PATTERN, "BQT2");
		
		new ImportMetadataManager().importFromTeiidConnection(PROJECT_NAME, SOURCE_TEIID_MODEL_NAME, iProps,null,teiidImporterProperties);
		//TEIIDDES-2799
		setGeometryDatatype(PROJECT_NAME,SOURCE_TEIID_MODEL_NAME+".xmi","BUILDINGS","POSITION : object(1)");
		setGeometryDatatype(PROJECT_NAME,SOURCE_TEIID_MODEL_NAME+".xmi","BUILDINGS","FOOTPRINT : object(1)");
		new ShellMenu("File","Save All").select();
		
		assertTrue(testSetDatatype(SOURCE_TEIID_MODEL_NAME,"geometry : xs:base64Binary","SDO_GEOMETRY"));
		
		createView(SOURCE_TEIID_MODEL_NAME,VIEW_TEIID_MODEL_NAME);
		
		assertTrue(testSetDatatype(VIEW_TEIID_MODEL_NAME,"geometry : xs:base64Binary","SDO_GEOMETRY"));
		
		VDBManager vdb = new VDBManager(); 
		vdb.createVDB(PROJECT_NAME, VDB_TEIID_NAME);
		vdb.addModelsToVDB(PROJECT_NAME, VDB_TEIID_NAME, new String[]{SOURCE_TEIID_MODEL_NAME + ".xmi", VIEW_TEIID_MODEL_NAME + ".xmi"});
		
		vdb.deployVDB(new String[]{PROJECT_NAME, VDB_TEIID_NAME});
		
		testVDB(VIEW_TEIID_MODEL_NAME,VDB_TEIID_NAME);
		
	}
	
	private void createView(String sourceModel,String viewModel){
		MetadataModelWizard wizard = new MetadataModelWizard();
		wizard.open();
		wizard.setLocation(PROJECT_NAME).setModelName(viewModel)
		.selectModelClass(ModelClass.RELATIONAL).selectModelType(ModelType.VIEW)
		.selectModelBuilder(ModelBuilder.TRANSFORM_EXISTING)
		.next();
		wizard.setExistingModel(PROJECT_NAME,sourceModel+".xmi")
		.finish();
		new WaitWhile(new IsInProgress(), TimePeriod.SHORT); 
		new ShellMenu("File","Save All").select();
	}
	
	private boolean testSetDatatype(String model,String exampleDatatype,String exampleNativeType){
		ModelEditor editor = new ModelEditor(model+".xmi");
		editor.showTab("Table Editor");
		new DefaultTabItem("Columns").activate();
		List<TableItem> items = new DefaultTable().getItems();
		return ( exampleDatatype.equals(items.get(2).getText(29)) && exampleDatatype.equals(items.get(3).getText(29))) && //check 
				exampleNativeType.equals(items.get(2).getText(3)) && exampleNativeType.equals(items.get(3).getText(3));  //check native type
	}
	
	private void setGeometryDatatype(String... tables){ 
		new ModelExplorer().activate();
		new ModelExplorerManager().getModelExplorerView().openModelEditor(tables[0],tables[1],tables[2],tables[3]);
		new WaitWhile(new IsInProgress(), TimePeriod.SHORT); 
		new ContextMenu("Modeling","Set Datatype").select();
		new DefaultShell("Select a Datatype");
		new DefaultText(0).setText("geometry");
		new DefaultTable().getItem(0).click();
		new PushButton("OK").click();
	}
	
	private void testVDB(String viewModel,String vdb){
		TeiidJDBCHelper jdbchelper = new TeiidJDBCHelper(teiidServer, vdb);
		String result;
		try {
 			ResultSet rs = jdbchelper.executeQueryWithResultSet("SELECT ST_AsText(FOOTPRINT) FROM \""+viewModel+"\".\"BUILDINGS\"");
 			rs.next(); 			
 			result = rs.getString(1);        
 			assertEquals("POLYGON ((50.0 31.0, 54.0 31.0, 54.0 29.0, 50.0 29.0, 50.0 31.0))",result);
 			
 		} catch (SQLException e) {
 			fail(e.getMessage());
 		}
		try {
 			ResultSet rs = jdbchelper.executeQueryWithResultSet("SELECT ST_AsText(POSITION) FROM \""+viewModel+"\".\"BUILDINGS\"");
 			rs.next(); 			
 			result = rs.getString(1);        
 			assertEquals("POINT (52.0 30.0)",result);
 			rs.next(); 			
 			result = rs.getString(1);        
 			assertEquals("POINT (64.0 33.0)",result);
 		} catch (SQLException e) {
 			fail(e.getMessage());
 		}
		
	}
}
