package org.jboss.tools.bpmn2.reddeer.editor.dialog.jbpm;

import org.jboss.reddeer.swt.impl.button.PushButton;
import org.jboss.reddeer.swt.impl.shell.DefaultShell;
import org.jboss.reddeer.swt.impl.text.DefaultText;

/**
 * 
 */
public class CalledActivityDialog {

	/**
	 * 
	 * @param callableActivityName
	 */
	public void add(String callableActivityName) {
		new DefaultShell("Called Element").setFocus();
		new DefaultText(0).setText(callableActivityName);
		new PushButton("OK").click();
	}
	
}
