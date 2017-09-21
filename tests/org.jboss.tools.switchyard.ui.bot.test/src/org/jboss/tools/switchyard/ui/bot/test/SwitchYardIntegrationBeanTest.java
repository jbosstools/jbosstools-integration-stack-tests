package org.jboss.tools.switchyard.ui.bot.test;

import static org.jboss.tools.switchyard.reddeer.binding.OperationOptionsPage.OPERATION_NAME;
import static org.junit.Assert.assertEquals;

import org.eclipse.reddeer.common.logging.Logger;
import org.eclipse.reddeer.eclipse.core.resources.ProjectItem;
import org.eclipse.reddeer.eclipse.ui.navigator.resources.ProjectExplorer;
import org.eclipse.reddeer.junit.requirement.inject.InjectRequirement;
import org.eclipse.reddeer.junit.runner.RedDeerSuite;
import org.eclipse.reddeer.workbench.impl.editor.TextEditor;
import org.eclipse.reddeer.workbench.impl.shell.WorkbenchShell;
import org.jboss.tools.switchyard.reddeer.binding.HTTPBindingPage;
import org.jboss.tools.switchyard.reddeer.component.Service;
import org.jboss.tools.switchyard.reddeer.component.SwitchYardComponent;
import org.jboss.tools.switchyard.reddeer.editor.SwitchYardEditor;
import org.jboss.tools.switchyard.reddeer.project.ProjectItemExt;
import org.jboss.tools.switchyard.reddeer.requirement.SwitchYardRequirement;
import org.jboss.tools.switchyard.reddeer.requirement.SwitchYardRequirement.SwitchYard;
import org.jboss.tools.switchyard.reddeer.view.JUnitView;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

@SwitchYard
@RunWith(RedDeerSuite.class)
public class SwitchYardIntegrationBeanTest {

	protected final Logger log = Logger.getLogger(this.getClass());

	private static final String PROJECT = "helloworld";
	private static final String PACKAGE = "com.example.switchyard.helloworld";

	@InjectRequirement
	private SwitchYardRequirement switchyardRequirement;

	@Before
	@After
	public void closeSwitchyardFile() {
		try {
			new SwitchYardEditor().saveAndClose();
		} catch (Exception ex) {
			// it is ok, we just try to close switchyard.xml if it is open
		}
	}

	@Test
	public void switchyardBeanIntegrationTest() {
		switchyardRequirement.project(PROJECT).impl("Bean").binding("HTTP").create();

		new SwitchYardEditor().addBeanImplementation().createJavaInterface("Hello").finish();
		new SwitchYardComponent("Hello").doubleClick();
		TextEditor textEditor = new TextEditor("Hello.java");
		textEditor.setText("package com.example.switchyard.helloworld;\n\n" + "public interface Hello {\n"
				+ "\tString sayHello(String name);\n" + "}");
		textEditor.save();
		textEditor.close();

		new SwitchYardEditor().save();

		new SwitchYardComponent("HelloBean").doubleClick();
		textEditor = new TextEditor("HelloBean.java");
		textEditor.setText(
				"package com.example.switchyard.helloworld;\n\n" + "import org.switchyard.component.bean.Service;\n\n"
						+ "@Service(Hello.class)\n" + "public class HelloBean implements Hello {\n" + "\t@Override\n"
						+ "\tpublic String sayHello(String name) {\n" + "\t\treturn \"Hello \" + name;\n" + "\t}\n}");
		textEditor.save();
		textEditor.close();

		new SwitchYardEditor().save();

		new Service("Hello").promoteService().activate().setServiceName("HelloService").finish();
		new Service("HelloService").addBinding("HTTP");
		HTTPBindingPage wizard = new HTTPBindingPage();
		wizard.setName("http-binding");
		wizard.getContextPath().setText("hello");
		wizard.setOperationSelector(OPERATION_NAME, "sayHello");
		wizard.finish();

		new SwitchYardEditor().save();

		new Service("Hello").createNewServiceTestClass("HTTP Mix-in");
		textEditor = new TextEditor("HelloTest.java");
		textEditor.setText("package com.example.switchyard.helloworld;\n\n" + "import org.junit.Assert;\n"
				+ "import org.junit.Test;\n" + "import org.junit.runner.RunWith;\n"
				+ "import org.switchyard.component.test.mixins.cdi.CDIMixIn;\n"
				+ "import org.switchyard.component.test.mixins.http.HTTPMixIn;\n"
				+ "import org.switchyard.test.BeforeDeploy;\n" + "import org.switchyard.test.SwitchYardRunner;\n"
				+ "import org.switchyard.test.SwitchYardTestCaseConfig;\n\n" + "@RunWith(SwitchYardRunner.class)\n"
				+ "@SwitchYardTestCaseConfig(config = SwitchYardTestCaseConfig.SWITCHYARD_XML, mixins = { CDIMixIn.class, HTTPMixIn.class })\n"
				+ "\tpublic class HelloTest {\n\n" + "\tprivate HTTPMixIn httpMixIn;\n\n" + "\t@BeforeDeploy\n"
				+ "\tpublic void setProperties() {\n"
				+ "\t\tSystem.setProperty(\"org.switchyard.component.http.standalone.port\", \"8123\");\n}"
				+ "\t@Test\n" + "\tpublic void testSayHello() throws Exception {\n"
				+ "\t\tString response = httpMixIn.postString(\"http://localhost:8123/hello\", \"World\");\n"
				+ "\t\tAssert.assertEquals(\"Hello World\", response);\n" + "\t}\n\n}");
		textEditor.save();
		textEditor.close();

		new SwitchYardEditor().save();
		new SwitchYardEditor().save();

		new WorkbenchShell().setFocus();
		ProjectExplorer projectExplorer = new ProjectExplorer();
		projectExplorer.open();
		ProjectItem item = projectExplorer.getProject(PROJECT).getProjectItem("src/test/java", PACKAGE,
				"HelloTest.java");
		new ProjectItemExt(item).runAsJUnitTest();

		JUnitView jUnitView = new JUnitView();
		jUnitView.open();
		assertEquals("1/1", new JUnitView().getRunStatus());
		assertEquals(0, new JUnitView().getNumberOfErrors());
		assertEquals(0, new JUnitView().getNumberOfFailures());
	}
}
