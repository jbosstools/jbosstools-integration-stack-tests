package org.jboss.tools.bpmn2.reddeer.properties.shell;

import org.jboss.reddeer.swt.impl.table.DefaultTable;
import org.jboss.tools.bpmn2.reddeer.DefaultCheckBox;
import org.jboss.tools.bpmn2.reddeer.editor.properties.SectionToolItem;

public class GatewayDefaultBranch implements SetUpAble {

	private String flow;
	
	public GatewayDefaultBranch(String flow) {
		this.flow = flow;
	}
	
	@Override
	public void setUpCTab() {
		new DefaultTable(0).select(flow);
		new SectionToolItem("Sequence Flow List", "Edit").click();
		new DefaultCheckBox().setChecked(true);
		new SectionToolItem("Sequence Flow Details", "Close").click();

	}

	@Override
	public String getTabLabel() {
		return "Gateway";
	}

}
