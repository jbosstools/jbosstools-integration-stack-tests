package org.jboss.tools.switchyard.ui.bot.test;

import static org.junit.Assert.assertEquals;

import org.jboss.reddeer.graphiti.impl.graphitieditpart.LabeledGraphitiEditPart;
import org.jboss.reddeer.junit.requirement.inject.InjectRequirement;
import org.jboss.reddeer.junit.runner.RedDeerSuite;
import org.jboss.reddeer.swt.impl.shell.WorkbenchShell;
import org.jboss.tools.switchyard.reddeer.editor.SwitchYardEditor;
import org.jboss.tools.switchyard.reddeer.requirement.SwitchYardRequirement;
import org.jboss.tools.switchyard.reddeer.requirement.SwitchYardRequirement.SwitchYard;
import org.jboss.tools.switchyard.reddeer.wizard.CamelJavaWizard;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * 
 * @author apodhrad
 *
 */
@SwitchYard
@RunWith(RedDeerSuite.class)
public class ImplementationCamelTest {

	public static final String PROJECT_NAME = "sy_camel_project";
	public static final String PROJECT_PACKAGE = "com.example.switchyard." + PROJECT_NAME;
	public static final String SERVICE__NAME = "Hello";
	public static final String SERVICE_IMPL_NAME = "HelloImpl";

	@InjectRequirement
	private static SwitchYardRequirement switchYardRequirement;

	@BeforeClass
	public static void maximizeWorkbench() {
		new WorkbenchShell().maximize();
	}

	@BeforeClass
	public static void createProject() {
		switchYardRequirement.project(PROJECT_NAME).create();
	}

	// @After
	public void deleteCamelImplementation() {
		new SwitchYardEditor().getClass();
		new LabeledGraphitiEditPart("HelloCamel");
	}

	@Test
	public void addCamelImplementationWithNewJavaInterfaceTest() throws Exception {
		SwitchYardEditor editor = new SwitchYardEditor();
		CamelJavaWizard camelJavaWizard = editor.addCamelJavaImplementation();
		camelJavaWizard.createJavaInterface(SERVICE__NAME);
		camelJavaWizard.setName(SERVICE_IMPL_NAME);
		camelJavaWizard.finish();

		assertEquals("1", editor.xpath("count(/switchyard/composite/component)"));
		assertEquals(SERVICE_IMPL_NAME, editor.xpath("/switchyard/composite/component/@name"));
		assertEquals("1", editor.xpath("count(/switchyard/composite/component/implementation.camel)"));
		assertEquals("1", editor.xpath("count(/switchyard/composite/component/implementation.camel/java)"));
		assertEquals(PROJECT_PACKAGE + "." + PROJECT_NAME,
				editor.xpath("/switchyard/composite/component/implementation.camel/java/@class"));
	}

}
