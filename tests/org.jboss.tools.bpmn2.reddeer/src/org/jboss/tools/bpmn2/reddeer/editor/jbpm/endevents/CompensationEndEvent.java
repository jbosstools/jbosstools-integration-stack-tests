package org.jboss.tools.bpmn2.reddeer.editor.jbpm.endevents;

import org.jboss.tools.bpmn2.reddeer.editor.ConstructType;

/**
 * 
 * @author Marek Baluch <mbaluch@redhat.com>
 */
public class CompensationEndEvent extends EndEvent {
	
	/**
	 * 
	 * @param name
	 */
	public CompensationEndEvent(String name) {
		super(name, ConstructType.COMPENSATION_END_EVENT);
	}

}