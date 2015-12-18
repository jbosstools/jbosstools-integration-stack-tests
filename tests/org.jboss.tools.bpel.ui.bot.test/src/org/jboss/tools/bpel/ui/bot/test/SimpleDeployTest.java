package org.jboss.tools.bpel.ui.bot.test;

import org.jboss.reddeer.junit.requirement.inject.InjectRequirement;
import org.jboss.reddeer.junit.runner.RedDeerSuite;
import org.jboss.reddeer.requirements.cleanworkspace.CleanWorkspaceRequirement.CleanWorkspace;
import org.jboss.reddeer.requirements.openperspective.OpenPerspectiveRequirement.OpenPerspective;
import org.jboss.reddeer.requirements.server.ServerReqState;
import org.jboss.reddeer.workbench.impl.shell.WorkbenchShell;
import org.jboss.reddeer.common.wait.AbstractWait;
import org.jboss.reddeer.common.wait.TimePeriod;
import org.jboss.tools.bpel.reddeer.activity.Assign;
import org.jboss.tools.bpel.reddeer.activity.Empty;
import org.jboss.tools.bpel.reddeer.activity.Receive;
import org.jboss.tools.bpel.reddeer.activity.Reply;
import org.jboss.tools.bpel.reddeer.activity.Sequence;
import org.jboss.tools.bpel.reddeer.editor.BpelDescriptorEditor;
import org.jboss.tools.bpel.reddeer.perspective.BPELPerspective;
import org.jboss.tools.bpel.reddeer.server.ServerDeployment;
import org.jboss.tools.bpel.reddeer.wizard.NewDescriptorWizard;
import org.jboss.tools.bpel.reddeer.wizard.NewProcessWizard;
import org.jboss.tools.bpel.reddeer.wizard.NewProjectWizard;
import org.jboss.tools.bpel.ui.bot.test.util.SoapClient;
import org.jboss.tools.runtime.reddeer.requirement.ServerReqType;
import org.jboss.tools.runtime.reddeer.requirement.ServerRequirement;
import org.jboss.tools.runtime.reddeer.requirement.ServerRequirement.Server;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * 
 * @author apodhrad
 * 
 */
@CleanWorkspace
@OpenPerspective(BPELPerspective.class)
@RunWith(RedDeerSuite.class)
@Server(type = ServerReqType.ANY, state = ServerReqState.RUNNING)
public class SimpleDeployTest {

	private static final String WSDL_URL = "http://localhost:8080/deployHello?wsdl";

	@InjectRequirement
	private ServerRequirement serverRequirement;

	@Test
	public void simpleDeployTest() throws Exception {
		new WorkbenchShell().maximize();

		String projectName = "deployTest";
		String processName = "deployHello";

		new NewProjectWizard(projectName).execute();
		new NewProcessWizard(projectName, processName).setSyncTemplate().execute();

		// call validate when implemented
		new Sequence("main");
		new Receive("receiveInput");
		new Empty("FIX_ME-Add_Business_Logic_Here");
		new Reply("replyOutput");

		// change Empty to Assign
		Assign assign = new Empty("FIX_ME-Add_Business_Logic_Here").toAssign();
		assign.setName("addHello");
		assign.addExpToVar("concat('Hello ', $input.payload/tns:input)", new String[] {
				"output : deployHelloResponseMessage", "payload : deployHelloResponse", "result : string" });

		// create descriptor
		new NewDescriptorWizard(projectName).execute();
		new BpelDescriptorEditor().setAssociatedPort("deployHelloPort");

		// deploy project
		String serverName = serverRequirement.getConfig().getName();
		ServerDeployment server = new ServerDeployment(serverName);
		server.deployProject(projectName);
		AbstractWait.sleep(TimePeriod.NORMAL);

		// test the deployed project
		SoapClient.testResponses(WSDL_URL, "Deploy_Hello");
	}

}
