package org.jboss.tools.switchyard.ui.bot.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.jboss.reddeer.eclipse.ui.perspectives.JavaEEPerspective;
import org.jboss.reddeer.junit.requirement.inject.InjectRequirement;
import org.jboss.reddeer.junit.runner.RedDeerSuite;
import org.jboss.reddeer.requirements.openperspective.OpenPerspectiveRequirement.OpenPerspective;
import org.jboss.reddeer.swt.condition.JobIsRunning;
import org.jboss.reddeer.swt.impl.ctab.DefaultCTabItem;
import org.jboss.reddeer.swt.wait.TimePeriod;
import org.jboss.reddeer.swt.wait.WaitWhile;
import org.jboss.tools.switchyard.reddeer.component.SwitchYardComponent;
import org.jboss.tools.switchyard.reddeer.editor.DomainEditor;
import org.jboss.tools.switchyard.reddeer.editor.SwitchYardEditor;
import org.jboss.tools.switchyard.reddeer.preference.CompositePropertiesPage;
import org.jboss.tools.switchyard.reddeer.preference.ResourcePage;
import org.jboss.tools.switchyard.reddeer.preference.component.ComponentPage;
import org.jboss.tools.switchyard.reddeer.preference.component.ComponentPropertiesPage;
import org.jboss.tools.switchyard.reddeer.preference.contract.ContractPage;
import org.jboss.tools.switchyard.reddeer.preference.contract.ContractSecurityPage;
import org.jboss.tools.switchyard.reddeer.preference.contract.ContractTransactionPage;
import org.jboss.tools.switchyard.reddeer.preference.implementation.ImplementationPage;
import org.jboss.tools.switchyard.reddeer.preference.implementation.ImplementationSecurityPage;
import org.jboss.tools.switchyard.reddeer.preference.implementation.ImplementationTransactionPage;
import org.jboss.tools.switchyard.reddeer.requirement.SwitchYardRequirement;
import org.jboss.tools.switchyard.reddeer.requirement.SwitchYardRequirement.SwitchYard;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * Testing implementations properties
 * 
 * @author tsedmik
 */
@SwitchYard
@OpenPerspective(JavaEEPerspective.class)
@RunWith(RedDeerSuite.class)
public class ImplementationsPropertiesTest {

	private static final String PROJECT = "ImplPropTestProject";
	private static final String BEAN = "BeanService";
	private static final String BEAN2 = "BeanService2";
	private CompositePropertiesPage properties;

	@InjectRequirement
	private static SwitchYardRequirement switchyardRequirement;

	@BeforeClass
	public static void setUp() {
		switchyardRequirement.project(PROJECT).create();
		new WaitWhile(new JobIsRunning(), TimePeriod.LONG);

		new SwitchYardEditor().addBeanImplementation().createJavaInterface(BEAN).finish();
		new WaitWhile(new JobIsRunning(), TimePeriod.LONG);

		new SwitchYardEditor().autoLayout();
		new WaitWhile(new JobIsRunning(), TimePeriod.LONG);

		new SwitchYardEditor().addBeanImplementation().createJavaInterface(BEAN2).finish();
		new WaitWhile(new JobIsRunning(), TimePeriod.LONG);
	}

	@AfterClass
	public static void closeSwitchyardFile() {
		try {
			new SwitchYardEditor().saveAndClose();
		} catch (Exception ex) {
			// it is ok, we just try to close switchyard.xml if it is open
		}
		try {
			new DomainEditor().close(true);
		} catch (Exception ex) {
			// it is ok, we just try to close switchyard.xml if it is open
		}
	}

	@Before
	public void openProperties() {
		properties = new SwitchYardComponent(BEAN + "Bean").showProperties();
	}

	@After
	public void closeProperties() {
		properties.ok();
	}

	/**
	 * Tests "Properties --> Resource" menu
	 */
	@Test
	public void resourceTest() {

		ResourcePage page = properties.selectResource();

		assertTrue(page.isAttrDerivedEnabled());
		assertTrue(page.isDefaultEncodingEnabled());
		assertTrue(page.getEncoding().equals("default"));
		assertTrue(page.isOtherEncodingEnabled());
		assertFalse(page.isOtherEncodingComboEnabled());

		page.setEncoding("UTF-8");

		assertTrue(page.isOtherEncodingComboEnabled());
	}

	/**
	 * Tests "Properties --> Component" menu
	 */
	@Test
	public void componentTest() {

		ComponentPage page = properties.selectComponent();
		assertEquals("BeanServiceBean", page.getName());
	}

