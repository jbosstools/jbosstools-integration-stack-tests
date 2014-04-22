package org.jboss.tools.bpmn2.reddeer.properties.jbpm;

import org.jboss.tools.bpmn2.reddeer.editor.jbpm.Expression;
import org.jboss.tools.reddeer.DefaultCheckBox;

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
		expression.setUp("On Entry Script");
	}
	
	/**
	 * 
	 * @param expression
	 */
	public void setOnExitScript(Expression expression) {
		expression.setUp("On Exit Script");
	}
	
}
