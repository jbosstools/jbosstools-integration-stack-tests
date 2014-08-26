package org.jboss.tools.bpmn2.reddeer.editor.dialog.jbpm;


import org.jboss.reddeer.swt.condition.JobIsRunning;
import org.jboss.reddeer.swt.condition.ShellWithTextIsActive;
import org.jboss.reddeer.swt.impl.button.PushButton;
import org.jboss.reddeer.swt.impl.shell.DefaultShell;
import org.jboss.reddeer.swt.wait.TimePeriod;
import org.jboss.reddeer.swt.wait.WaitWhile;
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
		new DefaultShell("Create New Operation").setFocus();
		operation.setUp();
		new PushButton("OK").click();
		new WaitWhile(new ShellWithTextIsActive("Create New Operation"),TimePeriod.LONG);
		new WaitWhile(new JobIsRunning(), TimePeriod.LONG);
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
