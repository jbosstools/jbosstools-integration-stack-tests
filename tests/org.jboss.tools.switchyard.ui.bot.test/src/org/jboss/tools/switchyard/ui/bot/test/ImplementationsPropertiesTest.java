package org.jboss.tools.switchyard.ui.bot.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.eclipse.draw2d.geometry.Rectangle;
import org.jboss.reddeer.swt.condition.JobIsRunning;
import org.jboss.reddeer.swt.impl.ctab.DefaultCTabItem;
import org.jboss.reddeer.swt.wait.TimePeriod;
import org.jboss.reddeer.swt.wait.WaitWhile;
import org.jboss.tools.switchyard.reddeer.component.Bean;
import org.jboss.tools.switchyard.reddeer.component.Component;
import org.jboss.tools.switchyard.reddeer.editor.DomainEditor;
import org.jboss.tools.switchyard.reddeer.preference.ImplementationPropertiesPage;
import org.jboss.tools.switchyard.reddeer.preference.implementation.DefaultComponentPage;
import org.jboss.tools.switchyard.reddeer.preference.implementation.DefaultComponentPropertiesPage;
import org.jboss.tools.switchyard.reddeer.preference.implementation.DefaultContractPage;
import org.jboss.tools.switchyard.reddeer.preference.implementation.DefaultContractSecurityPage;
import org.jboss.tools.switchyard.reddeer.preference.implementation.DefaultContractTransactionPage;
import org.jboss.tools.switchyard.reddeer.preference.implementation.DefaultImplementationPage;
import org.jboss.tools.switchyard.reddeer.preference.implementation.DefaultResourcePage;
import org.jboss.tools.switchyard.reddeer.preference.implementation.DefaultTransactionPage;
import org.jboss.tools.switchyard.reddeer.wizard.SwitchYardProjectWizard;
import org.jboss.tools.switchyard.ui.bot.test.suite.CleanWorkspaceRequirement.CleanWorkspace;
import org.jboss.tools.switchyard.ui.bot.test.suite.PerspectiveRequirement.Perspective;
import org.jboss.tools.switchyard.ui.bot.test.suite.SwitchyardSuite;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * Testing implementations properties
 * 
 * @author tsedmik
 */
@CleanWorkspace
@Perspective(name = "Java EE")
@RunWith(SwitchyardSuite.class)
public class ImplementationsPropertiesTest {
	
	private static final String PROJECT = "ImplPropTestProject";
	private static final String BEAN = "BeanService";
	private static final String BEAN2 = "BeanService2";
	private ImplementationPropertiesPage properties;
	
	@BeforeClass
	public static void setUp() {
		
		new SwitchYardProjectWizard(PROJECT).create(); 
		new WaitWhile(new JobIsRunning(), TimePeriod.LONG);
		
		Component compo = new Bean().setService(BEAN).create();
		new WaitWhile(new JobIsRunning(), TimePeriod.LONG);
		
		Rectangle rectangle = compo.getAbsoluteBounds();
		Integer[] coords = {rectangle.x, rectangle.y + rectangle.height + 2};
		new Bean().setService(BEAN2).create(coords);
		new WaitWhile(new JobIsRunning(), TimePeriod.LONG);
	}
	
	@Before
	public void openProperties() {
		properties = new ImplementationPropertiesPage();
		properties.openProperties(new Component(BEAN + "Bean")); 
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
		
		DefaultResourcePage page = properties.getResourcePage();
		
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
		
		DefaultComponentPage page = properties.getComponentPage();
		assertEquals("BeanServiceBean", page.getName());
	}
	
	/**
	 * Tests "Properties --> Component --> Properties" menu (adds and remove properties)
	 */
	@Test
	public void componentPropertiesTest() {
		
		DefaultComponentPropertiesPage page = properties.getComponentPropertiesPage();
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
		
		DefaultContractPage page = properties.getContractPage();
		
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
		
		DefaultContractTransactionPage page = properties.getContractTransactionPage();
		
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
		
		DefaultContractSecurityPage page = properties.getContractSecurityPage();
		
		assertTrue(page.isAuthorizationEnabled());
		assertTrue(page.isAuthenticationEnabled());
		assertTrue(page.isConfidentalityEnabled());
		assertFalse(page.isAuthorizationChecked());
		assertFalse(page.isAuthenticationChecked());
		assertFalse(page.isConfidentalityChecked());
		
		page.setAuthorization(true);
		page.setAuthentication(true);
		page.setConfidentality(true);
		
		assertTrue(page.isAuthorizationChecked());
		assertTrue(page.isAuthenticationChecked());
		assertTrue(page.isConfidentalityChecked());
		assertFalse(page.isSecurityConfComboEnabled());
		
		properties.ok();
		DomainEditor domain = new DomainEditor();
		domain.addSecurityConfiguration("default", null, null, null, null);
		domain.addSecurityConfiguration("default1", null, null, null, null);
		new DefaultCTabItem("Design").activate();
		properties.openProperties(new Component(BEAN + "Bean"));
		page = properties.getContractSecurityPage();
		
		assertTrue(page.isSecurityConfComboEnabled());
		page.setSecurityConf("default1");
		
		// Check bug: https://issues.jboss.org/browse/SWITCHYARD-1732
		properties.ok();
		properties = new ImplementationPropertiesPage();
		properties.openProperties(new Component(BEAN2 + "Bean")); 
		page = properties.getContractSecurityPage();
		assertFalse(page.isAuthorizationChecked());
		assertFalse(page.isAuthenticationChecked());
		assertFalse(page.isConfidentalityChecked());
	}
	
	/**
	 * Tests "Properties --> Implementation" menu
	 */
	@Test
	public void implementationTest() {
		
		DefaultImplementationPage page = properties.getImplementationPage();
		
		assertEquals("com.example.switchyard.ImplPropTestProject.BeanServiceBean", page.getBeanClass());
		assertTrue(page.isBrowseButtonEnabled());
	}

	/**
	 * Tests "Properties --> Transaction Policy" menu
	 */
	@Test
	public void transactionPolicyTest() {
		
		DefaultTransactionPage page = properties.getTransactionPage();
		
		assertTrue(page.isTransactionPolicyComboEnabled());
		assertEquals(0, page.getComboSelectionIndex());
		assertEquals(4, page.getAllTransactionPolicies().size());
		assertTrue(page.getAllTransactionPolicies().contains("None"));
		assertTrue(page.getAllTransactionPolicies().contains("managedTransaction.Local"));
		assertTrue(page.getAllTransactionPolicies().contains("managedTransaction.Global"));
		assertTrue(page.getAllTransactionPolicies().contains("noManagedTransaction"));
		
		page.setTransactionPolicy("managedTransaction.Local");
	}
}
