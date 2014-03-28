package org.jboss.tools.bpmn2.reddeer.editor.dialog.jbpm;

import org.jboss.reddeer.swt.api.Combo;
import org.jboss.reddeer.swt.impl.button.PushButton;
import org.jboss.reddeer.swt.impl.combo.LabeledCombo;
import org.jboss.reddeer.swt.impl.shell.DefaultShell;
import org.jboss.reddeer.swt.impl.text.LabeledText;
import org.jboss.tools.bpmn2.reddeer.editor.jbpm.Message;

/**
 * 
 */
public class MessageDialog {

	/**
	 * 
	 * @param name
	 * @param dataType
	 */
	public void addMessage(String name, String dataType) {
		add(new Message(name, dataType));
	}
	
	/**
	 * 
	 * @param message
	 */
	public void add(Message message) {
		new DefaultShell("Create New Message").setFocus();
		new LabeledText("Name").setText(message.getName());
		
		Combo dataTypeCombo = new LabeledCombo("Data Type");
		if (!dataTypeCombo.getItems().contains(message.getDataType())) {
			new PushButton(0).click();
			new DataTypeDialog().add(message.getDataType());
			new PushButton("OK").click();
		}
		dataTypeCombo.setSelection(message.getDataType());
	}
	
}
