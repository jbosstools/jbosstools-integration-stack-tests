package org.jboss.tools.teiid.ui.bot.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.jboss.reddeer.common.wait.AbstractWait;
import org.jboss.reddeer.common.wait.TimePeriod;
import org.jboss.reddeer.eclipse.ui.problems.ProblemsView;
import org.jboss.reddeer.eclipse.ui.problems.ProblemsView.ProblemType;
import org.jboss.reddeer.jface.viewers.CellEditor;
import org.jboss.reddeer.junit.requirement.inject.InjectRequirement;
import org.jboss.reddeer.junit.runner.RedDeerSuite;
import org.jboss.reddeer.requirements.openperspective.OpenPerspectiveRequirement.OpenPerspective;
import org.jboss.reddeer.requirements.server.ServerReqState;
import org.jboss.reddeer.swt.impl.tab.DefaultTabItem;
import org.jboss.reddeer.swt.impl.table.DefaultTable;
import org.jboss.reddeer.swt.impl.text.DefaultText;
import org.jboss.reddeer.workbench.impl.shell.WorkbenchShell;
import org.jboss.tools.teiid.reddeer.ChildType;
import org.jboss.tools.teiid.reddeer.ModelClass;
import org.jboss.tools.teiid.reddeer.ModelType;
import org.jboss.tools.teiid.reddeer.connection.ConnectionProfileConstants;
import org.jboss.tools.teiid.reddeer.connection.TeiidFileHelper;
import org.jboss.tools.teiid.reddeer.connection.TeiidJDBCHelper;
import org.jboss.tools.teiid.reddeer.dialog.InputSetEditorDialog;
import org.jboss.tools.teiid.reddeer.dialog.XmlDocumentBuilderDialog;
import org.jboss.tools.teiid.reddeer.editor.TableEditor;
import org.jboss.tools.teiid.reddeer.editor.TransformationEditor;
import org.jboss.tools.teiid.reddeer.editor.VDBEditor;
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
public class XmlSchemalessTest {
	private static final String PROJECT_NAME = "XmlSchemalessProject";
	private static final String VIEW_MODEL = "BooksXml.xmi";
	private static final String VDB_NAME = "XmlSchemalessVdb";
	
	@InjectRequirement
	private static TeiidServerRequirement teiidServer;
	
	private static ModelExplorer modelExplorer;
	
	@BeforeClass
	public static void importProject() {
		new WorkbenchShell().maximize();
		modelExplorer = new ModelExplorer();
		modelExplorer.importProject("resources/projects/" + PROJECT_NAME);
		modelExplorer.getProject(PROJECT_NAME).refresh();
		modelExplorer.changeConnectionProfile(ConnectionProfileConstants.ORACLE_11G_BOOKS, PROJECT_NAME, "Books.xmi");
	}
	
