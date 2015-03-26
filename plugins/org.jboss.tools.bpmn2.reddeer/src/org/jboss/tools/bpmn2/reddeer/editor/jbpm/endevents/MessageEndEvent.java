package org.jboss.tools.bpmn2.reddeer.editor.jbpm.endevents;

import org.jboss.tools.bpmn2.reddeer.editor.ElementType;
import org.jboss.tools.bpmn2.reddeer.editor.jbpm.Message;
import org.jboss.tools.bpmn2.reddeer.editor.jbpm.eventdefinitions.MappingType;
import org.jboss.tools.bpmn2.reddeer.properties.setup.MessageSetUp;

/**
 * 
 */
public class MessageEndEvent extends EndEvent {
	
	/**
	 * 
	 * @param name
	 */
	public MessageEndEvent(String name) {
		super(name, ElementType.MESSAGE_END_EVENT);
	}
	
	public void setMessage(Message message, String variableName) {
		propertiesHandler.setUp(new MessageSetUp(message, variableName, MappingType.SOURCE));
	}

}
