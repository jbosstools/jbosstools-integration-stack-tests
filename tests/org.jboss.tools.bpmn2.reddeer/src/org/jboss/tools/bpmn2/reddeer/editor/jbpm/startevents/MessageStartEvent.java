package org.jboss.tools.bpmn2.reddeer.editor.jbpm.startevents;

import org.jboss.tools.bpmn2.reddeer.editor.ConstructType;
import org.jboss.tools.bpmn2.reddeer.editor.jbpm.Message;
import org.jboss.tools.bpmn2.reddeer.editor.jbpm.eventdefinitions.MessageEventDefinition;
import org.jboss.tools.bpmn2.reddeer.editor.jbpm.eventdefinitions.MessageEventDefinition.Type;
import org.jboss.tools.bpmn2.reddeer.properties.jbpm.EventTab;

/**
 * 
 */
public class MessageStartEvent extends StartEvent {

	/**
	 * 
	 * @param name
	 */
	public MessageStartEvent(String name) {
		super(name, ConstructType.MESSAGE_START_EVENT);
	}

	/**
	 * 
	 * @param message
	 * @param variableName
	 */
	public void setMessageMapping(Message message, String targetVariable) {
		properties.getTab("Event", EventTab.class).set(new MessageEventDefinition(message, targetVariable, Type.TARGET));
		refresh();
	}
	
}