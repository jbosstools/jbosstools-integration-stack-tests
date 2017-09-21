package org.jboss.tools.bpmn2.reddeer.editor.dialog.jbpm;

import org.eclipse.reddeer.common.wait.WaitWhile;
import org.eclipse.reddeer.swt.condition.ShellIsActive;
import org.eclipse.reddeer.swt.impl.button.PushButton;
import org.eclipse.reddeer.swt.impl.shell.DefaultShell;
import org.eclipse.reddeer.swt.impl.tab.DefaultTabItem;

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
		new PushButton(0).click();
		new ImportJavaTypeDialog().add(type);
		new PushButton("OK").click();
		new WaitWhile(new ShellIsActive(SHELL_LABEL));
	}

}
