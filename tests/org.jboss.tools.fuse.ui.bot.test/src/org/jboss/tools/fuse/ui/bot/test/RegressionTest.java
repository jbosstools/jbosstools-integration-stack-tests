package org.jboss.tools.fuse.ui.bot.test;

import static org.jboss.reddeer.eclipse.ui.problems.ProblemsView.ProblemType.ERROR;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.util.Scanner;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.jboss.reddeer.common.exception.WaitTimeoutExpiredException;
import org.jboss.reddeer.common.matcher.RegexMatcher;
import org.jboss.reddeer.common.wait.AbstractWait;
import org.jboss.reddeer.common.wait.TimePeriod;
import org.jboss.reddeer.common.wait.WaitUntil;
import org.jboss.reddeer.core.condition.ShellWithTextIsAvailable;
import org.jboss.reddeer.core.exception.CoreLayerException;
import org.jboss.reddeer.core.matcher.WithTooltipTextMatcher;
import org.jboss.reddeer.eclipse.debug.core.IsSuspended;
import org.jboss.reddeer.eclipse.jdt.ui.ProjectExplorer;
import org.jboss.reddeer.eclipse.ui.console.ConsoleView;
import org.jboss.reddeer.junit.execution.annotation.RunIf;
import org.jboss.reddeer.junit.runner.RedDeerSuite;
import org.jboss.reddeer.requirements.openperspective.OpenPerspectiveRequirement.OpenPerspective;
import org.jboss.reddeer.swt.impl.button.CancelButton;
import org.jboss.reddeer.swt.impl.button.CheckBox;
import org.jboss.reddeer.swt.impl.button.OkButton;
import org.jboss.reddeer.swt.impl.button.PushButton;
import org.jboss.reddeer.swt.impl.ctab.DefaultCTabItem;
import org.jboss.reddeer.swt.impl.menu.ContextMenu;
import org.jboss.reddeer.swt.impl.menu.ShellMenu;
import org.jboss.reddeer.swt.impl.shell.DefaultShell;
import org.jboss.reddeer.swt.impl.styledtext.DefaultStyledText;
import org.jboss.reddeer.swt.impl.toolbar.DefaultToolItem;
import org.jboss.reddeer.swt.impl.tree.DefaultTreeItem;
import org.jboss.reddeer.workbench.impl.editor.DefaultEditor;
import org.jboss.reddeer.workbench.impl.shell.WorkbenchShell;
import org.jboss.tools.common.reddeer.LogGrapper;
import org.jboss.tools.common.reddeer.ResourceHelper;
import org.jboss.tools.common.reddeer.condition.IssueIsClosed;
import org.jboss.tools.common.reddeer.condition.IssueIsClosed.Jira;
import org.jboss.tools.common.reddeer.ext.PropertiesViewExt;
import org.jboss.tools.common.reddeer.view.ErrorLogView;
import org.jboss.tools.common.reddeer.view.ProblemsViewExt;
import org.jboss.tools.common.reddeer.widget.LabeledTextExt;
import org.jboss.tools.fuse.reddeer.ProjectTemplate;
import org.jboss.tools.fuse.reddeer.ProjectType;
import org.jboss.tools.fuse.reddeer.editor.CamelEditor;
import org.jboss.tools.fuse.reddeer.perspectives.FuseIntegrationPerspective;
import org.jboss.tools.fuse.reddeer.projectexplorer.CamelProject;
import org.jboss.tools.fuse.reddeer.view.FuseJMXNavigator;
import org.jboss.tools.fuse.ui.bot.test.utils.EditorManipulator;
import org.jboss.tools.fuse.ui.bot.test.utils.ProjectFactory;
import org.jboss.tools.runtime.reddeer.preference.FuseServerRuntimePreferencePage;
import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.w3c.dom.Document;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

/**
 * Class contains test cases verifying resolved issues
 * 
 * @author tsedmik
 */
@OpenPerspective(FuseIntegrationPerspective.class)
@RunWith(RedDeerSuite.class)
public class RegressionTest extends DefaultTest {

	@After
	public void setupClean() {

		ProjectFactory.deleteAllProjects();
	}

