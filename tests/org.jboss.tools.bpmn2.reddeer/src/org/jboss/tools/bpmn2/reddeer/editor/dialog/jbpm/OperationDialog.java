package org.jboss.tools.bpmn2.reddeer.editor.dialog.jbpm;

import org.eclipse.swtbot.swt.finder.SWTBot;
import org.jboss.reddeer.swt.impl.button.PushButton;
import org.jboss.reddeer.swt.impl.text.LabeledText;
import org.jboss.tools.bpmn2.reddeer.editor.jbpm.ErrorRef;
import org.jboss.tools.bpmn2.reddeer.editor.jbpm.Message;

/**
 * 
 */
public class OperationDialog {

	/**
	 * 
	 * @param operationName
	 * @param inMessage
	 * @param outMessage
	 * @param errorRef
	 */
	public void add(String operationName, Message inMessage, Message outMessage, ErrorRef errorRef) {
		new SWTBot().shell("Create New Operation").activate();
		new LabeledText("Name").setText(operationName);
		
		if (inMessage != null) {
			new PushButton(0).click();
			new MessageDialog().add(inMessage);
		}
		
		if (outMessage != null) {
			new PushButton(2).click();
			new MessageDialog().add(outMessage);
		}
		
		if (errorRef != null) {
			new PushButton(4).click();
			new ErrorDialog().add(errorRef);
		}
		
		new PushButton("OK").click();
	}
	
}
