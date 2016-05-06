package org.jboss.tools.teiid.ui.bot.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.awt.event.KeyEvent;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.eclipse.swtbot.eclipse.gef.finder.widgets.SWTBotGefEditPart;
import org.jboss.reddeer.common.wait.AbstractWait;
import org.jboss.reddeer.common.wait.TimePeriod;
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
import org.jboss.reddeer.swt.impl.tree.DefaultTreeItem;
import org.jboss.reddeer.swt.keyboard.KeyboardFactory;
import org.jboss.reddeer.workbench.impl.shell.WorkbenchShell;
import org.jboss.tools.teiid.reddeer.ModelBuilder;
import org.jboss.tools.teiid.reddeer.ModelClass;
import org.jboss.tools.teiid.reddeer.ModelType;
import org.jboss.tools.teiid.reddeer.connection.ConnectionProfileConstants;
import org.jboss.tools.teiid.reddeer.connection.TeiidJDBCHelper;
import org.jboss.tools.teiid.reddeer.editor.InputSetEditor;
import org.jboss.tools.teiid.reddeer.editor.MappingDiagramEditor;
import org.jboss.tools.teiid.reddeer.editor.ModelEditor;
import org.jboss.tools.teiid.reddeer.perspective.TeiidPerspective;
import org.jboss.tools.teiid.reddeer.requirement.TeiidServerRequirement;
import org.jboss.tools.teiid.reddeer.requirement.TeiidServerRequirement.TeiidServer;
import org.jboss.tools.teiid.reddeer.view.ModelExplorer;
import org.jboss.tools.teiid.reddeer.wizard.MetadataModelWizard;
import org.jboss.tools.teiid.reddeer.wizard.VdbWizard;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * @author skaleta
 */
@RunWith(RedDeerSuite.class)
@OpenPerspective(TeiidPerspective.class)
@TeiidServer(state = ServerReqState.RUNNING, connectionProfiles = {ConnectionProfileConstants.ORACLE_11G_BOOKS})
public class XmlStagingTableTest {
	private static final String PROJECT_NAME = "XmlStagingTableProject";

	@InjectRequirement
	private static TeiidServerRequirement teiidServer;

	private static ModelExplorer modelExplorer;

	@BeforeClass
	public static void importProject() {
		new WorkbenchShell().maximize();
		modelExplorer = new ModelExplorer();
		modelExplorer.importProject("resources/projects/" + PROJECT_NAME);
		modelExplorer.getProject(PROJECT_NAME).refresh();
		modelExplorer.changeConnectionProfile(ConnectionProfileConstants.ORACLE_11G_BOOKS, PROJECT_NAME, "sources", "Books.xmi");
	}

