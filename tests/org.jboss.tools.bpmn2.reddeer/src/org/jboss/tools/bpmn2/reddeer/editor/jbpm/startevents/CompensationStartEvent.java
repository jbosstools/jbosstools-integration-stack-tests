package org.jboss.tools.bpmn2.reddeer.editor.jbpm.startevents;

import org.jboss.tools.bpmn2.reddeer.editor.ConstructType;

/**
 * 
 */
public class CompensationStartEvent extends StartEvent {

	/**
	 * 
	 * @param name
	 */
	public CompensationStartEvent(String name) {
		super(name, ConstructType.COMPENSATION_START_EVENT);
	}
	
}