package org.jboss.tools.bpmn2.reddeer.editor.dialog.jbpm;

import org.eclipse.reddeer.common.wait.WaitWhile;
import org.eclipse.reddeer.swt.condition.ShellIsActive;
import org.eclipse.reddeer.swt.impl.button.PushButton;
import org.eclipse.reddeer.swt.impl.shell.DefaultShell;
import org.eclipse.reddeer.swt.impl.tab.DefaultTabItem;
import org.jboss.tools.bpmn2.reddeer.editor.jbpm.Message;

/**
 * 
 */
public class MessageDialog {

	private static final String SHELL_LABEL = "Create New Message";

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
		new DefaultShell(SHELL_LABEL);
		new DefaultTabItem("Message").activate();
		message.setUp("Attributes");
		new PushButton("OK").click();
		new WaitWhile(new ShellIsActive(SHELL_LABEL));
	}

}