	/**
	 * <p>
	 * GUI editor issue when using route scoped onException
	 * </p>
	 * <b>Link: </b>
	 * <a href="https://issues.jboss.org/browse/FUSETOOLS-674">https://issues.jboss.org/browse/FUSETOOLS-674</a> <br>
	 * <a href="https://issues.jboss.org/browse/FUSETOOLS-1728">https://issues.jboss.org/browse/FUSETOOLS-1728</a> <br>
	 * <a href="https://issues.jboss.org/browse/FUSETOOLS-1729">https://issues.jboss.org/browse/FUSETOOLS-1729</a> <br>
	 */
	@Test
	public void issue_674_1728_1729() throws ParserConfigurationException, SAXException, IOException {

		ProjectFactory.newProject("camel-spring").template(ProjectTemplate.CBR).type(ProjectType.SPRING).create();
		new CamelProject("camel-spring").openCamelContext("camel-context.xml");
		new DefaultCTabItem("Source").activate();

		// copy sample of camel-context.xml
		File testFile = new File(
				ResourceHelper.getResourceAbsolutePath(Activator.PLUGIN_ID, "resources/camel-context.xml"));
		DefaultStyledText editor = new DefaultStyledText();
		Scanner scanner = new Scanner(testFile);
		scanner.useDelimiter("\\Z");
		editor.setText(scanner.next());
		scanner.close();

		new DefaultCTabItem("Design").activate();
		new DefaultCTabItem("Source").activate();
		new ShellMenu("File", "Save").select();

		// check XML content
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder = factory.newDocumentBuilder();
		InputSource is = new InputSource(new StringReader(new DefaultStyledText().getText()));
		Document doc = builder.parse(is);
		int i = doc.getElementsByTagName("onException").item(0).getChildNodes().getLength();

		assertEquals("'camel-context.xml' file was changed!", 11, i);
	}

	/**
	 * <p>
	 * Propose a DebugAs option to start CamelContext & debug java code used by beans from camel routes
	 * </p>
	 * <b>Link: </b>
	 * <a href="https://issues.jboss.org/browse/FUSETOOLS-853">https://issues.jboss.org/browse/FUSETOOLS-853</a>
	 */
	@Test
	public void issue_853() {

		ProjectFactory.newProject("camel-blueprint").template(ProjectTemplate.CBR).type(ProjectType.BLUEPRINT).create();
		new ProjectExplorer().getProject("camel-blueprint")
				.getProjectItem("src/main/resources", "OSGI-INF", "blueprint", "blueprint.xml").select();
		try {
			new ContextMenu("Debug As", "3 Local Camel Context (without tests)");
		} catch (Exception e) {
			fail("Context menu 'Debug As --> Local Camel Context' is missing");
		}
	}

	/**
	 * <p>
	 * New Server Runtime Wizard - Finish button error
	 * </p>
	 * <b>Link: </b>
	 * <a href="https://issues.jboss.org/browse/FUSETOOLS-1076">https://issues.jboss.org/browse/FUSETOOLS-1076</a>
	 */
	@Test
	public void issue_1076() {

		FuseServerRuntimePreferencePage serverRuntime = new FuseServerRuntimePreferencePage();
		serverRuntime.open();
		new PushButton("Add...").click();
		new WaitUntil(new ShellWithTextIsAvailable("New Server Runtime Environment"));
		new DefaultShell("New Server Runtime Environment").setFocus();
		AbstractWait.sleep(TimePeriod.SHORT);
		new DefaultTreeItem("JBoss Fuse", "JBoss Fuse 6.1").select();
		if (new PushButton("Finish").isEnabled()) {
			new PushButton("Cancel").click();
			new DefaultShell("Preferences").close();
			fail("'Finish' button should not be enabled!");
		}
	}

