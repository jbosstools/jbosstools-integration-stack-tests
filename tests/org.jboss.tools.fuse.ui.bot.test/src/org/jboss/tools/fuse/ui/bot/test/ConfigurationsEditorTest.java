package org.jboss.tools.fuse.ui.bot.test;

import static org.junit.Assert.fail;

import java.io.ByteArrayInputStream;
import java.util.ArrayList;
import java.util.List;

import org.jboss.reddeer.common.logging.Logger;
import org.jboss.reddeer.eclipse.ui.perspectives.JavaEEPerspective;
import org.jboss.reddeer.junit.runner.RedDeerSuite;
import org.jboss.reddeer.requirements.autobuilding.AutoBuildingRequirement.AutoBuilding;
import org.jboss.reddeer.requirements.cleanworkspace.CleanWorkspaceRequirement.CleanWorkspace;
import org.jboss.reddeer.requirements.openperspective.OpenPerspectiveRequirement.OpenPerspective;
import org.jboss.reddeer.swt.impl.styledtext.DefaultStyledText;
import org.jboss.reddeer.swt.impl.text.LabeledText;
import org.jboss.reddeer.swt.impl.tree.DefaultTreeItem;
import org.jboss.reddeer.workbench.handler.EditorHandler;
import org.jboss.reddeer.workbench.impl.shell.WorkbenchShell;
import org.jboss.tools.common.reddeer.XPathEvaluator;
import org.jboss.tools.common.reddeer.view.ErrorLogView;
import org.jboss.tools.fuse.reddeer.ProjectType;
import org.jboss.tools.fuse.reddeer.editor.CamelDataFormatDialog;
import org.jboss.tools.fuse.reddeer.editor.CamelEditor;
import org.jboss.tools.fuse.reddeer.editor.CamelEndpointDialog;
import org.jboss.tools.fuse.reddeer.editor.ConfigurationsEditor;
import org.jboss.tools.fuse.reddeer.editor.ConfigurationsEditor.Element;
import org.jboss.tools.fuse.ui.bot.test.utils.ProjectFactory;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * Tests for global elements in Configurations Tab in Camel editor
 *  
 * @author djelinek
 */
@CleanWorkspace
@OpenPerspective(JavaEEPerspective.class)
@RunWith(RedDeerSuite.class)
@AutoBuilding(false)
public class ConfigurationsEditorTest extends DefaultTest {

	protected Logger log = Logger.getLogger(ConfigurationsEditorTest.class);
	
	private static final String PROJECT_NAME = "camel-spring";
	private static final String CONTEXT = "camel-context.xml";
	private static final String TYPE = "JBoss Fuse";
	
	/**
	 * Prepares test environment
	 * @throws FuseArchetypeNotFoundException
	 *             Fuse archetype was not found. Tests cannot be executed!
	 */
	@Before
	public void setupResetCamelContext() {

		new WorkbenchShell();
		ProjectFactory.newProject(PROJECT_NAME).version("2.15.1.redhat-621084").template("Content Based Router").type(ProjectType.SPRING);
		new ErrorLogView().deleteLog();
	}
	
	/**
	 * Cleans up test environment
	 */
	@After
	public void setupDeleteProjects() {
		
		new WorkbenchShell();
		EditorHandler.getInstance().closeAll(true);
		ProjectFactory.deleteAllProjects();
	}	

