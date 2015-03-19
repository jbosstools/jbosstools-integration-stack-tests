package org.jboss.tools.bpmn2.reddeer.editor.jbpm.startevents;

import org.jboss.tools.bpmn2.reddeer.editor.Element;
import org.jboss.tools.bpmn2.reddeer.editor.ElementType;
import org.jboss.tools.bpmn2.reddeer.editor.jbpm.Signal;
import org.jboss.tools.bpmn2.reddeer.editor.jbpm.eventdefinitions.MappingType;
import org.jboss.tools.bpmn2.reddeer.properties.setup.SignalSetUp;

/**
 * 
 */
public class SignalStartEvent extends StartEvent {

	/**
	 * 
	 * @param name
	 */
	public SignalStartEvent(String name) {
		super(name, ElementType.SIGNAL_START_EVENT);
	}
	
	public SignalStartEvent(Element element) {
		super(element);
	}

	public void setSignal(Signal signal, String variable) {
		propertiesHandler.setUp(new SignalSetUp(signal, variable, MappingType.TARGET));
	}
}