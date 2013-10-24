package org.jboss.tools.bpmn2.reddeer.editor.jbpm.throwevents;

import org.jboss.tools.bpmn2.reddeer.editor.AbstractEvent;
import org.jboss.tools.bpmn2.reddeer.editor.ConstructType;

/**
 * 
 * @author Marek Baluch <mbaluch@redhat.com>
 */
public class IntermediateThrowEvent extends AbstractEvent {
	
	/**
	 * 
	 * @param name
	 */
	public IntermediateThrowEvent(String name) {
		super(name, ConstructType.INTERMEDIATE_THROW_EVENT);
	}

}
