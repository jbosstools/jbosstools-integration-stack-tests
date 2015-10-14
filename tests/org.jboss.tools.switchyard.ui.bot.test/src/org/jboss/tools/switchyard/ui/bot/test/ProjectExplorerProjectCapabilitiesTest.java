package org.jboss.tools.switchyard.ui.bot.test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.File;

import org.jboss.reddeer.eclipse.jdt.ui.ProjectExplorer;
import org.jboss.reddeer.junit.requirement.inject.InjectRequirement;
import org.jboss.reddeer.junit.runner.RedDeerSuite;
import org.jboss.reddeer.core.condition.JobIsRunning;
import org.jboss.reddeer.core.condition.ShellWithTextIsActive;
import org.jboss.reddeer.swt.impl.button.CheckBox;
import org.jboss.reddeer.swt.impl.button.PushButton;
import org.jboss.reddeer.swt.impl.combo.LabeledCombo;
import org.jboss.reddeer.swt.impl.ctab.DefaultCTabItem;
import org.jboss.reddeer.swt.impl.group.DefaultGroup;
import org.jboss.reddeer.swt.impl.menu.ShellMenu;
import org.jboss.reddeer.swt.impl.shell.DefaultShell;
import org.jboss.reddeer.swt.impl.shell.WorkbenchShell;
import org.jboss.reddeer.swt.impl.text.LabeledText;
import org.jboss.reddeer.swt.impl.tree.DefaultTreeItem;
import org.jboss.reddeer.common.wait.TimePeriod;
import org.jboss.reddeer.common.wait.WaitWhile;
import org.jboss.reddeer.uiforms.impl.section.DefaultSection;
import org.jboss.reddeer.workbench.impl.editor.DefaultEditor;
import org.jboss.tools.switchyard.reddeer.editor.SwitchYardEditor;
import org.jboss.tools.switchyard.reddeer.editor.XPathEvaluator;
import org.jboss.tools.switchyard.reddeer.project.SwitchYardProject;
import org.jboss.tools.switchyard.reddeer.requirement.SwitchYardRequirement;
import org.jboss.tools.switchyard.reddeer.requirement.SwitchYardRequirement.SwitchYard;
import org.jboss.tools.switchyard.reddeer.shell.ProjectCapabilitiesShell;
import org.junit.AfterClass;
import org.junit.Assert;
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
public class ProjectExplorerProjectCapabilitiesTest {

	public static final String SY_PROJECT = "switchyard-child";
	public static final String SY_PARENT_PROJECT = "switchyard-parent";

	@InjectRequirement
	private static SwitchYardRequirement switchYardRequirement;

