package org.jboss.tools.bpmn2.reddeer.editor.jbpm.endevents;

import org.jboss.tools.bpmn2.reddeer.editor.ConstructType;

/**
 * 
 * @author Marek Baluch <mbaluch@redhat.com>
 */
public class CancelEndEvent extends EndEvent {
	
	/**
	 * 
	 * @param name
	 */
	public CancelEndEvent(String name) {
		super(name, ConstructType.CANCEL_END_EVENT);
	}

}