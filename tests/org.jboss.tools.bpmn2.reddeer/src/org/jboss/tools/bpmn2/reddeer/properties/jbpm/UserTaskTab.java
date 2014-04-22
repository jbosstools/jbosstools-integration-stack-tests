package org.jboss.tools.bpmn2.reddeer.properties.jbpm;

import org.jboss.reddeer.swt.impl.button.PushButton;
import org.jboss.reddeer.swt.impl.text.LabeledText;
import org.jboss.tools.bpmn2.reddeer.editor.dialog.jbpm.ImplementationDialog;
import org.jboss.tools.bpmn2.reddeer.editor.jbpm.Expression;
import org.jboss.tools.reddeer.DefaultCheckBox;
import org.jboss.tools.reddeer.DefaultCombo;
import org.jboss.tools.reddeer.DefaultSection;

/**
 * 
 */
public class UserTaskTab {

	/**
	 * 
	 * @param value
	 */
	public void setTaskName(String value) {
		new LabeledText("Task Name").setText(value);
	}
	
	/**
	 * 
	 * @param value
	 */
	public void setPriority(String value) {
		new LabeledText("Priority").setText(value);
	}
	
	/**
	 * 
	 * @param value
	 */
	public void setComment(String value) {
		new LabeledText("Comment").setText(value);
	}
	
	/**
	 * 
	 * @param value
	 */
	public void setGroupId(String value) {
		new LabeledText("Group Id").setText(value);
	}
	
	/**
	 * 
	 * @param value
	 */
	public void setSkippable(boolean value) {
		new DefaultCheckBox("Skippable").setChecked(value);
	}

	/**
	 * 
	 * @param value
	 */
	public void setContent(String value) {
		new LabeledText("Content").setText(value);
	}
	
	/**
	 * 
	 * @param value
	 */
	public void setLocale(String value) {
		new LabeledText("Locale").setText(value);
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
	
	/**
	 * 
	 * @param name
	 */
	public void addActor(String name) {
		new DefaultSection("Actors").getToolbarButton("Add").click();
		new LabeledText("Script").setText(name);
		new DefaultSection("Formal Expression Details").getToolbarButton("Close").click();
	}
	
	/**
	 * 
	 * @param name
	 */
	public void removeActor(String name) {
		DefaultSection section = new DefaultSection("Actors");
		section.getTable().select(name);
		section.getToolbarButton("Remove").click();
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
