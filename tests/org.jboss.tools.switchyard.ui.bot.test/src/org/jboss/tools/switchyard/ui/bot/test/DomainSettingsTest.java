package org.jboss.tools.switchyard.ui.bot.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.util.List;

import org.jboss.reddeer.eclipse.ui.perspectives.JavaEEPerspective;
import org.jboss.reddeer.junit.requirement.inject.InjectRequirement;
import org.jboss.reddeer.junit.runner.RedDeerSuite;
import org.jboss.reddeer.requirements.openperspective.OpenPerspectiveRequirement.OpenPerspective;
import org.jboss.reddeer.swt.api.TreeItem;
import org.jboss.reddeer.workbench.impl.shell.WorkbenchShell;
import org.jboss.tools.switchyard.reddeer.SwitchYardFile;
import org.jboss.tools.switchyard.reddeer.editor.DomainEditor;
import org.jboss.tools.switchyard.reddeer.editor.SwitchYardEditor;
import org.jboss.tools.switchyard.reddeer.project.SwitchYardProject;
import org.jboss.tools.switchyard.reddeer.requirement.SwitchYardRequirement;
import org.jboss.tools.switchyard.reddeer.requirement.SwitchYardRequirement.SwitchYard;
import org.jboss.tools.switchyard.reddeer.shell.DomainPropertiesFileShell;
import org.jboss.tools.switchyard.reddeer.utils.PreferenceUtils;
import org.jboss.tools.switchyard.reddeer.wizard.SecurityConfigurationWizard;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * Test for domain settings
 * 
 * @author apodhrad
 * 
 */
@SwitchYard
@OpenPerspective(JavaEEPerspective.class)
@RunWith(RedDeerSuite.class)
public class DomainSettingsTest {

	public static final String PROJECT = "domain-project";
	public static final String CALLBACK_CLASS = "org.switchyard.security.callback.handler.NamePasswordCallbackHandler";

	@InjectRequirement
	private static SwitchYardRequirement switchyardRequirement;

	private static String autoBuilding;

	@BeforeClass
	public static void turnOffAutoBuilding() {
		autoBuilding = PreferenceUtils.getAutoBuilding();
		PreferenceUtils.setAutoBuilding("false");
	}

	@AfterClass
	public static void turnBackAutoBuilding() {
		PreferenceUtils.setAutoBuilding(autoBuilding);
	}

	@BeforeClass
	public static void createProject() {
		closeSwitchYardEditor();
		switchyardRequirement.project(PROJECT).create();
		new WorkbenchShell().maximize();
	}

	@AfterClass
	public static void deleteProject() {
		closeSwitchYardEditor();
		new SwitchYardProject(PROJECT).delete(true);
	}

	@Test
	public void messageTraceTest() {
		openSwitchYardEditor();
		assertFalse(new DomainEditor().isMessageTraced());
		new DomainEditor().setMessageTrace(true);
		new DomainEditor().saveAndClose();
		openSwitchYardEditor();
		assertTrue(new DomainEditor().isMessageTraced());
		new DomainEditor().setMessageTrace(false);
		new DomainEditor().saveAndClose();
		openSwitchYardEditor();
		assertFalse(new DomainEditor().isMessageTraced());
	}

	@Test
	public void propertiesTest() {
		openSwitchYardEditor();
		new DomainEditor().removeAllProperties();
		assertEquals(0, new DomainEditor().getProperties().size());
		new DomainEditor().addProperty("foo", "bar");
		new DomainEditor().saveAndClose();

		openSwitchYardEditor();
		assertEquals(1, new DomainEditor().getProperties().size());
		assertEquals("bar", new DomainEditor().getProperty("foo"));
		new DomainEditor().removeProperty("foo");
		new DomainEditor().saveAndClose();

		openSwitchYardEditor();
		assertEquals(0, new DomainEditor().getProperties().size());
		assertNull(new DomainEditor().getProperty("foo"));
	}

