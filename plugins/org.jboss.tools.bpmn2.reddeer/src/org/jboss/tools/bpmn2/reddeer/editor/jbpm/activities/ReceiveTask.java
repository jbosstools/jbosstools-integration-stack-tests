package org.jboss.tools.bpmn2.reddeer.editor.jbpm.activities;

import org.jboss.tools.bpmn2.reddeer.editor.Element;
import org.jboss.tools.bpmn2.reddeer.editor.ElementType;
import org.jboss.tools.bpmn2.reddeer.editor.jbpm.ErrorRef;
import org.jboss.tools.bpmn2.reddeer.editor.jbpm.Message;
import org.jboss.tools.bpmn2.reddeer.editor.jbpm.ParameterMapping;
import org.jboss.tools.bpmn2.reddeer.properties.shell.AddParameterMappingSetUpCTab;
import org.jboss.tools.bpmn2.reddeer.properties.shell.CheckBoxSetUpCTab;
import org.jboss.tools.bpmn2.reddeer.properties.shell.ComboSetUpCTab;
import org.jboss.tools.bpmn2.reddeer.properties.shell.OperationSetUp;
import org.jboss.tools.bpmn2.reddeer.properties.shell.ScriptSetUpCTab;

/**
 * 
 */
public class ReceiveTask extends Task {

	private static final String RECEIVE_TASK = "Receive Task";

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
		propertiesHandler.setUp(new ComboSetUpCTab(RECEIVE_TASK, "Implementation", implementationUri));
	}
	
	/**
	 * 
	 * @param operationContractName
	 * @param inMessage
	 * @param outMessage
	 * @param errorRef
	 */
	public void setOperation(String operationContractName, Message inMessage, Message outMessage, ErrorRef errorRef) {
		propertiesHandler.setUp(new OperationSetUp(RECEIVE_TASK, operationContractName, inMessage, outMessage, errorRef));
	}
	
	/**
	 * 
	 * @param name
	 * @param dataType
	 */
	public void setMessage(String name, String dataType) {
		propertiesHandler.setUp(new ComboSetUpCTab(RECEIVE_TASK, "Message", name + "(" + dataType + ")"));
	}
	
	/**
	 *
	 * @param value
	 */
	public void setIsForCompensation(boolean value) {
		propertiesHandler.setUp(new CheckBoxSetUpCTab(RECEIVE_TASK, "Is For Compensation", value));
	}

	/**
	 *
	 * @param language
	 * @param script
	 */
	public void setOnEntryScript(String language, String script) {
		propertiesHandler.setUp(new ScriptSetUpCTab(RECEIVE_TASK, "On Entry Script", language, script));
	}

	/**
	 * 
	 * @param language
	 * @param script
	 */
	public void setOnExistScript(String language, String script) {
		propertiesHandler.setUp(new ScriptSetUpCTab(RECEIVE_TASK, "On Exit Script", language, script));
	}

	/**
	 *
	 * @param parameter
	 */
	public void addParameterMapping(ParameterMapping parameterMapping) {
		propertiesHandler.setUp(new AddParameterMappingSetUpCTab(parameterMapping));
	}
	
	public void setTarget(String varName) {
		propertiesHandler.setUp(new ComboSetUpCTab(RECEIVE_TASK, "Target", varName));
	}

}
