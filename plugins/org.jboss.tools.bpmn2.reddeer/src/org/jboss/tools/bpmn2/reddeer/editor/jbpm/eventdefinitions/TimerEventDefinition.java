package org.jboss.tools.bpmn2.reddeer.editor.jbpm.eventdefinitions;

import org.eclipse.reddeer.swt.impl.button.RadioButton;
import org.eclipse.reddeer.swt.impl.text.LabeledText;
import org.jboss.tools.bpmn2.reddeer.editor.jbpm.EventDefinition;
import org.jboss.tools.bpmn2.reddeer.editor.properties.SectionToolItem;

/**
 * 
 */
public class TimerEventDefinition extends EventDefinition {

	private String duration;

	/**
	 * 
	 * @param duration
	 */
	public TimerEventDefinition(String duration) {
		this.duration = duration;
	}

	/**
	 * BZ-1085520
	 */
	@Override
	public void setUp() {
		new RadioButton("Duration").click();
		new LabeledText("Value").setText(duration);
		new SectionToolItem("Timer Event Definition Details", "Close").click();
	}

}
