package org.jboss.tools.bpmn2.reddeer.editor.jbpm.eventdefinitions;

import org.jboss.reddeer.swt.impl.button.RadioButton;
import org.jboss.reddeer.swt.impl.text.LabeledText;
import org.jboss.tools.bpmn2.reddeer.editor.jbpm.EventDefinition;
import org.jboss.tools.bpmn2.reddeer.editor.jbpm.TimerType;
import org.jboss.tools.reddeer.DefaultSection;

/**
 * 
 */
public class TimerEventDefinition extends EventDefinition {

	private String duration;
	private TimerType timerType;
	
	/**
	 * 
	 * @param duration
	 */
	public TimerEventDefinition(String duration, TimerType timerType) {
		this.duration = duration;
		this.timerType = timerType;
	}
	
	/**
	 * BZ-1085520
	 */
	@Override
	public void setUp() {
		new RadioButton(timerType.label()).click();
		new LabeledText("Value").setText(duration);
		new DefaultSection("Timer Event Definition Details").getToolbarButton("Close").click();
	}
	
}
