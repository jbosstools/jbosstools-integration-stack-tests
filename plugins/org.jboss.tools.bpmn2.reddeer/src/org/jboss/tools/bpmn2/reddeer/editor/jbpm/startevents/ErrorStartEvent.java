package org.jboss.tools.bpmn2.reddeer.editor.jbpm.startevents;

import org.jboss.tools.bpmn2.reddeer.editor.ElementType;

/**
 * 
 */
public class ErrorStartEvent extends StartEvent {

	/**
	 * 
	 * @param name
	 */
	public ErrorStartEvent(String name) {
		super(name, ElementType.ERROR_START_EVENT);
	}

}