package org.jboss.tools.bpmn2.reddeer.editor.jbpm.startevents;

import org.jboss.tools.bpmn2.reddeer.editor.Element;
import org.jboss.tools.bpmn2.reddeer.editor.ElementType;
import org.jboss.tools.bpmn2.reddeer.editor.jbpm.eventdefinitions.TimerType;
import org.jboss.tools.bpmn2.reddeer.properties.shell.TimerSetUpCTab;

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
	
	public TimerStartEvent(Element element) {
		super(element);
	}

	public void setTimer(TimerType timerType, String duration) {
		propertiesHandler.setUp(new TimerSetUpCTab(timerType, duration));
		refresh();
	}
	
	public void setTimer(String duration) {
		setTimer(TimerType.DURATION, duration);
	}
	
}