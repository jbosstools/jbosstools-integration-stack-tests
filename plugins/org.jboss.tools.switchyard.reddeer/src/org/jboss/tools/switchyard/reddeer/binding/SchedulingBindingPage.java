package org.jboss.tools.switchyard.reddeer.binding;

import org.eclipse.reddeer.swt.impl.combo.LabeledCombo;
import org.eclipse.reddeer.swt.impl.text.LabeledText;

/**
 * Scheduling binding page
 * 
 * @author apodhrad
 * 
 */
public class SchedulingBindingPage extends OperationOptionsPage<SchedulingBindingPage> {

	public static final String SCHEDULING_TYPE_CRON = "cron";
	public static final String SCHEDULING_TYPE_TRIGGER = "trigger";

	public LabeledText getEndTime() {
		return new LabeledText("End Time");
	}

	public LabeledText getStartTime() {
		return new LabeledText("Start Time");
	}

	public LabeledText getRepeatInterval() {
		return new LabeledText("Repeat Interval");
	}

	public LabeledText getRepeatCount() {
		return new LabeledText("Repeat Count");
	}

	public LabeledText getCron() {
		return new LabeledText("Cron*");
	}

	public LabeledCombo getTimeZone() {
		return new LabeledCombo("Time Zone");
	}

	public LabeledCombo getSchedulingType() {
		return new LabeledCombo("Scheduling Type");
	}

}
