package org.jboss.tools.bpmn2.reddeer.properties.jbpm;

import org.jboss.reddeer.swt.impl.button.PushButton;
import org.jboss.tools.bpmn2.reddeer.editor.dialog.jbpm.CalledActivityDialog;
import org.jboss.tools.bpmn2.reddeer.editor.jbpm.Expression;
import org.jboss.tools.reddeer.DefaultCheckBox;

/**
 * 
 */
public class CallActivityTab {

	/**
	 * 
	 * @param value
	 */
	public void setWaitForCompletion(boolean value) {
		new DefaultCheckBox("Wait For Completion").setChecked(value);
	}
	
	/**
	 * 
	 * @param value
	 */
	public void setIndependent(boolean value) {
		new DefaultCheckBox("Independent").setChecked(value);
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
	 * @param activityName
	 */
	public void setCalledActivity(String activityName) {
		new PushButton(0).click();
		new CalledActivityDialog().add(activityName);
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
