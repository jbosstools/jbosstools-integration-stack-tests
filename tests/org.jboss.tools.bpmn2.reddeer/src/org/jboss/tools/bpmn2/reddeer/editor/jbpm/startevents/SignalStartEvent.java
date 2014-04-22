package org.jboss.tools.bpmn2.reddeer.editor.jbpm.startevents;

import org.jboss.tools.bpmn2.reddeer.editor.ElementType;

/**
 * 
 */
public class SignalStartEvent extends StartEvent {

	/**
	 * 
	 * @param name
	 */
	public SignalStartEvent(String name) {
		super(name, ElementType.SIGNAL_START_EVENT);
	}

}