	@BeforeClass
	public static void createProjects() {
		saveAndCloseSwitchYardFile();

		new WorkbenchShell().maximize();
		new ProjectExplorer().open();

		/* Create simple maven project */
		new ShellMenu("File", "New", "Project...").select();
		new DefaultShell("New Project");
		new DefaultTreeItem("Maven", "Maven Project").select();
		new PushButton("Next >").click();
		new DefaultShell("New Maven Project");
		new CheckBox("Create a simple project (skip archetype selection)").toggle(true);
		new PushButton("Next >").click();
		new DefaultShell("New Maven Project");
		new LabeledCombo(new DefaultGroup("Artifact"), "Group Id:").setText("com.example.switchyard");
		new LabeledCombo(new DefaultGroup("Artifact"), "Artifact Id:").setText(SY_PARENT_PROJECT);
		new LabeledCombo(new DefaultGroup("Artifact"), "Packaging:").setSelection("pom");
		new PushButton("Finish").click();
		new WaitWhile(new ShellWithTextIsActive("New Maven Project"));
		new WaitWhile(new JobIsRunning(), TimePeriod.LONG);

		/* Add http component to the parent project */
		new ProjectExplorer().getProject(SY_PARENT_PROJECT).getProjectItem("pom.xml").open();
		new DefaultEditor(SY_PARENT_PROJECT + "/pom.xml");
		new DefaultCTabItem("Dependencies").activate();
		new PushButton(new DefaultSection("Dependencies"), "Add...").click();
		new DefaultShell("Select Dependency");
		new LabeledText("Group Id:").setText("org.switchyard.components");
		new LabeledText("Artifact Id:").setText("switchyard-component-http");
		new LabeledText("Version: ").setText(switchYardRequirement.getConfig().getLibraryVersion());
		new PushButton("OK").click();
		new DefaultEditor(SY_PARENT_PROJECT + "/pom.xml").save();
		new WaitWhile(new JobIsRunning(), TimePeriod.LONG);
		new DefaultEditor(SY_PARENT_PROJECT + "/pom.xml").close();

		/* Create new switchyard project */
		switchYardRequirement.project(SY_PROJECT).create();

		/* Set the previous project as parent project */
		new ProjectExplorer().getProject(SY_PROJECT).getProjectItem("pom.xml").open();
		new DefaultEditor(SY_PROJECT + "/pom.xml");
		new DefaultCTabItem("Overview").activate();
		new DefaultSection("Parent").setExpanded(true);
		new LabeledText(new DefaultSection("Parent"), "Group Id:").setText("com.example.switchyard");
		new LabeledText(new DefaultSection("Parent"), "Artifact Id:").setText(SY_PARENT_PROJECT);
		new LabeledText(new DefaultSection("Parent"), "Version:").setText("0.0.1-SNAPSHOT");
		new DefaultEditor(SY_PROJECT + "/pom.xml").save();
		new WaitWhile(new JobIsRunning(), TimePeriod.LONG);
		new DefaultEditor(SY_PROJECT + "/pom.xml").close();
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
	public void projectCapabilitiesParentTest() {
		/* Check capabilities derived from parent */
		ProjectCapabilitiesShell capabilities = new SwitchYardProject(SY_PROJECT).configureCapabilities();
		assertTrue("HTTP binding is not checked", capabilities.isComponentChecked("Gateway Bindings", "HTTP"));
		/* Try to remove http component */
		capabilities.toggleComponent("Gateway Bindings", "HTTP", false).ok();
		/* The component shouldn't be removed from parent */
		File parentPomFile = new File(new SwitchYardProject(SY_PARENT_PROJECT).getFile(), "pom.xml");
		XPathEvaluator xpath = new XPathEvaluator(parentPomFile);
		String query = "/project/dependencies/dependency/artifactId[text()='switchyard-component-http']";
		assertTrue("HTTP binding was removed from parent project", xpath.evaluateBoolean(query));
	}

	@Test
	public void projectCapabilitiesComponentTest() {
		ProjectCapabilitiesShell capabilities = new SwitchYardProject(SY_PROJECT).configureCapabilities();
		assertFalse("SOAP binding is checked", capabilities.isComponentChecked("Gateway Bindings", "SOAP"));
		/* Try to add soap component */
		capabilities.toggleComponent("Gateway Bindings", "SOAP", true);
		assertTrue("SOAP binding wasn't checked", capabilities.isComponentChecked("Gateway Bindings", "SOAP"));
		capabilities.ok();
		/* The component should be added */
		File pomFile = new File(new SwitchYardProject(SY_PROJECT).getFile(), "pom.xml");
		XPathEvaluator xpath = new XPathEvaluator(pomFile);
		String query = "/project/dependencies/dependency/artifactId[text()='switchyard-component-soap']";
		assertTrue("SOAP binding wasn't added into the project", xpath.evaluateBoolean(query));
		/* Now, try to remove SOAP component */
		capabilities = new SwitchYardProject(SY_PROJECT).configureCapabilities();
		assertTrue("SOAP binding is not checked", capabilities.isComponentChecked("Gateway Bindings", "SOAP"));
		capabilities.toggleComponent("Gateway Bindings", "SOAP", false).ok();
		/* The component should be removed */
		pomFile = new File(new SwitchYardProject(SY_PROJECT).getFile(), "pom.xml");
		xpath = new XPathEvaluator(pomFile);
		assertFalse("SOAP binding wasn't removed from the project", xpath.evaluateBoolean(query));
	}

	@Test
	public void projectCapabilitiesVersionTest() {
		new SwitchYardProject(SY_PROJECT).openSwitchYardFile();
		/* Set configuration version to 1.0 */
		ProjectCapabilitiesShell capabilities = new SwitchYardProject(SY_PROJECT).configureCapabilities();
		capabilities.setConfigurationVersion("1.0").ok();
		assertSwitchYardNamespace("1.0");
		/* Set configuration version to 1.1 */
		capabilities = new SwitchYardProject(SY_PROJECT).configureCapabilities();
		capabilities.setConfigurationVersion("1.1").ok();
		assertSwitchYardNamespace("1.1");
		/* Set configuration version to 2.0 */
		if (switchYardRequirement.getConfig().getLibraryVersion().startsWith("2")) {
			capabilities = new SwitchYardProject(SY_PROJECT).configureCapabilities();
			capabilities.setConfigurationVersion("2.0").ok();
			assertSwitchYardNamespace("2.0");
		}

	}

	private void assertSwitchYardNamespace(String expectedVersion) {
		XPathEvaluator xpath = new XPathEvaluator(new SwitchYardEditor().getSourceFile(), true);
		String uri = "urn:switchyard-config:switchyard:" + expectedVersion;
		String query = "count(//*[local-name() = 'switchyard' and namespace-uri() = '" + uri + "']) = 1";
		if (!xpath.evaluateBoolean(query)) {
			fail("Expected configuration version " + expectedVersion + " but was " + xpath);
		}

	}
}
