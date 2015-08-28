package org.jboss.tools.switchyard.ui.bot.test;

import org.jboss.reddeer.junit.requirement.inject.InjectRequirement;
import org.jboss.reddeer.junit.runner.RedDeerSuite;
import org.jboss.reddeer.requirements.server.ServerReqState;
import org.jboss.reddeer.swt.impl.button.PushButton;
import org.jboss.reddeer.swt.impl.text.DefaultText;
import org.jboss.reddeer.common.wait.WaitWhile;
import org.jboss.tools.runtime.reddeer.ServerBase;
import org.jboss.tools.runtime.reddeer.requirement.ServerReqType;
import org.jboss.tools.runtime.reddeer.requirement.ServerRequirement.Server;
import org.jboss.tools.switchyard.reddeer.binding.SOAPBindingPage;
import org.jboss.tools.switchyard.reddeer.component.Service;
import org.jboss.tools.switchyard.reddeer.component.SwitchYardComponent;
import org.jboss.tools.switchyard.reddeer.condition.ConsoleHasChanged;
import org.jboss.tools.switchyard.reddeer.editor.SwitchYardEditor;
import org.jboss.tools.switchyard.reddeer.project.SwitchYardProject;
import org.jboss.tools.switchyard.reddeer.requirement.SwitchYardRequirement;
import org.jboss.tools.switchyard.reddeer.requirement.SwitchYardRequirement.SwitchYard;
import org.jboss.tools.switchyard.reddeer.wizard.ImportFileWizard;
import org.jboss.tools.switchyard.reddeer.wizard.PromoteServiceWizard;
import org.jboss.tools.switchyard.ui.bot.test.util.SoapClient;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * Creation test from existing BPEL process
 * 
 * @author apodhrad
 * 
 */
@SwitchYard(server = @Server(type = ServerReqType.ANY, state = ServerReqState.RUNNING))
@RunWith(RedDeerSuite.class)
public class BottomUpBPELTest {

	public static final String PROJECT = "bpel_project";

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
	public void bottomUpBPELtest() throws Exception {
		switchyardRequirement.project(PROJECT).impl("BPEL").binding("SOAP").create();
		new SwitchYardProject(PROJECT).getProjectItem("src/main/resources").select();
		new ImportFileWizard().importFile("resources/bpel", "SayHello.bpel");
		new ImportFileWizard().importFile("resources/wsdl", "SayHelloArtifacts.wsdl");

		// There is no way how to create deployment descriptor, only manually
		new ImportFileWizard().importFile("resources/bpel", "deploy.xml");

		new SwitchYardEditor().addComponent();
		new SwitchYardComponent("Component").select();
		new SwitchYardEditor().addBPELImplementation(new SwitchYardComponent("Component"));

		new PushButton("Browse...").click();
		new DefaultText(0).setText("SayHello.bpel");
		new PushButton("OK").click();
		new PushButton("Finish").click();

		new SwitchYardComponent("Component").getContextButton("Service").click();
		new PushButton("Browse...").click();
		new DefaultText(0).setText("SayHelloArtifacts.wsdl");
		new PushButton("OK").click();
		new PushButton("Finish").click();

		PromoteServiceWizard wizard = new Service("SayHello").promoteService();
		wizard.activate().setServiceName("SayHelloService").finish();

		new Service("SayHelloService").addBinding("SOAP");
		SOAPBindingPage soapWizard = new SOAPBindingPage();
		soapWizard.getContextPath().setText(PROJECT);
		soapWizard.finish();

		new SwitchYardEditor().save();

		/* Test SOAP Response */
		ServerBase server = switchyardRequirement.getConfig().getServerBase();
		server.deployProject(PROJECT);
		SoapClient.testResponses(server.getUrl(PROJECT + "/SayHelloService?wsdl"), "SayHello");
		new WaitWhile(new ConsoleHasChanged());
	}
}
