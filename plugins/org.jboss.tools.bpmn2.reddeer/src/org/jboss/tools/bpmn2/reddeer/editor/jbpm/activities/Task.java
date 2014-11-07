package org.jboss.tools.bpmn2.reddeer.editor.jbpm.activities;

import org.jboss.tools.bpmn2.reddeer.editor.ElementType;
import org.jboss.tools.bpmn2.reddeer.editor.jbpm.Element;
import org.jboss.tools.bpmn2.reddeer.editor.jbpm.ParameterMapping;
import org.jboss.tools.bpmn2.reddeer.properties.jbpm.IOParametersTab;

/**
 * 
 */
public class Task extends Element {

	/**
	 * 
	 * @param name
	 */
	public Task(String name) {
		super(name, ElementType.TASK);
	}

	/**
	 * 
	 * @param name
	 * @param type
	 */
	Task(String name, ElementType type) {
		super(name, type);
	}
	
	Task(String name, ElementType type, org.jboss.tools.bpmn2.reddeer.editor.Element parent) {
		super(name, type, parent);
	}
	
	/**
	 * 
	 * @param parameterMapping
	 */
	public void addParameterMapping(ParameterMapping parameterMapping) {
		properties.getTab("I/O Parameters", IOParametersTab.class).addParameter(parameterMapping);
	}
	
	/**
	 * 
	 * @param parameterMapping
	 */
	public void removeParameterMapping(ParameterMapping parameterMapping) {
		properties.getTab("I/O Parameters", IOParametersTab.class).removeParameter(parameterMapping);
	}
	
	/**
	 * 
	 * @param name
	 * @param eventType
	 */
	public void addEvent(String name, ElementType eventType) {
		super.addEvent(name, eventType);
	}

}
