package org.jboss.tools.bpmn2.reddeer.editor.dialog.jbpm;

import org.jboss.reddeer.swt.impl.button.PushButton;
import org.jboss.reddeer.swt.impl.shell.DefaultShell;
import org.jboss.reddeer.swt.impl.tab.DefaultTabItem;
import org.jboss.reddeer.swt.impl.text.LabeledText;
import org.jboss.tools.bpmn2.reddeer.editor.jbpm.ErrorRef;

/**
 * 
 */
public class ErrorDialog {

	/**
	 * 
	 * @param name
	 * @param code
	 * @param dataType
	 */
	public void addErrorRef(String name, String code, String dataType) {
		add(new ErrorRef(name, code, dataType));
	}
	
	/**
	 * 
	 * @param errorRef
	 */
	public void add(ErrorRef errorRef) {
		new DefaultShell("Create New Error").setFocus();
		new LabeledText("Name").setText(errorRef.getName());
		new DefaultTabItem("Error").activate();
		errorRef.setUp();		
		new PushButton("OK").click();
	}
	
}
