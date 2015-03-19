package org.jboss.tools.bpmn2.reddeer.editor.jbpm.startevents;

import org.jboss.tools.bpmn2.reddeer.editor.ElementType;
import org.jboss.tools.bpmn2.reddeer.editor.jbpm.Escalation;
import org.jboss.tools.bpmn2.reddeer.editor.jbpm.eventdefinitions.EscalationEventDefinition;
import org.jboss.tools.bpmn2.reddeer.properties.setup.EventDefinitionSetUp;

/**
 * 
 */
public class EscalationStartEvent extends StartEvent {

	/**
	 * 
	 * @param name
	 */
	public EscalationStartEvent(String name) {
		super(name, ElementType.ESCALATION_START_EVENT);
	}

	
	public void setEscalation(Escalation escalation, String variableForMapping) {
		propertiesHandler.setUp(new EventDefinitionSetUp(new EscalationEventDefinition(escalation, variableForMapping, "Target")));
		refresh();
	}
}