	/**
	 * <p>
	 * JMX Navigator - prevent from close Camel Context
	 * </p>
	 * <b>Link: </b>
	 * <a href="https://issues.jboss.org/browse/FUSETOOLS-1115">https://issues.jboss.org/browse/FUSETOOLS-1115</a>
	 */
	@Test
	public void issue_1115() {

		ProjectFactory.newProject("camel-spring").template(ProjectTemplate.CBR).type(ProjectType.SPRING).create();
		new CamelProject("camel-spring").runCamelContext("camel-context.xml");
		AbstractWait.sleep(TimePeriod.NORMAL);
		new FuseJMXNavigator().getNode("Local Camel Context", "Camel", "cbr-example-context").select();

		try {
			new ContextMenu("Close Camel Context");
		} catch (CoreLayerException ex) {
			return;
		} finally {
			new ConsoleView().terminateConsole();
		}

		fail("Context menu item 'Close Camel Context' is available!");
	}

	/**
	 * <p>
	 * context id is removed on save
	 * </p>
	 * <b>Link: </b>
	 * <a href="https://issues.jboss.org/browse/FUSETOOLS-1123">https://issues.jboss.org/browse/FUSETOOLS-1123</a>
	 * 
	 * @throws FuseTemplateNotFoundException
	 *             Fuse archetype was not found. Tests cannot be executed!
	 */
	@Test
	public void issue_1123() {

		ProjectFactory.newProject("camel-spring").template(ProjectTemplate.CBR).type(ProjectType.SPRING).create();
		new CamelProject("camel-spring").openCamelContext("camel-context.xml");
		CamelEditor.switchTab("Source");
		EditorManipulator.copyFileContentToCamelXMLEditor("resources/camel-context-routeContextId.xml");
		CamelEditor.switchTab("Design");
		CamelEditor.switchTab("Source");
		String editorText = new DefaultStyledText().getText();
		assertTrue(editorText.contains("id=\"test\""));
	}

	/**
	 * <p>
	 * New Fuse Project - Finish button
	 * </p>
	 * <b>Link: </b>
	 * <a href="https://issues.jboss.org/browse/FUSETOOLS-1149">https://issues.jboss.org/browse/FUSETOOLS-1149</a>
	 */
	@Test
	public void issue_1149() {

		new ShellMenu("File", "New", "Fuse Integration Project").select();
		new DefaultShell("New Fuse Integration Project");
		if (new PushButton("Finish").isEnabled()) {
			new DefaultShell().close();
			fail("'Finish' button should not be enabled!");
		}
	}

	/**
	 * <p>
	 * fix ugly title when debugging
	 * </p>
	 * <b>Link: </b>
	 * <a href="https://issues.jboss.org/browse/FUSETOOLS-1158">https://issues.jboss.org/browse/FUSETOOLS-1158</a>
	 */
	@Test
	public void issue_1158() {

		ProjectFactory.newProject("camel-spring").template(ProjectTemplate.CBR).type(ProjectType.SPRING).create();
		CamelProject project = new CamelProject("camel-spring");
		project.openCamelContext("camel-context.xml");
		CamelEditor editor = new CamelEditor("camel-context.xml");
		editor.activate();
		editor.setBreakpoint("Choice");
		editor.selectEditPart("file:work/cbr/input");
		editor.setProperty("Uri *", "file:src/main/data?noop=true");
		editor.save();
		project.debugCamelContextWithoutTests("camel-context.xml");
		new WaitUntil(new IsSuspended(), TimePeriod.LONG);
		try {
			new DefaultEditor(new RegexMatcher("camel-context.xml"));
		} catch (Exception e) {
			fail("Debuger Editor has wrong name");
		} finally {
			new ConsoleView().terminateConsole();
		}
	}

	/**
	 * <p>
	 * remove use of the customId attribute
	 * </p>
	 * <b>Link: </b>
	 * <a href="https://issues.jboss.org/browse/FUSETOOLS-1172">https://issues.jboss.org/browse/FUSETOOLS-1172</a>
	 */
	@Test
	public void issue_1172() throws ParserConfigurationException, SAXException, IOException {

		ProjectFactory.newProject("camel-spring").template(ProjectTemplate.CBR).type(ProjectType.SPRING).create();
		new CamelProject("camel-spring").openCamelContext("camel-context.xml");
		CamelEditor.switchTab("Design");
		CamelEditor editor = new CamelEditor("camel-context.xml");
		editor.click(5, 5);
		editor.setProperty("Route cbr-route", "Id", "1");
		editor.save();
		CamelEditor.switchTab("Source");

		// check XML content
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder = factory.newDocumentBuilder();
		InputSource is = new InputSource(new StringReader(new DefaultStyledText().getText()));
		Document doc = builder.parse(is);
		assertNull(doc.getElementsByTagName("route").item(0).getAttributes().getNamedItem("customId"));
		editor.close(true);
	}

