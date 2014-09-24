package org.jboss.tools.bpmn2.ui.bot.test;

import org.jbpm.test.JbpmJUnitBaseTestCase;
import org.kie.api.definition.process.Node;
import org.jbpm.workflow.instance.impl.WorkflowProcessInstanceImpl;

import org.kie.api.runtime.process.ProcessInstance;

public class JbpmAssertionsForBPMN2 extends JbpmJUnitBaseTestCase {

	@Override
	public void assertNumOfIncommingConnections(ProcessInstance process, String nodeName, int num) {
		assertNodeExists(process, nodeName);
		WorkflowProcessInstanceImpl instance = (WorkflowProcessInstanceImpl) process;
		for (Node node : instance.getNodeContainer().getNodes()) {
			if (node.getName().equals(nodeName)) {
				if (node.getIncomingConnections().get("DROOLS_DEFAULT").size() != num) {
					fail("Expected incomming connections: " + num + " - found " + node.getIncomingConnections().get("DROOLS_DEFAULT").size());
				} else {
					break;
				}
			}
		}
	}
	
	@Override
	public void assertNumOfOutgoingConnections(ProcessInstance process, String nodeName, int num) {
		assertNodeExists(process, nodeName);
		WorkflowProcessInstanceImpl instance = (WorkflowProcessInstanceImpl) process;
		for (Node node : instance.getNodeContainer().getNodes()) {
			if (node.getName().equals(nodeName)) {
				if (node.getOutgoingConnections().get("DROOLS_DEFAULT").size() != num) {
					fail("Expected outgoing connections: " + num + " - found " + node.getOutgoingConnections().get("DROOLS_DEFAULT").size());
				} else {
					break;
				}
			}
		}
	}
	
	
	
}
