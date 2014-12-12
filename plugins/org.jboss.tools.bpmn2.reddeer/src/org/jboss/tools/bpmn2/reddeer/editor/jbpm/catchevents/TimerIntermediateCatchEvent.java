package org.jboss.tools.bpmn2.reddeer.editor.jbpm.catchevents;

import org.jboss.tools.bpmn2.reddeer.editor.ElementType;
import org.jboss.tools.bpmn2.reddeer.editor.jbpm.eventdefinitions.TimerType;
import org.jboss.tools.bpmn2.reddeer.properties.shell.TimerSetUpCTab;

/**
 * 
 */
public class TimerIntermediateCatchEvent extends IntermediateCatchEvent {

	/**
	 * 
	 * @param name
	 */
	public TimerIntermediateCatchEvent(String name) {
		super(name, ElementType.TIMER_INTERMEDIATE_CATCH_EVENT);
	}

	public void setTimer(TimerType timerType, String duration) {
		graphitiProperties.setUpTabs(new TimerSetUpCTab(timerType, duration));
		refresh();
	}
	
	public void setTimer(String duration) {
		setTimer(TimerType.DURATION, duration);
	}
	
}
