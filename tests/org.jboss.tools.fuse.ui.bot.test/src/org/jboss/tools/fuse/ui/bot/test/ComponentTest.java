package org.jboss.tools.fuse.ui.bot.test;

import static org.junit.Assert.assertTrue;

import org.jboss.reddeer.common.logging.Logger;
import org.jboss.reddeer.eclipse.jdt.ui.ProjectExplorer;
import org.jboss.reddeer.eclipse.ui.perspectives.JavaEEPerspective;
import org.jboss.reddeer.gef.view.PaletteView;
import org.jboss.reddeer.junit.runner.RedDeerSuite;
import org.jboss.reddeer.requirements.autobuilding.AutoBuildingRequirement.AutoBuilding;
import org.jboss.reddeer.requirements.cleanworkspace.CleanWorkspaceRequirement.CleanWorkspace;
import org.jboss.reddeer.requirements.openperspective.OpenPerspectiveRequirement.OpenPerspective;
import org.jboss.reddeer.workbench.exception.WorkbenchLayerException;
import org.jboss.reddeer.workbench.handler.EditorHandler;
import org.jboss.reddeer.workbench.impl.shell.WorkbenchShell;
import org.jboss.tools.common.reddeer.LogGrapper;
import org.jboss.tools.common.reddeer.view.ErrorLogView;
import org.jboss.tools.fuse.reddeer.ProjectTemplate;
import org.jboss.tools.fuse.reddeer.ProjectType;
import org.jboss.tools.fuse.reddeer.component.CamelComponent;
import org.jboss.tools.fuse.reddeer.component.CamelComponents;
import org.jboss.tools.fuse.reddeer.editor.CamelEditor;
import org.jboss.tools.fuse.reddeer.projectexplorer.CamelProject;
import org.jboss.tools.fuse.ui.bot.test.utils.ProjectFactory;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * Tests creation of all components in Fuse Camel editor
 * 
 * @author tsedmik
 */
@CleanWorkspace
@OpenPerspective(JavaEEPerspective.class)
@RunWith(RedDeerSuite.class)
@AutoBuilding(false)
public class ComponentTest extends DefaultTest {

	protected Logger log = Logger.getLogger(ComponentTest.class);

	/**
	 * Prepares test environment
	 * 
	 * @throws FuseArchetypeNotFoundException
	 *             Fuse archetype was not found. Tests cannot be executed!
	 */
	@Before
	public void setupResetCamelContext() {

		new WorkbenchShell();
		ProjectFactory.newProject("camel-spring").template(ProjectTemplate.CBR).type(ProjectType.SPRING).create();
		new ErrorLogView().deleteLog();
	}

	@Before
	public void setupClosePaletteView() {
		new WorkbenchShell();
		log.info("Trying to close Palette View (if it's open)");
		try {
			new PaletteView().close();
		} catch (WorkbenchLayerException ex) {
			log.info("Palette view is already closed. Nothing to do.");
		}
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
	 * Test tries to create all endpoint components available in the Palette
	 * view associated with the Camel Editor.
	 * </p>
	 * <b>Steps</b>
	 * <ol>
	 * <li>create a new project with camel-archetype-spring archetype</li>
	 * <li>open Project Explorer view</li>
	 * <li>delete camel-context.xml and create a new empty one</li>
	 * <li>try to create all endpoint components in Palette View</li>
	 * <li>check if the component is present in Camel Editor</li>
	 * <li>delete the component from Camel Editor</li>
	 * </ol>
	 */
	@Test
	public void testComponents() {

		new ProjectExplorer().open();
		new CamelProject("camel-spring").deleteCamelContext("camel-context.xml");
		new CamelProject("camel-spring").createCamelContext("camel-context.xml");
		CamelEditor editor = new CamelEditor("camel-context.xml");

		for (CamelComponent component : CamelComponents.getEndpoints()) {
			new CamelProject("camel-spring").openCamelContext("camel-context.xml");
			log.info("Testing camel component '" + component.getPaletteEntry() + "'");
			editor.activate();
			editor.addCamelComponent(component, "Route _route1");
			editor.deleteCamelComponent(component);
		}
		assertTrue(LogGrapper.getPluginErrors("fuse").size() == 0);
	}
}
