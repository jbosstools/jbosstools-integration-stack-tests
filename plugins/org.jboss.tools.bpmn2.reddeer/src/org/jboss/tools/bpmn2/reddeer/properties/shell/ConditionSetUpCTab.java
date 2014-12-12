package org.jboss.tools.bpmn2.reddeer.properties.shell;

import org.jboss.reddeer.swt.impl.combo.LabeledCombo;
import org.jboss.reddeer.swt.impl.table.DefaultTable;
import org.jboss.reddeer.swt.impl.text.LabeledText;
import org.jboss.reddeer.uiforms.impl.section.DefaultSection;
import org.jboss.tools.bpmn2.reddeer.editor.properties.SectionToolItem;

public class ConditionSetUpCTab extends AbstractSetUpCTab {

	private String language;
	private String condition;
	
	public ConditionSetUpCTab(String language, String condition) {
		this.language = language;
		this.condition = condition;
	}
	
	@Override
	public void setUpCTab() {
		DefaultSection section = new DefaultSection("Event Definitions");
		if (new DefaultTable(new DefaultSection("Event Definitions")).rowCount() == 1) {
			DefaultTable table = new DefaultTable(section);
			table.select(0);
			String eventOnTabType = table.getItem(0).getText(0);
			if (!"Conditional".equals(eventOnTabType)) {
				throw new IllegalArgumentException("Not supported yet");
			} else {
				new SectionToolItem("Event Definitions", "Edit").click();
			}
		}
		
		new LabeledCombo( "Script Language").setSelection(language);
		new LabeledText( "Script").setText(condition);
		new SectionToolItem("Conditional Event Definition Details", "Close").click();
	}

	@Override
	public String getTabLabel() {
		return "Event";
	}

}
