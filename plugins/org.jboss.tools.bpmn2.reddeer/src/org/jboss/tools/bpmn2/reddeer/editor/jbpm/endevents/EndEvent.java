package org.jboss.tools.bpmn2.reddeer.editor.jbpm.endevents;

import org.jboss.tools.bpmn2.reddeer.editor.Element;
import org.jboss.tools.bpmn2.reddeer.editor.ElementType;

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
	
	public EndEvent(Element element) {
		super(element);
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
