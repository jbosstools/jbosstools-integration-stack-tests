package org.jboss.tools.bpmn2.reddeer.properties.setup;

import org.eclipse.reddeer.swt.impl.table.DefaultTable;
import org.eclipse.reddeer.uiforms.impl.section.DefaultSection;
import org.jboss.tools.bpmn2.reddeer.editor.jbpm.ParameterMapping;
import org.jboss.tools.bpmn2.reddeer.editor.properties.PropertiesTabs;
import org.jboss.tools.bpmn2.reddeer.editor.properties.SectionToolItem;

public class RemoveParameterMappingSetUp implements SetUpAble {

	private ParameterMapping parameterMapping;

	public RemoveParameterMappingSetUp(ParameterMapping parameterMapping) {
		this.parameterMapping = parameterMapping;
	}

	@Override
	public void setUpCTab() {

		String sn = parameterMapping.getType() == ParameterMapping.Type.INPUT ? "Input" : "Output";
		DefaultSection s = new DefaultSection(sn + " Data Mapping");
		new DefaultTable(s).select(parameterMapping.getFrom().getName() + " " + parameterMapping.getTo().getName());
		new SectionToolItem(s.getText(), "Remove").click();

	}

	@Override
	public String getTabLabel() {
		return PropertiesTabs.IO_PARAMETERS_TAB;
	}

}
