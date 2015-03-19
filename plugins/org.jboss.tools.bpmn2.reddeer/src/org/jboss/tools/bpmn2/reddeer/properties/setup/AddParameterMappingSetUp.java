package org.jboss.tools.bpmn2.reddeer.properties.setup;

import org.jboss.tools.bpmn2.reddeer.editor.jbpm.ParameterMapping;
import org.jboss.tools.bpmn2.reddeer.editor.properties.PropertiesTabs;
import org.jboss.tools.bpmn2.reddeer.editor.properties.SectionToolItem;


public class AddParameterMappingSetUp implements SetUpAble {

	private ParameterMapping parameterMapping;
	
	public AddParameterMappingSetUp(ParameterMapping parameterMapping) {
		this.parameterMapping = parameterMapping;
	}
	
	@Override
	public void setUpCTab() {
		 
		String sn = parameterMapping.getType() == ParameterMapping.Type.INPUT ? "Input" : "Output";
		new SectionToolItem(sn + " Data Mapping", "Add").click();

		parameterMapping.setUp();
		
		new SectionToolItem(sn + " Data Mapping Details", "Close").click();
	}

	@Override
	public String getTabLabel() {
		return PropertiesTabs.IO_PARAMETERS_TAB;
	}

}
