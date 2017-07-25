package org.jboss.tools.fuse.ui.bot.test;

import java.util.Arrays;
import java.util.Collection;

import org.jboss.reddeer.common.exception.RedDeerException;
import org.jboss.reddeer.common.logging.Logger;
import org.jboss.reddeer.eclipse.ui.views.log.LogView;
import org.jboss.reddeer.gef.view.PaletteView;
import org.jboss.reddeer.junit.internal.runner.ParameterizedRequirementsRunnerFactory;
import org.jboss.reddeer.junit.runner.RedDeerSuite;
import org.jboss.reddeer.requirements.autobuilding.AutoBuildingRequirement.AutoBuilding;
import org.jboss.reddeer.requirements.cleanworkspace.CleanWorkspaceRequirement.CleanWorkspace;
import org.jboss.reddeer.workbench.exception.WorkbenchLayerException;
import org.jboss.reddeer.workbench.handler.EditorHandler;
import org.jboss.reddeer.workbench.impl.shell.WorkbenchShell;
import org.jboss.tools.common.reddeer.JiraIssue;
import org.jboss.tools.fuse.reddeer.ProjectType;
import org.jboss.tools.fuse.reddeer.component.Route;
import org.jboss.tools.fuse.reddeer.editor.CamelComponentEditPart;
import org.jboss.tools.fuse.reddeer.editor.CamelEditor;
import org.jboss.tools.fuse.reddeer.projectexplorer.CamelProject;
import org.jboss.tools.fuse.ui.bot.test.utils.ProjectFactory;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized.Parameters;
import org.junit.runners.Parameterized.UseParametersRunnerFactory;

/**
 * Tests:
 * <ul>
 * <li>test for switching between two routes</li>
 * </ul>
 * 
 * Parameters:
 * <ul>
 * <li>SPRING</li>
 * <li>BLUEPRINT</li>
 * </ul>
 * 
 * @author apodhrad
 *
 */
@CleanWorkspace
@AutoBuilding(false)
@RunWith(RedDeerSuite.class)
@UseParametersRunnerFactory(ParameterizedRequirementsRunnerFactory.class)
public class CamelEditorRoutesTest {

	public static final String PROJECT_NAME = "routes";

	protected Logger log = Logger.getLogger(CamelEditorRoutesTest.class);

	@Parameters(name = "{0}")
	public static Collection<ProjectType> data() {
		return Arrays.asList(new ProjectType[] { ProjectType.SPRING, ProjectType.BLUEPRINT });
	}

	private ProjectType type;

	public CamelEditorRoutesTest(ProjectType type) {
		this.type = type;
	}

	/**
	 * Prepares test environment
	 */
	@Before
	public void createProject() {
		new WorkbenchShell();
		ProjectFactory.newProject(PROJECT_NAME).type(type).create();

		LogView view = new LogView();
		view.open();
		view.deleteLog();

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
	public void deleteProjects() {
		new WorkbenchShell();
		EditorHandler.getInstance().closeAll(true);
		ProjectFactory.deleteAllProjects();
	}

	/**
	 * <p>
	 * Test for switching between two routes (FUSETOOLS-1955).
	 * </p>
	 * <ol>
	 * <li>create an empty project</li>
	 * <li>add another route</li>
	 * <li>double click on the first route</li>
	 * <li>double click on camel context in Project Explorer</li>
	 * <li>double click on the second route</li>
	 * <li>double click on camel context in Project Explorer</li>
	 * <li>try to get a list of context buttons</li>
	 * </ol>
	 */
	@Test
	public void testSwitchingBetweenTwoRoutes() {
		CamelEditor editor = new CamelEditor(type.getCamelContext());
		editor.addCamelComponent(new Route(), 10, 10);

		CamelProject camelProject = new CamelProject(PROJECT_NAME);
		new CamelComponentEditPart("Route _route1").doubleClick();
		camelProject.openCamelContext(type.getCamelContext());
		new CamelComponentEditPart("Route _route2").doubleClick();
		camelProject.openCamelContext(type.getCamelContext());

		try {
			new CamelComponentEditPart("Route _route1").getContextButtons();
		} catch (RedDeerException e) {
			throw new JiraIssue("FUSETOOLS-1955", e);
		}
	}

}
