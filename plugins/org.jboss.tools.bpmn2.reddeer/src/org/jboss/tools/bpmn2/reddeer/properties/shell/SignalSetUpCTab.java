package org.jboss.tools.bpmn2.reddeer.properties.shell;

import org.jboss.reddeer.swt.impl.button.PushButton;
import org.jboss.reddeer.swt.impl.table.DefaultTable;
import org.jboss.reddeer.uiforms.impl.section.DefaultSection;
import org.jboss.tools.bpmn2.reddeer.DefaultCombo;
import org.jboss.tools.bpmn2.reddeer.editor.dialog.jbpm.SignalDialog;
import org.jboss.tools.bpmn2.reddeer.editor.jbpm.Signal;
import org.jboss.tools.bpmn2.reddeer.editor.jbpm.eventdefinitions.MappingType;
import org.jboss.tools.bpmn2.reddeer.editor.properties.SectionToolItem;

public class SignalSetUpCTab implements SetUpAble {
	
	private Signal signal;
	private String variableName;
	private MappingType mappingType;
	
	public SignalSetUpCTab(Signal signal, String var, MappingType type) {
		this.signal = signal;
		this.variableName = var;
		this.mappingType = type;
	}

	@Override
	public void setUpCTab() {
		DefaultSection section = new DefaultSection("Event Definitions");
		if (new DefaultTable(new DefaultSection("Event Definitions")).rowCount() == 1) {
			DefaultTable table = new DefaultTable(section);
			table.select(0);
			String eventOnTabType = table.getItem(0).getText(0);
			if (!"Signal".equals(eventOnTabType)) {
				throw new IllegalStateException("Unsuported yet");
			} else {
				new SectionToolItem("Event Definitions", "Edit").click();
			}
		}
		
		section = new DefaultSection("Signal Event Definition Details");
		
		DefaultCombo combo = new DefaultCombo(section, "Signal");
		String comboItem = signal.getName();
		if (!combo.contains(comboItem)) {
			new PushButton(0).click();
			new SignalDialog().add(signal);
		}
		combo.setSelection(comboItem);
		
		new DefaultCombo(section, mappingType.label()).setSelection(variableName);
		new SectionToolItem("Signal Event Definition Details", "Close").click();
	}

	@Override
	public String getTabLabel() {
		return "Event";
	}

}
