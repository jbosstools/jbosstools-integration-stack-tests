package org.jboss.tools.bpmn2.reddeer.editor.jbpm.startevents;

import org.jboss.tools.bpmn2.reddeer.editor.ElementType;
import org.jboss.tools.bpmn2.reddeer.editor.jbpm.Message;
import org.jboss.tools.bpmn2.reddeer.editor.jbpm.eventdefinitions.MappingType;
import org.jboss.tools.bpmn2.reddeer.properties.shell.MessageSetUpCTab;

/**
 * 
 */
public class MessageStartEvent extends StartEvent {

	/**
	 * 
	 * @param name
	 */
	public MessageStartEvent(String name) {
		super(name, ElementType.MESSAGE_START_EVENT);
	}

	/**
	 * 
	 * @param message
	 * @param variableName
	 */
	public void setMessageMapping(Message message, String targetVariable) {
//		properties.getTab("Event", EventTab.class).set(new MessageEventDefinition(message, targetVariable, Type.TARGET));
		graphitiProperties.setUpTabs(new MessageSetUpCTab(message, targetVariable, MappingType.TARGET));
		refresh();
	}
	
}