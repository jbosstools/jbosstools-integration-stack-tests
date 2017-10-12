package org.jboss.tools.bpmn2.reddeer.editor.dialog.jbpm;

import org.eclipse.reddeer.swt.impl.button.PushButton;
import org.eclipse.reddeer.swt.impl.shell.DefaultShell;
import org.eclipse.reddeer.swt.impl.text.DefaultText;

/**
 * 
 */
public class ImplementationDialog {

	/**
	 * 
	 * @param implementationUri
	 */
	public void add(String implementationUri) {
		new DefaultShell("Create New Service Implementation").setFocus();
		new DefaultText(0).setText(implementationUri);
		new PushButton("OK").click();
	}

}
