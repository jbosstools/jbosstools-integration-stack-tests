package org.jboss.tools.teiid.ui.bot.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.jboss.reddeer.common.wait.AbstractWait;
import org.jboss.reddeer.common.wait.TimePeriod;
import org.jboss.reddeer.eclipse.ui.problems.ProblemsView;
import org.jboss.reddeer.eclipse.ui.problems.ProblemsView.ProblemType;
import org.jboss.reddeer.junit.requirement.inject.InjectRequirement;
import org.jboss.reddeer.junit.runner.RedDeerSuite;
import org.jboss.reddeer.requirements.openperspective.OpenPerspectiveRequirement.OpenPerspective;
import org.jboss.reddeer.requirements.server.ServerReqState;
import org.jboss.reddeer.workbench.impl.shell.WorkbenchShell;
import org.jboss.tools.teiid.reddeer.ChildType;
import org.jboss.tools.teiid.reddeer.ModelBuilder;
import org.jboss.tools.teiid.reddeer.ModelClass;
import org.jboss.tools.teiid.reddeer.ModelType;
import org.jboss.tools.teiid.reddeer.connection.ConnectionProfileConstants;
import org.jboss.tools.teiid.reddeer.connection.TeiidJDBCHelper;
import org.jboss.tools.teiid.reddeer.dialog.InputSetEditorDialog;
import org.jboss.tools.teiid.reddeer.dialog.XmlDocumentBuilderDialog;
import org.jboss.tools.teiid.reddeer.editor.TransformationEditor;
import org.jboss.tools.teiid.reddeer.editor.XmlModelEditor;
import org.jboss.tools.teiid.reddeer.perspective.TeiidPerspective;
import org.jboss.tools.teiid.reddeer.requirement.TeiidServerRequirement;
import org.jboss.tools.teiid.reddeer.requirement.TeiidServerRequirement.TeiidServer;
import org.jboss.tools.teiid.reddeer.view.ModelExplorer;
import org.jboss.tools.teiid.reddeer.wizard.MetadataModelWizard;
import org.jboss.tools.teiid.reddeer.wizard.VdbWizard;
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
	private static final String VIEW_MODEL = "SchemaModel.xmi";
	private static final String VDB_NAME = "XmlStagingVdb";
	
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
	public void test() throws Exception {
		// 1. Create an XML document model using a schema
		MetadataModelWizard modelWizard = new MetadataModelWizard();
		modelWizard.open();
		modelWizard.setLocation(PROJECT_NAME, "views")
				.setModelName(VIEW_MODEL.substring(0, 11))
				.selectModelClass(ModelClass.XML)
				.selectModelType(ModelType.VIEW)
				.selectModelBuilder(ModelBuilder.BUILD_FROM_XML_SCHEMA);
		modelWizard.next();
		modelWizard.selectXMLSchemaFile(PROJECT_NAME, "schemas", "PublisherSchema.xsd")
				.addElement("ResultSet");
		modelWizard.finish();

		// 2. Model XML document without staging table
		XmlModelEditor editor = new XmlModelEditor(VIEW_MODEL);
		
		modelExplorer.renameModelItem(PROJECT_NAME + "/views/SchemaModel.xmi", "ResultSetDocument", "NoStagingDocument");
		
		editor.openMappingClass("publisher");
		TransformationEditor pubTransfEditor = editor.openTransformationEditor();
		pubTransfEditor.insertAndValidateSql("SELECT "
				+ "convert(Books.PUBLISHERS.PUBLISHER_ID, double) AS publisherId, Books.PUBLISHERS.NAME "
				+ "FROM Books.PUBLISHERS");
		pubTransfEditor.close();
		AbstractWait.sleep(TimePeriod.SHORT);
		editor.save();
		editor.returnToMappingClassOverview();
		
		editor.openMappingClass("book");
		InputSetEditorDialog inputSetEditor = editor.openInputSetEditor();
		inputSetEditor.addNewInputParam("publisher","publisherId : double");
		inputSetEditor.finish();
		TransformationEditor bookTransfEditor = editor.openTransformationEditor();
		bookTransfEditor.insertAndValidateSql("SELECT "
				+ "Books.BOOKS.ISBN AS isbn, Books.BOOKS.TITLE AS title "
 				+ "FROM Books.BOOKS "
 				+ "WHERE INPUTS.publisherId = Books.BOOKS.PUBLISHER "
 				+ "ORDER BY isbn");
		bookTransfEditor.close();
		AbstractWait.sleep(TimePeriod.SHORT);
		editor.save();
		editor.returnToMappingClassOverview();
		
		AbstractWait.sleep(TimePeriod.SHORT);
		assertTrue("Validation errors!", new ProblemsView().getProblems(ProblemType.ERROR).isEmpty());
		
		// 3. Model XML document with staging table
		modelExplorer.getProject(PROJECT_NAME).refresh();
		
		modelExplorer.addChildToModelItem(PROJECT_NAME + "/views/" + VIEW_MODEL, "", ChildType.XML_DOCUMENT);
		XmlDocumentBuilderDialog xmlDocumentBuilder = new XmlDocumentBuilderDialog();
		xmlDocumentBuilder.setSchema(PROJECT_NAME,"schemas","PublisherSchema.xsd")
				.addElement("ResultSet");
		xmlDocumentBuilder.finish();
		
		modelExplorer.renameModelItem(PROJECT_NAME + "/views/" + VIEW_MODEL, "ResultSetDocument", "StagingDocument");
		
		editor.createStagingTable("ResultSet");
		editor.openStagingTable("ST_ResultSet");
		TransformationEditor stTransfEditor = editor.openTransformationEditor();
		stTransfEditor.insertAndValidateSql("SELECT "
				+ "Books.BOOKS.ISBN AS BookISBN, Books.BOOKS.TITLE AS BookTITLE, Books.BOOKS.SUBTITLE, "
				+ "Books.BOOKS.PUBLISH_YEAR, Books.BOOKS.EDITION, Books.BOOKS.TYPE, Books.PUBLISHERS.PUBLISHER_ID, "
				+ "Books.PUBLISHERS.NAME AS PublisherName, Books.PUBLISHERS.LOCATION AS PublisherLocation "
				+ "FROM Books.PUBLISHERS LEFT OUTER JOIN Books.BOOKS ON Books.PUBLISHERS.PUBLISHER_ID = Books.BOOKS.PUBLISHER");
		stTransfEditor.close();
		AbstractWait.sleep(TimePeriod.SHORT);
		editor.save();
		editor.returnToMappingClassOverview();

		editor.openMappingClass("publisher");		
		pubTransfEditor = editor.openTransformationEditor();
		pubTransfEditor.insertAndValidateSql("SELECT DISTINCT "
 				+ "convert(SchemaModel.StagingDocument.MappingClasses.ST_ResultSet.PUBLISHER_ID, double) AS publisherId, "
 				+ "SchemaModel.StagingDocument.MappingClasses.ST_ResultSet.PublisherName AS name "
 				+ "FROM SchemaModel.StagingDocument.MappingClasses.ST_ResultSet");
		pubTransfEditor.close();
		AbstractWait.sleep(TimePeriod.SHORT);
		editor.save();
		editor.returnToMappingClassOverview();
 		
		editor.openMappingClass("book");
		inputSetEditor = editor.openInputSetEditor();
		inputSetEditor.addNewInputParam("publisher","publisherId : double");
		inputSetEditor.finish();
		bookTransfEditor = editor.openTransformationEditor();
		bookTransfEditor.insertAndValidateSql("SELECT "
 				+ "SchemaModel.StagingDocument.MappingClasses.ST_ResultSet.BookISBN AS isbn, "
 				+ "SchemaModel.StagingDocument.MappingClasses.ST_ResultSet.BookTITLE AS title "
 				+ "FROM SchemaModel.StagingDocument.MappingClasses.ST_ResultSet "
 				+ "WHERE INPUTS.publisherId = SchemaModel.StagingDocument.MappingClasses.ST_ResultSet.PUBLISHER_ID");
		bookTransfEditor.close();
		AbstractWait.sleep(TimePeriod.SHORT);
		editor.save();
		editor.returnToMappingClassOverview();

		AbstractWait.sleep(TimePeriod.SHORT);
		assertTrue("Validation errors!", new ProblemsView().getProblems(ProblemType.ERROR).isEmpty());

		// 4.Create a VDB and deploy
		VdbWizard vdbWizard = new VdbWizard();
		vdbWizard.open();
		vdbWizard.setLocation(PROJECT_NAME)
				.setName(VDB_NAME)
				.addModel(PROJECT_NAME, "views", VIEW_MODEL);
		vdbWizard.finish();
		
		modelExplorer.deployVdb(PROJECT_NAME, VDB_NAME);
		
		// 5. Test created models
		TeiidJDBCHelper jdbchelper = new TeiidJDBCHelper(teiidServer, VDB_NAME); 
		
		String outputWithSt = jdbchelper.executeQueryWithXmlStringResult("SELECT * FROM StagingDocument ORDER BY publisherId, isbn");
		String outputWithoutSt = jdbchelper.executeQueryWithXmlStringResult("SELECT * FROM NoStagingDocument ORDER BY publisherId, isbn");
		assertEquals(outputWithSt, outputWithoutSt);

		outputWithSt = jdbchelper.executeQueryWithXmlStringResult("SELECT * FROM StagingDocument WHERE title LIKE '%Software%' ORDER BY publisherId, isbn"); 
		outputWithoutSt = jdbchelper.executeQueryWithXmlStringResult("SELECT * FROM NoStagingDocument WHERE title LIKE '%Software%' ORDER BY publisherId, isbn");
		assertEquals(outputWithSt, outputWithoutSt);
	}
}
