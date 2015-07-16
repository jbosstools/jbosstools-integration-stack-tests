package org.jboss.tools.switchyard.ui.bot.test;

import static org.junit.Assert.assertEquals;

import org.jboss.reddeer.junit.requirement.inject.InjectRequirement;
import org.jboss.reddeer.junit.runner.RedDeerSuite;
import org.jboss.reddeer.requirements.server.ServerReqState;
import org.jboss.reddeer.workbench.impl.editor.TextEditor;
import org.jboss.tools.runtime.reddeer.ServerBase;
import org.jboss.tools.runtime.reddeer.requirement.ServerReqType;
import org.jboss.tools.runtime.reddeer.requirement.ServerRequirement.Server;
import org.jboss.tools.switchyard.reddeer.binding.HTTPBindingPage;
import org.jboss.tools.switchyard.reddeer.component.Service;
import org.jboss.tools.switchyard.reddeer.component.SwitchYardComponent;
import org.jboss.tools.switchyard.reddeer.editor.SwitchYardEditor;
import org.jboss.tools.switchyard.reddeer.requirement.SwitchYardRequirement;
import org.jboss.tools.switchyard.reddeer.requirement.SwitchYardRequirement.SwitchYard;
import org.jboss.tools.switchyard.ui.bot.test.util.HttpClient;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * 
 * @author apodhrad
 *
 */
@SwitchYard(server = @Server(type = ServerReqType.ANY, state = ServerReqState.RUNNING))
@RunWith(RedDeerSuite.class)
public class DeploymentTest {

	public static final String PROJECT_NAME = "deploy";

	@InjectRequirement
	private SwitchYardRequirement switchyardRequirement;

	@Test
	public void deployTest() throws Exception {
		switchyardRequirement.project(PROJECT_NAME).impl("Bean").binding("HTTP").create();

		new SwitchYardEditor().addBeanImplementation().createJavaInterface("Hello").finish();

		new SwitchYardComponent("Hello").doubleClick();
		TextEditor helloEditor = new TextEditor("Hello.java");
		helloEditor.setText("package com.example.switchyard.deploy;\n" + "public interface Hello {\n"
				+ "\tString sayHello(String name);\n}");
		helloEditor.save();
		helloEditor.close();

		new SwitchYardComponent("HelloBean").doubleClick();
		TextEditor helloBeanEditor = new TextEditor("HelloBean.java");
		helloBeanEditor.setText("package com.example.switchyard.deploy;\n"
				+ "import org.switchyard.component.bean.Service;\n" + "@Service(Hello.class)\n"
				+ "public class HelloBean implements Hello {\n" + "\t@Override\n"
				+ "\tpublic String sayHello(String name) {\n" + "\t\treturn \"Hello \" + name;\n\t}\n}");
		helloBeanEditor.save();
		helloBeanEditor.close();

		new Service("Hello").promoteService().activate().setServiceName("HelloService").finish();

		new Service("HelloService").addBinding("HTTP");
		HTTPBindingPage soapWizard = new HTTPBindingPage();
		soapWizard.setName("soap-binding");
		soapWizard.getContextPath().setText("hello");
		soapWizard.finish();

		new SwitchYardEditor().save();

		ServerBase server = switchyardRequirement.getConfig().getServerBase();
		server.deployProject(PROJECT_NAME);
		String response = new HttpClient(server.getUrl("hello")).post("World");
		assertEquals("Hello World", response);
	}
}
