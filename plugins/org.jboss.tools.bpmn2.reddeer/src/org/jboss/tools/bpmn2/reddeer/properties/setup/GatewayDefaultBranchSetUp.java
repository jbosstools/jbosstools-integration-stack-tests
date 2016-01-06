package org.jboss.tools.bpmn2.reddeer.properties.setup;

import org.jboss.reddeer.swt.impl.button.CheckBox;
import org.jboss.reddeer.swt.impl.table.DefaultTable;
import org.jboss.tools.bpmn2.reddeer.editor.properties.PropertiesTabs;
import org.jboss.tools.bpmn2.reddeer.editor.properties.SectionToolItem;

public class GatewayDefaultBranchSetUp implements SetUpAble {

	private String flow;

	public GatewayDefaultBranchSetUp(String flow) {
		this.flow = flow;
	}

	@Override
	public void setUpCTab() {
		new DefaultTable(0).select(flow);
		new SectionToolItem("Sequence Flow List", "Edit").click();
		new CheckBox().toggle(true);
		new SectionToolItem("Sequence Flow Details", "Close").click();

	}

	@Override
	public String getTabLabel() {
		return PropertiesTabs.GATEWAY_TAB;
	}

}
