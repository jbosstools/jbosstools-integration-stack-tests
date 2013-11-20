package org.jboss.tools.fuse.ui.bot.test;

import org.jboss.reddeer.eclipse.jdt.ui.ProjectExplorer;
import org.jboss.reddeer.junit.logging.Logger;
import org.jboss.reddeer.swt.impl.shell.WorkbenchShell;
import org.jboss.reddeer.swt.test.RedDeerTest;
import org.jboss.tools.fuse.reddeer.component.CamelComponent;
import org.jboss.tools.fuse.reddeer.component.CamelComponents;
import org.jboss.tools.fuse.reddeer.editor.CamelEditor;
import org.jboss.tools.fuse.reddeer.editor.GefEditor;
import org.jboss.tools.fuse.reddeer.projectexplorer.CamelProject;
import org.jboss.tools.fuse.reddeer.wizard.FuseProjectWizard;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestWatcher;
import org.junit.runner.Description;

/**
 * 
 * @author apodhrad
 * 
 */
public class CamelEditorTest extends RedDeerTest {

	protected Logger log = Logger.getLogger(CamelEditorTest.class);

	@Rule
	public GefWatchdog gefWatchdog = new GefWatchdog();

	@Test
	public void camelEditorTest() {
		new WorkbenchShell().maximize();

		/* Create fuse project */
		FuseProjectWizard projectWizard = new FuseProjectWizard();
		projectWizard.open();
		projectWizard.next();
		projectWizard.setFilter("camel-archetype-spring");
		projectWizard.selectFirstArchetype();
		projectWizard.finish();

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
