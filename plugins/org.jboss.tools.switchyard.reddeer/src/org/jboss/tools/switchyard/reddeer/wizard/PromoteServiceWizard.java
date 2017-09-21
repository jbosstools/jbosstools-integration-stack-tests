package org.jboss.tools.switchyard.reddeer.wizard;

import org.eclipse.reddeer.common.wait.TimePeriod;
import org.eclipse.reddeer.common.wait.WaitUntil;
import org.eclipse.reddeer.swt.condition.ControlIsEnabled;
import org.eclipse.reddeer.swt.impl.button.CheckBox;
import org.eclipse.reddeer.swt.impl.button.PushButton;
import org.eclipse.reddeer.swt.impl.combo.LabeledCombo;
import org.eclipse.reddeer.swt.impl.shell.DefaultShell;
import org.eclipse.reddeer.swt.impl.text.LabeledText;

/**
 * Wizard for promoting a service.
 * 
 * @author apodhrad
 * 
 */
public class PromoteServiceWizard extends ServiceWizard<PromoteServiceWizard> {

	public static final String DEFAULT_INTERFACE_TYPE = "Java";
	public static final String DEFAULT_TRANSFORMER_TYPE = "Java Transformer";

	public PromoteServiceWizard(String dialogTitle) {
		super(dialogTitle);
	}

	public PromoteServiceWizard setName(String name) {
		new LabeledText("Name:").setText(name);
		return this;
	}

	public PromoteServiceWizard setServiceName(String name) {
		new LabeledText("Service Name:").setText(name);
		return this;
	}

	public PromoteServiceWizard setTransformerType(String transformerType) {
		new DefaultShell("New Transformers");
		new LabeledCombo("Transformer Type:").setSelection(transformerType);
		return this;
	}

	public PromoteServiceWizard doNotCreateTransformers() {
		activate();
		CheckBox checkBox = getCreateRequiredTransformers();
		new WaitUntil(new ControlIsEnabled(checkBox), TimePeriod.LONG);
		checkBox.toggle(false);
		return this;
	}

	public CheckBox getCreateRequiredTransformers() {
		return new CheckBox("Create required transformers");
	}

	@Override
	protected void browse() {
		new PushButton("Browse...").click();
	}
}
