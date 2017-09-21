package org.jboss.tools.bpmn2.reddeer.properties.setup;

import org.eclipse.reddeer.swt.impl.button.PushButton;
import org.eclipse.reddeer.swt.impl.group.DefaultGroup;
import org.eclipse.reddeer.swt.impl.table.DefaultTable;
import org.eclipse.reddeer.uiforms.impl.section.DefaultSection;
import org.jboss.tools.bpmn2.reddeer.DefaultCombo;
import org.jboss.tools.bpmn2.reddeer.editor.dialog.jbpm.MessageDialog;
import org.jboss.tools.bpmn2.reddeer.editor.jbpm.Message;
import org.jboss.tools.bpmn2.reddeer.editor.jbpm.eventdefinitions.MappingType;
import org.jboss.tools.bpmn2.reddeer.editor.properties.PropertiesTabs;
import org.jboss.tools.bpmn2.reddeer.editor.properties.SectionToolItem;

public class MessageSetUp implements SetUpAble {

	private Message message;
	private String variableName;
	private MappingType mappingType;

	public MessageSetUp(Message msg, String var, MappingType type) {
		message = msg;
		variableName = var;
		mappingType = type;
	}

	@Override
	public void setUpCTab() {
		DefaultSection section = new DefaultSection("Event Definitions");
		if (new DefaultTable(new DefaultSection("Event Definitions")).rowCount() == 1) {
			DefaultTable table = new DefaultTable(section);
			table.select(0);
			String eventOnTabType = table.getItem(0).getText(0);
			if (!"Message".equals(eventOnTabType)) {
				throw new IllegalArgumentException("Not supported yet");
			} else {
				new SectionToolItem("Event Definitions", "Edit").click();
			}
		}

		section = new DefaultSection("Message Event Definition Details");

		DefaultCombo combo = new DefaultCombo(section, "Message");
		String comboItem = message.getName() + "(" + message.getDataType() + ")";
		if (!combo.contains(comboItem)) {
			new PushButton(0).click();
			new MessageDialog().add(message);
		} else {
			combo.setSelection(comboItem);
		}

		new DefaultCombo(new DefaultGroup(section), mappingType.label()).setSelection(variableName);
		new SectionToolItem("Message Event Definition Details", "Close").click();
	}

	@Override
	public String getTabLabel() {
		return PropertiesTabs.EVENT_TAB;
	}

}
