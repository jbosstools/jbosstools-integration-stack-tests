package org.jboss.tools.teiid.ui.bot.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.eclipse.reddeer.common.wait.AbstractWait;
import org.eclipse.reddeer.common.wait.TimePeriod;
import org.eclipse.reddeer.common.wait.WaitWhile;
import org.eclipse.reddeer.junit.requirement.inject.InjectRequirement;
import org.eclipse.reddeer.junit.runner.RedDeerSuite;
import org.eclipse.reddeer.requirements.server.ServerRequirementState;
import org.eclipse.reddeer.swt.impl.button.PushButton;
import org.eclipse.reddeer.swt.impl.ctab.DefaultCTabItem;
import org.eclipse.reddeer.swt.impl.menu.ContextMenuItem;
import org.eclipse.reddeer.swt.impl.menu.ShellMenuItem;
import org.eclipse.reddeer.swt.impl.shell.DefaultShell;
import org.eclipse.reddeer.swt.impl.table.DefaultTable;
import org.eclipse.reddeer.swt.impl.text.DefaultText;
import org.eclipse.reddeer.workbench.impl.shell.WorkbenchShell;
import org.jboss.tools.teiid.reddeer.condition.IsInProgress;
import org.jboss.tools.teiid.reddeer.connection.ConnectionProfileConstants;
import org.jboss.tools.teiid.reddeer.connection.TeiidJDBCHelper;
import org.jboss.tools.teiid.reddeer.editor.RelationalModelEditor;
import org.jboss.tools.teiid.reddeer.editor.TableEditor;
import org.jboss.tools.teiid.reddeer.requirement.TeiidServerRequirement;
import org.jboss.tools.teiid.reddeer.requirement.TeiidServerRequirement.TeiidServer;
import org.jboss.tools.teiid.reddeer.view.ModelExplorer;
import org.jboss.tools.teiid.reddeer.view.ServersViewExt;
import org.jboss.tools.teiid.reddeer.wizard.imports.ImportJDBCDatabaseWizard;
import org.jboss.tools.teiid.reddeer.wizard.imports.TeiidConnectionImportWizard;
import org.jboss.tools.teiid.reddeer.wizard.newWizard.MetadataModelWizard;
import org.jboss.tools.teiid.reddeer.wizard.newWizard.VdbWizard;
import org.junit.After;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
/**
 * @author mkralik
 */

