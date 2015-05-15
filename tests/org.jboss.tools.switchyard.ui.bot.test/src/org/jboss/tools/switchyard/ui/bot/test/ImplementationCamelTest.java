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
import org.jboss.tools.switchyard.reddeer.wizard.BPMServiceWizard;
import org.jboss.tools.switchyard.reddeer.wizard.BeanServiceWizard;
import org.jboss.tools.switchyard.reddeer.wizard.CamelJavaWizard;
import org.jboss.tools.switchyard.reddeer.wizard.CamelXMLWizard;
import org.jboss.tools.switchyard.reddeer.wizard.DroolsServiceWizard;
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
public class ImplementationCamelTest {

	public static final String PROJECT_NAME = "sy_camel_project";
	public static final String PROJECT_PACKAGE = "com.example.switchyard." + PROJECT_NAME;
	public static final String SERVICE__NAME = "Hello";
	public static final String SERVICE_IMPL_NAME = "HelloImpl";

	@InjectRequirement
	private static SwitchYardRequirement switchYardRequirement;

	private List<String[]> classesToDelete;
	private List<String[]> resourcesToDelete;

	@BeforeClass
	public static void maximizeWorkbench() {
		new WorkbenchShell().maximize();
	}

	@BeforeClass
	public static void createProject() {
		switchYardRequirement.project(PROJECT_NAME).implAll().create();
	}

	@Before
	public void initClassToDelete() {
		classesToDelete = new ArrayList<String[]>();
		resourcesToDelete = new ArrayList<String[]>();
	}

	@After
	public void deleteCreatedResousources() {
		new SwitchYardComponent(SERVICE_IMPL_NAME).delete();
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
		beanWizard.setName(SERVICE_IMPL_NAME);
		beanWizard.finish();

		editor.save();
		removeClassAfterTest(SERVICE_IMPL_NAME + ".java");

		assertEquals("1", editor.xpath("count(/switchyard/composite/component)"));
		assertEquals(SERVICE_IMPL_NAME, editor.xpath("/switchyard/composite/component/@name"));
		assertEquals("1", editor.xpath("count(/switchyard/composite/component/implementation.bean)"));
		assertEquals(PROJECT_PACKAGE + "." + SERVICE_IMPL_NAME,
				editor.xpath("/switchyard/composite/component/implementation.bean/@class"));
	}

	@Test
	public void addCamelJavaImplementationWithNewJavaInterfaceTest() throws Exception {
		SwitchYardEditor editor = new SwitchYardEditor();
		CamelJavaWizard camelJavaWizard = editor.addCamelJavaImplementation();
		camelJavaWizard.createJavaInterface("NewCamelJavaInterface");
		camelJavaWizard.setName(SERVICE_IMPL_NAME);
		camelJavaWizard.finish();

		editor.save();
		removeClassAfterTest(SERVICE_IMPL_NAME + ".java");

		assertEquals("1", editor.xpath("count(/switchyard/composite/component)"));
		assertEquals(SERVICE_IMPL_NAME, editor.xpath("/switchyard/composite/component/@name"));
		assertEquals("1", editor.xpath("count(/switchyard/composite/component/implementation.camel)"));
		assertEquals("1", editor.xpath("count(/switchyard/composite/component/implementation.camel/java)"));
		assertEquals(PROJECT_PACKAGE + "." + SERVICE_IMPL_NAME,
				editor.xpath("/switchyard/composite/component/implementation.camel/java/@class"));
	}

	@Test
	public void addCamelXMLImplementationWithNewJavaInterfaceTest() throws Exception {
		SwitchYardEditor editor = new SwitchYardEditor();
		CamelXMLWizard camelXMLWizard = editor.addCamelXMLImplementation();
		camelXMLWizard.createJavaInterface("NewCamelXMLInterface");
		camelXMLWizard.setFileName(SERVICE_IMPL_NAME);
		camelXMLWizard.finish();

		editor.save();
		removeResourceAfterTest(SERVICE_IMPL_NAME + ".xml");

		assertEquals("1", editor.xpath("count(/switchyard/composite/component)"));
		assertEquals(SERVICE_IMPL_NAME, editor.xpath("/switchyard/composite/component/@name"));
		assertEquals("1", editor.xpath("count(/switchyard/composite/component/implementation.camel)"));
		assertEquals("1", editor.xpath("count(/switchyard/composite/component/implementation.camel/xml)"));
		assertEquals(SERVICE_IMPL_NAME + ".xml",
				editor.xpath("/switchyard/composite/component/implementation.camel/xml/@path"));
	}

