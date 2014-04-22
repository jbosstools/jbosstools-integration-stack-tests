package org.jboss.tools.bpmn2.reddeer.editor.jbpm.boundaryevents;

import org.jboss.tools.bpmn2.reddeer.editor.Element;
import org.jboss.tools.bpmn2.reddeer.editor.ElementType;
import org.jboss.tools.bpmn2.reddeer.properties.jbpm.BoundaryEventTab;

/**
 * 
 */
public class BoundaryEvent extends Element {

	/**
	 * 
	 * @param name
	 */
	public BoundaryEvent(String name) {
		super(name, ElementType.BOUNDARY_EVENT);
	}
	
	/**
	 * 
	 * @param name
	 * @param type
	 */
	BoundaryEvent(String name, ElementType type) {
		super(name, type);
	}
	
	/**
	 * 
	 * @param value
	 */
	public void setCancelActivity(boolean value) {
		properties.getTab("Event", BoundaryEventTab.class).setCancelActivity(value);
	}
	
}
