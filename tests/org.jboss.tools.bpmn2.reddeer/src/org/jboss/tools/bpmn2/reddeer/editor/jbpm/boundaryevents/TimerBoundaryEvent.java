package org.jboss.tools.bpmn2.reddeer.editor.jbpm.boundaryevents;

import org.jboss.tools.bpmn2.reddeer.editor.ConstructType;
import org.jboss.tools.bpmn2.reddeer.editor.jbpm.eventdefinitions.TimerEventDefinition;
import org.jboss.tools.bpmn2.reddeer.properties.jbpm.EventTab;

/**
 * 
 */
public class TimerBoundaryEvent extends BoundaryEvent {

	/**
	 * 
	 * @param name
	 */
	public TimerBoundaryEvent(String name) {
		super(name, ConstructType.ERROR_BOUNDARY_EVENT);
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
