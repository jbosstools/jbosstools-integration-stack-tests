package org.jboss.tools.bpmn2.reddeer.editor.dialog.jbpm;

import org.eclipse.swtbot.swt.finder.SWTBot;
import org.jboss.reddeer.swt.api.Combo;
import org.jboss.reddeer.swt.impl.button.PushButton;
import org.jboss.reddeer.swt.impl.combo.LabeledCombo;
import org.jboss.reddeer.swt.impl.text.LabeledText;
import org.jboss.tools.bpmn2.reddeer.editor.jbpm.ErrorRef;

/**
 * 
 */
public class ErrorDialog {

	/**
	 * 
	 * @param errorRef
	 */
	public void add(ErrorRef errorRef) {
		new SWTBot().shell("Create New Error").activate();
		new LabeledText("Name").setText(errorRef.getName());
		new LabeledText("Error Code").setText(errorRef.getCode());
		
		Combo dataTypeCombo = new LabeledCombo("Data Type");
		if (!dataTypeCombo.getItems().contains(errorRef.getDataType())) {
			new PushButton(0).click();
			new DataTypeDialog().add(errorRef.getDataType());
			new PushButton("OK").click();
		}
		dataTypeCombo.setSelection(errorRef.getDataType());
		
		new PushButton("OK").click();
	}
	
}
