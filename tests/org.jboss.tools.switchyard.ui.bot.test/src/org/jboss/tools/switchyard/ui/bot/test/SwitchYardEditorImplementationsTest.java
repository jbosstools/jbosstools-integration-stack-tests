package org.jboss.tools.switchyard.ui.bot.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.jboss.reddeer.core.handler.ShellHandler;
import org.jboss.reddeer.junit.execution.annotation.RunIf;
import org.jboss.reddeer.junit.requirement.inject.InjectRequirement;
import org.jboss.reddeer.junit.runner.RedDeerSuite;
import org.jboss.reddeer.swt.impl.button.NoButton;
import org.jboss.reddeer.swt.impl.ctab.DefaultCTabItem;
import org.jboss.reddeer.swt.impl.shell.DefaultShell;
import org.jboss.reddeer.workbench.impl.shell.WorkbenchShell;
import org.jboss.tools.switchyard.reddeer.component.SwitchYardComponent;
import org.jboss.tools.switchyard.reddeer.editor.DomainEditor;
import org.jboss.tools.switchyard.reddeer.editor.SwitchYardEditor;
import org.jboss.tools.switchyard.reddeer.preference.CompositePropertiesPage;
import org.jboss.tools.switchyard.reddeer.preference.component.ComponentPage;
import org.jboss.tools.switchyard.reddeer.preference.component.ComponentPropertiesPage;
import org.jboss.tools.switchyard.reddeer.preference.contract.ContractPage;
import org.jboss.tools.switchyard.reddeer.preference.contract.ContractSecurityPage;
import org.jboss.tools.switchyard.reddeer.preference.contract.ContractTransactionPage;
import org.jboss.tools.switchyard.reddeer.preference.implementation.ImplementationKnowledgePage;
import org.jboss.tools.switchyard.reddeer.preference.implementation.ImplementationPage;
import org.jboss.tools.switchyard.reddeer.preference.implementation.ImplementationSecurityPage;
import org.jboss.tools.switchyard.reddeer.preference.implementation.ImplementationTransactionPage;
import org.jboss.tools.switchyard.reddeer.project.SwitchYardProject;
import org.jboss.tools.switchyard.reddeer.requirement.SwitchYardRequirement;
import org.jboss.tools.switchyard.reddeer.requirement.SwitchYardRequirement.SwitchYard;
import org.jboss.tools.switchyard.reddeer.utils.PreferenceUtils;
import org.jboss.tools.switchyard.reddeer.wizard.BPELServiceWizard;
import org.jboss.tools.switchyard.reddeer.wizard.BPMNServiceWizard;
import org.jboss.tools.switchyard.reddeer.wizard.BeanServiceWizard;
import org.jboss.tools.switchyard.reddeer.wizard.CamelJavaServiceWizard;
import org.jboss.tools.switchyard.reddeer.wizard.CamelXMLServiceWizard;
import org.jboss.tools.switchyard.reddeer.wizard.DroolsServiceWizard;
import org.jboss.tools.switchyard.reddeer.wizard.ExistingBPELServiceWizard;
import org.jboss.tools.switchyard.reddeer.wizard.ExistingBPMNServiceWizard;
import org.jboss.tools.switchyard.reddeer.wizard.ExistingCamelXMLServiceWizard;
import org.jboss.tools.switchyard.reddeer.wizard.ExistingDroolsServiceWizard;
import org.jboss.tools.switchyard.reddeer.wizard.ImportFileWizard;
import org.jboss.tools.switchyard.ui.bot.test.condition.IssueIsClosed;
import org.jboss.tools.switchyard.ui.bot.test.condition.IssueIsClosed.Jira;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * 
 * @author apodhrad
 *
 */
@SwitchYard
@RunWith(RedDeerSuite.class)
public class SwitchYardEditorImplementationsTest {

	public static final String PROJECT_NAME = "sy_implementations";
	public static final String PROJECT_PACKAGE = "com.example.switchyard." + PROJECT_NAME;
	public static final String SERVICE__NAME = "Hello";
	public static final String SERVICE_IMPL_NAME = "HelloImpl";

	public static final String XPATH_RULES = "/switchyard/composite/component/implementation.rules";
	public static final String XPATH_BPM = "/switchyard/composite/component/implementation.bpm";

	private static int index = 0;

	@InjectRequirement
	private static SwitchYardRequirement switchYardRequirement;

	private static String autoBuilding;

	private List<String[]> classesToDelete;
	private List<String[]> resourcesToDelete;
	private List<String> componentsToDelete;

	@BeforeClass
	public static void maximizeWorkbench() {
		new WorkbenchShell().maximize();
	}

	@BeforeClass
	public static void turnOffAutoBuilding() {
		autoBuilding = PreferenceUtils.getAutoBuilding();
		PreferenceUtils.setAutoBuilding("false");
	}

	@AfterClass
	public static void turnBackAutoBuilding() {
		if (autoBuilding != null) {
			PreferenceUtils.setAutoBuilding(autoBuilding);
		}
	}

	@BeforeClass
	public static void createProject() {
		switchYardRequirement.project(PROJECT_NAME).implAll().create();

		new SwitchYardProject(PROJECT_NAME).getProjectItem("src/main/resources").select();
		new ImportFileWizard().importFile("resources/" + PROJECT_NAME);
	}

	@Before
	public void initClassToDelete() {
		classesToDelete = new ArrayList<String[]>();
		resourcesToDelete = new ArrayList<String[]>();
		componentsToDelete = new ArrayList<String>();
	}

