package org.jboss.tools.switchyard.reddeer.binding;

import org.jboss.reddeer.jface.wizard.WizardDialog;
import org.jboss.reddeer.core.condition.JobIsRunning;
import org.jboss.reddeer.core.condition.ShellWithTextIsAvailable;
import org.jboss.reddeer.swt.impl.button.PushButton;
import org.jboss.reddeer.swt.impl.combo.DefaultCombo;
import org.jboss.reddeer.swt.impl.group.DefaultGroup;
import org.jboss.reddeer.swt.impl.shell.DefaultShell;
import org.jboss.reddeer.swt.impl.text.DefaultText;
import org.jboss.reddeer.common.wait.AbstractWait;
import org.jboss.reddeer.common.wait.TimePeriod;
import org.jboss.reddeer.common.wait.WaitWhile;
import org.jboss.tools.switchyard.reddeer.widget.LabeledText;

public abstract class OperationOptionsPage<T> extends WizardDialog {

	public static final String NAME = "Name";
	public static final String OPERATION_NAME = "Operation Name";
	public static final String OPERATION_SELECTOR = "Operation Selector";
	public static final String XPATH = "XPath";
	public static final String REGEX = "Regex";
	public static final String JAVA_CLASS = "Java Class";

	@SuppressWarnings("unchecked")
	public T setName(String name) {
		new LabeledText(NAME).setFocus();
		new LabeledText(NAME).typeText(name);
		new LabeledText(NAME).setText(name);
		new LabeledText(NAME).setFocusOut();
		return (T) this;
	}

	public String getName() {
		return new LabeledText(NAME).getText();
	}

	@SuppressWarnings("unchecked")
	public T setOperationSelector(String selector, String value) {
		setOperationSelector(selector);
		setOperationValue(value);
		return (T) this;
	}
	
	@SuppressWarnings("unchecked")
	public T setOperationSelector(String selector) {
		new DefaultCombo(new DefaultGroup(OPERATION_SELECTOR), 0).setSelection(selector);
		return (T) this;
	}

	@SuppressWarnings("unchecked")
	public T setOperationValue(String value) {
		String selector = getOperationSelector();
		if (OPERATION_NAME.equals(selector)) {
			new DefaultCombo(new DefaultGroup(OPERATION_SELECTOR), 1).setSelection(value);
		} else {
			new DefaultText(new DefaultGroup(OPERATION_SELECTOR), 0).setText(value);
		}
		return (T) this;
	}

	public String getOperationSelector() {
		DefaultGroup group = new DefaultGroup(OPERATION_SELECTOR);
		return new DefaultCombo(group).getText();
	}

	public String getOperationSelectorValue() {
		DefaultGroup group = new DefaultGroup(OPERATION_SELECTOR);
		return new DefaultCombo(group, 1).getText();
	}

	@Override
	public void finish() {
		AbstractWait.sleep(TimePeriod.SHORT);
		super.finish();
	}
	
	public void ok() {
		AbstractWait.sleep(TimePeriod.SHORT);

		String shellText = new DefaultShell().getText();
		new PushButton("OK").click();

		new WaitWhile(new ShellWithTextIsAvailable(shellText), TimePeriod.LONG);
		new WaitWhile(new JobIsRunning(), TimePeriod.LONG);
	}

}
