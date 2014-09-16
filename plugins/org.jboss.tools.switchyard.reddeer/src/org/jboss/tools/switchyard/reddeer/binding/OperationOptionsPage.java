package org.jboss.tools.switchyard.reddeer.binding;

import org.jboss.reddeer.jface.wizard.WizardDialog;
import org.jboss.reddeer.swt.exception.SWTLayerException;
import org.jboss.reddeer.swt.impl.combo.DefaultCombo;
import org.jboss.reddeer.swt.impl.group.DefaultGroup;
import org.jboss.reddeer.swt.impl.shell.DefaultShell;
import org.jboss.reddeer.swt.util.Display;
import org.jboss.reddeer.swt.util.ResultRunnable;
import org.jboss.reddeer.swt.wait.AbstractWait;
import org.jboss.reddeer.swt.wait.TimePeriod;
import org.jboss.tools.switchyard.reddeer.widget.LabeledText;

public abstract class OperationOptionsPage<T> extends WizardDialog {

	public static final String NAME = "Name";
	public static final String OPERATION_NAME = "Operation Name";
	public static final String OPERATION_SELECTOR = "Operation Selector";
	public static final String XPATH = "XPath";
	public static final String REGEX = "Regex";
	public static final String JAVA_CLASS = "Java Class";

	@SuppressWarnings("unchecked")
	public T activate() {
		new DefaultShell("");
		return (T) this;
	}

	@SuppressWarnings("unchecked")
	public T setName(String name) {
		activate();
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
		DefaultGroup group = new DefaultGroup(OPERATION_SELECTOR);
		new DefaultCombo(group).setSelection(selector);
		final org.eclipse.swt.widgets.Combo combo = new DefaultCombo(1).getSWTWidget();
		boolean hasFocus = Display.syncExec(new ResultRunnable<Boolean>() {
			@Override
			public Boolean run() {
				return combo.forceFocus();
			}
		});
		if (!hasFocus) {
			throw new SWTLayerException("Combo box doesn't have a focus");
		}
		;
		AbstractWait.sleep(TimePeriod.SHORT);
		new DefaultCombo(group, 1).setSelection(value);
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

}
