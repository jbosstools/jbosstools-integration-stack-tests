package org.jboss.tools.bpmn2.reddeer.editor.jbpm;

import org.jboss.tools.bpmn2.reddeer.editor.Element;
import org.jboss.tools.bpmn2.reddeer.editor.ElementType;
import org.jboss.tools.bpmn2.reddeer.editor.properties.SectionToolItemButton;
import org.jboss.tools.bpmn2.reddeer.properties.setup.ParameterMappingSetUp;
import org.jboss.tools.bpmn2.reddeer.properties.setup.RemoveParameterMappingSetUp;

/**
 * 
 */
public class ElementWithParamMapping extends Element {

	/**
	 * 
	 * @param name
	 * @param type
	 */
	public ElementWithParamMapping(String name, ElementType type) {
		super(name, type);
	}

	public ElementWithParamMapping(String name, ElementType type, org.jboss.tools.bpmn2.reddeer.editor.Element parent) {
		super(name, type, parent);
	}

	public ElementWithParamMapping(org.jboss.tools.bpmn2.reddeer.editor.Element element) {
		super(element);
	}

	/**
	 * 
	 * @param parameterMapping
	 */
	protected void addParameterMapping(ParameterMapping parameterMapping) {
		propertiesHandler.setUp(new ParameterMappingSetUp(parameterMapping, SectionToolItemButton.ADD));
	}

	/**
	 * 
	 * @param parameterMapping
	 */
	protected void removeParameterMapping(ParameterMapping parameterMapping) {
		propertiesHandler.setUp(new RemoveParameterMappingSetUp(parameterMapping));
	}

}
