package org.jboss.tools.teiid.ui.bot.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.awt.event.KeyEvent;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

import org.jboss.reddeer.common.wait.AbstractWait;
import org.jboss.reddeer.common.wait.TimePeriod;
import org.jboss.reddeer.jface.viewers.CellEditor;
import org.jboss.reddeer.junit.requirement.inject.InjectRequirement;
import org.jboss.reddeer.junit.runner.RedDeerSuite;
import org.jboss.reddeer.requirements.openperspective.OpenPerspectiveRequirement.OpenPerspective;
import org.jboss.reddeer.requirements.server.ServerReqState;
import org.jboss.reddeer.swt.impl.button.FinishButton;
import org.jboss.reddeer.swt.impl.ctab.DefaultCTabItem;
import org.jboss.reddeer.swt.impl.menu.ContextMenu;
import org.jboss.reddeer.swt.impl.menu.ShellMenu;
import org.jboss.reddeer.swt.impl.shell.DefaultShell;
import org.jboss.reddeer.swt.impl.tab.DefaultTabItem;
import org.jboss.reddeer.swt.impl.table.DefaultTable;
import org.jboss.reddeer.swt.impl.text.DefaultText;
import org.jboss.reddeer.swt.impl.tree.DefaultTreeItem;
import org.jboss.reddeer.swt.keyboard.KeyboardFactory;
import org.jboss.reddeer.workbench.impl.shell.WorkbenchShell;
import org.jboss.reddeer.workbench.impl.view.WorkbenchView;
import org.jboss.tools.teiid.reddeer.ChildType;
import org.jboss.tools.teiid.reddeer.ModelClass;
import org.jboss.tools.teiid.reddeer.ModelType;
import org.jboss.tools.teiid.reddeer.connection.ConnectionProfileConstants;
import org.jboss.tools.teiid.reddeer.connection.TeiidJDBCHelper;
import org.jboss.tools.teiid.reddeer.editor.InputSetEditor;
import org.jboss.tools.teiid.reddeer.editor.MappingDiagramEditor;
import org.jboss.tools.teiid.reddeer.editor.ModelEditor;
import org.jboss.tools.teiid.reddeer.editor.VDBEditor;
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
public class XmlSchemalessTest {
	private static final String PROJECT_NAME = "XmlSchemalessProject";
	private static final String VDB_NAME = "XmlSchemalessVdb";
	private static final String MODEL_NAME = "BooksXml.xmi";
	
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
	public void test(){
		// 1. Create an XML document model
		MetadataModelWizard modelWizard = new MetadataModelWizard();
		modelWizard.open();
		modelWizard.activate();
		modelWizard.setModelName(MODEL_NAME.substring(0,8))
				.selectModelClass(ModelClass.XML)
		        .selectModelType(ModelType.VIEW);
		modelWizard.finish();	
		String modelPath = PROJECT_NAME + "/" + MODEL_NAME;
		modelExplorer.open(modelPath.split("/"));
		modelExplorer.addChildToModelItem(modelPath, "", ChildType.XML_DOCUMENT);
		new DefaultShell("Build XML Documents From XML Schema");
		new FinishButton().click();
		KeyboardFactory.getKeyboard().type(KeyEvent.VK_TAB);
		modelExplorer.renameModelItem(modelPath,"NewXMLDocument", "bookListingDocument");
		AbstractWait.sleep(TimePeriod.SHORT);
		modelExplorer.getProject(PROJECT_NAME).refresh();
		new ShellMenu("File","Save All").select();
		AbstractWait.sleep(TimePeriod.SHORT);
		
		// 2. Model the document structure
		String xmlStructureBuilt = "bookListingDocument";
		modelExplorer.renameModelItem(modelPath, xmlStructureBuilt + "/Root_Element", "bookListing");
		xmlStructureBuilt += "/bookListing";
		
		modelExplorer.addChildToModelItem(modelPath, xmlStructureBuilt, ChildType.NAME_SPACE);
		new DefaultTreeItem((modelPath + "/" + xmlStructureBuilt).split("/")).doubleClick();
		AbstractWait.sleep(TimePeriod.SHORT);
		new DefaultCTabItem("Table Editor").activate();
		new DefaultTabItem("Xml Namespaces").activate();
		new DefaultTable().getItem("bookListing").doubleClick(1);
		new DefaultText(new CellEditor(new DefaultTable().getItem("bookListing"),1)).setText("xsd");
		new DefaultTable().getItem("bookListing").click();
		AbstractWait.sleep(TimePeriod.SHORT);
		new DefaultTable().getItem("bookListing").doubleClick(2);
		new DefaultText(new CellEditor(new DefaultTable().getItem("bookListing"),2)).setText("http://www.w3.org/2001/XMLSchema");
		new DefaultTable().getItem("bookListing").click();
		AbstractWait.sleep(TimePeriod.SHORT);
		new DefaultCTabItem("Mapping Diagram").activate();

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
		
		modelExplorer.getProject(PROJECT_NAME).refresh();
		AbstractWait.sleep(TimePeriod.SHORT);
		new ShellMenu("File","Save All").select();
		AbstractWait.sleep(TimePeriod.SHORT);
		
		// 3. Define the transformation
		modelExplorer.getProject(PROJECT_NAME).refresh();
		modelExplorer.open(PROJECT_NAME, MODEL_NAME, "bookListingDocument");
		MappingDiagramEditor mappingEditor = new MappingDiagramEditor(MODEL_NAME);
		ModelEditor editor = new ModelEditor(MODEL_NAME);
		
		new DefaultTreeItem("bookListing","sequence").select();
		editor.clickButtonOnToolbar("New Mapping Class");
		AbstractWait.sleep(TimePeriod.SHORT);
		mappingEditor.deleteAttribute("book", "book");
		
		editor.selectParts(mappingEditor.getMappingClasses("book"));
		new ContextMenu("Open").select();
		mappingEditor.showTransformation();
		editor.setTransformation("SELECT ISBN, TITLE, convert(EDITION, string) AS edition, NAME AS publisherName, LOCATION AS publisherLocation"
				+ " FROM Books.BOOKS, Books.PUBLISHERS WHERE PUBLISHER = PUBLISHER_ID");
		editor.saveAndValidateSql();
		AbstractWait.sleep(TimePeriod.SHORT);
		new WorkbenchShell();
		
		modelExplorer.getProject(PROJECT_NAME).refresh();
		AbstractWait.sleep(TimePeriod.SHORT);
		new ShellMenu("File","Save All").select();
		
		// 4. Create a VDB and query
		VdbWizard vdbWizard = new VdbWizard();
		vdbWizard.open();
		vdbWizard.activate();
		vdbWizard.setLocation(PROJECT_NAME)
				.setName(VDB_NAME)
				.addModel(PROJECT_NAME, MODEL_NAME);
		vdbWizard.finish();
		
		modelExplorer.deployVdb(PROJECT_NAME, VDB_NAME);
		
		TeiidJDBCHelper jdbchelper = new TeiidJDBCHelper(teiidServer, VDB_NAME);
		
		String output = null;
		try {
			ResultSet rs = jdbchelper.executeQueryWithResultSet("SELECT * FROM bookListingDocument");
			rs.next();			
            output = rs.getString(1);
		} catch (SQLException e) {
			e.printStackTrace();
			fail(e.getMessage());
		}
		
		String expectedOutput = null;	
		try {			
			TransformerFactory tFactory = TransformerFactory.newInstance();
            Transformer transformer = tFactory.newTransformer();
            
            String pathToFile = "resources/expectedResults/forXmlSchemalessTest.xml";
            InputStream is = new FileInputStream(new File(pathToFile).getAbsolutePath());
            StreamSource source = new StreamSource(is);           
            StringWriter sw = new StringWriter();
            StreamResult result = new StreamResult(sw);
            transformer.transform(source, result);
            
            expectedOutput = sw.getBuffer().toString().replaceAll("\n|\t", "");            
		} catch (IOException | TransformerException e) {
			fail(e.getMessage());
		} 	
	  
	    assertEquals(output, expectedOutput); 	
	    
	    // 5. Expand the document	    
	    modelExplorer.getProject(PROJECT_NAME).refresh();
 		new DefaultTreeItem((modelPath + "/" + xmlStructureBuilt).split("/")).expand();
 		new DefaultTreeItem((modelPath + "/" + xmlStructureBuilt + "/edition").split("/")).select();
 		new ContextMenu("New Sibling","Element").select();
 		new WorkbenchView("Model Explorer");
 		AbstractWait.sleep(TimePeriod.getCustom(2));
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
 		
 		modelExplorer.getProject(PROJECT_NAME).refresh();
 		AbstractWait.sleep(TimePeriod.SHORT);
 		new ShellMenu("File","Save All").select();
 		
 		modelExplorer.getProject(PROJECT_NAME).refresh();
 		modelExplorer.open(PROJECT_NAME,MODEL_NAME,"bookListingDocument");
 		AbstractWait.sleep(TimePeriod.SHORT);
 		new DefaultTreeItem("bookListing","sequence [MC: book]","book","sequence","authors","sequence").select();
 		editor.clickButtonOnToolbar("New Mapping Class");
 		AbstractWait.sleep(TimePeriod.SHORT);
 		mappingEditor.deleteAttribute("author", "author");
 		AbstractWait.sleep(TimePeriod.SHORT);
 		editor.selectParts(mappingEditor.getMappingClasses("author"));
 		AbstractWait.sleep(TimePeriod.SHORT);
 		new ContextMenu("Open").select();
 		
 		editor.openInputSetEditor(true);
 		InputSetEditor inputSetEditor = new InputSetEditor();
 		inputSetEditor.createNewInputParam(new String[]{"book","isbn : string"});
 		inputSetEditor.close();
 		
 		mappingEditor.showTransformation();
 		AbstractWait.sleep(TimePeriod.SHORT);
 		new WorkbenchShell();
 		editor.setTransformation("SELECT LASTNAME, FIRSTNAME FROM Books.AUTHORS, Books.BOOK_AUTHORS WHERE" 
 				+ "(INPUTS.isbn = ISBN) AND (Books.AUTHORS.AUTHOR_ID = Books.BOOK_AUTHORS.AUTHOR_ID)");
 		editor.saveAndValidateSql();
 		AbstractWait.sleep(TimePeriod.SHORT);
 		new WorkbenchShell();
 		
 		modelExplorer.getProject(PROJECT_NAME).refresh();
 		AbstractWait.sleep(TimePeriod.SHORT);
 		new ShellMenu("File","Save All").select();
 		AbstractWait.sleep(TimePeriod.SHORT);
 		
 		//6. Query the expanded model
 		VDBEditor.getInstance(VDB_NAME).synchronizeAll();
 		AbstractWait.sleep(TimePeriod.SHORT);
 		modelExplorer.deployVdb(PROJECT_NAME, VDB_NAME);
 		
 		jdbchelper = new TeiidJDBCHelper(teiidServer, VDB_NAME);
 		
 		output = null;
 		try {
 			ResultSet rs = jdbchelper.executeQueryWithResultSet("SELECT * FROM bookListingDocument");
 			rs.next(); 			
            output = rs.getString(1);           
 		} catch (SQLException e) {
 			e.printStackTrace();
 			fail(e.getMessage());
 		}	
 		
 		expectedOutput = null;	
 		try {			
 			TransformerFactory tFactory = TransformerFactory.newInstance();
             Transformer transformer = tFactory.newTransformer();
             
             String pathToFile = "resources/expectedResults/forXmlSchemalessTest2.xml";
             InputStream is = new FileInputStream(new File(pathToFile).getAbsolutePath());
             StreamSource source = new StreamSource(is);         
             StringWriter sw = new StringWriter();
             StreamResult result = new StreamResult(sw);             
             transformer.transform(source, result);
            
             expectedOutput = sw.getBuffer().toString().replaceAll("\n|\t", "");             
 		} catch (IOException | TransformerException e) {
 			fail(e.getMessage());
 		} 	
 	  
 	    assertEquals(output, expectedOutput); 	
	}
}
