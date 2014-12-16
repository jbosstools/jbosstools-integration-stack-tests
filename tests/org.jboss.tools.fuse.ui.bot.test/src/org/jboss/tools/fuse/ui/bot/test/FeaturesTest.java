package org.jboss.tools.fuse.ui.bot.test;

import static org.junit.Assert.fail;

import org.jboss.reddeer.gef.impl.editpart.LabeledEditPart;
import org.jboss.reddeer.junit.runner.RedDeerSuite;
import org.jboss.reddeer.requirements.cleanworkspace.CleanWorkspaceRequirement.CleanWorkspace;
import org.jboss.reddeer.requirements.openperspective.OpenPerspectiveRequirement.OpenPerspective;
import org.jboss.reddeer.swt.impl.shell.WorkbenchShell;
import org.jboss.tools.fuse.reddeer.editor.CamelEditor;
import org.jboss.tools.fuse.reddeer.perspectives.FuseIntegrationPerspective;
import org.jboss.tools.fuse.reddeer.preference.FuseToolingEditorPreferencePage;
import org.jboss.tools.fuse.reddeer.projectexplorer.CamelProject;
import org.jboss.tools.fuse.ui.bot.test.utils.ProjectFactory;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * Class contains test cases on a variety of feature requests
 * 
 * @author tsedmik
 */
@CleanWorkspace
@OpenPerspective(FuseIntegrationPerspective.class)
@RunWith(RedDeerSuite.class)
public class FeaturesTest {

	@BeforeClass
	public static void setup() {

		new WorkbenchShell().maximize();
	}

	/**
	 * Graphical Editor - Add option to configure if labels should be shown or not
	 * https://issues.jboss.org/browse/FUSETOOLS-837
	 */
	@Test
	public void testLabelOrIdInEditor() {

		ProjectFactory.createProject("camel-spring", "camel-archetype-spring");
		new CamelProject("camel-spring").openCamelContext("camel-context.xml");
		CamelEditor.switchTab("Design");
		CamelEditor editor = new CamelEditor("camel-context.xml");
		editor.setId("file:src/data?noo...", "start");

		// enable "If enabled the ID values will be used for labels if existing"
		FuseToolingEditorPreferencePage prefPage = new FuseToolingEditorPreferencePage();
		prefPage.open();
		prefPage.setShowIDinEditor(true);
		prefPage.ok();
		editor.activate();
		try {
			new LabeledEditPart("start").select();
		} catch (Exception e) {
			fail("'From' endpoint should be named after id value 'start'");
		}
		
		// disable "If enabled the ID values will be used for labels if existing"
		prefPage.open();
		prefPage.setShowIDinEditor(false);
		prefPage.ok();
		editor.activate();
		try {
			new LabeledEditPart("file:src/data?noo...").select();
		} catch (Exception e) {
			fail("'From' endpoint should be named after uri value 'file:src/data?noo...'");
		}
	}
}