	@Test
	public void test() {
		// 1. Create an XML document model using a schema
		MetadataModelWizard modelWizard = new MetadataModelWizard();
		modelWizard.open();
		modelWizard.setLocation(PROJECT_NAME, "views")
				.setModelName("SchemaModel")
				.selectModelClass(ModelClass.XML)
				.selectModelType(ModelType.VIEW)
				.selectModelBuilder(ModelBuilder.BUILD_FROM_XML_SCHEMA);
		modelWizard.next();
		modelWizard.selectXMLSchemaFile(PROJECT_NAME, "schemas", "PublisherSchema.xsd")
				.addElement("ResultSet");
		modelWizard.finish();

		// 2. Model XML document without staging table
		modelExplorer.renameModelItem(PROJECT_NAME + "/views/SchemaModel.xmi", "ResultSetDocument", "NoStagingDocument");
		
		modelExplorer.open(PROJECT_NAME,"views","SchemaModel.xmi","NoStagingDocument");
		MappingDiagramEditor mappingEditor = new MappingDiagramEditor("SchemaModel.xmi");
		ModelEditor editor = new ModelEditor("SchemaModel.xmi");		
		
		editor.selectParts(mappingEditor.getMappingClasses("publisher"));
		new ContextMenu("Open").select();
		mappingEditor.showTransformation();
		editor.setTransformation("SELECT "
				+ "convert(Books.PUBLISHERS.PUBLISHER_ID, double) AS publisherId, Books.PUBLISHERS.NAME "
				+ "FROM Books.PUBLISHERS");
		editor.saveAndValidateSql();
		AbstractWait.sleep(TimePeriod.SHORT);
		new WorkbenchShell();

		AbstractWait.sleep(TimePeriod.SHORT);
		editor.clickButtonOnToolbar("Show Parent Diagram");	
		AbstractWait.sleep(TimePeriod.SHORT);
		
		editor.selectParts(mappingEditor.getMappingClasses("book"));
		new ContextMenu("Open").select();
		
		editor.openInputSetEditor(true);
 		InputSetEditor inputSetEditor = new InputSetEditor();
 		inputSetEditor.createNewInputParam(new String[]{"publisher","publisherId : double"});
 		inputSetEditor.close();
 		
 		mappingEditor.showTransformation();
 		AbstractWait.sleep(TimePeriod.SHORT);
 		new WorkbenchShell();
 		editor.setTransformation("SELECT Books.BOOKS.ISBN AS isbn, Books.BOOKS.TITLE AS title "
 				+ "FROM Books.BOOKS "
 				+ "WHERE INPUTS.publisherId = Books.BOOKS.PUBLISHER "
 				+ "ORDER BY isbn ");
 		editor.saveAndValidateSql();
 		AbstractWait.sleep(TimePeriod.SHORT);
 		new WorkbenchShell();

 		AbstractWait.sleep(TimePeriod.SHORT);
		new ShellMenu("File","Save All").select();
		AbstractWait.sleep(TimePeriod.SHORT);
		assertTrue("There are validation errors", new ProblemsView().getProblems(ProblemType.ERROR).isEmpty());
		
		// 3. Model XML document with staging table
		modelExplorer.getProject(PROJECT_NAME).refresh();
		new DefaultTreeItem(PROJECT_NAME,"views","SchemaModel.xmi","NoStagingDocument").select();
		new ContextMenu("New Sibling","XML Document").select();
		new DefaultShell("Build XML Documents From XML Schema");
		new PushButton("...").click();
		new DefaultShell("Select an XML Schema");
		new DefaultTreeItem(PROJECT_NAME,"schemas","PublisherSchema.xsd").select();
		new PushButton("OK").click();
		new DefaultTable().select("ResultSet");
		new PushButton(1).click();
		new FinishButton().click();
		KeyboardFactory.getKeyboard().type(KeyEvent.VK_TAB);
		AbstractWait.sleep(TimePeriod.SHORT);
		modelExplorer.renameModelItem(PROJECT_NAME + "/views/SchemaModel.xmi", "ResultSetDocument", "StagingDocument");
		
		AbstractWait.sleep(TimePeriod.SHORT);
		new DefaultTreeItem("ResultSet").select();
		editor.clickButtonOnToolbar("New Staging Table");
		AbstractWait.sleep(TimePeriod.SHORT);
		editor.getModelDiagram("ST_ResultSet", "Mapping Diagram").select();
		new ContextMenu("Open").select();
		
		mappingEditor.showTransformation();
		AbstractWait.sleep(TimePeriod.SHORT);
 		new WorkbenchShell();
		editor.setTransformation("SELECT "
				+ "Books.BOOKS.ISBN AS BookISBN, Books.BOOKS.TITLE AS BookTITLE, Books.BOOKS.SUBTITLE, "
				+ "Books.BOOKS.PUBLISH_YEAR, Books.BOOKS.EDITION, Books.BOOKS.TYPE, Books.PUBLISHERS.PUBLISHER_ID, "
				+ "Books.PUBLISHERS.NAME AS PublisherName, Books.PUBLISHERS.LOCATION AS PublisherLocation "
				+ "FROM Books.PUBLISHERS LEFT OUTER JOIN Books.BOOKS ON Books.PUBLISHERS.PUBLISHER_ID = Books.BOOKS.PUBLISHER");
		editor.saveAndValidateSql();
		AbstractWait.sleep(TimePeriod.SHORT);
		new WorkbenchShell();
		
		AbstractWait.sleep(TimePeriod.SHORT);
		editor.clickButtonOnToolbar("Show Parent Diagram");	
		AbstractWait.sleep(TimePeriod.SHORT);
		
		editor.selectParts(mappingEditor.getMappingClasses("publisher"));
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
		
 		editor.show();
		mappingEditor.showTransformation();
		AbstractWait.sleep(TimePeriod.SHORT);
 		new WorkbenchShell();
 		editor.setTransformation("SELECT DISTINCT "
 				+ "convert(SchemaModel.StagingDocument.MappingClasses.ST_ResultSet.PUBLISHER_ID, double) AS publisherId, "
 				+ "SchemaModel.StagingDocument.MappingClasses.ST_ResultSet.PublisherName AS name "
 				+ "FROM SchemaModel.StagingDocument.MappingClasses.ST_ResultSet");
		editor.saveAndValidateSql();
		AbstractWait.sleep(TimePeriod.SHORT);
		new WorkbenchShell();
		
		AbstractWait.sleep(TimePeriod.SHORT);
		editor.clickButtonOnToolbar("Show Parent Diagram");	
		AbstractWait.sleep(TimePeriod.SHORT);
		
		editor.selectParts(mappingEditor.getMappingClasses("book"));
		new ContextMenu("Open").select();
		
		editor.openInputSetEditor(true);
 		inputSetEditor = new InputSetEditor();
 		inputSetEditor.createNewInputParam(new String[]{"publisher","publisherId : double"});
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
 		
 		editor.show();
 		mappingEditor.showTransformation();
 		AbstractWait.sleep(TimePeriod.SHORT);
 		new WorkbenchShell();
 		editor.setTransformation("SELECT "
 				+ "SchemaModel.StagingDocument.MappingClasses.ST_ResultSet.BookISBN AS isbn, "
 				+ "SchemaModel.StagingDocument.MappingClasses.ST_ResultSet.BookTITLE AS title "
 				+ "FROM SchemaModel.StagingDocument.MappingClasses.ST_ResultSet "
 				+ "WHERE INPUTS.publisherId = SchemaModel.StagingDocument.MappingClasses.ST_ResultSet.PUBLISHER_ID");
 		editor.saveAndValidateSql();
 		AbstractWait.sleep(TimePeriod.SHORT);
 		new WorkbenchShell();

		new ShellMenu("File","Save All").select();
		AbstractWait.sleep(TimePeriod.SHORT);
		assertTrue("There are validation errors", new ProblemsView().getProblems(ProblemType.ERROR).isEmpty());

		// 4.Create a VDB, deploy  XmlStagingVdb
		String vdbName = "XmlStagingVdb";
		
		VdbWizard vdbWizard = new VdbWizard();
		vdbWizard.open();
		vdbWizard.activate();
		vdbWizard.setLocation(PROJECT_NAME)
				.setName(vdbName)
				.addModel(PROJECT_NAME, "views", "SchemaModel.xmi");
		vdbWizard.finish();
		
		modelExplorer.deployVdb(PROJECT_NAME, vdbName);
		
		// 5. Test the created models
		TeiidJDBCHelper jdbchelper = new TeiidJDBCHelper(teiidServer, vdbName); 
		
		try {
			ResultSet withStRs = jdbchelper.executeQueryWithResultSet("SELECT * FROM NoStagingDocument ORDER BY publisherId, isbn");
			withStRs.next(); 			
            String outputWithST = withStRs.getString(1); 
			ResultSet withoutStRs = jdbchelper.executeQueryWithResultSet("SELECT * FROM StagingDocument ORDER BY publisherId, isbn");
			withoutStRs.next(); 			
            String outputWithoutST = withoutStRs.getString(1); 
            assertEquals(outputWithoutST, outputWithST);
		} catch (SQLException e) {
			fail(e.getMessage());
		}
		
		try {
			ResultSet withStRs = jdbchelper.executeQueryWithResultSet("SELECT * FROM StagingDocument WHERE title LIKE '%Software%' ORDER BY publisherId, isbn");
			withStRs.next(); 			
            String outputWithST = withStRs.getString(1); 
			ResultSet withoutStRs = jdbchelper.executeQueryWithResultSet("SELECT * FROM NoStagingDocument WHERE title LIKE '%Software%' ORDER BY publisherId, isbn");
			withoutStRs.next(); 			
            String outputWithoutST = withoutStRs.getString(1); 
            assertEquals(outputWithoutST, outputWithST);
		} catch (SQLException e) {
			fail(e.getMessage());
		}	
	}
}
