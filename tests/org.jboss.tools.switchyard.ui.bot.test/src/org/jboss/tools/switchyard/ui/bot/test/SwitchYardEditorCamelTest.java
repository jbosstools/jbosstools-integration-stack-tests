package org.jboss.tools.switchyard.ui.bot.test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.junit.Assert.assertEquals;

import org.jboss.reddeer.eclipse.jdt.ui.ProjectExplorer;
import org.jboss.reddeer.junit.requirement.inject.InjectRequirement;
import org.jboss.reddeer.junit.runner.RedDeerSuite;
import org.jboss.reddeer.core.condition.JobIsRunning;
import org.jboss.reddeer.core.condition.ShellWithTextIsActive;
import org.jboss.reddeer.core.condition.ShellWithTextIsAvailable;
import org.jboss.reddeer.swt.condition.TableHasRows;
import org.jboss.reddeer.workbench.handler.EditorHandler;
import org.jboss.reddeer.swt.impl.button.PushButton;
import org.jboss.reddeer.swt.impl.menu.ShellMenu;
import org.jboss.reddeer.swt.impl.shell.DefaultShell;
import org.jboss.reddeer.swt.impl.shell.WorkbenchShell;
import org.jboss.reddeer.swt.impl.table.DefaultTable;
import org.jboss.reddeer.swt.impl.text.DefaultText;
import org.jboss.reddeer.swt.impl.text.LabeledText;
import org.jboss.reddeer.swt.impl.tree.DefaultTreeItem;
import org.jboss.reddeer.common.wait.TimePeriod;
import org.jboss.reddeer.common.wait.WaitUntil;
import org.jboss.reddeer.common.wait.WaitWhile;
import org.jboss.reddeer.workbench.impl.editor.DefaultEditor;
import org.jboss.reddeer.workbench.impl.editor.TextEditor;
import org.jboss.tools.fuse.reddeer.editor.CamelEditor;
import org.jboss.tools.switchyard.reddeer.component.SwitchYardComponent;
import org.jboss.tools.switchyard.reddeer.editor.SwitchYardEditor;
import org.jboss.tools.switchyard.reddeer.project.SwitchYardProject;
import org.jboss.tools.switchyard.reddeer.requirement.SwitchYardRequirement;
import org.jboss.tools.switchyard.reddeer.requirement.SwitchYardRequirement.SwitchYard;
import org.jboss.tools.switchyard.reddeer.wizard.CamelJavaServiceWizard;
import org.jboss.tools.switchyard.reddeer.wizard.CamelXMLServiceWizard;
import org.jboss.tools.switchyard.reddeer.wizard.ImportFileWizard;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;

@SwitchYard
@RunWith(RedDeerSuite.class)
public class SwitchYardEditorCamelTest {

	public static final String PROJECT_NAME = "camelimpl";
	public static final String INTERFACE_NAME = "Hello";
	public static final String ROUTE_NAME = "CamelRoute";
	public static final String PACKAGE = "com.example.switchyard." + PROJECT_NAME;
	public static final String EXISTING_CAMEL_JAVA = "MyRouteBuilder";
	public static final String EXISTING_CAMEL_XML = "myroute";

	@InjectRequirement
	private static SwitchYardRequirement switchYardRequirement;

	@BeforeClass
	public static void createSwitchYardProject() {
		saveAndCloseSwitchYardFile();

		new WorkbenchShell().maximize();
		new ProjectExplorer().open();

		/* Create SwitchYard Project */
		switchYardRequirement.project(PROJECT_NAME).create();

		/* Create Java Interface */
		new SwitchYardProject(PROJECT_NAME).select();
		new ShellMenu("File", "New", "Other...").select();
		new DefaultShell("New");
		new DefaultTreeItem("Java", "Interface").select();
		new PushButton("Next >").click();
		new DefaultShell("New Java Interface");
		new LabeledText("Name:").setText(INTERFACE_NAME);
		new PushButton("Finish").click();
		new WaitWhile(new ShellWithTextIsActive("New Java Interface"));
		new WaitWhile(new JobIsRunning(), TimePeriod.LONG);
	}