	@After
	public void deleteCreatedResousources() {
		try {
			new DefaultShell("Replace Current Implementation");
			new NoButton().click();
		} catch (Exception e) {
			// ok
		}

		ShellHandler.getInstance().closeAllNonWorbenchShells();

		List<SwitchYardComponent> components = new SwitchYardEditor().getComponents();
		while (!components.isEmpty()) {
			components.get(0).delete();
			components = new SwitchYardEditor().getComponents();
		}
		for (String[] javaClass : classesToDelete) {
			new SwitchYardProject(PROJECT_NAME).getClass(javaClass).delete();
		}
		for (String[] resource : resourcesToDelete) {
			new SwitchYardProject(PROJECT_NAME).getResource(resource).delete();
		}
	}

	@Test
	public void addBeanImplementationWithNewJavaInterfaceTest() throws Exception {
		SwitchYardEditor editor = new SwitchYardEditor();
		BeanServiceWizard beanWizard = editor.addBeanImplementation();
		beanWizard.createJavaInterface("NewBeanInterface");
		beanWizard.setName("NewBeanImpl");
		beanWizard.finish();

		editor.save();
		removeComponentAfterTest("NewBeanImpl");
		removeClassAfterTest("NewBeanImpl.java");

		assertEquals("1", editor.xpath("count(/switchyard/composite/component)"));
		assertEquals("NewBeanImpl", editor.xpath("/switchyard/composite/component/@name"));
		assertEquals("1", editor.xpath("count(/switchyard/composite/component/implementation.bean)"));
		assertEquals(PROJECT_PACKAGE + ".NewBeanImpl",
				editor.xpath("/switchyard/composite/component/implementation.bean/@class"));
		checkImplementationProperties("NewBeanImpl", "com.example.switchyard.sy_implementations.NewBeanImpl");
		checkContractProperties("NewBeanImpl", "NewBeanInterface", "Java");
		checkComponentProperties("NewBeanImpl", "NewBeanImpl");
	}

	@Test
	public void addCamelJavaImplementationWithNewJavaInterfaceTest() throws Exception {
		SwitchYardEditor editor = new SwitchYardEditor();
		CamelJavaServiceWizard camelJavaWizard = editor.addCamelJavaImplementation();
		camelJavaWizard.createJavaInterface("NewCamelJavaInterface");
		camelJavaWizard.setName("NewCamelJavaImpl");
		camelJavaWizard.finish();

		editor.save();
		removeComponentAfterTest("NewCamelJavaImpl");
		removeClassAfterTest("NewCamelJavaImpl.java");

		assertEquals("1", editor.xpath("count(/switchyard/composite/component)"));
		assertEquals("NewCamelJavaImpl", editor.xpath("/switchyard/composite/component/@name"));
		assertEquals("1", editor.xpath("count(/switchyard/composite/component/implementation.camel)"));
		assertEquals("1", editor.xpath("count(/switchyard/composite/component/implementation.camel/java)"));
		assertEquals(PROJECT_PACKAGE + ".NewCamelJavaImpl",
				editor.xpath("/switchyard/composite/component/implementation.camel/java/@class"));
		checkImplementationProperties("NewCamelJavaImpl", "com.example.switchyard.sy_implementations.NewCamelJavaImpl");
		checkContractProperties("NewCamelJavaImpl", "NewCamelJavaInterface", "Java");
		checkComponentProperties("NewCamelJavaImpl", "NewCamelJavaImpl");
	}

	@Test
	public void addCamelXMLImplementationWithNewJavaInterfaceTest() throws Exception {
		SwitchYardEditor editor = new SwitchYardEditor();
		CamelXMLServiceWizard camelXMLWizard = editor.addCamelXMLImplementation();
		camelXMLWizard.createJavaInterface("NewCamelXMLInterface");
		camelXMLWizard.setFileName("NewCamelXMLImpl");
		camelXMLWizard.finish();

		editor.save();
		removeComponentAfterTest("NewCamelXMLImpl");
		removeResourceAfterTest("NewCamelXMLImpl.xml");

		assertEquals("1", editor.xpath("count(/switchyard/composite/component)"));
		assertEquals("NewCamelXMLImpl", editor.xpath("/switchyard/composite/component/@name"));
		assertEquals("1", editor.xpath("count(/switchyard/composite/component/implementation.camel)"));
		assertEquals("1", editor.xpath("count(/switchyard/composite/component/implementation.camel/xml)"));
		assertEquals("NewCamelXMLImpl.xml",
				editor.xpath("/switchyard/composite/component/implementation.camel/xml/@path"));
		checkImplementationProperties("NewCamelXMLImpl", "NewCamelXMLImpl.xml");
		checkContractProperties("NewCamelXMLImpl", "NewCamelXMLInterface", "Java");
		checkComponentProperties("NewCamelXMLImpl", "NewCamelXMLImpl");
	}

	@Test
	public void addExistingCamelXMLImplementationTest() throws Exception {
		SwitchYardEditor editor = new SwitchYardEditor();
		SwitchYardComponent component = editor.addComponent();
		ExistingCamelXMLServiceWizard camelWizard = editor.addCamelXmlImplementation(component);
		camelWizard.selectExistingImplementation("ExistingCamelXMLImpl");
		camelWizard.finish();

		editor.save();
		removeComponentAfterTest("Component");

		assertEquals("1", editor.xpath("count(/switchyard/composite/component)"));
		assertEquals("Component", editor.xpath("/switchyard/composite/component/@name"));
		assertEquals("1", editor.xpath("count(/switchyard/composite/component/implementation.camel)"));
		assertEquals("1", editor.xpath("count(/switchyard/composite/component/implementation.camel/xml)"));
		assertEquals("ExistingCamelXMLImpl.xml",
				editor.xpath("/switchyard/composite/component/implementation.camel/xml/@path"));
	}

