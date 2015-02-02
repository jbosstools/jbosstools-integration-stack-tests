package org.jboss.tools.bpmn2.reddeer.editor.jbpm.startevents;

import org.jboss.tools.bpmn2.reddeer.editor.Element;
import org.jboss.tools.bpmn2.reddeer.editor.ElementType;
import org.jboss.tools.bpmn2.reddeer.properties.shell.ConditionSetUpCTab;

/**
 * 
 */
public class ConditionalStartEvent extends StartEvent {

	/**
	 * 
	 * @param name
	 */
	public ConditionalStartEvent(String name) {
		super(name, ElementType.CONDITIONAL_START_EVENT);
	}

	public ConditionalStartEvent(Element element) {
		super(element);
	}
	
	/**
	 * 
	 * @param language
	 * @param script
	 */
	public void setCondition(String language, String script) {
//		properties.getTab("Event", EventTab.class).set(new ConditionalEventDefinition(language, script));
		graphitiProperties.setUpTabs(new ConditionSetUpCTab(language, script));
		refresh();
	}
	
}