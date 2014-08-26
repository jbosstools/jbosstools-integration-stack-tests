package org.jboss.tools.bpmn2.reddeer.editor.jbpm.boundaryevents;

import org.jboss.tools.bpmn2.reddeer.editor.ElementType;
import org.jboss.tools.bpmn2.reddeer.editor.jbpm.ErrorRef;
import org.jboss.tools.bpmn2.reddeer.editor.jbpm.eventdefinitions.ErrorEventDefinition;
import org.jboss.tools.bpmn2.reddeer.properties.jbpm.EventTab;

/**
 * 
 */
public class ErrorBoundaryEvent extends BoundaryEvent {

	/**
	 * 
	 * @param name
	 */
	public ErrorBoundaryEvent(String name) {
		super(name, ElementType.ERROR_BOUNDARY_EVENT);
	}
	
	/**
	 * 
	 * @param errorRef
	 */
	public void setErrorEvent(ErrorRef errorRef, String variableForMapping) {
		properties.getTab("Event", EventTab.class).set(new ErrorEventDefinition(errorRef, variableForMapping, "Target"));
		refresh();
	}
	
}