	@After
	public void closeAllEditors() {
		try {
			new SwitchYardComponent(ROUTE_NAME).delete();
		} catch (Exception ex) {
			// this is ok
		}
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
	public void camelJavaTest() throws Exception {
		CamelJavaServiceWizard wizard = new SwitchYardProject(PROJECT_NAME).openSwitchYardFile().addCamelJavaImplementation();
		wizard.setName(ROUTE_NAME).selectJavaInterface("Hello").finish();

		SwitchYardEditor editor = new SwitchYardEditor();
		editor.save();
		String componentQuery = "/switchyard/composite/component[@name='" + ROUTE_NAME + "']";
		String className = editor.xpath(componentQuery + "/implementation.camel/java/@class");
		assertEquals(PACKAGE + "." + ROUTE_NAME, className);
		String serviceName = editor.xpath(componentQuery + "/service/@name");
		assertEquals(INTERFACE_NAME, serviceName);

		new SwitchYardComponent(ROUTE_NAME).doubleClick();
		String content = new TextEditor(ROUTE_NAME + ".java").getText();
		assertContains("public class " + ROUTE_NAME + " extends RouteBuilder", content);
		assertContains("public void configure()", content);
		assertContains("from(\"switchyard://" + INTERFACE_NAME + "\")", content);
		new DefaultEditor(ROUTE_NAME + ".java").close();
	}

	@Test
	public void bottomUpCamelJavaTest() throws Exception {
		// Import java file
		new SwitchYardProject(PROJECT_NAME).getProjectItem("src/main/java", PACKAGE).select();
		new ImportFileWizard().importFile("resources/camel", EXISTING_CAMEL_JAVA + ".java");

		// Add existing Camel Java implementation
		new SwitchYardProject(PROJECT_NAME).openSwitchYardFile().addComponent();
		new SwitchYardComponent("Component").setLabel(ROUTE_NAME);
		new SwitchYardEditor().addCamelJavaImplementation(new SwitchYardComponent(ROUTE_NAME));
		new DefaultShell("Camel Implementation");
		new PushButton("Browse...").click();
		new DefaultShell("Select entries");
		new DefaultText().setText(EXISTING_CAMEL_JAVA);
		new WaitUntil(new TableHasRows(new DefaultTable()), TimePeriod.LONG);
		new PushButton("OK").click();
		new WaitWhile(new ShellWithTextIsAvailable("Select entries"));
		new WaitWhile(new JobIsRunning(), TimePeriod.LONG);
		new DefaultShell("Camel Implementation");
		new PushButton("Finish").click();
		new WaitWhile(new ShellWithTextIsAvailable("Camel Implementation"));
		new WaitWhile(new JobIsRunning(), TimePeriod.LONG);
		new SwitchYardEditor().save();

		// Check whether the result was correctly generated
		SwitchYardEditor editor = new SwitchYardEditor();
		editor.save();
		String componentQuery = "/switchyard/composite/component[@name='" + ROUTE_NAME + "']";
		String className = editor.xpath(componentQuery + "/implementation.camel/java/@class");
		assertEquals(PACKAGE + "." + EXISTING_CAMEL_JAVA, className);
	}

	@Test
	public void camelXmlTest() throws Exception {
		CamelXMLServiceWizard wizard = new SwitchYardProject(PROJECT_NAME).openSwitchYardFile().addCamelXMLImplementation();
		wizard.setFileName(ROUTE_NAME).selectJavaInterface(INTERFACE_NAME).finish();

		SwitchYardEditor editor = new SwitchYardEditor();
		editor.save();
		String componentQuery = "/switchyard/composite/component[@name='" + ROUTE_NAME + "']";
		String path = editor.xpath(componentQuery + "/implementation.camel/xml/@path");
		assertEquals(ROUTE_NAME + ".xml", path);
		String serviceName = editor.xpath(componentQuery + "/service/@name");
		assertEquals(INTERFACE_NAME, serviceName);

		new SwitchYardComponent(ROUTE_NAME).doubleClick();
		new CamelEditor(ROUTE_NAME + ".xml");
	}

	@Test
	public void bottomUpCamelXmlTest() throws Exception {
		// Import XML file
		new SwitchYardProject(PROJECT_NAME).getProjectItem("src/main/resources").select();
		new ImportFileWizard().importFile("resources/camel", EXISTING_CAMEL_XML + ".xml");

		// Add existing Camel XML implementation
		new SwitchYardProject(PROJECT_NAME).openSwitchYardFile().addComponent();
		new SwitchYardComponent("Component").setLabel(ROUTE_NAME);
		new SwitchYardEditor().addCamelXmlImplementation(new SwitchYardComponent(ROUTE_NAME));
		new DefaultShell("");
		new PushButton("Browse...").click();
		new DefaultShell("Select Route XML File from Project");
		new DefaultText().setText("myroute.xml");
		new WaitUntil(new TableHasRows(new DefaultTable()), TimePeriod.LONG);
		new PushButton("OK").click();
		new WaitWhile(new ShellWithTextIsAvailable("Select Route XML File from Project"));
		new WaitWhile(new JobIsRunning(), TimePeriod.LONG);
		new DefaultShell("");
		new PushButton("Finish").click();
		new WaitWhile(new ShellWithTextIsAvailable(""));
		new WaitWhile(new JobIsRunning(), TimePeriod.LONG);
		new SwitchYardEditor().save();

		// Check whether the result was correctly generated
		SwitchYardEditor editor = new SwitchYardEditor();
		editor.save();
		String componentQuery = "/switchyard/composite/component[@name='" + ROUTE_NAME + "']";
		String path = editor.xpath(componentQuery + "/implementation.camel/xml/@path");
		assertEquals("myroute.xml", path);
	}
	

	/**
	 * Asserts that the <code>needle</code> is contained within the <code>hayStack</code>.
	 * 
	 * @param needle the text to search in the <code>hayStack</code>.
	 * @param hayStack the text to look within.
	 */
	public static void assertContains(String needle, String hayStack) {
		assertThat(hayStack, containsString(needle));
	}
}
