package org.jboss.tools.fuse.ui.bot.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.io.StringReader;

import org.jboss.reddeer.common.logging.Logger;
import org.jboss.reddeer.eclipse.jdt.ui.ProjectExplorer;
import org.jboss.reddeer.eclipse.ui.perspectives.JavaEEPerspective;
import org.jboss.reddeer.junit.runner.RedDeerSuite;
import org.jboss.reddeer.requirements.cleanworkspace.CleanWorkspaceRequirement.CleanWorkspace;
import org.jboss.reddeer.requirements.openperspective.OpenPerspectiveRequirement.OpenPerspective;
import org.jboss.reddeer.workbench.impl.shell.WorkbenchShell;
import org.jboss.tools.fuse.reddeer.component.AbstractURICamelComponent;
import org.jboss.tools.fuse.reddeer.component.CamelComponent;
import org.jboss.tools.fuse.reddeer.component.SAPIDocDestination;
import org.jboss.tools.fuse.reddeer.component.SAPIDocListDestination;
import org.jboss.tools.fuse.reddeer.component.SAPIDocListServer;
import org.jboss.tools.fuse.reddeer.component.SAPQIDocDestination;
import org.jboss.tools.fuse.reddeer.component.SAPQIDocListDestination;
import org.jboss.tools.fuse.reddeer.component.SAPQRFCDestination;
import org.jboss.tools.fuse.reddeer.component.SAPSRFCDestination;
import org.jboss.tools.fuse.reddeer.component.SAPSRFCServer;
import org.jboss.tools.fuse.reddeer.component.SAPTRFCDestination;
import org.jboss.tools.fuse.reddeer.component.SAPTRFCServer;
import org.jboss.tools.fuse.reddeer.component.Stop;
import org.jboss.tools.fuse.reddeer.editor.CamelEditor;
import org.jboss.tools.fuse.reddeer.projectexplorer.CamelProject;
import org.jboss.tools.fuse.reddeer.utils.XPathEvaluator;
import org.jboss.tools.fuse.reddeer.view.ErrorLogView;
import org.jboss.tools.fuse.ui.bot.test.utils.FuseArchetypeNotFoundException;
import org.jboss.tools.fuse.ui.bot.test.utils.ProjectFactory;
import org.jboss.tools.runtime.reddeer.requirement.SAPRequirement.SAP;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * Tests for all SAP components in Camel editor.
 * 
 * @author apodhrad
 */
@SAP
@CleanWorkspace
@OpenPerspective(JavaEEPerspective.class)
@RunWith(RedDeerSuite.class)
public class SAPComponentTest extends DefaultTest {

	protected Logger log = Logger.getLogger(SAPComponentTest.class);

	private CamelEditor editor;
	private CamelComponent stopComponent = new Stop();

	/**
	 * Prepares test environment
	 * 
	 * @throws FuseArchetypeNotFoundException Fuse archetype was not found. Tests cannot be executed!
	 */
	@BeforeClass
	public static void setupResetCamelContext() throws FuseArchetypeNotFoundException {
		new WorkbenchShell();
		ProjectFactory.createProject("camel-spring", "camel-archetype-spring");
		new ErrorLogView().deleteLog();

		new ProjectExplorer().open();
		new CamelProject("camel-spring").deleteCamelContext("camel-context.xml");
		new CamelProject("camel-spring").createCamelContext("camel-context.xml");
	}

	/**
	 * Cleans up test environment
	 */
	@AfterClass
	public static void setupDeleteProjects() {
		new WorkbenchShell();
		new ProjectExplorer().deleteAllProjects();
	}

	/**
	 * <p>Tries to create/delete SAP component (see the method name) in the Camel Editor.</p>
	 * <b>Steps:</b>
	 * <ol>
	 * <li>create a new project with camel-archetype-spring archetype</li>
	 * <li>open Project Explorer view</li>
	 * <li>delete camel-context.xml and create a new empty one</li>
	 * <li>try to create a SAP component (see the method name) in Palette View</li>
	 * <li>check if the component is present in Camel Editorh</li>
	 * <li>delete the component from Camel Editor</li>
	 * </ol>
	 * 
	 * @throws Exception Something bad happened
	 */
	@Test
	public void testSAPIDocListServer() throws Exception {
		AbstractURICamelComponent component = new SAPIDocListServer();
		new CamelProject("camel-spring").openCamelContext("camel-context.xml");
		log.info("Testing camel component '" + component.getPaletteEntry() + "'");
		editor = new CamelEditor("camel-context.xml");
		editor.addCamelComponent(component);
		editor.doOperation(component.getLabel(), "Add", "Miscellaneous", "Stop");
		editor.save();
		String source = editor.getSource();
		editor.deleteCamelComponent(component);
		editor.deleteCamelComponent(stopComponent);
		editor.save();

		assertXPath(source, component.getUri(), "//route/from/@uri");
		assertTrue(new ErrorLogView().getErrorMessages().size() == 0);
	}

