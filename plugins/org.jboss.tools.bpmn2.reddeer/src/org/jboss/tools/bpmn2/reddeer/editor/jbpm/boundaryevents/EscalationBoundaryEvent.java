package org.jboss.tools.bpmn2.reddeer.editor.jbpm.boundaryevents;

import org.jboss.tools.bpmn2.reddeer.editor.ElementType;
import org.jboss.tools.bpmn2.reddeer.editor.jbpm.Escalation;
import org.jboss.tools.bpmn2.reddeer.editor.jbpm.eventdefinitions.EscalationEventDefinition;
import org.jboss.tools.bpmn2.reddeer.properties.jbpm.EventTab;

/**
 * 
 */
public class EscalationBoundaryEvent extends BoundaryEvent {

	/**
	 * 
	 * @param name
	 */
	public EscalationBoundaryEvent(String name) {
		super(name, ElementType.CONDITIONAL_BOUNDARY_EVENT);
	}

	/**
	 * 
	 * @param escalation
	 */
	public void setEscalation(Escalation escalation, String variableForMapping) {
		properties.getTab("Event", EventTab.class).set(new EscalationEventDefinition(escalation, variableForMapping, "Target"));
		refresh();
	}
	
}