	/**
	 * <p>
	 * Test tries create <i>Endpoint</i> global element inside Camel Editor in configurations tab
	 * </p>
	 * <ol>
	 * <li>create a new project with camel-archetype-spring archetype</li>
	 * <li>open Project Explorer view</li>
	 * <li>open camel-context.xml file</li>
	 * <li>get list of all endpoints of global elements</li>
	 * <li>creates each of <i>Endpoints</i> list component</li>
	 * <li>check that each of <i>Endpoint</i> component was created</li>
	 * <li>check that error list is empty</li>
	 * </ol>
	 */
	@Test
	public void testCreateGlobalEndpoint() {
		
		ConfigurationsEditor confEditor = new ConfigurationsEditor(PROJECT_NAME, CONTEXT);
		List<String> endpoints = CamelEndpointDialog.getEndpoints();
		List<String> errors = new ArrayList<String>();
		for (String element : endpoints) {
			String title = element.substring(0, 10);
			confEditor.createNewGlobalEndpoint(title, element);	
			try {
				new DefaultTreeItem(new String[] { TYPE, title + " (Endpoint)" }).isDisposed();
			} catch (Exception e) {
				errors.add(title + " |Endpoint");
			}
		}
		
		StringBuilder build = new StringBuilder("\nFailed to create:");
		if(!errors.isEmpty()) {
			for (String error : errors) {
				build.append("\n" + error);			
			}			
			fail(build.toString());
		}
	}
	
	/**
	 * <p>
	 * Test tries create <i>Data Format</i> global element inside Camel Editor in configurations tab
	 * </p>
	 * <ol>
	 * <li>create a new project with camel-archetype-spring archetype</li>
	 * <li>open Project Explorer view</li>
	 * <li>open camel-context.xml file</li>
	 * <li>get list of all data formats of global elements</li>
	 * <li>creates each of <i>Data Formats</i> list component</li>
	 * <li>check that each of <i>Data Format</i> component was created</li>
	 * <li>check that error list is empty</li>
	 * </ol>
	 */
	@Test
	public void testCreateGlobalDataFormat()	{
		
		ConfigurationsEditor confEditor = new ConfigurationsEditor(PROJECT_NAME, CONTEXT);
		List<String> dataFormats = CamelDataFormatDialog.getDataFormats();
		List<String> errors = new ArrayList<String>();
		for (String element : dataFormats) {
			String title = element.substring(0, 10);
			confEditor.createNewGlobalDataFormat(title, element);	
			try {
				new DefaultTreeItem(new String[] { TYPE, title + " (Data Format)" }).isDisposed();
			} catch (Exception e) {
				errors.add(title + " |Data Format");
			}
		}
		
		StringBuilder build = new StringBuilder("\nFailed to create:");
		if(!errors.isEmpty()) {
			for (String error : errors) {
				build.append("\n" + error);			
			}			
			fail(build.toString());
		}
	}
	
	/**
	 * <p>
	 * Test tries edit <i>Endpoint</i> global element inside Camel Editor in configurations tab
	 * </p>
	 * <ol>
	 * <li>create a new project with camel-archetype-spring archetype</li>
	 * <li>open Project Explorer view</li>
	 * <li>open camel-context.xml file</li>
	 * <li>get list of all data formats of global elements</li>
	 * <li>creates each of <i>Endpoints</i> list component</li>
	 * <li>edits name each of <i>Endpoints</i> list component</li>
	 * <li>check that each of <i>Endpoint</i> component was edited</li>
	 * <li>check that error list is empty</li>
	 * </ol>
	 */
	@Test
	public void testEditGlobalEndpoint()	{
		
		ConfigurationsEditor confEditor = new ConfigurationsEditor(PROJECT_NAME, CONTEXT);
		List<String> endpoints = CamelEndpointDialog.getEndpoints();
		List<String> errors = new ArrayList<String>();
		for (String element : endpoints) {
			String title = element.substring(0, 10);
			confEditor.createNewGlobalEndpoint(title, element);	
			try {
				confEditor.editGlobalEndpoint(title);					
				new LabeledText("Id").setText("changed" + title);
				confEditor.activate();
				new DefaultTreeItem(new String[] { TYPE, "changed" + title + " (Endpoint)" }).isDisposed();
			} catch (Exception e) {
				errors.add(title + " |Endpoint");
			}
		}
		
		StringBuilder build = new StringBuilder("\nFailed to edit:");
		if(!errors.isEmpty()) {
			for (String error : errors) {
				build.append("\n" + error);	
			}			
			fail(build.toString());
		}
	}
	
