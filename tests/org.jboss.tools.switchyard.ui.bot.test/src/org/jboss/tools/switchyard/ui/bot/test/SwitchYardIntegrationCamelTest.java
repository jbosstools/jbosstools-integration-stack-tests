package org.jboss.tools.switchyard.ui.bot.test;

import static org.junit.Assert.assertEquals;

import org.eclipse.reddeer.eclipse.ui.navigator.resources.ProjectExplorer;
import org.eclipse.reddeer.eclipse.ui.views.properties.PropertySheet;
import org.eclipse.reddeer.junit.requirement.inject.InjectRequirement;
import org.eclipse.reddeer.junit.runner.RedDeerSuite;
import org.eclipse.reddeer.swt.api.CCombo;
import org.eclipse.reddeer.workbench.handler.EditorHandler;
import org.eclipse.reddeer.workbench.impl.editor.TextEditor;
import org.jboss.tools.fuse.reddeer.editor.CamelComponentEditPart;
import org.jboss.tools.fuse.reddeer.editor.CamelEditor;
import org.jboss.tools.switchyard.reddeer.component.Service;
import org.jboss.tools.switchyard.reddeer.component.SwitchYardComponent;
import org.jboss.tools.switchyard.reddeer.editor.SwitchYardEditor;
import org.jboss.tools.switchyard.reddeer.project.SwitchYardProject;
import org.jboss.tools.switchyard.reddeer.requirement.SwitchYardRequirement;
import org.jboss.tools.switchyard.reddeer.requirement.SwitchYardRequirement.SwitchYard;
import org.jboss.tools.switchyard.reddeer.view.JUnitView;
import org.jboss.tools.switchyard.reddeer.wizard.CamelXMLServiceWizard;
import org.jboss.tools.switchyard.reddeer.wizard.ServiceTestClassWizard;
import org.jboss.tools.switchyard.ui.bot.test.util.TemplateHandler;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Test;
import org.junit.runner.RunWith;

@SwitchYard
@RunWith(RedDeerSuite.class)
public class SwitchYardIntegrationCamelTest {

	public static final String PROJECT_NAME = "camel-integration";
	public static final String INTERFACE_NAME = "Hello";
	public static final String ROUTE_NAME = "CamelRoute";
	public static final String PACKAGE = "com.example.switchyard." + PROJECT_NAME.replace('-', '_');
	public static final String EXISTING_CAMEL_JAVA = "MyRouteBuilder";
	public static final String EXISTING_CAMEL_XML = "myroute";

	@InjectRequirement
	private static SwitchYardRequirement switchYardRequirement;

	@After
	public void closeAllEditors() {
		EditorHandler.getInstance().closeAll(true);
	}

	@AfterClass
	public static void deleteSwitchYardProject() {
		saveAndCloseSwitchYardFile();
		new ProjectExplorer().deleteAllProjects();
	}

	public static void saveAndCloseSwitchYardFile() {
		try {
			new SwitchYardEditor().saveAndClose();
		} catch (Exception ex) {
			// it is ok, we just try to close switchyard.xml if it is open
		}
	}

	@Test
	public void switchYardCamelXMLIntegrationTest() throws Exception {
		/* Create SwitchYard Project */
		switchYardRequirement.project(PROJECT_NAME).impl("Camel Route").create();

		/* Create Java Interface */
		TextEditor textEditor = new SwitchYardProject(PROJECT_NAME).createJavaInterface(PACKAGE, "Hello");
		textEditor.setText(TemplateHandler.javaSource("Hello.java", PACKAGE));
		textEditor.close(true);

		/* Add Camel XML implementation */
		CamelXMLServiceWizard camelWizard = new SwitchYardEditor().addCamelXMLImplementation();
		camelWizard.setFileName(ROUTE_NAME).selectJavaInterface("Hello");
		camelWizard.finish();

		/* Edit Camel XML implementation */
		new SwitchYardComponent(ROUTE_NAME).doubleClick();
		CamelEditor camelEditor = new CamelEditor(ROUTE_NAME + ".xml");
		camelEditor.addCamelComponent("Set Body", "Route _route1");
		camelEditor.addConnection("Log _log1", "SetBody _setBody1");
		PropertySheet propertiesView = new PropertySheet();
		propertiesView.open();
		new CamelComponentEditPart("SetBody _setBody1").select();
		camelEditor.setProperty(CCombo.class, "Language", "simple");
		camelEditor.setProperty("Expression", "Hello ${body}");
		camelEditor.save();

		/* Create the test */
		ServiceTestClassWizard testWizard = new Service("Hello").newServiceTestClass();
		testWizard.setPackage(PACKAGE);
		testWizard.setName("HelloTest");
		testWizard.finish();
		textEditor = new TextEditor("HelloTest.java");
		textEditor.setText(TemplateHandler.javaSource("CamelHelloTest.java", PACKAGE));
		textEditor.save();

		/* Run JUnit test */
		new SwitchYardProject(PROJECT_NAME).getTestClass(PACKAGE, "HelloTest.java").runAsJUnitTest();
		JUnitView jUnitView = new JUnitView();
		jUnitView.open();
		assertEquals("1/1", new JUnitView().getRunStatus());
		assertEquals(0, new JUnitView().getNumberOfErrors());
		assertEquals(0, new JUnitView().getNumberOfFailures());
	}
	
}
