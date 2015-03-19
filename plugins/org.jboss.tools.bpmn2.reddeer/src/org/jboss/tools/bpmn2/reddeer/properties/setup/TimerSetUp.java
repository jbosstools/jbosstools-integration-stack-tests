package org.jboss.tools.bpmn2.reddeer.properties.setup;

import org.jboss.reddeer.swt.impl.button.RadioButton;
import org.jboss.reddeer.swt.impl.table.DefaultTable;
import org.jboss.reddeer.swt.impl.text.LabeledText;
import org.jboss.reddeer.uiforms.impl.section.DefaultSection;
import org.jboss.tools.bpmn2.reddeer.editor.jbpm.eventdefinitions.TimerType;
import org.jboss.tools.bpmn2.reddeer.editor.properties.PropertiesTabs;
import org.jboss.tools.bpmn2.reddeer.editor.properties.SectionToolItem;

public class TimerSetUp implements SetUpAble {
	
	private TimerType timerType;
	private String duration;
	
	public TimerSetUp(TimerType timerType, String duration) {
		this.timerType = timerType;
		this.duration = duration;
	}


	@Override
	public void setUpCTab() {
		DefaultSection section = new DefaultSection("Event Definitions");
		
		if (new DefaultTable(new DefaultSection("Event Definitions")).rowCount() == 1) {
			DefaultTable table = new DefaultTable(section);
			table.select(0);
			String eventOnTabType = table.getItem(0).getText(0);
			if (!"Timer".equals(eventOnTabType)) {
				throw new IllegalArgumentException("Not supported yet");
			} else {
				new SectionToolItem("Event Definitions", "Edit").click();
			}
		}
		new RadioButton(timerType.label()).click();
		new LabeledText("Value").setText(duration);
		new SectionToolItem("Timer Event Definition Details", "Close").click();
		
	}

	@Override
	public String getTabLabel() {
		return PropertiesTabs.EVENT_TAB;
	}
	
	

}
