package org.jboss.tools.bpmn2.ui.bot.test.jbpm;

import static org.junit.Assert.assertEquals;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import org.kie.api.event.process.ProcessCompletedEvent;
import org.kie.api.event.process.ProcessEventListener;
import org.kie.api.event.process.ProcessNodeLeftEvent;
import org.kie.api.event.process.ProcessNodeTriggeredEvent;
import org.kie.api.event.process.ProcessStartedEvent;
import org.kie.api.event.process.ProcessVariableChangedEvent;
import org.kie.api.runtime.process.ProcessInstance;

public class TriggeredNodesListener implements ProcessEventListener {

	private Set<String> nodesToBeTriggered;
	private Set<String> nodesNotToBeTriggered;
	private int toNotBeTriggeredCount;
	private int expectedFinalProcessState = -1;
	private boolean wasCompletitionReached = false;
	
	public TriggeredNodesListener(Collection<String> toBeTriggered, Collection<String> notToBeTriggered) {
		if(toBeTriggered != null) {
			nodesToBeTriggered = new HashSet<String>(toBeTriggered);
		} else {
			nodesToBeTriggered = new HashSet<String>();
		}
		
		if(notToBeTriggered != null) {
			nodesNotToBeTriggered = new HashSet<String>(notToBeTriggered);
		} else {
			nodesNotToBeTriggered = new HashSet<String>();
		}
		
		toNotBeTriggeredCount = nodesNotToBeTriggered.size();
	}
	
	@Override
	public void beforeProcessStarted(ProcessStartedEvent event) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void afterProcessStarted(ProcessStartedEvent event) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void beforeProcessCompleted(ProcessCompletedEvent event) {
		StringBuilder errorMessage = new StringBuilder("There were not triggered nodes: ");
		for(String s : nodesToBeTriggered) {
			errorMessage.append(s).append(", ");
		}
		
		assertEquals(errorMessage.toString(), 0, nodesToBeTriggered.size());
		
		errorMessage = new StringBuilder("These nodes were not executed: ");
		for(String s : nodesNotToBeTriggered) {
			errorMessage.append(s).append(", ");
		}
		errorMessage.append(" but it is less than excpeted");
		
		assertEquals(errorMessage.toString(), toNotBeTriggeredCount, nodesNotToBeTriggered.size());
		
		if(expectedFinalProcessState != -1) {
			switch (expectedFinalProcessState) {
			case ProcessInstance.STATE_ABORTED:
				assertEquals("Process was not aborted", ProcessInstance.STATE_ABORTED, event.getProcessInstance().getState());
				break;
			case ProcessInstance.STATE_COMPLETED:
				assertEquals("Process was not completed", ProcessInstance.STATE_COMPLETED, event.getProcessInstance().getState());
				break;

			case ProcessInstance.STATE_ACTIVE:
				assertEquals("Process was not active", ProcessInstance.STATE_ACTIVE, event.getProcessInstance().getState());
				break;
			}			
			wasCompletitionReached = true;
		}
		
	}

	@Override
	public void afterProcessCompleted(ProcessCompletedEvent event) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void beforeNodeTriggered(ProcessNodeTriggeredEvent event) {
		String nodeName = event.getNodeInstance().getNodeName();
		nodesToBeTriggered.remove(nodeName);
		nodesNotToBeTriggered.remove(nodeName);
	}

	@Override
	public void afterNodeTriggered(ProcessNodeTriggeredEvent event) {
		String nodeName = event.getNodeInstance().getNodeName();
		nodesToBeTriggered.remove(nodeName);
		nodesNotToBeTriggered.remove(nodeName);
	}

	@Override
	public void beforeNodeLeft(ProcessNodeLeftEvent event) {
		String nodeName = event.getNodeInstance().getNodeName();
		nodesToBeTriggered.remove(nodeName);
		nodesNotToBeTriggered.remove(nodeName);
	}

	@Override
	public void afterNodeLeft(ProcessNodeLeftEvent event) {
		String nodeName = event.getNodeInstance().getNodeName();
		nodesToBeTriggered.remove(nodeName);
		nodesNotToBeTriggered.remove(nodeName);
	}

	@Override
	public void beforeVariableChanged(ProcessVariableChangedEvent event) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void afterVariableChanged(ProcessVariableChangedEvent event) {
		// TODO Auto-generated method stub
		
	}

	public int getExpectedFinalProcessState() {
		return expectedFinalProcessState;
	}

	public void setExpectedFinalProcessState(int expectedFinalProcessState) {
		this.expectedFinalProcessState = expectedFinalProcessState;
	}

	public boolean wasCompletitionReached() {
		return wasCompletitionReached;
	}
}
