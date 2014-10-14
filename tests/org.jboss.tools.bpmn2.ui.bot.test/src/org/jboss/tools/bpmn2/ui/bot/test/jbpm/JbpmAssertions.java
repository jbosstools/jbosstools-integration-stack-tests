package org.jboss.tools.bpmn2.ui.bot.test.jbpm;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

import java.util.ArrayList;
import java.util.List;

import org.kie.api.definition.process.Node;
import org.jbpm.workflow.instance.impl.WorkflowProcessInstanceImpl;
import org.kie.api.runtime.KieSession;
import org.kie.api.runtime.process.ProcessInstance;

/**
 * Inspired by org.jbpm.test.JbpmJUnitBaseTestCase
 * @author jomarko
 *
 */
public class JbpmAssertions {

	public static void assertProcessInstanceCompleted(ProcessInstance process, KieSession ksession) {
		assertEquals(process.getProcessName() + " wasn't completed.", ProcessInstance.STATE_COMPLETED, process.getState());
		assertNull(process.getProcessName() + " wasn't completed.", ksession.getProcessInstance(process.getId()));
    }
	
	public static void assertProcessInstanceAborted(ProcessInstance process, KieSession ksession) {
		assertEquals(process.getProcessName() + " wasn't aborted.", ProcessInstance.STATE_ABORTED, process.getState());
		assertNull(process.getProcessName() + " wasn't aborted.", ksession.getProcessInstance(process.getId()));
    }

	public static void assertProcessInstanceActive(ProcessInstance process, KieSession ksession) {
		assertEquals(process.getProcessName() + " wasn't active.", ProcessInstance.STATE_ACTIVE, process.getState());
		assertNotNull(process.getProcessName() + " wasn't active.", ksession.getProcessInstance(process.getId()));
    }
	
	public static void assertNumOfIncommingConnections(ProcessInstance process, String nodeName, int num) {
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
	
	public static void assertNumOfOutgoingConnections(ProcessInstance process, String nodeName, int num) {
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
	
	public static void assertNodeExists(ProcessInstance process, String... nodeNames) {
        WorkflowProcessInstanceImpl instance = (WorkflowProcessInstanceImpl) process;
        List<String> names = new ArrayList<String>();
        for (String nodeName : nodeNames) {
            names.add(nodeName);
        }

        for (Node node : instance.getNodeContainer().getNodes()) {
            if (names.contains(node.getName())) {
                names.remove(node.getName());
            }
        }

        if (!names.isEmpty()) {
            String s = names.get(0);
            for (int i = 1; i < names.size(); i++) {
                s += ", " + names.get(i);
            }
            fail("Node(s) do not exist: " + s);
        }
    }
	
}