	@Test
	public void filePropertiesTest() throws Exception {
		openSwitchYardEditor();
		DomainPropertiesFileShell domainPropertiesFile = new DomainEditor().openDomainPropertiesFile();
		domainPropertiesFile.getProjectClasspath().setText("META-INF/switchyard.xml");
		domainPropertiesFile.ok();
		new DomainEditor().saveAndClose();

		openSwitchYardEditor();
		assertEquals("META-INF/switchyard.xml", new DomainEditor().getPropertiesFile().getText());
		assertXPath("META-INF/switchyard.xml", "/switchyard/domain/properties/@load");
		domainPropertiesFile = new DomainEditor().openDomainPropertiesFile();
		domainPropertiesFile.getPublicURL().setText("http://localhost/my.properties");
		domainPropertiesFile.ok();
		new DomainEditor().saveAndClose();

		openSwitchYardEditor();
		assertEquals("http://localhost/my.properties", new DomainEditor().getPropertiesFile().getText());
		assertXPath("http://localhost/my.properties", "/switchyard/domain/properties/@load");
		new DomainEditor().getPropertiesFile().setText("my.properties");
		new DomainEditor().saveAndClose();

		openSwitchYardEditor();
		assertEquals("my.properties", new DomainEditor().getPropertiesFile().getText());
		assertXPath("my.properties", "/switchyard/domain/properties/@load");
		new DomainEditor().saveAndClose();
	}

	@Test
	public void securityConfigurationTest() {
		/* Add configuration security */
		openSwitchYardEditor();
		new DomainEditor().addSecurityConfiguration("test-security", "tester, admin", "user", "other",
				"NamePasswordCallbackHandler");
		new DomainEditor().saveAndClose();

		/* Test if it is displayed in the editor */
		openSwitchYardEditor();
		List<TreeItem> items = new DomainEditor().getSecurityConfigurations();
		assertEquals(1, items.size());
		TreeItem item = new DomainEditor().getSecurityConfiguration("test-security");
		assertEquals("test-security", item.getCell(0));
		assertEquals(CALLBACK_CLASS, item.getCell(1));
		assertEquals("tester, admin", item.getCell(2));
		assertEquals("user", item.getCell(3));
		assertEquals("other", item.getCell(4));

		/* Test if all properties are displayed in the wizard */
		new DomainEditor().editSecurityConfiguration("test-security");
		SecurityConfigurationWizard wizard = new SecurityConfigurationWizard();
		assertEquals("test-security", wizard.getName());
		assertEquals(CALLBACK_CLASS, wizard.getCallbackHandlerClass());
		assertEquals("tester, admin", wizard.getRolesAllowed());
		assertEquals("user", wizard.getRunAs());
		assertEquals("other", wizard.getSecurityDomain());
		wizard.cancel();

		/* Test if the configuration can be properly deleted */
		new DomainEditor().removeSecurityConfiguration("test-security");
		new DomainEditor().saveAndClose();
		openSwitchYardEditor();
		items = new DomainEditor().getSecurityConfigurations();
		assertEquals(0, items.size());
		new DomainEditor().saveAndClose();
	}

	public static void closeSwitchYardEditor() {
		try {
			new SwitchYardEditor().saveAndClose();
		} catch (Exception ex) {
			// it is ok, we just try to close switchyard.xml if it is open
		}
		try {
			new DomainEditor().saveAndClose();
		} catch (Exception ex) {
			// it is ok, we just try to close switchyard.xml if it is open
		}
	}

	public static void openSwitchYardEditor() {
		new SwitchYardProject(PROJECT).getProjectItem("SwitchYard").open();
	}

	private void assertXPath(String expected, String xpath) throws IOException {
		SwitchYardFile switchyardFile = new SwitchYardFile(PROJECT);
		assertEquals(switchyardFile.getSource(), expected, switchyardFile.xpath(xpath));
	}

}
