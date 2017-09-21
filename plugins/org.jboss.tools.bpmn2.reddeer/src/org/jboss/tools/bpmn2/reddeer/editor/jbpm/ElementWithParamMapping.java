package org.jboss.tools.bpmn2.reddeer.editor.jbpm;

import org.eclipse.reddeer.swt.impl.table.DefaultTable;
import org.jboss.tools.bpmn2.reddeer.DefaultSection;
import org.jboss.tools.bpmn2.reddeer.editor.Element;
import org.jboss.tools.bpmn2.reddeer.editor.ElementType;
import org.jboss.tools.bpmn2.reddeer.editor.properties.PropertiesTabs;
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
	
	public int getInputParameterMappingCount() {
		propertiesHandler.selectTabInPropertiesView(PropertiesTabs.IO_PARAMETERS_TAB);
		return new DefaultTable(new DefaultSection("Input Data Mapping")).getItems().size();
	}
	
	public int getOutputParameterMappingCount() {
		propertiesHandler.selectTabInPropertiesView(PropertiesTabs.IO_PARAMETERS_TAB);
		return new DefaultTable(new DefaultSection("Output Data Mapping")).getItems().size();
	}

}
