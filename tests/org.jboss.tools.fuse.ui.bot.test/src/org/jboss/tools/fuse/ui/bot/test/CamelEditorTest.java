package org.jboss.tools.fuse.ui.bot.test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.jboss.reddeer.common.logging.Logger;
import org.jboss.reddeer.eclipse.jdt.ui.ProjectExplorer;
import org.jboss.reddeer.eclipse.ui.perspectives.JavaEEPerspective;
import org.jboss.reddeer.jface.text.contentassist.ContentAssistant;
import org.jboss.reddeer.junit.runner.RedDeerSuite;
import org.jboss.reddeer.requirements.cleanworkspace.CleanWorkspaceRequirement.CleanWorkspace;
import org.jboss.reddeer.requirements.openperspective.OpenPerspectiveRequirement.OpenPerspective;
import org.jboss.reddeer.swt.impl.shell.WorkbenchShell;
import org.jboss.reddeer.common.wait.AbstractWait;
import org.jboss.reddeer.common.wait.TimePeriod;
import org.jboss.tools.fuse.reddeer.component.CamelComponent;
import org.jboss.tools.fuse.reddeer.component.CamelComponents;
import org.jboss.tools.fuse.reddeer.component.Generic;
import org.jboss.tools.fuse.reddeer.component.Log;
import org.jboss.tools.fuse.reddeer.component.Otherwise;
import org.jboss.tools.fuse.reddeer.editor.CamelEditor;
import org.jboss.tools.fuse.reddeer.editor.SourceEditor;
import org.jboss.tools.fuse.reddeer.projectexplorer.CamelProject;
import org.jboss.tools.fuse.reddeer.view.ErrorLogView;
import org.jboss.tools.fuse.ui.bot.test.utils.EditorManipulator;
import org.jboss.tools.fuse.ui.bot.test.utils.FuseArchetypeNotFoundException;
import org.jboss.tools.fuse.ui.bot.test.utils.ProjectFactory;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * Tests:
 * <ul>
 * <li>creation of all components in Fuse Camel editor</li>
 * <li>manipulation with components in the Camel Editor</li>
 * </ul>
 * 
 * @author tsedmik
 */
@CleanWorkspace
@OpenPerspective(JavaEEPerspective.class)
@RunWith(RedDeerSuite.class)
public class CamelEditorTest extends DefaultTest {

	protected Logger log = Logger.getLogger(CamelEditorTest.class);

	/**
	 * Prepares test environment
	 * 
	 * @throws FuseArchetypeNotFoundException Fuse archetype was not found. Tests cannot be executed!
	 */
	@Before
	public void setupResetCamelContext() throws FuseArchetypeNotFoundException {

		new WorkbenchShell();
		ProjectFactory.createProject("camel-spring", "camel-archetype-spring");
		new ErrorLogView().deleteLog();
	}

	/**
	 * Cleans up test environment
	 */
	@After
	public void setupDeleteProjects() {

		new WorkbenchShell();
		ProjectFactory.deleteAllProjects();
	}

	/**
	 * Prepares IDE for tests manipulate with components in the Camel Editor
	 */
	private static void setupPrepareIDEForManipulationTests() {

		new CamelProject("camel-spring").openCamelContext("camel-context.xml");
		CamelEditor.switchTab("Source");
		EditorManipulator.copyFileContentToCamelXMLEditor("resources/camel-context-otherwise.xml");
		CamelEditor.switchTab("Design");
	}

	/**
	 * <p>Test tries to create all components available in the Palette view associated
	 *  with the Camel Editor.</p>
	 * <b>Steps</b>
	 * <ol>
	 * <li>create a new project with camel-archetype-spring archetype</li>
	 * <li>open Project Explorer view</li>
	 * <li>delete camel-context.xml and create a new empty one</li>
	 * <li>try to create all components in Palette View</li>
	 * <li>check if the component is present in Camel Editor</li>
	 * <li>delete the component from Camel Editor</li>
	 * </ol>
	 */
	@Test
	public void testComponents() {

		new ProjectExplorer().open();
		new CamelProject("camel-spring").deleteCamelContext("camel-context.xml");
		new CamelProject("camel-spring").createCamelContext("camel-context.xml");

		for (CamelComponent component : CamelComponents.getAll()) {
			new CamelProject("camel-spring").openCamelContext("camel-context.xml");
			log.info("Testing camel component '" + component.getPaletteEntry() + "'");
			CamelEditor editor = new CamelEditor("camel-context.xml");
			editor.addCamelComponent(component);
			editor.deleteCamelComponent(component);
		}
		assertTrue(new ErrorLogView().getErrorMessages().size() == 0);
	}

