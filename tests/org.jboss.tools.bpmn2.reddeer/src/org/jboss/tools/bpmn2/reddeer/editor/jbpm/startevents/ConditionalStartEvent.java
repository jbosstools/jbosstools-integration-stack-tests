package org.jboss.tools.bpmn2.reddeer.editor.jbpm.startevents;

import org.jboss.tools.bpmn2.reddeer.editor.ConstructType;
import org.jboss.tools.bpmn2.reddeer.editor.jbpm.eventdefinitions.ConditionalEventDefinition;
import org.jboss.tools.bpmn2.reddeer.properties.jbpm.EventTab;

/**
 * 
 */
public class ConditionalStartEvent extends StartEvent {

	/**
	 * 
	 * @param name
	 */
	public ConditionalStartEvent(String name) {
		super(name, ConstructType.CONDITIONAL_START_EVENT);
	}

	/**
	 * 
	 * @param language
	 * @param script
	 */
	public void setCondition(String language, String script) {
		properties.getTab("Event", EventTab.class).set(new ConditionalEventDefinition(language, script));
		refresh();
	}
	
}