	@Test
	public void test() throws Exception {
		// 1. Create an XML document model
		MetadataModelWizard modelWizard = new MetadataModelWizard();
		modelWizard.open();
		modelWizard.setModelName(VIEW_MODEL.substring(0,8))
				.selectModelClass(ModelClass.XML)
		        .selectModelType(ModelType.VIEW);
		modelWizard.finish();	
		
		String modelPath = PROJECT_NAME + "/" + VIEW_MODEL;
		modelExplorer.openModelEditor(modelPath.split("/"));
		XmlModelEditor editor = new XmlModelEditor(VIEW_MODEL);
		
		modelExplorer.addChildToModelItem(modelPath, "", ChildType.XML_DOCUMENT);
		XmlDocumentBuilderDialog xmlDocumentBuilder = new XmlDocumentBuilderDialog();
		xmlDocumentBuilder.finish();
		modelExplorer.renameModelItem(modelPath,"NewXMLDocument", "bookListingDocument");
		
		AbstractWait.sleep(TimePeriod.SHORT);
		editor.save();
		
		// 2. Model the document structure
		String xmlStructureBuilt = "bookListingDocument";
		modelExplorer.renameModelItem(modelPath, xmlStructureBuilt + "/Root_Element", "bookListing");
		xmlStructureBuilt += "/bookListing";
		
		modelExplorer.addChildToModelItem(modelPath, xmlStructureBuilt, ChildType.NAME_SPACE);

		editor.show();
		TableEditor tableEditor = editor.openTableEditor(XmlModelEditor.MAPPING_DIAGRAM);
		//TODO begin: this move to TableEditor (after his implementation)
		new DefaultTabItem("Xml Namespaces").activate();
		new DefaultTable().getItem("bookListing").doubleClick(1);
		new DefaultText(new CellEditor(new DefaultTable().getItem("bookListing"),1)).setText("xsd");
		new DefaultTable().getItem("bookListing").click();
		AbstractWait.sleep(TimePeriod.SHORT);
		new DefaultTable().getItem("bookListing").doubleClick(2);
		new DefaultText(new CellEditor(new DefaultTable().getItem("bookListing"),2)).setText("http://www.w3.org/2001/XMLSchema");
		new DefaultTable().getItem("bookListing").click();
		//TODO end
		tableEditor.close();
		
		modelExplorer.addChildToModelItem(modelPath, xmlStructureBuilt, ChildType.SEQUENCE);
		xmlStructureBuilt += "/sequence";
		
		modelExplorer.addChildToModelItem(modelPath, xmlStructureBuilt, ChildType.ELEMENT);
		modelExplorer.renameModelItem(modelPath, xmlStructureBuilt + "/NewXmlElement", "book");
		xmlStructureBuilt += "/book";
		
		modelExplorer.addChildToModelItem(modelPath, xmlStructureBuilt, ChildType.SEQUENCE);
		xmlStructureBuilt += "/sequence";
		
		modelExplorer.addChildToModelItem(modelPath, xmlStructureBuilt, ChildType.ELEMENT);
		modelExplorer.renameModelItem(modelPath, xmlStructureBuilt + "/NewXmlElement", "isbn");
		
		modelExplorer.addChildToModelItem(modelPath, xmlStructureBuilt, ChildType.ELEMENT);
		modelExplorer.renameModelItem(modelPath, xmlStructureBuilt + "/NewXmlElement", "title");
		
		modelExplorer.addChildToModelItem(modelPath, xmlStructureBuilt, ChildType.ELEMENT);
		modelExplorer.renameModelItem(modelPath, xmlStructureBuilt + "/NewXmlElement", "edition");
		
		modelExplorer.addChildToModelItem(modelPath, xmlStructureBuilt, ChildType.ELEMENT);
		modelExplorer.renameModelItem(modelPath, xmlStructureBuilt + "/NewXmlElement", "publisherName");
		
		modelExplorer.addChildToModelItem(modelPath, xmlStructureBuilt, ChildType.ELEMENT);
		modelExplorer.renameModelItem(modelPath, xmlStructureBuilt + "/NewXmlElement", "publisherLocation");

		AbstractWait.sleep(TimePeriod.SHORT);
		editor.save();
		
		// 3. Define the transformation
		modelExplorer.getProject(PROJECT_NAME).refresh();
		editor.show();
		
		editor.createMappingClass("bookListing","sequence");
		editor.deleteAttribute("book", "book");
		editor.openMappingClass("book");
		TransformationEditor bookTransfEditor = editor.openTransformationEditor();
		bookTransfEditor.insertAndValidateSql("SELECT ISBN, TITLE, convert(EDITION, string) AS edition, NAME AS publisherName, LOCATION AS publisherLocation"
				+ " FROM Books.BOOKS, Books.PUBLISHERS WHERE PUBLISHER = PUBLISHER_ID");
		bookTransfEditor.close();
		AbstractWait.sleep(TimePeriod.SHORT);
		editor.save();
		editor.returnToMappingClassOverview();
		
		AbstractWait.sleep(TimePeriod.SHORT);
		assertTrue("Validation errors!", new ProblemsView().getProblems(ProblemType.ERROR).isEmpty());
		
		// 4. Create a VDB and query
		VdbWizard vdbWizard = new VdbWizard();
		vdbWizard.open();
		vdbWizard.setLocation(PROJECT_NAME)
				.setName(VDB_NAME)
				.addModel(PROJECT_NAME, VIEW_MODEL);
		vdbWizard.finish();
		
		modelExplorer.deployVdb(PROJECT_NAME, VDB_NAME);
		
		TeiidJDBCHelper jdbcHelper = new TeiidJDBCHelper(teiidServer, VDB_NAME);
		TeiidFileHelper fileHelper = new TeiidFileHelper();
		
		String output = jdbcHelper.executeQueryWithXmlStringResult("SELECT * FROM bookListingDocument");
		String expectedOutput = fileHelper.getXmlExpectedResult("forXmlSchemalessTest",false);
		assertEquals(output, expectedOutput);
	    
	    // 5. Expand the document	    
	    modelExplorer.addSiblingToModelItem(modelPath, xmlStructureBuilt + "/edition", ChildType.ELEMENT);
 		modelExplorer.renameModelItem(modelPath, xmlStructureBuilt + "/NewXmlElement", "authors");
 		xmlStructureBuilt += "/authors";
 		
 		modelExplorer.addChildToModelItem(modelPath, xmlStructureBuilt, ChildType.SEQUENCE);
 		xmlStructureBuilt += "/sequence";
 		
 		modelExplorer.addChildToModelItem(modelPath, xmlStructureBuilt, ChildType.ELEMENT);
 		modelExplorer.renameModelItem(modelPath, xmlStructureBuilt + "/NewXmlElement", "author");
 		xmlStructureBuilt += "/author";
 		
 		modelExplorer.addChildToModelItem(modelPath, xmlStructureBuilt, ChildType.SEQUENCE);
 		xmlStructureBuilt += "/sequence";
 		
 		modelExplorer.addChildToModelItem(modelPath, xmlStructureBuilt, ChildType.ELEMENT);
 		modelExplorer.renameModelItem(modelPath, xmlStructureBuilt + "/NewXmlElement", "lastName");
 		
 		modelExplorer.addChildToModelItem(modelPath, xmlStructureBuilt, ChildType.ELEMENT);
 		modelExplorer.renameModelItem(modelPath, xmlStructureBuilt + "/NewXmlElement", "firstName");

 		AbstractWait.sleep(TimePeriod.SHORT);
 		editor.save();
		
		editor.createMappingClass("bookListing","sequence [MC: book]","book","sequence","authors","sequence");
		editor.deleteAttribute("author", "author");
		editor.openMappingClass("author");
		InputSetEditorDialog inputSetEditor = editor.openInputSetEditor();
		inputSetEditor.addNewInputParam("book","isbn : string");
		inputSetEditor.finish();
		TransformationEditor authTransfEditor = editor.openTransformationEditor();
		authTransfEditor.insertAndValidateSql("SELECT LASTNAME, FIRSTNAME FROM Books.AUTHORS, Books.BOOK_AUTHORS WHERE" 
 				+ "(INPUTS.isbn = ISBN) AND (Books.AUTHORS.AUTHOR_ID = Books.BOOK_AUTHORS.AUTHOR_ID)");
		authTransfEditor.close();
		AbstractWait.sleep(TimePeriod.SHORT);
		editor.save();
		editor.returnToMappingClassOverview();
		
		AbstractWait.sleep(TimePeriod.SHORT);
		assertTrue("Validation errors!", new ProblemsView().getProblems(ProblemType.ERROR).isEmpty());
 		
 		//6. Query the expanded model
 		VDBEditor.getInstance(VDB_NAME).synchronizeAll();
 		modelExplorer.deployVdb(PROJECT_NAME, VDB_NAME);
 		
 		output = jdbcHelper.executeQueryWithXmlStringResult("SELECT * FROM bookListingDocument");
 		expectedOutput = fileHelper.getXmlExpectedResult("forXmlSchemalessTest2",false);	
 	    assertEquals(output, expectedOutput); 	
	}
}
