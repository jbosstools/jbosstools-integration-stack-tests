package org.jboss.tools.switchyard.ui.bot.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.File;
import java.util.List;

import org.jboss.reddeer.eclipse.ui.problems.Problem;
import org.jboss.reddeer.eclipse.ui.problems.ProblemsView;
import org.jboss.reddeer.eclipse.ui.problems.ProblemsView.ProblemType;
import org.jboss.reddeer.junit.execution.annotation.RunIf;
import org.jboss.reddeer.junit.requirement.inject.InjectRequirement;
import org.jboss.reddeer.junit.runner.RedDeerSuite;
import org.jboss.reddeer.swt.impl.shell.DefaultShell;
import org.jboss.reddeer.workbench.impl.shell.WorkbenchShell;
import org.jboss.tools.switchyard.reddeer.editor.SwitchYardEditor;
import org.jboss.tools.switchyard.reddeer.editor.XPathEvaluator;
import org.jboss.tools.switchyard.reddeer.project.SwitchYardProject;
import org.jboss.tools.switchyard.reddeer.requirement.SwitchYardRequirement;
import org.jboss.tools.switchyard.reddeer.requirement.SwitchYardRequirement.SwitchYard;
import org.jboss.tools.switchyard.reddeer.wizard.SwitchYardProjectWizard;
import org.jboss.tools.switchyard.ui.bot.test.condition.SwitchYardRequirementHasServer;
import org.junit.After;
import org.junit.Before;
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
public class ProjectCreationTest {

	@InjectRequirement
	private static SwitchYardRequirement switchyardRequirement;

	private SwitchYardProjectWizard wizard;

	@BeforeClass
	public static void maximizeWorkbench() {
		new WorkbenchShell().maximize();
	}

	@Before
	public void saveAndCloseSwitchYardFile() {
		try {
			new SwitchYardEditor().saveAndClose();
		} catch (Exception ex) {
			// it is ok, we just try to close switchyard.xml if it is open
		}
	}

	@After
	public void closeProjectWizard() {
		try {
			new DefaultShell("New SwitchYard Project").close();
		} catch (Exception ex) {
			// it is ok, we just try to close the wizard if it is open
		}
	}

	@After
	public void deleteSwitchYardProjects() {
		switchyardRequirement.deleteAllProjects();
	}

	@Test
	public void createProjectWithoutServerTest() {
		String projectName = "noserver";

		wizard = switchyardRequirement.project(projectName);
		wizard.open();
		assertEquals(projectName, wizard.getArtifactId());
		assertEquals("com.example.switchyard", wizard.getGroupId());
		assertEquals("urn:com.example.switchyard:noserver:1.0", wizard.gettargetNamespace());
		assertEquals("com.example.switchyard.noserver", wizard.getPackageName());
		wizard.setGroupId("abcd");
		assertEquals("abcd", wizard.getGroupId());
		assertEquals("urn:abcd:noserver:1.0", wizard.gettargetNamespace());
		assertEquals("abcd.noserver", wizard.getPackageName());
		wizard.setConfigurationVersion(switchyardRequirement.getConfig().getConfigurationVersion());
		wizard.setTargetRuntime("<None>");
		wizard.setLibraryVersion(switchyardRequirement.getConfig().getLibraryVersion());
		wizard.finish();

		XPathEvaluator xpath = new XPathEvaluator(new File(new SwitchYardProject(projectName).getFile(), "pom.xml"));
		assertEquals(projectName, xpath.evaluateString("/project/artifactId"));
		assertEquals("abcd", xpath.evaluateString("/project/groupId"));
		assertEquals(switchyardRequirement.getConfig().getLibraryVersion(),
				xpath.evaluateString("/project/properties/switchyard.version"));
	}

	 @Test
	@RunIf(conditionClass = SwitchYardRequirementHasServer.class)
	public void createProjectWithServerTest() {
		String projectName = "withserver";

		wizard = switchyardRequirement.project(projectName);
		wizard.open();
		wizard.setConfigurationVersion(switchyardRequirement.getConfig().getConfigurationVersion());
		wizard.setTargetRuntime(switchyardRequirement.getTargetRuntimeLabel());
		assertEquals(switchyardRequirement.getTargetRuntimeLabel(), wizard.getTargetRuntime());
		assertEquals(switchyardRequirement.getConfig().getLibraryVersion(), wizard.getLibraryVersion());
		wizard.setTargetRuntime("<None>");
		wizard.setLibraryVersion("foo");
		assertEquals("<None>", wizard.getTargetRuntime());
		assertEquals("foo", wizard.getLibraryVersion());
		wizard.setTargetRuntime(switchyardRequirement.getTargetRuntimeLabel());
		assertEquals(switchyardRequirement.getTargetRuntimeLabel(), wizard.getTargetRuntime());
		assertEquals(switchyardRequirement.getConfig().getLibraryVersion(), wizard.getLibraryVersion());
		wizard.setConfigurationVersion(switchyardRequirement.getConfig().getConfigurationVersion());
		assertEquals(switchyardRequirement.getTargetRuntimeLabel(), wizard.getTargetRuntime());
		assertEquals(switchyardRequirement.getConfig().getLibraryVersion(), wizard.getLibraryVersion());
		wizard.finish();

		XPathEvaluator xpath = new XPathEvaluator(new File(new SwitchYardProject(projectName).getFile(), "pom.xml"));
		assertEquals(projectName, xpath.evaluateString("/project/artifactId"));
		assertEquals("com.example.switchyard", xpath.evaluateString("/project/groupId"));
		assertEquals(switchyardRequirement.getConfig().getLibraryVersion(),
				xpath.evaluateString("/project/properties/switchyard.version"));
	}

