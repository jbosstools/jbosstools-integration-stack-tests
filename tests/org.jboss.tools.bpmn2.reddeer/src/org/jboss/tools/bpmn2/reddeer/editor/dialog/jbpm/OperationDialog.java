package org.jboss.tools.bpmn2.reddeer.editor.dialog.jbpm;

import org.eclipse.swtbot.swt.finder.SWTBot;
import org.jboss.reddeer.swt.impl.button.PushButton;
import org.jboss.tools.bpmn2.reddeer.editor.jbpm.ErrorRef;
import org.jboss.tools.bpmn2.reddeer.editor.jbpm.Message;
import org.jboss.tools.bpmn2.reddeer.editor.jbpm.Operation;

/**
 * 
 */
public class OperationDialog {

	/**
	 * 
	 * @param operation
	 */
	public void add(Operation operation) {
		new SWTBot().shell("Create New Operation").activate();
		operation.setUp();
		new PushButton("OK").click();
	}
	
	/**
	 * 
	 * @param operationName
	 * @param inMessage
	 * @param outMessage
	 * @param errorRef
	 */
	public void addOperation(String name, Message inMessage, Message outMessage, ErrorRef errorRef) {
		add(new Operation(name, inMessage, outMessage, errorRef));
	}
	
}