	@Test
	public void addBPELImplementationWithNewWSDLInterfaceTest() throws Exception {
		SwitchYardEditor editor = new SwitchYardEditor();
		BPELServiceWizard bpelWizard = editor.addBPELImplementation();
		bpelWizard.createWSDLInterface("NewBPELInterface");
		bpelWizard.setFileName("NewBPELImpl");
		bpelWizard.finish();

		editor.save();
		removeComponentAfterTest("NewBPELImpl");
		removeResourceAfterTest("NewBPELImpl.bpel");

		assertEquals("1", editor.xpath("count(/switchyard/composite/component)"));
		assertEquals("NewBPELImpl", editor.xpath("/switchyard/composite/component/@name"));
		assertEquals("1", editor.xpath("count(/switchyard/composite/component/implementation.bpel)"));
		assertEquals("newBPELImpl:NewBPELImpl",
				editor.xpath("/switchyard/composite/component/implementation.bpel/@process"));
		checkImplementationProperties("NewBPELImpl", "{urn:com.example.switchyard:sy_implementations:1.0}NewBPELImpl");
		checkContractProperties("NewBPELImpl", "NewBPELInterface", "WSDL");
		checkComponentProperties("NewBPELImpl", "NewBPELImpl");
	}

	@Test
	public void addExistingBPELImplementationTest() throws Exception {
		SwitchYardEditor editor = new SwitchYardEditor();
		SwitchYardComponent component = editor.addComponent();
		ExistingBPELServiceWizard bpelWizard = editor.addBPELImplementation(component);
		bpelWizard.selectExistingImplementation("ExistingBPELImpl");
		bpelWizard.finish();

		editor.save();
		removeComponentAfterTest("Component");

		assertEquals("1", editor.xpath("count(/switchyard/composite/component)"));
		assertEquals("Component", editor.xpath("/switchyard/composite/component/@name"));
		assertEquals("1", editor.xpath("count(/switchyard/composite/component/implementation.bpel)"));
		assertEquals("existingBPELImpl:SayHello",
				editor.xpath("/switchyard/composite/component/implementation.bpel/@process"));
	}

	@Test
	public void addBPMNImplementationWithNewJavaInterfaceTest() throws Exception {
		SwitchYardEditor editor = new SwitchYardEditor();
		BPMNServiceWizard bpmnWizard = editor.addBPMNImplementation();
		bpmnWizard.createJavaInterface("NewBPMNInterface");
		bpmnWizard.setFileName("NewBPMNImpl");
		bpmnWizard.finish();

		editor.save();
		removeComponentAfterTest("NewBPMNImpl");
		removeResourceAfterTest("NewBPMNImpl.bpmn");

		assertEquals("1", editor.xpath("count(/switchyard/composite/component)"));
		assertEquals("NewBPMNImpl", editor.xpath("/switchyard/composite/component/@name"));
		assertEquals("1", editor.xpath("count(" + XPATH_BPM + ")"));
		assertEquals("1", editor.xpath("count(" + XPATH_BPM + "/manifest)"));
		assertEquals("1", editor.xpath("count(" + XPATH_BPM + "/manifest/resources)"));
		assertEquals("1", editor.xpath("count(" + XPATH_BPM + "/manifest/resources/resource)"));
		assertEquals("NewBPMNImpl.bpmn", editor.xpath(XPATH_BPM + "/manifest/resources/resource/@location"));
		assertEquals("BPMN2", editor.xpath(XPATH_BPM + "/manifest/resources/resource/@type"));

		checkBPMOperationsProperties("NewBPMNImpl", "operation1", "START_PROCESS");
		checkBPMOperationsProperties("NewBPMNImpl", "operation2", "SIGNAL_EVENT");
		checkBPMAdvancedProperties("NewBPMNImpl");
	}

	@Test
	public void addExistingBPMNImplementationTest() throws Exception {
		SwitchYardEditor editor = new SwitchYardEditor();
		SwitchYardComponent component = editor.addComponent();
		ExistingBPMNServiceWizard bpmnWizard = editor.addBPMNImplementation(component);
		bpmnWizard.selectProjectResource();
		bpmnWizard.selectExistingImplementation("ExistingBPMNImpl");
		bpmnWizard.finish();

		editor.save();
		removeComponentAfterTest("Component");

		assertEquals("1", editor.xpath("count(/switchyard/composite/component)"));
		assertEquals("Component", editor.xpath("/switchyard/composite/component/@name"));
		assertEquals("1", editor.xpath("count(" + XPATH_BPM + ")"));
		assertEquals("1", editor.xpath("count(" + XPATH_BPM + "/manifest)"));
		assertEquals("1", editor.xpath("count(" + XPATH_BPM + "/manifest/resources)"));
		assertEquals("1", editor.xpath("count(" + XPATH_BPM + "/manifest/resources/resource)"));
		assertEquals("ExistingBPMNImpl.bpmn", editor.xpath(XPATH_BPM + "/manifest/resources/resource/@location"));
		assertEquals("BPMN2", editor.xpath(XPATH_BPM + "/manifest/resources/resource/@type"));

		checkBPMOperationsProperties("Component", "operation1", "START_PROCESS");
		checkBPMOperationsProperties("Component", "operation2", "SIGNAL_EVENT");
	}

