package org.jboss.tools.bpmn2.reddeer.editor;

import org.jboss.tools.bpmn2.reddeer.editor.jbpm.ParameterMapping;
import org.jboss.tools.bpmn2.reddeer.properties.jbpm.IOParametersTab;

/**
 * 
 */
public abstract class AbstractConstruct extends Construct {

	/**
	 * 
	 * @param name
	 * @param type
	 */
	public AbstractConstruct(String name, ConstructType type) {
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
