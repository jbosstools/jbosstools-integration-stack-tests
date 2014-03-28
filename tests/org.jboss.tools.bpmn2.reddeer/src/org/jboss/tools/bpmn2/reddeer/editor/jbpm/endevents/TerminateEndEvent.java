package org.jboss.tools.bpmn2.reddeer.editor.jbpm.endevents;

import org.jboss.tools.bpmn2.reddeer.editor.ConstructType;

/**
 * 
 */
public class TerminateEndEvent extends EndEvent {
	
	/**
	 * 
	 * @param name
	 */
	public TerminateEndEvent(String name) {
		super(name, ConstructType.TERMINATE_END_EVENT);
	}

}
