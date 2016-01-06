package org.jboss.tools.bpmn2.ui.bot.test.jbpm;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.kie.api.event.process.ProcessCompletedEvent;
import org.kie.api.event.process.ProcessEventListener;
import org.kie.api.event.process.ProcessNodeLeftEvent;
import org.kie.api.event.process.ProcessNodeTriggeredEvent;
import org.kie.api.event.process.ProcessStartedEvent;
import org.kie.api.event.process.ProcessVariableChangedEvent;
import org.kie.api.runtime.process.NodeInstance;

public class ParallelGatewaysListener implements ProcessEventListener {

	private String diverging;
	private String converging;

	private NodeInstance divergingGateway;
	private NodeInstance convergingGateway;

	public ParallelGatewaysListener(String diverging, String converging) {
		this.diverging = diverging;
		this.converging = converging;
		divergingGateway = null;
		convergingGateway = null;
	}

	@Override
	public void beforeProcessStarted(ProcessStartedEvent event) {
		JbpmAssertions.assertNumOfIncommingConnections(event.getProcessInstance(), diverging, 1);
		JbpmAssertions.assertNumOfOutgoingConnections(event.getProcessInstance(), diverging, 2);
		JbpmAssertions.assertNumOfIncommingConnections(event.getProcessInstance(), converging, 2);
		JbpmAssertions.assertNumOfOutgoingConnections(event.getProcessInstance(), converging, 1);
	}

	@Override
	public void afterProcessStarted(ProcessStartedEvent event) {
		// TODO Auto-generated method stub

	}

	@Override
	public void beforeProcessCompleted(ProcessCompletedEvent event) {

	}

	@Override
	public void afterProcessCompleted(ProcessCompletedEvent event) {

	}

	@Override
	public void beforeNodeTriggered(ProcessNodeTriggeredEvent event) {
		NodeInstance node = event.getNodeInstance();
		String nodeName = node.getNodeName();

		if (nodeName.compareTo(diverging) == 0) {
			assertNull(convergingGateway);
			divergingGateway = node;
		}

		if (nodeName.compareTo(converging) == 0) {
			assertNotNull(divergingGateway);
			convergingGateway = node;
		}

	}

	@Override
	public void afterNodeTriggered(ProcessNodeTriggeredEvent event) {
		// TODO Auto-generated method stub

	}

	@Override
	public void beforeNodeLeft(ProcessNodeLeftEvent event) {
		// TODO Auto-generated method stub
	}

	@Override
	public void afterNodeLeft(ProcessNodeLeftEvent event) {
		// TODO Auto-generated method stub

	}

	@Override
	public void beforeVariableChanged(ProcessVariableChangedEvent event) {
		// TODO Auto-generated method stub

	}

	@Override
	public void afterVariableChanged(ProcessVariableChangedEvent event) {
		// TODO Auto-generated method stub

	}
}