package org.jboss.tools.bpmn2.reddeer.properties.jbpm;

import org.jboss.reddeer.swt.impl.button.PushButton;
import org.jboss.reddeer.swt.impl.text.LabeledText;
import org.jboss.tools.bpmn2.reddeer.editor.dialog.jbpm.DataTypeDialog;
import org.jboss.tools.bpmn2.reddeer.editor.jbpm.Expression;
import org.jboss.tools.reddeer.DefaultCheckBox;
import org.jboss.tools.reddeer.DefaultCombo;
import org.jboss.tools.reddeer.DefaultSection;

/**
 * 
 */
public class AdHocSubProcessTab {

	/**
	 * 
	 * @param value
	 */
	public void setCancelRemainingActivities(boolean value) {
		new DefaultCheckBox("Cancel Remaining Instances").setChecked(value);
	}
	
	/**
	 * 
	 * @param value
	 */
	public void setIsForCompensation(boolean value) {
		new DefaultCheckBox("Is For Compensation").setChecked(value);
	}
	
	/**
	 * 
	 * @param expression
	 */
	public void setCompletionCondition(Expression expression) {
		new DefaultCombo("Script Language").setSelection(expression.getLanguage());
		new LabeledText("Script").setText(expression.getScript());
	}
	
	/**
	 * 
	 * @param name
	 * @param dataType
	 */
	public void addVariable(String name, String dataType) {
		new DefaultSection("Variable List").getToolbarButton("Add").click();
		new LabeledText("Name").setText(name);
		
		DefaultCombo c = new DefaultCombo("Data Type");
		if (!c.contains(dataType)) {
			new PushButton(0).click();
			new DataTypeDialog().add(dataType);
		}
		c.setSelection(dataType);
		
		new DefaultSection("Variable Details").getToolbarButton("Close").click();
	}
	
	/**
	 * 
	 * @param name
	 */
	public void removeVariable(String name) {
		DefaultSection s = new DefaultSection("Variable List");
		s.getTable().select(name);
		s.getToolbarButton("Remove").click();
	}
	
}
