package org.jboss.tools.bpmn2.reddeer.editor.dialog.jbpm;

import org.jboss.reddeer.swt.impl.button.PushButton;
import org.jboss.reddeer.swt.impl.shell.DefaultShell;
import org.jboss.reddeer.swt.impl.table.DefaultTable;

/**
 * 
 */
public class InterfaceDialog {

	/**
	 * 
	 * @param implementationUri
	 */
	public void add(String implementationUri) {
		throw new UnsupportedOperationException();
	}
	
	/**
	 * 
	 * @param interfaceName
	 */
	public void select(String interfaceName) {
		new DefaultShell("Select an Interface for the new Operation").setFocus();
		new DefaultTable().select(interfaceName);
		new PushButton("OK").click();
	}
	
}
