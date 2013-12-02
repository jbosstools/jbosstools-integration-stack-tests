package org.jboss.tools.bpmn2.reddeer.editor.jbpm.startevents;

import org.jboss.reddeer.swt.impl.table.DefaultTable;
import org.jboss.tools.bpmn2.reddeer.editor.ConstructType;

/**
 * 
 * @author Marek Baluch <mbaluch@redhat.com>
 */
public class TimerStartEvent extends StartEvent {

	public enum Type {
		DURATION, CYCLE
	}
	
	/**
	 * 
	 * @param name
	 */
	public TimerStartEvent(String name) {
		super(name, ConstructType.TIMER_START_EVENT);
	}

	public void setTimer(String value, Type type) {
		properties.selectTab("Event");
		new DefaultTable().select(0);
		properties.toolbarButton("Event Definitions", "Edit").click();
		bot.textWithLabel("Script", type.ordinal());
		properties.toolbarButton("Timer Event Definition Details", "Close").click();
	}
	
}