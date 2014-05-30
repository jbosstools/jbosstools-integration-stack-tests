package org.jboss.tools.bpmn2.reddeer.editor.jbpm.startevents;

import org.jboss.tools.bpmn2.reddeer.editor.ElementType;
import org.jboss.tools.bpmn2.reddeer.editor.jbpm.Element;

/**
 * 
 */
public class StartEvent extends Element {

	/**
	 * 
	 * @param name
	 */
	public StartEvent(String name) {
		this(name, ElementType.START_EVENT);
	}

	/**
	 * 
	 * @param name
	 * @param type
	 */
    StartEvent(String name, ElementType type) {
		super(name, type);
	}
	
}