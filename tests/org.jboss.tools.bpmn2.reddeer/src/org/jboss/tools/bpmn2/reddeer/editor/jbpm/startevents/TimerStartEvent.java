package org.jboss.tools.bpmn2.reddeer.editor.jbpm.startevents;

import org.jboss.reddeer.swt.impl.button.RadioButton;
import org.jboss.reddeer.swt.impl.table.DefaultTable;
import org.jboss.reddeer.swt.impl.text.LabeledText;
import org.jboss.tools.bpmn2.reddeer.editor.ConstructType;

/**
 * 
 * @author Marek Baluch <mbaluch@redhat.com>
 */
public class TimerStartEvent extends StartEvent {

	public enum Type {
		DURATION, INTERVAL
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
		
		switch (type) {
			case INTERVAL:
				new RadioButton("Interval").click();
				break;
			case DURATION:
				new RadioButton("Duration").click();
				break;
			default:
				throw new UnsupportedOperationException();
		}
		
		new LabeledText("Value").setText(value);
		
		properties.toolbarButton("Timer Event Definition Details", "Close").click();
	}
	
}