package org.jboss.tools.bpmn2.reddeer.editor.jbpm.throwevents;

import org.jboss.tools.bpmn2.reddeer.editor.AbstractEvent;
import org.jboss.tools.bpmn2.reddeer.editor.ConstructType;
import org.jboss.tools.bpmn2.reddeer.editor.jbpm.Escalation;
import org.jboss.tools.bpmn2.reddeer.editor.jbpm.eventdefinitions.EscalationEventDefinition;
import org.jboss.tools.bpmn2.reddeer.properties.jbpm.EventTab;

/**
 * 
 */
public class EscalationIntermediateThrowEvent extends AbstractEvent {
	
	/**
	 * 
	 * @param name
	 */
	public EscalationIntermediateThrowEvent(String name) {
		super(name, ConstructType.ESCALATION_INTERMEDIATE_THROW_EVENT);
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
