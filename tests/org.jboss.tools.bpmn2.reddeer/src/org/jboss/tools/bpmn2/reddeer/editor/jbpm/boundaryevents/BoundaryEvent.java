package org.jboss.tools.bpmn2.reddeer.editor.jbpm.boundaryevents;

import org.jboss.tools.bpmn2.reddeer.editor.Construct;
import org.jboss.tools.bpmn2.reddeer.editor.ConstructType;
import org.jboss.tools.bpmn2.reddeer.properties.jbpm.BoundaryEventTab;

/**
 * 
 */
public class BoundaryEvent extends Construct {

	/**
	 * 
	 * @param name
	 */
	public BoundaryEvent(String name) {
		super(name, ConstructType.BOUNDARY_EVENT);
	}
	
	/**
	 * 
	 * @param name
	 * @param type
	 */
	BoundaryEvent(String name, ConstructType type) {
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
