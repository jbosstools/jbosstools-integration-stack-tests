package org.jboss.tools.bpmn2.reddeer.editor.dialog.jbpm;

import org.jboss.reddeer.swt.impl.button.PushButton;
import org.jboss.reddeer.swt.impl.tab.DefaultTabItem;
import org.jboss.reddeer.swt.impl.shell.DefaultShell;
import org.jboss.reddeer.swt.impl.text.LabeledText;

/**
 * 
 */
public class DataTypeDialog {

	/**
	 * 
	 * @param type
	 */
	public void add(String type) {
		new DefaultShell("Create New Data Type").setFocus();
		new DefaultTabItem("Data Type").activate();
		new LabeledText("Structure").setText(type);
		new PushButton("OK").click();
	}
	
}
