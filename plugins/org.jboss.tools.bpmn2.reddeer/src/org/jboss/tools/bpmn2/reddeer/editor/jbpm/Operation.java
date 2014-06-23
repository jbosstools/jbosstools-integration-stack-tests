package org.jboss.tools.bpmn2.reddeer.editor.jbpm;

import org.jboss.reddeer.swt.impl.button.PushButton;
import org.jboss.reddeer.swt.impl.text.LabeledText;
import org.jboss.tools.bpmn2.reddeer.editor.dialog.jbpm.ErrorDialog;
import org.jboss.tools.bpmn2.reddeer.editor.dialog.jbpm.MessageDialog;

/**
 * 
 */
public class Operation {

	private String name;
	private Message inMessage;
	private Message outMessage;
	private ErrorRef errorRef;

	/**
	 * 
	 * @param name
	 * @param inMessage
	 * @param outMessage
	 * @param errorRef
	 */
	public Operation(String name, Message inMessage, Message outMessage, ErrorRef errorRef) {
		this.name = name;
		this.inMessage = inMessage;
		this.outMessage = outMessage;
		this.errorRef = errorRef;
	}

	/**
	 * 
	 * @return
	 */
	public String getName() {
		return name;
	}

	/**
	 * 
	 * @return
	 */
	public Message getInMessage() {
		return inMessage;
	}

	/**
	 * 
	 * @return
	 */
	public Message getOutMessage() {
		return outMessage;
	}

	/**
	 * 
	 * @return
	 */
	public ErrorRef getErrorRef() {
		return errorRef;
	}

	/**
	 * Perform user actions which are required to set up this object
	 * in the UI.
	 */
	public void setUp() {
		new LabeledText("Name").setText(name);
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
	}
	
}
