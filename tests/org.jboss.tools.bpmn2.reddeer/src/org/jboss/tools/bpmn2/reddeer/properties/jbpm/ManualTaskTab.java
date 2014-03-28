package org.jboss.tools.bpmn2.reddeer.properties.jbpm;

import org.jboss.tools.bpmn2.reddeer.editor.jbpm.Expression;
import org.jboss.tools.reddeer.DefaultCheckBox;
import org.jboss.tools.reddeer.DefaultSection;

/**
 * 
 */
public class ManualTaskTab {

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
	public void setOnEntryScript(Expression expression) {
		DefaultSection section = new DefaultSection("On Entry Script");
		section.getComboBox("Script Language").setSelection(expression.getLanguage());
		section.getText("Script").setText(expression.getScript());
	}
	
	/**
	 * 
	 * @param expression
	 */
	public void setOnExitScript(Expression expression) {
		DefaultSection section = new DefaultSection("On Exit Script");
		section.getComboBox("Script Language").setSelection(expression.getLanguage());
		section.getText("Script").setText(expression.getScript());
	}
	
}
