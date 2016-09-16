package org.jboss.tools.bpmn2.reddeer.editor.dialog.jbpm;

import org.jboss.reddeer.swt.impl.button.PushButton;
import org.jboss.reddeer.swt.impl.shell.DefaultShell;
import org.jboss.reddeer.swt.impl.text.DefaultText;

public class EditNameDialog {
	private static final String SHELL_LABEL = "Edit Name";

	/**
	 * 
	 * @param type
	 */
	public void setName(String name) {
		new DefaultShell(SHELL_LABEL);
		new DefaultText().setText(name);
		new PushButton("OK").click();
	}

}