	@Test
	public void createProjectWithOSGiTest() {
		String projectName = "osgi";

		wizard = switchyardRequirement.project(projectName);
		wizard.open();
		wizard.setConfigurationVersion(switchyardRequirement.getConfig().getConfigurationVersion());
		wizard.setTargetRuntime("<None>");
		wizard.setLibraryVersion(switchyardRequirement.getConfig().getLibraryVersion());
		assertFalse("OSGi bundle should be unchecked by default", wizard.isOSGiBundle());
		wizard.setOSGiBundle(true);
		assertTrue("OSGi bundle cannot be checked", wizard.isOSGiBundle());
		wizard.setOSGiBundle(false);
		assertFalse("OSGi bundle cannot be unchecked", wizard.isOSGiBundle());
		wizard.setOSGiBundle(true);
		assertTrue("OSGi bundle cannot be re-checked", wizard.isOSGiBundle());
		wizard.finish();

		XPathEvaluator xpath = new XPathEvaluator(new File(new SwitchYardProject(projectName).getFile(), "pom.xml"));
		assertEquals(projectName, xpath.evaluateString("/project/artifactId"));
		assertEquals("com.example.switchyard", xpath.evaluateString("/project/groupId"));
		assertEquals(switchyardRequirement.getConfig().getLibraryVersion(),
				xpath.evaluateString("/project/properties/switchyard.version"));
		assertEquals("bundle", xpath.evaluateString("/project/packaging"));
		assertEquals("com.example.switchyard." + projectName + "*",
				xpath.evaluateString("/project/properties/switchyard.osgi.export"));
		assertEquals("com.example.switchyard." + projectName,
				xpath.evaluateString("/project/properties/switchyard.osgi.symbolic.name"));
	}

	@Test
	public void createProjectWithComponentsTest() {
		String projectName = "components";

		wizard = switchyardRequirement.project(projectName);
		wizard.open();
		wizard.setConfigurationVersion(switchyardRequirement.getConfig().getConfigurationVersion());
		wizard.setTargetRuntime("<None>");
		wizard.setLibraryVersion(switchyardRequirement.getConfig().getLibraryVersion());
		
		assertComponent("Implementation Support", "Bean");
		assertComponent("Implementation Support", "BPEL");
		assertComponent("Implementation Support", "BPM (jBPM)");
		assertComponent("Implementation Support", "Camel Route");
		assertComponent("Implementation Support", "Rules (Drools)");
		assertComponent("Gateway Bindings", "Camel Core (SEDA/Timer/URI)");
		assertComponent("Gateway Bindings", "File");
		assertComponent("Gateway Bindings", "File Transfer (FTP/FTPS/SFTP)");
		assertComponent("Gateway Bindings", "HTTP");
		assertComponent("Gateway Bindings", "JCA");
		assertComponent("Gateway Bindings", "JMS");
		assertComponent("Gateway Bindings", "JPA");
		assertComponent("Gateway Bindings", "Mail");
		assertComponent("Gateway Bindings", "Network (TCP/UDP)");
		assertComponent("Gateway Bindings", "REST");
		assertComponent("Gateway Bindings", "SCA");
		assertComponent("Gateway Bindings", "Scheduling");
		assertComponent("Gateway Bindings", "SOAP");
		assertComponent("Gateway Bindings", "SQL");
		assertComponent("Test Mixins", "HornetQ Mixin");
		assertComponent("Test Mixins", "HTTP Mixin");
		assertComponent("Test Mixins", "Naming Mixin");
		assertComponent("Test Mixins", "Smooks Mixin");

		if (switchyardRequirement.getConfig().getConfigurationVersion().equals("2.0")) {
			assertComponent("Gateway Bindings", "Atom");
			assertComponent("Gateway Bindings", "CXF");
			assertComponent("Gateway Bindings", "RSS");
			assertComponent("Gateway Bindings", "SAP");
		}

		wizard.finish();

		new SwitchYardProject(projectName).update();

		XPathEvaluator xpath = new XPathEvaluator(new File(new SwitchYardProject(projectName).getFile(), "pom.xml"));
		assertEquals(projectName, xpath.evaluateString("/project/artifactId"));
		assertEquals("com.example.switchyard", xpath.evaluateString("/project/groupId"));
		assertEquals(switchyardRequirement.getConfig().getLibraryVersion(),
				xpath.evaluateString("/project/properties/switchyard.version"));

		ProblemsView problemsView = new ProblemsView();
		problemsView.open();
		List<Problem> errors = problemsView.getProblems(ProblemType.ERROR);
		if (!errors.isEmpty()) {
			StringBuffer msg = new StringBuffer("The following errors occured during creating SY project:");
			msg.append(System.getProperty("line.separator"));
			for (Problem error : errors) {
				msg.append(error.getDescription()).append(System.getProperty("line.separator"));
			}
			fail(msg.toString());
		}
	}