	@Test
	public void addExistingBPMNImplementationWithKnowledgeContainerTest() throws Exception {
		SwitchYardEditor editor = new SwitchYardEditor();
		SwitchYardComponent component = editor.addComponent();
		ExistingBPMNServiceWizard bpmnWizard = editor.addBPMNImplementation(component);
		bpmnWizard.selectKnowledgeContainer();
		bpmnWizard.getSessionName().setText("session");
		bpmnWizard.getBaseName().setText("base");
		bpmnWizard.getGroupID().setText("com.example");
		bpmnWizard.getArtifactID().setText("hello-world");
		bpmnWizard.getVersion().setText("1.0.0-SNAPSHOT");

		bpmnWizard.getScanForUpdates().toggle(false);
		Assert.assertFalse(bpmnWizard.getScanInterval().isEnabled());

		bpmnWizard.getScanForUpdates().toggle(true);
		Assert.assertTrue(bpmnWizard.getScanInterval().isEnabled());
		bpmnWizard.getScanInterval().setText("12345");

		bpmnWizard.finish();

		editor.save();
		removeComponentAfterTest("Component");

		assertEquals("1", editor.xpath("count(/switchyard/composite/component)"));
		assertEquals("Component", editor.xpath("/switchyard/composite/component/@name"));
		assertEquals("1", editor.xpath("count(" + XPATH_BPM + ")"));
		assertEquals("1", editor.xpath("count(" + XPATH_BPM + "/manifest)"));
		assertEquals("1", editor.xpath("count(" + XPATH_BPM + "/manifest/container)"));
		assertEquals("session", editor.xpath(XPATH_BPM + "/manifest/container/@sessionName"));
		assertEquals("base", editor.xpath(XPATH_BPM + "/manifest/container/@baseName"));
		assertEquals("com.example:hello-world:1.0.0-SNAPSHOT",
				editor.xpath(XPATH_BPM + "/manifest/container/@releaseId"));
		assertEquals("true", editor.xpath(XPATH_BPM + "/manifest/container/@scan"));
		assertEquals("12345", editor.xpath(XPATH_BPM + "/manifest/container/@scanInterval"));
	}

	@Test
	public void addExistingBPMNImplementationWithRemoteJMSTest() throws Exception {
		SwitchYardEditor editor = new SwitchYardEditor();
		SwitchYardComponent component = editor.addComponent();
		ExistingBPMNServiceWizard bpmnWizard = editor.addBPMNImplementation(component);
		bpmnWizard.selectRemoteJMS();
		bpmnWizard.getDeploymentID().setText("jmsID");
		bpmnWizard.getHostName().setText("jmsHost");
		bpmnWizard.getRemotingPort().setText("9001");
		bpmnWizard.getMessagingPort().setText("9002");
		bpmnWizard.getUserName().setText("jmsAdmin");
		bpmnWizard.getPassword().setText("jmsAdmin123$");
		bpmnWizard.getTimeout().setText("123");

		bpmnWizard.getUseSSL().setSelection("false");
		Assert.assertFalse(bpmnWizard.getKeystorePassword().isEnabled());
		Assert.assertFalse(bpmnWizard.getKeystoreLocation().isEnabled());
		Assert.assertFalse(bpmnWizard.getTruststorePassword().isEnabled());
		Assert.assertFalse(bpmnWizard.getTruststoreLocation().isEnabled());

		bpmnWizard.getUseSSL().setSelection("true");
		Assert.assertTrue(bpmnWizard.getKeystorePassword().isEnabled());
		Assert.assertTrue(bpmnWizard.getKeystoreLocation().isEnabled());
		Assert.assertTrue(bpmnWizard.getTruststorePassword().isEnabled());
		Assert.assertTrue(bpmnWizard.getTruststoreLocation().isEnabled());

		bpmnWizard.getKeystorePassword().setText("keystorePass");
		bpmnWizard.getKeystoreLocation().setText("keystoreLoc");
		bpmnWizard.getTruststorePassword().setText("truststorePass");
		bpmnWizard.getTruststoreLocation().setText("truststoreLoc");

		bpmnWizard.finish();

		editor.save();
		removeComponentAfterTest("Component");

		assertEquals("1", editor.xpath("count(/switchyard/composite/component)"));
		assertEquals("Component", editor.xpath("/switchyard/composite/component/@name"));
		assertEquals("1", editor.xpath("count(" + XPATH_BPM + ")"));
		assertEquals("1", editor.xpath("count(" + XPATH_BPM + "/manifest)"));
		assertEquals("1", editor.xpath("count(" + XPATH_BPM + "/manifest/remoteJms)"));
		assertEquals("jmsID", editor.xpath(XPATH_BPM + "/manifest/remoteJms/@deploymentId"));
		assertEquals("jmsAdmin", editor.xpath(XPATH_BPM + "/manifest/remoteJms/@userName"));
		assertEquals("jmsAdmin123$", editor.xpath(XPATH_BPM + "/manifest/remoteJms/@password"));
		assertEquals("123", editor.xpath(XPATH_BPM + "/manifest/remoteJms/@timeout"));
		assertEquals("jmsHost", editor.xpath(XPATH_BPM + "/manifest/remoteJms/@hostName"));
		assertEquals("9001", editor.xpath(XPATH_BPM + "/manifest/remoteJms/@remotingPort"));
		assertEquals("9002", editor.xpath(XPATH_BPM + "/manifest/remoteJms/@messagingPort"));
		assertEquals("true", editor.xpath(XPATH_BPM + "/manifest/remoteJms/@useSsl"));
		assertEquals("keystoreLoc", editor.xpath(XPATH_BPM + "/manifest/remoteJms/@keystoreLocation"));
		assertEquals("keystorePass", editor.xpath(XPATH_BPM + "/manifest/remoteJms/@keystorePassword"));
		assertEquals("truststoreLoc", editor.xpath(XPATH_BPM + "/manifest/remoteJms/@truststoreLocation"));
		assertEquals("truststorePass", editor.xpath(XPATH_BPM + "/manifest/remoteJms/@truststorePassword"));
	}

