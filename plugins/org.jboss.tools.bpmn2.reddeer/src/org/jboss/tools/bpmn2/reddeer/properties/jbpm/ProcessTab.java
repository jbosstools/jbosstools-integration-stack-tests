package org.jboss.tools.bpmn2.reddeer.properties.jbpm;

import org.jboss.reddeer.swt.impl.button.CheckBox;
import org.jboss.reddeer.swt.impl.text.LabeledText;
import org.jboss.tools.bpmn2.reddeer.DefaultCheckBox;

/**
 * 
 */
public class ProcessTab {

	/**
	 * 
	 * @param text
	 */
	public void setId(String text) {
		new LabeledText("Id").setText(text);
	}

	/**
	 * 
	 * @param text
	 */
	public void setName(String text) {
		new LabeledText("Name").setText(text);
	}

	/**
	 * 
	 * @param text
	 */
	public void setVersion(String text) {
		new LabeledText("Version").setText(text);
	}

	/**
	 * 
	 * @param text
	 */
	public void setPackageName(String text) {
		new LabeledText("Package Name").setText(text);
	}

	/**
	 * 
	 * @param value
	 */
	public void setAdHoc(boolean value) {
		new DefaultCheckBox("Ad Hoc").setChecked(value);
	}

	/**
	 * 
	 * @param value
	 */
	public void setExecutable(boolean value) {
		//new DefaultCheckBox("Is Executable").setChecked(value); TMP UNTIL REDDEER 0.7.0 WILL AVAILABLE
		CheckBox executable = new CheckBox(1);
		if((executable.isChecked() && !value) || (!executable.isChecked() && value)){
			executable.click();
		}
	}

}