	@Test
	public void createProjectWithBOMTest() {
		String projectName = "bom";

		wizard = switchyardRequirement.project(projectName);
		wizard.open();
		wizard.setTargetRuntime("<None>");

		wizard.setLibraryVersion("1.1.0.Final");

		wizard.setConfigurationVersion("1.0");
		assertFalse("BOM dependency should not be enabled with 1.0 version when library version is '1.1.0.Final'", wizard.isBOMDependencyEnabled());

		wizard.setConfigurationVersion("1.1");
		assertFalse("BOM dependency should not be enabled with 1.0 version when library version is '1.1.0.Final'", wizard.isBOMDependencyEnabled());

		wizard.setConfigurationVersion("2.0");
		assertFalse("BOM dependency should not be enabled with 1.0 version when library version is '1.1.0.Final'", wizard.isBOMDependencyEnabled());
		
		wizard.setLibraryVersion("2.0.0.Final");

		wizard.setConfigurationVersion("1.0");
		assertTrue("BOM dependency should be enabled with 1.0 version", wizard.isBOMDependencyEnabled());
		wizard.setBOMDependency(true);
		assertTrue("BOM dependency cannot be checked with 1.0 version", wizard.isBOMDependencyChecked());
		wizard.setBOMDependency(false);
		assertFalse("BOM dependency cannot be unchecked with 1.0 version", wizard.isBOMDependencyChecked());
		wizard.setBOMDependency(true);
		assertTrue("BOM dependency cannot be checked with 1.0 version", wizard.isBOMDependencyChecked());

		wizard.setConfigurationVersion("1.1");
		assertTrue("BOM dependency should be enabled with 1.1 version", wizard.isBOMDependencyEnabled());
		wizard.setBOMDependency(true);
		assertTrue("BOM dependency cannot be checked with 1.1 version", wizard.isBOMDependencyChecked());
		wizard.setBOMDependency(false);
		assertFalse("BOM dependency cannot be unchecked with 1.1 version", wizard.isBOMDependencyChecked());
		wizard.setBOMDependency(true);
		assertTrue("BOM dependency cannot be checked with 1.1 version", wizard.isBOMDependencyChecked());

		wizard.setConfigurationVersion("2.0");
		assertTrue("BOM dependency should be enabled with 2.0 version", wizard.isBOMDependencyEnabled());
		wizard.setBOMDependency(true);
		assertTrue("BOM dependency cannot be checked with 2.0 version", wizard.isBOMDependencyChecked());
		wizard.setBOMDependency(false);
		assertFalse("BOM dependency cannot be unchecked with 2.0 version", wizard.isBOMDependencyChecked());
		wizard.setBOMDependency(true);
		assertTrue("BOM dependency cannot be checked with 2.0 version", wizard.isBOMDependencyChecked());

		wizard.finish();

		XPathEvaluator xpath = new XPathEvaluator(new File(new SwitchYardProject(projectName).getFile(), "pom.xml"));
		String bomDependency = "/project/dependencyManagement/dependencies/dependency/";
		assertEquals(projectName, xpath.evaluateString("/project/artifactId"));
		assertEquals("switchyard-bom", xpath.evaluateString(bomDependency + "/artifactId"));
		assertEquals("pom", xpath.evaluateString(bomDependency + "/type"));
		assertEquals("import", xpath.evaluateString(bomDependency + "/scope"));
	}

	private void assertComponent(String group, String component) {
		assertFalse("" + component + " should by unchecked by default", wizard.isComponent(group, component));
		wizard.setComponent(group, component, true);
		assertTrue("" + component + " cannot be checked", wizard.isComponent(group, component));
	}
}