	/**
	 * <p>Test tries to manipulate with XML source of the camel-context.xml file and 
	 * checks if it affects components in the Camel Editor.</p>
	 * <b>Steps</b>
	 * <ol>
	 * <li>create a new project with camel-archetype-spring archetype</li>
	 * <li>open Project Explorer view</li>
	 * <li>open camel-context.xml file</li>
	 * <li>switch to Source tab in the Camel Editor</li>
	 * <li>cut branch otherwise</li>
	 * <li>switch to Design tab in the Camel Editor</li>
	 * <li>check if the otherwise component is no longer available in the Camel Editor</li>
	 * <li>switch to Source tab in the Camel Editor</li>
	 * <li>paste branch otherwise</li>
	 * <li>switch to Design tab in the Camel Editor</li>
	 * <li>check if the otherwise component is available in the Camel Editor</li>
	 * </ol>
	 */
	@Test
	public void testXMLEditor() {

		setupPrepareIDEForManipulationTests();
		CamelEditor editor = new CamelEditor("camel-context.xml");
		assertFalse(editor.isComponentAvailable("otherwise"));
		CamelEditor.switchTab("Source");
		EditorManipulator.copyFileContentToCamelXMLEditor("resources/camel-context-all.xml");
		CamelEditor.switchTab("Design");
		assertTrue(editor.isComponentAvailable("otherwise"));
		assertTrue(editor.isComponentAvailable("file:target/messa..."));
		assertTrue(new ErrorLogView().getErrorMessages().size() == 0);
	}

	/**
	 * <p>Test tries to add components in the Camel Editor via component's context menu and the option Add.</p>
	 * <b>Steps</b>
	 * <ol>
	 * <li>create a new project with camel-archetype-spring archetype</li>
	 * <li>open Project Explorer view</li>
	 * <li>open camel-context.xml file</li>
	 * <li>switch to Source tab in the Camel Editor</li>
	 * <li>remove branch otherwise</li>
	 * <li>switch to Design tab in the Camel Editor</li>
	 * <li>add back removed components (from branch otherwise) via the context menu and the option Add</li>
	 * <li>switch to Source tab in the Camel Editor</li>
	 * <li>check if the content of the file is equals to content of the same file immediately after project creation</li>
	 * </ol>
	 */
	@Test
	public void testAddComponents() {

		setupPrepareIDEForManipulationTests();
		CamelEditor editor = new CamelEditor("camel-context.xml");
		editor.setId("log", "log1");
		editor.doOperation("choice", "Add", "Routing", "Otherwise");
		editor.doOperation("otherwise", "Add", "Components", "Log");
		editor.setProperty("log", "Message", "Other message");
		editor.doOperation("log", "Add", "Components", "Generic");
		editor.setComboProperty("Endpoint", 0, "file:target/messages/others");
		editor.setId("log1", "");
		assertTrue(editor.isComponentAvailable("otherwise"));
		assertTrue(editor.isComponentAvailable("file:target/messa..."));
		CamelEditor.switchTab("Source");
		assertTrue(EditorManipulator.isEditorContentEqualsFile("resources/camel-context-all.xml"));
		assertTrue(new ErrorLogView().getErrorMessages().size() == 0);
	}

