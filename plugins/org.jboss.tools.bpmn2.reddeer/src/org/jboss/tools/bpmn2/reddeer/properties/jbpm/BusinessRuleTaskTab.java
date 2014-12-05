package org.jboss.tools.bpmn2.reddeer.properties.jbpm;

import org.jboss.reddeer.swt.impl.button.PushButton;
import org.jboss.reddeer.swt.impl.text.LabeledText;
import org.jboss.tools.bpmn2.reddeer.DefaultCheckBox;
import org.jboss.tools.bpmn2.reddeer.DefaultCombo;
import org.jboss.tools.bpmn2.reddeer.editor.dialog.jbpm.ImplementationDialog;

/**
 * 
 */
public class BusinessRuleTaskTab extends GeneralPropertiesTab {

	/**
	 * 
	 * @param value
	 */
	public void setRuleFlowGroup(String value) {
		new LabeledText("Rule Flow Group").setText(value);
	}

	/**
	 * 
	 * @param implementationUri
	 */
	public void setImplementation(String implementationUri) {
		DefaultCombo combo = new DefaultCombo("Implementation");
		if (!combo.contains(implementationUri)) {
			new PushButton(0).click();
			new ImplementationDialog().add(implementationUri);
		}
		combo.setSelection(implementationUri);
	}
	
	/**
	 * 
	 * @param value
	 */
	public void setIsForCompensation(boolean value) {
		new DefaultCheckBox("Is For Compensation").setChecked(value);
	}
	
}
