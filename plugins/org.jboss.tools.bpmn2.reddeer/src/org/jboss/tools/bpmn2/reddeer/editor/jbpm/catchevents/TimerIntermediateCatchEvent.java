package org.jboss.tools.bpmn2.reddeer.editor.jbpm.catchevents;

import org.jboss.tools.bpmn2.reddeer.editor.Element;
import org.jboss.tools.bpmn2.reddeer.editor.ElementType;
import org.jboss.tools.bpmn2.reddeer.editor.jbpm.eventdefinitions.TimerType;
import org.jboss.tools.bpmn2.reddeer.properties.setup.TimerSetUp;

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
	
	public TimerIntermediateCatchEvent(Element element) {
		super(element);
	}

	public void setTimer(TimerType timerType, String value) {
		propertiesHandler.setUp(new TimerSetUp(timerType, value));
		refresh();
	}
	
	public void setTimer(String duration) {
		setTimer(TimerType.DURATION, duration);
	}
	
}