	/**
	 * <p>Test tries to add components in the Camel Editor via Drag&Drop feature (Adds components
	 * from Palette and connections between components via the Camel Editor).</p>
	 * <ol>
	 * <li>create a new project with camel-archetype-spring archetype</li>
	 * <li>open Project Explorer view</li>
	 * <li>open camel-context.xml file</li>
	 * <li>switch to Source tab in the Camel Editor</li>
	 * <li>remove branch otherwise</li>
	 * <li>switch to Design tab in the Camel Editor</li>
	 * <li>add back removed components (from branch otherwise) via Drag&Drop feature (Adds components from Palette 
	 * and connections between components via the Camel Editor)</li>
	 * <li>switch to Source tab in the Camel Editor</li>
	 * <li>check if the content of the file is equals to content of the same file immediately after project creation</li>
	 * </ol>
	 */
	@Test
	public void testDragAndDropComponents() {

		setupPrepareIDEForManipulationTests();
		CamelEditor editor = new CamelEditor("camel-context.xml");
		editor.addCamelComponent(new Otherwise(), 0, -100);
		editor.addConnection("choice", "otherwise");
		editor.save();
		AbstractWait.sleep(TimePeriod.SHORT);
		editor.setId("log", "log1");
		editor.setId("file:target/messa...", "temp");
		editor.addCamelComponent(new Log());
		editor.setProperty("log", "Message", "Other message");
		editor.addConnection("otherwise", "log");
		editor.save();
		AbstractWait.sleep(TimePeriod.getCustom(1));
		editor.addCamelComponent(new Generic());
		editor.setComboProperty("Endpoint", 0, "file:target/messages/others");
		editor.addConnection("log", "file:target/messa...");
		editor.save();
		AbstractWait.sleep(TimePeriod.getCustom(1));
		editor.setId("temp", "");
		editor.setId("log1", "");
		CamelEditor.switchTab("Source");
		assertTrue(EditorManipulator.isEditorContentEqualsFile("resources/camel-context-all.xml"));
		assertTrue(new ErrorLogView().getErrorMessages().size() == 0);
	}

	/**
	 * <p>Test tries to code completion assistant in the Camel Editor on Source tab.</p>
	 * <ol>
	 * <li>create a new project with camel-archetype-spring archetype</li>
	 * <li>open Project Explorer view</li>
	 * <li>open camel-context.xml file</li>
	 * <li>switch to Source tab in the Camel Editor</li>
	 * <li>invoke content assistant on from element</li>
	 * <li>check available entries (id, ref)</li>
	 * <li>check that already set attribute uri is not present</li>
	 * <li>add id attribute</li>
	 * <li>check that id attribute is not present in content assistant</li>
	 * <li>invoke content assistant within route element</li>
	 * <li>insert to element</li>
	 * </ol>
	 */
	@Test
	public void testCodeCompletion() {

		new CamelProject("camel-spring").openCamelContext("camel-context.xml");
		CamelEditor.switchTab("Source");
		EditorManipulator.copyFileContentToCamelXMLEditor("resources/camel-context-all.xml");
		SourceEditor editor = new SourceEditor();
		editor.setCursorPosition(713);
		ContentAssistant assistent = editor.openContentAssistant();
		List<String> proposals = assistent.getProposals();
		assertTrue("Content Assistent does not contain 'id' value", proposals.contains("id"));
		assertTrue("Content Assistent does not contain 'ref' value", proposals.contains("ref"));
		assertFalse("Content Assistent does contain 'uri' value. The attribute is already set", proposals.contains("uri"));
		assistent.close();
		editor.insertTest("id=\"test\" ");
		assistent = editor.openContentAssistant();
		proposals = assistent.getProposals();
		assertFalse("Content Assistent does contain 'id' value", proposals.contains("id"));
		assistent.close();
		editor.setCursorPosition(701);
		assistent = editor.openContentAssistant();
		assistent.chooseProposal("to");
		assertTrue("Editor does not contain generated text", editor.getText().contains("<to></to>"));
		editor.setCursorPosition(704);
		editor.insertTest(" ");
		editor.setCursorPosition(705);
		assistent = editor.openContentAssistant();
		proposals = assistent.getProposals();
		assertTrue("Content Assistent does not contain 'uri' value", proposals.contains("uri"));
		assertTrue(new ErrorLogView().getErrorMessages().size() == 0);
	}
}