	/**
	 * <p>Tries to create/delete SAP component (see the method name) in the Camel Editor.</p>
	 * <b>Steps:</b>
	 * <ol>
	 * <li>create a new project with camel-archetype-spring archetype</li>
	 * <li>open Project Explorer view</li>
	 * <li>delete camel-context.xml and create a new empty one</li>
	 * <li>try to create a SAP component (see the method name) in Palette View</li>
	 * <li>check if the component is present in Camel Editorh</li>
	 * <li>delete the component from Camel Editor</li>
	 * </ol>
	 * 
	 * @throws Exception Something bad happened
	 */
	@Test
	public void testSAPSRFCServer() throws Exception {
		AbstractURICamelComponent component = new SAPSRFCServer();
		new CamelProject("camel-spring").openCamelContext("camel-context.xml");
		log.info("Testing camel component '" + component.getPaletteEntry() + "'");
		editor = new CamelEditor("camel-context.xml");
		editor.addCamelComponent(component);
		editor.doOperation(component.getLabel(), "Add", "Miscellaneous", "Stop");
		editor.save();
		String source = editor.getSource();
		editor.deleteCamelComponent(component);
		editor.deleteCamelComponent(stopComponent);
		editor.save();

		assertXPath(source, component.getUri(), "//route/from/@uri");
		assertTrue(new ErrorLogView().getErrorMessages().size() == 0);
	}

	/**
	 * <p>Tries to create/delete SAP component (see the method name) in the Camel Editor.</p>
	 * <b>Steps:</b>
	 * <ol>
	 * <li>create a new project with camel-archetype-spring archetype</li>
	 * <li>open Project Explorer view</li>
	 * <li>delete camel-context.xml and create a new empty one</li>
	 * <li>try to create a SAP component (see the method name) in Palette View</li>
	 * <li>check if the component is present in Camel Editorh</li>
	 * <li>delete the component from Camel Editor</li>
	 * </ol>
	 * 
	 * @throws Exception Something bad happened
	 */
	@Test
	public void testSAPTRFCServer() throws Exception {
		AbstractURICamelComponent component = new SAPTRFCServer();
		new CamelProject("camel-spring").openCamelContext("camel-context.xml");
		log.info("Testing camel component '" + component.getPaletteEntry() + "'");
		editor = new CamelEditor("camel-context.xml");
		editor.addCamelComponent(component);
		editor.doOperation(component.getLabel(), "Add", "Miscellaneous", "Stop");
		editor.save();
		String source = editor.getSource();
		editor.deleteCamelComponent(component);
		editor.deleteCamelComponent(stopComponent);
		editor.save();

		assertXPath(source, component.getUri(), "//route/from/@uri");
		assertTrue(new ErrorLogView().getErrorMessages().size() == 0);
	}

	/**
	 * <p>Tries to create/delete SAP component (see the method name) in the Camel Editor.</p>
	 * <b>Steps:</b>
	 * <ol>
	 * <li>create a new project with camel-archetype-spring archetype</li>
	 * <li>open Project Explorer view</li>
	 * <li>delete camel-context.xml and create a new empty one</li>
	 * <li>try to create a SAP component (see the method name) in Palette View</li>
	 * <li>check if the component is present in Camel Editorh</li>
	 * <li>delete the component from Camel Editor</li>
	 * </ol>
	 * 
	 * @throws Exception Something bad happened
	 */
	@Test
	public void testSAPIDocDestination() throws Exception {
		AbstractURICamelComponent component = new SAPIDocDestination();
		new CamelProject("camel-spring").openCamelContext("camel-context.xml");
		log.info("Testing camel component '" + component.getPaletteEntry() + "'");
		editor = new CamelEditor("camel-context.xml");
		editor.addCamelComponent(component);
		editor.doOperation(component.getLabel(), "Add", "Miscellaneous", "Stop");
		editor.save();
		String source = editor.getSource();
		editor.deleteCamelComponent(component);
		editor.deleteCamelComponent(stopComponent);
		editor.save();

		assertXPath(source, component.getUri(), "//route/from/@uri");
		assertTrue(new ErrorLogView().getErrorMessages().size() == 0);
	}

