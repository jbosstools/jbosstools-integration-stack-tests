package org.jboss.tools.bpmn2.reddeer.editor.jbpm;

import org.jboss.reddeer.swt.api.Combo;
import org.jboss.reddeer.swt.impl.button.PushButton;
import org.jboss.reddeer.swt.impl.combo.LabeledCombo;
import org.jboss.reddeer.swt.impl.shell.DefaultShell;
import org.jboss.reddeer.swt.impl.tab.DefaultTabItem;
import org.jboss.reddeer.swt.impl.text.LabeledText;
import org.jboss.tools.bpmn2.reddeer.editor.dialog.jbpm.DataTypeDialog;
import org.jboss.tools.reddeer.DefaultSection;

/**
 * 
 */
public class Escalation {

	private String name;
	private String code;
	private String dataType;

	/**
	 * 
	 * @param name
	 * @param code
	 */
    public Escalation(String name, String code, String dataType) {
    	this.name = name;
    	this.code = code;
    	this.dataType = dataType;
    	
    	// default value
    	if (this.name == null || this.name.isEmpty()) {
    		this.name = "Escalation Code: " + code;
    	}
    }

    /**
     * 
     * @return
     */
	public String getName() {
		return name;
	}

	/**
	 * 
	 * @return
	 */
	public String getCode() {
		return code;
	}
	
	public String getDataType() {
		return dataType;
	}
	
	/**
	 * Perform user actions which are required to set up this object
	 * in the UI.
	 */
	public void setUpViaProcessDefinitions() {
		new LabeledText(new DefaultSection("Escalation Details"), "Name").setText(name);
		new LabeledText("Escalation Code").setText(code);
		setUpDataType();
	}
	
	public void setUpViaDialog() {
		new DefaultShell("Create New Escalation").setFocus();
		new DefaultTabItem("General").activate();
		new LabeledText(new DefaultSection("Attributes"), "Name").setText(name);
		new DefaultTabItem("Escalation").activate();
		new LabeledText("Escalation Code").setText(code);
		setUpDataType();
		new PushButton("OK").click();
	}
	
	private void setUpDataType() {
		Combo dataTypeCombo = new LabeledCombo("Data Type");
		if (!dataTypeCombo.getItems().contains(dataType)) {
			new PushButton(0).click();
			new DataTypeDialog().add(dataType);
		}
		dataTypeCombo.setSelection(dataType);
	}
	
}
