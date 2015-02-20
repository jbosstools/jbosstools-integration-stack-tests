package org.jboss.tools.bpmn2.reddeer.editor.jbpm.boundaryevents;

import org.jboss.tools.bpmn2.reddeer.editor.Element;
import org.jboss.tools.bpmn2.reddeer.editor.ElementType;
import org.jboss.tools.bpmn2.reddeer.editor.jbpm.Escalation;
import org.jboss.tools.bpmn2.reddeer.editor.jbpm.eventdefinitions.EscalationEventDefinition;
import org.jboss.tools.bpmn2.reddeer.properties.shell.EventDefinitionSetUp;

/**
 * 
 */
public class EscalationBoundaryEvent extends BoundaryEvent {

	/**
	 * 
	 * @param name
	 */
	public EscalationBoundaryEvent(String name) {
		super(name, ElementType.ESCALATION_BOUNDARY_EVENT);
	}

	public EscalationBoundaryEvent(Element element) {
		super(element);
	}
	
	/**
	 * 
	 * @param escalation
	 */
	public void setEscalation(Escalation escalation, String variableForMapping) {
		propertiesHandler.setUp(new EventDefinitionSetUp(new EscalationEventDefinition(escalation, variableForMapping, "Target")));
		refresh();
	}
	
}
