package org.jboss.tools.bpmn2.reddeer.editor.jbpm.catchevents;

import org.jboss.tools.bpmn2.reddeer.editor.Element;
import org.jboss.tools.bpmn2.reddeer.editor.ElementType;
import org.jboss.tools.bpmn2.reddeer.editor.jbpm.eventdefinitions.SignalEventDefinition;
import org.jboss.tools.bpmn2.reddeer.editor.jbpm.eventdefinitions.SignalEventDefinition.Type;
import org.jboss.tools.bpmn2.reddeer.properties.shell.EventDefinitionSetUp;

/**
 * 
 */
public class SignalIntermediateCatchEvent extends IntermediateCatchEvent {

	/**
	 * 
	 * @param name
	 */
	public SignalIntermediateCatchEvent(String name) {
		super(name, ElementType.SIGNAL_INTERMEDIATE_CATCH_EVENT);
	}
	
	public SignalIntermediateCatchEvent(Element element) {
		super(element);
	}
	
	/**
	 * 
	 * @param signal
	 * @param variable
	 */
	public void setSignalMapping(String signal, String targetVariable) {
		propertiesHandler.setUp(new EventDefinitionSetUp(new SignalEventDefinition(signal, targetVariable, Type.TARGET)));
		refresh();
	}
	
	
}
