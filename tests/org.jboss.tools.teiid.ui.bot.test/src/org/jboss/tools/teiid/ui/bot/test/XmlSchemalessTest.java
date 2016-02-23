package org.jboss.tools.teiid.ui.bot.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

import org.eclipse.swtbot.eclipse.finder.SWTWorkbenchBot;
import org.eclipse.swtbot.swt.finder.keyboard.Keystrokes;
import org.jboss.reddeer.common.wait.AbstractWait;
import org.jboss.reddeer.common.wait.TimePeriod;
import org.jboss.reddeer.eclipse.core.resources.Project;
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
import org.jboss.reddeer.workbench.impl.shell.WorkbenchShell;
import org.jboss.reddeer.workbench.impl.view.WorkbenchView;
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
@TeiidServer(state = ServerReqState.RUNNING, connectionProfiles = { ConnectionProfilesConstants.ORACLE_11G_BOOKS})
public class XmlSchemalessTest {
	private static final String PROJECT_NAME = "XmlSchemalessProject";
	private static final String VDB_NAME = "XmlSchemalessVdb";
	
	@InjectRequirement
	private static TeiidServerRequirement teiidServer;
	
	private static Project project;
	private ModelExplorerManager modelExplorerManager = new ModelExplorerManager();
	
	@BeforeClass
	public static void importProject(){
		new WorkbenchShell().maximize();
		
		TeiidBot teiidBot = new TeiidBot();
		new ImportManager().importProject(teiidBot.toAbsolutePath("resources/projects/" + PROJECT_NAME));
		project = teiidBot.modelExplorer().getProject(PROJECT_NAME);
		project.refresh();
		
		new ModelExplorer().changeConnectionProfile(ConnectionProfilesConstants.ORACLE_11G_BOOKS, PROJECT_NAME, "Books.xmi");
	}
	
