package org.jboss.tools.bpmn2.reddeer.editor.jbpm.boundaryevents;

import org.jboss.tools.bpmn2.reddeer.editor.ConstructType;
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
		super(name, ConstructType.CONDITIONAL_BOUNDARY_EVENT);
	}

	/**
	 * 
	 * @param escalation
	 */
	public void setEscalation(Escalation escalation) {
		properties.getTab("Event", EventTab.class).set(new EscalationEventDefinition(escalation));
		refresh();
	}
	
}
