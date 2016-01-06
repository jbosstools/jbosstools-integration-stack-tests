package org.jboss.tools.bpmn2.reddeer.properties.setup;

import org.jboss.reddeer.swt.impl.combo.LabeledCombo;
import org.jboss.reddeer.swt.impl.table.DefaultTable;
import org.jboss.reddeer.uiforms.impl.section.DefaultSection;
import org.jboss.tools.bpmn2.reddeer.editor.properties.PropertiesTabs;
import org.jboss.tools.bpmn2.reddeer.editor.properties.SectionToolItem;

public class CompensationActivitySetUp implements SetUpAble {

	private String activityName;

	public CompensationActivitySetUp(String activityName) {
		this.activityName = activityName;
	}

	@Override
	public void setUpCTab() {
		new DefaultTable(new DefaultSection("Event Definitions")).select(0);
		new SectionToolItem("Event Definitions", "Edit").click();
		new LabeledCombo("Activity").setSelection(activityName);

	}

	@Override
	public String getTabLabel() {
		return PropertiesTabs.EVENT_TAB;
	}

}
