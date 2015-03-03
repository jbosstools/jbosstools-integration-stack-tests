package org.jboss.tools.bpmn2.reddeer.properties.jbpm;

import java.util.Arrays;
import java.util.List;

import org.jboss.tools.bpmn2.reddeer.editor.jbpm.ParameterMapping;
import org.jboss.tools.reddeer.DefaultSection;

/**
 * 
 */
public class IOParametersTab {

	private List<ParameterMapping.Type> typeList;

	/**
	 * 
	 */
	public IOParametersTab() {
		this(ParameterMapping.Type.INPUT, ParameterMapping.Type.OUTPUT);
	}
	
	/**
	 * 
	 * @param types
	 */
	public IOParametersTab(ParameterMapping.Type ... types) {
		typeList = Arrays.asList(types);
	}
	
	/**
	 * 
	 * @param parameterMapping
	 */
	public void addParameter(ParameterMapping parameterMapping) {
		if (!typeList.contains(parameterMapping.getType())) {
			throw new UnsupportedOperationException();
		}
		
		String sn = getSectionType(parameterMapping.getType());
		DefaultSection s = new DefaultSection(sn + " Data Mapping");
		s.getToolbarButton("Add").click();

		parameterMapping.setUp();
		
		DefaultSection sd = new DefaultSection(sn + " Parameter Mapping Details");
		sd.getToolbarButton("Close").click();
	}
	
	/**
	 * 
	 * @param parameterMapping
	 */
	public void removeParameter(ParameterMapping parameterMapping) {
		if (!typeList.contains(parameterMapping.getType())) {
			throw new UnsupportedOperationException();
		}
		
		DefaultSection s = new DefaultSection(getSectionType(parameterMapping.getType()) + " Parameter Mapping");
		s.getTable().select(parameterMapping.getFrom().getName() + " " + parameterMapping.getTo().getName());
		s.getToolbarButton("Remove").click();
	}
	
	/**
	 * 
	 * @param parameterMappingType
	 * @return
	 */
	protected String getSectionType(ParameterMapping.Type parameterMappingType) {
		return parameterMappingType == ParameterMapping.Type.INPUT ? "Input" : "Output";
	}
	
}