	@Test
	public void addBPELImplementationWithNewWSDLInterfaceTest() throws Exception {
		SwitchYardEditor editor = new SwitchYardEditor();
		BPELServiceWizard bpelWizard = editor.addBPELImplementation();
		bpelWizard.createWSDLInterface("NewBPELInterface");
		bpelWizard.setFileName(SERVICE_IMPL_NAME);
		bpelWizard.finish();

		editor.save();
		removeResourceAfterTest(SERVICE_IMPL_NAME + ".bpel");

		assertEquals("1", editor.xpath("count(/switchyard/composite/component)"));
		assertEquals(SERVICE_IMPL_NAME, editor.xpath("/switchyard/composite/component/@name"));
		assertEquals("1", editor.xpath("count(/switchyard/composite/component/implementation.bpel)"));
		assertEquals("helloImpl:" + SERVICE_IMPL_NAME,
				editor.xpath("/switchyard/composite/component/implementation.bpel/@process"));
	}

	@Test
	public void addBPMNImplementationWithNewJavaInterfaceTest() throws Exception {
		SwitchYardEditor editor = new SwitchYardEditor();
		BPMServiceWizard bpmnWizard = editor.addBPMNImplementation();
		bpmnWizard.createJavaInterface("NewBPMNInterface");
		bpmnWizard.setFileName(SERVICE_IMPL_NAME);
		bpmnWizard.finish();

		editor.save();
		removeResourceAfterTest(SERVICE_IMPL_NAME + ".bpmn");

		assertEquals("1", editor.xpath("count(/switchyard/composite/component)"));
		assertEquals(SERVICE_IMPL_NAME, editor.xpath("/switchyard/composite/component/@name"));
		assertEquals("1", editor.xpath("count(/switchyard/composite/component/implementation.bpm)"));
		assertEquals("1", editor.xpath("count(/switchyard/composite/component/implementation.bpm/manifest)"));
		assertEquals("1", editor.xpath("count(/switchyard/composite/component/implementation.bpm/manifest/resources)"));
		assertEquals("1",
				editor.xpath("count(/switchyard/composite/component/implementation.bpm/manifest/resources/resource)"));
		assertEquals(
				SERVICE_IMPL_NAME + ".bpmn",
				editor.xpath("/switchyard/composite/component/implementation.bpm/manifest/resources/resource/@location"));
		assertEquals("BPMN2",
				editor.xpath("/switchyard/composite/component/implementation.bpm/manifest/resources/resource/@type"));
	}

	@Test
	public void addDroolsImplementationWithNewJavaInterfaceTest() throws Exception {
		SwitchYardEditor editor = new SwitchYardEditor();
		DroolsServiceWizard droolsWizard = editor.addDroolsImplementation();
		droolsWizard.createJavaInterface("NewDroolsInterface");
		droolsWizard.setFileName(SERVICE_IMPL_NAME);
		droolsWizard.finish();

		editor.save();
		removeResourceAfterTest(SERVICE_IMPL_NAME + ".drl");

		assertEquals("1", editor.xpath("count(/switchyard/composite/component)"));
		assertEquals(SERVICE_IMPL_NAME, editor.xpath("/switchyard/composite/component/@name"));
		assertEquals("1", editor.xpath("count(/switchyard/composite/component/implementation.rules)"));
		assertEquals("1", editor.xpath("count(/switchyard/composite/component/implementation.rules/manifest)"));
		assertEquals("1",
				editor.xpath("count(/switchyard/composite/component/implementation.rules/manifest/resources)"));
		assertEquals("1",
				editor.xpath("count(/switchyard/composite/component/implementation.rules/manifest/resources/resource)"));
		assertEquals(
				SERVICE_IMPL_NAME + ".drl",
				editor.xpath("/switchyard/composite/component/implementation.rules/manifest/resources/resource/@location"));
		assertEquals("DRL",
				editor.xpath("/switchyard/composite/component/implementation.rules/manifest/resources/resource/@type"));
	}

	private void removeClassAfterTest(String className) {
		removeClassAfterTest(PROJECT_PACKAGE, className);
	}
	
	private void removeClassAfterTest(String packageName, String className) {
		classesToDelete.add(new String[] {packageName, className});
	}
	
	private void removeResourceAfterTest(String... resource) {
		resourcesToDelete.add(resource);
	}

}
