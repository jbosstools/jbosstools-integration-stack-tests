package org.jboss.tools.bpmn2.reddeer.editor.jbpm.endevents;

import org.jboss.tools.bpmn2.reddeer.editor.ElementType;

/**
 * 
 */
public class SignalEndEvent extends EndEvent {

	/**
	 * 
	 * @param name
	 */
	public SignalEndEvent(String name) {
		super(name, ElementType.SIGNAL_END_EVENT);
	}

}
