package org.jboss.tools.bpmn2.reddeer.properties.shell;

import org.jboss.tools.bpmn2.reddeer.editor.jbpm.ParameterMapping;
import org.jboss.tools.bpmn2.reddeer.editor.properties.SectionToolItem;


public class AddParameterMappingSetUpCTab extends AbstractSetUpCTab {

	private ParameterMapping parameterMapping;
	
	public AddParameterMappingSetUpCTab(ParameterMapping parameterMapping) {
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
		return "I/O Parameters";
	}

}
