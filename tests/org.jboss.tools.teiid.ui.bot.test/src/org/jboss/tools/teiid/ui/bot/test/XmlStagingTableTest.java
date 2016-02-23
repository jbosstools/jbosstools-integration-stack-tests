package org.jboss.tools.teiid.ui.bot.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Time;

import org.eclipse.swtbot.eclipse.finder.SWTWorkbenchBot;
import org.eclipse.swtbot.eclipse.gef.finder.widgets.SWTBotGefEditPart;
import org.eclipse.swtbot.swt.finder.keyboard.Keystrokes;
import org.jboss.reddeer.common.wait.AbstractWait;
import org.jboss.reddeer.common.wait.TimePeriod;
import org.jboss.reddeer.eclipse.core.resources.Project;
import org.jboss.reddeer.eclipse.ui.problems.ProblemsView;
import org.jboss.reddeer.eclipse.ui.problems.ProblemsView.ProblemType;
import org.jboss.reddeer.junit.requirement.inject.InjectRequirement;
import org.jboss.reddeer.junit.runner.RedDeerSuite;
import org.jboss.reddeer.requirements.openperspective.OpenPerspectiveRequirement.OpenPerspective;
import org.jboss.reddeer.requirements.server.ServerReqState;
import org.jboss.reddeer.swt.impl.button.FinishButton;
import org.jboss.reddeer.swt.impl.button.PushButton;
import org.jboss.reddeer.swt.impl.menu.ContextMenu;
import org.jboss.reddeer.swt.impl.menu.ShellMenu;
import org.jboss.reddeer.swt.impl.shell.DefaultShell;
import org.jboss.reddeer.swt.impl.table.DefaultTable;
import org.jboss.reddeer.swt.impl.text.DefaultText;
import org.jboss.reddeer.swt.impl.tree.DefaultTreeItem;
import org.jboss.reddeer.workbench.impl.shell.WorkbenchShell;
import org.jboss.tools.teiid.reddeer.ModelBuilder;
import org.jboss.tools.teiid.reddeer.ModelClass;
import org.jboss.tools.teiid.reddeer.ModelType;
import org.jboss.tools.teiid.reddeer.connection.TeiidJDBCHelper;
import org.jboss.tools.teiid.reddeer.editor.InputSetEditor;
import org.jboss.tools.teiid.reddeer.editor.MappingDiagramEditor;
import org.jboss.tools.teiid.reddeer.editor.ModelEditor;
import org.jboss.tools.teiid.reddeer.manager.ConnectionProfilesConstants;
import org.jboss.tools.teiid.reddeer.manager.ImportManager;
import org.jboss.tools.teiid.reddeer.manager.ModelExplorerManager;
import org.jboss.tools.teiid.reddeer.manager.VDBManager;
import org.jboss.tools.teiid.reddeer.perspective.TeiidPerspective;
import org.jboss.tools.teiid.reddeer.requirement.TeiidServerRequirement;
import org.jboss.tools.teiid.reddeer.requirement.TeiidServerRequirement.TeiidServer;
import org.jboss.tools.teiid.reddeer.view.ModelExplorer;
import org.jboss.tools.teiid.reddeer.wizard.MetadataModelWizard;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * @author skaleta
 */
@RunWith(RedDeerSuite.class)
@OpenPerspective(TeiidPerspective.class)
 @TeiidServer(state = ServerReqState.RUNNING, connectionProfiles = {
 ConnectionProfilesConstants.SQL_SERVER_2000_SEARCHB})
public class XmlStagingTableTest {
	private static final String PROJECT_NAME = "XmlStagingProject";

	 @InjectRequirement
	 private static TeiidServerRequirement teiidServer;

	private static Project project;

