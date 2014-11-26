package org.jboss.tools.bpmn2.reddeer.properties.jbpm;

import org.jboss.reddeer.swt.impl.button.PushButton;
import org.jboss.reddeer.swt.impl.table.DefaultTable;
import org.jboss.reddeer.swt.impl.text.LabeledText;
import org.jboss.reddeer.uiforms.impl.section.DefaultSection;
import org.jboss.tools.bpmn2.reddeer.editor.dialog.jbpm.DataTypeDialog;
import org.jboss.tools.bpmn2.reddeer.editor.jbpm.Expression;
import org.jboss.tools.bpmn2.reddeer.editor.properties.SectionToolItem;
import org.jboss.tools.reddeer.DefaultCheckBox;
import org.jboss.tools.reddeer.DefaultCombo;

/**
 * 
 */
public class AdHocSubProcessTab extends GeneralPropertiesTab {

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
		expression.setUp("Attributes");
	}
	
	/**
	 * 
	 * @param name
	 * @param dataType
	 */
	public void addVariable(String name, String dataType) {
		new SectionToolItem("Local Variable List", "Add").click();
		new LabeledText("Name").setText(name);
		
		DefaultCombo c = new DefaultCombo("Data Type");
		if (!c.contains(dataType)) {
			new PushButton(0).click();
			new DataTypeDialog().add(dataType);
		}
		c.setSelection(dataType);
		
		new SectionToolItem("Local Variable Details", "Close").click();
	}
	
	/**
	 * 
	 * @param name
	 */
	public void removeVariable(String name) {
		DefaultSection s = new DefaultSection("Variable List");
		new DefaultTable(s).select(name);
		new SectionToolItem("Variable List", "Remove").click();
	}
	
}
