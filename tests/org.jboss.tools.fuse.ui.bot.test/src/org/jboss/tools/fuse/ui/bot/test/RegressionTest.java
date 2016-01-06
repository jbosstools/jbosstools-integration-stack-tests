package org.jboss.tools.fuse.ui.bot.test;

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

import org.jboss.reddeer.common.matcher.RegexMatcher;
import org.jboss.reddeer.common.wait.AbstractWait;
import org.jboss.reddeer.common.wait.TimePeriod;
import org.jboss.reddeer.common.wait.WaitUntil;
import org.jboss.reddeer.core.condition.ShellWithTextIsAvailable;
import org.jboss.reddeer.core.exception.CoreLayerException;
import org.jboss.reddeer.core.matcher.WithTooltipTextMatcher;
import org.jboss.reddeer.eclipse.condition.ConsoleHasText;
import org.jboss.reddeer.eclipse.jdt.ui.ProjectExplorer;
import org.jboss.reddeer.eclipse.ui.console.ConsoleView;
import org.jboss.reddeer.junit.runner.RedDeerSuite;
import org.jboss.reddeer.requirements.openperspective.OpenPerspectiveRequirement.OpenPerspective;
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
import org.jboss.tools.fuse.reddeer.component.Log;
import org.jboss.tools.fuse.reddeer.debug.IsSuspended;
import org.jboss.tools.fuse.reddeer.editor.CamelEditor;
import org.jboss.tools.fuse.reddeer.perspectives.FuseIntegrationPerspective;
import org.jboss.tools.fuse.reddeer.projectexplorer.CamelProject;
import org.jboss.tools.fuse.reddeer.utils.ResourceHelper;
import org.jboss.tools.fuse.reddeer.view.JMXNavigator;
import org.jboss.tools.fuse.ui.bot.test.utils.EditorManipulator;
import org.jboss.tools.fuse.ui.bot.test.utils.FuseArchetypeNotFoundException;
import org.jboss.tools.fuse.ui.bot.test.utils.ProjectFactory;
import org.jboss.tools.runtime.reddeer.preference.FuseServerRuntimePreferencePage;
import org.junit.After;
import org.junit.Ignore;
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
	 * <i>NOTE: not fixed yet - deferred to 8.0</i>
	 */
	@Ignore
	@Test
	public void issue_674()
			throws ParserConfigurationException, SAXException, IOException, FuseArchetypeNotFoundException {

		ProjectFactory.createProject("camel-spring", "camel-archetype-spring");
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
	 * 
	 * @throws FuseArchetypeNotFoundException
	 */
	@Test
	public void issue_853() throws FuseArchetypeNotFoundException {

		ProjectFactory.createProject("camel-blueprint", "camel-archetype-blueprint");
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
		new DefaultShell("New Server Runtime Environment").setFocus();
		new DefaultTreeItem("JBoss Fuse", "JBoss Fuse 6.1").select();
		if (new PushButton("Finish").isEnabled()) {
			new PushButton("Cancel").click();
			new DefaultShell("Preferences").close();
			fail("'Finish' button should not be enabled!");
		}
	}

	/**
	 * <p>
	 * camel context won't run without tests in eclipse kepler
	 * </p>
	 * <b>Link: </b>
	 * <a href="https://issues.jboss.org/browse/FUSETOOLS-1077">https://issues.jboss.org/browse/FUSETOOLS-1077</a>
	 * 
	 * @throws FuseArchetypeNotFoundException
	 *             Fuse archetype was not found. Tests cannot be executed!
	 */
	@Test
	public void issue_1077() throws FuseArchetypeNotFoundException {

		ProjectFactory.createProject("camel-web", "camel-archetype-web");
		new CamelProject("camel-web").runApplicationContextWithoutTests("applicationContext.xml");
		new WaitUntil(new ConsoleHasText("[INFO] Started Jetty Server"), TimePeriod.VERY_LONG);
		new WaitUntil(new ConsoleHasText("Hello Web Application, how are you?"), TimePeriod.LONG);
	}

	/**
	 * <p>
	 * An endpoint is lost after saving
	 * </p>
	 * <b>Link: </b>
	 * <a href="https://issues.jboss.org/browse/FUSETOOLS-1085">https://issues.jboss.org/browse/FUSETOOLS-1085</a>
	 * 
	 * @throws FuseArchetypeNotFoundException
	 *             Fuse archetype was not found. Tests cannot be executed!
	 */
	@Test
	public void issue_1085() throws FuseArchetypeNotFoundException {

		ProjectFactory.createProject("camel-spring", "camel-archetype-spring");
		new CamelProject("camel-spring").openCamelContext("camel-context.xml");
		CamelEditor editor = new CamelEditor("camel-context.xml");
		editor.addCamelComponent(new Log());
		new DefaultToolItem(new WorkbenchShell(), 0, new WithTooltipTextMatcher(new RegexMatcher("Save All.*")))
				.click();
		new WaitUntil(new ShellWithTextIsAvailable("Please confirm..."));
		new DefaultShell("Please confirm...");
		new PushButton("No").click();
		try {
			new WaitUntil(new ShellWithTextIsAvailable("Please confirm..."));
			new DefaultShell("Please confirm...");
			new PushButton("No").click();
		} catch (Exception e) {
			// Nothing to do.
		}
		assertTrue(editor.isDirty());
		new DefaultToolItem(new WorkbenchShell(), 0, new WithTooltipTextMatcher(new RegexMatcher("Save All.*")))
				.click();
		new WaitUntil(new ShellWithTextIsAvailable("Please confirm..."));
		new DefaultShell("Please confirm...");
		new PushButton("Yes").click();
		AbstractWait.sleep(TimePeriod.getCustom(5));
		assertFalse(editor.isDirty());
	}

	/**
	 * <p>
	 * JMX Navigator - prevent from close Camel Context
	 * </p>
	 * <b>Link: </b>
	 * <a href="https://issues.jboss.org/browse/FUSETOOLS-1115">https://issues.jboss.org/browse/FUSETOOLS-1115</a>
	 * 
	 * @throws FuseArchetypeNotFoundException
	 *             Fuse archetype was not found. Tests cannot be executed!
	 */
	@Test
	public void issue_1115() throws FuseArchetypeNotFoundException {

		ProjectFactory.createProject("camel-spring", "camel-archetype-spring");
		new CamelProject("camel-spring").runCamelContext("camel-context.xml");
		new JMXNavigator().getNode("Local Camel Context", "Camel", "camel").select();

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
	 * @throws FuseArchetypeNotFoundException
	 *             Fuse archetype was not found. Tests cannot be executed!
	 */
	@Test
	public void issue_1123() throws FuseArchetypeNotFoundException {

		ProjectFactory.createProject("camel-spring", "camel-archetype-spring");
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

		new ShellMenu("File", "New", "Fuse Project").select();
		new DefaultShell("New Fuse Project");
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
	 * 
	 * @throws FuseArchetypeNotFoundException
	 *             Fuse archetype was not found. Tests cannot be executed!
	 */
	@Test
	public void issue_1158() throws FuseArchetypeNotFoundException {

		ProjectFactory.createProject("camel-spring", "camel-archetype-spring");
		CamelProject project = new CamelProject("camel-spring");
		project.openCamelContext("camel-context.xml");
		CamelEditor editor = new CamelEditor("camel-context.xml");
		editor.activate();
		editor.setBreakpoint("choice");
		editor.save();
		project.debugCamelContextWithoutTests("camel-context.xml");
		new WaitUntil(new IsSuspended(), TimePeriod.LONG);
		try {
			new DefaultEditor(new RegexMatcher("CamelContext: context.*"));
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
	 * 
	 * @throws FuseArchetypeNotFoundException
	 *             Fuse archetype was not found. Tests cannot be executed!
	 */
	@Test
	public void issue_1172()
			throws ParserConfigurationException, SAXException, IOException, FuseArchetypeNotFoundException {

		ProjectFactory.createProject("camel-spring", "camel-archetype-spring");
		new CamelProject("camel-spring").openCamelContext("camel-context.xml");
		CamelEditor.switchTab("Design");
		CamelEditor editor = new CamelEditor("camel-context.xml");
		editor.click(5, 5);
		editor.setProperty("Id", "1");
		editor.save();
		CamelEditor.switchTab("Source");

		// check XML content
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder = factory.newDocumentBuilder();
		InputSource is = new InputSource(new StringReader(new DefaultStyledText().getText()));
		Document doc = builder.parse(is);
		assertNull(doc.getElementsByTagName("route").item(0).getAttributes().getNamedItem("customId"));
	}

	/**
	 * <p>
	 * Opening Camel Editor on Source tab + dirty flag
	 * </p>
	 * <b>Link: </b>
	 * <a href="https://issues.jboss.org/browse/FUSETOOLS-1243">https://issues.jboss.org/browse/FUSETOOLS-1243</a>
	 * 
	 * @throws FuseArchetypeNotFoundException
	 *             Fuse archetype was not found. Tests cannot be executed!
	 */
	@Test
	public void issue_1243() throws FuseArchetypeNotFoundException {

		ProjectFactory.createProject("camel-spring", "camel-archetype-spring");
		new CamelProject("camel-spring").openCamelContext("camel-context.xml");
		assertFalse("Camel editor should not be dirty",
				new DefaultToolItem(new WorkbenchShell(), 1, new WithTooltipTextMatcher(new RegexMatcher("Save.*")))
						.isEnabled());
		CamelEditor.switchTab("Source");
		assertFalse("Camel editor should not be dirty",
				new DefaultToolItem(new WorkbenchShell(), 1, new WithTooltipTextMatcher(new RegexMatcher("Save.*")))
						.isEnabled());
	}

	/**
	 * <p>
	 * Run Configurations dialog shows launch config types for server adapters for Karaf, SMX, Fuse and Fabric8 which
	 * partially don't work
	 * </p>
	 * <b>Link: </b>
	 * <a href="https://issues.jboss.org/browse/FUSETOOLS-1214">https://issues.jboss.org/browse/FUSETOOLS-1214</a>
	 * 
	 * @throws FuseArchetypeNotFoundException
	 *             Fuse archetype was not found. Tests cannot be executed!
	 */
	@Test
	public void issue_1214() throws FuseArchetypeNotFoundException {

		ProjectFactory.createProject("camel-spring", "camel-archetype-spring");
		new ProjectExplorer().selectProjects("camel-spring");
		new ContextMenu("Run As", "Run Configurations...").select();
		new WaitUntil(new ShellWithTextIsAvailable("Run Configurations"));
		new DefaultShell("Run Configurations");
		new DefaultTreeItem("Apache Tomcat").select();
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
	 * 
	 * @throws FuseArchetypeNotFoundException
	 *             Fuse archetype was not found. Tests cannot be executed!
	 */
	@Test
	public void issue_1403() throws FuseArchetypeNotFoundException {

		ProjectFactory.createProject("camel-blueprint", "camel-archetype-blueprint");
		new CamelProject("camel-blueprint").selectProjectItem("src/main/resources", "OSGI-INF", "blueprint",
				"blueprint.xml");
		new ContextMenu("Open").select();
		assertFalse("Camel Editor is dirty! But no editing was performed.", new CamelEditor("blueprint.xml").isDirty());
	}
}
