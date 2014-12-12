package org.jboss.tools.bpmn2.reddeer.editor.jbpm.throwevents;

import org.jboss.tools.bpmn2.reddeer.editor.ElementType;
import org.jboss.tools.bpmn2.reddeer.editor.jbpm.Element;
import org.jboss.tools.bpmn2.reddeer.editor.jbpm.Message;
import org.jboss.tools.bpmn2.reddeer.editor.jbpm.eventdefinitions.MappingType;
import org.jboss.tools.bpmn2.reddeer.properties.shell.MessageSetUpCTab;

/**
 * 
 */
public class MessageIntermediateThrowEvent extends Element {
	
	/**
	 * 
	 * @param name
	 */
	public MessageIntermediateThrowEvent(String name) {
		super(name, ElementType.MESSAGE_INTERMEDIATE_THROW_EVENT);
	}

	/**
	 * 
	 * @param message
	 * @param sourceVariable
	 */
	public void setMessageMapping(Message message, String sourceVariable) {
//		properties.getTab("Event", EventTab.class).set(new MessageEventDefinition(message, sourceVariable, Type.SOURCE));
		graphitiProperties.setUpTabs(new MessageSetUpCTab(message, sourceVariable, MappingType.SOURCE));

		refresh();
	}
	
}
