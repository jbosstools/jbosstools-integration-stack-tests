package org.jboss.tools.bpmn2.reddeer.properties.shell;

import org.jboss.reddeer.swt.impl.table.DefaultTable;
import org.jboss.reddeer.swt.impl.text.LabeledText;
import org.jboss.tools.bpmn2.reddeer.editor.properties.SectionToolItem;

public class GatewayPrioritySetUpCTab extends AbstractSetUpCTab {

	private String priority;
	private String flow;
	
	public GatewayPrioritySetUpCTab(String flow, String priority) {
		this.flow = flow;
		this.priority = priority;
	}
	
	@Override
	public void setUpCTab() {
		new DefaultTable(0).select(flow);
		new SectionToolItem("Sequence Flow List", "Edit").click();
		new LabeledText("Priority").setText(priority);
		new SectionToolItem("Sequence Flow Details", "Close").click();

	}

	@Override
	public String getTabLabel() {
		return "Gateway";
	}

}
