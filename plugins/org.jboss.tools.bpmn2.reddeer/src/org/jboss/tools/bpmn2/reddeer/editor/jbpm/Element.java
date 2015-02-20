package org.jboss.tools.bpmn2.reddeer.editor.jbpm;

import org.jboss.tools.bpmn2.reddeer.editor.ElementType;
import org.jboss.tools.bpmn2.reddeer.properties.shell.AddParameterMappingSetUpCTab;
import org.jboss.tools.bpmn2.reddeer.properties.shell.RemoveParameterMappingSetUp;

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
	
	public Element(String name, ElementType type, org.jboss.tools.bpmn2.reddeer.editor.Element parent) {
		super(name, type, parent);
	}
	
	public Element(org.jboss.tools.bpmn2.reddeer.editor.Element element) {
		super(element);
	}
	
	/**
	 * 
	 * @param parameterMapping
	 */
	protected void addParameterMapping(ParameterMapping parameterMapping) {
		propertiesHandler.setUp(new AddParameterMappingSetUpCTab(parameterMapping));
	}
	
	/**
	 * 
	 * @param parameterMapping
	 */
	protected void removeParameterMapping(ParameterMapping parameterMapping) {
		propertiesHandler.setUp(new RemoveParameterMappingSetUp(parameterMapping));
	}
	
}
