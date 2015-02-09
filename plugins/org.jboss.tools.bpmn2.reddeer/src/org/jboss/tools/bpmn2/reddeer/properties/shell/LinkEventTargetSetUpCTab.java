package org.jboss.tools.bpmn2.reddeer.properties.shell;

import org.jboss.reddeer.swt.impl.combo.LabeledCombo;
import org.jboss.reddeer.swt.impl.table.DefaultTable;
import org.jboss.reddeer.swt.impl.text.LabeledText;
import org.jboss.reddeer.uiforms.impl.section.DefaultSection;
import org.jboss.tools.bpmn2.reddeer.editor.properties.SectionToolItem;

public class LinkEventTargetSetUpCTab extends AbstractSetUpCTab {

	private String targetEventName;
	private int targetEventIndexInCombo;
	
	public LinkEventTargetSetUpCTab(String targetEventName,int targetEventIndexInCombo) {
		this.targetEventName = targetEventName;
		this.targetEventIndexInCombo = targetEventIndexInCombo;
	}
	
	@Override
	public void setUpCTab() {
		new DefaultTable(new DefaultSection("Event Definitions")).select(0);
		new SectionToolItem("Event Definitions", "Edit").click();
		new LabeledText("Name").setText(targetEventName);
		new LabeledCombo("Target").setSelection(targetEventIndexInCombo);

	}

	@Override
	public String getTabLabel() {
		return "Event";
	}

}
