package org.jboss.tools.bpmn2.reddeer.editor.jbpm.startevents;

import org.jboss.tools.bpmn2.reddeer.editor.ConstructType;

/**
 * 
 */
public class EscalationStartEvent extends StartEvent {

	/**
	 * 
	 * @param name
	 */
	public EscalationStartEvent(String name) {
		super(name, ConstructType.ESCALATION_START_EVENT);
	}

}