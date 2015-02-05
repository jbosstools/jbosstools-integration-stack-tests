package org.jboss.tools.bpmn2.reddeer.editor.jbpm.throwevents;

import org.jboss.reddeer.swt.impl.combo.LabeledCombo;
import org.jboss.reddeer.swt.impl.table.DefaultTable;
import org.jboss.reddeer.uiforms.impl.section.DefaultSection;
import org.jboss.tools.bpmn2.reddeer.editor.properties.SectionToolItem;
import org.jboss.tools.bpmn2.reddeer.properties.shell.AbstractSetUpCTab;

public class CompensationActivitySetUpCTab extends AbstractSetUpCTab {

	private String activityName;
	
	public CompensationActivitySetUpCTab(String activityName) {
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
		return "Event";
	}

}
