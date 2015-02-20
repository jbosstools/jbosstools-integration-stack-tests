package org.jboss.tools.bpmn2.reddeer.editor.jbpm.activities;

import org.jboss.tools.bpmn2.reddeer.editor.Element;
import org.jboss.tools.bpmn2.reddeer.editor.ElementType;
import org.jboss.tools.bpmn2.reddeer.editor.jbpm.ErrorRef;
import org.jboss.tools.bpmn2.reddeer.editor.jbpm.Message;
import org.jboss.tools.bpmn2.reddeer.editor.jbpm.ParameterMapping;
import org.jboss.tools.bpmn2.reddeer.properties.shell.AddParameterMappingSetUpCTab;
import org.jboss.tools.bpmn2.reddeer.properties.shell.CheckBoxSetUpCTab;
import org.jboss.tools.bpmn2.reddeer.properties.shell.ImplementationSetUp;
import org.jboss.tools.bpmn2.reddeer.properties.shell.OperationSetUp;

/**
 * 
 */
public class ServiceTask extends Task {

	private static final String SERVICE_TASK = "Service Task";

	/**
	 * 
	 * @param name
	 */
	public ServiceTask(String name) {
		super(name, ElementType.SERVICE_TASK);
	}
	
	public ServiceTask(Element element){
		super(element);
	}
	
	/**
	 * 
	 * @param implementationUri
	 */
	public void setImplementation(String implementationUri) {
		propertiesHandler.setUp(new ImplementationSetUp(SERVICE_TASK, implementationUri));
	}
	
	/**
	 * 
	 * @param operationContractName
	 * @param inMessage
	 * @param outMessage
	 * @param errorRef
	 */
	public void setOperation(String operationContractName, Message inMessage, Message outMessage, ErrorRef errorRef) {
		propertiesHandler.setUp(new OperationSetUp(SERVICE_TASK, operationContractName, inMessage, outMessage, errorRef));
	}

	/**
	 *
	 * @param value
	 */
	public void setIsForCompensation(boolean value) {
		propertiesHandler.setUp(new CheckBoxSetUpCTab(SERVICE_TASK, "Is For Compensation", value));
	}

	/**
	 *
	 * @param parameter
	 */
	public void addParameterMapping(ParameterMapping parameterMapping) {
		propertiesHandler.setUp(new AddParameterMappingSetUpCTab(parameterMapping));
	}

}
