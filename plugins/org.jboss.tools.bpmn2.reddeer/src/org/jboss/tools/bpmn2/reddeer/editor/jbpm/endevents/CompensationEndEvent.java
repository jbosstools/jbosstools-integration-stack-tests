package org.jboss.tools.bpmn2.reddeer.editor.jbpm.endevents;

import org.jboss.tools.bpmn2.reddeer.editor.ElementType;

/**
 * 
 */
public class CompensationEndEvent extends EndEvent {
	
	/**
	 * 
	 * @param name
	 */
	public CompensationEndEvent(String name) {
		super(name, ElementType.COMPENSATION_END_EVENT);
	}

}