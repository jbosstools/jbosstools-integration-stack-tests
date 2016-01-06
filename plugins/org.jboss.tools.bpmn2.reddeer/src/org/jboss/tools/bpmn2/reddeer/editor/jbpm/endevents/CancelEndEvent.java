package org.jboss.tools.bpmn2.reddeer.editor.jbpm.endevents;

import org.jboss.tools.bpmn2.reddeer.editor.ElementType;

/**
 * 
 */
public class CancelEndEvent extends EndEvent {

	/**
	 * 
	 * @param name
	 */
	public CancelEndEvent(String name) {
		super(name, ElementType.CANCEL_END_EVENT);
	}

}