	/**
	 * Tests "Properties --> Component --> Properties" menu (adds and remove properties)
	 */
	@Test
	public void componentPropertiesTest() {

		ComponentPropertiesPage page = properties.selectComponentProperties();
		assertEquals(0, page.getPropertiesCount());
		page.addProperty("AAA", "BBB");
		assertEquals(1, page.getPropertiesCount());
		page.removeProperty("AAA");
		assertEquals(0, page.getPropertiesCount());
	}

	/**
	 * Tests "Properties --> Contract" menu
	 */
	@Test
	public void contractTest() {

		ContractPage page = properties.selectContract();

		assertTrue(page.isInterfaceTypeEnabled("Java"));
		assertFalse(page.isInterfaceTypeEnabled("WSDL"));
		assertFalse(page.isInterfaceTypeEnabled("ESB"));
		assertEquals("BeanService", page.getServiceName());
	}

	/**
	 * Tests "Properties --> Contract --> Transaction Policy" menu
	 */
	@Test
	public void contractTransactionPolicyTest() {

		ContractTransactionPage page = properties.selectContractTransaction();

		assertTrue(page.isComboEnabled());
		assertEquals(0, page.getComboSelectionIndex());
		assertEquals(3, page.getAllTransactionPolicies().size());
		assertTrue(page.getAllTransactionPolicies().contains("None"));
		assertTrue(page.getAllTransactionPolicies().contains("propagatesTransaction"));
		assertTrue(page.getAllTransactionPolicies().contains("suspendsTransaction"));

		page.setTransactionPolicy("propagatesTransaction");
	}

	/**
	 * Tests "Properties --> Contract --> Security Policy" menu
	 */
	@Test
	public void contractSecurityPolicyTest() {

		ContractSecurityPage page = properties.selectContractSecurity();

		assertTrue(page.isAuthenticationEnabled());
		assertTrue(page.isConfidentalityEnabled());
		assertFalse(page.isAuthenticationChecked());
		assertFalse(page.isConfidentalityChecked());

		page.setAuthentication(true);
		page.setConfidentality(true);

		assertTrue(page.isAuthenticationChecked());
		assertTrue(page.isConfidentalityChecked());
		assertFalse(page.isSecurityConfComboEnabled());

		properties.ok();
		DomainEditor domain = new DomainEditor();
		domain.addSecurityConfiguration("default", null, null, null, null);
		domain.addSecurityConfiguration("default1", null, null, null, null);
		new DefaultCTabItem("Design").activate();
		properties = new SwitchYardComponent(BEAN + "Bean").showProperties();
		page = properties.selectContractSecurity();

		assertTrue(page.isSecurityConfComboEnabled());
		page.setSecurityConf("default1");

		// Check bug: https://issues.jboss.org/browse/SWITCHYARD-1732
		properties.ok();
		properties = new SwitchYardComponent(BEAN2 + "Bean").showProperties();
		page = properties.selectContractSecurity();
		assertFalse(page.isAuthenticationChecked());
		assertFalse(page.isConfidentalityChecked());
	}

	/**
	 * Tests "Properties --> Implementation" menu
	 */
	@Test
	public void implementationTest() {

		ImplementationPage page = properties.selectImplementation();

		assertEquals("com.example.switchyard.ImplPropTestProject.BeanServiceBean", page.getBeanClass());
		assertTrue(page.isBrowseButtonEnabled());
	}

	/**
	 * Tests "Properties --> Implementation --> Transaction Policy" menu
	 */
	@Test
	public void implementationTransactionPolicyTest() {

		ImplementationTransactionPage page = properties.selectImplementationTransaction();

		assertTrue(page.isTransactionPolicyComboEnabled());
		assertEquals(0, page.getComboSelectionIndex());
		assertEquals(4, page.getAllTransactionPolicies().size());
		assertTrue(page.getAllTransactionPolicies().contains("None"));
		assertTrue(page.getAllTransactionPolicies().contains("managedTransaction.Local"));
		assertTrue(page.getAllTransactionPolicies().contains("managedTransaction.Global"));
		assertTrue(page.getAllTransactionPolicies().contains("noManagedTransaction"));

		page.setTransactionPolicy("managedTransaction.Local");
	}

	/**
	 * Tests "Properties --> Implementation --> Security Policy" menu
	 */
	@Test
	public void implementationSecurityPolicyTest() {

		ImplementationSecurityPage page = properties.selectImplementationSecurity();

		assertTrue(page.isAuthorizationEnabled());
		page.setAuthorization(true);
		properties.ok();

		properties = new SwitchYardComponent(BEAN + "Bean").showProperties();
		assertTrue(page.isAuthorizationChecked());
		page.setAuthorization(false);
		properties.ok();

		properties = new SwitchYardComponent(BEAN + "Bean").showProperties();
		assertFalse(page.isAuthorizationChecked());
	}
}
