package org.jboss.tools.bpmn2.reddeer.editor.jbpm.catchevents;

import org.jboss.tools.bpmn2.reddeer.editor.ElementType;
import org.jboss.tools.bpmn2.reddeer.editor.jbpm.eventdefinitions.SignalEventDefinition;
import org.jboss.tools.bpmn2.reddeer.editor.jbpm.eventdefinitions.SignalEventDefinition.Type;
import org.jboss.tools.bpmn2.reddeer.properties.jbpm.EventTab;

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
	
	/**
	 * 
	 * @param signal
	 * @param variable
	 */
	public void setSignalMapping(String signal, String targetVariable) {
		properties.getTab("Event", EventTab.class).set(new SignalEventDefinition(signal, targetVariable, Type.TARGET));
		refresh();
	}
	
	
}
