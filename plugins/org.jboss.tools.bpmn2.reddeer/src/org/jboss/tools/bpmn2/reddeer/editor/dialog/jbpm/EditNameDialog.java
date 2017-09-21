package org.jboss.tools.bpmn2.reddeer.editor.dialog.jbpm;

import static org.junit.Assert.assertFalse;

import org.eclipse.reddeer.swt.impl.button.PushButton;
import org.eclipse.reddeer.swt.impl.shell.DefaultShell;
import org.eclipse.reddeer.swt.impl.text.DefaultText;

public class EditNameDialog {
	private static final String SHELL_LABEL = "Edit Name";
	
	private boolean isNameReserved;
	
	public EditNameDialog() {
		this(false);
	}

	public EditNameDialog(boolean isNameReserved) {
		this.isNameReserved = isNameReserved;
	}
	
	/**
	 * 
	 * @param type
	 */
	public void setName(String name) {
		new DefaultShell(SHELL_LABEL);
		new DefaultText().setText(name);
		PushButton okButton = new PushButton("OK");
		
		if(isNameReserved) {
			assertFalse("'OK' button should be disabled", okButton.isEnabled());
			new PushButton("Cancel").click();
		} else {
			new PushButton("OK").click();
		}
	}

}