	/**
	 * <p>
	 * Opening Camel Editor on Source tab + dirty flag
	 * </p>
	 * <b>Link: </b>
	 * <a href="https://issues.jboss.org/browse/FUSETOOLS-1243">https://issues.jboss.org/browse/FUSETOOLS-1243</a>
	 */
	@Test
	public void issue_1243() {

		ProjectFactory.newProject("camel-spring").template(ProjectTemplate.CBR).type(ProjectType.SPRING).create();
		new CamelProject("camel-spring").openCamelContext("camel-context.xml");
		assertFalse("Camel editor should not be dirty",
				new DefaultToolItem(new WorkbenchShell(), 1, new WithTooltipTextMatcher(new RegexMatcher("Save.*")))
						.isEnabled());
		CamelEditor.switchTab("Source");
		assertFalse("Camel editor should not be dirty",
				new DefaultToolItem(new WorkbenchShell(), 1, new WithTooltipTextMatcher(new RegexMatcher("Save.*")))
						.isEnabled());
		DefaultEditor editor = new DefaultEditor("camel-context.xml");
		editor.activate();
		editor.close();
	}

	/**
	 * <p>
	 * Run Configurations dialog shows launch config types for server adapters for Karaf, SMX, Fuse and Fabric8 which
	 * partially don't work
	 * </p>
	 * <b>Link: </b>
	 * <a href="https://issues.jboss.org/browse/FUSETOOLS-1214">https://issues.jboss.org/browse/FUSETOOLS-1214</a>
	 */
	@Test
	public void issue_1214() {

		ProjectFactory.newProject("camel-spring").template(ProjectTemplate.CBR).type(ProjectType.SPRING).create();
		new ProjectExplorer().selectProjects("camel-spring");
		new ContextMenu("Run As", "Run Configurations...").select();
		new WaitUntil(new ShellWithTextIsAvailable("Run Configurations"));
		new DefaultShell("Run Configurations");
		new DefaultTreeItem("Java Application").select();
		try {
			new DefaultTreeItem("Apache Karaf Launcher").select();
			fail("Run Configurations contains forbidden item");
		} catch (CoreLayerException e) {
		}
		try {
			new DefaultTreeItem("Apache ServiceMix Launcher").select();
			fail("Run Configurations contains forbidden item");
		} catch (CoreLayerException e) {
		}
		try {
			new DefaultTreeItem("Fabric8 Launcher").select();
			fail("Run Configurations contains forbidden item");
		} catch (CoreLayerException e) {
		}
		try {
			new DefaultTreeItem("JBoss Fuse Launcher").select();
			fail("Run Configurations contains forbidden item");
		} catch (CoreLayerException e) {
		}
	}

	/**
	 * <p>
	 * Camel Editor is still indicating that something was changed
	 * </p>
	 * <b>Link: </b>
	 * <a href="https://issues.jboss.org/browse/FUSETOOLS-1403">https://issues.jboss.org/browse/FUSETOOLS-1403</a>
	 */
	@Test
	public void issue_1403() {

		ProjectFactory.newProject("camel-blueprint").template(ProjectTemplate.CBR).type(ProjectType.BLUEPRINT).create();
		new CamelProject("camel-blueprint").selectProjectItem("src/main/resources", "OSGI-INF", "blueprint",
				"blueprint.xml");
		new ContextMenu("Open").select();
		assertFalse("Camel Editor is dirty! But no editing was performed.", new CamelEditor("blueprint.xml").isDirty());
	}

