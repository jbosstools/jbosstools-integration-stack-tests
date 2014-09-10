package org.jboss.tools.switchyard.reddeer.binding;

import org.eclipse.swt.SWT;
import org.jboss.reddeer.swt.impl.text.LabeledText;
import org.jboss.reddeer.swt.keyboard.KeyboardFactory;

/**
 * Scheduling binding page
 * 
 * @author apodhrad
 * 
 */
public class SchedulingBindingPage extends OperationOptionsPage<SchedulingBindingPage> {

	public static final String NAME = "Name*";
	public static final String CRON = "Cron*";
	public static final String START_TIME = "Start Time";
	public static final String END_TIME = "End Time";

	public SchedulingBindingPage setName(String name) {
		new LabeledText(NAME).setFocus();
		new LabeledText(NAME).setText(name);
		KeyboardFactory.getKeyboard().invokeKeyCombination(SWT.TAB);
		return this;
	}

	public String getName() {
		return new LabeledText(NAME).getText();
	}

	public SchedulingBindingPage setCron(String cron) {
		new LabeledText(CRON).setFocus();
		new LabeledText(CRON).setText(cron);
		KeyboardFactory.getKeyboard().invokeKeyCombination(SWT.TAB);
		return this;
	}

	public String getCron() {
		return new LabeledText(CRON).getText();
	}

	public SchedulingBindingPage setStartTime(String startTime) {
		new LabeledText(START_TIME).setFocus();
		new LabeledText(START_TIME).setText(startTime);
		KeyboardFactory.getKeyboard().invokeKeyCombination(SWT.TAB);
		return this;
	}

	public String getStartTime() {
		return new LabeledText(START_TIME).getText();
	}

	public SchedulingBindingPage setEndTime(String startTime) {
		new LabeledText(END_TIME).setFocus();
		new LabeledText(END_TIME).setText(startTime);
		KeyboardFactory.getKeyboard().invokeKeyCombination(SWT.TAB);
		return this;
	}

	public String getEndTime() {
		return new LabeledText(END_TIME).getText();
	}

}
