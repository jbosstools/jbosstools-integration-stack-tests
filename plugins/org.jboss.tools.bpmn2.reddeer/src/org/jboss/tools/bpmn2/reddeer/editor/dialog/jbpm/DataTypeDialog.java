package org.jboss.tools.bpmn2.reddeer.editor.dialog.jbpm;

import org.jboss.reddeer.core.condition.ShellWithTextIsActive;
import org.jboss.reddeer.swt.impl.button.PushButton;
import org.jboss.reddeer.swt.impl.shell.DefaultShell;
import org.jboss.reddeer.swt.impl.tab.DefaultTabItem;
import org.jboss.reddeer.swt.impl.text.LabeledText;
import org.jboss.reddeer.common.wait.WaitWhile;

/**
 * 
 */
public class DataTypeDialog {

	private static final String SHELL_LABEL = "Create New Data Type";

	/**
	 * 
	 * @param type
	 */
	public void add(String type) {
		new DefaultShell(SHELL_LABEL);
		new DefaultTabItem("Data Type").activate();
		new LabeledText("Structure").setText(type);
		new PushButton("OK").click();
		new WaitWhile(new ShellWithTextIsActive(SHELL_LABEL));
	}

}