	@Test
	public void addExistingBPMNImplementationWithRemoteRESTTest() throws Exception {
		SwitchYardEditor editor = new SwitchYardEditor();
		SwitchYardComponent component = editor.addComponent();
		ExistingBPMNServiceWizard bpmnWizard = editor.addBPMNImplementation(component);
		bpmnWizard.selectRemoteREST();
		bpmnWizard.getDeploymentID().setText("restID");
		bpmnWizard.getRESTURL().setText("http://localhost/rest");
		bpmnWizard.getUserName().setText("restAdmin");
		bpmnWizard.getPassword().setText("restAdmin123$");
		bpmnWizard.getTimeout().setText("123");
		bpmnWizard.getUseFormBasedAuthentication().setSelection("true");
		bpmnWizard.finish();

		editor.save();
		removeComponentAfterTest("Component");

		assertEquals("1", editor.xpath("count(/switchyard/composite/component)"));
		assertEquals("Component", editor.xpath("/switchyard/composite/component/@name"));
		assertEquals("1", editor.xpath("count(" + XPATH_BPM + ")"));
		assertEquals("1", editor.xpath("count(" + XPATH_BPM + "/manifest)"));
		assertEquals("1", editor.xpath("count(" + XPATH_BPM + "/manifest/remoteRest)"));
		assertEquals("restID", editor.xpath(XPATH_BPM + "/manifest/remoteRest/@deploymentId"));
		assertEquals("http://localhost/rest", editor.xpath(XPATH_BPM + "/manifest/remoteRest/@url"));
		assertEquals("restAdmin", editor.xpath(XPATH_BPM + "/manifest/remoteRest/@userName"));
		assertEquals("restAdmin123$", editor.xpath(XPATH_BPM + "/manifest/remoteRest/@password"));
		assertEquals("123", editor.xpath(XPATH_BPM + "/manifest/remoteRest/@timeout"));
		assertEquals("true", editor.xpath(XPATH_BPM + "/manifest/remoteRest/@useFormBasedAuth"));
	}

	@Test
	@Jira("SWITCHYARD-2782")
	@RunIf(conditionClass = IssueIsClosed.class)
	public void addDroolsImplementationWithNewJavaInterfaceTest() throws Exception {
		SwitchYardEditor editor = new SwitchYardEditor();
		DroolsServiceWizard droolsWizard = editor.addDroolsImplementation();
		droolsWizard.createJavaInterface("NewDroolsInterface");
		droolsWizard.setFileName("NewDroolsImpl");
		droolsWizard.finish();

		editor.save();
		removeComponentAfterTest("NewDroolsImpl");
		removeResourceAfterTest("NewDroolsImpl.drl");

		assertEquals("1", editor.xpath("count(/switchyard/composite/component)"));
		assertEquals("NewDroolsImpl", editor.xpath("/switchyard/composite/component/@name"));
		assertEquals("1", editor.xpath("count(" + XPATH_RULES + ")"));
		assertEquals("1", editor.xpath("count(" + XPATH_RULES + "/manifest)"));
		assertEquals("1", editor.xpath("count(" + XPATH_RULES + "/manifest/resources)"));
		assertEquals("1", editor.xpath("count(" + XPATH_RULES + "/manifest/resources/resource)"));
		assertEquals("NewDroolsImpl.drl", editor.xpath(XPATH_RULES + "/manifest/resources/resource/@location"));
		assertEquals("DRL", editor.xpath(XPATH_RULES + "/manifest/resources/resource/@type"));

		checkDroolsOperationsProperties("NewDroolsImpl", "operation1", "EXECUTE");
		checkDroolsOperationsProperties("NewDroolsImpl", "operation2", "INSERT");
		checkDroolsAdvancedProperties("NewDroolsImpl");
	}

	@Test
	public void addExistingDroolsImplementationTest() throws Exception {
		SwitchYardEditor editor = new SwitchYardEditor();
		SwitchYardComponent component = editor.addComponent();
		ExistingDroolsServiceWizard droolsWizard = editor.addDroolsImplementation(component);
		droolsWizard.selectProjectResource();
		droolsWizard.selectExistingImplementation("ExistingDroolsImpl");
		droolsWizard.finish();

		editor.save();
		removeComponentAfterTest("Component");

		assertEquals("1", editor.xpath("count(/switchyard/composite/component)"));
		assertEquals("Component", editor.xpath("/switchyard/composite/component/@name"));
		assertEquals("1", editor.xpath("count(" + XPATH_RULES + ")"));
		assertEquals("1", editor.xpath("count(" + XPATH_RULES + "/manifest)"));
		assertEquals("1", editor.xpath("count(" + XPATH_RULES + "/manifest/resources)"));
		assertEquals("1", editor.xpath("count(" + XPATH_RULES + "/manifest/resources/resource)"));
		assertEquals("ExistingDroolsImpl.drl", editor.xpath(XPATH_RULES + "/manifest/resources/resource/@location"));
		assertEquals("DRL", editor.xpath(XPATH_RULES + "/manifest/resources/resource/@type"));

		checkDroolsOperationsProperties("Component", "operation1", "EXECUTE");
		checkDroolsOperationsProperties("Component", "operation2", "INSERT");
	}

