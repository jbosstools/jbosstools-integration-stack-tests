package org.jboss.tools.bpmn2.reddeer.editor.jbpm.endevents;

import org.jboss.tools.bpmn2.reddeer.editor.AbstractEvent;
import org.jboss.tools.bpmn2.reddeer.editor.ConstructType;

/**
 * 
 */
public class EndEvent extends AbstractEvent {
	
	/**
	 * 
	 * @param name
	 */
	public EndEvent(String name) {
		super(name, ConstructType.END_EVENT);
	}

	/**
	 * 
	 * @param name
	 * @param type
	 */
	EndEvent(String name, ConstructType type) {
		super(name, type);
	}
	
}
