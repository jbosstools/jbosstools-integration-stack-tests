package org.jboss.tools.bpmn2.reddeer.editor.jbpm.endevents;

import org.jboss.tools.bpmn2.reddeer.editor.ElementType;
import org.jboss.tools.bpmn2.reddeer.editor.jbpm.ErrorRef;
import org.jboss.tools.bpmn2.reddeer.editor.jbpm.eventdefinitions.ErrorEventDefinition;
import org.jboss.tools.bpmn2.reddeer.properties.jbpm.EventTab;

/**
 * 
 */
public class ErrorEndEvent extends EndEvent {
	
	/**
	 * 
	 * @param name
	 */
	public ErrorEndEvent(String name) {
		super(name, ElementType.ERROR_END_EVENT);
	}

	/**
	 * 
	 * @param errorRef
	 */
	public void setErrorEvent(ErrorRef errorRef) {
		properties.getTab("Event", EventTab.class).set(new ErrorEventDefinition(errorRef));
		refresh();
	}
	
}
