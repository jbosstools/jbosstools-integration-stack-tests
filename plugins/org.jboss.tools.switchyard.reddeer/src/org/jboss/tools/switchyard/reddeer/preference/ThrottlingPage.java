package org.jboss.tools.switchyard.reddeer.preference;

import org.jboss.reddeer.jface.preference.PreferencePage;
import org.jboss.reddeer.swt.impl.button.CheckBox;
import org.jboss.reddeer.swt.impl.text.LabeledText;

/**
 * Represents a properties page "Throttling".
 * 
 * @author apodhrad
 */
public class ThrottlingPage extends PreferencePage {

	public static final String ENABLE_THROTTLING = "Enable throttling";
	public static final String MAX_REQUESTS = "Maximum Requests:";
	public static final String TIME_PERIOD = "Time Period:";

	public boolean isThrottlingEnableChecked() {
		return new CheckBox(ENABLE_THROTTLING).isChecked();
	}

	public ThrottlingPage setThrottlingEnable(boolean checked) {
		new CheckBox(ENABLE_THROTTLING).toggle(checked);
		return this;
	}

	public ThrottlingPage setMaximumRequests(String maxRequests) {
		new LabeledText(MAX_REQUESTS).setText(maxRequests);
		return this;
	}

	public String getMaximumRequests() {
		return new LabeledText(MAX_REQUESTS).getText();
	}

	public boolean isMaximumRequestsEnabled() {
		return new LabeledText(MAX_REQUESTS).isEnabled();
	}

	public boolean isMaximumRequestsReadOnly() {
		return new LabeledText(MAX_REQUESTS).isReadOnly();
	}

	public ThrottlingPage setTimePeriod(String timePeriod) {
		new LabeledText(TIME_PERIOD).setText(timePeriod);
		return this;
	}

	public String getTimePeriod() {
		return new LabeledText(TIME_PERIOD).getText();
	}

	public boolean isTimePeriodEnabled() {
		return new LabeledText(TIME_PERIOD).isEnabled();
	}

	public boolean isTimePeriodReadOnly() {
		return new LabeledText(TIME_PERIOD).isReadOnly();
	}
}
