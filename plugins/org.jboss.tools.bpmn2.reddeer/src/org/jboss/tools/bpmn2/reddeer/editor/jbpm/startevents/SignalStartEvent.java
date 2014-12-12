package org.jboss.tools.bpmn2.reddeer.editor.jbpm.startevents;

import org.jboss.tools.bpmn2.reddeer.editor.ElementType;
import org.jboss.tools.bpmn2.reddeer.editor.jbpm.Signal;
import org.jboss.tools.bpmn2.reddeer.editor.jbpm.eventdefinitions.MappingType;
import org.jboss.tools.bpmn2.reddeer.properties.shell.SignalSetUpCTab;

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

	public void setSignal(Signal signal, String variable) {
//		properties.getTab("Event", EventTab.class).addNewSignal(signal);
		graphitiProperties.setUpTabs(new SignalSetUpCTab(signal, variable, MappingType.TARGET));
	}
}