package org.jboss.tools.bpmn2.reddeer.editor.jbpm.throwevents;

import org.jboss.tools.bpmn2.reddeer.editor.ElementType;
import org.jboss.tools.bpmn2.reddeer.editor.jbpm.ElementWithParamMapping;

/**
 * 
 */
public class IntermediateThrowEvent extends ElementWithParamMapping {

	/**
	 * 
	 * @param name
	 */
	public IntermediateThrowEvent(String name) {
		super(name, ElementType.INTERMEDIATE_THROW_EVENT);
	}

	public IntermediateThrowEvent(org.jboss.tools.bpmn2.reddeer.editor.Element element) {
		super(element);
	}
}
