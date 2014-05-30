package org.jboss.tools.bpmn2.reddeer.editor.dialog.jbpm;

import org.eclipse.swtbot.swt.finder.SWTBot;
import org.jboss.reddeer.swt.impl.button.PushButton;
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
		new SWTBot().shell("Create New Data Type").activate();
		new LabeledText("Structure").setText(type);
		new PushButton("OK").click();
	}
	
}
