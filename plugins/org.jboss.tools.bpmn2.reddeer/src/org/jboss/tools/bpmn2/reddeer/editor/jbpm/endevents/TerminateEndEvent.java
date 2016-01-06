package org.jboss.tools.bpmn2.reddeer.editor.jbpm.endevents;

import org.jboss.tools.bpmn2.reddeer.editor.Element;
import org.jboss.tools.bpmn2.reddeer.editor.ElementType;

/**
 * 
 */
public class TerminateEndEvent extends EndEvent {

	/**
	 * 
	 * @param name
	 */
	public TerminateEndEvent(String name) {
		super(name, ElementType.TERMINATE_END_EVENT);
	}

	public TerminateEndEvent(Element element) {
		super(element);
	}

}