	@Test
	public void test(){
		// 1. Create an XML document model
		project.select();
		MetadataModelWizard modelWizard = new MetadataModelWizard();
		modelWizard.open();
		modelWizard.activate();
		modelWizard.setModelName("BooksXml");
		modelWizard.selectModelClass(ModelClass.XML);
		modelWizard.selectModelType(ModelType.VIEW);
		modelWizard.finish();	
		modelExplorerManager.getModelExplorerView().open(PROJECT_NAME,"BooksXml.xmi");
		modelExplorerManager.addChildToItem(new String[]{PROJECT_NAME,"BooksXml.xmi"}, "XML Document");
		new DefaultShell("Build XML Documents From XML Schema");
		new FinishButton().click();
		new SWTWorkbenchBot().activeShell().pressShortcut(Keystrokes.TAB);
		modelExplorerManager.renameItem(new String[]{PROJECT_NAME,"BooksXml.xmi","NewXMLDocument"}, "bookListingDocument");
		AbstractWait.sleep(TimePeriod.SHORT);
		project.refresh();
		new ShellMenu("File","Save All").select();
		
		// 2. Model the document structure
		String xmlStructureBuilt = "XmlSchemalessProject/BooksXml.xmi/bookListingDocument";
		modelExplorerManager.renameItem((xmlStructureBuilt + "/Root_Element").split("/"), "bookListing");
		xmlStructureBuilt += "/bookListing";
		
		modelExplorerManager.addChildToItem(xmlStructureBuilt.split("/"), "Namespace Declaration");
		new DefaultTreeItem(xmlStructureBuilt.split("/")).doubleClick();
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

		modelExplorerManager.addChildToItem(xmlStructureBuilt.split("/"), "sequence");
		xmlStructureBuilt += "/sequence";
		
		modelExplorerManager.addChildToItem(xmlStructureBuilt.split("/"), "Element");
		modelExplorerManager.renameItem((xmlStructureBuilt + "/NewXmlElement").split("/"), "book");
		xmlStructureBuilt += "/book";
		
		modelExplorerManager.addChildToItem(xmlStructureBuilt.split("/"), "sequence");
		xmlStructureBuilt += "/sequence";
		
		modelExplorerManager.addChildToItem(xmlStructureBuilt.split("/"), "Element");
		modelExplorerManager.renameItem((xmlStructureBuilt + "/NewXmlElement").split("/"), "isbn");
		
		modelExplorerManager.addChildToItem(xmlStructureBuilt.split("/"), "Element");
		modelExplorerManager.renameItem((xmlStructureBuilt + "/NewXmlElement").split("/"), "title");
		
		modelExplorerManager.addChildToItem(xmlStructureBuilt.split("/"), "Element");
		modelExplorerManager.renameItem((xmlStructureBuilt + "/NewXmlElement").split("/"), "edition");
		
		modelExplorerManager.addChildToItem(xmlStructureBuilt.split("/"), "Element");
		modelExplorerManager.renameItem((xmlStructureBuilt + "/NewXmlElement").split("/"), "publisherName");
		
		modelExplorerManager.addChildToItem(xmlStructureBuilt.split("/"), "Element");
		modelExplorerManager.renameItem((xmlStructureBuilt + "/NewXmlElement").split("/"), "publisherLocation");
		
		project.refresh();
		AbstractWait.sleep(TimePeriod.SHORT);
		new ShellMenu("File","Save All").select();
		
		// 3. Define the transformation
		project.select();
		new ModelExplorerManager().getModelExplorerView().open(PROJECT_NAME,"BooksXml.xmi","bookListingDocument");
		MappingDiagramEditor mappingEditor = new MappingDiagramEditor("BooksXml.xmi");
		ModelEditor editor = new ModelEditor("BooksXml.xmi");
		
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
		
		project.refresh();
		AbstractWait.sleep(TimePeriod.SHORT);
		new ShellMenu("File","Save All").select();
		
		// 4. Create a VDB and query
		VDBManager mgr = new VDBManager(); 
		mgr.createVDB(PROJECT_NAME, VDB_NAME);
		mgr.addModelsToVDB(PROJECT_NAME, VDB_NAME, new String[]{"BooksXml.xmi"});
		mgr.deployVDB(new String[]{PROJECT_NAME, VDB_NAME});
		
		TeiidJDBCHelper jdbchelper = new TeiidJDBCHelper(teiidServer, VDB_NAME);
		
		String output = null;
		try {
			ResultSet rs = jdbchelper.executeQuery("SELECT * FROM bookListingDocument");
			rs.next();			
            output = rs.getString(1);
		} catch (SQLException e) {
			fail(e.getMessage());
		}
		
		String expectedOutput = null;	
		try {			
			TransformerFactory tFactory = TransformerFactory.newInstance();
            Transformer transformer = tFactory.newTransformer();
            
            String pathToFile = "resources/expectedResults/forXmlSchemalessTest.xml";
            InputStream is = new FileInputStream(new File(new TeiidBot().toAbsolutePath(pathToFile)));
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
	    project.refresh();
 		new DefaultTreeItem(xmlStructureBuilt.split("/")).expand();
 		new DefaultTreeItem((xmlStructureBuilt + "/edition").split("/")).select();
 		new ContextMenu("New Sibling","Element").select();
 		new WorkbenchView("Model Explorer");
 		AbstractWait.sleep(TimePeriod.getCustom(2));
 		modelExplorerManager.renameItem((xmlStructureBuilt + "/NewXmlElement").split("/"), "authors");
 		xmlStructureBuilt += "/authors";
 		
 		modelExplorerManager.addChildToItem(xmlStructureBuilt.split("/"), "sequence");
 		xmlStructureBuilt += "/sequence";
 		
 		modelExplorerManager.addChildToItem(xmlStructureBuilt.split("/"), "Element");
 		modelExplorerManager.renameItem((xmlStructureBuilt + "/NewXmlElement").split("/"), "author");
 		xmlStructureBuilt += "/author";
 		
 		modelExplorerManager.addChildToItem(xmlStructureBuilt.split("/"), "sequence");
 		xmlStructureBuilt += "/sequence";
 		
 		modelExplorerManager.addChildToItem(xmlStructureBuilt.split("/"), "Element");
 		modelExplorerManager.renameItem((xmlStructureBuilt + "/NewXmlElement").split("/"), "lastName");
 		
 		modelExplorerManager.addChildToItem(xmlStructureBuilt.split("/"), "Element");
 		modelExplorerManager.renameItem((xmlStructureBuilt + "/NewXmlElement").split("/"), "firstName");
 		
 		project.refresh();
 		AbstractWait.sleep(TimePeriod.SHORT);
 		new ShellMenu("File","Save All").select();
 		
 		project.select();
 		new ModelExplorerManager().getModelExplorerView().open(PROJECT_NAME,"BooksXml.xmi","bookListingDocument");
 		AbstractWait.sleep(TimePeriod.SHORT);
 		new DefaultTreeItem("bookListing","sequence [MC: book]","book","sequence","authors","sequence").select();
 		editor.clickButtonOnToolbar("New Mapping Class");
 		AbstractWait.sleep(TimePeriod.SHORT);
 		mappingEditor.deleteAttribute("author", "author");
 		
 		editor.selectParts(mappingEditor.getMappingClasses("author")); 
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
 		
 		project.refresh();
 		AbstractWait.sleep(TimePeriod.SHORT);
 		new ShellMenu("File","Save All").select();
 		
 		//6. Query the expanded model
 		mgr.synchronizeAll(PROJECT_NAME, VDB_NAME);
 		mgr.deployVDB(new String[]{PROJECT_NAME, VDB_NAME});
 		
 		jdbchelper = new TeiidJDBCHelper(teiidServer, VDB_NAME);
 		
 		output = null;
 		try {
 			ResultSet rs = jdbchelper.executeQuery("SELECT * FROM bookListingDocument");
 			rs.next(); 			
            output = rs.getString(1);           
 		} catch (SQLException e) {
 			fail(e.getMessage());
 		}	
 		
 		expectedOutput = null;	
 		try {			
 			TransformerFactory tFactory = TransformerFactory.newInstance();
             Transformer transformer = tFactory.newTransformer();
             
             String pathToFile = "resources/expectedResults/forXmlSchemalessTest2.xml";
             InputStream is = new FileInputStream(new File(new TeiidBot().toAbsolutePath(pathToFile)));
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
