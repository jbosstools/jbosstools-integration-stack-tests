package org.jboss.tools.fuse.ui.bot.test;

import org.jboss.reddeer.common.logging.Logger;
import org.jboss.reddeer.eclipse.jdt.ui.ProjectExplorer;
import org.jboss.reddeer.eclipse.ui.perspectives.JavaEEPerspective;
import org.jboss.reddeer.junit.runner.RedDeerSuite;
import org.jboss.reddeer.requirements.cleanworkspace.CleanWorkspaceRequirement.CleanWorkspace;
import org.jboss.reddeer.requirements.openperspective.OpenPerspectiveRequirement.OpenPerspective;
import org.jboss.reddeer.swt.impl.shell.WorkbenchShell;
import org.jboss.tools.fuse.reddeer.component.CamelComponent;
import org.jboss.tools.fuse.reddeer.component.CamelComponents;
import org.jboss.tools.fuse.reddeer.editor.CamelEditor;
import org.jboss.tools.fuse.reddeer.editor.GefEditor;
import org.jboss.tools.fuse.reddeer.projectexplorer.CamelProject;
import org.jboss.tools.fuse.ui.bot.test.utils.ProjectFactory;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestWatcher;
import org.junit.runner.Description;
import org.junit.runner.RunWith;

/**
 * Tests creation of all components in Fuse Camel editor
 * 
 * @author apodhrad
 */
@CleanWorkspace
@OpenPerspective(JavaEEPerspective.class)
@RunWith(RedDeerSuite.class)
public class CamelEditorTest {

	protected Logger log = Logger.getLogger(CamelEditorTest.class);

	@Rule
	public GefWatchdog gefWatchdog = new GefWatchdog();

	@Test
	public void camelEditorTest() {

		new WorkbenchShell().maximize();

		// Create fuse project
		ProjectFactory.createProject("camel-archetype-spring");

		new ProjectExplorer().open();
		new CamelProject("camel-spring").deleteCamelContext("camel-context.xml");
		new CamelProject("camel-spring").createCamelContext("camel-context.xml");

		for (CamelComponent component : CamelComponents.getAll()) {
			new CamelProject("camel-spring").openCamelContext("camel-context.xml");
			log.info("Testing camel component '" + component.getPaletteEntry() + "'");
			CamelEditor editor = new CamelEditor("camel-context.xml");
			editor.addCamelComponent(component);
			editor.deleteCamelComponent(component);
			editor.close();
		}

		System.out.println();
	}

	public class GefWatchdog extends TestWatcher {

		@Override
		protected void failed(Throwable e, Description description) {
			new GefEditor().printAllFigures();
			super.failed(e, description);
		}
	}
}
