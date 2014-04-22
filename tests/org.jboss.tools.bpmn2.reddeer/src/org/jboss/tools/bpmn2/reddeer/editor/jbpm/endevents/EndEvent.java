package org.jboss.tools.bpmn2.reddeer.editor.jbpm.endevents;

import org.jboss.tools.bpmn2.reddeer.editor.ElementType;
import org.jboss.tools.bpmn2.reddeer.editor.jbpm.Element;

/**
 * 
 */
public class EndEvent extends Element {
	
	/**
	 * 
	 * @param name
	 */
	public EndEvent(String name) {
		super(name, ElementType.END_EVENT);
	}

	/**
	 * 
	 * @param name
	 * @param type
	 */
	EndEvent(String name, ElementType type) {
		super(name, type);
	}
	
}
