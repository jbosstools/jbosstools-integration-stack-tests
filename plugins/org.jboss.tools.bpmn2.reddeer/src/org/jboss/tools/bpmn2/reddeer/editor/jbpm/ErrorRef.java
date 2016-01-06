package org.jboss.tools.bpmn2.reddeer.editor.jbpm;

import org.jboss.reddeer.swt.api.Combo;
import org.jboss.reddeer.core.condition.ShellWithTextIsActive;
import org.jboss.reddeer.swt.impl.button.PushButton;
import org.jboss.reddeer.swt.impl.combo.LabeledCombo;
import org.jboss.reddeer.swt.impl.shell.DefaultShell;
import org.jboss.reddeer.swt.impl.tab.DefaultTabItem;
import org.jboss.reddeer.swt.impl.text.LabeledText;
import org.jboss.reddeer.common.wait.WaitWhile;
import org.jboss.reddeer.uiforms.impl.section.DefaultSection;
import org.jboss.tools.bpmn2.reddeer.editor.dialog.jbpm.DataTypeDialog;

/**
 * 
 */
public class ErrorRef {

	private static final String SHELL_LABEL = "Create New Error";
	private String name;
	private String code;
	private String dataType;

	/**
	 * 
	 * @param name
	 * @param code
	 * @param dataType
	 */
	public ErrorRef(String name, String code, String dataType) {
		this.name = name;
		this.code = code;
		this.dataType = dataType;
		// default value
		if (this.name == null || this.name.isEmpty()) {
			this.name = "Error Code: " + code;
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

	/**
	 * 
	 * @return
	 */
	public String getDataType() {
		return dataType;
	}

	/**
	 * Perform user actions which are required to set up this object in the UI.
	 */
	public void setUp() {
		new LabeledText(new DefaultSection("Error Details"), "Name").setText(name);
		new LabeledText("Error Code").setText(code);
		setUpDataType();
	}

	public void setUpViaDialog() {
		new DefaultShell(SHELL_LABEL);
		new DefaultTabItem("General").activate();
		new LabeledText("Name").setText(name);
		new DefaultTabItem("Error").activate();
		new LabeledText("Error Code").setText(code);
		setUpDataType();
		new DefaultShell(SHELL_LABEL);
		new PushButton("OK").click();
		new WaitWhile(new ShellWithTextIsActive(SHELL_LABEL));
	}

	private void setUpDataType() {
		Combo dataTypeCombo = new LabeledCombo("Data Type");
		if (!dataTypeCombo.getItems().contains(dataType)) {
			new PushButton(0).click();
			new DataTypeDialog().add(dataType);
		} else {
			dataTypeCombo.setSelection(dataType);
		}
	}

}