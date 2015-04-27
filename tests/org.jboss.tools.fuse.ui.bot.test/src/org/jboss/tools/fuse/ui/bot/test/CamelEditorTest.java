package org.jboss.tools.fuse.ui.bot.test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.IOException;

import javax.xml.parsers.ParserConfigurationException;

import org.jboss.reddeer.common.logging.Logger;
import org.jboss.reddeer.eclipse.jdt.ui.ProjectExplorer;
import org.jboss.reddeer.eclipse.ui.perspectives.JavaEEPerspective;
import org.jboss.reddeer.junit.runner.RedDeerSuite;
import org.jboss.reddeer.requirements.cleanworkspace.CleanWorkspaceRequirement.CleanWorkspace;
import org.jboss.reddeer.requirements.openperspective.OpenPerspectiveRequirement.OpenPerspective;
import org.jboss.reddeer.swt.impl.shell.WorkbenchShell;
import org.jboss.reddeer.swt.wait.AbstractWait;
import org.jboss.reddeer.swt.wait.TimePeriod;
import org.jboss.tools.fuse.reddeer.component.CamelComponent;
import org.jboss.tools.fuse.reddeer.component.CamelComponents;
import org.jboss.tools.fuse.reddeer.component.Generic;
import org.jboss.tools.fuse.reddeer.component.Log;
import org.jboss.tools.fuse.reddeer.component.Otherwise;
import org.jboss.tools.fuse.reddeer.editor.CamelEditor;
import org.jboss.tools.fuse.reddeer.projectexplorer.CamelProject;
import org.jboss.tools.fuse.reddeer.view.ErrorLogView;
import org.jboss.tools.fuse.ui.bot.test.utils.EditorManipulator;
import org.jboss.tools.fuse.ui.bot.test.utils.ProjectFactory;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.xml.sax.SAXException;

/**
 * Tests:
 * <ul>
 * <li>creation of all components in Fuse Camel editor</li>
 * <li>manipulation with components in the Camel Editor</li>
 * </ul>
 * 
 * @author apodhrad, tsedmik
 */
@CleanWorkspace
@OpenPerspective(JavaEEPerspective.class)
@RunWith(RedDeerSuite.class)
public class CamelEditorTest extends DefaultTest {

	protected Logger log = Logger.getLogger(CamelEditorTest.class);

	@Before
	public void resetCamelContext() {

		new WorkbenchShell();
		ProjectFactory.createProject("camel-spring", "camel-archetype-spring");
		new ErrorLogView().deleteLog();
	}

	@After
	public void deleteProjects() {

		new WorkbenchShell();
		new ProjectExplorer().deleteAllProjects();
	}

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

	@Test
	public void testXMLEditor() {

		prepareIDEForManipulationTests();
		CamelEditor editor = new CamelEditor("camel-context.xml");
		assertFalse(editor.isComponentAvailable("otherwise"));
		CamelEditor.switchTab("Source");
		EditorManipulator.copyFileContentToCamelXMLEditor("resources/camel-context-all.xml");
		CamelEditor.switchTab("Design");
		assertTrue(editor.isComponentAvailable("otherwise"));
		assertTrue(editor.isComponentAvailable("file:target/messa..."));
		assertTrue(new ErrorLogView().getErrorMessages().size() == 0);
	}

	@Test
	public void testAddComponents() {

		prepareIDEForManipulationTests();
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

	@Test
	public void testDragAndDropComponents() throws ParserConfigurationException, SAXException, IOException {

		prepareIDEForManipulationTests();
		CamelEditor editor = new CamelEditor("camel-context.xml");
		editor.addCamelComponent(new Otherwise());
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
	 * Prepares IDE for tests manipulate with components in the Camel Editor
	 */
	private static void prepareIDEForManipulationTests() {

		new CamelProject("camel-spring").openCamelContext("camel-context.xml");
		CamelEditor.switchTab("Source");
		EditorManipulator.copyFileContentToCamelXMLEditor("resources/camel-context-otherwise.xml");
		CamelEditor.switchTab("Design");
	}
}
