package org.jboss.tools.bpmn2.reddeer.editor.jbpm.activities;

import org.jboss.tools.bpmn2.reddeer.editor.Element;
import org.jboss.tools.bpmn2.reddeer.editor.ElementType;
import org.jboss.tools.bpmn2.reddeer.editor.jbpm.ErrorRef;
import org.jboss.tools.bpmn2.reddeer.editor.jbpm.Message;
import org.jboss.tools.bpmn2.reddeer.editor.jbpm.ParameterMapping;
import org.jboss.tools.bpmn2.reddeer.editor.properties.PropertiesTabs;
import org.jboss.tools.bpmn2.reddeer.editor.properties.SectionToolItemButton;
import org.jboss.tools.bpmn2.reddeer.properties.setup.ParameterMappingSetUp;
import org.jboss.tools.bpmn2.reddeer.properties.setup.CheckBoxSetUp;
import org.jboss.tools.bpmn2.reddeer.properties.setup.ComboSetUp;
import org.jboss.tools.bpmn2.reddeer.properties.setup.OperationSetUp;
import org.jboss.tools.bpmn2.reddeer.properties.setup.ScriptSetUp;

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

	public SendTask(Element element) {
		super(element);
	}

	/**
	 * 
	 * @param implementationUri
	 */
	public void setImplementation(String implementationUri) {
		propertiesHandler.setUp(new ComboSetUp(PropertiesTabs.SEND_TASK_TAB, "Implementation", implementationUri));
	}

	/**
	 * 
	 * @param operation
	 */
	public void setOperation(String operationContractName, Message inMessage, Message outMessage, ErrorRef errorRef) {
		propertiesHandler.setUp(new OperationSetUp(PropertiesTabs.SEND_TASK_TAB, operationContractName, inMessage,
				outMessage, errorRef));
	}

	/**
	 * 
	 * @param name
	 * @param dataType
	 */
	public void setMessage(String name, String dataType) {
		propertiesHandler.setUp(new ComboSetUp(PropertiesTabs.SEND_TASK_TAB, "Message", name + "(" + dataType + ")"));
	}

	/**
	 *
	 * @param value
	 */
	public void setIsForCompensation(boolean value) {
		propertiesHandler.setUp(new CheckBoxSetUp(PropertiesTabs.SEND_TASK_TAB, "Is For Compensation", value));
	}

	/**
	 *
	 * @param language
	 * @param script
	 */
	public void setOnEntryScript(String language, String script) {
		propertiesHandler.setUp(new ScriptSetUp(PropertiesTabs.SEND_TASK_TAB, "On Entry Script", language, script));
	}

	/**
	 * 
	 * @param language
	 * @param script
	 */
	public void setOnExistScript(String language, String script) {
		propertiesHandler.setUp(new ScriptSetUp(PropertiesTabs.SEND_TASK_TAB, "On Exit Script", language, script));
	}

	/**
	 *
	 * @param parameter
	 */
	public void addParameterMapping(ParameterMapping parameterMapping) {
		propertiesHandler.setUp(new ParameterMappingSetUp(parameterMapping, SectionToolItemButton.ADD));
	}
	
	public void setOutgoingMessageMappingFromVariable(String variableName) {
		propertiesHandler.setUp(new ComboSetUp(PropertiesTabs.SEND_TASK_TAB, "Source", variableName));
	}

}
