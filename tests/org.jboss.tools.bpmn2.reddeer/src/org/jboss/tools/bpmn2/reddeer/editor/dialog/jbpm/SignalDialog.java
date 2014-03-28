package org.jboss.tools.bpmn2.reddeer.editor.dialog.jbpm;

import org.eclipse.swtbot.swt.finder.SWTBot;
import org.jboss.reddeer.swt.impl.button.PushButton;
import org.jboss.reddeer.swt.impl.text.LabeledText;

/**
 * 
 */
public class SignalDialog {

	/**
	 * 
	 * @param signalName
	 */
	public void add(String signalName) {
		new SWTBot().shell("Create New Signal").activate();
		new LabeledText("Name").setText(signalName);
		new PushButton("OK").click();
	}
	
}