	/**
	 * <p>
	 * Test tries edit <i>Data Format</i> global element inside Camel Editor in configurations tab
	 * </p>
	 * <ol>
	 * <li>create a new project with camel-archetype-spring archetype</li>
	 * <li>open Project Explorer view</li>
	 * <li>open camel-context.xml file</li>
	 * <li>get list of all data formats of global elements</li>
	 * <li>creates each of <i>Data Formats</i> list component</li>
	 * <li>edits name each of <i>Data Formats</i> list component</li>
	 * <li>check that each of <i>Data Format</i> component was edited</li>
	 * <li>check that error list is empty</li>
	 * </ol>
	 */
	@Test
	public void testEditGlobalDataFormat()	{
		
		ConfigurationsEditor confEditor = new ConfigurationsEditor(PROJECT_NAME, CONTEXT);
		List<String> dataFormats = CamelDataFormatDialog.getDataFormats();
		List<String> errors = new ArrayList<String>();
		for (String element : dataFormats) {
			String title = element.substring(0, 10);
			confEditor.createNewGlobalDataFormat(title, element);	
			try {
				confEditor.editGlobalDataFormat(title);				
				new LabeledText("Id").setText("changed" + title);
				confEditor.activate();
				new DefaultTreeItem(new String[] { TYPE, "changed" + title + " (Data Format)" }).isDisposed();
			} catch (Exception e) {
				errors.add(title + " |Data Format");
			}
		}
		
		StringBuilder build = new StringBuilder("\nFailed to edit:");
		if(!errors.isEmpty()) {
			for (String error : errors) {
				build.append("\n" + error);	
			}			
			fail(build.toString());
		}
	}
	
	/**
	 * <p>
	 * Test tries delete <i>Endpoint and Data Format</i> global element inside Camel Editor in configurations tab
	 * </p>
	 * <ol>
	 * <li>create a new project with camel-archetype-spring archetype</li>
	 * <li>open Project Explorer view</li>
	 * <li>open camel-context.xml file</li>
	 * <li>get list of all endpoints of global elements</li>
	 * <li>get list of all data formats of global elements</li>
	 * <li>creates each of <i>Endpoints</i> list component</li>
	 * <li>delete each of <i>Endpoints</i> list component</li>
	 * <li>check that each of <i>Endpoint</i> component was deleted</li>
	 * <li>creates each of <i>Data Formats</i> list component</li>
	 * <li>delete each of <i>Data Formats</i> list component</li>
	 * <li>check that each of <i>Data Format</i> component was deleted</li>
	 * <li>check that error list is empty</li>
	 * </ol>
	 */
	@Test
	public void testDeleteGlobalElement()	{
		
		ConfigurationsEditor confEditor = new ConfigurationsEditor(PROJECT_NAME, CONTEXT);
		List<String> endpoints = CamelEndpointDialog.getEndpoints();
		List<String> dataFormats = CamelDataFormatDialog.getDataFormats();		
		List<String> errors = new ArrayList<String>();
		for (String element : endpoints) {
			String title = element.substring(0, 10);
			confEditor.createNewGlobalEndpoint(title, element);	
			confEditor.deleteGlobalElement(Element.ENDPOINT, title);
			try {
				new DefaultTreeItem(new String[] { TYPE, title + " (Endpoint)" }).isDisposed();
				errors.add(title + " |Endpoint");
			} catch (Exception e) {
				log.info("Endpoint with title - " + title + ", was deleted");
			}
		}
		
		for (String element : dataFormats) {
			String title = element.substring(0, 10);
			confEditor.createNewGlobalDataFormat(title, element);	
			confEditor.deleteGlobalElement(Element.DATAFORMAT, title);
			try {
				new DefaultTreeItem(new String[] { TYPE, title + " (Data Format)" }).isDisposed();
				errors.add(title + " |Data Format");
			} catch (Exception e) {
				log.info("Data Format with title - " + title + ", was deleted");
			}
		}
		
		StringBuilder build = new StringBuilder("\nFailed to delete:");
		if(!errors.isEmpty()) {
			for (String error : errors) {
				build.append("\n" + error);			
			}			
			fail(build.toString());
		}
	}
	
