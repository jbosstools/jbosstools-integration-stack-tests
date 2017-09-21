package org.jboss.tools.bpmn2.reddeer.editor.dialog.jbpm;

import org.eclipse.reddeer.common.wait.TimePeriod;
import org.eclipse.reddeer.common.wait.WaitWhile;
import org.eclipse.reddeer.swt.condition.ShellIsActive;
import org.eclipse.reddeer.swt.impl.button.PushButton;
import org.eclipse.reddeer.swt.impl.shell.DefaultShell;
import org.eclipse.reddeer.workbench.core.condition.JobIsRunning;
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
		new DefaultShell("Create New Operation");
		operation.setUp();
		new PushButton("OK").click();
		new WaitWhile(new ShellIsActive("Create New Operation"), TimePeriod.LONG);
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