	/**
	 * <p>Tries to create/delete SAP component (see the method name) in the Camel Editor.</p>
	 * <b>Steps:</b>
	 * <ol>
	 * <li>create a new project with camel-archetype-spring archetype</li>
	 * <li>open Project Explorer view</li>
	 * <li>delete camel-context.xml and create a new empty one</li>
	 * <li>try to create a SAP component (see the method name) in Palette View</li>
	 * <li>check if the component is present in Camel Editorh</li>
	 * <li>delete the component from Camel Editor</li>
	 * </ol>
	 * 
	 * @throws Exception Something bad happened
	 */
	@Test
	public void testSAPIDocListDestination() throws Exception {
		AbstractURICamelComponent component = new SAPIDocListDestination();
		new CamelProject("camel-spring").openCamelContext("camel-context.xml");
		log.info("Testing camel component '" + component.getPaletteEntry() + "'");
		editor = new CamelEditor("camel-context.xml");
		editor.addCamelComponent(component);
		editor.doOperation(component.getLabel(), "Add", "Miscellaneous", "Stop");
		editor.save();
		String source = editor.getSource();
		editor.deleteCamelComponent(component);
		editor.deleteCamelComponent(stopComponent);
		editor.save();

		assertXPath(source, component.getUri(), "//route/from/@uri");
		assertTrue(new ErrorLogView().getErrorMessages().size() == 0);
	}

	/**
	 * <p>Tries to create/delete SAP component (see the method name) in the Camel Editor.</p>
	 * <b>Steps:</b>
	 * <ol>
	 * <li>create a new project with camel-archetype-spring archetype</li>
	 * <li>open Project Explorer view</li>
	 * <li>delete camel-context.xml and create a new empty one</li>
	 * <li>try to create a SAP component (see the method name) in Palette View</li>
	 * <li>check if the component is present in Camel Editorh</li>
	 * <li>delete the component from Camel Editor</li>
	 * </ol>
	 * 
	 * @throws Exception Something bad happened
	 */
	@Test
	public void testSAPQIDocDestination() throws Exception {
		AbstractURICamelComponent component = new SAPQIDocDestination();
		new CamelProject("camel-spring").openCamelContext("camel-context.xml");
		log.info("Testing camel component '" + component.getPaletteEntry() + "'");
		editor = new CamelEditor("camel-context.xml");
		editor.addCamelComponent(component);
		editor.doOperation(component.getLabel(), "Add", "Miscellaneous", "Stop");
		editor.save();
		String source = editor.getSource();
		editor.deleteCamelComponent(component);
		editor.deleteCamelComponent(stopComponent);
		editor.save();

		assertXPath(source, component.getUri(), "//route/from/@uri");
		assertTrue(new ErrorLogView().getErrorMessages().size() == 0);
	}

	/**
	 * <p>Tries to create/delete SAP component (see the method name) in the Camel Editor.</p>
	 * <b>Steps:</b>
	 * <ol>
	 * <li>create a new project with camel-archetype-spring archetype</li>
	 * <li>open Project Explorer view</li>
	 * <li>delete camel-context.xml and create a new empty one</li>
	 * <li>try to create a SAP component (see the method name) in Palette View</li>
	 * <li>check if the component is present in Camel Editorh</li>
	 * <li>delete the component from Camel Editor</li>
	 * </ol>
	 * 
	 * @throws Exception Something bad happened
	 */
	@Test
	public void testSAPQIDocListDestination() throws Exception {
		AbstractURICamelComponent component = new SAPQIDocListDestination();
		new CamelProject("camel-spring").openCamelContext("camel-context.xml");
		log.info("Testing camel component '" + component.getPaletteEntry() + "'");
		editor = new CamelEditor("camel-context.xml");
		editor.addCamelComponent(component);
		editor.doOperation(component.getLabel(), "Add", "Miscellaneous", "Stop");
		editor.save();
		String source = editor.getSource();
		editor.deleteCamelComponent(component);
		editor.deleteCamelComponent(stopComponent);
		editor.save();

		assertXPath(source, component.getUri(), "//route/from/@uri");
		assertTrue(new ErrorLogView().getErrorMessages().size() == 0);
	}

