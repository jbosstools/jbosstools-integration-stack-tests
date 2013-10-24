package org.jboss.tools.bpmn2.reddeer.editor.jbpm.catchevents;

import org.jboss.tools.bpmn2.reddeer.editor.AbstractEvent;
import org.jboss.tools.bpmn2.reddeer.editor.ConstructType;

/**
 * 
 * @author Marek Baluch <mbaluch@redhat.com>
 */
public class IntermediateCatchEvent extends AbstractEvent {

	/**
	 * 
	 * @param name
	public IntermediateCatchEvent(String name) {
		super(name, ConstructType.INTERMEDIATE_CATCH_EVENT);
	}
	 */
	
	/**
	 * 
	 * @param name
	 * @param type
	 */
	IntermediateCatchEvent(String name, ConstructType type) {
		super(name, type);
	}

}
