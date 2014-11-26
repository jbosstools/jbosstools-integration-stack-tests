package org.jboss.tools.bpmn2.reddeer.editor.jbpm.boundaryevents;

import org.jboss.tools.bpmn2.reddeer.editor.Element;
import org.jboss.tools.bpmn2.reddeer.editor.ElementType;
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
		super(name, ElementType.ERROR_BOUNDARY_EVENT);
	}
	
	public TimerBoundaryEvent(Element element) {
		super(element);
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