	@Test
	public void addExistingDroolsImplementationWithKnowledgeContainerTest() throws Exception {
		SwitchYardEditor editor = new SwitchYardEditor();
		SwitchYardComponent component = editor.addComponent();
		ExistingDroolsServiceWizard droolsWizard = editor.addDroolsImplementation(component);
		droolsWizard.selectKnowledgeContainer();
		droolsWizard.getSessionName().setText("session");
		droolsWizard.getBaseName().setText("base");
		droolsWizard.getGroupID().setText("com.example");
		droolsWizard.getArtifactID().setText("hello-world");
		droolsWizard.getVersion().setText("1.0.0-SNAPSHOT");

		droolsWizard.getScanForUpdates().toggle(false);
		Assert.assertFalse(droolsWizard.getScanInterval().isEnabled());

		droolsWizard.getScanForUpdates().toggle(true);
		Assert.assertTrue(droolsWizard.getScanInterval().isEnabled());
		droolsWizard.getScanInterval().setText("12345");

		droolsWizard.finish();

		editor.save();
		removeComponentAfterTest("Component");

		assertEquals("1", editor.xpath("count(/switchyard/composite/component)"));
		assertEquals("Component", editor.xpath("/switchyard/composite/component/@name"));
		assertEquals("1", editor.xpath("count(" + XPATH_RULES + ")"));
		assertEquals("1", editor.xpath("count(" + XPATH_RULES + "/manifest)"));
		assertEquals("1", editor.xpath("count(" + XPATH_RULES + "/manifest/container)"));
		assertEquals("session", editor.xpath(XPATH_RULES + "/manifest/container/@sessionName"));
		assertEquals("base", editor.xpath(XPATH_RULES + "/manifest/container/@baseName"));
		assertEquals("com.example:hello-world:1.0.0-SNAPSHOT",
				editor.xpath(XPATH_RULES + "/manifest/container/@releaseId"));
		assertEquals("true", editor.xpath(XPATH_RULES + "/manifest/container/@scan"));
		assertEquals("12345", editor.xpath(XPATH_RULES + "/manifest/container/@scanInterval"));
	}

	@Test
	@Ignore("Removed as a part of SWITCHYARD-2817")
	public void addExistingDroolsImplementationWithRemoteJMSTest() throws Exception {
		SwitchYardEditor editor = new SwitchYardEditor();
		SwitchYardComponent component = editor.addComponent();
		ExistingDroolsServiceWizard droolsWizard = editor.addDroolsImplementation(component);
		droolsWizard.selectRemoteJMS();
		droolsWizard.getDeploymentID().setText("jmsID");
		droolsWizard.getHostName().setText("jmsHost");
		droolsWizard.getRemotingPort().setText("9001");
		droolsWizard.getMessagingPort().setText("9002");
		droolsWizard.getUserName().setText("jmsAdmin");
		droolsWizard.getPassword().setText("jmsAdmin123$");
		droolsWizard.getTimeout().setText("123");

		droolsWizard.getUseSSL().setSelection("false");
		Assert.assertFalse(droolsWizard.getKeystorePassword().isEnabled());
		Assert.assertFalse(droolsWizard.getKeystoreLocation().isEnabled());
		Assert.assertFalse(droolsWizard.getTruststorePassword().isEnabled());
		Assert.assertFalse(droolsWizard.getTruststoreLocation().isEnabled());

		droolsWizard.getUseSSL().setSelection("true");
		Assert.assertTrue(droolsWizard.getKeystorePassword().isEnabled());
		Assert.assertTrue(droolsWizard.getKeystoreLocation().isEnabled());
		Assert.assertTrue(droolsWizard.getTruststorePassword().isEnabled());
		Assert.assertTrue(droolsWizard.getTruststoreLocation().isEnabled());

		droolsWizard.getKeystorePassword().setText("keystorePass");
		droolsWizard.getKeystoreLocation().setText("keystoreLoc");
		droolsWizard.getTruststorePassword().setText("truststorePass");
		droolsWizard.getTruststoreLocation().setText("truststoreLoc");

		droolsWizard.finish();

		editor.save();
		removeComponentAfterTest("Component");

		assertEquals("1", editor.xpath("count(/switchyard/composite/component)"));
		assertEquals("Component", editor.xpath("/switchyard/composite/component/@name"));
		assertEquals("1", editor.xpath("count(" + XPATH_RULES + ")"));
		assertEquals("1", editor.xpath("count(" + XPATH_RULES + "/manifest)"));
		assertEquals("1", editor.xpath("count(" + XPATH_RULES + "/manifest/remoteJms)"));
		assertEquals("jmsID", editor.xpath(XPATH_RULES + "/manifest/remoteJms/@deploymentId"));
		assertEquals("jmsAdmin", editor.xpath(XPATH_RULES + "/manifest/remoteJms/@userName"));
		assertEquals("jmsAdmin123$", editor.xpath(XPATH_RULES + "/manifest/remoteJms/@password"));
		assertEquals("123", editor.xpath(XPATH_RULES + "/manifest/remoteJms/@timeout"));
		assertEquals("jmsHost", editor.xpath(XPATH_RULES + "/manifest/remoteJms/@hostName"));
		assertEquals("9001", editor.xpath(XPATH_RULES + "/manifest/remoteJms/@remotingPort"));
		assertEquals("9002", editor.xpath(XPATH_RULES + "/manifest/remoteJms/@messagingPort"));
		assertEquals("true", editor.xpath(XPATH_RULES + "/manifest/remoteJms/@useSsl"));
		assertEquals("keystoreLoc", editor.xpath(XPATH_RULES + "/manifest/remoteJms/@keystoreLocation"));
		assertEquals("keystorePass", editor.xpath(XPATH_RULES + "/manifest/remoteJms/@keystorePassword"));
		assertEquals("truststoreLoc", editor.xpath(XPATH_RULES + "/manifest/remoteJms/@truststoreLocation"));
		assertEquals("truststorePass", editor.xpath(XPATH_RULES + "/manifest/remoteJms/@truststorePassword"));
	}

