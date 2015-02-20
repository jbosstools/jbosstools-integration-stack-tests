package org.jboss.tools.bpmn2.reddeer.editor.jbpm.endevents;

import org.jboss.tools.bpmn2.reddeer.editor.Element;
import org.jboss.tools.bpmn2.reddeer.editor.ElementType;
import org.jboss.tools.bpmn2.reddeer.editor.jbpm.ErrorRef;
import org.jboss.tools.bpmn2.reddeer.editor.jbpm.eventdefinitions.ErrorEventDefinition;
import org.jboss.tools.bpmn2.reddeer.properties.shell.EventDefinitionSetUp;

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

	public ErrorEndEvent(Element element) {
		super(element);
	}
	
	/**
	 * 
	 * @param errorRef
	 */
	public void setErrorEvent(ErrorRef errorRef, String variableForMapping) {
		propertiesHandler.setUp(new EventDefinitionSetUp(new ErrorEventDefinition(errorRef, variableForMapping, "Source")));
		refresh();
	}
	
}
