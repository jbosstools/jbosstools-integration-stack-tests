package org.jboss.tools.bpmn2.reddeer.properties.jbpm;

import java.util.Arrays;
import java.util.List;

import org.jboss.reddeer.swt.impl.table.DefaultTable;
import org.jboss.reddeer.uiforms.impl.section.DefaultSection;
import org.jboss.tools.bpmn2.reddeer.editor.jbpm.ParameterMapping;
import org.jboss.tools.bpmn2.reddeer.editor.properties.SectionToolItem;

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
		new SectionToolItem(sn + " Data Mapping", "Add").click();

		parameterMapping.setUp();
		
		new SectionToolItem(sn + " Data Mapping Details", "Close").click();
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
		new DefaultTable(s).select(parameterMapping.getFrom().getName() + " " + parameterMapping.getTo().getName());
		new SectionToolItem(s.getText(), "Remove").click();
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
