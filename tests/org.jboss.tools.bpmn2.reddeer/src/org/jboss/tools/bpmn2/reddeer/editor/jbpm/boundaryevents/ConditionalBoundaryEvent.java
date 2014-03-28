package org.jboss.tools.bpmn2.reddeer.editor.jbpm.boundaryevents;

import org.jboss.tools.bpmn2.reddeer.editor.ConstructType;
import org.jboss.tools.bpmn2.reddeer.editor.jbpm.eventdefinitions.ConditionalEventDefinition;
import org.jboss.tools.bpmn2.reddeer.properties.jbpm.EventTab;

/**
 * 
 */
public class ConditionalBoundaryEvent extends BoundaryEvent {

	/**
	 * 
	 * @param name
	 */
	public ConditionalBoundaryEvent(String name) {
		super(name, ConstructType.CONDITIONAL_BOUNDARY_EVENT);
	}
	
	/**
	 * 
	 * @param language
	 * @param script
	 */
	public void setScript(String language, String script) {
		properties.getTab("Event", EventTab.class).set(new ConditionalEventDefinition(language, script));
		refresh();
	}
	
}
