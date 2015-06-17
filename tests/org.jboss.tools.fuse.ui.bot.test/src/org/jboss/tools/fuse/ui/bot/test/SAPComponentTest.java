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
import org.jboss.reddeer.swt.impl.shell.WorkbenchShell;
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

	@BeforeClass
	public static void resetCamelContext() throws FuseArchetypeNotFoundException {
		new WorkbenchShell();
		ProjectFactory.createProject("camel-spring", "camel-archetype-spring");
		new ErrorLogView().deleteLog();

		new ProjectExplorer().open();
		new CamelProject("camel-spring").deleteCamelContext("camel-context.xml");
		new CamelProject("camel-spring").createCamelContext("camel-context.xml");
	}

	@AfterClass
	public static void deleteProjects() {
		new WorkbenchShell();
		new ProjectExplorer().deleteAllProjects();
	}

	@Test
	public void sapIDocListServerTest() throws Exception {
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

	@Test
	public void sapSRFCServerTest() throws Exception {
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

	@Test
	public void sapTRFCServerTest() throws Exception {
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

	@Test
	public void sapIDocDestinationTest() throws Exception {
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

	@Test
	public void sapIDocListDestinationTest() throws Exception {
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

	@Test
	public void sapQIDocDestinationTest() throws Exception {
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

	@Test
	public void sapQIDocListDestinationTest() throws Exception {
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

	@Test
	public void sapQRFCDestinationTest() throws Exception {
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

	@Test
	public void sapSRFCDestinationTest() throws Exception {
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

	@Test
	public void sapTRFCDestinationTest() throws Exception {
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
