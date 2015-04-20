package org.jboss.tools.bpmn2.reddeer.properties.setup;

import org.jboss.reddeer.swt.impl.table.DefaultTable;
import org.jboss.tools.bpmn2.reddeer.DefaultSection;
import org.jboss.tools.bpmn2.reddeer.editor.jbpm.ParameterMapping;
import org.jboss.tools.bpmn2.reddeer.editor.properties.PropertiesTabs;
import org.jboss.tools.bpmn2.reddeer.editor.properties.SectionToolItem;
import org.jboss.tools.bpmn2.reddeer.editor.properties.SectionToolItemButton;


public class ParameterMappingSetUp implements SetUpAble {

	private static final String DATA_MAPPING = " Data Mapping";
    private ParameterMapping parameterMapping;
	private SectionToolItemButton button;
	
	public ParameterMappingSetUp(ParameterMapping parameterMapping, SectionToolItemButton button) {
		this.parameterMapping = parameterMapping;
		this.button = button;
	}
	
	@Override
	public void setUpCTab() {
		 
		String sn = parameterMapping.getType() == ParameterMapping.Type.INPUT ? "Input" : "Output";
		if(button == SectionToolItemButton.EDIT) {
		    new DefaultTable(new DefaultSection(sn + DATA_MAPPING)).select(0);
		}
		
		new SectionToolItem(sn + DATA_MAPPING, button.toString()).click();

		parameterMapping.setUp();
		
		new SectionToolItem(sn + DATA_MAPPING + " Details", "Close").click();
	}

	@Override
	public String getTabLabel() {
		return PropertiesTabs.IO_PARAMETERS_TAB;
	}

}
