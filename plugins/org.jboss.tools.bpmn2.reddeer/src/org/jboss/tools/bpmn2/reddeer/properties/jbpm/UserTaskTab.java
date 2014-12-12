package org.jboss.tools.bpmn2.reddeer.properties.jbpm;

import org.jboss.reddeer.swt.impl.button.PushButton;
import org.jboss.reddeer.swt.impl.table.DefaultTable;
import org.jboss.reddeer.swt.impl.text.LabeledText;
import org.jboss.reddeer.uiforms.impl.section.DefaultSection;
import org.jboss.tools.bpmn2.reddeer.DefaultCheckBox;
import org.jboss.tools.bpmn2.reddeer.DefaultCombo;
import org.jboss.tools.bpmn2.reddeer.editor.dialog.jbpm.ImplementationDialog;
import org.jboss.tools.bpmn2.reddeer.editor.properties.SectionToolItem;

/**
 * 
 */
public class UserTaskTab extends GeneralPropertiesTab {

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
		new SectionToolItem("Actors", "Add").click();
		new LabeledText("Name").setText(name);
		//new SectionToolItem("Actor Details", "Close").click();
		//WORKAROUND @BZ https://bugzilla.redhat.com/show_bug.cgi?id=1175772
		new SectionToolItem("Formal Expression Details", "Close").click(); 
	}
	
	/**
	 * 
	 * @param name
	 */
	public void removeActor(String name) {
		DefaultSection section = new DefaultSection("Actors");
		new DefaultTable(section).select(name);
		new SectionToolItem("Actors", "Remove").click();
	}
	
	public void addLocalVariable(String varName, String dataType) {
		new SectionToolItem("Local Variable List","Add").click();
		DefaultSection variableDetails = new DefaultSection("Local Variable Details");
		new LabeledText(variableDetails, "Name").setText(varName);
		DefaultCombo combo = new DefaultCombo("Data Type");
		if (!combo.contains(dataType)) {
			throw new UnsupportedOperationException("Adding variable of type: " + dataType + " is not supported yet");
		}
		combo.setSelection(dataType);
		new SectionToolItem("Local Variable Details", "Close").click();
	}
	
}
