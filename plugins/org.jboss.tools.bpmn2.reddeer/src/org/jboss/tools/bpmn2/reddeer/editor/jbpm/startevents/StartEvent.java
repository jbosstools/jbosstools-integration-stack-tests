package org.jboss.tools.bpmn2.reddeer.editor.jbpm.startevents;

import org.jboss.tools.bpmn2.reddeer.editor.ElementType;
import org.jboss.tools.bpmn2.reddeer.editor.jbpm.ElementWithParamMapping;

/**
 * 
 */
public class StartEvent extends ElementWithParamMapping {

	/**
	 * 
	 * @param name
	 */
	public StartEvent(String name) {
		this(name, ElementType.START_EVENT);
	}

	public StartEvent(org.jboss.tools.bpmn2.reddeer.editor.Element element) {
		super(element);
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