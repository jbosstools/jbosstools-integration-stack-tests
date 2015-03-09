package org.jboss.tools.bpmn2.reddeer.editor.dialog.jbpm;

import org.jboss.reddeer.swt.impl.button.PushButton;
import org.jboss.reddeer.swt.impl.shell.DefaultShell;
import org.jboss.reddeer.swt.impl.text.LabeledText;
import org.jboss.tools.reddeer.DefaultSection;

/**
 * 
 */
public class SignalDialog {

	/**
	 * 
	 * @param signalName
	 */
	public void add(String signalName) {
		new DefaultShell("Create New Signal").setFocus();
		new LabeledText("Name").setText(signalName);
		new PushButton("OK").click();
	}
	
}
