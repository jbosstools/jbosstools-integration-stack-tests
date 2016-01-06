package org.jboss.tools.bpmn2.reddeer.editor.dialog.jbpm;

import org.jboss.reddeer.core.condition.ShellWithTextIsActive;
import org.jboss.reddeer.swt.impl.button.PushButton;
import org.jboss.reddeer.swt.impl.shell.DefaultShell;
import org.jboss.reddeer.swt.impl.tab.DefaultTabItem;
import org.jboss.reddeer.common.wait.WaitWhile;
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
		new WaitWhile(new ShellWithTextIsActive(SHELL_LABEL));
	}

}
