package org.jboss.tools.bpmn2.reddeer.editor.jbpm.catchevents;

import org.jboss.tools.bpmn2.reddeer.editor.ElementType;
import org.jboss.tools.bpmn2.reddeer.editor.jbpm.eventdefinitions.TimerEventDefinition;
import org.jboss.tools.bpmn2.reddeer.properties.jbpm.EventTab;

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

	/**
	 * 
	 * @param duration
	 */
	public void setTimer(String duration) {
		properties.getTab("Event", EventTab.class).set(new TimerEventDefinition(duration));
		refresh();
	}
	
}