	/**
	 * <p>Tries to create/delete SAP component (see the method name) in the Camel Editor.</p>
	 * <b>Steps:</b>
	 * <ol>
	 * <li>create a new project with camel-archetype-spring archetype</li>
	 * <li>open Project Explorer view</li>
	 * <li>delete camel-context.xml and create a new empty one</li>
	 * <li>try to create a SAP component (see the method name) in Palette View</li>
	 * <li>check if the component is present in Camel Editorh</li>
	 * <li>delete the component from Camel Editor</li>
	 * </ol>
	 * 
	 * @throws Exception Something bad happened
	 */
	@Test
	public void testSAPQRFCDestination() throws Exception {
		AbstractURICamelComponent component = new SAPQRFCDestination();
		new CamelProject("camel-spring").openCamelContext("camel-context.xml");
		log.info("Testing camel component '" + component.getPaletteEntry() + "'");
		editor = new CamelEditor("camel-context.xml");
		editor.addCamelComponent(component);
		editor.doOperation(component.getLabel(), "Add", "Miscellaneous", "Stop");
		editor.save();
		String source = editor.getSource();
		editor.deleteCamelComponent(component);
		editor.deleteCamelComponent(stopComponent);
		editor.save();

		assertXPath(source, component.getUri(), "//route/from/@uri");
		assertTrue(new ErrorLogView().getErrorMessages().size() == 0);
	}

	/**
	 * <p>Tries to create/delete SAP component (see the method name) in the Camel Editor.</p>
	 * <b>Steps:</b>
	 * <ol>
	 * <li>create a new project with camel-archetype-spring archetype</li>
	 * <li>open Project Explorer view</li>
	 * <li>delete camel-context.xml and create a new empty one</li>
	 * <li>try to create a SAP component (see the method name) in Palette View</li>
	 * <li>check if the component is present in Camel Editorh</li>
	 * <li>delete the component from Camel Editor</li>
	 * </ol>
	 * 
	 * @throws Exception Something bad happened
	 */
	@Test
	public void testSAPSRFCDestination() throws Exception {
		AbstractURICamelComponent component = new SAPSRFCDestination();
		new CamelProject("camel-spring").openCamelContext("camel-context.xml");
		log.info("Testing camel component '" + component.getPaletteEntry() + "'");
		editor = new CamelEditor("camel-context.xml");
		editor.addCamelComponent(component);
		editor.doOperation(component.getLabel(), "Add", "Miscellaneous", "Stop");
		editor.save();
		String source = editor.getSource();
		editor.deleteCamelComponent(component);
		editor.deleteCamelComponent(stopComponent);
		editor.save();

		assertXPath(source, component.getUri(), "//route/from/@uri");
		assertTrue(new ErrorLogView().getErrorMessages().size() == 0);
	}

	/**
	 * <p>Tries to create/delete SAP component (see the method name) in the Camel Editor.</p>
	 * <b>Steps:</b>
	 * <ol>
	 * <li>create a new project with camel-archetype-spring archetype</li>
	 * <li>open Project Explorer view</li>
	 * <li>delete camel-context.xml and create a new empty one</li>
	 * <li>try to create a SAP component (see the method name) in Palette View</li>
	 * <li>check if the component is present in Camel Editorh</li>
	 * <li>delete the component from Camel Editor</li>
	 * </ol>
	 * 
	 * @throws Exception Something bad happened
	 */
	@Test
	public void testSAPTRFCDestination() throws Exception {
		AbstractURICamelComponent component = new SAPTRFCDestination();
		new CamelProject("camel-spring").openCamelContext("camel-context.xml");
		log.info("Testing camel component '" + component.getPaletteEntry() + "'");
		editor = new CamelEditor("camel-context.xml");
		editor.addCamelComponent(component);
		editor.doOperation(component.getLabel(), "Add", "Miscellaneous", "Stop");
		editor.save();
		String source = editor.getSource();
		editor.deleteCamelComponent(component);
		editor.deleteCamelComponent(stopComponent);
		editor.save();

		assertXPath(source, component.getUri(), "//route/from/@uri");
		assertTrue(new ErrorLogView().getErrorMessages().size() == 0);
	}

	private static void assertXPath(String source, String expected, String expr) throws IOException {
		String actual = new XPathEvaluator(new StringReader(source)).evaluateString(expr);
		assertEquals(source, expected, actual);
	}

}
