package org.jboss.tools.bpmn2.reddeer.editor.jbpm.eventdefinitions;

import org.jboss.reddeer.swt.impl.button.PushButton;
import org.jboss.reddeer.uiforms.impl.section.DefaultSection;
import org.jboss.tools.bpmn2.reddeer.DefaultCombo;
import org.jboss.tools.bpmn2.reddeer.editor.dialog.jbpm.MessageDialog;
import org.jboss.tools.bpmn2.reddeer.editor.jbpm.EventDefinition;
import org.jboss.tools.bpmn2.reddeer.editor.jbpm.Message;
import org.jboss.tools.bpmn2.reddeer.editor.properties.SectionToolItem;

/**
 * 
 */
public class MessageEventDefinition extends EventDefinition {

	private Message message;
	private String variableName;
	private MappingType type;

	/**
	 * 
	 * @param signalName
	 */
	public MessageEventDefinition(Message message, MappingType type) {
		this(message, null, type);
	}

	/**
	 * 
	 * @param signalName
	 * @param variableName
	 */
	public MessageEventDefinition(Message message, String variableName, MappingType type) {
		this.message = message;
		this.variableName = variableName;
		this.type = type;
	}

	@Override
	public void setUp() {

		DefaultSection section = new DefaultSection("Message Event Definition Details");

		DefaultCombo combo = new DefaultCombo(section, "Message");
		String comboItem = message.getName() + "(" + message.getDataType() + ")";
		if (!combo.contains(comboItem)) {
			new PushButton(0).click();
			new MessageDialog().add(message);
		}
		combo.setSelection(comboItem);

		new DefaultCombo(section, type.label()).setSelection(variableName);
		new SectionToolItem("Message Event Definition Details", "Close").click();
	}

}