@RunWith(RedDeerSuite.class)
@TeiidServer(state = ServerRequirementState.RUNNING, connectionProfiles = {
	ConnectionProfileConstants.ORACLE_11G_BQT2})
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
		new ModelExplorer().createProject(PROJECT_NAME);
	}
	
	@After
	public void after(){
        new ServersViewExt().deleteDatasource(teiidServer.getName(), "java:/" + ConnectionProfileConstants.ORACLE_11G_BQT2);
        new ServersViewExt().deleteDatasource(teiidServer.getName(), "java:/" + ConnectionProfileConstants.ORACLE_11G_BQT2 + "_DS");
        new ServersViewExt().deleteDatasource(teiidServer.getName(), "java:/Check_" + ConnectionProfileConstants.ORACLE_11G_BQT2);

		new ServersViewExt().undeployVdb(teiidServer.getName(), VDB_NAME);
		new ServersViewExt().undeployVdb(teiidServer.getName(), VDB_TEIID_NAME);
		
		new ServersViewExt().deleteDatasource(teiidServer.getName(), "java:/" + VDB_NAME);
		new ServersViewExt().deleteDatasource(teiidServer.getName(), "java:/" + VDB_TEIID_NAME);
	}
	
	@Test
	public void importFromJDBC(){
		ImportJDBCDatabaseWizard.openWizard()
				.setConnectionProfile(ConnectionProfileConstants.ORACLE_11G_BQT2)
				.nextPage()
				.setTableTypes(false, true, false)
				.nextPage()
				.setTables("DVQE/TABLE/BUILDINGS")
				.nextPage()
				.setFolder(PROJECT_NAME)
				.setModelName(SOURCE_MODEL_NAME)
				.finish();
		// Package Diagram / Diagram Editor must be open
		new RelationalModelEditor( SOURCE_MODEL_NAME +".xmi").activate();
		new DefaultCTabItem(1).activate();
		
		//TEIIDDES-2799
		setGeometryDatatype(PROJECT_NAME,SOURCE_MODEL_NAME+".xmi","BUILDINGS","POSITION : object(1)");
		setGeometryDatatype(PROJECT_NAME,SOURCE_MODEL_NAME+".xmi","BUILDINGS","FOOTPRINT : object(1)");
		new ShellMenuItem(new WorkbenchShell(), "File","Save All").select();

		assertTrue(testSetDatatype(SOURCE_MODEL_NAME,"geometry : xs:base64Binary","SDO_GEOMETRY"));
		
		createView(SOURCE_MODEL_NAME,VIEW_MODEL_NAME);
		
		assertTrue(testSetDatatype(VIEW_MODEL_NAME,"geometry : xs:base64Binary","SDO_GEOMETRY"));
		
		VdbWizard.openVdbWizard()
				.setLocation(PROJECT_NAME)
				.setName(VDB_NAME)
				.addModel(PROJECT_NAME, SOURCE_MODEL_NAME + ".xmi")
				.addModel(PROJECT_NAME, VIEW_MODEL_NAME + ".xmi")
				.finish();
		
		new ModelExplorer().deployVdb(PROJECT_NAME, VDB_NAME);
		
		testVDB(VIEW_MODEL_NAME,VDB_NAME);
	}
	@Test
	public void importViaTeiid(){		
		new ServersViewExt().createDatasource(teiidServer.getName(), ConnectionProfileConstants.ORACLE_11G_BQT2);

		TeiidConnectionImportWizard.openWizard()
				.selectDataSource("java:/" + ConnectionProfileConstants.ORACLE_11G_BQT2)
				.nextPage()
				.setImportPropertie(TeiidConnectionImportWizard.IMPORT_PROPERTY_TABLE_NAME_PATTERN, "BUILDINGS")
				.setImportPropertie(TeiidConnectionImportWizard.IMPORT_PROPERTY_SCHEMA_PATTERN, "DVQE")
				.nextPage()
				.setModelName(SOURCE_TEIID_MODEL_NAME)
				.setProject(PROJECT_NAME)
				.nextPageWithWait()
				.nextPageWithWait()
				.finish();
		
		//TEIIDDES-2799
		setGeometryDatatype(PROJECT_NAME,SOURCE_TEIID_MODEL_NAME+".xmi","BUILDINGS","POSITION : object(1)");
		setGeometryDatatype(PROJECT_NAME,SOURCE_TEIID_MODEL_NAME+".xmi","BUILDINGS","FOOTPRINT : object(1)");
		new ShellMenuItem(new WorkbenchShell(), "File","Save All").select();
		
		assertTrue(testSetDatatype(SOURCE_TEIID_MODEL_NAME,"geometry : xs:base64Binary","SDO_GEOMETRY"));
		
		createView(SOURCE_TEIID_MODEL_NAME,VIEW_TEIID_MODEL_NAME);
		
		assertTrue(testSetDatatype(VIEW_TEIID_MODEL_NAME,"geometry : xs:base64Binary","SDO_GEOMETRY"));
		
		VdbWizard.openVdbWizard()
				.setLocation(PROJECT_NAME)
				.setName(VDB_TEIID_NAME)
				.addModel(PROJECT_NAME, SOURCE_TEIID_MODEL_NAME + ".xmi")
				.addModel(PROJECT_NAME, VIEW_TEIID_MODEL_NAME + ".xmi")
				.finish();
		
		new ModelExplorer().deployVdb(PROJECT_NAME, VDB_TEIID_NAME);
		
		testVDB(VIEW_TEIID_MODEL_NAME,VDB_TEIID_NAME);
		
	}
	
	private void createView(String sourceModel,String viewModel){
		MetadataModelWizard.openWizard()
				.setLocation(PROJECT_NAME).setModelName(viewModel)
				.selectModelClass(MetadataModelWizard.ModelClass.RELATIONAL).selectModelType(MetadataModelWizard.ModelType.VIEW)
				.selectModelBuilder(MetadataModelWizard.ModelBuilder.TRANSFORM_EXISTING)
				.nextPage()
				.setExistingModel(PROJECT_NAME,sourceModel+".xmi")
				.finish();
		new WaitWhile(new IsInProgress(), TimePeriod.SHORT);
		new RelationalModelEditor(viewModel+".xmi").save();
	}
	
	private boolean testSetDatatype(String model,String exampleDatatype,String exampleNativeType){
		TableEditor table = new RelationalModelEditor(model+".xmi").openTableEditor();
		table.openTab("Columns");
		return ( exampleDatatype.equals(table.getCellText(2, 29)) && exampleDatatype.equals(table.getCellText(3, 29))) && //check 
				exampleNativeType.equals(table.getCellText(2, 3)) && exampleNativeType.equals(table.getCellText(3, 3));  //check native type
	}
	
	private void setGeometryDatatype(String... tables){ 
		new ModelExplorer().activate();
		new ModelExplorer().openModelEditor(tables[0],tables[1],tables[2],tables[3]);
		new WaitWhile(new IsInProgress(), TimePeriod.SHORT); 
		new ContextMenuItem("Modeling","Set Datatype").select();
		new DefaultShell("Select a Datatype");
		new DefaultText(0).setText("geometry");
        AbstractWait.sleep(TimePeriod.SHORT); // wait if show only geometry in the table
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