	@Test
	@Ignore("Removed as a part of SWITCHYARD-2817")
	public void addExistingDroolsImplementationWithRemoteRESTTest() throws Exception {
		SwitchYardEditor editor = new SwitchYardEditor();
		SwitchYardComponent component = editor.addComponent();
		ExistingDroolsServiceWizard bpmnWizard = editor.addDroolsImplementation(component);
		bpmnWizard.selectRemoteREST();
		bpmnWizard.getDeploymentID().setText("restID");
		bpmnWizard.getRESTURL().setText("http://localhost/rest");
		bpmnWizard.getUserName().setText("restAdmin");
		bpmnWizard.getPassword().setText("restAdmin123$");
		bpmnWizard.getTimeout().setText("123");
		bpmnWizard.getUseFormBasedAuthentication().setSelection("true");
		bpmnWizard.finish();

		editor.save();
		removeComponentAfterTest("Component");

		assertEquals("1", editor.xpath("count(/switchyard/composite/component)"));
		assertEquals("Component", editor.xpath("/switchyard/composite/component/@name"));
		assertEquals("1", editor.xpath("count(" + XPATH_RULES + ")"));
		assertEquals("1", editor.xpath("count(" + XPATH_RULES + "/manifest)"));
		assertEquals("1", editor.xpath("count(" + XPATH_RULES + "/manifest/remoteRest)"));
		assertEquals("restID", editor.xpath(XPATH_RULES + "/manifest/remoteRest/@deploymentId"));
		assertEquals("http://localhost/rest", editor.xpath(XPATH_RULES + "/manifest/remoteRest/@url"));
		assertEquals("restAdmin", editor.xpath(XPATH_RULES + "/manifest/remoteRest/@userName"));
		assertEquals("restAdmin123$", editor.xpath(XPATH_RULES + "/manifest/remoteRest/@password"));
		assertEquals("123", editor.xpath(XPATH_RULES + "/manifest/remoteRest/@timeout"));
		assertEquals("true", editor.xpath(XPATH_RULES + "/manifest/remoteRest/@useFormBasedAuth"));
	}

	private void checkImplementationProperties(String tooltip, String name) throws Exception {
		CompositePropertiesPage properties = new SwitchYardComponent(tooltip).showProperties();

		ImplementationPage implPage = properties.selectImplementation();
		assertEquals(name, implPage.getName());
		assertTrue(implPage.isBrowseButtonEnabled());

		ImplementationTransactionPage implTransactionpage = properties.selectImplementationTransaction();
		assertTrue(implTransactionpage.isTransactionPolicyComboEnabled());
		assertEquals(0, implTransactionpage.getComboSelectionIndex());
		assertEquals(4, implTransactionpage.getAllTransactionPolicies().size());
		assertTrue(implTransactionpage.getAllTransactionPolicies().contains("None"));
		assertTrue(implTransactionpage.getAllTransactionPolicies().contains("managedTransaction.Local"));
		assertTrue(implTransactionpage.getAllTransactionPolicies().contains("managedTransaction.Global"));
		assertTrue(implTransactionpage.getAllTransactionPolicies().contains("noManagedTransaction"));
		implTransactionpage.setTransactionPolicy("managedTransaction.Local");

		ImplementationSecurityPage implSecurityPage = properties.selectImplementationSecurity();
		assertTrue(implSecurityPage.isAuthorizationEnabled());
		implSecurityPage.setAuthorization(true);
		properties.ok();
		properties = new SwitchYardComponent(tooltip).showProperties();
		assertTrue(implSecurityPage.isAuthorizationChecked());
		implSecurityPage.setAuthorization(false);
		properties.ok();
		properties = new SwitchYardComponent(tooltip).showProperties();
		assertFalse(implSecurityPage.isAuthorizationChecked());

		properties.ok();
	}

	private void checkContractProperties(String tooltip, String name, String type) throws Exception {
		CompositePropertiesPage properties = new SwitchYardComponent(tooltip).showProperties();

		ContractPage contractPage = properties.selectContract();

		assertEquals(type.equals("Java"), contractPage.isInterfaceTypeSelected("Java"));
		assertEquals(type.equals("WSDL"), contractPage.isInterfaceTypeSelected("WSDL"));
		assertEquals(type.equals("ESB"), contractPage.isInterfaceTypeSelected("ESB"));
		assertEquals(name, contractPage.getServiceName());

		ContractTransactionPage contractTransactionPage = properties.selectContractTransaction();

		assertTrue(contractTransactionPage.isComboEnabled());
		assertEquals(0, contractTransactionPage.getComboSelectionIndex());
		assertEquals(3, contractTransactionPage.getAllTransactionPolicies().size());
		assertTrue(contractTransactionPage.getAllTransactionPolicies().contains("None"));
		assertTrue(contractTransactionPage.getAllTransactionPolicies().contains("propagatesTransaction"));
		assertTrue(contractTransactionPage.getAllTransactionPolicies().contains("suspendsTransaction"));

		contractTransactionPage.setTransactionPolicy("propagatesTransaction");

		ContractSecurityPage page = properties.selectContractSecurity();

		assertTrue(page.isAuthenticationEnabled());
		assertTrue(page.isConfidentalityEnabled());
		assertFalse(page.isAuthenticationChecked());
		assertFalse(page.isConfidentalityChecked());

		page.setAuthentication(true);
		page.setConfidentality(true);

		assertTrue(page.isAuthenticationChecked());
		assertTrue(page.isConfidentalityChecked());

		properties.ok();

		DomainEditor domain = new DomainEditor();
		String securityConfiguration1 = "default" + index++;
		String securityConfiguration2 = "default" + index++;
		domain.addSecurityConfiguration(securityConfiguration1, null, null, null, null);
		domain.addSecurityConfiguration(securityConfiguration2, null, null, null, null);
		new DefaultCTabItem("Design").activate();
		properties = new SwitchYardComponent(tooltip).showProperties();
		page = properties.selectContractSecurity();
		assertTrue(page.isSecurityConfComboEnabled());
		page.setSecurityConf(securityConfiguration1);

		// Complete the following use case
		// Check bug: https://issues.jboss.org/browse/SWITCHYARD-1732
		// properties.ok();
		// properties = new SwitchYardComponent(BEAN2 + "Bean").showProperties();
		// page = properties.selectContractSecurity();
		// assertFalse(page.isAuthenticationChecked());
		// assertFalse(page.isConfidentalityChecked());

		properties.ok();
	}

