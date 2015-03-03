package org.jboss.tools.bpmn2.reddeer.editor.jbpm.startevents;

import org.jboss.tools.bpmn2.reddeer.editor.ElementType;
import org.jboss.tools.bpmn2.reddeer.editor.jbpm.TimerType;
import org.jboss.tools.bpmn2.reddeer.editor.jbpm.eventdefinitions.TimerEventDefinition;
import org.jboss.tools.bpmn2.reddeer.properties.jbpm.EventTab;

/**
 * 
 */
public class TimerStartEvent extends StartEvent {

	/**
	 * 
	 * @param name
	 */
	public TimerStartEvent(String name) {
		super(name, ElementType.TIMER_START_EVENT);
	}

	public void setTimer(String duration, TimerType timerType) {
		properties.getTab("Event", EventTab.class).set(new TimerEventDefinition(duration, timerType));
		refresh();
	}
	
}