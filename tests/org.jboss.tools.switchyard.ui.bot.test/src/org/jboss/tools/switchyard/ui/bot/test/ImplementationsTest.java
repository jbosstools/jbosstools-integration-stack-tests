package org.jboss.tools.switchyard.ui.bot.test;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.jboss.reddeer.junit.requirement.inject.InjectRequirement;
import org.jboss.reddeer.junit.runner.RedDeerSuite;
import org.jboss.reddeer.swt.impl.shell.WorkbenchShell;
import org.jboss.tools.switchyard.reddeer.component.SwitchYardComponent;
import org.jboss.tools.switchyard.reddeer.editor.SwitchYardEditor;
import org.jboss.tools.switchyard.reddeer.project.SwitchYardProject;
import org.jboss.tools.switchyard.reddeer.requirement.SwitchYardRequirement;
import org.jboss.tools.switchyard.reddeer.requirement.SwitchYardRequirement.SwitchYard;
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
public class ImplementationsTest {

	public static final String PROJECT_NAME = "sy_implementations";
	public static final String PROJECT_PACKAGE = "com.example.switchyard." + PROJECT_NAME;
	public static final String SERVICE__NAME = "Hello";
	public static final String SERVICE_IMPL_NAME = "HelloImpl";

	@InjectRequirement
	private static SwitchYardRequirement switchYardRequirement;

	private List<String[]> classesToDelete;
	private List<String[]> resourcesToDelete;
	private List<String> componentsToDelete;

	@BeforeClass
	public static void maximizeWorkbench() {
		new WorkbenchShell().maximize();
	}

	@BeforeClass
	public static void createProject() {
		switchYardRequirement.project(PROJECT_NAME).implAll().create();

		new SwitchYardProject(PROJECT_NAME).getProjectItem("src/main/resources").select();
		new ImportFileWizard().importFile("resources/" + PROJECT_NAME);

		System.out.println();
	}

	@Before
	public void initClassToDelete() {
		classesToDelete = new ArrayList<String[]>();
		resourcesToDelete = new ArrayList<String[]>();
		componentsToDelete = new ArrayList<String>();
	}

	@After
	public void deleteCreatedResousources() {
		for (String component : componentsToDelete) {
			new SwitchYardComponent(component).delete();
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
		assertEquals("1", editor.xpath("count(/switchyard/composite/component/implementation.bpm)"));
		assertEquals("1", editor.xpath("count(/switchyard/composite/component/implementation.bpm/manifest)"));
		assertEquals("1", editor.xpath("count(/switchyard/composite/component/implementation.bpm/manifest/resources)"));
		assertEquals("1",
				editor.xpath("count(/switchyard/composite/component/implementation.bpm/manifest/resources/resource)"));
		assertEquals(
				"NewBPMNImpl.bpmn",
				editor.xpath("/switchyard/composite/component/implementation.bpm/manifest/resources/resource/@location"));
		assertEquals("BPMN2",
				editor.xpath("/switchyard/composite/component/implementation.bpm/manifest/resources/resource/@type"));
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
		assertEquals("1", editor.xpath("count(/switchyard/composite/component/implementation.bpm)"));
		assertEquals("1", editor.xpath("count(/switchyard/composite/component/implementation.bpm/manifest)"));
		assertEquals("1", editor.xpath("count(/switchyard/composite/component/implementation.bpm/manifest/resources)"));
		assertEquals("1",
				editor.xpath("count(/switchyard/composite/component/implementation.bpm/manifest/resources/resource)"));
		assertEquals(
				"ExistingBPMNImpl.bpmn",
				editor.xpath("/switchyard/composite/component/implementation.bpm/manifest/resources/resource/@location"));
		assertEquals("BPMN2",
				editor.xpath("/switchyard/composite/component/implementation.bpm/manifest/resources/resource/@type"));
	}

	@Test
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
		assertEquals("1", editor.xpath("count(/switchyard/composite/component/implementation.rules)"));
		assertEquals("1", editor.xpath("count(/switchyard/composite/component/implementation.rules/manifest)"));
		assertEquals("1",
				editor.xpath("count(/switchyard/composite/component/implementation.rules/manifest/resources)"));
		assertEquals("1",
				editor.xpath("count(/switchyard/composite/component/implementation.rules/manifest/resources/resource)"));
		assertEquals(
				"NewDroolsImpl.drl",
				editor.xpath("/switchyard/composite/component/implementation.rules/manifest/resources/resource/@location"));
		assertEquals("DRL",
				editor.xpath("/switchyard/composite/component/implementation.rules/manifest/resources/resource/@type"));
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
		assertEquals("1", editor.xpath("count(/switchyard/composite/component/implementation.rules)"));
		assertEquals("1", editor.xpath("count(/switchyard/composite/component/implementation.rules/manifest)"));
		assertEquals("1",
				editor.xpath("count(/switchyard/composite/component/implementation.rules/manifest/resources)"));
		assertEquals("1",
				editor.xpath("count(/switchyard/composite/component/implementation.rules/manifest/resources/resource)"));
		assertEquals(
				"ExistingDroolsImpl.drl",
				editor.xpath("/switchyard/composite/component/implementation.rules/manifest/resources/resource/@location"));
		assertEquals("DRL",
				editor.xpath("/switchyard/composite/component/implementation.rules/manifest/resources/resource/@type"));
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
