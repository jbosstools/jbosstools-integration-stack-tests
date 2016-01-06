package org.jboss.tools.bpmn2.reddeer.editor.jbpm.startevents;

import org.jboss.tools.bpmn2.reddeer.editor.Element;
import org.jboss.tools.bpmn2.reddeer.editor.ElementType;
import org.jboss.tools.bpmn2.reddeer.editor.jbpm.Message;
import org.jboss.tools.bpmn2.reddeer.editor.jbpm.eventdefinitions.MappingType;
import org.jboss.tools.bpmn2.reddeer.properties.setup.MessageSetUp;

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

	public MessageStartEvent(Element element) {
		super(element);
	}

	/**
	 * 
	 * @param message
	 * @param variableName
	 */
	public void setMessageMapping(Message message, String targetVariable) {
		propertiesHandler.setUp(new MessageSetUp(message, targetVariable, MappingType.TARGET));
		refresh();
	}

}