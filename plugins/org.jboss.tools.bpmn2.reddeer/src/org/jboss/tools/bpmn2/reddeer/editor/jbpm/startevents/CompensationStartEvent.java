package org.jboss.tools.bpmn2.reddeer.editor.jbpm.startevents;

import org.jboss.tools.bpmn2.reddeer.editor.ElementType;

/**
 * 
 */
public class CompensationStartEvent extends StartEvent {

	/**
	 * 
	 * @param name
	 */
	public CompensationStartEvent(String name) {
		super(name, ElementType.COMPENSATION_START_EVENT);
	}

}