	@BeforeClass
	public static void importProject() {
		new WorkbenchShell().maximize();

		TeiidBot teiidBot = new TeiidBot();
		new ImportManager().importProject(teiidBot.toAbsolutePath("resources/projects/" + PROJECT_NAME));
		project = teiidBot.modelExplorer().getProject(PROJECT_NAME);
		project.refresh();

		 new ModelExplorer().changeConnectionProfile(ConnectionProfilesConstants.SQL_SERVER_2000_SEARCHB,
		 PROJECT_NAME, "TvGuideStaging.xmi");
	}

	@Test
	public void test() {
		// 1. Create an XML document model using a schema
		project.select();
		MetadataModelWizard modelWizard = new MetadataModelWizard();
		modelWizard.open();
		modelWizard.setModelName("SchemaModel")
				.selectModelClass(ModelClass.XML)
				.selectModelType(ModelType.VIEW)
				.selectModelBuilder(ModelBuilder.BUILD_FROM_XML_SCHEMA);
		modelWizard.next();
		modelWizard.selectXMLSchemaFile(PROJECT_NAME, "ProducersAndShowsSchema.xsd")
				.addElement("ResultSet");
		modelWizard.finish();

		// 2. Model XML document without staging table
		project.select();
		new DefaultTreeItem(PROJECT_NAME,"SchemaModel.xmi","ResultSetDocument").select();
		new ContextMenu("Rename...").select();
		new DefaultText().setText("TvGuideNoStaging");
		new SWTWorkbenchBot().activeShell().pressShortcut(Keystrokes.TAB);	
		AbstractWait.sleep(TimePeriod.SHORT);
		
		new ModelExplorerManager().getModelExplorerView().open(PROJECT_NAME,"SchemaModel.xmi","TvGuideNoStaging");
		MappingDiagramEditor mappingEditor = new MappingDiagramEditor("SchemaModel.xmi");
		ModelEditor editor = new ModelEditor("SchemaModel.xmi");		

		editor.showMappingTransformation("producer");
		editor.setTransformation("SELECT convert(ID, double) AS producerID, NAME "
				+ "FROM TvGuideStaging.PRODUCTION_HOUSES");
		editor.saveAndValidateSql();
		AbstractWait.sleep(TimePeriod.SHORT);
		new WorkbenchShell();

		AbstractWait.sleep(TimePeriod.SHORT);
		editor.clickButtonOnToolbar("Show Parent Diagram");	
		AbstractWait.sleep(TimePeriod.SHORT);
		
		editor.selectParts(mappingEditor.getMappingClasses("shows"));
		new ContextMenu("Open").select();
		
		editor.openInputSetEditor(true);
 		InputSetEditor inputSetEditor = new InputSetEditor();
 		inputSetEditor.createNewInputParam(new String[]{"producer","producerID : double"});
 		inputSetEditor.close();
 		
 		mappingEditor.showTransformation();
 		AbstractWait.sleep(TimePeriod.SHORT);
 		new WorkbenchShell();
 		editor.setTransformation("SELECT convert(TvGuideStaging.AVAILABLE_PROGRAMS.ID, double) "
 				+ "AS programId, TvGuideStaging.AVAILABLE_PROGRAMS.NAME AS programName, TvGuideStaging.AVAILABLE_PROGRAMS.RATING "
 				+ "FROM TvGuideStaging.AVAILABLE_PROGRAMS "
 				+ "WHERE INPUTS.producerID = TvGuideStaging.AVAILABLE_PROGRAMS.PRODID "
 				+ "ORDER BY programId");
 		editor.saveAndValidateSql();
 		AbstractWait.sleep(TimePeriod.SHORT);
 		new WorkbenchShell();

		new ShellMenu("File","Save All").select();
		AbstractWait.sleep(TimePeriod.SHORT);
		assertTrue("There are validation errors", new ProblemsView().getProblems(ProblemType.ERROR).isEmpty());
		
		// 3. Model XML document with staging table
		project.select();
		new DefaultTreeItem(new String[]{PROJECT_NAME,"SchemaModel.xmi","TvGuideNoStaging"}).select();
		new ContextMenu("New Sibling","XML Document").select();
		new DefaultShell("Build XML Documents From XML Schema");
		new PushButton("...").click();
		new DefaultShell("Select an XML Schema");
		new DefaultTreeItem(PROJECT_NAME, "ProducersAndShowsSchema.xsd").select();
		new PushButton("OK").click();
		new DefaultTable().select("ResultSet");
		new PushButton(1).click();
		new FinishButton().click();
		new SWTWorkbenchBot().activeShell().pressShortcut(Keystrokes.TAB);
		AbstractWait.sleep(TimePeriod.SHORT);
		new ModelExplorerManager().renameItem(new String[]{PROJECT_NAME,"SchemaModel.xmi","ResultSetDocument"}, "TvGuideRootAll");
		
		AbstractWait.sleep(TimePeriod.SHORT);
		new DefaultTreeItem("ResultSet").select();
		editor.clickButtonOnToolbar("New Staging Table");
		AbstractWait.sleep(TimePeriod.SHORT);
		editor.getModelDiagram("ST_ResultSet", "Mapping Diagram").select();
		new ContextMenu("Open").select();
		
		mappingEditor.showTransformation();
		AbstractWait.sleep(TimePeriod.SHORT);
 		new WorkbenchShell();
		editor.setTransformation("SELECT TvGuideStaging.AVAILABLE_PROGRAMS.ID, TvGuideStaging.AVAILABLE_PROGRAMS.NAME AS showName, "
				+ "TvGuideStaging.AVAILABLE_PROGRAMS.LENGTH, TvGuideStaging.AVAILABLE_PROGRAMS.DESCRIPTION, TvGuideStaging.AVAILABLE_PROGRAMS.RATING, "
				+ "TvGuideStaging.AVAILABLE_PROGRAMS.BROADCASTCOUNT, TvGuideStaging.PRODUCTION_HOUSES.ID AS PRODID, "
				+ "TvGuideStaging.PRODUCTION_HOUSES.NAME AS ProducerName, TvGuideStaging.PRODUCTION_HOUSES.ADDRESS, TvGuideStaging.PRODUCTION_HOUSES.CITY, "
				+ "TvGuideStaging.PRODUCTION_HOUSES.POSTALCODE, TvGuideStaging.PRODUCTION_HOUSES.COUNTRYCODE "
				+ "FROM TVguideStaging.PRODUCTION_HOUSES LEFT OUTER JOIN TVguideStaging.AVAILABLE_PROGRAMS "
				+ "ON TvGuideStaging.AVAILABLE_PROGRAMS.PRODID = TvGuideStaging.PRODUCTION_HOUSES.ID");
		editor.saveAndValidateSql();
		AbstractWait.sleep(TimePeriod.SHORT);
		new WorkbenchShell();
		
		AbstractWait.sleep(TimePeriod.SHORT);
		editor.clickButtonOnToolbar("Show Parent Diagram");	
		AbstractWait.sleep(TimePeriod.SHORT);
		
		editor.selectParts(mappingEditor.getMappingClasses("producer"));
		new ContextMenu("Open").select();
		
 		for ( SWTBotGefEditPart part : mappingEditor.getRefArrows()){
			part.select();
			try {
				ContextMenu menu = new ContextMenu("Add Transformation Source(s)");
				if (menu.isEnabled()){
					menu.select();	
				}
			} catch (Exception e) {
				// continue
			}
		}
		
		mappingEditor.showTransformation();
		AbstractWait.sleep(TimePeriod.SHORT);
 		new WorkbenchShell();
 		editor.setTransformation("SELECT DISTINCT convert(SchemaModel.TvGuideRootAll.MappingClasses.ST_ResultSet.PRODID, double) AS producerID, "
 				+ "SchemaModel.TvGuideRootAll.MappingClasses.ST_ResultSet.ProducerName AS name "
 				+ "FROM SchemaModel.TvGuideRootAll.MappingClasses.ST_ResultSet");
		editor.saveAndValidateSql();
		AbstractWait.sleep(TimePeriod.SHORT);
		new WorkbenchShell();
		
		AbstractWait.sleep(TimePeriod.SHORT);
		editor.clickButtonOnToolbar("Show Parent Diagram");	
		AbstractWait.sleep(TimePeriod.SHORT);
		
		editor.selectParts(mappingEditor.getMappingClasses("shows"));
		new ContextMenu("Open").select();
		
		editor.openInputSetEditor(true);
 		inputSetEditor = new InputSetEditor();
 		inputSetEditor.createNewInputParam(new String[]{"producer","producerID : double"});
 		inputSetEditor.close();
 		
 		for ( SWTBotGefEditPart part : mappingEditor.getRefArrows()){
			part.select();
			try {
				ContextMenu menu = new ContextMenu("Add Transformation Source(s)");
				if (menu.isEnabled()){
					menu.select();
				}
			} catch (Exception e) {
				// continue
			}
		}
 		
 		mappingEditor.showTransformation();
 		AbstractWait.sleep(TimePeriod.SHORT);
 		new WorkbenchShell();
 		editor.setTransformation("SELECT convert(SchemaModel.TvGuideRootAll.MappingClasses.ST_ResultSet.ID, double) AS programId, "
 				+ "SchemaModel.TvGuideRootAll.MappingClasses.ST_ResultSet.showName AS programName, SchemaModel.TvGuideRootAll.MappingClasses.ST_ResultSet.RATING "
 				+ "FROM SchemaModel.TvGuideRootAll.MappingClasses.ST_ResultSet "
 				+ "WHERE INPUTS.producerID = SchemaModel.TvGuideRootAll.MappingClasses.ST_ResultSet.PRODID");
 		editor.saveAndValidateSql();
 		AbstractWait.sleep(TimePeriod.SHORT);
 		new WorkbenchShell();

		new ShellMenu("File","Save All").select();
		AbstractWait.sleep(TimePeriod.SHORT);
		assertTrue("There are validation errors", new ProblemsView().getProblems(ProblemType.ERROR).isEmpty());

		// 4.Create a VDB, deploy  XmlStagingVdb
		VDBManager mgr = new VDBManager();
		String vdbName = "XmlStagingVdb";
		mgr.createVDB(PROJECT_NAME, vdbName);
		mgr.addModelsToVDB(PROJECT_NAME, vdbName, "SchemaModel.xmi");
		mgr.deployVDB(new String[]{PROJECT_NAME, vdbName});
		
		// 5. Test the created models
		TeiidJDBCHelper jdbchelper = new TeiidJDBCHelper(teiidServer, vdbName); 
		
		try {
			ResultSet withStRs = jdbchelper.executeQuery("SELECT * FROM TvGuideRootAll ORDER BY producerID, programID");
			withStRs.next(); 			
            String outputWithST = withStRs.getString(1); 
			ResultSet withoutStRs = jdbchelper.executeQuery("SELECT * FROM TvGuideNoStaging ORDER BY producerID, programID");
			withoutStRs.next(); 			
            String outputWithoutST = withoutStRs.getString(1); 
            assertEquals(outputWithoutST, outputWithST);
		} catch (SQLException e) {
			fail(e.getMessage());
		}
		
		try {
			ResultSet withStRs = jdbchelper.executeQuery("SELECT * FROM TvGuideRootAll WHERE programName LIKE '%Project%' ORDER BY producerID, programID");
			withStRs.next(); 			
            String outputWithST = withStRs.getString(1); 
			ResultSet withoutStRs = jdbchelper.executeQuery("SELECT * FROM TvGuideNoStaging WHERE programName LIKE '%Project%' ORDER BY producerID, programID");
			withoutStRs.next(); 			
            String outputWithoutST = withoutStRs.getString(1); 
            assertEquals(outputWithoutST, outputWithST);
		} catch (SQLException e) {
			fail(e.getMessage());
		}	
	}
}