	/**
	 * <p>
	 * Node not deleted if trying to delete two elements sequentially which are the starting point of the route
	 * </p>
	 * <b>Link: </b>
	 * <a href="https://issues.jboss.org/browse/FUSETOOLS-1701">https://issues.jboss.org/browse/FUSETOOLS-1701</a>
	 */
	@Test
	@Jira("FUSETOOLS-1701")
	@RunIf(conditionClass = IssueIsClosed.class)
	public void issue_1701() {
		ProjectFactory.newProject("test").type(ProjectType.BLUEPRINT).create();
		CamelEditor editor = new CamelEditor("blueprint.xml");
		editor.activate();
		editor.addComponent("File", "Route _route1");
		editor.selectEditPart("file:directoryName");
		editor.setProperty("Uri *", "file:src/main/data?noop=true");
		editor.addComponent("File", "file:src/main/data?noop=true");
		editor.addComponent("Log", "file:directoryName");
		editor.setProperty("Message *", "XXX");
		editor.save();
		new ErrorLogView().deleteLog();
		editor.activate();
		editor.deleteCamelComponent("file:src/main/data?noop=true");
		editor.deleteCamelComponent("file:directoryName");
		editor.save();
		assertFalse("Deleted component is still present in Camel Editor!", editor.isComponentAvailable("file:directoryName"));
		assertTrue("There are some errors in Error Log!", LogGrapper.getPluginErrors("fuse").size() == 0);
	}

	/**
	 * <p>
	 * Double-click on an error in problems View of a Fuse error when corresponding editor is closed don't select the correct node.
	 * </p>
	 * <b>Link: </b>
	 * <a href="https://issues.jboss.org/browse/FUSETOOLS-1726">https://issues.jboss.org/browse/FUSETOOLS-1726</a>
	 */
	@Test
	@Jira("FUSETOOLS-1726")
	@RunIf(conditionClass = IssueIsClosed.class)
	public void issue_1726() {
		ProjectFactory.newProject("test").type(ProjectType.BLUEPRINT).create();
		CamelEditor editor = new CamelEditor("blueprint.xml");
		editor.activate();
		editor.addComponent("IMAP", "Route _route1");
		editor.close();
		new ErrorLogView().deleteLog();
		ProblemsViewExt problems = new ProblemsViewExt();
		problems.open();
		AbstractWait.sleep(TimePeriod.getCustom(5));
		problems.doubleClickProblem("The parameter port requires a numeric value.", ERROR);
		AbstractWait.sleep(TimePeriod.getCustom(5));
		try {
			new CamelEditor("blueprint.xml");
		} catch (Exception e) {
			fail ("Camel Editor was not opened after double-click on a problem in Problems view!");
		}
		PropertiesViewExt properties = new PropertiesViewExt();
		properties.activate();
		try {
			properties.selectTab("Details");
			String title = new LabeledTextExt("Uri *").getText();
			assertEquals("imap:host:port", title);
		} catch (Exception e) {
			fail ("Properties view does not contains properties of appropriate Camel Component!");
		}
	}

	/**
	 * <p>
	 * Error during a project deletion if switched to source after saving modifications in design editor
	 * </p>
	 * <b>Link:</b>
	 * <a href="https://issues.jboss.org/browse/FUSETOOLS-1730">https://issues.jboss.org/browse/FUSETOOLS-1730</a>
	 */
	@Test
	public void issue_1730() {
		ProjectFactory.newProject("camel-spring").template(ProjectTemplate.CBR).type(ProjectType.SPRING).create();
		CamelEditor editor = new CamelEditor("camel-context.xml");
		editor.activate();
		editor.setId("Route cbr-route", "1");
		editor.save();
		CamelEditor.switchTab("Source");
		new ProjectExplorer().getProject("camel-spring").select();
		new ContextMenu("Delete").select();
		new WaitUntil(new ShellWithTextIsAvailable("Delete Resources"));
		new DefaultShell("Delete Resources");
		new CheckBox().toggle(true);
		new OkButton().click();
		AbstractWait.sleep(TimePeriod.SHORT);
		try {
			// issue occurred!
			new WaitUntil(new ShellWithTextIsAvailable("Delete Resources"));
			new CancelButton().click();
			// perform workaround to project deletion
			new DefaultEditor().close();
			new ProjectExplorer().deleteAllProjects();
			fail("Error during project deletion occurred - see https://issues.jboss.org/browse/FUSETOOLS-1730");
		} catch (WaitTimeoutExpiredException e) {
			// issue is not present - great :-)
		}
	}
}
