package org.jboss.tools.bpmn2.reddeer.editor.jbpm.catchevents;

import org.jboss.tools.bpmn2.reddeer.editor.ElementType;
import org.jboss.tools.bpmn2.reddeer.editor.jbpm.ElementWithParamMapping;

/**
 * 
 */
public class IntermediateCatchEvent extends ElementWithParamMapping {

	/**
	 * 
	 * @param name
	 * @param type
	 */
	IntermediateCatchEvent(String name, ElementType type) {
		super(name, type);
	}

	public IntermediateCatchEvent(org.jboss.tools.bpmn2.reddeer.editor.Element element) {
		super(element);
	}
}
