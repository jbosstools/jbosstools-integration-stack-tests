package org.jboss.tools.bpmn2.reddeer.properties.jbpm;

import org.jboss.reddeer.swt.api.Combo;
import org.jboss.reddeer.swt.impl.button.PushButton;
import org.jboss.reddeer.swt.impl.combo.LabeledCombo;
import org.jboss.tools.bpmn2.reddeer.editor.dialog.jbpm.DataTypeDialog;

/**
 * 
 */
public class DataObjectTab {

	/**
	 * 
	 * @param name
	 */
	public void setDataType(String name) {
		Combo dataTypeCombo = new LabeledCombo("Data Type");
		if (!dataTypeCombo.getItems().contains(name)) {
			new PushButton(0).click();
			new DataTypeDialog().add(name);
		}
		dataTypeCombo.setSelection(name);
	}

}
