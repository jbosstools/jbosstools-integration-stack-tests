package org.jboss.tools.bpmn2.reddeer.editor.jbpm.activities;

import org.jboss.tools.bpmn2.reddeer.editor.Element;
import org.jboss.tools.bpmn2.reddeer.editor.ElementType;
import org.jboss.tools.bpmn2.reddeer.editor.jbpm.ErrorRef;
import org.jboss.tools.bpmn2.reddeer.editor.jbpm.Expression;
import org.jboss.tools.bpmn2.reddeer.editor.jbpm.Message;
import org.jboss.tools.bpmn2.reddeer.editor.jbpm.ParameterMapping;
import org.jboss.tools.bpmn2.reddeer.properties.jbpm.IOParametersTab;
import org.jboss.tools.bpmn2.reddeer.properties.jbpm.ReceiveTaskTab;

/**
 * 
 */
public class ReceiveTask extends Task {

	/**
	 * 
	 * @param name
	 */
	public ReceiveTask(String name) {
		super(name, ElementType.RECEIVE_TASK);
	}
	
	public ReceiveTask(Element element) {
		super(element);
	}
	
	/**
	 * 
	 * @param implementationUri
	 */
	public void setImplementation(String implementationUri) {
		properties.getTab("Receive Task", ReceiveTaskTab.class).setImplementation(implementationUri);
	}
	
	/**
	 * 
	 * @param operationContractName
	 * @param inMessage
	 * @param outMessage
	 * @param errorRef
	 */
	public void setOperation(String operationContractName, Message inMessage, Message outMessage, ErrorRef errorRef) {
		properties.getTab("Receive Task", ReceiveTaskTab.class).setOperation(operationContractName, inMessage, outMessage, errorRef);
	}
	
	/**
	 * 
	 * @param name
	 * @param dataType
	 */
	public void setMessage(String name, String dataType) {
		properties.getTab("Receive Task", ReceiveTaskTab.class).setMessage(new Message(name, dataType));
	}
	
	/**
	 *
	 * @param value
	 */
	public void setIsForCompensation(boolean value) {
		properties.getTab("Receive Task", ReceiveTaskTab.class).setIsForCompensation(value);
	}

	/**
	 *
	 * @param language
	 * @param script
	 */
	public void setOnEntryScript(String language, String script) {
		properties.getTab("Receive Task", ReceiveTaskTab.class).setOnEntryScript(new Expression(language, script));
	}

	/**
	 * 
	 * @param language
	 * @param script
	 */
	public void setOnExistScript(String language, String script) {
		properties.getTab("Receive Task", ReceiveTaskTab.class).setOnExitScript(new Expression(language, script));
	}

	/**
	 *
	 * @param parameter
	 */
	public void addParameterMapping(ParameterMapping parameterMapping) {
		properties.getTab("I/O Parameters", IOParametersTab.class).addParameter(parameterMapping);
	}
	
	public void setTarget(String varName) {
		properties.getTab("Receive Task", ReceiveTaskTab.class).setTarget(varName);
	}

}