	/**
	 * <p>
	 * Test tries create and delete <i>Endpoint and Data Format</i> global element and checks Source XML
	 * </p>
	 * <ol>
	 * <li>create a new project with camel-archetype-spring archetype</li>
	 * <li>open Project Explorer view</li>
	 * <li>open camel-context.xml file</li>
	 * <li>get list of all endpoints of global elements</li>
	 * <li>get list of all data formats of global elements</li>
	 * <li>creates each of <i>Endpoints</i> list component</li>
	 * <li>checks source XML that each of <i>Endpoints</i> list component was created</li>
	 * <li>delete each of <i>Endpoints</i> list component</li>
	 * <li>checks source XML that each of <i>Endpoints</i> list component was deleted</li>	 
	 * <li>creates each of <i>Data Formats</i> list component</li>
	 * <li>checks source XML that each of <i>Data Formats</i> list component was created</li>
	 * <li>delete each of <i>Data Formats</i> list component</li>
	 * <li>checks source XML that each of <i>Data formats</i> list component was deleted</li>
	 * <li>check that error list is empty</li>
	 * </ol>
	 */
	@Test
	public void testSourceXML()	{
		
		ConfigurationsEditor confEditor = new ConfigurationsEditor(PROJECT_NAME, CONTEXT);
		List<String> endpoints = CamelEndpointDialog.getEndpoints();
		List<String> dataFormats = CamelDataFormatDialog.getDataFormats();		
		List<String> errors = new ArrayList<String>();
		
		for (String element : endpoints) {
			String title = element.substring(0, 10);
			confEditor.createNewGlobalEndpoint(title, element);	
			CamelEditor.switchTab("Source");	
			String content = new DefaultStyledText().getText();	
			log.info(content);
			XPathEvaluator eval = new XPathEvaluator(new ByteArrayInputStream(content.getBytes()));
			if(!eval.evaluateBoolean("/beans/camelContext/endpoint[@id='" + title + "']"))
				errors.add(title + " |Endpoint");
			CamelEditor.switchTab("Configurations");
			confEditor.deleteGlobalElement(Element.ENDPOINT, title);
			CamelEditor.switchTab("Source");
			content = new DefaultStyledText().getText();
			log.info(content);
			eval = new XPathEvaluator(new ByteArrayInputStream(content.getBytes()));
			if(eval.evaluateBoolean("/beans/camelContext/endpoint[@id='" + title + "']"))
				errors.add(title + " |Endpoint");
			CamelEditor.switchTab("Configurations");
		}
		
		for (String element : dataFormats) {
			String title = element.substring(0, 15);
			String[] node = title.split(" ");
			confEditor.createNewGlobalDataFormat(title, element);	
			CamelEditor.switchTab("Source");	
			String content = new DefaultStyledText().getText();	
			log.info(content);
			XPathEvaluator eval = new XPathEvaluator(new ByteArrayInputStream(content.getBytes()));
			if(!eval.evaluateBoolean("/beans/camelContext/dataFormats/" + node[0] +"[@id='" + title + "']"))
				errors.add(title + " |Data Format");
			CamelEditor.switchTab("Configurations");
			confEditor.deleteGlobalElement(Element.DATAFORMAT, title);
			CamelEditor.switchTab("Source");
			content = new DefaultStyledText().getText();
			log.info(content);
			eval = new XPathEvaluator(new ByteArrayInputStream(content.getBytes()));
			if(eval.evaluateBoolean("/beans/camelContext/dataFormats/" + node[0] +"[@id='" + title + "']"))
				errors.add(title + " |Data Format");
			CamelEditor.switchTab("Configurations");
		}
		
		StringBuilder build = new StringBuilder("\nError elements in source XML:");
		if(!errors.isEmpty()) {
			for (String error : errors) {
				build.append("\n" + error);			
			}			
			fail(build.toString());
		}		
	}
	
}