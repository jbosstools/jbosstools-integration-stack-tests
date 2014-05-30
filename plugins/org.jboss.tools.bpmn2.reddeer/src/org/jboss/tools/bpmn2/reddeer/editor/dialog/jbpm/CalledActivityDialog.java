package org.jboss.tools.bpmn2.reddeer.editor.dialog.jbpm;

import org.eclipse.swtbot.swt.finder.SWTBot;
import org.jboss.reddeer.swt.impl.button.PushButton;
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
		new SWTBot().shell("Called Activity").activate();
		new DefaultText(0).setText(callableActivityName);
		new PushButton("OK").click();
	}
	
}
