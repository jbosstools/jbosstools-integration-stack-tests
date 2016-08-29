package org.jboss.tools.teiid.ui.bot.test;

import static org.junit.Assert.assertEquals;

import java.sql.SQLException;

import org.jboss.reddeer.common.wait.AbstractWait;
import org.jboss.reddeer.common.wait.TimePeriod;
import org.jboss.reddeer.junit.requirement.inject.InjectRequirement;
import org.jboss.reddeer.junit.runner.RedDeerSuite;
import org.jboss.reddeer.requirements.openperspective.OpenPerspectiveRequirement.OpenPerspective;
import org.jboss.reddeer.requirements.server.ServerReqState;
import org.jboss.reddeer.workbench.impl.shell.WorkbenchShell;
import org.jboss.tools.teiid.reddeer.ChildType;
import org.jboss.tools.teiid.reddeer.ModelClass;
import org.jboss.tools.teiid.reddeer.ModelType;
import org.jboss.tools.teiid.reddeer.connection.ConnectionProfileConstants;
import org.jboss.tools.teiid.reddeer.connection.ResourceFileHelper;
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
import org.jboss.tools.teiid.reddeer.view.ProblemsViewEx;
import org.jboss.tools.teiid.reddeer.wizard.newWizard.MetadataModelWizard;
import org.jboss.tools.teiid.reddeer.wizard.newWizard.VdbWizard;
import org.junit.Before;
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
	
	private ModelExplorer modelExplorer;
	private ResourceFileHelper fileHelper;
	private TeiidJDBCHelper jdbcHelper;
	
	@Before
	public  void importProject() {
		new WorkbenchShell().maximize();
		modelExplorer = new ModelExplorer();
		modelExplorer.importProject(PROJECT_NAME);
		modelExplorer.getProject(PROJECT_NAME).refresh();
		modelExplorer.changeConnectionProfile(ConnectionProfileConstants.ORACLE_11G_BOOKS, PROJECT_NAME, "Books.xmi");
		fileHelper = new ResourceFileHelper();
		jdbcHelper = new TeiidJDBCHelper(teiidServer, VDB_NAME);
	}
	
	@Test
	public void test() throws SQLException {
		// 1. Create an XML document model
		MetadataModelWizard.openWizard()
				.setModelName(VIEW_MODEL.substring(0,8))
				.selectModelClass(ModelClass.XML)
		        .selectModelType(ModelType.VIEW)
				.finish();	
		
		String modelPath = PROJECT_NAME + "/" + VIEW_MODEL;
		modelExplorer.openModelEditor(modelPath.split("/"));
		XmlModelEditor editor = new XmlModelEditor(VIEW_MODEL);
		
		modelExplorer.addChildToModelItem(ChildType.XML_DOCUMENT, modelPath.split("/"));
		XmlDocumentBuilderDialog xmlDocumentBuilder = new XmlDocumentBuilderDialog();
		xmlDocumentBuilder.finish();
		modelExplorer.renameModelItem("bookListingDocument", (modelPath+"/NewXMLDocument").split("/"));
		
		AbstractWait.sleep(TimePeriod.SHORT);
		editor.save();
		
		// 2. Model the document structure
		String xmlStructureBuilt = "bookListingDocument";
		modelExplorer.renameModelItem("bookListing", (modelPath+"/"+xmlStructureBuilt+"/Root_Element").split("/"));
		xmlStructureBuilt += "/bookListing";
		
		modelExplorer.addChildToModelItem(ChildType.NAME_SPACE, (modelPath+"/"+xmlStructureBuilt).split("/"));

		editor.show();
		TableEditor tableEditor = editor.openTableEditor(XmlModelEditor.MAPPING_DIAGRAM);
		tableEditor.openTab(TableEditor.Tabs.XML_NAMESPACES);
		tableEditor.setCellText(0, "bookListing", "Prefix", "xsd");
		tableEditor.setCellText(0, "bookListing", "Uri", "http://www.w3.org/2001/XMLSchema");
		tableEditor.close();
		
		modelExplorer.addChildToModelItem(ChildType.SEQUENCE, (modelPath+"/"+xmlStructureBuilt).split("/"));
		xmlStructureBuilt += "/sequence";
		
		modelExplorer.addChildToModelItem(ChildType.ELEMENT, (modelPath+"/"+xmlStructureBuilt).split("/"));
		modelExplorer.renameModelItem("book", (modelPath+"/"+xmlStructureBuilt+"/NewXmlElement").split("/"));
		xmlStructureBuilt += "/book";
		
		modelExplorer.addChildToModelItem(ChildType.SEQUENCE, (modelPath+"/"+xmlStructureBuilt).split("/"));
		xmlStructureBuilt += "/sequence";
		
		modelExplorer.addChildToModelItem(ChildType.ELEMENT, (modelPath+"/"+xmlStructureBuilt).split("/"));
		modelExplorer.renameModelItem("isbn", (modelPath+"/"+xmlStructureBuilt+"/NewXmlElement").split("/"));
		
		modelExplorer.addChildToModelItem(ChildType.ELEMENT, (modelPath+"/"+xmlStructureBuilt).split("/"));
		modelExplorer.renameModelItem("title", (modelPath+"/"+xmlStructureBuilt+"/NewXmlElement").split("/"));
		
		modelExplorer.addChildToModelItem(ChildType.ELEMENT, (modelPath+"/"+xmlStructureBuilt).split("/"));
		modelExplorer.renameModelItem("edition", (modelPath+"/"+xmlStructureBuilt+"/NewXmlElement").split("/"));
		
		modelExplorer.addChildToModelItem(ChildType.ELEMENT, (modelPath+"/"+xmlStructureBuilt).split("/"));
		modelExplorer.renameModelItem("publisherName", (modelPath+"/"+xmlStructureBuilt+"/NewXmlElement").split("/"));
		
		modelExplorer.addChildToModelItem(ChildType.ELEMENT, (modelPath+"/"+xmlStructureBuilt).split("/"));
		modelExplorer.renameModelItem("publisherLocation", (modelPath+"/"+xmlStructureBuilt+"/NewXmlElement").split("/"));

		AbstractWait.sleep(TimePeriod.SHORT);
		editor.save();
		
		// 3. Define the transformation
		modelExplorer.getProject(PROJECT_NAME).refresh();
		editor.show();
		
		editor.createMappingClass("bookListing","sequence");
		editor.deleteAttribute("book", "book");
		editor.openMappingClass("book");
		TransformationEditor bookTransfEditor = editor.openTransformationEditor();
		bookTransfEditor.insertAndValidateSql(fileHelper.getSql("XmlSchemalessTest/Book"));
		bookTransfEditor.close();
		editor.returnToMappingClassOverview();
		
		AbstractWait.sleep(TimePeriod.SHORT);
		editor.save();

		new ProblemsViewEx().checkErrors();
		
		// 4. Create a VDB and query
		VdbWizard.openVdbWizard()
				.setLocation(PROJECT_NAME)
				.setName(VDB_NAME)
				.addModel(PROJECT_NAME, VIEW_MODEL)
				.finish();
		modelExplorer.deployVdb(PROJECT_NAME, VDB_NAME);
		
		String output = jdbcHelper.executeQueryWithXmlStringResult("SELECT * FROM bookListingDocument");
		String expectedOutput = fileHelper.getXml("XmlSchemalessTest/BuiltDocument");
		assertEquals(output, expectedOutput);
	    
	    // 5. Expand the document	    
	    modelExplorer.addSiblingToModelItem(ChildType.ELEMENT, (modelPath+"/"+xmlStructureBuilt+"/edition").split("/"));
 		modelExplorer.renameModelItem("authors", (modelPath+"/"+xmlStructureBuilt+"/NewXmlElement").split("/"));
 		xmlStructureBuilt += "/authors";
 		
 		modelExplorer.addChildToModelItem(ChildType.SEQUENCE, (modelPath+"/"+xmlStructureBuilt).split("/"));
 		xmlStructureBuilt += "/sequence";
 		
 		modelExplorer.addChildToModelItem(ChildType.ELEMENT, (modelPath+"/"+xmlStructureBuilt).split("/"));
 		modelExplorer.renameModelItem("author", (modelPath+"/"+xmlStructureBuilt+"/NewXmlElement").split("/"));
 		xmlStructureBuilt += "/author";
 		
 		modelExplorer.addChildToModelItem(ChildType.SEQUENCE, (modelPath+"/"+xmlStructureBuilt).split("/"));
 		xmlStructureBuilt += "/sequence";
 		
 		modelExplorer.addChildToModelItem(ChildType.ELEMENT, (modelPath+"/"+xmlStructureBuilt).split("/"));
 		modelExplorer.renameModelItem("lastName", (modelPath+"/"+xmlStructureBuilt+"/NewXmlElement").split("/"));
 		
 		modelExplorer.addChildToModelItem(ChildType.ELEMENT, (modelPath+"/"+xmlStructureBuilt).split("/"));
 		modelExplorer.renameModelItem("firstName", (modelPath+"/"+xmlStructureBuilt+"/NewXmlElement").split("/"));

 		AbstractWait.sleep(TimePeriod.SHORT);
 		editor.save();
		
		editor.createMappingClass("bookListing","sequence [MC: book]","book","sequence","authors","sequence");
		editor.deleteAttribute("author", "author");
		editor.openMappingClass("author");
		InputSetEditorDialog inputSetEditor = editor.openInputSetEditor();
		inputSetEditor.addNewInputParam("book","isbn : string");
		inputSetEditor.finish();
		TransformationEditor authTransfEditor = editor.openTransformationEditor();
		authTransfEditor.insertAndValidateSql(fileHelper.getSql("XmlSchemalessTest/Author"));
		authTransfEditor.close();
		editor.returnToMappingClassOverview();
		
		AbstractWait.sleep(TimePeriod.SHORT);
		editor.save();

		new ProblemsViewEx().checkErrors();
 		
 		//6. Query the expanded model
 		VDBEditor.getInstance(VDB_NAME).synchronizeAll();
 		modelExplorer.deployVdb(PROJECT_NAME, VDB_NAME);
 		
 		output = jdbcHelper.executeQueryWithXmlStringResult("SELECT * FROM bookListingDocument");
 		expectedOutput = fileHelper.getXml("XmlSchemalessTest/BuiltDocumentExtended");	
 	    assertEquals(output, expectedOutput); 	
	}
}
