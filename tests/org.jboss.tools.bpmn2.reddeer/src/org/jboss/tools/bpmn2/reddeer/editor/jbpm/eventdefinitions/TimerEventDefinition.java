package org.jboss.tools.bpmn2.reddeer.editor.jbpm.eventdefinitions;

import org.jboss.reddeer.swt.impl.button.RadioButton;
import org.jboss.reddeer.swt.impl.text.LabeledText;
import org.jboss.tools.bpmn2.reddeer.editor.jbpm.AbstractEventDefinition;
import org.jboss.tools.reddeer.DefaultSection;

/**
 * 
 */
public class TimerEventDefinition extends AbstractEventDefinition {

	/**
	 * 
	 */
	public enum Type {
		CYCLE, DURATION;
		
		public String label() {
		    return name().charAt(0) + name().substring(1).toLowerCase();
		}
	}
	
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
		new DefaultSection("Timer Event Definition Details").getToolbarButton("Close").click();
	}
	
}
