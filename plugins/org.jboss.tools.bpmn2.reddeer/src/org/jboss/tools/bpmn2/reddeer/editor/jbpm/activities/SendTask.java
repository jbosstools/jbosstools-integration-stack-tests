package org.jboss.tools.bpmn2.reddeer.editor.jbpm.activities;

import org.jboss.tools.bpmn2.reddeer.editor.ElementType;
import org.jboss.tools.bpmn2.reddeer.editor.jbpm.ErrorRef;
import org.jboss.tools.bpmn2.reddeer.editor.jbpm.Expression;
import org.jboss.tools.bpmn2.reddeer.editor.jbpm.Message;
import org.jboss.tools.bpmn2.reddeer.editor.jbpm.ParameterMapping;
import org.jboss.tools.bpmn2.reddeer.properties.jbpm.IOParametersTab;
import org.jboss.tools.bpmn2.reddeer.properties.jbpm.SendTaskTab;

/**
 * 
 */
public class SendTask extends Task {

	/**
	 * 
	 * @param name
	 */
	public SendTask(String name) {
		super(name, ElementType.SEND_TASK);
	}
	
	/**
	 * 
	 * @param implementationUri
	 */
	public void setImplementation(String implementationUri) {
		properties.getTab("Send Task", SendTaskTab.class).setImplementation(implementationUri);
	}
	
	public void setOperation(String operationContractName) {
		setOperation(operationContractName, null, null, null);
	}
	
	/**
	 * 
	 * @param operation
	 */
	public void setOperation(String operationContractName, Message inMessage, Message outMessage, ErrorRef errorRef) {
		properties.getTab("Send Task", SendTaskTab.class).setOperation(operationContractName, inMessage, outMessage, errorRef);
	}
	
	/**
	 * 
	 * @param name
	 * @param dataType
	 */
	public void setMessage(Message message) {
		properties.getTab("Send Task", SendTaskTab.class).setMessage(message);
	}

	/**
	 *
	 * @param value
	 */
	public void setIsForCompensation(boolean value) {
		properties.getTab("Send Task", SendTaskTab.class).setIsForCompensation(value);
	}

	/**
	 *
	 * @param language
	 * @param script
	 */
	public void setOnEntryScript(String language, String script) {
		properties.getTab("Send Task", SendTaskTab.class).setOnEntryScript(new Expression(language, script));
	}

	/**
	 * 
	 * @param language
	 * @param script
	 */
	public void setOnExistScript(String language, String script) {
		properties.getTab("Send Task", SendTaskTab.class).setOnExitScript(new Expression(language, script));
	}

	/**
	 *
	 * @param parameter
	 */
	public void addParameterMapping(ParameterMapping parameterMapping) {
		properties.getTab("I/O Parameters", IOParametersTab.class).addParameter(parameterMapping);
	}

}
