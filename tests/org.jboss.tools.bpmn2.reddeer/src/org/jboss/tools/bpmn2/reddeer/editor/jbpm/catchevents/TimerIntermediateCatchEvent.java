package org.jboss.tools.bpmn2.reddeer.editor.jbpm.catchevents;

import org.jboss.reddeer.swt.impl.table.DefaultTable;
import org.jboss.tools.bpmn2.reddeer.editor.ConstructType;

public class TimerIntermediateCatchEvent extends IntermediateCatchEvent {

	public enum Type {
		DURATION, CYCLE
	}
	
	public TimerIntermediateCatchEvent(String name) {
		super(name, ConstructType.TIMER_INTERMEDIATE_CATCH_EVENT);
	}
	
	public void setTimer(String value) {
		setTimer(value, Type.DURATION);
	}
	
	public void setTimer(String value, Type type) {
		properties.selectTab("Event");
		new DefaultTable().select(0);
		properties.toolbarButton("Event Definitions", "Edit").click();
		bot.textWithLabel("Script", type.ordinal()).setText(value);
		properties.toolbarButton("Timer Event Definition Details", "Close").click();
	}
	
}
