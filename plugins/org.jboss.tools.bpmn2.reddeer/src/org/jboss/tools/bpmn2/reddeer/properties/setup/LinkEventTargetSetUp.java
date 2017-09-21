package org.jboss.tools.bpmn2.reddeer.properties.setup;

import org.eclipse.reddeer.swt.impl.combo.LabeledCombo;
import org.eclipse.reddeer.swt.impl.table.DefaultTable;
import org.eclipse.reddeer.swt.impl.text.LabeledText;
import org.eclipse.reddeer.uiforms.impl.section.DefaultSection;
import org.jboss.tools.bpmn2.reddeer.editor.properties.PropertiesTabs;
import org.jboss.tools.bpmn2.reddeer.editor.properties.SectionToolItem;

public class LinkEventTargetSetUp implements SetUpAble {

	private String targetEventName;
	private String targetElementEvent;

	public LinkEventTargetSetUp(String targetEventName, String targetElementEvent) {
		this.targetEventName = targetEventName;
		this.targetElementEvent = targetElementEvent;
	}

	@Override
	public void setUpCTab() {
		new DefaultTable(new DefaultSection("Event Definitions")).select(0);
		new SectionToolItem("Event Definitions", "Edit").click();
		new LabeledText("Name").setText(targetEventName);
		new LabeledCombo("Target").setSelection(targetElementEvent);

	}

	@Override
	public String getTabLabel() {
		return PropertiesTabs.EVENT_TAB;
	}

}