	private void checkComponentProperties(String tooltip, String name) throws Exception {
		CompositePropertiesPage properties = new SwitchYardComponent(tooltip).showProperties();

		ComponentPage componanetPage = properties.selectComponent();
		assertEquals(name, componanetPage.getName());

		ComponentPropertiesPage componentPropertiesPage = properties.selectComponentProperties();
		assertEquals(0, componentPropertiesPage.getPropertiesCount());
		componentPropertiesPage.addProperty("AAA", "BBB");
		assertEquals(1, componentPropertiesPage.getPropertiesCount());
		componentPropertiesPage.removeProperty("AAA");
		assertEquals(0, componentPropertiesPage.getPropertiesCount());

		properties.ok();
	}

	private void checkBPMOperationsProperties(String tooltip, String operation, String type) throws Exception {
		checkOperationsProperties(XPATH_BPM, tooltip, operation, type);
	}

	private void checkDroolsOperationsProperties(String tooltip, String operation, String type) throws Exception {
		checkOperationsProperties(XPATH_RULES, tooltip, operation, type);
	}

	private void checkOperationsProperties(String xpath, String tooltip, String operation, String type)
			throws Exception {
		SwitchYardEditor editor = new SwitchYardEditor();

		ImplementationKnowledgePage rulesPage = new SwitchYardComponent(tooltip).showProperties()
				.selectRulesImplementation();
		rulesPage.selectOperations();
		rulesPage.addOperation(operation, type, null);
		rulesPage.addGlobal(operation + "globalfrom", operation + "globalto");
		rulesPage.addInput(operation + "inputfrom", operation + "inputto", operation + "inputoutput");
		rulesPage.addOutput(operation + "outputfrom", operation + "outputto");
		rulesPage.addFault(operation + "faultfrom", operation + "faultto");
		rulesPage.ok();

		editor.save();

		assertEquals(type, editor.xpath(xpath + "/operations/operation[@name='" + operation + "']/@type"));
		assertEquals(operation + "globalfrom",
				editor.xpath(xpath + "/operations/operation[@name='" + operation + "']/globals/global/@from"));
		assertEquals(operation + "globalto",
				editor.xpath(xpath + "/operations/operation[@name='" + operation + "']/globals/global/@to"));
		assertEquals(operation + "inputfrom",
				editor.xpath(xpath + "/operations/operation[@name='" + operation + "']/inputs/input/@from"));
		assertEquals(operation + "inputto",
				editor.xpath(xpath + "/operations/operation[@name='" + operation + "']/inputs/input/@to"));
		assertEquals(operation + "inputoutput",
				editor.xpath(xpath + "/operations/operation[@name='" + operation + "']/inputs/input/@output"));
		assertEquals(operation + "outputfrom",
				editor.xpath(xpath + "/operations/operation[@name='" + operation + "']/outputs/output/@from"));
		assertEquals(operation + "outputto",
				editor.xpath(xpath + "/operations/operation[@name='" + operation + "']/outputs/output/@to"));
		assertEquals(operation + "faultfrom",
				editor.xpath(xpath + "/operations/operation[@name='" + operation + "']/faults/fault/@from"));
		assertEquals(operation + "faultto",
				editor.xpath(xpath + "/operations/operation[@name='" + operation + "']/faults/fault/@to"));
	}

	private void checkBPMAdvancedProperties(String tooltip) throws Exception {
		checkAdvancedProperties(XPATH_BPM, tooltip);
	}

	private void checkDroolsAdvancedProperties(String tooltip) throws Exception {
		checkAdvancedProperties(XPATH_RULES, tooltip);
	}

	private void checkAdvancedProperties(String xpath, String tooltip) throws Exception {
		SwitchYardEditor editor = new SwitchYardEditor();

		ImplementationKnowledgePage knowledgePage = new SwitchYardComponent(tooltip).showProperties()
				.selectRulesImplementation();
		knowledgePage.selectAdvanced();
		knowledgePage.addChannel("myChannel", "myOperation", "myReference", "SwitchYardServiceChannel");
		knowledgePage.addListener("AgendaStats");
		knowledgePage.addLogger("CONSOLE", null, null);
		knowledgePage.addProperty("prop", "val");
		knowledgePage.ok();

		editor.save();

		assertEquals("1", editor.xpath("count(" + xpath + "/channels)"));
		assertEquals("myChannel", editor.xpath(xpath + "/channels/channel/@name"));
		assertEquals("myOperation", editor.xpath(xpath + "/channels/channel/@operation"));
		assertEquals("myReference", editor.xpath(xpath + "/channels/channel/@reference"));
		assertEquals("org.switchyard.component.common.knowledge.service.SwitchYardServiceChannel",
				editor.xpath(xpath + "/channels/channel/@class"));
		assertEquals("1", editor.xpath("count(" + xpath + "/listeners)"));
		assertEquals("org.drools.core.management.KieSessionMonitoringImpl$AgendaStats",
				editor.xpath(xpath + "/listeners/listener/@class"));
		assertEquals("1", editor.xpath("count(" + xpath + "/loggers)"));
		assertEquals("CONSOLE", editor.xpath(xpath + "/loggers/logger/@type"));
		assertEquals("1", editor.xpath("count(" + xpath + "/properties)"));
		assertEquals("prop", editor.xpath(xpath + "/properties/property/@name"));
		assertEquals("val", editor.xpath(xpath + "/properties/property/@value"));
	}

	private void removeClassAfterTest(String className) {
		removeClassAfterTest(PROJECT_PACKAGE, className);
	}

	private void removeClassAfterTest(String packageName, String className) {
		classesToDelete.add(new String[] { packageName, className });
	}

	private void removeResourceAfterTest(String... resource) {
		resourcesToDelete.add(resource);
	}

	private void removeComponentAfterTest(String component) {
		componentsToDelete.add(component);
	}

}
