package org.jboss.tools.bpmn2.reddeer.editor.jbpm.boundaryevents;

import org.jboss.tools.bpmn2.reddeer.editor.Element;
import org.jboss.tools.bpmn2.reddeer.editor.ElementType;
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
		super(name, ElementType.CONDITIONAL_BOUNDARY_EVENT);
	}
	
	public ConditionalBoundaryEvent(Element element) {
		super(element);
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
