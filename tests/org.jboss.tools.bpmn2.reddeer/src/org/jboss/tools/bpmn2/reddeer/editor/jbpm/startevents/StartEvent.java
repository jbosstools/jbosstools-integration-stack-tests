package org.jboss.tools.bpmn2.reddeer.editor.jbpm.startevents;

import org.jboss.tools.bpmn2.reddeer.editor.AbstractEvent;
import org.jboss.tools.bpmn2.reddeer.editor.ConstructType;

/**
 * 
 */
public class StartEvent extends AbstractEvent {

	/**
	 * 
	 * @param name
	 */
	public StartEvent(String name) {
		this(name, ConstructType.START_EVENT);
	}

	/**
	 * 
	 * @param name
	 * @param type
	 */
    StartEvent(String name, ConstructType type) {
		super(name, type);
	}
	
}