package org.jboss.tools.bpmn2.reddeer.properties.setup;

import org.jboss.reddeer.swt.impl.combo.LabeledCombo;
import org.jboss.reddeer.swt.impl.table.DefaultTable;
import org.jboss.reddeer.swt.impl.text.LabeledText;
import org.jboss.reddeer.uiforms.impl.section.DefaultSection;
import org.jboss.tools.bpmn2.reddeer.editor.properties.PropertiesTabs;
import org.jboss.tools.bpmn2.reddeer.editor.properties.SectionToolItem;

public class LinkEventTargetSetUp implements SetUpAble {

	private String targetEventName;
	private int targetEventIndexInCombo;
	
	public LinkEventTargetSetUp(String targetEventName,int targetEventIndexInCombo) {
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
		return PropertiesTabs.EVENT_TAB;
	}

}
