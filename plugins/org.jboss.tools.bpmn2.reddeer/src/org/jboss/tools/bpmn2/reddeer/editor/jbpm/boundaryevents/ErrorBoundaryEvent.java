package org.jboss.tools.bpmn2.reddeer.editor.jbpm.boundaryevents;

import org.jboss.tools.bpmn2.reddeer.editor.Element;
import org.jboss.tools.bpmn2.reddeer.editor.ElementType;
import org.jboss.tools.bpmn2.reddeer.editor.jbpm.ErrorRef;
import org.jboss.tools.bpmn2.reddeer.editor.jbpm.eventdefinitions.ErrorEventDefinition;
import org.jboss.tools.bpmn2.reddeer.properties.setup.EventDefinitionSetUp;

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
	
	public ErrorBoundaryEvent(Element element) {
		super(element);
	}
	
	/**
	 * 
	 * @param errorRef
	 */
	public void setErrorEvent(ErrorRef errorRef, String variableForMapping) {
		propertiesHandler.setUp(new EventDefinitionSetUp(new ErrorEventDefinition(errorRef, variableForMapping, "Target")));
		refresh();
	}
	
}
