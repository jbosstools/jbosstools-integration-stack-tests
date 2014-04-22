package org.jboss.tools.bpmn2.reddeer.editor.dialog.jbpm;

import org.jboss.reddeer.swt.impl.button.PushButton;
import org.jboss.reddeer.swt.impl.shell.DefaultShell;
import org.jboss.tools.bpmn2.reddeer.editor.jbpm.Message;

/**
 * 
 */
public class MessageDialog {

	/**
	 * 
	 * @param name
	 * @param dataType
	 */
	public void addMessage(String name, String dataType) {
		add(new Message(name, dataType));
	}
	
	/**
	 * 
	 * @param message
	 */
	public void add(Message message) {
		new DefaultShell("Create New Message").setFocus();
		message.setUp();
		new PushButton("OK").click();
	}
	
}
