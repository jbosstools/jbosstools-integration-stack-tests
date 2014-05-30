package org.jboss.tools.bpmn2.reddeer.editor.jbpm;

import org.jboss.tools.bpmn2.reddeer.editor.ElementType;
import org.jboss.tools.bpmn2.reddeer.properties.jbpm.IOParametersTab;

/**
 * 
 */
public class Element extends org.jboss.tools.bpmn2.reddeer.editor.Element {

	/**
	 * 
	 * @param name
	 * @param type
	 */
	public Element(String name, ElementType type) {
		super(name, type);
	}
	
	/**
	 * 
	 * @param parameterMapping
	 */
	protected void addParameterMapping(ParameterMapping parameterMapping) {
		properties.getTab("I/O Parameters", IOParametersTab.class).addParameter(parameterMapping);
	}
	
	/**
	 * 
	 * @param parameterMapping
	 */
	protected void removeParameterMapping(ParameterMapping parameterMapping) {
		properties.getTab("I/O Parameters", IOParametersTab.class).removeParameter(parameterMapping);
	}
	
}
