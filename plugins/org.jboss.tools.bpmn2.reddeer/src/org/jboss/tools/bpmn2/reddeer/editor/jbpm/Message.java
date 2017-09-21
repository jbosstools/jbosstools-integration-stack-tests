package org.jboss.tools.bpmn2.reddeer.editor.jbpm;

import org.eclipse.reddeer.swt.api.Combo;
import org.eclipse.reddeer.swt.impl.button.PushButton;
import org.eclipse.reddeer.swt.impl.combo.LabeledCombo;
import org.eclipse.reddeer.swt.impl.text.LabeledText;
import org.eclipse.reddeer.uiforms.impl.section.DefaultSection;
import org.jboss.tools.bpmn2.reddeer.editor.dialog.jbpm.DataTypeDialog;

/**
 * 
 */
public class Message {

	private String name;
	private String dataType;

	/**
	 * 
	 * @param name
	 * @param dataType
	 */
	public Message(String name, String dataType) {
		this.name = name;
		this.dataType = dataType;
	}

	/**
	 * 
	 * @return
	 */
	public String getName() {
		return name;
	}

	/**
	 * 
	 * @return
	 */
	public String getDataType() {
		return dataType;
	}

	/**
	 * Perform user actions which are required to set up this object in the UI
	 */
	public void setUp(String sectionName) {
		new LabeledText(new DefaultSection(sectionName), "Name").setText(name);
		Combo dataTypeCombo = new LabeledCombo("Data Type");
		if (!dataTypeCombo.getItems().contains(dataType)) {
			new PushButton(0).click();
			new DataTypeDialog().add(dataType);
		}
		dataTypeCombo.setSelection(dataType);
	}
}
